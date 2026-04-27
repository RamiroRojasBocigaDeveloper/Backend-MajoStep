package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.entity.*;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.VentaMapper;
import com.chancla.chancla_lite_auth.repository.*;
import com.chancla.chancla_lite_auth.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaMapper ventaMapper;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            DetalleVentaRepository detalleVentaRepository,
                            ProductoRepository productoRepository,
                            SesionTrabajoRepository sesionTrabajoRepository,
                            MetodoPagoRepository metodoPagoRepository,
                            UsuarioRepository usuarioRepository,
                            VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaMapper = ventaMapper;
    }
    @Override
    public VentaResponse procesarVenta(VentaRequest request) {
        // 1. Validar Sesión y Permisos
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        SesionTrabajoEntity sesion = null;

        // Lógica de atribución de usuario para Administradores
        if (isAdmin && request.getUsuarioId() != null) {
            // Intentar encontrar una sesión abierta para el usuario seleccionado
            sesion = sesionTrabajoRepository.findByUsuarioIdAndEstado(request.getUsuarioId(), EstadoSesion.ABIERTA)
                    .orElseGet(() -> {
                        // Si no hay sesión abierta, buscar la última sesión de ese usuario
                        List<SesionTrabajoEntity> sesiones = sesionTrabajoRepository.findByUsuarioId(request.getUsuarioId());
                        if (!sesiones.isEmpty()) {
                            return sesiones.get(sesiones.size() - 1);
                        }
                        
                        // Si el usuario no tiene ninguna sesión, crear una automática
                        UsuarioEntity usuario = usuarioRepository.findById(request.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado ID: " + request.getUsuarioId()));
                        
                        SesionTrabajoEntity nuevaSesion = new SesionTrabajoEntity();
                        nuevaSesion.setUsuario(usuario);
                        nuevaSesion.setEstado(EstadoSesion.CERRADA); // Se crea cerrada por ser histórica/automática
                        nuevaSesion.setRolUsuario(usuario.getRol().getNombre());
                        
                        // Establecer fecha/hora basada en la fecha histórica o actual
                        java.time.LocalDateTime fechaRef = (request.getFechaHistorica() != null) 
                            ? request.getFechaHistorica().atTime(9, 0) 
                            : java.time.LocalDateTime.now();
                            
                        nuevaSesion.setHoraInicio(fechaRef);
                        nuevaSesion.setHoraFin(fechaRef.plusHours(8));
                        
                        return sesionTrabajoRepository.save(nuevaSesion);
                    });
        } else {
            // Flujo normal por sesionId
            sesion = sesionTrabajoRepository.findById(request.getSesionId())
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));
        }

        // Regla: Bloquea si la sesión no está abierta (excepto para administradores)
        if (!isAdmin && sesion.getEstado() != EstadoSesion.ABIERTA) {
            throw new RuntimeException("La sesión no está abierta. No se puede procesar la venta.");
        }

        // 2. Validar Método de Pago
        MetodoPagoEntity metodoPago = metodoPagoRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado."));

        // 3. Crear cabecera de Venta
        VentaEntity venta = new VentaEntity();
        venta.setSesion(sesion);
        venta.setMetodoPago(metodoPago);
        venta.setNumeroFactura(generarNumeroFactura());
        venta.setDescuento(request.getDescuento());
        
        if (request.getFechaHistorica() != null) {
            // Validar que el usuario sea ADMIN para usar fecha histórica
            if (!isAdmin) {
                throw new RuntimeException("No tienes permisos para registrar ventas con fecha histórica. Solo administradores pueden realizar esta acción.");
            }
            venta.setFechaRegistroManual(request.getFechaHistorica().atTime(12, 0));
        } else {
            venta.setFechaRegistroManual(LocalDateTime.now());
        }
        
        Double subtotal = 0.0;
        List<DetalleVentaEntity> detalles = new ArrayList<>();

        // 4. Procesar Detalles y Stock
        for (VentaRequest.DetalleVentaRequest detReq : request.getDetalles()) {
            ProductoEntity producto = productoRepository.findById(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detReq.getProductoId()));

            if (producto.getStockActual() < detReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + 
                        " (Pedido: " + detReq.getCantidad() + ", Disponible: " + producto.getStockActual() + ")");
            }

            // El stock se descuenta automáticamente por el Trigger de la Base de Datos
            // trg_detalle_venta_ai -> inserta en movimientos_inventario -> trg_mov_inv_ai -> descuenta stock real.
            
            // Crear Detalle
            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detReq.getCantidad());
            
            if (detReq.getPrecioUnitario() != null) {
                detalle.setPrecioUnitario(detReq.getPrecioUnitario());
            } else {
                detalle.setPrecioUnitario(producto.getPrecioVenta());
            }
            
            detalle.setCostoUnitario(producto.getPrecioCompra());
            
            detalles.add(detalle);
            subtotal += (detalle.getPrecioUnitario() * detalle.getCantidad());

            // Nota: No es necesario crear MovimientoInventarioEntity aquí.
            // El trigger 'trg_detalle_venta_ai' de la base de datos lo generará automáticamente.
        }

        venta.setSubtotal(subtotal);
        venta.setTotal(subtotal - request.getDescuento());

        VentaEntity ventaGuardada = ventaRepository.save(venta);
        
        // Asignar referenciaId a los movimientos y guardar detalles
        for (DetalleVentaEntity d : detalles) {
            d.setVenta(ventaGuardada);
            detalleVentaRepository.save(d);
        }
        
        // Actualizar movimientos con el ID de la venta (opcional pero recomendado)
        // Por simplicidad, aquí los dejamos así o podrías buscarlos y asignarles el ID.

        return ventaMapper.toResponse(ventaGuardada, ventaMapper.toDetalleResponseList(detalles));
    }

    private String generarNumeroFactura() {
        LocalDateTime now = LocalDateTime.now();
        String prefix = "VEN-";
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefix + datePart + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id) {
        VentaEntity venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));
        List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(id);
        return ventaMapper.toResponse(venta, ventaMapper.toDetalleResponseList(detalles));
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorNumeroFactura(String numeroFactura) {
        VentaEntity venta = ventaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));
        List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(venta.getId());
        return ventaMapper.toResponse(venta, ventaMapper.toDetalleResponseList(detalles));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerTodas() {
        // Por eficiencia, en un entorno real usaríamos DTOs directos o proyecciones
        return ventaRepository.findAll().stream()
                .map(v -> obtenerPorId(v.getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorSesion(Long sesionId) {
        return ventaRepository.findBySesionId(sesionId).stream()
                .map(v -> obtenerPorId(v.getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorUsuario(Long usuarioId) {
        return ventaRepository.findBySesionUsuarioId(usuarioId).stream()
                .map(v -> obtenerPorId(v.getId()))
                .toList();
    }
}

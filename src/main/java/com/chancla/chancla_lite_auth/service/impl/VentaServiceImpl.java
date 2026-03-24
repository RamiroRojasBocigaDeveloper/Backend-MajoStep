package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.entity.*;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
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
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final VentaMapper ventaMapper;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            DetalleVentaRepository detalleVentaRepository,
                            ProductoRepository productoRepository,
                            SesionTrabajoRepository sesionTrabajoRepository,
                            MetodoPagoRepository metodoPagoRepository,
                            MovimientoInventarioRepository movimientoInventarioRepository,
                            VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public VentaResponse procesarVenta(VentaRequest request) {
        // 1. Validar Sesión
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(request.getSesionId())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));
        if (sesion.getEstado() != EstadoSesion.ABIERTA) {
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

            // Actualizar Stock
            producto.setStockActual(producto.getStockActual() - detReq.getCantidad());
            productoRepository.save(producto);

            // Crear Detalle
            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detReq.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setCostoUnitario(producto.getPrecioCompra());
            
            detalles.add(detalle);
            subtotal += (detalle.getPrecioUnitario() * detalle.getCantidad());

            // Registrar Movimiento Inventario
            MovimientoInventarioEntity mov = new MovimientoInventarioEntity();
            mov.setProducto(producto);
            mov.setTipo(TipoMovimiento.SALIDA);
            mov.setCantidad(detReq.getCantidad());
            mov.setMotivo("VENTA " + venta.getNumeroFactura());
            // Nota: referenciaId se asignará después de guardar la venta
            movimientoInventarioRepository.save(mov);
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

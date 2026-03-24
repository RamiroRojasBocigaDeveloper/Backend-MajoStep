package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.MovimientoInventarioMapper;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioMapper movimientoInventarioMapper;

    @Autowired
    public MovimientoInventarioServiceImpl(MovimientoInventarioRepository movimientoInventarioRepository,
                                           ProductoRepository productoRepository,
                                           MovimientoInventarioMapper movimientoInventarioMapper) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
        this.movimientoInventarioMapper = movimientoInventarioMapper;
    }

    @Override
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request) {
        ProductoEntity producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + request.getProductoId()));

        TipoMovimiento tipo = TipoMovimiento.valueOf(request.getTipo().toUpperCase());

        // Actualizar Stock del Producto
        int nuevoStock = producto.getStockActual();
        switch (tipo) {
            case ENTRADA:
                nuevoStock += request.getCantidad();
                break;
            case SALIDA:
                if (producto.getStockActual() < request.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para realizar salida manual.");
                }
                nuevoStock -= request.getCantidad();
                break;
            case AJUSTE:
                // El motivo debería indicar si es positivo o negativo
                // O podemos asumir que la cantidad ya es absoluta y el motivo lo explica
                // Por diseño simple: AJUSTE aquí sumará. Si quieren restar, deberían enviar SALIDA con motivo Ajuste.
                // O podemos permitir cantidad negativa en Ajuste si lo cambiamos en el DTO.
                // Usaremos: ENTRADA para sumar, SALIDA para restar. AJUSTE para corregir a un valor fijo? 
                // No, AJUSTE sumará por ahora.
                nuevoStock += request.getCantidad(); 
                break;
        }

        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);

        // Crear Movimiento
        MovimientoInventarioEntity movimento = movimientoInventarioMapper.toEntity(request);
        movimento.setProducto(producto);
        movimento.setTipo(tipo);

        MovimientoInventarioEntity guardado = movimientoInventarioRepository.save(movimento);
        return movimientoInventarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerHistorialPorProducto(Long productoId) {
        return movimientoInventarioMapper.toResponseList(movimientoInventarioRepository.findByProductoId(productoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerTodoElHistorial() {
        return movimientoInventarioMapper.toResponseList(movimientoInventarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoInventarioResponse obtenerPorId(Long id) {
        MovimientoInventarioEntity movimiento = movimientoInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento de inventario no encontrado."));
        return movimientoInventarioMapper.toResponse(movimiento);
    }
}

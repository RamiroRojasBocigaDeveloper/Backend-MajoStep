package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.service.ProductoService;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class InversionMercanciaServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ReporteService reporteService;

    @BeforeEach
    void autenticar() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test", null, List.of()));
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void crearProductoRegistraCompraYLosAumentosDeStockSoloValorizanLaDiferencia() {
        Integer categoriaId = categoriaRepository.findAll().get(0).getId();
        ProductoRequest request = productoRequest(categoriaId, 2, 10_000.0);

        ProductoResponse producto = productoService.crear(request);

        List<MovimientoInventarioEntity> movimientos = movimientosDe(producto.getId());
        assertEquals(1, movimientos.size());
        assertEquals(TipoMovimiento.ENTRADA, movimientos.get(0).getTipo());
        assertTrue(movimientos.get(0).getEsCompra());
        assertEquals(2, movimientos.get(0).getCantidad());
        assertEquals(10_000.0, movimientos.get(0).getCostoUnitario());

        // Aumenta el stock de 2 a 5 con un precio nuevo: solo las 3 unidades
        // nuevas se valorizan a 15.000.
        ProductoRequest aumento = productoRequest(categoriaId, 5, 15_000.0);
        aumento.setNombre(producto.getNombre());
        aumento.setReferencia(producto.getReferencia());
        productoService.actualizar(producto.getId(), aumento);

        movimientos = movimientosDe(producto.getId());
        assertEquals(2, movimientos.size());
        MovimientoInventarioEntity compraAumento = movimientos.get(1);
        assertEquals(TipoMovimiento.ENTRADA, compraAumento.getTipo());
        assertTrue(compraAumento.getEsCompra());
        assertEquals(3, compraAumento.getCantidad());
        assertEquals(15_000.0, compraAumento.getCostoUnitario());

        // Total: 2 x 10.000 + 3 x 15.000 = 65.000.
        EstadisticasInversionResponse inversion = reporteService.obtenerEstadisticasInversionGlobal();
        assertEquals(65_000.0, inversion.getInversionMercanciaPeriodo(), 0.01);

        // Disminuir el stock no resta inversiÃ³n (el dinero ya se gastÃ³).
        ProductoRequest disminucion = productoRequest(categoriaId, 3, 15_000.0);
        disminucion.setNombre(producto.getNombre());
        disminucion.setReferencia(producto.getReferencia());
        productoService.actualizar(producto.getId(), disminucion);

        movimientos = movimientosDe(producto.getId());
        assertEquals(3, movimientos.size());
        MovimientoInventarioEntity salida = movimientos.get(2);
        assertEquals(TipoMovimiento.SALIDA, salida.getTipo());
        assertFalse(salida.getEsCompra());

        EstadisticasInversionResponse inversionFinal = reporteService.obtenerEstadisticasInversionGlobal();
        assertEquals(65_000.0, inversionFinal.getInversionMercanciaPeriodo(), 0.01);
    }

    private List<MovimientoInventarioEntity> movimientosDe(Long productoId) {
        return movimientoInventarioRepository.findByProductoId(productoId).stream()
                .sorted(Comparator.comparing(MovimientoInventarioEntity::getId))
                .toList();
    }

    private ProductoRequest productoRequest(Integer categoriaId, Integer stock, Double precioCompra) {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto test inversion " + UUID.randomUUID());
        request.setReferencia("TEST-" + UUID.randomUUID());
        request.setCategoriaId(categoriaId);
        request.setPrecioCompra(precioCompra);
        request.setPrecioVenta(precioCompra * 2);
        request.setStockActual(stock);
        request.setStockMinimo(1);
        request.setActivo(true);
        return request;
    }
}


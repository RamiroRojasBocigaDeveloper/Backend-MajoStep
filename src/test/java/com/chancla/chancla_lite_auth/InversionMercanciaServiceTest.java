package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.CompraMercanciaRequest;
import com.chancla.chancla_lite_auth.dto.request.DetalleCompraRequest;
import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.enums.MetodoPagoCompra;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.service.CompraMercanciaService;
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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
class InversionMercanciaServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CompraMercanciaService compraMercanciaService;

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

    /**
     * El cargue inicial y las compras del módulo Compras deben sumar UNA sola vez
     * en la inversión en mercancía (antes se duplicaban porque ambos movimientos
     * se marcaban con esCompra = true y referenciaId = null).
     */
    @Test
    void cargueInicialYComprasCuentanUnaSolaVezEnLaInversion() {
        EstadisticasInversionResponse antes = reporteService.obtenerEstadisticasInversionGlobal();
        double inversionAntes = antes.getInversionMercanciaTotal();
        double entradasAntes = antes.getEntradasIniciales();
        double comprasAntes = antes.getComprasRegistradas();

        Integer categoriaId = categoriaRepository.findAll().get(0).getId();
        ProductoRequest request = productoRequest(categoriaId, 2, 10_000.0);

        ProductoResponse producto = productoService.crear(request);

        List<MovimientoInventarioEntity> movimientos = movimientosDe(producto.getId());
        assertEquals(1, movimientos.size());
        assertEquals(TipoMovimiento.ENTRADA, movimientos.get(0).getTipo());
        assertFalse(movimientos.get(0).getEsCompra(), "El cargue inicial no es una compra");
        assertEquals(2, movimientos.get(0).getCantidad());
        assertEquals(10_000.0, movimientos.get(0).getCostoUnitario());

        // Solo cargue inicial: 2 x 10.000 = 20.000 (una sola vez).
        EstadisticasInversionResponse trasInicial = reporteService.obtenerEstadisticasInversionGlobal();
        assertEquals(20_000.0, trasInicial.getInversionMercanciaTotal() - inversionAntes, 0.01);
        assertEquals(20_000.0, trasInicial.getEntradasIniciales() - entradasAntes, 0.01);
        assertEquals(0.0, trasInicial.getComprasRegistradas() - comprasAntes, 0.01);

        // Compra real por el módulo Compras: 3 unidades a 5.000 = 15.000.
        CompraMercanciaRequest compra = new CompraMercanciaRequest();
        compra.setFecha(LocalDate.now());
        compra.setMetodoPago(MetodoPagoCompra.EFECTIVO);
        DetalleCompraRequest linea = new DetalleCompraRequest();
        linea.setProductoId(producto.getId());
        linea.setCantidad(3);
        linea.setCostoUnitario(5_000.0);
        compra.setLineas(List.of(linea));
        compraMercanciaService.crear(compra);

        EstadisticasInversionResponse trasCompra = reporteService.obtenerEstadisticasInversionGlobal();
        assertEquals(15_000.0, trasCompra.getComprasRegistradas() - comprasAntes, 0.01);
        // La entrada de la compra NO debe sumarse como "entrada inicial".
        assertEquals(20_000.0, trasCompra.getEntradasIniciales() - entradasAntes, 0.01);
        // Total = cargue inicial + compra = 35.000 (no 50.000).
        assertEquals(35_000.0, trasCompra.getInversionMercanciaTotal() - inversionAntes, 0.01);
    }

    /**
     * El valor del inventario actual se calcula al costo de compra e incluye la
     * mercancía del cargue inicial. El valor potencial de venta es solo referencia.
     */
    @Test
    void valorInventarioSeCalculaAlCostoEIncluyeCargueInicial() {
        EstadisticasInversionResponse antes = reporteService.obtenerEstadisticasInversionGlobal();

        Integer categoriaId = categoriaRepository.findAll().get(0).getId();
        ProductoRequest request = productoRequest(categoriaId, 5, 1_000.0);
        request.setPrecioVenta(2_000.0);
        productoService.crear(request);

        EstadisticasInversionResponse despues = reporteService.obtenerEstadisticasInversionGlobal();
        // 5 unidades a costo 1.000 = 5.000
        assertEquals(5_000.0, despues.getValorInventarioActual() - antes.getValorInventarioActual(), 0.01);
        // 5 unidades a precio de venta 2.000 = 10.000
        assertEquals(10_000.0, despues.getValorInventarioVenta() - antes.getValorInventarioVenta(), 0.01);
        // Ganancia potencial = 5.000
        assertEquals(5_000.0, despues.getValorPotencialGanancia() - antes.getValorPotencialGanancia(), 0.01);
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

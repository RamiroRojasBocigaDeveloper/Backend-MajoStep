package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import com.chancla.chancla_lite_auth.repository.DetalleVentaRepository;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.SueldoPagadoRepository;
import com.chancla.chancla_lite_auth.repository.VentaRepository;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final SueldoPagadoRepository sueldoPagadoRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public ReporteServiceImpl(VentaRepository ventaRepository,
                              GastoRepository gastoRepository,
                              SueldoPagadoRepository sueldoPagadoRepository,
                              DetalleVentaRepository detalleVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.gastoRepository = gastoRepository;
        this.sueldoPagadoRepository = sueldoPagadoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public DashboardResponse obtenerResumenPorSesion(Long sesionId) {
        List<VentaEntity> ventas = ventaRepository.findBySesionId(sesionId);
        List<GastoEntity> gastos = gastoRepository.findBySesionId(sesionId);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findBySesionId(sesionId);

        return construirDashboard(ventas, gastos, sueldos);
    }

    @Override
    public DashboardResponse obtenerResumenPorRangoFechas(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<VentaEntity> ventas = ventaRepository.findByRangoFechas(inicioDateTime, finDateTime);
        List<GastoEntity> gastos = gastoRepository.findByCreatedAtBetween(inicioDateTime, finDateTime);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findByFechaPagoBetween(inicio, fin);

        return construirDashboard(ventas, gastos, sueldos);
    }

    @Override
    public DashboardResponse obtenerResumenGlobal() {
        return construirDashboard(ventaRepository.findAll(), gastoRepository.findAll(), sueldoPagadoRepository.findAll());
    }

    private DashboardResponse construirDashboard(List<VentaEntity> ventas, List<GastoEntity> gastos, List<SueldoPagadoEntity> sueldos) {
        DashboardResponse dashboard = new DashboardResponse();
        
        double totalVentas = ventas.stream().mapToDouble(VentaEntity::getTotal).sum();
        double totalGastos = gastos.stream().mapToDouble(GastoEntity::getMonto).sum();
        double totalSueldos = sueldos.stream().mapToDouble(SueldoPagadoEntity::getMonto).sum();

        // Cálculo de Ganancia Real basada en el Margen de los productos (PrecioVenta - PrecioCosto)
        double gananciaProductos = 0.0;
        double costoMercancia = 0.0;

        if (!ventas.isEmpty()) {
            gananciaProductos = detalleVentaRepository.sumMargenByVentas(ventas);
            costoMercancia = detalleVentaRepository.sumCostoByVentas(ventas);
        }

        dashboard.setTotalVentas(totalVentas);
        dashboard.setTotalGastos(totalGastos);
        dashboard.setTotalSueldos(totalSueldos);
        dashboard.setGananciaProductos(gananciaProductos);
        dashboard.setCostoMercancia(costoMercancia);
        
        // Flujo de Caja (Efectivo en Caja) = Ventas Totales - (Gastos Operativos + Sueldos)
        dashboard.setGananciaNeta(totalVentas - (totalGastos + totalSueldos));
        dashboard.setCantidadVentas((long) ventas.size());

        // Agrupación por método de pago
        Map<String, Double> ventasPorMetodo = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getMetodoPago().getNombre(),
                        Collectors.summingDouble(VentaEntity::getTotal)
                ));
        dashboard.setVentasPorMetodoPago(ventasPorMetodo);

        // Agrupación por Categoría y Productos más vendidos
        Map<String, Long> productosFavoritos = new HashMap<>();
        Map<String, Double> ventasPorCat = new HashMap<>();
        Map<String, Double> gananciasPorCat = new HashMap<>();

        for (VentaEntity v : ventas) {
            List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(v.getId());
            for (DetalleVentaEntity d : detalles) {
                // Productos Favoritos
                String nombreProd = d.getProducto().getNombre();
                productosFavoritos.put(nombreProd, productosFavoritos.getOrDefault(nombreProd, 0L) + d.getCantidad());

                // Ventas por Categoría
                String nombreCat = (d.getProducto().getCategoria() != null) ? d.getProducto().getCategoria().getNombre() : "Sin Categoría";
                double subtotal = d.getSubtotalItem() != null ? d.getSubtotalItem() : (d.getPrecioUnitario() * d.getCantidad());
                double margenDetalle = d.getMargenItem() != null ? d.getMargenItem() : ((d.getPrecioUnitario() - d.getCostoUnitario()) * d.getCantidad());

                ventasPorCat.put(nombreCat, ventasPorCat.getOrDefault(nombreCat, 0.0) + subtotal);
                gananciasPorCat.put(nombreCat, gananciasPorCat.getOrDefault(nombreCat, 0.0) + margenDetalle);
            }
        }
        
        dashboard.setProductosMasVendidos(productosFavoritos);
        dashboard.setVentasPorCategoria(ventasPorCat);
        dashboard.setGananciasPorCategoria(gananciasPorCat);

        // Gastos por Categoría
        Map<String, Double> gastosPorCat = gastos.stream()
                .collect(Collectors.groupingBy(
                        g -> (g.getCategoriaGasto() != null) ? g.getCategoriaGasto().getNombre() : "Sin Categoría",
                        Collectors.summingDouble(GastoEntity::getMonto)
                ));
        dashboard.setGastosPorCategoria(gastosPorCat);

        return dashboard;
    }
}

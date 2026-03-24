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
        // Nota: En una app real, usaríamos queries JPA que filtren por fecha (createdAt)
        // Aquí simulamos filtrando sobre todos los registros (no óptimo, pero funcional)
        List<VentaEntity> ventas = ventaRepository.findAll().stream()
                .filter(v -> (v.getCreatedAt().toLocalDate().isEqual(inicio) || v.getCreatedAt().toLocalDate().isAfter(inicio)) 
                          && (v.getCreatedAt().toLocalDate().isEqual(fin) || v.getCreatedAt().toLocalDate().isBefore(fin)))
                .toList();

        List<GastoEntity> gastos = gastoRepository.findAll().stream()
                .filter(g -> (g.getCreatedAt().toLocalDate().isEqual(inicio) || g.getCreatedAt().toLocalDate().isAfter(inicio)) 
                          && (g.getCreatedAt().toLocalDate().isEqual(fin) || g.getCreatedAt().toLocalDate().isBefore(fin)))
                .toList();

        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findAll().stream()
                .filter(s -> (s.getFechaPago().isEqual(inicio) || s.getFechaPago().isAfter(inicio)) 
                          && (s.getFechaPago().isEqual(fin) || s.getFechaPago().isBefore(fin)))
                .toList();

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

        dashboard.setTotalVentas(totalVentas);
        dashboard.setTotalGastos(totalGastos);
        dashboard.setTotalSueldos(totalSueldos);
        dashboard.setGananciaNeta(totalVentas - (totalGastos + totalSueldos));
        dashboard.setCantidadVentas((long) ventas.size());

        // Agrupación por método de pago
        Map<String, Double> ventasPorMetodo = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getMetodoPago().getNombre(),
                        Collectors.summingDouble(VentaEntity::getTotal)
                ));
        dashboard.setVentasPorMetodoPago(ventasPorMetodo);

        // Productos más vendidos (Mapeo de detales)
        Map<String, Long> productosFavoritos = new HashMap<>();
        for (VentaEntity v : ventas) {
            List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(v.getId());
            for (DetalleVentaEntity d : detalles) {
                String nombre = d.getProducto().getNombre();
                productosFavoritos.put(nombre, productosFavoritos.getOrDefault(nombre, 0L) + d.getCantidad());
            }
        }
        
        // Limitar a los 5 más vendidos (opcional)
        dashboard.setProductosMasVendidos(productosFavoritos);

        return dashboard;
    }
}

package com.chancla.chancla_lite_auth.dto.response;

import java.util.List;
import java.util.Map;

public class EstadisticasInversionResponse {

    private Double gastosOperativos;
    private Double inversionMercanciaPeriodo;
    private Double comprasRegistradas;
    private Double entradasIniciales;
    private Double inversionMercanciaTotal;
    private Double totalInvertidoPeriodo;
    private Double totalInvertidoAcumulado;
    private Double valorInventarioActual;
    private Double valorInventarioVenta;
    private Double valorPotencialGanancia;
    private Long unidadesInventarioActual;
    private Integer productosConStock;
    private Map<String, Double> gastosOperativosPorSubcategoria;
    private List<GastoResponse> gastosOperativosDetalle;
    private Map<String, Double> comprasPorProveedor;
    private List<InversionProductoDTO> entradasInicialesPorProducto;

    public EstadisticasInversionResponse() {
    }

    public Double getGastosOperativos() { return gastosOperativos; }
    public void setGastosOperativos(Double gastosOperativos) { this.gastosOperativos = gastosOperativos; }

    public Double getInversionMercanciaPeriodo() { return inversionMercanciaPeriodo; }
    public void setInversionMercanciaPeriodo(Double inversionMercanciaPeriodo) { this.inversionMercanciaPeriodo = inversionMercanciaPeriodo; }

    public Double getComprasRegistradas() { return comprasRegistradas; }
    public void setComprasRegistradas(Double comprasRegistradas) { this.comprasRegistradas = comprasRegistradas; }

    public Double getEntradasIniciales() { return entradasIniciales; }
    public void setEntradasIniciales(Double entradasIniciales) { this.entradasIniciales = entradasIniciales; }

    public Double getInversionMercanciaTotal() { return inversionMercanciaTotal; }
    public void setInversionMercanciaTotal(Double inversionMercanciaTotal) { this.inversionMercanciaTotal = inversionMercanciaTotal; }

    public Double getTotalInvertidoPeriodo() { return totalInvertidoPeriodo; }
    public void setTotalInvertidoPeriodo(Double totalInvertidoPeriodo) { this.totalInvertidoPeriodo = totalInvertidoPeriodo; }

    public Double getTotalInvertidoAcumulado() { return totalInvertidoAcumulado; }
    public void setTotalInvertidoAcumulado(Double totalInvertidoAcumulado) { this.totalInvertidoAcumulado = totalInvertidoAcumulado; }

    public Double getValorInventarioActual() { return valorInventarioActual; }
    public void setValorInventarioActual(Double valorInventarioActual) { this.valorInventarioActual = valorInventarioActual; }

    public Double getValorInventarioVenta() { return valorInventarioVenta; }
    public void setValorInventarioVenta(Double valorInventarioVenta) { this.valorInventarioVenta = valorInventarioVenta; }

    public Double getValorPotencialGanancia() { return valorPotencialGanancia; }
    public void setValorPotencialGanancia(Double valorPotencialGanancia) { this.valorPotencialGanancia = valorPotencialGanancia; }

    public Long getUnidadesInventarioActual() { return unidadesInventarioActual; }
    public void setUnidadesInventarioActual(Long unidadesInventarioActual) { this.unidadesInventarioActual = unidadesInventarioActual; }

    public Integer getProductosConStock() { return productosConStock; }
    public void setProductosConStock(Integer productosConStock) { this.productosConStock = productosConStock; }

    public Map<String, Double> getGastosOperativosPorSubcategoria() { return gastosOperativosPorSubcategoria; }
    public void setGastosOperativosPorSubcategoria(Map<String, Double> gastosOperativosPorSubcategoria) { this.gastosOperativosPorSubcategoria = gastosOperativosPorSubcategoria; }

    public List<GastoResponse> getGastosOperativosDetalle() { return gastosOperativosDetalle; }
    public void setGastosOperativosDetalle(List<GastoResponse> gastosOperativosDetalle) { this.gastosOperativosDetalle = gastosOperativosDetalle; }

    public Map<String, Double> getComprasPorProveedor() { return comprasPorProveedor; }
    public void setComprasPorProveedor(Map<String, Double> comprasPorProveedor) { this.comprasPorProveedor = comprasPorProveedor; }

    public List<InversionProductoDTO> getEntradasInicialesPorProducto() { return entradasInicialesPorProducto; }
    public void setEntradasInicialesPorProducto(List<InversionProductoDTO> entradasInicialesPorProducto) { this.entradasInicialesPorProducto = entradasInicialesPorProducto; }
}

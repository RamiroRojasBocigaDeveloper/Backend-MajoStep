package com.chancla.chancla_lite_auth.dto.response;

import java.util.Map;

public class DashboardResponse {

    private Double totalVentas;
    private Double totalGastos;
    private Double totalSueldos;
    private Double gananciaNeta;
    private Long cantidadVentas;
    private Map<String, Double> ventasPorMetodoPago;
    private Map<String, Long> productosMasVendidos;

    public DashboardResponse() {
    }

    // Getters y Setters
    public Double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(Double totalVentas) { this.totalVentas = totalVentas; }

    public Double getTotalGastos() { return totalGastos; }
    public void setTotalGastos(Double totalGastos) { this.totalGastos = totalGastos; }

    public Double getTotalSueldos() { return totalSueldos; }
    public void setTotalSueldos(Double totalSueldos) { this.totalSueldos = totalSueldos; }

    public Double getGananciaNeta() { return gananciaNeta; }
    public void setGananciaNeta(Double gananciaNeta) { this.gananciaNeta = gananciaNeta; }

    public Long getCantidadVentas() { return cantidadVentas; }
    public void setCantidadVentas(Long cantidadVentas) { this.cantidadVentas = cantidadVentas; }

    public Map<String, Double> getVentasPorMetodoPago() { return ventasPorMetodoPago; }
    public void setVentasPorMetodoPago(Map<String, Double> ventasPorMetodoPago) { this.ventasPorMetodoPago = ventasPorMetodoPago; }

    public Map<String, Long> getProductosMasVendidos() { return productosMasVendidos; }
    public void setProductosMasVendidos(Map<String, Long> productosMasVendidos) { this.productosMasVendidos = productosMasVendidos; }
}

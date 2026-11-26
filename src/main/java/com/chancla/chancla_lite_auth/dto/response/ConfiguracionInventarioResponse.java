package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConfiguracionInventarioResponse {

    private LocalDate fechaCorte;
    private Boolean cargaInicialCerrada;
    private String actualizadoPor;
    private LocalDateTime actualizadoEn;

    public ConfiguracionInventarioResponse() {
    }

    public ConfiguracionInventarioResponse(LocalDate fechaCorte, Boolean cargaInicialCerrada,
                                           String actualizadoPor, LocalDateTime actualizadoEn) {
        this.fechaCorte = fechaCorte;
        this.cargaInicialCerrada = cargaInicialCerrada;
        this.actualizadoPor = actualizadoPor;
        this.actualizadoEn = actualizadoEn;
    }

    public LocalDate getFechaCorte() { return fechaCorte; }
    public void setFechaCorte(LocalDate fechaCorte) { this.fechaCorte = fechaCorte; }

    public Boolean getCargaInicialCerrada() { return cargaInicialCerrada; }
    public void setCargaInicialCerrada(Boolean cargaInicialCerrada) { this.cargaInicialCerrada = cargaInicialCerrada; }

    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}

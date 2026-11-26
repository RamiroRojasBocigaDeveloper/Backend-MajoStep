package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CerrarCargaInicialRequest {

    @NotNull(message = "La fecha de corte es obligatoria")
    private LocalDate fechaCorte;

    public LocalDate getFechaCorte() { return fechaCorte; }
    public void setFechaCorte(LocalDate fechaCorte) { this.fechaCorte = fechaCorte; }
}

package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.response.ConfiguracionInventarioResponse;

import java.time.LocalDate;

public interface ConfiguracionInventarioService {

    ConfiguracionInventarioResponse obtener();

    boolean isCargaInicialCerrada();

    ConfiguracionInventarioResponse cerrarCargaInicial(LocalDate fechaCorte);

    ConfiguracionInventarioResponse reabrirCargaInicial();
}

package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.ConfiguracionInventarioResponse;
import com.chancla.chancla_lite_auth.entity.ConfiguracionInventarioEntity;
import com.chancla.chancla_lite_auth.repository.ConfiguracionInventarioRepository;
import com.chancla.chancla_lite_auth.service.ConfiguracionInventarioService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class ConfiguracionInventarioServiceImpl implements ConfiguracionInventarioService {

    private static final Long CONFIG_ID = 1L;

    private final ConfiguracionInventarioRepository configuracionInventarioRepository;

    public ConfiguracionInventarioServiceImpl(ConfiguracionInventarioRepository configuracionInventarioRepository) {
        this.configuracionInventarioRepository = configuracionInventarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ConfiguracionInventarioResponse obtener() {
        return configuracionInventarioRepository.findById(CONFIG_ID)
                .map(this::toResponse)
                .orElseGet(() -> new ConfiguracionInventarioResponse(null, false, null, null));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCargaInicialCerrada() {
        return configuracionInventarioRepository.findById(CONFIG_ID)
                .map(c -> Boolean.TRUE.equals(c.getCargaInicialCerrada()))
                .orElse(false);
    }

    @Override
    public ConfiguracionInventarioResponse cerrarCargaInicial(LocalDate fechaCorte) {
        if (fechaCorte == null) {
            throw new RuntimeException("La fecha de corte es obligatoria.");
        }
        ConfiguracionInventarioEntity config = obtenerEntidad();
        config.setFechaCorte(fechaCorte);
        config.setCargaInicialCerrada(true);
        config.setActualizadoPor(usuarioActual());
        config.setActualizadoEn(LocalDateTime.now());
        return toResponse(configuracionInventarioRepository.save(config));
    }

    @Override
    public ConfiguracionInventarioResponse reabrirCargaInicial() {
        ConfiguracionInventarioEntity config = obtenerEntidad();
        config.setCargaInicialCerrada(false);
        config.setActualizadoPor(usuarioActual());
        config.setActualizadoEn(LocalDateTime.now());
        return toResponse(configuracionInventarioRepository.save(config));
    }

    private ConfiguracionInventarioEntity obtenerEntidad() {
        return configuracionInventarioRepository.findById(CONFIG_ID).orElseGet(() -> {
            ConfiguracionInventarioEntity nueva = new ConfiguracionInventarioEntity();
            nueva.setId(CONFIG_ID);
            nueva.setCargaInicialCerrada(false);
            return nueva;
        });
    }

    private ConfiguracionInventarioResponse toResponse(ConfiguracionInventarioEntity config) {
        return new ConfiguracionInventarioResponse(
                config.getFechaCorte(),
                Boolean.TRUE.equals(config.getCargaInicialCerrada()),
                config.getActualizadoPor(),
                config.getActualizadoEn());
    }

    private String usuarioActual() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return null;
        }
    }
}

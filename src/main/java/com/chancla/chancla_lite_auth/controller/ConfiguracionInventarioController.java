package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.CerrarCargaInicialRequest;
import com.chancla.chancla_lite_auth.dto.response.ConfiguracionInventarioResponse;
import com.chancla.chancla_lite_auth.service.ConfiguracionInventarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion-inventario")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'JEFE')")
public class ConfiguracionInventarioController {

    private final ConfiguracionInventarioService configuracionInventarioService;

    public ConfiguracionInventarioController(ConfiguracionInventarioService configuracionInventarioService) {
        this.configuracionInventarioService = configuracionInventarioService;
    }

    @GetMapping
    public ResponseEntity<ConfiguracionInventarioResponse> obtener() {
        return ResponseEntity.ok(configuracionInventarioService.obtener());
    }

    @PostMapping("/cerrar-carga-inicial")
    public ResponseEntity<ConfiguracionInventarioResponse> cerrarCargaInicial(
            @Valid @RequestBody CerrarCargaInicialRequest request) {
        return ResponseEntity.ok(configuracionInventarioService.cerrarCargaInicial(request.getFechaCorte()));
    }

    @PostMapping("/reabrir-carga-inicial")
    public ResponseEntity<ConfiguracionInventarioResponse> reabrirCargaInicial() {
        return ResponseEntity.ok(configuracionInventarioService.reabrirCargaInicial());
    }
}

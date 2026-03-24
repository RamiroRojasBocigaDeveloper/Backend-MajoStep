package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.SesionTrabajoResponse;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.SesionTrabajoMapper;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.ProductoService;
import com.chancla.chancla_lite_auth.service.SesionTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SesionTrabajoServiceImpl implements SesionTrabajoService {

    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;
    private final SesionTrabajoMapper sesionTrabajoMapper;

    @Autowired
    public SesionTrabajoServiceImpl(SesionTrabajoRepository sesionTrabajoRepository,
                                    UsuarioRepository usuarioRepository,
                                    ProductoService productoService,
                                    SesionTrabajoMapper sesionTrabajoMapper) {
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoService = productoService;
        this.sesionTrabajoMapper = sesionTrabajoMapper;
    }

    @Override
    public SesionTrabajoResponse abrirSesion(Long usuarioId) {
        // Verificar si ya tiene una sesión abierta
        if (sesionTrabajoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoSesion.ABIERTA).isPresent()) {
            throw new RuntimeException("El usuario ya tiene una sesión abierta.");
        }

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        SesionTrabajoEntity nuevaSesion = new SesionTrabajoEntity();
        nuevaSesion.setUsuario(usuario);
        nuevaSesion.setHoraInicio(LocalDateTime.now());
        nuevaSesion.setEstado(EstadoSesion.ABIERTA);
        
        // Obtener el rol del usuario (ManyToOne)
        if (usuario.getRol() != null) {
            nuevaSesion.setRolUsuario(usuario.getRol().getNombre());
        }

        SesionTrabajoEntity guardada = sesionTrabajoRepository.save(nuevaSesion);
        return sesionTrabajoMapper.toResponse(guardada);
    }

    @Override
    public SesionTrabajoResponse cerrarSesion(Long sesionId) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        if (sesion.getEstado() != EstadoSesion.ABIERTA) {
            throw new RuntimeException("La sesión ya está cerrada o en un estado inválido.");
        }

        sesion.setHoraFin(LocalDateTime.now());
        sesion.setEstado(EstadoSesion.CERRADA);

        SesionTrabajoEntity actualizada = sesionTrabajoRepository.save(sesion);
        
        // Adjuntar alertas de stock bajo a la respuesta
        SesionTrabajoResponse response = sesionTrabajoMapper.toResponse(actualizada);
        response.setAlertasStock(productoService.obtenerStockBajo());
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SesionTrabajoResponse obtenerSesionActiva(Long usuarioId) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoSesion.ABIERTA)
                .orElseThrow(() -> new RuntimeException("No se encontró sesión activa para el usuario."));
        return sesionTrabajoMapper.toResponse(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionTrabajoResponse> obtenerHistorialUsuario(Long usuarioId) {
        return sesionTrabajoMapper.toResponseList(sesionTrabajoRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionTrabajoResponse> obtenerTodas() {
        return sesionTrabajoMapper.toResponseList(sesionTrabajoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SesionTrabajoResponse obtenerPorId(Long id) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));
        return sesionTrabajoMapper.toResponse(sesion);
    }
}

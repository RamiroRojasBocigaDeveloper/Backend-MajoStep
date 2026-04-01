package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.GastoRequest;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.GastoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaGastoRepository;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.SubcategoriaGastoRepository;
import com.chancla.chancla_lite_auth.entity.SubcategoriaGastoEntity;
import com.chancla.chancla_lite_auth.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GastoServiceImpl implements GastoService {

    private final GastoRepository gastoRepository;
    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final CategoriaGastoRepository categoriaGastoRepository;
    private final SubcategoriaGastoRepository subcategoriaGastoRepository;
    private final GastoMapper gastoMapper;

    @Autowired
    public GastoServiceImpl(GastoRepository gastoRepository,
                            SesionTrabajoRepository sesionTrabajoRepository,
                            CategoriaGastoRepository categoriaGastoRepository,
                            SubcategoriaGastoRepository subcategoriaGastoRepository,
                            GastoMapper gastoMapper) {
        this.gastoRepository = gastoRepository;
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.categoriaGastoRepository = categoriaGastoRepository;
        this.subcategoriaGastoRepository = subcategoriaGastoRepository;
        this.gastoMapper = gastoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoResponse> obtenerTodos() {
        return gastoMapper.toResponseList(gastoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoResponse> obtenerPorSesion(Long sesionId) {
        return gastoMapper.toResponseList(gastoRepository.findBySesionId(sesionId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoResponse> obtenerPorCategoria(Integer categoriaId) {
        return gastoMapper.toResponseList(gastoRepository.findByCategoriaGastoId(categoriaId));
    }

    @Override
    public GastoResponse crear(GastoRequest request) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(request.getSesionId())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        // Verifica rol ADMINISTRADOR para omitir bloqueo de sesión cerrada
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Regla: Bloquea cajeros si la sesión no está abierta
        if (!isAdmin && sesion.getEstado() != EstadoSesion.ABIERTA) {
            throw new RuntimeException("Solo un administrador puede registrar gastos en una sesión cerrada.");
        }

        CategoriaGastoEntity categoria = categoriaGastoRepository.findById(request.getCategoriaGastoId())
                .orElseThrow(() -> new RuntimeException("Categoría de gasto no encontrada."));

        SubcategoriaGastoEntity subcategoria = null;
        if (request.getSubcategoriaGastoId() != null) {
            subcategoria = subcategoriaGastoRepository.findById(request.getSubcategoriaGastoId())
                    .orElseThrow(() -> new RuntimeException("Subcategoría de gasto no encontrada."));
        }

        GastoEntity nuevoGasto = gastoMapper.toEntity(request);
        nuevoGasto.setSesion(sesion);
        nuevoGasto.setCategoriaGasto(categoria);
        if (subcategoria != null) {
            nuevoGasto.setSubcategoriaGasto(subcategoria);
        }

        if (request.getFechaHistorica() != null) {
            nuevoGasto.setFechaRegistroManual(request.getFechaHistorica().atTime(12, 0));
        } else {
            nuevoGasto.setFechaRegistroManual(java.time.LocalDateTime.now());
        }

        return gastoMapper.toResponse(gastoRepository.save(nuevoGasto));
    }

    @Override
    public GastoResponse actualizar(Long id, GastoRequest request) {
        GastoEntity existente = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado."));

        if (!existente.getSesion().getId().equals(request.getSesionId())) {
             SesionTrabajoEntity nuevaSesion = sesionTrabajoRepository.findById(request.getSesionId())
                    .orElseThrow(() -> new RuntimeException("Nueva sesión no encontrada."));
             existente.setSesion(nuevaSesion);
        }

        if (!existente.getCategoriaGasto().getId().equals(request.getCategoriaGastoId())) {
            CategoriaGastoEntity nuevaCategoria = categoriaGastoRepository.findById(request.getCategoriaGastoId())
                    .orElseThrow(() -> new RuntimeException("Nueva categoría no encontrada."));
            existente.setCategoriaGasto(nuevaCategoria);
        }

        if (request.getSubcategoriaGastoId() != null) {
            if (existente.getSubcategoriaGasto() == null || !existente.getSubcategoriaGasto().getId().equals(request.getSubcategoriaGastoId())) {
                SubcategoriaGastoEntity nuevaSubcategoria = subcategoriaGastoRepository.findById(request.getSubcategoriaGastoId())
                        .orElseThrow(() -> new RuntimeException("Nueva subcategoría no encontrada."));
                existente.setSubcategoriaGasto(nuevaSubcategoria);
            }
        } else {
            existente.setSubcategoriaGasto(null);
        }

        gastoMapper.updateEntityFromRequest(request, existente);
        return gastoMapper.toResponse(gastoRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new RuntimeException("Gasto no encontrado.");
        }
        gastoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GastoResponse obtenerPorId(Long id) {
        GastoEntity gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado."));
        return gastoMapper.toResponse(gasto);
    }
}

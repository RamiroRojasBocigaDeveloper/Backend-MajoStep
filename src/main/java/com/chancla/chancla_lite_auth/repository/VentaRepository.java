package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
    List<VentaEntity> findBySesionId(Long sesionId);
    Optional<VentaEntity> findByNumeroFactura(String numeroFactura);
    List<VentaEntity> findBySesionUsuarioId(Long usuarioId);
}

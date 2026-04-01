package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<GastoEntity, Long> {
    List<GastoEntity> findBySesionId(Long sesionId);
    List<GastoEntity> findByCategoriaGastoId(Integer categoriaGastoId);
    List<GastoEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

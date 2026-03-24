package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {
    List<DetalleVentaEntity> findByVentaId(Long ventaId);
    boolean existsByProductoId(Long productoId);
}

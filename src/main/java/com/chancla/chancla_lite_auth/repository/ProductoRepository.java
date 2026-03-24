package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    Optional<ProductoEntity> findByReferenciaIgnoreCase(String referencia);
    boolean existsByReferenciaIgnoreCase(String referencia);
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoEntity> findByCategoriaId(Integer categoriaId);
    boolean existsByCategoriaId(Integer categoriaId);
    
    // Buscar productos con stock igual o inferior al mínimo
    List<ProductoEntity> findByStockActualLessThanEqualAndActivoTrue(Integer stockMinimo);
}

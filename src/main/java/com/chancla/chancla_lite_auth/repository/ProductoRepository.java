package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    Optional<ProductoEntity> findByReferenciaIgnoreCase(String referencia);
    boolean existsByReferenciaIgnoreCase(String referencia);
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT p FROM ProductoEntity p WHERE LOWER(p.nombre) LIKE :termino OR LOWER(p.referencia) LIKE :termino")
    List<ProductoEntity> buscarDinamico(@Param("termino") String termino);

    List<ProductoEntity> findByCategoriaId(Integer categoriaId);
    boolean existsByCategoriaId(Integer categoriaId);
    
    // Buscar productos con stock igual o inferior al mínimo
    List<ProductoEntity> findByStockActualLessThanEqualAndActivoTrue(Integer stockMinimo);
}

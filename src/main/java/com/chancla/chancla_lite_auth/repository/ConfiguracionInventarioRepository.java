package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.ConfiguracionInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionInventarioRepository extends JpaRepository<ConfiguracionInventarioEntity, Long> {
}

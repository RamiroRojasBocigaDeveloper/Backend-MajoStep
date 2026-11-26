package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "configuracion_inventario")
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionInventarioEntity {

    @Id
    private Long id;

    @Column(name = "fecha_corte")
    private LocalDate fechaCorte;

    @Column(name = "carga_inicial_cerrada", nullable = false)
    private Boolean cargaInicialCerrada = false;

    @Column(name = "actualizado_por", length = 100)
    private String actualizadoPor;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

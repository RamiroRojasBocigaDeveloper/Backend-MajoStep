package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gastos")
@AllArgsConstructor
@NoArgsConstructor
public class GastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionTrabajoEntity sesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_gasto_id", nullable = false)
    private CategoriaGastoEntity categoriaGasto;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}

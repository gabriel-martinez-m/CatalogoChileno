package com.catalogochileno.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una Comuna perteneciente a una Región.
 */
@Entity
@Table(name = "comuna")
@Getter
@Setter
@NoArgsConstructor
public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;
}
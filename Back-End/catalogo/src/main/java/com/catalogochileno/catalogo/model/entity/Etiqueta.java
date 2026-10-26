package com.catalogochileno.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad para palabras clave/tags asociadas a los productos del catálogo.
 */
@Entity
@Table(name = "etiqueta")
@Getter
@Setter
@NoArgsConstructor
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String slug;
}
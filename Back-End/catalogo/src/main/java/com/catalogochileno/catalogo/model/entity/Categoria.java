package com.catalogochileno.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que clasifica la disciplina o categoría artística del producto.
 */
@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 500)
    private String descripcion;

    private Boolean activa = true;
}
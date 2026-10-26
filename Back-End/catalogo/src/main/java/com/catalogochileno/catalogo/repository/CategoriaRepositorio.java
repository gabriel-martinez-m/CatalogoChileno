package com.catalogochileno.catalogo.repository;

import com.catalogochileno.catalogo.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la consulta y administración de categorías.
 */
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findBySlug(String slug);
}
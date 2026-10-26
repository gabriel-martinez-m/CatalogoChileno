package com.catalogochileno.catalogo.repository;

import com.catalogochileno.catalogo.model.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Repositorio JPA para la búsqueda masiva de etiquetas.
 */
public interface EtiquetaRepositorio extends JpaRepository<Etiqueta, Long> {

    Set<Etiqueta> findByNombreIn(Set<String> nombres);
}
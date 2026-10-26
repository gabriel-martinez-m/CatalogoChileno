package com.catalogochileno.catalogo.repository;

import com.catalogochileno.catalogo.model.entity.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;

/**Para permitir que ProductoServicio busque la Región y Comuna asignada a un producto al crearlo: */
public interface ComunaRepositorio extends JpaRepository<Comuna, Long> {
}
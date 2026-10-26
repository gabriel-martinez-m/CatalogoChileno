package com.catalogochileno.catalogo.repository;

import com.catalogochileno.catalogo.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

/**Para permitir que ProductoServicio busque la Región y Comuna asignada a un producto al crearlo: */

public interface RegionRepositorio extends JpaRepository<Region, Long> {
}
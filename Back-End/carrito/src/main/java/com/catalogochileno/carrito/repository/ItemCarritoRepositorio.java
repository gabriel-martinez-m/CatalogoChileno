package com.catalogochileno.carrito.repository;

import com.catalogochileno.carrito.model.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad ItemCarrito.
 */
public interface ItemCarritoRepositorio extends JpaRepository<ItemCarrito, Long> {

    void deleteByCarritoIdCarritoAndIdProductoRef(Long idCarrito, Long idProducto);
}
package com.catalogochileno.carrito.repository;

import com.catalogochileno.carrito.model.entity.Carrito;
import com.catalogochileno.carrito.model.enums.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Carrito.
 */
public interface CarritoRepositorio extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByIdUsuarioRefAndEstado(Long idUsuario, EstadoCarrito estado);

    Optional<Carrito> findByTokenSesionInvitadoAndEstado(String token, EstadoCarrito estado);
}
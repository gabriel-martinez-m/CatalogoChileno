package com.catalogochileno.usuarios.repository;

import com.catalogochileno.usuarios.model.entity.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad UsuarioPerfil.
 * Proporciona métodos para consultar perfiles por RUT e idCredencialRef.
 */
public interface UsuarioPerfilRepositorio extends JpaRepository<UsuarioPerfil, Long> {

    /**
     * Busca un perfil de usuario por su RUT chileno.
     */
    Optional<UsuarioPerfil> findByRutChileno(String rut);

    /**
     * Busca un perfil de usuario usando la referencia de su credencial de autenticación.
     */
    Optional<UsuarioPerfil> findByIdCredencialRef(Long idCredencial);

    /**
     * Verifica si existe un perfil registrado con un RUT chileno específico.
     */
    Boolean existsByRutChileno(String rut);
}
package com.catalogochileno.usuarios.repository;

import com.catalogochileno.usuarios.model.entity.PerfilArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad PerfilArtista.
 * Maneja las consultas relativas a la información pública del artista.
 */
public interface PerfilArtistaRepositorio extends JpaRepository<PerfilArtista, Long> {

    /**
     * Busca el perfil de artista asociado a un ID de usuario específico.
     */
    Optional<PerfilArtista> findByUsuarioPerfilIdUsuario(Long idUsuario);
}
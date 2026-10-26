package com.catalogochileno.usuarios.repository;

import com.catalogochileno.usuarios.model.entity.DatosBancariosArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad DatosBancariosArtista.
 * Permite consultar la información bancaria registrada para liquidación de fondos.
 */
public interface DatosBancariosArtistaRepositorio extends JpaRepository<DatosBancariosArtista, Long> {

    /**
     * Busca los datos bancarios vinculados a un ID de usuario en particular.
     */
    Optional<DatosBancariosArtista> findByUsuarioPerfilIdUsuario(Long idUsuario);
}
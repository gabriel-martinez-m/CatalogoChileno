package com.catalogochileno.comisiones.repository;

import com.catalogochileno.comisiones.model.entity.LedgerArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LedgerRepositorio extends JpaRepository<LedgerArtista, Long> {
    Optional<LedgerArtista> findByIdUsuarioArtistaRef(Long idUsuarioArtistaRef);
}
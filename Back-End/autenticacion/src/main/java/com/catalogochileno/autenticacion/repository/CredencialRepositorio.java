package com.catalogochileno.autenticacion.repository;

import com.catalogochileno.autenticacion.model.entity.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredencialRepositorio extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByCorreoElectronico(String correoElectronico);

    Boolean existsByCorreoElectronico(String correoElectronico);
}
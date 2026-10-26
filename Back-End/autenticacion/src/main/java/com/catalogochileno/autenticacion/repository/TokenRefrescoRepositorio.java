package com.catalogochileno.autenticacion.repository;

import com.catalogochileno.autenticacion.model.entity.Credencial;
import com.catalogochileno.autenticacion.model.entity.TokenRefresco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRefrescoRepositorio extends JpaRepository<TokenRefresco, Long> {

    Optional<TokenRefresco> findByToken(String token);

    Integer deleteByCredencial(Credencial credencial);
}
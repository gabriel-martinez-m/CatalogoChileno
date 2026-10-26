package com.catalogochileno.autenticacion.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad para almacenar los Tokens de Refresco (Refresh Tokens) asociados a una credencial.
 */
@Entity
@Table(name = "token_refresco")
@Getter
@Setter
@NoArgsConstructor
public class TokenRefresco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credencial", nullable = false)
    private Credencial credencial;

    @Column(nullable = false)
    private Instant fechaExpiracion;

    private Boolean estaRevocado = false;

    // --- MÉTODOS DE NEGOCIO SEGÚN UML ---

    /**
     * Verifica si el token no está revocado y no ha superado su fecha de expiración.
     */
    public Boolean esValido() {
        return !this.estaRevocado && this.fechaExpiracion.isAfter(Instant.now());
    }

    /**
     * Revoca explícitamente el token de refresco (p. ej. al cerrar sesión).
     */
    public void revocar() {
        this.estaRevocado = true;
    }
}
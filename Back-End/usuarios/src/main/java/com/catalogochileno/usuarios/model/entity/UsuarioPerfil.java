package com.catalogochileno.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad principal que representa el perfil del usuario.
 * Mantiene la referencia hacia la credencial de autenticación y maneja
 * las relaciones 1 a 1 opcionales con el perfil de artista y datos bancarios.
 */
@Entity
@Table(name = "usuario_perfil")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioPerfil {

    // Identificador único del perfil de usuario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    // Referencia externa a la tabla de credenciales en servicio-autenticacion
    @Column(nullable = false, unique = true)
    private Long idCredencialRef;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String rutChileno;

    private String telefono;

    // Estado de verificación KYC (Verificación de identidad)
    private Boolean estaVerificado = false;

    // Fecha de creación del registro (no modificable)
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // Relación 1 a 1 opcional con PerfilArtista
    @OneToOne(mappedBy = "usuarioPerfil", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilArtista perfilArtista;

    // Relación 1 a 1 opcional con DatosBancariosArtista
    @OneToOne(mappedBy = "usuarioPerfil", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DatosBancariosArtista datosBancarios;

    /**
     * Lógica de negocio: Verifica si el perfil actual corresponde a un artista.
     */
    public Boolean esArtista() {
        return this.perfilArtista != null;
    }

    /**
     * Lógica de negocio: Cambia el estado del usuario a verificado tras revisar KYC.
     */
    public void verificarIdentidad() {
        this.estaVerificado = true;
    }
}
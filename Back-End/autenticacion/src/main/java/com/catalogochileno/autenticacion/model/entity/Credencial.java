package com.catalogochileno.autenticacion.model.entity;

import com.catalogochileno.autenticacion.model.enums.RolNombre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad principal que representa las credenciales de acceso de un usuario.
 */
@Entity
@Table(name = "credencial")
@Getter
@Setter
@NoArgsConstructor
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCredencial;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

    @Column(nullable = false)
    private String contrasenaEncriptada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolNombre rol;

    private Boolean estaActivo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime fechaUltimoAcceso;

    // --- GETTERS Y SETTERS EXPLÍCITOS (Compatibilidad total Java 21 / IDE) ---

    public Long getIdCredencial() {
        return idCredencial;
    }

    public void setIdCredencial(Long idCredencial) {
        this.idCredencial = idCredencial;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasenaEncriptada() {
        return contrasenaEncriptada;
    }

    public void setContrasenaEncriptada(String contrasenaEncriptada) {
        this.contrasenaEncriptada = contrasenaEncriptada;
    }

    public RolNombre getRol() {
        return rol;
    }

    public void setRol(RolNombre rol) {
        this.rol = rol;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    // --- MÉTODOS DE NEGOCIO SEGÚN UML ---

    /**
     * Actualiza la marca temporal del último inicio de sesión exitoso.
     */
    public void actualizarUltimoAcceso() {
        this.fechaUltimoAcceso = LocalDateTime.now();
    }

    /**
     * Desactiva la cuenta e impide futuros inicios de sesión.
     */
    public void desactivarCuenta() {
        this.estaActivo = false;
    }
}
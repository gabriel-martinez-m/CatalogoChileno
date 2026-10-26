package com.catalogochileno.notificaciones.model.entity;

import com.catalogochileno.notificaciones.model.enums.EstadoEnvio;
import com.catalogochileno.notificaciones.model.enums.TipoCorreo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_notificacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    @Column(nullable = false)
    private String correoDestinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCorreo tipo;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenidoHtml;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
    private String errorDetalle;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PENDIENTE;
        }
    }

    public void marcarEnviado() {
        this.estado = EstadoEnvio.ENVIADO;
        this.fechaEnvio = LocalDateTime.now();
        this.errorDetalle = null;
    }

    public void marcarFallido(String error) {
        this.estado = EstadoEnvio.FALLIDO;
        this.errorDetalle = error;
    }
}
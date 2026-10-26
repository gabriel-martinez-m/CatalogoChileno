package com.catalogochileno.comisiones.model.entity;

import com.catalogochileno.comisiones.model.enums.EstadoLiquidacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_liquidacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudLiquidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    @Column(nullable = false)
    private Long idUsuarioArtistaRef;

    @Column(nullable = false)
    private Integer montoSolicitadoClp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLiquidacion estado;

    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;

    @PrePersist
    public void prePersist() {
        this.fechaSolicitud = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoLiquidacion.PENDIENTE;
        }
    }

    public void aprobar() {
        this.estado = EstadoLiquidacion.APROBADA;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void rechazar() {
        this.estado = EstadoLiquidacion.RECHAZADA;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void marcarComoTransferida() {
        this.estado = EstadoLiquidacion.TRANSFERIDA;
        this.fechaResolucion = LocalDateTime.now();
    }
}
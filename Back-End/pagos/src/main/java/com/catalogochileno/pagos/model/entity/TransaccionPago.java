package com.catalogochileno.pagos.model.entity;

import com.catalogochileno.pagos.model.enums.EstadoTransaccion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    @Column(nullable = false)
    private Long idOrdenRef;

    private String tokenWs;

    @Column(nullable = false)
    private Integer montoTotalClp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado;

    private String codigoAutorizacion;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoTransaccion.INICIADA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void aprobarTransaccion(String codigoAuth) {
        this.estado = EstadoTransaccion.APROBADA;
        this.codigoAutorizacion = codigoAuth;
    }

    public void rechazarTransaccion() {
        this.estado = EstadoTransaccion.RECHAZADA;
    }
}
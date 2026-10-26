package com.catalogochileno.comisiones.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ledger_artista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLedger;

    @Column(nullable = false, unique = true)
    private Long idUsuarioArtistaRef;

    @Builder.Default
    private Integer saldoDisponibleClp = 0;

    @Builder.Default
    private Integer saldoRetenidoClp = 0;

    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "ledger", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Movimiento> movimientos = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void sumarCredito(Integer monto) {
        this.saldoDisponibleClp += monto;
    }

    public void retenerFondos(Integer monto) {
        if (this.saldoDisponibleClp < monto) {
            throw new IllegalArgumentException("Saldo insuficiente para retener");
        }
        this.saldoDisponibleClp -= monto;
        this.saldoRetenidoClp += monto;
    }

    public void liberarFondos(Integer monto) {
        if (this.saldoRetenidoClp < monto) {
            throw new IllegalArgumentException("El monto a liberar excede los fondos retenidos");
        }
        this.saldoRetenidoClp -= monto;
    }
}

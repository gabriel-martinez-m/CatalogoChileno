package com.catalogochileno.comisiones.model.entity;

import com.catalogochileno.comisiones.model.enums.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ledger", nullable = false)
    @JsonIgnore
    private LedgerArtista ledger;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer montoClp;

    private Long idOrdenRef;

    private LocalDateTime fechaMovimiento;

    @PrePersist
    public void prePersist() {
        this.fechaMovimiento = LocalDateTime.now();
    }
}
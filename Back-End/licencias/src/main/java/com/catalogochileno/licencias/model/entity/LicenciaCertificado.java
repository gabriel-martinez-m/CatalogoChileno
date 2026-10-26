package com.catalogochileno.licencias.model.entity;

import com.catalogochileno.licencias.model.enums.EstadoLicencia;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "licencias_certificados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenciaCertificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLicencia;

    @Column(nullable = false, unique = true)
    private String codigoUnicoQr;

    @Column(nullable = false)
    private Long idUsuarioCompradorRef;

    @Column(nullable = false)
    private Long idProductoRef;

    @Column(nullable = false)
    private Long idOrdenRef;

    private String urlPdfGenerado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLicencia estado;

    private LocalDateTime fechaEmision;

    @PrePersist
    public void prePersist() {
        this.fechaEmision = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoLicencia.ACTIVA;
        }
    }

    public void revocar() {
        this.estado = EstadoLicencia.REVOCADA;
    }

    public Boolean esValida() {
        return EstadoLicencia.ACTIVA.equals(this.estado);
    }
}
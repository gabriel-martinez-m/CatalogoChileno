package com.catalogochileno.licencias.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enlaces_descarga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnlaceDescarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_licencia", nullable = false)
    private LicenciaCertificado licencia;

    @Column(length = 1000, nullable = false)
    private String urlFirmadaAwsS3;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Builder.Default
    private Boolean estaUsado = false;

    public void marcarComoUsado() {
        this.estaUsado = true;
    }

    public Boolean estaExpirado() {
        return LocalDateTime.now().isAfter(this.fechaExpiracion);
    }
}

package com.catalogochileno.usuarios.model.entity;

import com.catalogochileno.usuarios.model.enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Entidad que contiene los datos bancarios del artista para la transferencia
 * de fondos y liquidación de ventas/subastas.
 */
@Entity
@Table(name = "datos_bancarios")
@Getter
@Setter
@NoArgsConstructor
public class DatosBancariosArtista {

    // Identificador único de los datos bancarios
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDatosBancarios;

    // Vinculación 1 a 1 con la entidad principal UsuarioPerfil
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioPerfil usuarioPerfil;

    @Column(nullable = false)
    private String bancoNombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(nullable = false)
    private String numeroCuenta;

    @Column(nullable = false)
    private String rutTitular;

    /**
     * Lógica de negocio: Actualiza la información bancaria del titular.
     */
    public void actualizarCuenta(String banco, TipoCuenta tipo, String numero, String rut) {
        this.bancoNombre = banco;
        this.tipoCuenta = tipo;
        this.numeroCuenta = numero;
        this.rutTitular = rut;
    }
}
package com.catalogochileno.catalogo.model.entity;

import com.catalogochileno.catalogo.model.enums.EstadoProductoEnum;
import com.catalogochileno.catalogo.model.enums.TipoMedioEnum;
import com.catalogochileno.catalogo.model.enums.TipoVentaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad principal que representa una obra o producto del catálogo.
 * Contiene métodos de negocio para cambios de estado de aprobación/rechazo/desactivación.
 */
@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idArtista;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMedioEnum tipoMedio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProductoEnum estado = EstadoProductoEnum.PENDIENTE_REVISION;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVentaEnum tipoVenta;

    private String urlPrevisualizacion;
    private String formato;
    private Integer ancho;
    private Integer alto;
    private Integer duracionSegundos;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comuna")
    private Comuna comuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "producto_etiqueta",
        joinColumns = @JoinColumn(name = "id_producto"),
        inverseJoinColumns = @JoinColumn(name = "id_etiqueta")
    )
    private Set<Etiqueta> etiquetas = new HashSet<>();

    @PreUpdate
    public void alActualizar() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // --- MÉTODOS DE NEGOCIO SEGÚN UML ---

    /**
     * Aprueba la publicación del producto en el catálogo público.
     */
    public void aprobar() {
        this.estado = EstadoProductoEnum.APROBADO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Rechaza la publicación del producto indicando un motivo.
     */
    public void rechazar(String motivo) {
        this.estado = EstadoProductoEnum.RECHAZADO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Desactiva el producto para removerlo de la oferta comercial.
     */
    public void desactivar() {
        this.estado = EstadoProductoEnum.INACTIVO;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
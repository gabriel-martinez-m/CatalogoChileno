package com.catalogochileno.carrito.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un producto individual agregado al carrito.
 */
@Entity
@Table(name = "item_carrito")
@Getter
@Setter
@NoArgsConstructor
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCarrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito", nullable = false)
    @JsonIgnoreProperties("items")
    private Carrito carrito;

    @Column(nullable = false)
    private Long idProductoRef;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer precioUnitarioClp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAgregado = LocalDateTime.now();

    // --- MÉTODOS DE NEGOCIO SEGÚN UML ---

    /**
     * Actualiza la cantidad demandada del ítem en el carrito.
     */
    public void actualizarCantidad(Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.cantidad = nuevaCantidad;
    }

    /**
     * Calcula el subtotal multiplicando el precio unitario por la cantidad.
     */
    public Integer calcularSubtotal() {
        return this.precioUnitarioClp * this.cantidad;
    }
}
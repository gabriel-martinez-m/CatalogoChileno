package com.catalogochileno.carrito.controller;

import com.catalogochileno.carrito.model.entity.Carrito;
import com.catalogochileno.carrito.service.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el carrito de compras.
 */
@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoControlador {

    private final CarritoServicio carritoServicio;

    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito(
            @RequestParam(required = false) Long idUsuarioRef,
            @RequestParam(required = false) String tokenInvitado) {
        
        Carrito carrito = carritoServicio.obtenerOCrearCarrito(idUsuarioRef, tokenInvitado);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/items")
    public ResponseEntity<Carrito> agregarItem(
            @RequestParam(required = false) Long idUsuarioRef,
            @RequestParam(required = false) String tokenInvitado,
            @RequestParam Long idProductoRef,
            @RequestParam Integer cantidad,
            @RequestParam Integer precioUnitarioClp) {

        Carrito carrito = carritoServicio.agregarItem(idUsuarioRef, tokenInvitado, idProductoRef, cantidad, precioUnitarioClp);
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/items/{idItem}")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Long idItem,
            @RequestParam Integer nuevaCantidad) {

        Carrito carrito = carritoServicio.actualizarCantidad(idItem, nuevaCantidad);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{idCarrito}/items/{idProductoRef}")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable Long idCarrito,
            @PathVariable Long idProductoRef) {

        Carrito carrito = carritoServicio.eliminarItem(idCarrito, idProductoRef);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/{idCarrito}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idCarrito) {
        carritoServicio.vaciarCarrito(idCarrito);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCarrito}/procesar")
    public ResponseEntity<Void> marcarComoProcesado(@PathVariable Long idCarrito) {
        carritoServicio.marcarComoProcesado(idCarrito);
        return ResponseEntity.noContent().build();
    }
}
package com.catalogochileno.catalogo.controller;

import com.catalogochileno.catalogo.model.entity.Producto;
import com.catalogochileno.catalogo.model.enums.EstadoProductoEnum;
import com.catalogochileno.catalogo.model.enums.TipoMedioEnum;
import com.catalogochileno.catalogo.model.enums.TipoVentaEnum;
import com.catalogochileno.catalogo.service.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Controlador REST para el catálogo de obras. Delega toda la lógica de negocio al servicio.
 */
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @RequestParam Long idArtista,
            @RequestParam String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam BigDecimal precio,
            @RequestParam TipoMedioEnum tipoMedio,
            @RequestParam TipoVentaEnum tipoVenta,
            @RequestParam Long idCategoria,
            @RequestParam(required = false) Long idRegion,
            @RequestParam(required = false) Long idComuna,
            @RequestParam(required = false) Set<Long> idsEtiquetas) {

        Producto producto = productoServicio.crearProducto(idArtista, titulo, descripcion, precio, tipoMedio, tipoVenta, idCategoria, idRegion, idComuna, idsEtiquetas);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoServicio.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarConFiltros(
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) TipoMedioEnum tipoMedio,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idRegion,
            @RequestParam(required = false) Long idComuna,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) EstadoProductoEnum estado) {

        List<Producto> productos = productoServicio.listarConFiltros(termino, tipoMedio, idCategoria, idRegion, idComuna, precioMin, precioMax, estado);
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Producto> aprobarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoServicio.aprobarProducto(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Producto> rechazarProducto(@PathVariable Long id, @RequestParam String motivo) {
        return ResponseEntity.ok(productoServicio.rechazarProducto(id, motivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
        productoServicio.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
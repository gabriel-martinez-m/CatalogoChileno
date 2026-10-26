package com.catalogochileno.catalogo.service;

import com.catalogochileno.catalogo.model.entity.*;
import com.catalogochileno.catalogo.model.enums.EstadoProductoEnum;
import com.catalogochileno.catalogo.model.enums.TipoMedioEnum;
import com.catalogochileno.catalogo.model.enums.TipoVentaEnum;
import com.catalogochileno.catalogo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Servicio principal de gestión del catálogo.
 * Encapsula la creación, búsqueda, aprobación y filtrado de obras.
 */
@Service
@RequiredArgsConstructor
public class ProductoServicio {

    private final ProductoRepositorio productoRepo;
    private final CategoriaRepositorio categoriaRepo;
    private final EtiquetaRepositorio etiquetaRepo;
    private final RegionRepositorio regionRepo;
    private final ComunaRepositorio comunaRepo;

    /**
     * Regla de Negocio: Crea un nuevo producto asociándolo a su categoría, ubicación y etiquetas.
     */
    @Transactional
    public Producto crearProducto(Long idArtista, String titulo, String descripcion, BigDecimal precio,
                                  TipoMedioEnum tipoMedio, TipoVentaEnum tipoVenta, Long idCategoria,
                                  Long idRegion, Long idComuna, Set<Long> idsEtiquetas) {

        Categoria categoria = categoriaRepo.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

        Producto producto = new Producto();
        producto.setIdArtista(idArtista);
        producto.setTitulo(titulo);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setTipoMedio(tipoMedio);
        producto.setTipoVenta(tipoVenta);
        producto.setCategoria(categoria);
        producto.setEstado(EstadoProductoEnum.PENDIENTE_REVISION);

        if (idRegion != null) {
            Region region = regionRepo.findById(idRegion).orElse(null);
            producto.setRegion(region);
        }

        if (idComuna != null) {
            Comuna comuna = comunaRepo.findById(idComuna).orElse(null);
            producto.setComuna(comuna);
        }

        if (idsEtiquetas != null && !idsEtiquetas.isEmpty()) {
            Set<Etiqueta> etiquetas = new HashSet<>(etiquetaRepo.findAllById(idsEtiquetas));
            producto.setEtiquetas(etiquetas);
        }

        return productoRepo.save(producto);
    }

    /**
     * Obtiene un producto por su ID.
     */
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Lista productos aplicando filtros opcionales.
     */
    @Transactional(readOnly = true)
    public List<Producto> listarConFiltros(String termino, TipoMedioEnum tipoMedio, Long idCategoria,
                                          Long idRegion, Long idComuna, BigDecimal precioMin,
                                          BigDecimal precioMax, EstadoProductoEnum estado) {
        return productoRepo.buscarConFiltros(termino, tipoMedio, idCategoria, idRegion, idComuna, precioMin, precioMax, estado);
    }

    /**
     * Regla de Negocio: Aprueba un producto pendiente para publicarlo en la tienda.
     */
    @Transactional
    public Producto aprobarProducto(Long id) {
        Producto producto = obtenerPorId(id);
        producto.aprobar();
        return productoRepo.save(producto);
    }

    /**
     * Regla de Negocio: Rechaza la publicación de un producto.
     */
    @Transactional
    public Producto rechazarProducto(Long id, String motivo) {
        Producto producto = obtenerPorId(id);
        producto.rechazar(motivo);
        return productoRepo.save(producto);
    }

    /**
     * Regla de Negocio: Desactiva un producto del catálogo.
     */
    @Transactional
    public void desactivarProducto(Long id) {
        Producto producto = obtenerPorId(id);
        producto.desactivar();
        productoRepo.save(producto);
    }
}
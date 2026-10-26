package com.catalogochileno.catalogo.repository;

import com.catalogochileno.catalogo.model.entity.Producto;
import com.catalogochileno.catalogo.model.enums.EstadoProductoEnum;
import com.catalogochileno.catalogo.model.enums.TipoMedioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para la gestión de productos y búsquedas con filtros dinámicos.
 */
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {

    List<Producto> findByEstado(EstadoProductoEnum estado);

    List<Producto> findByIdArtista(Long idArtista);

    /**
     * Consulta JPQL para filtrar productos dinámicamente por múltiples criterios opcionales.
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "(:termino IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))) AND " +
           "(:tipoMedio IS NULL OR p.tipoMedio = :tipoMedio) AND " +
           "(:idCategoria IS NULL OR p.categoria.id = :idCategoria) AND " +
           "(:idRegion IS NULL OR p.region.id = :idRegion) AND " +
           "(:idComuna IS NULL OR p.comuna.id = :idComuna) AND " +
           "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
           "(:estado IS NULL OR p.estado = :estado)")
    List<Producto> buscarConFiltros(
            @Param("termino") String termino,
            @Param("tipoMedio") TipoMedioEnum tipoMedio,
            @Param("idCategoria") Long idCategoria,
            @Param("idRegion") Long idRegion,
            @Param("idComuna") Long idComuna,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("estado") EstadoProductoEnum estado
    );
}
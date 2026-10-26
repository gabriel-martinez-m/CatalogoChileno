package com.catalogochileno.carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente HTTP Feign para la comunicación síncrona con servicio-catalogo.
 */
@FeignClient(name = "servicio-catalogo", url = "${app.catalogo-service.url:http://localhost:8091}")
public interface CatalogoClienteFeign {

    /**
     * Valida si un producto existe y se encuentra disponible para venta.
     */
    @GetMapping("/productos/{idProducto}/disponible")
    Boolean validarProductoParaCompra(@PathVariable("idProducto") Long idProducto);
}
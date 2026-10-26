package com.catalogochileno.licencias.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-catalogo", url = "${app.feign.catalogo-url:http://localhost:8081}")
public interface CatalogoClienteFeign {

    @GetMapping("/api/productos/{idProducto}")
    Object obtenerMetadatosLicencia(@PathVariable("idProducto") Long idProducto);
}
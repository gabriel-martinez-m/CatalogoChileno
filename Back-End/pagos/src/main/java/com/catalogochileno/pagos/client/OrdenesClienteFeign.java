package com.catalogochileno.pagos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ordenes", url = "${app.feign.ordenes-url:http://localhost:8084}")
public interface OrdenesClienteFeign {

    @GetMapping("/api/ordenes/{idOrden}")
    Object obtenerOrden(@PathVariable("idOrden") Long idOrden);
}
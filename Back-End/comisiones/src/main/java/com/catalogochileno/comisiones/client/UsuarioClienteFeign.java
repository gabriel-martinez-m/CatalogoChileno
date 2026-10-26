package com.catalogochileno.comisiones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios", url = "${app.feign.usuarios-url:http://localhost:8085}")
public interface UsuarioClienteFeign {

    @GetMapping("/api/usuarios/{idUsuario}/datos-bancarios")
    Object obtenerDatosBancarios(@PathVariable("idUsuario") Long idUsuario);
}
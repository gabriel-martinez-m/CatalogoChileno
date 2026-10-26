package com.catalogochileno.autenticacion.client;

import com.catalogochileno.autenticacion.model.enums.RolNombre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente Feign para notificar la creación de perfil inicial a servicio-usuario.
 */
@FeignClient(name = "servicio-usuario", url = "${app.usuario-service.url:http://localhost:8090}")
public interface UsuarioClienteFeign {

    @PostMapping("/usuarios/perfil-inicial")
    Void crearPerfilInicial(
            @RequestParam("idCredencialRef") Long idCredencialRef,
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("rutChileno") String rutChileno,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam("rol") RolNombre rol
    );
}
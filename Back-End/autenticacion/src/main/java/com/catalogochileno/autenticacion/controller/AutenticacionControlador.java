package com.catalogochileno.autenticacion.controller;

import com.catalogochileno.autenticacion.model.entity.Credencial;
import com.catalogochileno.autenticacion.model.enums.RolNombre;
import com.catalogochileno.autenticacion.service.AutenticacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones de Autenticación, Tokens JWT y Sesiones.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;

    @PostMapping("/registro")
    public ResponseEntity<Credencial> registrar(
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam RolNombre rol,
            @RequestParam String nombre,
            @RequestParam String rut,
            @RequestParam(required = false) String telefono) {

        Credencial credencial = autenticacionServicio.registrar(correo, contrasena, rol, nombre, rut, telefono);
        return ResponseEntity.status(HttpStatus.CREATED).body(credencial);
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(
            @RequestParam String correo,
            @RequestParam String contrasena) {

        String tokenAcceso = autenticacionServicio.autenticar(correo, contrasena);
        return ResponseEntity.ok(tokenAcceso);
    }

    @PostMapping("/refrescar")
    public ResponseEntity<String> refrescarToken(@RequestParam String tokenRefresco) {
        String nuevoToken = autenticacionServicio.refrescarToken(tokenRefresco);
        return ResponseEntity.ok(nuevoToken);
    }

    @GetMapping("/validar")
    public ResponseEntity<Boolean> validarToken(@RequestParam String token) {
        Boolean esValido = autenticacionServicio.validarToken(token);
        return ResponseEntity.ok(esValido);
    }

    @PostMapping("/logout/{idCredencial}")
    public ResponseEntity<Void> cerrarSesion(@PathVariable Long idCredencial) {
        autenticacionServicio.cerrarSesion(idCredencial);
        return ResponseEntity.noContent().build();
    }
}
package com.catalogochileno.usuarios.controller;

import com.catalogochileno.usuarios.model.entity.DatosBancariosArtista;
import com.catalogochileno.usuarios.model.entity.PerfilArtista;
import com.catalogochileno.usuarios.model.entity.UsuarioPerfil;
import com.catalogochileno.usuarios.model.enums.RolUsuario;
import com.catalogochileno.usuarios.model.enums.TipoCuenta;
import com.catalogochileno.usuarios.service.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de usuarios, perfiles de artista y datos bancarios.
 * No contiene lógica de negocio; delega las operaciones a UsuarioServicio.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    /**
     * Endpoint para crear el perfil inicial de un usuario.
     */
    @PostMapping("/perfil-inicial")
    public ResponseEntity<UsuarioPerfil> crearPerfilInicial(
            @RequestParam Long idCredencialRef,
            @RequestParam String nombreCompleto,
            @RequestParam String rutChileno,
            @RequestParam(required = false) String telefono,
            @RequestParam RolUsuario rol) {
        
        UsuarioPerfil perfil = usuarioServicio.crearPerfilInicial(idCredencialRef, nombreCompleto, rutChileno, telefono, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
    }

    /**
     * Endpoint para obtener el perfil de un usuario mediante su ID.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioPerfil> obtenerPorId(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioServicio.obtenerPorId(idUsuario));
    }

    /**
     * Endpoint para obtener el perfil mediante la referencia de su credencial de autenticación.
     */
    @GetMapping("/credencial/{idCredencialRef}")
    public ResponseEntity<UsuarioPerfil> obtenerPorCredencialRef(@PathVariable Long idCredencialRef) {
        return ResponseEntity.ok(usuarioServicio.obtenerPorCredencialRef(idCredencialRef));
    }

    /**
     * Endpoint para registrar o actualizar el perfil público de un artista.
     */
    @PostMapping("/{idUsuario}/artista")
    public ResponseEntity<PerfilArtista> registrarPerfilArtista(
            @PathVariable Long idUsuario,
            @RequestParam String nombreArtistico,
            @RequestParam String biografia,
            @RequestParam String fotoAnverso,
            @RequestParam String fotoReverso) {
        
        PerfilArtista perfilArtista = usuarioServicio.registrarPerfilArtista(idUsuario, nombreArtistico, biografia, fotoAnverso, fotoReverso);
        return ResponseEntity.ok(perfilArtista);
    }

    /**
     * Endpoint para registrar o actualizar la información de la cuenta bancaria del artista.
     */
    @PutMapping("/{idUsuario}/datos-bancarios")
    public ResponseEntity<DatosBancariosArtista> actualizarDatosBancarios(
            @PathVariable Long idUsuario,
            @RequestParam String bancoNombre,
            @RequestParam TipoCuenta tipoCuenta,
            @RequestParam String numeroCuenta,
            @RequestParam String rutTitular) {
        
        DatosBancariosArtista datos = usuarioServicio.actualizarDatosBancarios(idUsuario, bancoNombre, tipoCuenta, numeroCuenta, rutTitular);
        return ResponseEntity.ok(datos);
    }

    /**
     * Endpoint para validar el proceso de verificación KYC de identidad de un artista.
     */
    @PutMapping("/{idUsuario}/verificar")
    public ResponseEntity<Void> verificarIdentidadArtista(
            @PathVariable Long idUsuario,
            @RequestParam Boolean verificado) {
        
        usuarioServicio.verificarIdentidadArtista(idUsuario, verificado);
        return ResponseEntity.noContent().build();
    }
}
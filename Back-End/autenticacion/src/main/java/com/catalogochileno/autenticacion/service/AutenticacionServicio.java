package com.catalogochileno.autenticacion.service;

import com.catalogochileno.autenticacion.client.UsuarioClienteFeign;
import com.catalogochileno.autenticacion.model.entity.Credencial;
import com.catalogochileno.autenticacion.model.entity.TokenRefresco;
import com.catalogochileno.autenticacion.model.enums.RolNombre;
import com.catalogochileno.autenticacion.repository.CredencialRepositorio;
import com.catalogochileno.autenticacion.repository.TokenRefrescoRepositorio;
import com.catalogochileno.autenticacion.security.JwtProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Servicio de Autenticación. Maneja registro, login, generación de JWT y revocación de sesiones.
 */
@Service
@RequiredArgsConstructor
public class AutenticacionServicio {

    private final CredencialRepositorio credencialRepo;
    private final TokenRefrescoRepositorio tokenRefrescoRepo;
    private final JwtProveedor jwtProveedor;
    private final UsuarioClienteFeign usuarioCliente;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Regla de Negocio: Registra credenciales y crea el perfil correspondiente en servicio-usuario mediante Feign.
     */
    @Transactional
    public Credencial registrar(String correo, String contrasena, RolNombre rol, String nombre, String rut, String telefono) {
        if (credencialRepo.existsByCorreoElectronico(correo)) {
            throw new IllegalArgumentException("El correo electrónico ya se encuentra registrado.");
        }

        Credencial credencial = new Credencial();
        credencial.setCorreoElectronico(correo);
        credencial.setContrasenaEncriptada(passwordEncoder.encode(contrasena));
        credencial.setRol(rol);
        credencial.setEstaActivo(true);

        Credencial guardada = credencialRepo.save(credencial);

        // Notificar a servicio-usuario vía Feign
        usuarioCliente.crearPerfilInicial(guardada.getIdCredencial(), nombre, rut, telefono, rol);

        return guardada;
    }

    /**
     * Regla de Negocio: Autentica credenciales y emite un token JWT de acceso.
     */
    @Transactional
    public String autenticar(String correo, String contrasena) {
        Credencial credencial = credencialRepo.findByCorreoElectronico(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (Boolean.FALSE.equals(credencial.getEstaActivo())) {
            throw new IllegalStateException("La cuenta de usuario se encuentra desactivada.");
        }

        if (!passwordEncoder.matches(contrasena, credencial.getContrasenaEncriptada())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        credencial.actualizarUltimoAcceso();
        credencialRepo.save(credencial);

        // Generar token de refresco y persistir
        String refreshTokenStr = jwtProveedor.generarTokenRefresco(credencial);

        tokenRefrescoRepo.deleteByCredencial(credencial);

        TokenRefresco tokenRefresco = new TokenRefresco();
        tokenRefresco.setCredencial(credencial);
        tokenRefresco.setToken(refreshTokenStr);
        tokenRefresco.setFechaExpiracion(Instant.now().plus(1, ChronoUnit.DAYS));
        tokenRefresco.setEstaRevocado(false);
        tokenRefrescoRepo.save(tokenRefresco);

        return jwtProveedor.generarTokenAcceso(credencial);
    }

    /**
     * Regla de Negocio: Emite un nuevo token de acceso a partir de un token de refresco válido.
     */
    @Transactional
    public String refrescarToken(String tokenRefrescoStr) {
        TokenRefresco tokenRefresco = tokenRefrescoRepo.findByToken(tokenRefrescoStr)
                .orElseThrow(() -> new IllegalArgumentException("Token de refresco no encontrado."));

        if (!tokenRefresco.esValido()) {
            throw new IllegalStateException("El token de refresco ha expirado o ha sido revocado.");
        }

        return jwtProveedor.generarTokenAcceso(tokenRefresco.getCredencial());
    }

    /**
     * Valida si la firma y la vigencia de un token JWT son correctas.
     */
    @Transactional(readOnly = true)
    public Boolean validarToken(String token) {
        return jwtProveedor.validarToken(token);
    }

    /**
     * Invalida los tokens de refresco activos del usuario.
     */
    @Transactional
    public void cerrarSesion(Long idCredencial) {
        Credencial credencial = credencialRepo.findById(idCredencial)
                .orElseThrow(() -> new RuntimeException("Credencial no encontrada."));

        tokenRefrescoRepo.deleteByCredencial(credencial);
    }
}
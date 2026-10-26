package com.catalogochileno.usuarios.service;

import com.catalogochileno.usuarios.model.entity.DatosBancariosArtista;
import com.catalogochileno.usuarios.model.entity.PerfilArtista;
import com.catalogochileno.usuarios.model.entity.UsuarioPerfil;
import com.catalogochileno.usuarios.model.enums.RolUsuario;
import com.catalogochileno.usuarios.model.enums.TipoCuenta;
import com.catalogochileno.usuarios.repository.DatosBancariosArtistaRepositorio;
import com.catalogochileno.usuarios.repository.PerfilArtistaRepositorio;
import com.catalogochileno.usuarios.repository.UsuarioPerfilRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal del microservicio de usuarios.
 * Encapsula las reglas de negocio, la interacción con los repositorios
 * y no utiliza objetos DTO, operando directamente con las entidades.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServicio {

    private final UsuarioPerfilRepositorio usuarioRepo;
    private final PerfilArtistaRepositorio artistaRepo;
    private final DatosBancariosArtistaRepositorio bancoRepo;

    /**
     * Regla de Negocio: Crea el perfil inicial de un usuario tras registrar sus credenciales.
     * Valida que el RUT chileno no esté duplicado en la base de datos.
     */
    @Transactional
    public UsuarioPerfil crearPerfilInicial(Long idCredencialRef, String nombreCompleto, String rutChileno, String telefono, RolUsuario rol) {
        if (usuarioRepo.existsByRutChileno(rutChileno)) {
            throw new IllegalArgumentException("El RUT ingresado ya se encuentra registrado.");
        }

        UsuarioPerfil perfil = new UsuarioPerfil();
        perfil.setIdCredencialRef(idCredencialRef);
        perfil.setNombreCompleto(nombreCompleto);
        perfil.setRutChileno(rutChileno);
        perfil.setTelefono(telefono);
        
        return usuarioRepo.save(perfil);
    }

    /**
     * Obtiene el perfil de un usuario mediante su identificador único.
     */
    @Transactional(readOnly = true)
    public UsuarioPerfil obtenerPorId(Long idUsuario) {
        return usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
    }

    /**
     * Obtiene el perfil de un usuario utilizando la referencia de su credencial de autenticación.
     */
    @Transactional(readOnly = true)
    public UsuarioPerfil obtenerPorCredencialRef(Long idCredencialRef) {
        return usuarioRepo.findByIdCredencialRef(idCredencialRef)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para la credencial ID: " + idCredencialRef));
    }

    /**
     * Regla de Negocio: Vincula o actualiza el perfil de artista de un usuario.
     */
    @Transactional
    public PerfilArtista registrarPerfilArtista(Long idUsuario, String nombreArtistico, String biografia, String fotoAnverso, String fotoReverso) {
        UsuarioPerfil usuario = obtenerPorId(idUsuario);

        PerfilArtista perfilArtista = artistaRepo.findByUsuarioPerfilIdUsuario(idUsuario)
                .orElseGet(() -> {
                    PerfilArtista nuevo = new PerfilArtista();
                    nuevo.setUsuarioPerfil(usuario);
                    return nuevo;
                });

        perfilArtista.actualizarPerfil(nombreArtistico, biografia, fotoAnverso, fotoReverso);
        usuario.setPerfilArtista(perfilArtista);

        return artistaRepo.save(perfilArtista);
    }

    /**
     * Regla de Negocio: Registra o actualiza la información bancaria vinculada al perfil del artista.
     */
    @Transactional
    public DatosBancariosArtista actualizarDatosBancarios(Long idUsuario, String bancoNombre, TipoCuenta tipoCuenta, String numeroCuenta, String rutTitular) {
        UsuarioPerfil usuario = obtenerPorId(idUsuario);

        DatosBancariosArtista datosBancarios = bancoRepo.findByUsuarioPerfilIdUsuario(idUsuario)
                .orElseGet(() -> {
                    DatosBancariosArtista nuevo = new DatosBancariosArtista();
                    nuevo.setUsuarioPerfil(usuario);
                    return nuevo;
                });

        datosBancarios.actualizarCuenta(bancoNombre, tipoCuenta, numeroCuenta, rutTitular);
        usuario.setDatosBancarios(datosBancarios);

        return bancoRepo.save(datosBancarios);
    }

    /**
     * Regla de Negocio: Actualiza el estado de verificación KYC del usuario tras validar sus documentos.
     */
    @Transactional
    public void verificarIdentidadArtista(Long idUsuario, Boolean verificado) {
        UsuarioPerfil usuario = obtenerPorId(idUsuario);
        if (Boolean.TRUE.equals(verificado)) {
            usuario.verificarIdentidad();
            usuarioRepo.save(usuario);
        }
    }
}
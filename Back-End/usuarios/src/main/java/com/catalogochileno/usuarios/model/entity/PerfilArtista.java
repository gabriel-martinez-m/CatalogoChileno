package com.catalogochileno.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Entidad que almacena la información pública y técnica de un artista,
 * incluyendo las rutas de las fotos del carnet para su validación.
 */
@Entity
@Table(name = "perfil_artista")
@Getter
@Setter
@NoArgsConstructor
public class PerfilArtista {

    // Identificador único del perfil del artista
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfilArtista;

    // Vinculación 1 a 1 con la entidad principal UsuarioPerfil
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioPerfil usuarioPerfil;

    @Column(nullable = false)
    private String nombreArtistico;

    @Column(length = 1000)
    private String biografia;

    // Rutas relativas del almacenamiento de archivos de carnet de identidad
    private String fotoCarnetAnversoRuta;
    private String fotoCarnetReversoRuta;

    /**
     * Lógica de negocio: Actualiza la información del perfil del artista.
     */
    public void actualizarPerfil(String nombre, String biografia, String fotoAnverso, String fotoReverso) {
        this.nombreArtistico = nombre;
        this.biografia = biografia;
        this.fotoCarnetAnversoRuta = fotoAnverso;
        this.fotoCarnetReversoRuta = fotoReverso;
    }
}
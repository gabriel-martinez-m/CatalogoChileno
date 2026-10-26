classDiagram
    direction TB

    %% ENUMS
    class TipoCuenta {
        <<enum>>
        CORRIENTE
        VISTA
        RUT
        AHORRO
    }

    class RolUsuario {
        <<enum>>
        ROLE_CLIENTE
        ROLE_ARTISTA
        ROLE_ADMINISTRADOR
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_usuarios)
    class UsuarioPerfil {
        <<Entity>>
        -Long idUsuario
        -Long idCredencialRef
        -String nombreCompleto
        -String rutChileno
        -String telefono
        -Boolean estaVerificado
        -LocalDateTime fechaRegistro
        -PerfilArtista perfilArtista
        -DatosBancariosArtista datosBancarios
        +esArtista() Boolean
        +verificarIdentidad() Void
    }

    class PerfilArtista {
        <<Entity>>
        -Long idPerfilArtista
        -UsuarioPerfil usuarioPerfil
        -String nombreArtistico
        -String biografia
        -String fotoCarnetAnversoRuta
        -String fotoCarnetReversoRuta
        +actualizarPerfil(nombre, biografia, fotoAnverso, fotoReverso) Void
    }

    class DatosBancariosArtista {
        <<Entity>>
        -Long idDatosBancarios
        -UsuarioPerfil usuarioPerfil
        -String bancoNombre
        -TipoCuenta tipoCuenta
        -String numeroCuenta
        -String rutTitular
        +actualizarCuenta(banco, tipo, numero, rut) Void
    }

    %% REPOSITORIOS (Spring Data JPA)
    class UsuarioPerfilRepositorio {
        <<interface>>
        +findByRutChileno(String rut) Optional~UsuarioPerfil~
        +findByIdCredencialRef(Long idCredencial) Optional~UsuarioPerfil~
        +existsByRutChileno(String rut) Boolean
    }

    class PerfilArtistaRepositorio {
        <<interface>>
        +findByUsuarioPerfilIdUsuario(Long idUsuario) Optional~PerfilArtista~
    }

    class DatosBancariosArtistaRepositorio {
        <<interface>>
        +findByUsuarioPerfilIdUsuario(Long idUsuario) Optional~DatosBancariosArtista~
    }

    %% MODELO / SERVICIO PRINCIPAL (Contiene la lógica y métodos)
    class UsuarioServicio {
        <<Service>>
        -UsuarioPerfilRepositorio usuarioRepo
        -PerfilArtistaRepositorio artistaRepo
        -DatosBancariosArtistaRepositorio bancoRepo
        +crearPerfilInicial(Long idCredencialRef, String nombreCompleto, String rutChileno, String telefono, RolUsuario rol) UsuarioPerfil
        +obtenerPorId(Long idUsuario) UsuarioPerfil
        +obtenerPorCredencialRef(Long idCredencialRef) UsuarioPerfil
        +registrarPerfilArtista(Long idUsuario, String nombreArtistico, String biografia, String fotoAnverso, String fotoReverso) PerfilArtista
        +actualizarDatosBancarios(Long idUsuario, String bancoNombre, TipoCuenta tipoCuenta, String numeroCuenta, String rutTitular) DatosBancariosArtista
        +verificarIdentidadArtista(Long idUsuario, Boolean verificado) Void
    }

    %% CONTROLADOR (Sin métodos de negocio, solo referencia)
    class UsuarioControlador {
        <<Controller>>
        -UsuarioServicio usuarioServicio
    }

    %% RELACIONES
    UsuarioPerfil "1" <--> "0..1" PerfilArtista : posee
    UsuarioPerfil "1" <--> "0..1" DatosBancariosArtista : posee
    DatosBancariosArtista --> TipoCuenta : usa
    UsuarioPerfil --> RolUsuario : usa

    UsuarioServicio --> UsuarioPerfil : gestiona
    UsuarioServicio --> PerfilArtista : gestiona
    UsuarioServicio --> DatosBancariosArtista : gestiona
    UsuarioServicio --> UsuarioPerfilRepositorio : conecta
    UsuarioServicio --> PerfilArtistaRepositorio : conecta
    UsuarioServicio --> DatosBancariosArtistaRepositorio : conecta

    UsuarioControlador --> UsuarioServicio : delega
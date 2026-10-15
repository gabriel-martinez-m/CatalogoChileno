classDiagram
    direction TB

    %% ENUMS
    class RolNombre {
        <<enum>>
        ROLE_CLIENTE
        ROLE_ARTISTA
        ROLE_ADMINISTRADOR
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_autenticacion)
    class Credencial {
        <<Entity>>
        -Long idCredencial
        -String correoElectronico
        -String contrasenaEncriptada
        -RolNombre rol
        -Boolean estaActivo
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaUltimoAcceso
        +registrar(correo, contrasena, rol) Credencial
        +autenticar(contrasena) Boolean
        +actualizarUltimoAcceso() Void
        +desactivarCuenta() Void
    }

    class TokenRefresco {
        <<Entity>>
        -Long idToken
        -String token
        -Credencial credencial
        -Instant fechaExpiracion
        -Boolean estaRevocado
        +esValido() Boolean
        +revocar() Void
    }

    %% COMPONENTES Y UTILS DE SEGURIDAD
    class JwtProveedor {
        <<Component>>
        -String claveSecreta
        -Long expiracionTokenAccesoMs
        -Long expiracionTokenRefrescoMs
        +generarTokenAcceso(Credencial credencial) String
        +generarTokenRefresco(Credencial credencial) String
        +obtenerCorreoDesdeToken(String token) String
        +validarToken(String token) Boolean
    }

    %% REPOSITORIOS (Spring Data JPA)
    class CredencialRepositorio {
        <<interface>>
        +findByCorreoElectronico(String correo) Optional~Credencial~
        +existsByCorreoElectronico(String correo) Boolean
    }

    class TokenRefrescoRepositorio {
        <<interface>>
        +findByToken(String token) Optional~TokenRefresco~
        +deleteByCredencial(Credencial credencial) Int
    }

    %% CLIENTE FEIGN (Comunicación con servicio-usuario)
    class UsuarioClienteFeign {
        <<interface>>
        +crearPerfilInicial(Long idCredencialRef, String nombre, String rut, String telefono, RolNombre rol) Void
    }

    %% MODELO / SERVICIO PRINCIPAL (Contiene la lógica y métodos)
    class AutenticacionServicio {
        <<Service>>
        -CredencialRepositorio credencialRepo
        -TokenRefrescoRepositorio tokenRefrescoRepo
        -JwtProveedor jwtProveedor
        -UsuarioClienteFeign usuarioCliente
        +registrar(String correo, String contrasena, RolNombre rol, String nombre, String rut, String telefono) Credencial
        +autenticar(String correo, String contrasena) String
        +refrescarToken(String tokenRefresco) String
        +validarToken(String token) Boolean
        +cerrarSesion(Long idCredencial) Void
    }

    %% CONTROLADOR (Sin métodos de negocio, solo referencia)
    class AutenticacionControlador {
        <<Controller>>
        -AutenticacionServicio autenticacionServicio
    }

    %% RELACIONES
    Credencial "1" <-- "0..*" TokenRefresco : posee
    Credencial --> RolNombre : asigna

    AutenticacionServicio --> Credencial : gestiona
    AutenticacionServicio --> CredencialRepositorio : conecta
    AutenticacionServicio --> TokenRefrescoRepositorio : conecta
    AutenticacionServicio --> JwtProveedor : usa
    AutenticacionServicio --> UsuarioClienteFeign : notifica

    AutenticacionControlador --> AutenticacionServicio : delega
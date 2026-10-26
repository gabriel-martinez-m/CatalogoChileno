# Servicio de Usuarios (`servicio-usuario`)

Microservicio encargado de la gestión integral de perfiles de usuario, perfiles de artista y datos bancarios para liquidación de fondos dentro del ecosistema **CatalogoChileno**.

## Estructura del Proyecto

```text
com.catalogochileno.usuarios
├── controller
│   └── UsuarioControlador.java
├── model
│   ├── entity
│   │   ├── UsuarioPerfil.java
│   │   ├── PerfilArtista.java
│   │   └── DatosBancariosArtista.java
│   └── enums
│       ├── RolUsuario.java
│       └── TipoCuenta.java
├── repository
│   ├── UsuarioPerfilRepositorio.java
│   ├── PerfilArtistaRepositorio.java
│   └── DatosBancariosArtistaRepositorio.java
└── service
    └── UsuarioServicio.java
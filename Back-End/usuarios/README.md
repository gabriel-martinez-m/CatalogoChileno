# Servicio de Usuarios (`servicio-usuario`)

Microservicio encargado de la gestión integral de perfiles de usuario, perfiles de artista y datos bancarios para liquidación de fondos dentro del ecosistema **CatalogoChileno**.

## Arquitectura y Diseño

El microservicio sigue el patrón **Modelo - Servicio - Controlador** bajo arquitectura *Database-per-Service*, adaptado a restricciones académicas estrictas:
- **Sin DTOs:** Transferencia y manipulación directa de entidades del modelo de dominio.
- **Lógica en el Modelo/Servicio:** Encapsulación de reglas de negocio en la entidad y la capa de servicio.
- **Controlador Limpio:** Delegación directa de endpoints al servicio.

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
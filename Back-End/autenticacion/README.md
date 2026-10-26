# Servicio de Autenticación (`servicio-autenticacion`)

Microservicio encargado de la gestión de credenciales, autenticación mediante Tokens JWT, refresco de sesión y orquestación inicial del registro de usuarios en **CatalogoChileno**.

## Estructura del Proyecto

```text
com.catalogochileno.autenticacion
├── client
│   └── UsuarioClienteFeign.java
├── controller
│   └── AutenticacionControlador.java
├── model
│   ├── entity
│   │   ├── Credencial.java
│   │   └── TokenRefresco.java
│   └── enums
│       └── RolNombre.java
├── repository
│   ├── CredencialRepositorio.java
│   └── TokenRefrescoRepositorio.java
├── security
│   └── JwtProveedor.java
└── service
    └── AutenticacionServicio.java
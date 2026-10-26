# Servicio de Carrito (`servicio-carrito`)

Microservicio temporal encargado de gestionar la bolsa de compras voluntaria para usuarios autenticados e invitados en **CatalogoChileno**.

## Estructura del Proyecto

```text
com.catalogochileno.carrito
├── client
│   └── CatalogoClienteFeign.java
├── controller
│   └── CarritoControlador.java
├── model
│   ├── entity
│   │   ├── Carrito.java
│   │   └── ItemCarrito.java
│   └── enums
│       └── EstadoCarrito.java
├── repository
│   ├── CarritoRepositorio.java
│   └── ItemCarritoRepositorio.java
└── service
    └── CarritoServicio.java
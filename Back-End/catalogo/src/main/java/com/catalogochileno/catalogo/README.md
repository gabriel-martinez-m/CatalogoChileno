# Servicio de Catálogo (`servicio-catalogo`)

Microservicio encargado de la administración, publicación, moderación y búsqueda filtrada de obras artísticas en el ecosistema **CatalogoChileno**.

## Estructura del Proyecto

```text
com.catalogochileno.catalogo
├── controller
│   └── ProductoControlador.java
├── model
│   ├── entity
│   │   ├── Producto.java
│   │   ├── Categoria.java
│   │   ├── Etiqueta.java
│   │   ├── Region.java
│   │   └── Comuna.java
│   └── enums
│       ├── EstadoProductoEnum.java
│       ├── TipoMedioEnum.java
│       └── TipoVentaEnum.java
├── repository
│   ├── ProductoRepositorio.java
│   ├── CategoriaRepositorio.java
│   ├── EtiquetaRepositorio.java
│   ├── RegionRepositorio.java
│   └── ComunaRepositorio.java
└── service
    └── ProductoServicio.java
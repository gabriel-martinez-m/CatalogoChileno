classDiagram
    direction TB

    %% ENUMS
    class EstadoProductoEnum {
        <<enum>>
        PENDIENTE_REVISION
        APROBADO
        RECHAZADO
        INACTIVO
    }

    class TipoMedioEnum {
        <<enum>>
        FOTO
        VIDEO
        AUDIO
    }

    class TipoVentaEnum {
        <<enum>>
        VENTA_NORMAL
        SUBASTA
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_catalogo_productos)
    class Region {
        <<Entity>>
        -Long id
        -String nombre
        -String codigo
        -List~Comuna~ comunas
    }

    class Comuna {
        <<Entity>>
        -Long id
        -String nombre
        -Region region
    }

    class Categoria {
        <<Entity>>
        -Long id
        -String nombre
        -String slug
        -String descripcion
        -Boolean activa
    }

    class Etiqueta {
        <<Entity>>
        -Long id
        -String nombre
        -String slug
    }

    class Producto {
        <<Entity>>
        -Long id
        -Long idArtista
        -String titulo
        -String descripcion
        -BigDecimal precio
        -TipoMedioEnum tipoMedio
        -EstadoProductoEnum estado
        -TipoVentaEnum tipoVenta
        -String urlPrevisualizacion
        -String formato
        -Integer ancho
        -Integer alto
        -Integer duracionSegundos
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaActualizacion
        -Region region
        -Comuna comuna
        -Categoria categoria
        -Set~Etiqueta~ etiquetas
        +aprobar() Void
        +rechazar(String motivo) Void
        +desactivar() Void
    }

    %% REPOSITORIOS (Spring Data JPA)
    class ProductoRepositorio {
        <<interface>>
        +findByEstado(EstadoProductoEnum estado) List~Producto~
        +findByArtistaId(Long idArtista) List~Producto~
        +buscarConFiltros(String termino, TipoMedioEnum tipoMedio, Long idCategoria, Long idRegion, Long idComuna, BigDecimal precioMin, BigDecimal precioMax, EstadoProductoEnum estado) List~Producto~
    }

    class CategoriaRepositorio {
        <<interface>>
        +findBySlug(String slug) Optional~Categoria~
    }

    class EtiquetaRepositorio {
        <<interface>>
        +findByNombreIn(Set~String~ nombres) Set~Etiqueta~
    }

    %% MODELO / SERVICIO PRINCIPAL (Contiene la lógica y métodos)
    class ProductoServicio {
        <<Service>>
        -ProductoRepositorio productoRepo
        -CategoriaRepositorio categoriaRepo
        -EtiquetaRepositorio etiquetaRepo
        +crearProducto(Long idArtista, String titulo, String descripcion, BigDecimal precio, TipoMedioEnum tipoMedio, TipoVentaEnum tipoVenta, Long idCategoria, Long idRegion, Long idComuna, Set~Long~ idsEtiquetas) Producto
        +obtenerPorId(Long id) Producto
        +listarConFiltros(String termino, TipoMedioEnum tipoMedio, Long idCategoria, Long idRegion, Long idComuna, BigDecimal precioMin, BigDecimal precioMax, EstadoProductoEnum estado) List~Producto~
        +aprobarProducto(Long id) Producto
        +rechazarProducto(Long id, String motivo) Producto
        +desactivarProducto(Long id) Void
    }

    %% CONTROLADOR (Sin métodos de negocio, solo referencia)
    class ProductoControlador {
        <<Controller>>
        -ProductoServicio productoServicio
    }

    %% RELACIONES ENTRE ENTIDADES
    Region "1" *-- "1..*" Comuna : contiene
    Comuna "1" <-- "0..1" Producto : ubicada_en
    Region "1" <-- "0..1" Producto : ubicada_en
    Categoria "1" <-- "*" Producto : pertenece_a
    Etiqueta "*" <--> "*" Producto : etiquetada_con
    EstadoProductoEnum <-- Producto : posee
    TipoMedioEnum <-- Producto : define_tipo
    TipoVentaEnum <-- Producto : modalidad

    %% RELACIONES DE CAPAS
    ProductoServicio --> Producto : gestiona
    ProductoServicio --> ProductoRepositorio : conecta
    ProductoServicio --> CategoriaRepositorio : conecta
    ProductoServicio --> EtiquetaRepositorio : conecta

    ProductoControlador --> ProductoServicio : delega
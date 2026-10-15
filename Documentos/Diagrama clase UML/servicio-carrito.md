classDiagram
    direction TB

    %% ENUMS
    class EstadoCarrito {
        <<enum>>
        ACTIVO
        PROCESANDO
        CONVERTIDO_EN_ORDEN
        ABANDONADO
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_carrito)
    class Carrito {
        <<Entity>>
        -Long idCarrito
        -Long idUsuarioRef
        -String tokenSesionInvitado
        -EstadoCarrito estado
        -List~ItemCarrito~ items
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaActualizacion
        +calcularMontoTotal() Integer
        +vaciar() Void
        +marcarComoProcesado() Void
    }

    class ItemCarrito {
        <<Entity>>
        -Long idItemCarrito
        -Carrito carrito
        -Long idProductoRef
        -Integer cantidad
        -Integer precioUnitarioClp
        -LocalDateTime fechaAgregado
        +actualizarCantidad(Integer nuevaCantidad) Void
        +calcularSubtotal() Integer
    }

    %% CLIENTE FEIGN (Comunicación síncrona con servicio-catalogo)
    class CatalogoClienteFeign {
        <<interface>>
        +validarProductoParaCompra(Long idProducto) Boolean
    }

    %% REPOSITORIOS (Spring Data JPA)
    class CarritoRepositorio {
        <<interface>>
        +findByIdUsuarioRefAndEstado(Long idUsuario, EstadoCarrito estado) Optional~Carrito~
        +findByTokenSesionInvitadoAndEstado(String token, EstadoCarrito estado) Optional~Carrito~
    }

    class ItemCarritoRepositorio {
        <<interface>>
        +deleteByCarritoIdCarritoAndIdProductoRef(Long idCarrito, Long idProducto) Void
    }

    %% MODELO / SERVICIO PRINCIPAL (Contiene la lógica y métodos)
    class CarritoServicio {
        <<Service>>
        -CarritoRepositorio carritoRepo
        -ItemCarritoRepositorio itemRepo
        -CatalogoClienteFeign catalogoFeign
        +obtenerOCrearCarrito(Long idUsuarioRef, String tokenInvitado) Carrito
        +agregarItem(Long idUsuarioRef, String tokenInvitado, Long idProductoRef, Integer cantidad) Carrito
        +actualizarCantidad(Long idItem, Integer nuevaCantidad) Carrito
        +eliminarItem(Long idCarrito, Long idProductoRef) Carrito
        +vaciarCarrito(Long idCarrito) Void
        +marcarComoProcesado(Long idCarrito) Void
    }

    %% CONTROLADOR (Sin métodos de negocio, solo referencia)
    class CarritoControlador {
        <<Controller>>
        -CarritoServicio carritoServicio
    }

    %% RELACIONES
    Carrito "1" <--> "0..*" ItemCarrito : contiene
    Carrito --> EstadoCarrito : usa

    CarritoServicio --> Carrito : gestiona
    CarritoServicio --> ItemCarrito : gestiona
    CarritoServicio --> CarritoRepositorio : conecta
    CarritoServicio --> ItemCarritoRepositorio : conecta
    CarritoServicio --> CatalogoClienteFeign : usa

    CarritoControlador --> CarritoServicio : delega
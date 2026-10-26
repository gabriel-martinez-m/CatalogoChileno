classDiagram
    direction TB

    %% ENUMS
    class OrigenOrden {
        <<enum>>
        VENTA_DIRECTA
        SUBASTA_GANADA
    }

    class EstadoOrden {
        <<enum>>
        PENDIENTE_PAGO
        PAGADA
        CANCELADA
        RECHAZADA
        EXPIRADA
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_ordenes)
    class OrdenCompra {
        <<Entity>>
        -Long idOrden
        -Long idUsuarioRef
        -String correoComprador
        -OrigenOrden origen
        -Long idOrigenRef
        -Integer montoTotalClp
        -EstadoOrden estado
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaExpiracion
        -List~DetalleOrden~ detalles
        +marcarComoPagada() Void
        +cancelar() Void
        +expirar() Void
        +calcularMontoTotal() Integer
    }

    class DetalleOrden {
        <<Entity>>
        -Long idDetalle
        -OrdenCompra ordenCompra
        -Long idProductoRef
        -String tituloProducto
        -Integer precioUnitarioClp
        -Integer cantidad
        -Integer subtotalClp
        +calcularSubtotal() Integer
    }

    %% CLIENTE FEIGN (Comunicación síncrona con servicio-carrito)
    class CarritoClienteFeign {
        <<interface>>
        +marcarComoProcesado(Long idCarrito) Void
    }

    %% REPOSITORIOS (Spring Data JPA)
    class OrdenCompraRepositorio {
        <<interface>>
        +findByIdAndEstado(Long idOrden, EstadoOrden estado) Optional~OrdenCompra~
        +findByCorreoComprador(String correo) List~OrdenCompra~
        +findByIdUsuarioRef(Long idUsuario) List~OrdenCompra~
        +findByEstadoAndFechaExpiracionBefore(EstadoOrden estado, LocalDateTime fecha) List~OrdenCompra~
    }

    %% MODELO / SERVICIO PRINCIPAL (Contiene la lógica y métodos)
    class OrdenCompraServicio {
        <<Service>>
        -OrdenCompraRepositorio ordenRepo
        -CarritoClienteFeign carritoFeign
        +crearDesdeCarrito(Long idCarrito, Long idUsuarioRef, String correoComprador) OrdenCompra
        +crearDesdeSubasta(Long idSubastaRef, Long idUsuarioGanadorRef, String correoComprador, Long idProductoRef, String tituloProducto, Integer montoFinalPujaClp) OrdenCompra
        +obtenerPorId(Long idOrden) OrdenCompra
        +actualizarEstado(Long idOrden, EstadoOrden nuevoEstado) Void
        +listarPorUsuario(Long idUsuario) List~OrdenCompra~
        +expirarOrdenesVencidas() Void
    }

    %% CONTROLADOR (Sin métodos de negocio, solo referencia)
    class OrdenCompraControlador {
        <<Controller>>
        -OrdenCompraServicio ordenServicio
    }

    %% RELACIONES
    OrdenCompra "1" <--> "1..*" DetalleOrden : contiene
    OrdenCompra --> OrigenOrden : clasifica
    OrdenCompra --> EstadoOrden : gestiona

    OrdenCompraServicio --> OrdenCompra : gestiona
    OrdenCompraServicio --> DetalleOrden : gestiona
    OrdenCompraServicio --> OrdenCompraRepositorio : conecta
    OrdenCompraServicio --> CarritoClienteFeign : usa

    OrdenCompraControlador --> OrdenCompraServicio : delega
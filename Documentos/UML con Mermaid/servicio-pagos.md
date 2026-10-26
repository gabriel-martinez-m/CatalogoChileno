classDiagram
    direction TB

    %% ENUMS
    class EstadoTransaccion {
        <<enum>>
        INICIADA
        APROBADA
        RECHAZADA
        ANULADA
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_pagos)
    class TransaccionPago {
        <<Entity>>
        -Long idTransaccion
        -Long idOrdenRef
        -String tokenWs
        -Integer montoTotalClp
        -EstadoTransaccion estado
        -String codigoAutorizacion
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaActualizacion
        +aprobarTransaccion(String codigoAuth) Void
        +rechazarTransaccion() Void
    }

    %% CLIENTE FEIGN (Comunicación con servicio-ordenes)
    class OrdenesClienteFeign {
        <<interface>>
        +obtenerOrden(Long idOrden) Object
    }

    %% REPOSITORIOS (Spring Data JPA)
    class TransaccionPagoRepositorio {
        <<interface>>
        +findByTokenWs(String token) Optional~TransaccionPago~
        +findByIdOrdenRef(Long idOrden) Optional~TransaccionPago~
    }

    %% MODELO / SERVICIO PRINCIPAL
    class PagosServicio {
        <<Service>>
        -TransaccionPagoRepositorio transaccionRepo
        -OrdenesClienteFeign ordenesCliente
        +iniciarTransaccion(Long idOrden) String
        +confirmarTransaccion(String tokenWs) TransaccionPago
        +anularTransaccion(String tokenWs) Void
    }

    %% CONTROLADOR
    class PagosControlador {
        <<Controller>>
        -PagosServicio pagosServicio
    }

    %% RELACIONES
    TransaccionPago --> EstadoTransaccion : posee
    PagosServicio --> TransaccionPago : gestiona
    PagosServicio --> TransaccionPagoRepositorio : conecta
    PagosServicio --> OrdenesClienteFeign : usa
    PagosControlador --> PagosServicio : delega
classDiagram
    direction TB

    %% ENUMS
    class TipoMovimiento {
        <<enum>>
        CREDITO_VENTA
        DEBITO_LIQUIDACION
        DEBITO_FEE_PLATAFORMA
    }

    class EstadoLiquidacion {
        <<enum>>
        PENDIENTE
        APROBADA
        RECHAZADA
        TRANSFERIDA
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_comisiones)
    class LedgerArtista {
        <<Entity>>
        -Long idLedger
        -Long idUsuarioArtistaRef
        -Integer saldoDisponibleClp
        -Integer saldoRetenidoClp
        -LocalDateTime fechaActualizacion
        +sumarCredito(Integer monto) Void
        +retenerFondos(Integer monto) Void
        +liberarFondos(Integer monto) Void
    }

    class Movimiento {
        <<Entity>>
        -Long idMovimiento
        -LedgerArtista ledger
        -TipoMovimiento tipo
        -Integer montoClp
        -Long idOrdenRef
        -LocalDateTime fechaMovimiento
    }

    class SolicitudLiquidacion {
        <<Entity>>
        -Long idSolicitud
        -Long idUsuarioArtistaRef
        -Integer montoSolicitadoClp
        -EstadoLiquidacion estado
        -LocalDateTime fechaSolicitud
        -LocalDateTime fechaResolucion
        +aprobar() Void
        +rechazar() Void
        +marcarComoTransferida() Void
    }

    %% CLIENTE FEIGN
    class UsuarioClienteFeign {
        <<interface>>
        +obtenerDatosBancarios(Long idUsuario) Object
    }

    %% REPOSITORIOS
    class LedgerRepositorio {
        <<interface>>
        +findByIdUsuarioArtistaRef(Long idArtista) Optional~LedgerArtista~
    }
    
    class SolicitudLiquidacionRepositorio {
        <<interface>>
        +findByEstado(EstadoLiquidacion estado) List~SolicitudLiquidacion~
    }

    %% SERVICIO PRINCIPAL
    class ComisionesServicio {
        <<Service>>
        -LedgerRepositorio ledgerRepo
        -SolicitudLiquidacionRepositorio solicitudRepo
        -UsuarioClienteFeign usuarioCliente
        +registrarVenta(Long idArtista, Long idOrden, Integer montoTotal) Void
        +solicitarLiquidacion(Long idArtista, Integer monto) SolicitudLiquidacion
        +procesarLiquidacion(Long idSolicitud) Void
    }

    %% CONTROLADOR
    class ComisionesControlador {
        <<Controller>>
        -ComisionesServicio comisionesServicio
    }

    %% RELACIONES
    LedgerArtista "1" <--> "0..*" Movimiento : registra
    Movimiento --> TipoMovimiento : clasifica
    SolicitudLiquidacion --> EstadoLiquidacion : posee
    ComisionesServicio --> LedgerArtista : gestiona
    ComisionesServicio --> SolicitudLiquidacion : gestiona
    ComisionesServicio --> LedgerRepositorio : conecta
    ComisionesServicio --> UsuarioClienteFeign : usa
    ComisionesControlador --> ComisionesServicio : delega
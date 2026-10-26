classDiagram
    direction TB

    %% ENUMS
    class EstadoLicencia {
        <<enum>>
        ACTIVA
        REVOCADA
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_licencias_entregables)
    class LicenciaCertificado {
        <<Entity>>
        -Long idLicencia
        -String codigoUnicoQr
        -Long idUsuarioCompradorRef
        -Long idProductoRef
        -Long idOrdenRef
        -String urlPdfGenerado
        -EstadoLicencia estado
        -LocalDateTime fechaEmision
        +revocar() Void
        +esValida() Boolean
    }

    class EnlaceDescarga {
        <<Entity>>
        -Long idEnlace
        -LicenciaCertificado licencia
        -String urlFirmadaAwsS3
        -LocalDateTime fechaExpiracion
        -Boolean estaUsado
        +marcarComoUsado() Void
        +estaExpirado() Boolean
    }

    %% CLIENTE FEIGN
    class CatalogoClienteFeign {
        <<interface>>
        +obtenerMetadatosLicencia(Long idProducto) Object
    }

    %% REPOSITORIOS
    class LicenciaRepositorio {
        <<interface>>
        +findByCodigoUnicoQr(String codigo) Optional~LicenciaCertificado~
        +findByIdUsuarioCompradorRef(Long idUsuario) List~LicenciaCertificado~
    }

    class EnlaceDescargaRepositorio {
        <<interface>>
        +findByUrlFirmadaAwsS3(String url) Optional~EnlaceDescarga~
    }

    %% SERVICIO PRINCIPAL
    class LicenciaServicio {
        <<Service>>
        -LicenciaRepositorio licenciaRepo
        -EnlaceDescargaRepositorio enlaceRepo
        -CatalogoClienteFeign catalogoCliente
        +generarLicencia(Long idUsuario, Long idProducto, Long idOrden) LicenciaCertificado
        +validarLicenciaPublica(String codigoQr) Boolean
        +generarEnlaceDescarga(Long idLicencia) EnlaceDescarga
    }

    %% CONTROLADOR
    class LicenciaControlador {
        <<Controller>>
        -LicenciaServicio licenciaServicio
    }

    %% RELACIONES
    LicenciaCertificado "1" <--> "0..*" EnlaceDescarga : genera
    LicenciaCertificado --> EstadoLicencia : posee
    LicenciaServicio --> LicenciaCertificado : gestiona
    LicenciaServicio --> LicenciaRepositorio : conecta
    LicenciaServicio --> EnlaceDescargaRepositorio : conecta
    LicenciaServicio --> CatalogoClienteFeign : usa
    LicenciaControlador --> LicenciaServicio : delega
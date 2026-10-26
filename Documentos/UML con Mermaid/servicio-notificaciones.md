classDiagram
    direction TB

    %% ENUMS
    class TipoCorreo {
        <<enum>>
        BIENVENIDA_USUARIO
        CONFIRMACION_COMPRA
        ENLACES_DESCARGA
        ALERTA_SUBASTA
        LIQUIDACION_FONDOS
    }

    class EstadoEnvio {
        <<enum>>
        PENDIENTE
        ENVIADO
        FALLIDO
    }

    %% ENTIDADES / MODELO DE DOMINIO (bd_notificaciones)
    class RegistroNotificacion {
        <<Entity>>
        -Long idNotificacion
        -String correoDestinatario
        -TipoCorreo tipo
        -String asunto
        -String contenidoHtml
        -EstadoEnvio estado
        -LocalDateTime fechaCreacion
        -LocalDateTime fechaEnvio
        -String errorDetalle
        +marcarEnviado() Void
        +marcarFallido(String error) Void
    }

    %% COMPONENTES / UTILIDADES
    class SmtpProveedor {
        <<Component>>
        -String host
        -Integer puerto
        -String usuario
        -String contrasena
        +enviarCorreo(String destino, String asunto, String html) Boolean
    }

    %% REPOSITORIOS
    class NotificacionRepositorio {
        <<interface>>
        +findByEstado(EstadoEnvio estado) List~RegistroNotificacion~
        +findByCorreoDestinatario(String correo) List~RegistroNotificacion~
    }

    %% SERVICIO PRINCIPAL
    class NotificacionServicio {
        <<Service>>
        -NotificacionRepositorio notificacionRepo
        -SmtpProveedor smtpProveedor
        +encolarCorreo(String destinatario, TipoCorreo tipo, String asunto, String html) RegistroNotificacion
        +procesarEnviosPendientes() Void
        +reintentarFallidos() Void
    }

    %% LISTENERS / CONTROLADORES
    class NotificacionListener {
        <<Component>>
        -NotificacionServicio notificacionServicio
        +recibirEventoCompra(Object evento) Void
        +recibirEventoRegistro(Object evento) Void
    }

    %% RELACIONES
    RegistroNotificacion --> TipoCorreo : clasifica
    RegistroNotificacion --> EstadoEnvio : posee
    NotificacionServicio --> RegistroNotificacion : gestiona
    NotificacionServicio --> NotificacionRepositorio : conecta
    NotificacionServicio --> SmtpProveedor : usa
    NotificacionListener --> NotificacionServicio : reacciona
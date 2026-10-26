package com.catalogochileno.carrito.model.enums;

/**
 * Define el estado actual del carrito de compras dentro de su ciclo de vida.
 */
public enum EstadoCarrito {
    ACTIVO,
    PROCESANDO,
    CONVERTIDO_EN_ORDEN,
    ABANDONADO
}
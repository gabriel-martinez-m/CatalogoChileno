package com.catalogochileno.carrito.service;

import com.catalogochileno.carrito.client.CatalogoClienteFeign;
import com.catalogochileno.carrito.model.entity.Carrito;
import com.catalogochileno.carrito.model.entity.ItemCarrito;
import com.catalogochileno.carrito.model.enums.EstadoCarrito;
import com.catalogochileno.carrito.repository.CarritoRepositorio;
import com.catalogochileno.carrito.repository.ItemCarritoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de lógica de negocio para la gestión de carritos de compra e ítems.
 */
@Service
@RequiredArgsConstructor
public class CarritoServicio {

    private final CarritoRepositorio carritoRepo;
    private final ItemCarritoRepositorio itemRepo;
    private final CatalogoClienteFeign catalogoFeign;

    /**
     * Obtiene el carrito activo del usuario/invitado o crea uno nuevo si no existe.
     */
    @Transactional
    public Carrito obtenerOCrearCarrito(Long idUsuarioRef, String tokenInvitado) {
        if (idUsuarioRef != null) {
            return carritoRepo.findByIdUsuarioRefAndEstado(idUsuarioRef, EstadoCarrito.ACTIVO)
                    .orElseGet(() -> {
                        Carrito nuevo = new Carrito();
                        nuevo.setIdUsuarioRef(idUsuarioRef);
                        return carritoRepo.save(nuevo);
                    });
        } else if (tokenInvitado != null) {
            return carritoRepo.findByTokenSesionInvitadoAndEstado(tokenInvitado, EstadoCarrito.ACTIVO)
                    .orElseGet(() -> {
                        Carrito nuevo = new Carrito();
                        nuevo.setTokenSesionInvitado(tokenInvitado);
                        return carritoRepo.save(nuevo);
                    });
        }
        throw new IllegalArgumentException("Se requiere idUsuarioRef o tokenInvitado para identificar el carrito.");
    }

    /**
     * Regla de Negocio: Agrega un producto previa validación con el microservicio de catálogo.
     */
    @Transactional
    public Carrito agregarItem(Long idUsuarioRef, String tokenInvitado, Long idProductoRef, Integer cantidad, Integer precioUnitarioClp) {
        // Validar disponibilidad en catálogo mediante cliente Feign
        Boolean disponible = catalogoFeign.validarProductoParaCompra(idProductoRef);
        if (Boolean.FALSE.equals(disponible)) {
            throw new IllegalStateException("El producto con ID " + idProductoRef + " no se encuentra disponible.");
        }

        Carrito carrito = obtenerOCrearCarrito(idUsuarioRef, tokenInvitado);

        // Verificar si el producto ya existe en el carrito
        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getIdProductoRef().equals(idProductoRef))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.actualizarCantidad(item.getCantidad() + cantidad);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setIdProductoRef(idProductoRef);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitarioClp(precioUnitarioClp);
            carrito.getItems().add(nuevoItem);
        }

        return carritoRepo.save(carrito);
    }

    /**
     * Actualiza la cantidad de un ítem existente en el carrito.
     */
    @Transactional
    public Carrito actualizarCantidad(Long idItem, Integer nuevaCantidad) {
        ItemCarrito item = itemRepo.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem de carrito no encontrado con ID: " + idItem));

        item.actualizarCantidad(nuevaCantidad);
        itemRepo.save(item);

        return item.getCarrito();
    }

    /**
     * Elimina un producto específico del carrito.
     */
    @Transactional
    public Carrito eliminarItem(Long idCarrito, Long idProductoRef) {
        Carrito carrito = carritoRepo.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + idCarrito));

        carrito.getItems().removeIf(item -> item.getIdProductoRef().equals(idProductoRef));
        return carritoRepo.save(carrito);
    }

    /**
     * Vacía el contenido completo del carrito.
     */
    @Transactional
    public void vaciarCarrito(Long idCarrito) {
        Carrito carrito = carritoRepo.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + idCarrito));

        carrito.vaciar();
        carritoRepo.save(carrito);
    }

    /**
     * Marca el carrito como procesado/convertido en orden.
     */
    @Transactional
    public void marcarComoProcesado(Long idCarrito) {
        Carrito carrito = carritoRepo.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + idCarrito));

        carrito.marcarComoProcesado();
        carritoRepo.save(carrito);
    }
}
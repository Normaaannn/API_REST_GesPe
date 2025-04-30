package com.example.GesPeSpring.Tablas.PedidoDetalle;

import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalle;
import com.example.GesPeSpring.Tablas.Pedido.PedidoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoDetalleService {

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private PedidoRepository pedidoRepository; // Asegúrate de tener el repositorio de Pedido

    // Método para obtener un detalle por su id
    public Optional<PedidoDetalle> obtenerPorId(Long id) {
        return pedidoDetalleRepository.findById(id);
    }

    // Método para obtener todos los detalles de un pedido
    public List<PedidoDetalle> obtenerDetallesPorPedido(Long idPedido) {
        return pedidoDetalleRepository.findByPedidoId(idPedido);
    }

    // Método para obtener todos los detalles de un producto
    public List<PedidoDetalle> obtenerDetallesPorProducto(Long idProducto) {
        return pedidoDetalleRepository.findByProductoId(idProducto);
    }

    // Método para agregar un nuevo detalle de pedido
    public List<PedidoDetalle> agregarDetalle(List<PedidoDetalle> detalles) {
        // Verificar si el pedido es el último pedido creado
        if (!esUltimoPedido(detalles.get(0).getPedido().getId())) {
            throw new IllegalStateException("No se pueden agregar más detalles a este pedido porque ya se ha creado un pedido posterior.");
        }

        // Guardamos el nuevo detalle en la base de datos
        return pedidoDetalleRepository.saveAll(detalles);
    }

    // Verificar si el pedido es el último pedido creado
    private boolean esUltimoPedido(Long idPedido) {
        Optional<Pedido> ultimoPedido = pedidoRepository.findTopByOrderByIdDesc();

        // Si el pedido existe y es el último, entonces podemos añadir el detalle
        return ultimoPedido.isPresent() && ultimoPedido.get().getId().equals(idPedido);
    }
}

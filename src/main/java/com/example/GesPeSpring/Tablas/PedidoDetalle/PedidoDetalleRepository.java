package com.example.GesPeSpring.Tablas.PedidoDetalle;

import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    // Buscar todos los detalles de un pedido por su idPedido
    List<PedidoDetalle> findByPedidoId(Long idPedido);
    
    List<PedidoDetalle> findByProductoId(Long idProducto);

}

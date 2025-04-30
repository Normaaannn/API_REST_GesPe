package com.example.GesPeSpring.Tablas.PedidoDetalle;

import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.Pedido.PedidoService;
import com.example.GesPeSpring.Tablas.Producto.Producto;
import com.example.GesPeSpring.Tablas.Producto.ProductoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos/{idPedido}/detalles")
public class PedidoDetalleController {

    @Autowired
    private PedidoDetalleService pedidoDetalleService;
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<PedidoDetalleDTO>> obtenerDetallesPorPedido(@PathVariable Long idPedido, Authentication authentication) {
        String usernameAuth = authentication.getName();

        Optional<Pedido> optionalPedido = pedidoService.obtenerPorId(idPedido);

        if (optionalPedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Pedido pedido = optionalPedido.get();

        if (!pedido.getUsuarioCreador().getUsername().equals(usernameAuth) && !authentication.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<PedidoDetalleDTO> detallesDTO = pedidoDetalleService.obtenerDetallesDTOporPedido(idPedido);

        return ResponseEntity.ok(detallesDTO);
    }

    @PostMapping
    public String agregarDetalle(@PathVariable Long idPedido, @RequestBody List<PedidoDetalle> detalles) {
        pedidoDetalleService.agregarDetalle(detalles);
        return "Detalles creados";
    }

// Obtener detalles por producto (opcional, si lo necesitas)
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<PedidoDetalle>> obtenerDetallesPorProducto(@PathVariable Long idProducto) {
        List<PedidoDetalle> detalles = pedidoDetalleService.obtenerDetallesPorProducto(idProducto);
        return detalles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(detalles);
    }
}

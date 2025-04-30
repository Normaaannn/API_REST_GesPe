package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.Usuario.Usuario;
import com.example.GesPeSpring.Tablas.Usuario.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> obtenerPorCliente(@PathVariable Long clienteId) {
        return pedidoService.obtenerPorCliente(clienteId);
    }

    @GetMapping("/usuarioCreador/{page}")
    public ResponseEntity<Page<Pedido>> obtenerPorUsuarioCreador(
            Authentication authentication,
            @PathVariable int page,
            @RequestParam(defaultValue = "10") int size) {

        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Pedido> pedidos = pedidoService.obtenerPorUsuarioCreador(username, pageable);

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/fecha")
    public List<Pedido> obtenerPorMesYAnio(@RequestParam int year, @RequestParam int month) {
        return pedidoService.obtenerPorMesYAnio(year, month);
    }

    @PostMapping
    public ResponseEntity<Long> crearPedido(@RequestBody Pedido pedido, Authentication authentication) {
        
        String usernameAuth = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(usernameAuth);
        
        pedido.setUsuarioCreador(usuario);
        Pedido pedidoCreado = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(pedidoCreado.getId());
    }

}

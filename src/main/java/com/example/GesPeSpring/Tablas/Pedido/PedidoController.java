package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.InfoEmpresa.InfoEmpresa;
import com.example.GesPeSpring.Tablas.InfoEmpresa.InfoEmpresaService;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalleDTO;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalleService;
import com.example.GesPeSpring.Tablas.Usuario.Usuario;
import com.example.GesPeSpring.Tablas.Usuario.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private PedidoDetalleService pedidoDetalleService;

    @Autowired
    private InfoEmpresaService infoEmpresaService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
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
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        //Si es admin, le devuelve todos
        if (authentication.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN")) {
            Page<Pedido> pedidos = pedidoService.obtenerTodosPageable(pageable);
            return ResponseEntity.ok(pedidos);
        }
        Page<Pedido> pedidos = pedidoService.obtenerPorUsuarioCreador(username, pageable);

        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<Page<Pedido>> buscarPedidos(
            Authentication authentication,
            @RequestParam int year1,
            @RequestParam int month1,
            @RequestParam int year2,
            @RequestParam int month2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        if (authentication.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN")) {
            Page<Pedido> pedidos = pedidoService.buscarPedidosFechaBetween(pageable, year1, month1, year2, month2);
            return ResponseEntity.ok(pedidos);
        }       
        Page<Pedido> pedidos = pedidoService.buscarPedidosUsuarioFechaBetween(pageable, username, year1, month1, year2, month2);
        return ResponseEntity.ok(pedidos);
    }

    
    /*
    ///pedido/fechaBetween?year1=2023&month1=10&year2=2025&month2=5
    @GetMapping("/fechaBetween")
    public ResponseEntity<List<Pedido>> obtenerPedidosFechaBetween(
        @RequestParam int year1,
        @RequestParam int month1,
        @RequestParam int year2,
        @RequestParam int month2
    ) {
        List<Pedido> pedidos = pedidoService.buscarPedidosFechaBetween(year1, month1, year2, month2);
        return ResponseEntity.ok(pedidos);
    }*/

    @PostMapping
    public ResponseEntity<Long> crearPedido(@RequestBody Pedido pedido, Authentication authentication) {

        String usernameAuth = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(usernameAuth);

        pedido.setUsuarioCreador(usuario);
        Pedido pedidoCreado = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(pedidoCreado.getId());
    }

    //factura
    @GetMapping("/factura/{id}/pdf")
    public ResponseEntity<byte[]> generarFactura(@PathVariable Long id) throws Exception {
        Pedido pedido = pedidoService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        List<PedidoDetalleDTO> detalles = pedidoDetalleService.obtenerDetallesDTOporPedido(id);
        InfoEmpresa empresa = infoEmpresaService.obtenerInfoEmpresa()
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        byte[] pdf = pedidoService.generarPdfFactura(pedido, detalles, empresa);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}

package com.example.GesPeSpring.Misc;

import com.example.GesPeSpring.Tablas.Cliente.Cliente;
import com.example.GesPeSpring.Tablas.Cliente.ClienteService;
import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.Pedido.PedidoService;
import com.example.GesPeSpring.Tablas.Producto.Producto;
import com.example.GesPeSpring.Tablas.Producto.ProductoService;
import com.example.GesPeSpring.Tablas.Usuario.UsuarioRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Norman
 */
@RestController
@RequestMapping("/misc")
public class MiscController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/datosHome")
    public DatosHome datosHome(Authentication authentication) {

        String username = authentication.getName();
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id").descending());

        Page<Pedido> pedidos;
        float total;
        //Si es admin, le devuelve todos
        if (authentication.getAuthorities().iterator().next().getAuthority().equals("ROLE_ADMIN")) {
            pedidos = pedidoService.obtenerTodosPageable(pageable);
            total = pedidoService.obtenerSumaTotalPedidosPorMes(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        } else {
            pedidos = pedidoService.obtenerPorUsuarioCreador(username, pageable);
            total = pedidoService.obtenerSumaTotalPedidosPorMesUsername(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), username);
        }

        Page<Cliente> clientes = clienteService.obtenerClientesActivosPaginados(pageable);
        Page<Producto> productos = productoService.obtenerProductosActivosPaginados(pageable);

        DatosHome datosHome = new DatosHome();
        datosHome.setTotal(total);
        datosHome.setPedidos(pedidos.getContent());
        datosHome.setClientes(clientes.getContent());
        datosHome.setProductos(productos.getContent());

        return datosHome;
    }
}

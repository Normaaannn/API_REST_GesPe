
package com.example.GesPeSpring.Misc;

import com.example.GesPeSpring.Tablas.Cliente.Cliente;
import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.Producto.Producto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Norman
 */
@Getter
@Setter
public class DatosHome {
    
    private float total;
    private List<Pedido> pedidos;
    private List<Cliente> clientes;
    private List<Producto> productos;
}


package com.example.GesPeSpring.Tablas.PedidoDetalle;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PedidoDetalleDTO {
    
    private String nombreProducto;
    private String descripcion;
    private BigDecimal iva;
    private BigDecimal precio;
    private BigDecimal cantidad;
    private BigDecimal subtotal;
    
}

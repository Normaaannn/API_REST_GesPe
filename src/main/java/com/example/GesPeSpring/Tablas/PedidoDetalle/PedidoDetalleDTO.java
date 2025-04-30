
package com.example.GesPeSpring.Tablas.PedidoDetalle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PedidoDetalleDTO {
    
    private String nombreProducto;
    private String descripcion;
    private float iva;
    private float precio;
    private int cantidad;
    private float subtotal;
    
}

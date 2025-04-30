package com.example.GesPeSpring.Tablas.Producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Norman
 */
@Getter
@Setter
@AllArgsConstructor
public class ProductoDTO {

    private String nombre;
    private String descripcion;
    private float precioNeto;
    private float iva;

}

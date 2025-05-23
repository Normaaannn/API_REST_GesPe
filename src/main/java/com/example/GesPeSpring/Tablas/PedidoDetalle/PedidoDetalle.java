package com.example.GesPeSpring.Tablas.PedidoDetalle;

import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.Producto.Producto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedidodetalle")
public class PedidoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idPedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private BigDecimal precioNeto;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(nullable = true)
    private BigDecimal subtotal;

    @PrePersist
    protected void onCreate() {
        if (this.producto != null && this.precioNeto != null && this.cantidad != null) {
            //Obtener el IVA como porcentaje decimal
            BigDecimal ivaDecimal = producto.getIva().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal multiplicadorIVA = ivaDecimal.add(BigDecimal.ONE); // (1 + iva/100)

            //Calcular el subtotal: precioNeto * (1 + iva) * cantidad
            this.subtotal = precioNeto
                    .multiply(multiplicadorIVA)
                    .multiply(cantidad)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new IllegalStateException("Producto, precioNeto o cantidad no pueden ser nulos");
        }
    }
}

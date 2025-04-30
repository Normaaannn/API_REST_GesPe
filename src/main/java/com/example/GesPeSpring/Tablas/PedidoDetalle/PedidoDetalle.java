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
    private float precioNeto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = true)
    private float subtotal;

    @PrePersist
    protected void onCreate() {
        if (this.producto != null) {
            this.subtotal = this.cantidad * (this.precioNeto * (1 + (this.producto.getIva() / 100)));
        } else {
            throw new IllegalStateException("Producto no puede ser nulo");
        }
    }
}

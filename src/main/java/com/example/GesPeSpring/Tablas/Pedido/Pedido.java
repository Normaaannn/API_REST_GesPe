
package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.Cliente.Cliente;
import com.example.GesPeSpring.Tablas.Usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, updatable = false)
    private LocalDate fechaEmision;
    
    @Column(nullable = false, updatable = false, precision = 50, scale = 2)
    private BigDecimal total;
    
    @ManyToOne
    @JoinColumn(name = "idUsuarioCreador", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Usuario usuarioCreador;

    @PrePersist
    protected void onCreate() {
        this.fechaEmision = LocalDate.now();  //Establece la fecha actual (solo año, mes y día)
    }
    
}

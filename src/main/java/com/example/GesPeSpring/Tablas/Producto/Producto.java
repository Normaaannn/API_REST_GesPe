package com.example.GesPeSpring.Tablas.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precioNeto;

    @Column(nullable = false)
    private BigDecimal iva;

    @Column(nullable = false)
    private BigDecimal precioBruto;

    @Column(updatable = false)
    private LocalDate fechaRegistro;

    @Column(nullable = false)
    private boolean activo;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();  //Establece la fecha actual (solo año, mes y día)
        calcularPrecioBruto();
        this.activo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        calcularPrecioBruto();
    }
    
    private void calcularPrecioBruto() {
        if (precioNeto != null && iva != null) {
            BigDecimal multiplicadorIVA = iva.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                                             .add(BigDecimal.ONE);
            this.precioBruto = precioNeto.multiply(multiplicadorIVA)
                                         .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.precioBruto = BigDecimal.ZERO;
        }
    }
}

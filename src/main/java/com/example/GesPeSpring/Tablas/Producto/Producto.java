package com.example.GesPeSpring.Tablas.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
    private float precioNeto;

    @Column(nullable = false)
    private float iva;

    @Column(nullable = false)
    private float precioBruto;

    @Column(updatable = false)
    private LocalDate fechaRegistro;

    @Column(nullable = false)
    private boolean activo;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();  //Establece la fecha actual (solo año, mes y día)
        this.precioBruto = this.precioNeto * (1 + (this.iva / 100));
        this.activo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.precioBruto = this.precioNeto * (1 + (this.iva / 100));
    }
}

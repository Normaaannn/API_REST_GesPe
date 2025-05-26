package com.example.GesPeSpring.Tablas.Cliente;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String apellidos;

    @Column(unique = true, nullable = false, length = 15)
    private String nif;  //DNI o número de identificación

    @Column(nullable = false, length = 60)
    private String email;
    
    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 100)
    private String direccion;
    @Column(length = 50)
    private String ciudad;
    @Column(length = 10)
    private String codigoPostal;
    @Column(length = 50)
    private String pais;
    
    @Column(updatable = false)
    private LocalDate fechaRegistro;
    
    @Column(nullable = false)
    private boolean activo;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();  //Establece la fecha actual (solo año, mes y día)
        this.activo = true;
    }
}

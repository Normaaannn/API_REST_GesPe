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

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String nif;  //DNI o número de identificación

    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String telefono;

    private String direccion;
    private String ciudad;
    private String codigoPostal;
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

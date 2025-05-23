
package com.example.GesPeSpring.Tablas.InfoEmpresa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "info_empresa")
public class InfoEmpresa {
    
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
    
    private String logo;
    
}

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

    @Column(length = 2083)
    private String logoUrl;

}

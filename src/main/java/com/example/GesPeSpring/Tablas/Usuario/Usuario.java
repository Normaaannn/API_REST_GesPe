package com.example.GesPeSpring.Tablas.Usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 25)
    private String username;

    @Column(unique = true, nullable = false, length = 60)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Para que al hacer un GET, no reciba el hash de la contraseña
    @Column(nullable = false)
    private String passwordHash;

    private String role; //ROLE_USER, ROLE_ADMIN
    
    @Column(length = 2083)
    private String avatarUrl;
    
    @Column(updatable = false)
    private LocalDate fechaRegistro;
    
    //Tokens para reiniciar la contraseña
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Para que al hacer un GET, no reciba el hash de la contraseña
    private String resetToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Para que al hacer un GET, no reciba el hash de la contraseña
    private LocalDateTime tokenExpiration;
    
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();  //Establece la fecha actual (solo año, mes y día)
    }
    
}



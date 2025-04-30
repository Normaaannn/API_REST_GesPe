
package com.example.GesPeSpring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Algo que me hizo perder unas cuantas horas porque no entendia por que
//me iban las solicitudes en el postman y no en el navegador, en fin, restricciones
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  //Permite todas las rutas
                .allowedOrigins("http://localhost:8100")  //Permite cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  //Métodos permitidos
                .allowedHeaders("Authorization", "Content-Type", "Access-Control-Allow-Origin")  //Permite cualquier encabezado
                .allowCredentials(true);  //Permite el envío de cookies o cabeceras de autenticación
    }
}

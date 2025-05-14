package com.example.GesPeSpring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    //Inyección del filtro JWT
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) //Desactivar CSRF (usualmente para APIs)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //No usar sesiones en el servidor
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //Permitir OPTIONS para solicitudes preflight
                .requestMatchers("/auth/login", "/auth/refresh", "/usuario/register", "/usuario/forgot-password", "/usuario/reset-password").permitAll() //Permitir acceso a login y refresh sin autenticación
                .requestMatchers("/usuario/actualizarPassword").hasAnyRole("ADMIN", "USER", "GUEST")
                .requestMatchers("/info_empresa/**", "/usuario/**").hasRole("ADMIN")
                .requestMatchers("/producto/todos/**", "/cliente/todos/**").hasRole("ADMIN")
                .requestMatchers("/producto/inactivos/**", "/cliente/inactivos/**").hasRole("ADMIN")
                .requestMatchers("/producto/buscar/todos/**", "/cliente/buscar/todos/**").hasRole("ADMIN")
                .requestMatchers("/producto/buscar/inactivos/**", "/cliente/buscar/inactivos/**").hasRole("ADMIN")
                .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().denyAll() //Todo lo demás requiere autenticación
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //Agregar el filtro JWT antes del filtro de autenticación de usuario

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

package com.example.GesPeSpring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        if (jwtUtil.isTokenValid(token)) {
            // Extraemos el nombre de usuario y el único rol desde el JWT
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);  // Asumimos que tienes un método para obtener el rol desde el token

            // Crear un objeto UserDetails con el único rol extraído
            GrantedAuthority authority = new SimpleGrantedAuthority(role);  // Solo un rol

            UserDetails userDetails = User.withUsername(username)
                    .password("") // No se necesita contraseña si estás usando JWT
                    .authorities(authority) // Solo un rol
                    .build();

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            );
        }
    }

    // Continuar con el filtrado
    chain.doFilter(request, response);
    }
}

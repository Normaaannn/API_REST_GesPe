package com.example.GesPeSpring;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //Key aleatoria cada vez que se reinicia la API
    private final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; //15 minutos
    private final long REFRESH_TOKEN_EXPIRATION = 20 * 24 * 60 * 60 * 1000; //20 dias

    //Generar Access Token
    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    //Generar Refresh Token
    public String generateRefreshToken(String username, String role) {
        Date expirationDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
    System.out.println("Refresh token expiration date: " + expirationDate);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    //Validar token y extraer el username
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    //Verificar si un token es valido
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); //Verifica la firma del token
            return true;
        } catch (JwtException e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }
}

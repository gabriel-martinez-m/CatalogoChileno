package com.catalogochileno.autenticacion.security;

import com.catalogochileno.autenticacion.model.entity.Credencial;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Componente utilitario para la generación, lectura y validación de tokens JWT.
 */
@Component
public class JwtProveedor {

    @Value("${jwt.secret:ClaveSecretaParaFirmarLosTokensJWTDeCatalogoChileno2026!}")
    private String claveSecreta;

    @Value("${jwt.expiration.access-token-ms:3600000}")
    private Long expiracionTokenAccesoMs;

    @Value("${jwt.expiration.refresh-token-ms:86400000}")
    private Long expiracionTokenRefrescoMs;

    private Key obtenerLlaveFirma() {
        byte[] keyBytes = claveSecreta.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarTokenAcceso(Credencial credencial) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiracionTokenAccesoMs);

        return Jwts.builder()
                .setSubject(credencial.getCorreoElectronico())
                .claim("idCredencial", credencial.getIdCredencial())
                .claim("rol", credencial.getRol().name())
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(obtenerLlaveFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generarTokenRefresco(Credencial credencial) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiracionTokenRefrescoMs);

        return Jwts.builder()
                .setSubject(credencial.getCorreoElectronico())
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(obtenerLlaveFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerCorreoDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(obtenerLlaveFirma())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(obtenerLlaveFirma())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}                           
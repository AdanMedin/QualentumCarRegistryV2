package com.aalonso.CarRegistry.services.imp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * Este servicio se encarga de la generación, validación y extracción de información de los tokens JWT (JSON Web Tokens).
 */
@Service
public class JwtService {

    // Estos campos almacenan la clave secreta y el tiempo de expiración del token.
    @Value("${token.secret.key}") // Se obtiene el valor de la propiedad token.secret.key del archivo application.properties
            String jwtSecretKey;
    @Value("${token.expiration.ms}") // Se obtiene el valor de la propiedad token.expirationms del archivo application.properties
    String jwtExpirationMs;

    // Este método extrae el correo electrónico del token JWT.
    public String extractEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    // Este método genera un token JWT a partir de los detalles de un usuario.

    /**
     * En este caso, se está pasando un HashMap vacío.
     * No se añaden reclamaciones y datos adicionales al token.
     * Si quisieras añadir reclamaciones adicionales,
     * podrías hacerlo añadiendo pares clave-valor al HashMap.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Este método verifica si el token JWT es válido.
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String email = extractEmail(jwtToken);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    // Este método verifica si el token JWT ha expirado.
    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    // Este método extrae la fecha de expiración del token JWT.
    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    // Este método genera un token JWT a partir de las reclamaciones y los detalles de un usuario.
    private String generateToken(HashMap<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts
                .builder() // Este método inicia la construcción de un token JWT.
                .setClaims(extractClaims) // Este método establece las reclamaciones del token.
                .setSubject(userDetails.getUsername()) // Este método establece el sujeto del token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Este método establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpirationMs))) // Este método establece la fecha de expiración del token.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Este método establece el algoritmo de firma y la clave secreta del token.
                .compact(); // Este método finaliza la construcción del token y lo devuelve como una cadena de texto.
    }

    // Este método extrae una reclamación específica de un token JWT.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Este método extrae todas las reclamaciones de un token JWT.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Este método genera la clave de firma para los tokens JWT a partir de la clave secreta.
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

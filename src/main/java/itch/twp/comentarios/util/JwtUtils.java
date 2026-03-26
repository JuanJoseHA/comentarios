package itch.twp.comentarios.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

        private static final String SECRET_KEY = "VGhpcyBpcyBhIHN1cGVyIHNlY3JldCBrZXkgZm9yIEpXVCBhdXRoZW50aWNhdGlvbiAyMDI2";

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método para abrir el token y sacar todos los datos (Claims)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

  
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }


    public Long extractUsuarioId(String token) {
        return extractAllClaims(token).get("id", Long.class); 
        
    }
}
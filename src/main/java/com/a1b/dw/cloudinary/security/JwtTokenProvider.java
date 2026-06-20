package com.a1b.dw.cloudinary.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;

    public JwtTokenProvider(String base64Secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Local JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        String role = parseClaims(token).get("role", String.class);
        return role != null ? role : "USER";
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }
}

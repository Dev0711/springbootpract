package com.ecommerce.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Utility class for JWT validation at the gateway level.
 * Uses JJWT 0.12.5 API — no deprecated methods.
 */
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // Build HMAC-SHA key from the secret string
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates the token signature and expiry.
     * Returns true if valid, false otherwise.
     */
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts all claims from the token.
     * Throws JwtException if token is invalid or expired.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)   // JJWT 0.12.x API
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Extracts the user's email (subject) from the token. */
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    /** Extracts the user's UUID from the token. */
    public String getUserId(String token) {
        Object userId = getClaims(token).get("userId");
        return userId != null ? userId.toString() : null;
    }

    /** Extracts the user's role from the token. */
    public String getRole(String token) {
        Object role = getClaims(token).get("role");
        return role != null ? role.toString() : null;
    }
}

package com.springprat.auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration; // in milliseconds

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, UUID userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)  // Updated: claims() instead of setClaims()
                .subject(email)  // Updated: subject() instead of setSubject()
                .issuedAt(new Date())  // Updated: issuedAt() instead of setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))  // Updated: expiration() instead of setExpiration()
                .signWith(getSigningKey())  // Updated: signWith() without algorithm parameter
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()  // Updated: parser() instead of parserBuilder()
                .verifyWith(getSigningKey())  // Updated: verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token)  // Updated: parseSignedClaims() instead of parseClaimsJws()
                .getPayload();  // Updated: getPayload() instead of getBody()
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    public Long getAccessTokenExpirationInSeconds() {
        return accessTokenExpiration / 1000;
    }
}
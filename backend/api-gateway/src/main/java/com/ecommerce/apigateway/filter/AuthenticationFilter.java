package com.ecommerce.apigateway.filter;

import com.ecommerce.apigateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * JWT Authentication Filter — applied per-route on protected endpoints.
 *
 * Flow:
 * 1. Extract "Authorization: Bearer <token>" header
 * 2. Validate the JWT using JwtUtil
 * 3. If valid → add X-User-Id, X-User-Email, X-User-Role headers to upstream
 * request
 * 4. If invalid → return 401 JSON error immediately
 */
@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Extract Authorization header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or malformed Authorization header for path: {}", request.getPath());
                return unauthorizedResponse(exchange, "Authorization header is missing or invalid");
            }

            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // 2. Validate JWT
            if (!jwtUtil.isValid(token)) {
                log.warn("Invalid JWT token for path: {}", request.getPath());
                return unauthorizedResponse(exchange, "Invalid or expired token");
            }

            // 3. Extract claims and inject as upstream headers
            String userId = jwtUtil.getUserId(token);
            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);

            log.debug("Authenticated request — userId: {}, email: {}, role: {}", userId, email, role);

            // Mutate request to add user identity headers for downstream services
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId != null ? userId : "")
                    .header("X-User-Email", email != null ? email : "")
                    .header("X-User-Role", role != null ? role : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    /**
     * Writes a JSON 401 Unauthorized response without forwarding to any upstream
     * service.
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"success\":false,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message, LocalDateTime.now());

        var buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * Configuration class required by AbstractGatewayFilterFactory (no config
     * needed).
     */
    public static class Config {
    }
}

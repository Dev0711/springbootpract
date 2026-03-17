package com.ecommerce.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Fallback controller — returns user-friendly responses when downstream
 * services are unavailable (circuit breaker opens).
 *
 * Routes in application.yml forward to these endpoints via:
 *   filters:
 *     - name: CircuitBreaker
 *       args:
 *         fallbackUri: forward:/fallback/auth
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /** Fallback for auth-service being down */
    @RequestMapping("/auth")
    public Mono<ResponseEntity<Map<String, Object>>> authFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of(
                "success",   false,
                "status",    503,
                "error",     "Service Unavailable",
                "message",   "Auth service is temporarily unavailable. Please try again shortly.",
                "timestamp", LocalDateTime.now().toString()
            )
        ));
    }

    /** Fallback for user-service being down */
    @RequestMapping("/user")
    public Mono<ResponseEntity<Map<String, Object>>> userFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of(
                "success",   false,
                "status",    503,
                "error",     "Service Unavailable",
                "message",   "User service is temporarily unavailable. Please try again shortly.",
                "timestamp", LocalDateTime.now().toString()
            )
        ));
    }
}

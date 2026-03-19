package com.ecommerce.apigateway.handler;

import lombok.extern.slf4j.Slf4j;


import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Global error handler for the gateway.
 *
 * Catches all unhandled exceptions and converts them into
 * a consistent JSON error response format.
 *
 * Order(-2) ensures this runs before Spring Boot's DefaultErrorWebExceptionHandler.
 */
@Slf4j
@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();

        // Determine appropriate HTTP status
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred";

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason() != null ? rse.getReason() : ex.getMessage();
        }

        log.error("Gateway error [{}]: {}", status.value(), ex.getMessage());

        // Build JSON error body
        String jsonBody = String.format(
            "{\"success\":false,\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
            status.value(),
            status.getReasonPhrase(),
            message,
            LocalDateTime.now()
        );

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var buffer = response.bufferFactory().wrap(jsonBody.getBytes());
        return response.writeWith(Mono.just(buffer));
    }
}

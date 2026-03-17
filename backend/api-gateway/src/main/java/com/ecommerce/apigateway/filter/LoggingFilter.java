package com.ecommerce.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global logging filter — runs on EVERY request/response passing through the gateway.
 *
 * Logs: method, path, status code, and duration in milliseconds.
 * Order: Ordered.LOWEST_PRECEDENCE - 1 ensures it wraps all other filters.
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.currentTimeMillis();

        String method = request.getMethod().name();
        String path   = request.getPath().value();
        String clientIp = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        log.info("→ Incoming  [{} {}] from IP: {}", method, path, clientIp);

        // Use doFinally to log after the response is sent (reactive — must not block)
        return chain.filter(exchange).doFinally(signalType -> {
            ServerHttpResponse response = exchange.getResponse();
            long duration = System.currentTimeMillis() - startTime;

            Integer statusCode = response.getStatusCode() != null
                    ? response.getStatusCode().value()
                    : 0;

            log.info("← Completed [{} {}] → Status: {} | Duration: {}ms", method, path, statusCode, duration);
        });
    }

    /**
     * Priority: very high (runs first) so we wrap the full request lifecycle.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

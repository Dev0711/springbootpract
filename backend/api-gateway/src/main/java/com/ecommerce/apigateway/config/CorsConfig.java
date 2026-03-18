//package com.ecommerce.apigateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//
//        // Allowed origins
//        corsConfig.setAllowedOrigins(Arrays.asList(
//            "http://localhost:3000",
//            "http://localhost:5173",
//            "http://127.0.0.1:5173"
//        ));
//
//        // Allowed methods
//        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//
//        // Allowed headers
//        corsConfig.setAllowedHeaders(Arrays.asList("*"));
//
//        // Exposed headers
//        corsConfig.setExposedHeaders(Arrays.asList(
//            "Authorization",
//            "X-User-Id",
//            "X-User-Email",
//            "X-User-Role"
//        ));
//
//        corsConfig.setAllowCredentials(true);
//        corsConfig.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // Apply to all routes
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
//}

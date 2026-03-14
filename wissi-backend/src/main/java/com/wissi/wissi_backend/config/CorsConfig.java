package com.wissi.wissi_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "https://detalles-wissi.vercel.app",
                        "http://localhost:3000"
                )
                .allowedMethods(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS"
                )
                .allowedHeaders(
                        "Origin", "Content-Type", "Accept", "Authorization"
                )
                .allowCredentials(true)
                .maxAge(3600);
        
        // También permitir CORS para el endpoint /health
        registry.addMapping("/health")
                .allowedOrigins(
                        "https://detalles-wissi.vercel.app",
                        "http://localhost:3000"
                )
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .allowCredentials(true);
    }
}

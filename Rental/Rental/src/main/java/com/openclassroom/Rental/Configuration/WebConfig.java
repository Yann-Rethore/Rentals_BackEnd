package com.openclassroom.Rental.Configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(@NotNull CorsRegistry registry) {
                    registry.addMapping("/**") // Autorise tous les endpoints
                            .allowedOrigins("http://localhost:4200") // Autorise les requêtes depuis Angular
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                            .allowedHeaders("*") // Autorise tous les en-têtes
                            .allowCredentials(true); // Autorise les cookies ou les informations d'authentification
                }
            };
        }
    }

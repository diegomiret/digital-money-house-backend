package com.dh.digitalMoneyHouse.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String ALLOWED_ORIGINS = "http://localhost:5173";
    public static final String MAX_AGE = "3600";
    public static final String ALLOW_CREDENTIALS = "false";
    public static final String ALLOW_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    public static final String COMMA_SEPARATOR = ",";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(
             authorizeExchangeSpec -> authorizeExchangeSpec
                .anyExchange().permitAll()
                )
               .csrf(csrfSpec -> csrfSpec.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwkSetUri("http://localhost:8080/realms/wallet-realm/protocol/openid-connect/certs")
                        )
                );

        return http.build();
    }

}

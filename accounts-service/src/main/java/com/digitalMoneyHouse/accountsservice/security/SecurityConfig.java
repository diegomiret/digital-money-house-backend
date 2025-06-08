package com.digitalMoneyHouse.accountsservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthConverter());

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/account/user-information")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/transactions")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/activity")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/activity/{transactionId}")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/users/{userId}/accounts")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/register-card")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/cards")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/card/{id}")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/delete-card/{cardNumber}")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/deposit")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/account/send-money")).authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf-> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(baseUrl.concat("/protocol/openid-connect/certs")).build();
    }

}

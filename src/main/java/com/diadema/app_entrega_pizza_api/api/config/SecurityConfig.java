package com.diadema.app_entrega_pizza_api.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF para facilitar testes no Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // LIBERA TUDO: Permite acesso a qualquer endpoint sem senha
                );

        return http.build();
    }
}
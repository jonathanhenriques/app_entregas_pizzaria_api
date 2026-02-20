package com.diadema.app_entrega_pizza_api.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Entrega de Pizza")
                        .version("1.0")
                        .description("API para gerenciamento de pedidos e entregas para motoboys")
                        .contact(new Contact()
                                .name("jonathan Henrique")
                                .email("silva.henriquejonas@gmail.com")));
    }
}
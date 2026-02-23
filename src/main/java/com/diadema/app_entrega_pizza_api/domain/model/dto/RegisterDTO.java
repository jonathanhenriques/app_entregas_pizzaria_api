package com.diadema.app_entrega_pizza_api.domain.model.dto;


import com.diadema.app_entrega_pizza_api.domain.model.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
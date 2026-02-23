package com.diadema.app_entrega_pizza_api.domain.model.enums;


import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }


}
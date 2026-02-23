package com.diadema.app_entrega_pizza_api.domain.model.dto;


import java.math.BigDecimal;

public record GanhosDTO(long totalEntregas, BigDecimal valorEstimado) {
}
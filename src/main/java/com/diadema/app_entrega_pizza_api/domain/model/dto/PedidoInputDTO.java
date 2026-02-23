package com.diadema.app_entrega_pizza_api.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
public record PedidoInputDTO(
        String nomeCliente,
        String endereco,
        String telefone,
        String sabores,
        Double valorTotal,
        String formaPagamento,
        Double distanciaKm,    // Novo campo
        Integer tempoMin       // Novo campo
) {}
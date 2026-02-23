package com.diadema.app_entrega_pizza_api.domain.model.dto;

import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PedidoResumoDTO {
    private UUID id;
    private String nomeCliente;
    private String telefone;
    private String sabores;
    private String notaFiscal;
    private BigDecimal valorTotal;
    private String formaPagamento;
    private StatusPedido status;
    private LocalDateTime dataHoraPedido;

    // Mostramos apenas o essencial do motoboy, não o objeto todo
    private UUID motoboyId;
    private String motoboyNome;


    private String endereco;

    // Adicione este campo NOVO
    private String linkMaps;

    // Se você estiver usando Mapper manual (PedidoAssembler), a lógica fica lá.
    // Se quiser que o Lombok/Jackson gere, pode fazer um getter customizado aqui:

    public String getLinkMaps() {
        if (endereco != null && !endereco.isBlank()) {
            // Gera link universal: abre Google Maps no Android ou Maps no iOS
            // Ex: https://www.google.com/maps/search/?api=1&query=Rua+das+Flores,+100
            String enderecoCodificado = URLEncoder.encode(endereco, StandardCharsets.UTF_8);
            return "https://www.google.com/maps/search/?api=1&query=" + enderecoCodificado;
        }
        return null;
    }
}
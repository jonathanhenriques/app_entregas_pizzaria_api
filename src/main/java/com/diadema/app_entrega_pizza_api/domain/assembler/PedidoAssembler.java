package com.diadema.app_entrega_pizza_api.domain.assembler;


import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.dto.PedidoInputDTO;
import com.diadema.app_entrega_pizza_api.domain.model.dto.PedidoResumoDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoAssembler {
    public Pedido toEntity(PedidoInputDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setNomeCliente(dto.nomeCliente());
        pedido.setEndereco(dto.endereco());
        pedido.setTelefone(dto.telefone());
        pedido.setSabores(dto.sabores());

        // Converte Double para BigDecimal com segurança
        if (dto.valorTotal() != null) {
            pedido.setValorTotal(BigDecimal.valueOf(dto.valorTotal()));
        }

        pedido.setFormaPagamento(dto.formaPagamento());

        // Novos campos de logística da Ponte Inteligente
        pedido.setDistanciaKm(dto.distanciaKm());
        pedido.setTempoEstimadoMin(dto.tempoMin());

        return pedido;
    }

    public PedidoResumoDTO toDTO(Pedido pedido) {
        PedidoResumoDTO dto = new PedidoResumoDTO();
        dto.setId(pedido.getId());
        dto.setNomeCliente(pedido.getNomeCliente());
        dto.setEndereco(pedido.getEndereco());
        dto.setTelefone(pedido.getTelefone());
        dto.setSabores(pedido.getSabores());
        dto.setNotaFiscal(pedido.getNotaFiscal());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setFormaPagamento(pedido.getFormaPagamento());
        dto.setStatus(pedido.getStatus());
        dto.setDataHoraPedido(pedido.getDataHoraPedido());

        if (pedido.getMotoboy() != null) {
            dto.setMotoboyId(pedido.getMotoboy().getId());
            dto.setMotoboyNome(pedido.getMotoboy().getNome());
        }

        return dto;
    }

    public List<PedidoResumoDTO> toCollectionDTO(List<Pedido> pedidos) {
        return pedidos.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
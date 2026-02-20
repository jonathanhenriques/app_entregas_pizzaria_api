package com.diadema.app_entrega_pizza_api.domain.repository;

import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByNomeClienteContainingIgnoreCase(String nomeCliente);

    List<Pedido> findByStatus(StatusPedido status);

    // Agora busca pelo Objeto Motoboy (usando o ID dele por baixo dos panos)
    List<Pedido> findByMotoboyAndStatus(Motoboy motoboy, StatusPedido status);

    List<Pedido> findByMotoboyAndStatusOrderByDataHoraEntregaDesc(Motoboy motoboy, StatusPedido status);
}
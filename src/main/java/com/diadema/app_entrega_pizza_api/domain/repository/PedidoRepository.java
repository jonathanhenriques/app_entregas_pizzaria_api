package com.diadema.app_entrega_pizza_api.domain.repository;

import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByNomeClienteContainingIgnoreCase(String nomeCliente);

    List<Pedido> findByStatus(StatusPedido status);

    // Agora busca pelo Objeto Motoboy (usando o ID dele por baixo dos panos)
    List<Pedido> findByMotoboyAndStatus(Motoboy motoboy, StatusPedido status);

    List<Pedido> findByMotoboyAndStatusOrderByDataHoraEntregaDesc(Motoboy motoboy, StatusPedido status);


    // 1. DASHBOARD FINANCEIRO: Faturamento do dia
    // Útil para: O dono saber quanto ganhou hoje até o momento.
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.dataHoraPedido BETWEEN :inicio AND :fim AND p.status <> 'CANCELADO'")
    BigDecimal faturamentoTotalNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);


    // 2. OPERACIONAL: Pedidos "Atrasados" na Cozinha
    // Útil para: Alerta visual na tela da cozinha. Busca pedidos feitos antes de X horário que ainda não saíram.
    @Query("SELECT p FROM Pedido p WHERE p.status = 'AGUARDANDO_PREPARO' AND p.dataHoraPedido < :horarioLimite")
    List<Pedido> encontrarPedidosAtrasados(@Param("horarioLimite") LocalDateTime horarioLimite);

    // 3. ESTATÍSTICAS: Contagem por Status
    // Útil para: Mostrar cards no topo do dashboard (Ex: "5 na cozinha", "2 em rota").
    long countByStatus(StatusPedido status);

    // 4. RELATÓRIO: Entregas de um Motoboy em um intervalo de datas
    // Útil para: Fechamento de caixa/pagamento do motoboy (Ex: Quanto o João entregou essa semana?).
    List<Pedido> findByMotoboyIdAndDataHoraEntregaBetween(UUID motoboyId, LocalDateTime inicio, LocalDateTime fim);


    // Busca entregas finalizadas de um motoboy em um intervalo de tempo
    List<Pedido> findByMotoboyIdAndStatusAndDataHoraEntregaBetween(
            UUID motoboyId,
            StatusPedido status,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    // Busca a média de minutos por motoboy no dia de hoje
    @Query(value = """
    SELECT m.nome, AVG(DATEDIFF('MINUTE', p.data_hora_pedido, p.data_hora_entrega))
    FROM tb_pedidos p
    JOIN tb_motoboys m ON p.motoboy_id = m.id
    WHERE p.status = 'ENTREGUE' 
    AND CAST(p.data_hora_entrega AS DATE) = CURRENT_DATE
    GROUP BY m.nome
    """, nativeQuery = true)
    List<Object[]> findEficienciaMotoboysHoje();
}
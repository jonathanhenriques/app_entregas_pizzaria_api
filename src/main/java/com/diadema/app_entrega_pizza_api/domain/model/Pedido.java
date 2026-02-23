package com.diadema.app_entrega_pizza_api.domain.model;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Gera UUIDs automaticamente
    private UUID id; // Mudou de Long para UUID

    // Dados Cliente
    private String nomeCliente;
    private String endereco;
    private String telefone;
    private String sabores;
    private String notaFiscal;

    // --- NOVO: DADOS FINANCEIROS ---
    private BigDecimal valorTotal; // BigDecimal é o correto para dinheiro
    private String formaPagamento; // Ex: "Dinheiro (Troco p/ 50)", "Cartão Crédito"

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Column(name = "data_hora_pedido") // Ou data_hora_saida se você preferir
    private LocalDateTime dataHoraPedido;

    @Column(name = "data_hora_entrega")
    private LocalDateTime dataHoraEntrega;

    @ManyToOne
    @JoinColumn(name = "motoboy_id")
    private Motoboy motoboy;

    // Adicione estes campos à classe Pedido
    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "tempo_estimado_min")
    private Integer tempoEstimadoMin;

// Não esqueça de gerar os Getters e Setters para esses campos!

    @PrePersist
    public void prePersist() {
        this.dataHoraPedido = LocalDateTime.now();
        this.status = StatusPedido.AGUARDANDO_PREPARO;
    }
}
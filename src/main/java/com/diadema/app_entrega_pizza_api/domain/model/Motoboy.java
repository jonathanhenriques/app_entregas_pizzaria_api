package com.diadema.app_entrega_pizza_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_motoboys")
public class Motoboy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Gera UUIDs automaticamente
    private UUID id; // Mudou de Long para UUID

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    // Relacionamento: Um motoboy tem vários pedidos
    // @JsonIgnore impede o loop infinito e impede que a lista venha gigante ao consultar apenas o motoboy
    @OneToMany(mappedBy = "motoboy")
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();
}
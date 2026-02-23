package com.diadema.app_entrega_pizza_api.domain.repository;

import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MotoboyRepository extends JpaRepository<Motoboy, UUID> {
    // Método mágico do Spring Data: gera o SQL "SELECT * FROM ... WHERE cpf = ?"
    Optional<Motoboy> findByCpf(String cpf);



    // 1. VALIDAÇÃO RÁPIDA: Verifica existência (Mais leve que buscar o objeto inteiro)
    // Útil para: O validador do Service. O banco responde true/false instantaneamente.
    boolean existsByCpf(String cpf);

    // 2. BUSCA INTELIGENTE: Autocomplete
    // Útil para: Quando o gerente começa a digitar "Jo" e o sistema sugere "João da Silva", "Jonas", etc.
    // O 'ContainingIgnoreCase' faz o LIKE %texto% ignorando maiúsculas.
    Page<Motoboy> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // 3. GESTÃO: Quem está livre? (Query Avançada)
    // Útil para: O sistema sugerir automaticamente motoboys que NÃO têm entrega em andamento.
    @Query("SELECT m FROM Motoboy m WHERE m.id NOT IN (SELECT p.motoboy.id FROM Pedido p WHERE p.status = 'SAIU_PARA_ENTREGA')")
    List<Motoboy> encontrarMotoboysLivres();
}
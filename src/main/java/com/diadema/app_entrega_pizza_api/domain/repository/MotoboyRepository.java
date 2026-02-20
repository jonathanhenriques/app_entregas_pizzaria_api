package com.diadema.app_entrega_pizza_api.domain.repository;

import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoboyRepository extends JpaRepository<Motoboy, Long> {
    // Pode adicionar busca por CPF se precisar:
    // Optional<Motoboy> findByCpf(String cpf);
}
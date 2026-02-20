package com.diadema.app_entrega_pizza_api.domain.controller;

import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import com.diadema.app_entrega_pizza_api.domain.repository.MotoboyRepository;
import com.diadema.app_entrega_pizza_api.domain.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/motoboys")
@CrossOrigin(origins = "*")
public class MotoboyController {

    @Autowired
    private MotoboyRepository motoboyRepository;

    @Autowired
    private PedidoRepository pedidoRepository; // [NOVA INJEÇÃO]

    @GetMapping
    public List<Motoboy> listarTodos() {
        return motoboyRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Motoboy> cadastrar(@RequestBody @Valid Motoboy motoboy) {
        return ResponseEntity.ok(motoboyRepository.save(motoboy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motoboy> buscarPorId(@PathVariable Long id) {
        return motoboyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- NOVO ENDPOINT SOLICITADO ---
    // URL: GET /api/motoboys/{id}/entregas-pendentes
    // Objetivo: O motoboy consulta na própria rota dele o que tem para entregar
    @GetMapping("/{id}/entregas-pendentes")
    public ResponseEntity<?> consultarEntregasDoMotoboy(@PathVariable Long id) {

        // 1. Verifica se o motoboy existe
        Optional<Motoboy> motoboyOpt = motoboyRepository.findById(id);

        if (motoboyOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Motoboy não encontrado");
        }

        // 2. Busca na tabela de pedidos usando o Objeto Motoboy
        List<Pedido> entregas = pedidoRepository.findByMotoboyAndStatus(
                motoboyOpt.get(),
                StatusPedido.SAIU_PARA_ENTREGA
        );

        return ResponseEntity.ok(entregas);
    }
}

package com.diadema.app_entrega_pizza_api.domain.controller;
import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import com.diadema.app_entrega_pizza_api.domain.repository.MotoboyRepository;
import com.diadema.app_entrega_pizza_api.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MotoboyRepository motoboyRepository; // Injeção do repo do motoboy

    // --- CRUD BÁSICO ---
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/novo")
    public ResponseEntity<Pedido> fazerPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    // --- LÓGICA DO MOTOBOY ---

    @GetMapping("/disponiveis-para-entrega")
    public List<Pedido> listarDisponiveis() {
        return pedidoRepository.findByStatus(StatusPedido.AGUARDANDO_PREPARO);
    }

    // A MÁGICA ACONTECE AQUI: Vincula Motoboy ID ao Pedido ID
    @PutMapping("/pegar-entrega/{idPedido}/{idMotoboy}")
    public ResponseEntity<?> pegarParaEntrega(@PathVariable Long idPedido, @PathVariable Long idMotoboy) {

        // 1. Constraint: O Motoboy existe?
        Optional<Motoboy> motoboyOpt = motoboyRepository.findById(idMotoboy);
        if (motoboyOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Motoboy inválido ou não encontrado.");
        }

        // 2. Constraint: O Pedido existe?
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Pedido não encontrado.");
        }

        Pedido pedido = pedidoOpt.get();

        // 3. Constraint: O Pedido está livre?
        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PREPARO) {
            return ResponseEntity.badRequest().body("Pedido não está disponível. Status: " + pedido.getStatus());
        }

        // Tudo certo: Faz o vínculo
        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.setMotoboy(motoboyOpt.get()); // Passa o OBJETO Motoboy completo

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    // Busca entregas pelo ID do motoboy
    @GetMapping("/minhas-entregas-atuais/{idMotoboy}")
    public ResponseEntity<?> minhasEntregasAtuais(@PathVariable Long idMotoboy) {
        Optional<Motoboy> motoboyOpt = motoboyRepository.findById(idMotoboy);
        if (motoboyOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<Pedido> lista = pedidoRepository.findByMotoboyAndStatus(motoboyOpt.get(), StatusPedido.SAIU_PARA_ENTREGA);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/finalizar-entrega/{idPedido}")
    public ResponseEntity<?> finalizarEntrega(@PathVariable Long idPedido) {
        return pedidoRepository.findById(idPedido).map(pedido -> {
            if (pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA) {
                // Aqui poderíamos lançar erro, mas vamos permitir finalizar
            }
            pedido.setStatus(StatusPedido.ENTREGUE);
            pedido.setDataHoraEntrega(LocalDateTime.now());
            return ResponseEntity.ok(pedidoRepository.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historico/{idMotoboy}")
    public ResponseEntity<?> historicoMotoboy(@PathVariable Long idMotoboy) {
        Optional<Motoboy> motoboyOpt = motoboyRepository.findById(idMotoboy);
        if (motoboyOpt.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pedidoRepository.findByMotoboyAndStatusOrderByDataHoraEntregaDesc(motoboyOpt.get(), StatusPedido.ENTREGUE));
    }
}
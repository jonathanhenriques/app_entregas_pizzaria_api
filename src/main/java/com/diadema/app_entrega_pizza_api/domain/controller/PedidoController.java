package com.diadema.app_entrega_pizza_api.domain.controller;
import com.diadema.app_entrega_pizza_api.domain.assembler.PedidoAssembler;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.dto.PedidoInputDTO;
import com.diadema.app_entrega_pizza_api.domain.model.dto.PedidoResumoDTO;
import com.diadema.app_entrega_pizza_api.domain.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
//@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService; // Agora usamos Service, não Repository!

    @Autowired
    private PedidoAssembler assembler;

    // NOVO: Paginação (Ex: /api/pedidos?page=0&size=10)
    @GetMapping
    public Page<PedidoResumoDTO> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        Page<Pedido> pedidosPage = pedidoService.listarTodos(pageable);

        // Convertendo a página de Entidades para página de DTOs
        return pedidosPage.map(pedido -> assembler.toDTO(pedido));
    }

    @GetMapping("/{id}")
    public PedidoResumoDTO buscarPorId(@PathVariable UUID id) {
        return assembler.toDTO(pedidoService.buscarOuFalhar(id));
    }

    @PostMapping("/novo")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResumoDTO fazerPedido(@RequestBody @Valid PedidoInputDTO pedidoInput) {
        Pedido pedido = assembler.toEntity(pedidoInput);
        pedido = pedidoService.emitir(pedido);
        return assembler.toDTO(pedido);
    }

    // --- FLUXO DO MOTOBOY ---

    @GetMapping("/disponiveis-para-entrega")
    public List<PedidoResumoDTO> listarDisponiveis() {
        return assembler.toCollectionDTO(pedidoService.listarDisponiveis());
    }

    @PutMapping("/pegar-entrega/{idPedido}/{idMotoboy}")
    public PedidoResumoDTO pegarParaEntrega(@PathVariable UUID idPedido, @PathVariable UUID idMotoboy) {
        Pedido pedido = pedidoService.pegarParaEntrega(idPedido, idMotoboy);
        return assembler.toDTO(pedido);
    }

    @GetMapping("/minhas-entregas-atuais/{idMotoboy}")
    public List<PedidoResumoDTO> minhasEntregasAtuais(@PathVariable UUID idMotoboy) {
        return assembler.toCollectionDTO(pedidoService.listarEntregasAtuais(idMotoboy));
    }

    @PutMapping("/finalizar-entrega/{idPedido}")
    public PedidoResumoDTO finalizarEntrega(@PathVariable UUID idPedido) {
        Pedido pedido = pedidoService.finalizarEntrega(idPedido);
        return assembler.toDTO(pedido);
    }

    @GetMapping("/historico/{idMotoboy}")
    public List<PedidoResumoDTO> historicoMotoboy(@PathVariable UUID idMotoboy) {
        return assembler.toCollectionDTO(pedidoService.listarHistorico(idMotoboy));
    }


    @PutMapping("/devolver/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 (Sucesso sem corpo)
    public void devolverPedido(@PathVariable UUID id) {
        pedidoService.devolverPedido(id);
    }
}
package com.diadema.app_entrega_pizza_api.domain.controller;

import com.diadema.app_entrega_pizza_api.domain.assembler.PedidoAssembler;
import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.dto.GanhosDTO;
import com.diadema.app_entrega_pizza_api.domain.model.dto.PedidoResumoDTO;
import com.diadema.app_entrega_pizza_api.domain.service.MotoboyService;
import com.diadema.app_entrega_pizza_api.domain.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import com.diadema.app_entrega_pizza_api.domain.repository.PedidoRepository; // Importe o repo aqui para agilizar, ou crie no Servic

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/motoboys")
//@CrossOrigin(origins = "*")
public class MotoboyController {

    @Autowired
    private MotoboyService motoboyService;

    @Autowired
    private PedidoService pedidoService; // Único ponto de contato para lógica de pedidos

    @Autowired
    private PedidoAssembler pedidoAssembler;

    @GetMapping
    public Page<Motoboy> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        return motoboyService.listarTodos(pageable);
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Motoboy cadastrar(@RequestBody @Valid Motoboy motoboy) {
        return motoboyService.salvar(motoboy);
    }

    @GetMapping("/{id}")
    public Motoboy buscarPorId(@PathVariable UUID id) {
        return motoboyService.buscarOuFalhar(id);
    }

    // --- ENDPOINT OTIMIZADO ---
    // Antes: Fazia a busca manual no Repository [6]
    // Agora: Chama o PedidoService que já tem essa regra pronta e testada [7]
    @GetMapping("/{id}/entregas-pendentes")
    public List<PedidoResumoDTO> consultarEntregasDoMotoboy(@PathVariable UUID id) {
        // O Service já valida se o motoboy existe e busca as entregas
        return pedidoAssembler.toCollectionDTO(pedidoService.listarEntregasAtuais(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        motoboyService.excluir(id);
    }

    @GetMapping("/{id}/historico-hoje")
    public List<PedidoResumoDTO> historicoHoje(@PathVariable UUID id) {
        // O Controller não sabe "que dia é hoje", ele apenas pede ao serviço
        List<Pedido> pedidos = pedidoService.listarEntregasHoje(id);
        return pedidoAssembler.toCollectionDTO(pedidos);
    }

    @GetMapping("/{id}/historico-semana")
    public List<PedidoResumoDTO> historicoSemana(@PathVariable UUID id) {
        List<Pedido> pedidos = pedidoService.listarEntregasSemana(id);
        return pedidoAssembler.toCollectionDTO(pedidos);
    }

    @GetMapping("/{id}/historico-mes")
    public List<PedidoResumoDTO> historicoMes(@PathVariable UUID id) {
        List<Pedido> pedidos = pedidoService.listarEntregasMes(id);
        return pedidoAssembler.toCollectionDTO(pedidos);
    }

    @GetMapping("/{id}/ganhos-dia")
    public GanhosDTO consultarGanhosDia(@PathVariable UUID id) {
        return pedidoService.calcularGanhosDoDia(id);
    }
}
package com.diadema.app_entrega_pizza_api.domain.service;

import com.diadema.app_entrega_pizza_api.domain.exception.RegraNegocioException;
import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.model.Pedido;
import com.diadema.app_entrega_pizza_api.domain.model.dto.GanhosDTO;
import com.diadema.app_entrega_pizza_api.domain.model.enums.StatusPedido;
import com.diadema.app_entrega_pizza_api.domain.repository.MotoboyRepository;
import com.diadema.app_entrega_pizza_api.domain.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MotoboyRepository motoboyRepository;

    @Autowired
    private MotoboyService motoboyService;

    public Page<Pedido> listarTodos(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    public Pedido buscarOuFalhar(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
    }

    @Transactional
    public Pedido emitir(Pedido pedido) {
        // Aqui poderiam entrar regras como: verificar se a pizzaria está aberta, validar cep, etc.
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido pegarParaEntrega(UUID idPedido, UUID idMotoboy) {
        Pedido pedido = buscarOuFalhar(idPedido);

        Motoboy motoboy = motoboyRepository.findById(idMotoboy)
                .orElseThrow(() -> new EntityNotFoundException("Motoboy não encontrado"));

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PREPARO) {
            throw new RegraNegocioException(
                    String.format("Pedido %d não está disponível para entrega. Status atual: %s", idPedido, pedido.getStatus())
            );
        }

        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.setMotoboy(motoboy);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido finalizarEntrega(UUID idPedido) {
        Pedido pedido = buscarOuFalhar(idPedido);

        if (pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA) {
            throw new RegraNegocioException("Não é possível finalizar um pedido que não está em rota.");
        }

        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDataHoraEntrega(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    // Métodos de consulta simples podem delegar direto ou ter lógica de filtro
    public List<Pedido> listarDisponiveis() {
        return pedidoRepository.findByStatus(StatusPedido.AGUARDANDO_PREPARO);
    }

    public List<Pedido> listarEntregasAtuais(UUID idMotoboy) {
        Motoboy motoboy = motoboyRepository.findById(idMotoboy)
                .orElseThrow(() -> new EntityNotFoundException("Motoboy não encontrado"));
        return pedidoRepository.findByMotoboyAndStatus(motoboy, StatusPedido.SAIU_PARA_ENTREGA);
    }

    public List<Pedido> listarHistorico(UUID idMotoboy) {
        Motoboy motoboy = motoboyRepository.findById(idMotoboy)
                .orElseThrow(() -> new EntityNotFoundException("Motoboy não encontrado"));
        return pedidoRepository.findByMotoboyAndStatusOrderByDataHoraEntregaDesc(motoboy, StatusPedido.ENTREGUE);
    }


    public List<Pedido> listarEntregasHoje(UUID motoboyId) {
        // Valida se motoboy existe antes de buscar (Segurança)
        motoboyService.buscarOuFalhar(motoboyId);

        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = LocalDate.now().atTime(LocalTime.MAX);

        return pedidoRepository.findByMotoboyIdAndStatusAndDataHoraEntregaBetween(
                motoboyId, StatusPedido.ENTREGUE, inicioDia, fimDia);
    }

    public List<Pedido> listarEntregasSemana(UUID motoboyId) {
        motoboyService.buscarOuFalhar(motoboyId);

        LocalDateTime inicioSemana = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        LocalDateTime agora = LocalDateTime.now();

        return pedidoRepository.findByMotoboyIdAndStatusAndDataHoraEntregaBetween(
                motoboyId, StatusPedido.ENTREGUE, inicioSemana, agora);
    }

    public List<Pedido> listarEntregasMes(UUID motoboyId) {
        motoboyService.buscarOuFalhar(motoboyId);

        LocalDateTime inicioMes = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay();
        LocalDateTime agora = LocalDateTime.now();

        return pedidoRepository.findByMotoboyIdAndStatusAndDataHoraEntregaBetween(
                motoboyId, StatusPedido.ENTREGUE, inicioMes, agora);
    }



    @Transactional
    public void devolverPedido(UUID idPedido) {
        Pedido pedido = buscarOuFalhar(idPedido);

        // Só pode devolver se estiver na rua
        if (pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA) {
            throw new RegraNegocioException("Apenas pedidos em rota de entrega podem ser devolvidos.");
        }

        // Volta para a fila da cozinha e remove o motoboy
        pedido.setStatus(StatusPedido.AGUARDANDO_PREPARO);
        pedido.setMotoboy(null);

        pedidoRepository.save(pedido);
    }

    public GanhosDTO calcularGanhosDoDia(UUID idMotoboy) {
        // Valida se motoboy existe
        motoboyService.buscarOuFalhar(idMotoboy);

        // Reutilizamos o método que já criamos de "Entregas de Hoje"
        List<Pedido> entregasHoje = listarEntregasHoje(idMotoboy);

        long quantidade = entregasHoje.size();

        // REGRA DE NEGÓCIO: Vamos supor que ele ganha R$ 5,00 fixo por entrega.
        // Em um sistema real, isso viria de uma configuração no banco.
        BigDecimal valorPorEntrega = new BigDecimal("5.00");
        BigDecimal total = valorPorEntrega.multiply(new BigDecimal(quantidade));

        return new GanhosDTO(quantidade, total);
    }
}
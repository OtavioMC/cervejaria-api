package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.Pedido;
import com.example.cervejaria_api.model.ItemPedido;
import com.example.cervejaria_api.model.Garcom;
import com.example.cervejaria_api.repository.PedidoRepository;
import com.example.cervejaria_api.repository.GarcomRepository;
import com.example.cervejaria_api.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private GarcomRepository garcomRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    /**
     * Lista todos os pedidos
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllWithGarcomAndItens();
    }

    /**
     * Busca pedido por ID
     */
    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    /**
     * Lista pedidos por status
     */
    public List<Pedido> listarPorStatus(String status) {
        return pedidoRepository.findByStatusOrderByDataHoraPedidoDesc(status);
    }

    /**
     * Lista pedidos por mesa
     */
    public List<Pedido> listarPorMesa(Integer numeroMesa) {
        return pedidoRepository.findByNumeroMesaOrderByDataHoraPedidoDesc(numeroMesa);
    }

    /**
     * Lista pedidos por garçom
     */
    public List<Pedido> listarPorGarcom(Integer garcomId) {
        return pedidoRepository.findByGarcomId(garcomId);
    }

    /**
     * Lista pedidos em período
     */
    public List<Pedido> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByPeriodo(inicio, fim);
    }

    /**
     * Lista pedidos do dia
     */
    public List<Pedido> listarPedidosHoje() {
        return pedidoRepository.findPedidosHoje();
    }

    /**
     * Lista mesas com pedidos abertos
     */
    public List<Integer> listarMesasComPedidosAbertos() {
        return pedidoRepository.findMesasComPedidosAbertos();
    }

    /**
     * Busca último pedido da mesa
     */
    public Optional<Pedido> buscarUltimoPedidoMesa(Integer numeroMesa) {
        List<Pedido> pedidos = pedidoRepository.findUltimoPedidoMesa(numeroMesa);
        return pedidos.isEmpty() ? Optional.empty() : Optional.of(pedidos.get(0));
    }

    /**
     * Conta pedidos por status
     */
    public long contarPorStatus(String status) {
        return pedidoRepository.countByStatus(status);
    }

    /**
     * Calcula total de vendas em período
     */
    public BigDecimal calcularTotalVendasPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        BigDecimal total = pedidoRepository.calcularTotalVendasPeriodo(inicio, fim);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Calcula total de vendas do garçom
     */
    public BigDecimal calcularTotalVendasGarcom(Integer garcomId) {
        BigDecimal total = pedidoRepository.calcularTotalVendasGarcom(garcomId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Cria novo pedido
     */
    @Transactional
    public Pedido criar(Pedido pedido) {
        validarPedido(pedido);
        
        // Busca e valida o garçom
        Garcom garcom = garcomRepository.findById(pedido.getGarcom().getId())
                .orElseThrow(() -> new RuntimeException("Garçom não encontrado"));
        
        if (!garcom.getAtivo()) {
            throw new RuntimeException("Garçom inativo");
        }
        
        pedido.setGarcom(garcom);
        
        if (pedido.getDataCriacao() == null) {
            pedido.setDataCriacao(LocalDate.now());
        }
        
        if (pedido.getDataHoraPedido() == null) {
            pedido.setDataHoraPedido(LocalDateTime.now());
        }
        
        if (pedido.getStatus() == null || pedido.getStatus().isEmpty()) {
            pedido.setStatus("ABERTO");
        }
        
        if (pedido.getValorTotal() == null) {
            pedido.setValorTotal(BigDecimal.ZERO);
        }
        
        return pedidoRepository.save(pedido);
    }

    /**
     * Atualiza pedido existente
     */
    @Transactional
    public Pedido atualizar(Integer id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        validarPedido(pedidoAtualizado);

        // Atualiza os campos
        pedidoExistente.setNumeroMesa(pedidoAtualizado.getNumeroMesa());
        
        if (pedidoAtualizado.getGarcom() != null && pedidoAtualizado.getGarcom().getId() != null) {
            Garcom garcom = garcomRepository.findById(pedidoAtualizado.getGarcom().getId())
                    .orElseThrow(() -> new RuntimeException("Garçom não encontrado"));
            pedidoExistente.setGarcom(garcom);
        }
        
        pedidoExistente.setStatus(pedidoAtualizado.getStatus());
        pedidoExistente.setObservacoes(pedidoAtualizado.getObservacoes());
        pedidoExistente.setDataUltimaAlteracao(LocalDate.now());

        // Se for pago, registra data de pagamento
        if ("PAGO".equals(pedidoAtualizado.getStatus()) && pedidoExistente.getDataHoraPagamento() == null) {
            pedidoExistente.setDataHoraPagamento(LocalDateTime.now());
        }

        return pedidoRepository.save(pedidoExistente);
    }

    /**
     * Deleta pedido
     */
    @Transactional
    public void deletar(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    /**
     * Adiciona item ao pedido
     */
    @Transactional
    public Pedido adicionarItem(Integer pedidoId, ItemPedido item) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        item.setPedido(pedido);
        pedido.adicionarItem(item);
        
        return pedidoRepository.save(pedido);
    }

    /**
     * Remove item do pedido
     */
    @Transactional
    public Pedido removerItem(Integer pedidoId, Integer itemId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        
        pedido.removerItem(item);
        itemPedidoRepository.delete(item);
        
        return pedidoRepository.save(pedido);
    }

    /**
     * Altera status do pedido
     */
    @Transactional
    public Pedido alterarStatus(Integer id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(novoStatus);
        
        if ("PAGO".equals(novoStatus) && pedido.getDataHoraPagamento() == null) {
            pedido.setDataHoraPagamento(LocalDateTime.now());
        }
        
        pedido.setDataUltimaAlteracao(LocalDate.now());
        
        return pedidoRepository.save(pedido);
    }

    /**
     * Valida dados do pedido
     */
    private void validarPedido(Pedido pedido) {
        if (pedido.getNumeroMesa() == null || pedido.getNumeroMesa() <= 0) {
            throw new RuntimeException("Número da mesa é obrigatório e deve ser maior que zero");
        }

        if (pedido.getGarcom() == null || pedido.getGarcom().getId() == null) {
            throw new RuntimeException("Garçom é obrigatório");
        }
    }

    /**
     * Busca todos os pedidos
     */
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca pedido por ID
     */
    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
    }

    /**
     * Busca pedido com itens
     */
    public Pedido findByIdWithItens(Integer id) {
        Pedido pedido = pedidoRepository.findByIdWithItens(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        return pedido;
    }

    /**
     * Busca pedidos por status
     */
    public List<Pedido> findByStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    /**
     * Busca pedidos por mesa
     */
    public List<Pedido> findByMesa(Integer numeroMesa) {
        return pedidoRepository.findByNumeroMesa(numeroMesa);
    }

    /**
     * Busca pedidos abertos de uma mesa
     */
    public List<Pedido> findAbertosbyMesa(Integer numeroMesa) {
        return pedidoRepository.findByNumeroMesaAndStatus(numeroMesa, "ABERTO");
    }

    /**
     * Busca pedidos por garçom
     */
    public List<Pedido> findByGarcom(Integer garcomId) {
        return pedidoRepository.findByGarcomId(garcomId);
    }

    /**
     * Busca pedidos por período
     */
    public List<Pedido> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByPeriodo(inicio, fim);
    }

    /**
     * Conta pedidos por status
     */
    public Long countByStatus(String status) {
        return pedidoRepository.countByStatus(status);
    }

    /**
     * Cria novo pedido
     */
    @Transactional
    public Pedido create(Pedido pedido) {
        if (pedido.getNumeroMesa() == null) {
            throw new RuntimeException("Número da mesa é obrigatório");
        }
        if (pedido.getGarcom() == null || pedido.getGarcom().getId() == null) {
            throw new RuntimeException("Garçom é obrigatório");
        }

        // Verifica se garçom existe
        Garcom garcom = garcomRepository.findById(pedido.getGarcom().getId())
                .orElseThrow(() -> new RuntimeException("Garçom não encontrado"));

        if (!garcom.getAtivo()) {
            throw new RuntimeException("Garçom inativo");
        }

        pedido.setGarcom(garcom);
        pedido.setStatus("ABERTO");
        pedido.setDataHoraPedido(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    /**
     * Atualiza pedido
     */
    @Transactional
    public Pedido update(Integer id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = findById(id);

        if (pedidoAtualizado.getNumeroMesa() != null) {
            pedidoExistente.setNumeroMesa(pedidoAtualizado.getNumeroMesa());
        }
        if (pedidoAtualizado.getObservacoes() != null) {
            pedidoExistente.setObservacoes(pedidoAtualizado.getObservacoes());
        }

        return pedidoRepository.save(pedidoExistente);
    }

    /**
     * Fecha pedido
     */
    @Transactional
    public Pedido fechar(Integer id) {
        Pedido pedido = findById(id);

        if (!"ABERTO".equals(pedido.getStatus())) {
            throw new RuntimeException("Apenas pedidos abertos podem ser fechados");
        }

        pedido.setStatus("FECHADO");
        return pedidoRepository.save(pedido);
    }

    /**
     * Paga pedido
     */
    @Transactional
    public Pedido pagar(Integer id) {
        Pedido pedido = findById(id);

        if ("PAGO".equals(pedido.getStatus())) {
            throw new RuntimeException("Pedido já foi pago");
        }

        pedido.setStatus("PAGO");
        pedido.setDataHoraPagamento(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    /**
     * Cancela pedido
     */
    @Transactional
    public Pedido cancelar(Integer id) {
        Pedido pedido = findById(id);

        if ("PAGO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível cancelar um pedido já pago");
        }

        pedido.setStatus("CANCELADO");
        return pedidoRepository.save(pedido);
    }

    /**
     * Deleta pedido
     */
    @Transactional
    public void delete(Integer id) {
        Pedido pedido = findById(id);

        if ("PAGO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível excluir um pedido pago");
        }

        pedidoRepository.deleteById(id);
    }
}
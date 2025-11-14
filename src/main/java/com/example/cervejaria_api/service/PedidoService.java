package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.*;
import com.example.cervejaria_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private GarcomRepository garcomRepository;

    @Autowired
    private CaixaRepository caixaRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
    }

    public List<Pedido> buscarPorMesa(Integer numeroMesa) {
        return pedidoRepository.findByNumeroMesa(numeroMesa);
    }

    public List<Pedido> buscarPorGarcom(Integer garcomId) {
        return pedidoRepository.findByGarcomId(garcomId);
    }

    public List<Pedido> buscarPorStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    @Transactional
    public Pedido criar(Pedido pedido) {
        validarPedido(pedido);

        if (pedido.getGarcom() != null && pedido.getGarcom().getId() != null) {
            Garcom garcom = garcomRepository.findById(pedido.getGarcom().getId())
                    .orElseThrow(() -> new RuntimeException("Garçom não encontrado"));
            pedido.setGarcom(garcom);
        }

        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedido.setValorTotal(BigDecimal.ZERO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedidoSalvo);
                adicionarItem(pedidoSalvo.getId(), item);
            }
        }

        return buscarPorId(pedidoSalvo.getId());
    }

    @Transactional
    public ItemPedido adicionarItem(Integer pedidoId, ItemPedido item) {
        Pedido pedido = buscarPorId(pedidoId);

        if (!"PENDENTE".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível adicionar itens a um pedido que não está pendente");
        }

        Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getEstoque() < item.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        item.setPedido(pedido);
        item.setProduto(produto);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));

        ItemPedido itemSalvo = itemPedidoRepository.save(item);

        produto.setEstoque(produto.getEstoque() - item.getQuantidade());
        produtoRepository.save(produto);

        atualizarValorTotal(pedidoId);

        return itemSalvo;
    }

    @Transactional
    public void removerItem(Integer pedidoId, Integer itemId) {
        Pedido pedido = buscarPorId(pedidoId);

        if (!"PENDENTE".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível remover itens de um pedido que não está pendente");
        }

        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (!item.getPedido().getId().equals(pedidoId)) {
            throw new RuntimeException("Item não pertence ao pedido especificado");
        }

        Produto produto = item.getProduto();
        produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        produtoRepository.save(produto);

        itemPedidoRepository.delete(item);

        atualizarValorTotal(pedidoId);
    }

    @Transactional
    public Pedido atualizar(Integer id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = buscarPorId(id);

        if (!"PENDENTE".equals(pedidoExistente.getStatus())) {
            throw new RuntimeException("Apenas pedidos pendentes podem ser atualizados");
        }

        if (pedidoAtualizado.getObservacoes() != null) {
            pedidoExistente.setObservacoes(pedidoAtualizado.getObservacoes());
        }

        pedidoExistente.setDataUltimaAlteracao(LocalDateTime.now());

        return pedidoRepository.save(pedidoExistente);
    }

    @Transactional
    public Pedido confirmar(Integer id) {
        Pedido pedido = buscarPorId(id);

        if (!"PENDENTE".equals(pedido.getStatus())) {
            throw new RuntimeException("Apenas pedidos pendentes podem ser confirmados");
        }

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível confirmar um pedido sem itens");
        }

        pedido.setStatus("CONFIRMADO");
        pedido.setDataUltimaAlteracao(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido entregar(Integer id) {
        Pedido pedido = buscarPorId(id);

        if (!"CONFIRMADO".equals(pedido.getStatus())) {
            throw new RuntimeException("Apenas pedidos confirmados podem ser entregues");
        }

        pedido.setStatus("ENTREGUE");
        pedido.setDataUltimaAlteracao(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelar(Integer id, String motivo) {
        Pedido pedido = buscarPorId(id);

        if ("PAGO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível cancelar um pedido já pago");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus("CANCELADO");
        pedido.setObservacoes(pedido.getObservacoes() != null 
            ? pedido.getObservacoes() + "\nMotivo do cancelamento: " + motivo
            : "Motivo do cancelamento: " + motivo);
        pedido.setDataUltimaAlteracao(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void excluir(Integer id) {
        Pedido pedido = buscarPorId(id);

        if (!"CANCELADO".equals(pedido.getStatus())) {
            throw new RuntimeException("Apenas pedidos cancelados podem ser excluídos");
        }

        pedidoRepository.delete(pedido);
    }

    public BigDecimal calcularValorTotal(Integer pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        return pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void atualizarValorTotal(Integer pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        BigDecimal valorTotal = calcularValorTotal(pedidoId);
        pedido.setValorTotal(valorTotal);
        pedidoRepository.save(pedido);
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getNumeroMesa() == null) {
            throw new RuntimeException("Número da mesa é obrigatório");
        }
    }

    public List<Pedido> buscarPedidosAbertos() {
        List<Pedido> pendentes = pedidoRepository.findByStatus("PENDENTE");
        List<Pedido> confirmados = pedidoRepository.findByStatus("CONFIRMADO");
        List<Pedido> entregues = pedidoRepository.findByStatus("ENTREGUE");
        pendentes.addAll(confirmados);
        pendentes.addAll(entregues);
        return pendentes;
    }

    public List<Pedido> buscarPorMesaEStatus(Integer numeroMesa, String status) {
        return pedidoRepository.findByNumeroMesaAndStatus(numeroMesa, status);
    }

    @Transactional
    public Pedido pagarPedido(Integer pedidoId, Integer caixaId, String formaPagamento) {
        Pedido pedido = buscarPorId(pedidoId);

        if ("PAGO".equals(pedido.getStatus())) {
            throw new RuntimeException("Pedido já foi pago");
        }

        if ("CANCELADO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível pagar um pedido cancelado");
        }

        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado"));

        if (!"ABERTO".equals(caixa.getStatus())) {
            throw new RuntimeException("Caixa não está aberto");
        }

        pedido.setStatus("PAGO");
        pedido.setDataUltimaAlteracao(LocalDateTime.now());

        caixa.setTotalVendido(caixa.getTotalVendido().add(pedido.getValorTotal()));
        caixaRepository.save(caixa);

        return pedidoRepository.save(pedido);
    }
}
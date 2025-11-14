package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.ItemPedido;
import com.example.cervejaria_api.model.Pedido;
import com.example.cervejaria_api.model.Produto;
import com.example.cervejaria_api.repository.ItemPedidoRepository;
import com.example.cervejaria_api.repository.PedidoRepository;
import com.example.cervejaria_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Busca todos os itens de pedido
     */
    public List<ItemPedido> findAll() {
        return itemPedidoRepository.findAll();
    }

    /**
     * Busca item por ID
     */
    public ItemPedido findById(Integer id) {
        return itemPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de pedido não encontrado com ID: " + id));
    }

    /**
     * Busca itens de um pedido específico
     */
    public List<ItemPedido> findByPedidoId(Integer pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }

    /**
     * Busca itens de um pedido com produto (otimizado)
     */
    public List<ItemPedido> findByPedidoIdWithProduto(Integer pedidoId) {
        return itemPedidoRepository.findByPedidoIdWithProduto(pedidoId);
    }

    /**
     * Busca itens por produto
     */
    public List<ItemPedido> findByProdutoId(Integer produtoId) {
        return itemPedidoRepository.findByProdutoId(produtoId);
    }

    /**
     * Cria novo item de pedido
     */
    @Transactional
    public ItemPedido create(ItemPedido itemPedido) {
        // Validações
        if (itemPedido.getPedido() == null || itemPedido.getPedido().getId() == null) {
            throw new RuntimeException("Pedido é obrigatório");
        }
        if (itemPedido.getProduto() == null || itemPedido.getProduto().getId() == null) {
            throw new RuntimeException("Produto é obrigatório");
        }
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        // Carrega pedido e produto
        Pedido pedido = pedidoRepository.findById(itemPedido.getPedido().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        // Verifica se pedido está aberto
        if (!"ABERTO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível adicionar itens a um pedido " + pedido.getStatus().toLowerCase());
        }
        
        Produto produto = produtoRepository.findById(itemPedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Verifica se produto está ativo
        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto inativo: " + produto.getNome());
        }

        // Verifica estoque
        if (produto.getEstoque() < itemPedido.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        // Define valores
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setPrecoUnitario(produto.getPreco());
        
        // Calcula subtotal
        BigDecimal subtotal = produto.getPreco()
                .multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
        itemPedido.setSubtotal(subtotal);

        // Atualiza estoque
        produto.setEstoque(produto.getEstoque() - itemPedido.getQuantidade());
        produtoRepository.save(produto);

        // Salva item
        ItemPedido savedItem = itemPedidoRepository.save(itemPedido);

        // Atualiza total do pedido
        atualizarTotalPedido(pedido.getId());

        return savedItem;
    }

    /**
     * Atualiza item de pedido
     */
    @Transactional
    public ItemPedido update(Integer id, ItemPedido itemPedidoAtualizado) {
        ItemPedido itemExistente = findById(id);

        // Verifica se pedido está aberto
        Pedido pedido = itemExistente.getPedido();
        if (!"ABERTO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível atualizar itens de um pedido " + pedido.getStatus().toLowerCase());
        }

        if (itemPedidoAtualizado.getQuantidade() == null || itemPedidoAtualizado.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        // Calcula diferença de estoque
        Integer diferencaQuantidade = itemPedidoAtualizado.getQuantidade() - itemExistente.getQuantidade();
        
        Produto produto = itemExistente.getProduto();
        
        // Verifica se produto está ativo
        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto inativo: " + produto.getNome());
        }

        // Verifica estoque se aumentou quantidade
        if (diferencaQuantidade > 0 && produto.getEstoque() < diferencaQuantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        // Atualiza estoque
        produto.setEstoque(produto.getEstoque() - diferencaQuantidade);
        produtoRepository.save(produto);

        // Atualiza item
        itemExistente.setQuantidade(itemPedidoAtualizado.getQuantidade());
        
        BigDecimal subtotal = itemExistente.getPrecoUnitario()
                .multiply(BigDecimal.valueOf(itemExistente.getQuantidade()));
        itemExistente.setSubtotal(subtotal);

        // Atualiza observações se fornecidas
        if (itemPedidoAtualizado.getObservacoes() != null) {
            itemExistente.setObservacoes(itemPedidoAtualizado.getObservacoes());
        }

        ItemPedido updated = itemPedidoRepository.save(itemExistente);

        // Atualiza total do pedido
        atualizarTotalPedido(itemExistente.getPedido().getId());

        return updated;
    }

    /**
     * Deleta item de pedido
     */
    @Transactional
    public void delete(Integer id) {
        ItemPedido item = findById(id);
        
        // Verifica se pedido está aberto
        Pedido pedido = item.getPedido();
        if (!"ABERTO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível remover itens de um pedido " + pedido.getStatus().toLowerCase());
        }
        
        // Devolve ao estoque
        Produto produto = item.getProduto();
        produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        produtoRepository.save(produto);

        Integer pedidoId = item.getPedido().getId();
        
        itemPedidoRepository.deleteById(id);

        // Atualiza total do pedido
        atualizarTotalPedido(pedidoId);
    }

    /**
     * Deleta todos os itens de um pedido
     */
    @Transactional
    public void deleteByPedidoId(Integer pedidoId) {
        // Verifica se pedido está aberto
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (!"ABERTO".equals(pedido.getStatus())) {
            throw new RuntimeException("Não é possível remover itens de um pedido " + pedido.getStatus().toLowerCase());
        }

        List<ItemPedido> itens = findByPedidoId(pedidoId);
        
        // Devolve ao estoque
        for (ItemPedido item : itens) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        itemPedidoRepository.deleteByPedidoId(pedidoId);

        // Atualiza total do pedido
        atualizarTotalPedido(pedidoId);
    }

    /**
     * Conta itens de um pedido
     */
    public Long countByPedidoId(Integer pedidoId) {
        return itemPedidoRepository.countByPedidoId(pedidoId);
    }

    /**
     * Busca produtos mais vendidos
     */
    public List<Object[]> findProdutosMaisVendidos() {
        return itemPedidoRepository.findProdutosMaisVendidos();
    }

    /**
     * Busca quantidade total vendida de um produto
     */
    public Long countTotalQuantidadeProduto(Integer produtoId) {
        Long total = itemPedidoRepository.countTotalQuantidadeProduto(produtoId);
        return total != null ? total : 0L;
    }

    /**
     * Busca relatório de pedido
     */
    public List<Object[]> findRelatorioPedido(Integer pedidoId) {
        return itemPedidoRepository.findRelatorioPedido(pedidoId);
    }

    /**
     * Busca itens por categoria de produto
     */
    public List<ItemPedido> findByProdutoCategoria(String categoria) {
        return itemPedidoRepository.findByProdutoCategoria(categoria);
    }

    /**
     * Calcula total vendido de um produto
     */
    public BigDecimal calcularTotalVendidoProduto(Integer produtoId) {
        BigDecimal total = itemPedidoRepository.calcularTotalVendidoProduto(produtoId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Busca produtos mais vendidos por categoria
     */
    public List<Object[]> findProdutosMaisVendidosPorCategoria(String categoria) {
        return itemPedidoRepository.findProdutosMaisVendidosPorCategoria(categoria);
    }

    /**
     * Calcula média de quantidade por pedido de um produto
     */
    public Double calcularMediaQuantidadeProduto(Integer produtoId) {
        Double media = itemPedidoRepository.calcularMediaQuantidadeProduto(produtoId);
        return media != null ? media : 0.0;
    }

    /**
     * Busca todos com produto e pedido carregados
     */
    public List<ItemPedido> findAllWithProdutoAndPedido() {
        return itemPedidoRepository.findAllWithProdutoAndPedido();
    }

    /**
     * Conta total de itens vendidos
     */
    public Long countTotalItensVendidos() {
        Long total = itemPedidoRepository.countTotalItensVendidos();
        return total != null ? total : 0L;
    }

    /**
     * Atualiza o total do pedido baseado nos itens
     */
    @Transactional
    private void atualizarTotalPedido(int pedidoId) {
        List<ItemPedido> itens = findByPedidoId(pedidoId);

        BigDecimal total = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setValorTotal(total);
        pedidoRepository.save(pedido);
    }
}
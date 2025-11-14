package com.example.cervejaria_api.controller;

import com.example.cervejaria_api.model.ItemPedido;
import com.example.cervejaria_api.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itens-pedido")
@CrossOrigin(origins = "*")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService itemPedidoService;

    /**
     * GET /api/itens-pedido - Lista todos os itens de pedido
     */
    @GetMapping
    public ResponseEntity<List<ItemPedido>> findAll() {
        List<ItemPedido> itens = itemPedidoService.findAll();
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /api/itens-pedido/{id} - Busca item por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> findById(@PathVariable Integer id) {
        ItemPedido item = itemPedidoService.findById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * GET /api/itens-pedido/pedido/{pedidoId} - Lista itens de um pedido
     */
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedido>> findByPedidoId(@PathVariable Integer pedidoId) {
        List<ItemPedido> itens = itemPedidoService.findByPedidoIdWithProduto(pedidoId);
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /api/itens-pedido/produto/{produtoId} - Lista itens por produto
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ItemPedido>> findByProdutoId(@PathVariable Integer produtoId) {
        List<ItemPedido> itens = itemPedidoService.findByProdutoId(produtoId);
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /api/itens-pedido/categoria/{categoria} - Lista itens por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ItemPedido>> findByCategoria(@PathVariable String categoria) {
        List<ItemPedido> itens = itemPedidoService.findByProdutoCategoria(categoria);
        return ResponseEntity.ok(itens);
    }

    /**
     * POST /api/itens-pedido - Cria novo item de pedido
     */
    @PostMapping
    public ResponseEntity<ItemPedido> create(@RequestBody ItemPedido itemPedido) {
        ItemPedido novoItem = itemPedidoService.create(itemPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    /**
     * PUT /api/itens-pedido/{id} - Atualiza item de pedido
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemPedido> update(
            @PathVariable Integer id,
            @RequestBody ItemPedido itemPedido) {
        ItemPedido itemAtualizado = itemPedidoService.update(id, itemPedido);
        return ResponseEntity.ok(itemAtualizado);
    }

    /**
     * DELETE /api/itens-pedido/{id} - Remove item de pedido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        itemPedidoService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item de pedido removido com sucesso");
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/itens-pedido/pedido/{pedidoId} - Remove todos os itens de um pedido
     */
    @DeleteMapping("/pedido/{pedidoId}")
    public ResponseEntity<Map<String, String>> deleteByPedidoId(@PathVariable Integer pedidoId) {
        itemPedidoService.deleteByPedidoId(pedidoId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todos os itens do pedido foram removidos");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/itens-pedido/pedido/{pedidoId}/count - Conta itens de um pedido
     */
    @GetMapping("/pedido/{pedidoId}/count")
    public ResponseEntity<Map<String, Long>> countByPedidoId(@PathVariable Integer pedidoId) {
        Long count = itemPedidoService.countByPedidoId(pedidoId);
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/itens-pedido/relatorios/mais-vendidos - Produtos mais vendidos
     */
    @GetMapping("/relatorios/mais-vendidos")
    public ResponseEntity<List<Object[]>> findProdutosMaisVendidos() {
        List<Object[]> produtos = itemPedidoService.findProdutosMaisVendidos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/itens-pedido/relatorios/produto/{produtoId}/quantidade - Total vendido de um produto
     */
    @GetMapping("/relatorios/produto/{produtoId}/quantidade")
    public ResponseEntity<Map<String, Long>> countTotalQuantidadeProduto(@PathVariable Integer produtoId) {
        Long total = itemPedidoService.countTotalQuantidadeProduto(produtoId);
        Map<String, Long> response = new HashMap<>();
        response.put("quantidadeTotal", total);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/itens-pedido/relatorios/produto/{produtoId}/valor-total - Valor total vendido
     */
    @GetMapping("/relatorios/produto/{produtoId}/valor-total")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotalVendidoProduto(@PathVariable Integer produtoId) {
        BigDecimal total = itemPedidoService.calcularTotalVendidoProduto(produtoId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("valorTotal", total);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/itens-pedido/relatorios/pedido/{pedidoId} - Relatório do pedido
     */
    @GetMapping("/relatorios/pedido/{pedidoId}")
    public ResponseEntity<List<Object[]>> findRelatorioPedido(@PathVariable Integer pedidoId) {
        List<Object[]> relatorio = itemPedidoService.findRelatorioPedido(pedidoId);
        return ResponseEntity.ok(relatorio);
    }

    /**
     * GET /api/itens-pedido/relatorios/categoria/{categoria}/mais-vendidos
     */
    @GetMapping("/relatorios/categoria/{categoria}/mais-vendidos")
    public ResponseEntity<List<Object[]>> findProdutosMaisVendidosPorCategoria(@PathVariable String categoria) {
        List<Object[]> produtos = itemPedidoService.findProdutosMaisVendidosPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/itens-pedido/relatorios/produto/{produtoId}/media-quantidade
     */
    @GetMapping("/relatorios/produto/{produtoId}/media-quantidade")
    public ResponseEntity<Map<String, Double>> calcularMediaQuantidadeProduto(@PathVariable Integer produtoId) {
        Double media = itemPedidoService.calcularMediaQuantidadeProduto(produtoId);
        Map<String, Double> response = new HashMap<>();
        response.put("mediaQuantidade", media);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/itens-pedido/relatorios/total-itens-vendidos
     */
    @GetMapping("/relatorios/total-itens-vendidos")
    public ResponseEntity<Map<String, Long>> countTotalItensVendidos() {
        Long total = itemPedidoService.countTotalItensVendidos();
        Map<String, Long> response = new HashMap<>();
        response.put("totalItensVendidos", total);
        return ResponseEntity.ok(response);
    }
}

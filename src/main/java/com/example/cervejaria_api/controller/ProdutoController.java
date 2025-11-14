package com.example.cervejaria_api.controller;

import com.example.cervejaria_api.model.Produto;
import com.example.cervejaria_api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * GET /api/produtos - Lista todos os produtos
     */
    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        List<Produto> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/ativos - Lista produtos ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<Produto>> findAtivos() {
        List<Produto> produtos = produtoService.findAtivos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/{id} - Busca produto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable Integer id) {
        Produto produto = produtoService.findById(id);
        return ResponseEntity.ok(produto);
    }

    /**
     * GET /api/produtos/categoria/{categoria} - Lista por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> findByCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoService.findByCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/categoria/{categoria}/ativos - Lista ativos por categoria
     */
    @GetMapping("/categoria/{categoria}/ativos")
    public ResponseEntity<List<Produto>> findAtivosByCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoService.findAtivosByCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/buscar?nome= - Busca por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> findByNome(@RequestParam String nome) {
        List<Produto> produtos = produtoService.findByNome(nome);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/estoque-baixo?quantidade= - Lista com estoque baixo
     */
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> findEstoqueBaixo(@RequestParam(defaultValue = "10") Integer quantidade) {
        List<Produto> produtos = produtoService.findEstoqueBaixo(quantidade);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/categorias - Lista todas as categorias
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> findCategorias() {
        List<String> categorias = produtoService.findAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    /**
     * POST /api/produtos - Cria novo produto
     */
    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.create(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    /**
     * PUT /api/produtos/{id} - Atualiza produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable Integer id, @RequestBody Produto produto) {
        Produto produtoAtualizado = produtoService.update(id, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * DELETE /api/produtos/{id} - Inativa produto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        produtoService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Produto inativado com sucesso");
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/produtos/{id}/permanente - Deleta permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Map<String, String>> deletePermanente(@PathVariable Integer id) {
        produtoService.deletePermanente(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Produto excluído permanentemente");
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/produtos/{id}/estoque/adicionar - Adiciona estoque
     */
    @PatchMapping("/{id}/estoque/adicionar")
    public ResponseEntity<Produto> adicionarEstoque(
            @PathVariable Integer id,
            @RequestParam Integer quantidade) {
        Produto produto = produtoService.adicionarEstoque(id, quantidade);
        return ResponseEntity.ok(produto);
    }

    /**
     * PATCH /api/produtos/{id}/estoque/remover - Remove estoque
     */
    @PatchMapping("/{id}/estoque/remover")
    public ResponseEntity<Produto> removerEstoque(
            @PathVariable Integer id,
            @RequestParam Integer quantidade) {
        Produto produto = produtoService.removerEstoque(id, quantidade);
        return ResponseEntity.ok(produto);
    }
}

package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.Produto;
import com.example.cervejaria_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Busca todos os produtos
     */
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    /**
     * Busca produtos ativos
     */
    public List<Produto> findAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    /**
     * Busca produto por ID
     */
    public Produto findById(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    /**
     * Busca produtos por categoria
     */
    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    /**
     * Busca produtos ativos por categoria
     */
    public List<Produto> findAtivosByCategoria(String categoria) {
        return produtoRepository.findByCategoriaAndAtivoTrue(categoria);
    }

    /**
     * Busca produtos por nome
     */
    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContaining(nome);
    }

    /**
     * Busca produtos com estoque baixo
     */
    public List<Produto> findEstoqueBaixo(Integer quantidade) {
        return produtoRepository.findByEstoqueBaixo(quantidade);
    }

    /**
     * Busca todas as categorias
     */
    public List<String> findAllCategorias() {
        return produtoRepository.findAllCategorias();
    }

    /**
     * Cria novo produto
     */
    @Transactional
    public Produto create(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do produto é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco().doubleValue() <= 0) {
            throw new RuntimeException("Preço deve ser maior que zero");
        }
        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("Categoria é obrigatória");
        }

        if (produto.getEstoque() == null) {
            produto.setEstoque(0);
        }
        if (produto.getAtivo() == null) {
            produto.setAtivo(true);
        }

        return produtoRepository.save(produto);
    }

    /**
     * Atualiza produto
     */
    @Transactional
    public Produto update(Integer id, Produto produtoAtualizado) {
        Produto produtoExistente = findById(id);

        if (produtoAtualizado.getNome() != null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
        }
        if (produtoAtualizado.getDescricao() != null) {
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        }
        if (produtoAtualizado.getPreco() != null) {
            if (produtoAtualizado.getPreco().doubleValue() <= 0) {
                throw new RuntimeException("Preço deve ser maior que zero");
            }
            produtoExistente.setPreco(produtoAtualizado.getPreco());
        }
        if (produtoAtualizado.getCategoria() != null) {
            produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        }
        if (produtoAtualizado.getEstoque() != null) {
            produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        }
        if (produtoAtualizado.getAtivo() != null) {
            produtoExistente.setAtivo(produtoAtualizado.getAtivo());
        }
        if (produtoAtualizado.getImagem() != null) {
            produtoExistente.setImagem(produtoAtualizado.getImagem());
        }

        return produtoRepository.save(produtoExistente);
    }

    /**
     * Deleta produto (soft delete)
     */
    @Transactional
    public void delete(Integer id) {
        Produto produto = findById(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    /**
     * Deleta produto permanentemente
     */
    @Transactional
    public void deletePermanente(Integer id) {
        produtoRepository.deleteById(id);
    }

    /**
     * Adiciona estoque
     */
    @Transactional
    public Produto adicionarEstoque(Integer id, Integer quantidade) {
        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        Produto produto = findById(id);
        produto.setEstoque(produto.getEstoque() + quantidade);
        return produtoRepository.save(produto);
    }

    /**
     * Remove estoque
     */
    @Transactional
    public Produto removerEstoque(Integer id, Integer quantidade) {
        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        Produto produto = findById(id);
        
        if (produto.getEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setEstoque(produto.getEstoque() - quantidade);
        return produtoRepository.save(produto);
    }
}
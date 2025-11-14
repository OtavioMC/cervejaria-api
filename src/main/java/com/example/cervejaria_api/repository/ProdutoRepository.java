package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    /**
     * Busca produtos por categoria
     */
    List<Produto> findByCategoriaAndDisponivelTrueOrderByNome(String categoria);

    /**
     * Busca todos os produtos disponíveis
     */
    List<Produto> findByDisponivelTrueOrderByCategoriaAscNomeAsc();

    /**
     * Busca produtos por nome (busca parcial, case insensitive)
     */
    List<Produto> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    /**
     * Busca produtos por categoria (incluindo indisponíveis)
     */
    List<Produto> findByCategoriaOrderByNome(String categoria);

    /**
     * Busca produtos com preço menor ou igual a um valor
     */
    @Query("SELECT p FROM Produto p WHERE p.preco <= :precoMaximo AND p.disponivel = true ORDER BY p.preco ASC")
    List<Produto> findByPrecoMaximo(@Param("precoMaximo") BigDecimal precoMaximo);

    /**
     * Busca produtos em uma faixa de preço
     */
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :min AND :max AND p.disponivel = true ORDER BY p.preco ASC")
    List<Produto> findByPrecoRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    /**
     * Altera disponibilidade de um produto
     */
    @Modifying
    @Query("UPDATE Produto p SET p.disponivel = :disponivel WHERE p.id = :id")
    void updateDisponivelById(@Param("id") Integer id, @Param("disponivel") boolean disponivel);

    /**
     * Conta produtos disponíveis
     */
    long countByDisponivelTrue();

    /**
     * Conta produtos por categoria
     */
    long countByCategoriaAndDisponivelTrue(String categoria);

    /**
     * Lista todas as categorias distintas
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p ORDER BY p.categoria")
    List<String> findDistinctCategorias();

    /**
     * Busca produtos mais caros (top N)
     */
    @Query("SELECT p FROM Produto p WHERE p.disponivel = true ORDER BY p.preco DESC")
    List<Produto> findProdutosMaisCaros();

    /**
     * Busca produtos mais baratos (top N)
     */
    @Query("SELECT p FROM Produto p WHERE p.disponivel = true ORDER BY p.preco ASC")
    List<Produto> findProdutosMaisBaratos();

    /**
     * Calcula preço médio por categoria
     */
    @Query("SELECT AVG(p.preco) FROM Produto p WHERE p.categoria = :categoria AND p.disponivel = true")
    BigDecimal calcularPrecoMedioPorCategoria(@Param("categoria") String categoria);

    /**
     * Lista todos os produtos ordenados por nome
     */
    List<Produto> findAllByOrderByNome();

    /**
     * Busca produtos ativos
     */
    List<Produto> findByAtivoTrue();

    /**
     * Busca produtos ativos por categoria
     */
    List<Produto> findByCategoriaAndAtivoTrue(String categoria);

    /**
     * Busca produtos por nome (contém)
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Produto> findByNomeContaining(@Param("nome") String nome);

    /**
     * Busca produtos com estoque baixo
     */
    @Query("SELECT p FROM Produto p WHERE p.estoque <= :quantidade AND p.ativo = true")
    List<Produto> findByEstoqueBaixo(@Param("quantidade") Integer quantidade);

    /**
     * Busca categorias distintas
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p ORDER BY p.categoria")
    List<String> findAllCategorias();

    /**
     * Busca produtos por categoria
     */
    List<Produto> findByCategoria(String categoria);
}
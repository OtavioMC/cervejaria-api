package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    /**
     * Busca itens de um pedido específico
     */
    @Query("SELECT i FROM ItemPedido i WHERE i.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoId(@Param("pedidoId") Integer pedidoId);

    /**
     * Busca itens de um pedido com produto (evita N+1)
     */
    @Query("SELECT i FROM ItemPedido i JOIN FETCH i.produto WHERE i.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoIdWithProduto(@Param("pedidoId") Integer pedidoId);

    /**
     * Busca itens por produto
     */
    @Query("SELECT i FROM ItemPedido i WHERE i.produto.id = :produtoId")
    List<ItemPedido> findByProdutoId(@Param("produtoId") Integer produtoId);

    /**
     * Conta itens de um pedido
     */
    @Query("SELECT COUNT(i) FROM ItemPedido i WHERE i.pedido.id = :pedidoId")
    Long countByPedidoId(@Param("pedidoId") Integer pedidoId);

    /**
     * Deleta todos os itens de um pedido
     */
    @Modifying
    @Query("DELETE FROM ItemPedido i WHERE i.pedido.id = :pedidoId")
    void deleteByPedidoId(@Param("pedidoId") Integer pedidoId);

    /**
     * Busca produtos mais vendidos (top N)
     */
    @Query("SELECT i.produto.nome, SUM(i.quantidade) as total " +
           "FROM ItemPedido i " +
           "GROUP BY i.produto.id, i.produto.nome " +
           "ORDER BY total DESC")
    List<Object[]> findProdutosMaisVendidos();

    /**
     * Busca quantidade total vendida de um produto
     */
    @Query("SELECT SUM(i.quantidade) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    Long countTotalQuantidadeProduto(@Param("produtoId") Integer produtoId);

    /**
     * Busca produtos vendidos em um pedido com suas quantidades
     */
    @Query("SELECT i.produto.nome, i.quantidade, i.precoUnitario, i.subtotal " +
           "FROM ItemPedido i WHERE i.pedido.id = :pedidoId " +
           "ORDER BY i.produto.nome")
    List<Object[]> findRelatorioPedido(@Param("pedidoId") Integer pedidoId);

    /**
     * Busca itens por categoria de produto
     */
    @Query("SELECT i FROM ItemPedido i WHERE i.produto.categoria = :categoria")
    List<ItemPedido> findByProdutoCategoria(@Param("categoria") String categoria);

    /**
     * Calcula total vendido de um produto específico
     */
    @Query("SELECT SUM(i.subtotal) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    java.math.BigDecimal calcularTotalVendidoProduto(@Param("produtoId") Integer produtoId);

    /**
     * Busca produtos mais vendidos por categoria
     */
    @Query("SELECT i.produto.categoria, i.produto.nome, SUM(i.quantidade) as total " +
           "FROM ItemPedido i " +
           "WHERE i.produto.categoria = :categoria " +
           "GROUP BY i.produto.categoria, i.produto.id, i.produto.nome " +
           "ORDER BY total DESC")
    List<Object[]> findProdutosMaisVendidosPorCategoria(@Param("categoria") String categoria);

    /**
     * Busca média de quantidade por pedido de um produto
     */
    @Query("SELECT AVG(i.quantidade) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    Double calcularMediaQuantidadeProduto(@Param("produtoId") Integer produtoId);

    /**
     * Busca todos os itens de pedidos com produto e pedido carregados (evita N+1)
     */
    @Query("SELECT i FROM ItemPedido i JOIN FETCH i.produto JOIN FETCH i.pedido")
    List<ItemPedido> findAllWithProdutoAndPedido();

    /**
     * Conta total de itens vendidos
     */
    @Query("SELECT SUM(i.quantidade) FROM ItemPedido i")
    Long countTotalItensVendidos();
}
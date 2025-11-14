package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    /**
     * Busca pedidos por status
     */
    List<Pedido> findByStatusOrderByDataHoraPedidoDesc(String status);

    /**
     * Busca pedidos por número da mesa
     */
    List<Pedido> findByNumeroMesaOrderByDataHoraPedidoDesc(Integer numeroMesa);

    /**
     * Busca todos os pedidos abertos
     */
    List<Pedido> findByStatusOrderByDataHoraPedidoAsc(String status);

    /**
     * Busca pedidos por garçom
     */
    @Query("SELECT p FROM Pedido p WHERE p.garcom.id = :garcomId ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findByGarcomId(@Param("garcomId") Integer garcomId);

    /**
     * Busca pedidos de um garçom em um período
     */
    @Query("SELECT p FROM Pedido p WHERE p.garcom.id = :garcomId AND p.dataHoraPedido BETWEEN :inicio AND :fim ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findByGarcomIdAndPeriodo(@Param("garcomId") Integer garcomId, 
                                           @Param("inicio") LocalDateTime inicio, 
                                           @Param("fim") LocalDateTime fim);

    /**
     * Busca pedidos em um período
     */
    @Query("SELECT p FROM Pedido p WHERE p.dataHoraPedido BETWEEN :inicio AND :fim ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca pedidos com valor total acima de um valor
     */
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal >= :valorMinimo ORDER BY p.valorTotal DESC")
    List<Pedido> findByValorTotalMinimo(@Param("valorMinimo") BigDecimal valorMinimo);

    /**
     * Conta pedidos por status
     */
    long countByStatus(String status);

    /**
     * Conta pedidos de uma mesa
     */
    long countByNumeroMesa(Integer numeroMesa);

    /**
     * Calcula total de vendas em um período
     */
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.status = 'PAGO' AND p.dataHoraPagamento BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalVendasPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Calcula total de vendas por garçom
     */
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.garcom.id = :garcomId AND p.status = 'PAGO'")
    BigDecimal calcularTotalVendasGarcom(@Param("garcomId") Integer garcomId);

    /**
     * Busca mesas com pedidos abertos
     */
    @Query("SELECT DISTINCT p.numeroMesa FROM Pedido p WHERE p.status = 'ABERTO' ORDER BY p.numeroMesa")
    List<Integer> findMesasComPedidosAbertos();

    /**
     * Busca pedido mais recente de uma mesa
     */
    @Query("SELECT p FROM Pedido p WHERE p.numeroMesa = :numeroMesa ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findUltimoPedidoMesa(@Param("numeroMesa") Integer numeroMesa);

    /**
     * Busca pedidos do dia atual
     */
    @Query("SELECT p FROM Pedido p WHERE DATE(p.dataHoraPedido) = CURRENT_DATE ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findPedidosHoje();

    /**
     * Busca top N pedidos com maior valor
     */
    @Query("SELECT p FROM Pedido p ORDER BY p.valorTotal DESC")
    List<Pedido> findPedidosMaioresValores();

    /**
     * Conta total de pedidos em um período
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.dataHoraPedido BETWEEN :inicio AND :fim")
    long countByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca todos os pedidos com garçom e itens (evita N+1)
     */
    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.garcom LEFT JOIN FETCH p.itens ORDER BY p.dataHoraPedido DESC")
    List<Pedido> findAllWithGarcomAndItens();
}
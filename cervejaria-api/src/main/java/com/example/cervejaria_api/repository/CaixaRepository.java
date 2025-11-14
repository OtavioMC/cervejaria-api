package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Integer> {

    /**
     * Busca todos os caixas ativos
     */
    List<Caixa> findByAtivoTrueOrderByNome();

    /**
     * Busca caixa por código
     */
    Optional<Caixa> findByCodigo(String codigo);

    /**
     * Verifica se código já existe
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica se código existe excluindo um ID específico
     */
    @Query("SELECT COUNT(c) > 0 FROM Caixa c WHERE c.codigo = :codigo AND (:id IS NULL OR c.id != :id)")
    boolean existsByCodigoExcludingId(@Param("codigo") String codigo, @Param("id") Integer id);

    /**
     * Atualiza o total vendido de um caixa
     */
    @Modifying
    @Query("UPDATE Caixa c SET c.totalVendido = c.totalVendido + :valorVenda WHERE c.id = :id")
    void atualizarTotalVendido(@Param("id") Integer id, @Param("valorVenda") BigDecimal valorVenda);

    /**
     * Busca ranking de vendas (top N caixas)
     */
    @Query("SELECT c FROM Caixa c WHERE c.ativo = true ORDER BY c.totalVendido DESC")
    List<Caixa> findRankingVendas();

    /**
     * Busca caixas por nome (busca parcial, case insensitive)
     */
    List<Caixa> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    /**
     * Conta caixas ativos
     */
    long countByAtivoTrue();

    /**
     * Busca caixas com total vendido acima de um valor
     */
    @Query("SELECT c FROM Caixa c WHERE c.totalVendido >= :valorMinimo AND c.ativo = true ORDER BY c.totalVendido DESC")
    List<Caixa> findByTotalVendidoMinimo(@Param("valorMinimo") BigDecimal valorMinimo);

    /**
     * Busca caixas com salário em uma faixa específica
     */
    @Query("SELECT c FROM Caixa c WHERE c.salario BETWEEN :min AND :max ORDER BY c.nome")
    List<Caixa> findBySalarioRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    /**
     * Calcula o total vendido por todos os caixas
     */
    @Query("SELECT SUM(c.totalVendido) FROM Caixa c WHERE c.ativo = true")
    BigDecimal calcularTotalVendasGeral();

    /**
     * Lista todos os caixas ordenados por nome
     */
    List<Caixa> findAllByOrderByNome();
}
package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.Garcom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarcomRepository extends JpaRepository<Garcom, Integer> {

    /**
     * Busca garçom por matrícula
     */
    Optional<Garcom> findByMatricula(String matricula);

    /**
     * Busca todos os garçons ativos
     */
    List<Garcom> findByAtivoTrueOrderByNome();

    /**
     * Busca garçons por turno
     */
    List<Garcom> findByTurnoAndAtivoTrueOrderByNome(String turno);

    /**
     * Busca garçons por turno (incluindo inativos)
     */
    List<Garcom> findByTurnoOrderByNome(String turno);

    /**
     * Busca garçons por nome (busca parcial, case insensitive)
     */
    List<Garcom> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    /**
     * Verifica se matrícula já existe
     */
    boolean existsByMatricula(String matricula);

    /**
     * Verifica se matrícula existe excluindo um ID
     */
    @Query("SELECT COUNT(g) > 0 FROM Garcom g WHERE g.matricula = :matricula AND (:id IS NULL OR g.id != :id)")
    boolean existsByMatriculaExcludingId(@Param("matricula") String matricula, @Param("id") Integer id);

    /**
     * Altera status do garçom
     */
    @Modifying
    @Query("UPDATE Garcom g SET g.ativo = :ativo WHERE g.id = :id")
    void updateAtivoById(@Param("id") Integer id, @Param("ativo") boolean ativo);

    /**
     * Conta garçons ativos
     */
    long countByAtivoTrue();

    /**
     * Conta garçons por turno
     */
    long countByTurnoAndAtivoTrue(String turno);

    /**
     * Busca garçons com salário acima de um valor
     */
    @Query("SELECT g FROM Garcom g WHERE g.salario >= :valorMinimo AND g.ativo = true ORDER BY g.salario DESC")
    List<Garcom> findBySalarioMinimo(@Param("valorMinimo") java.math.BigDecimal valorMinimo);

    /**
     * Lista todos os garçons ordenados por nome
     */
    List<Garcom> findAllByOrderByNome();
}
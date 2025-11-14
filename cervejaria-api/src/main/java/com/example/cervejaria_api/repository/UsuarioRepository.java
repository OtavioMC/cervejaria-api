package com.example.cervejaria_api.repository;

import com.example.cervejaria_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuário por CPF
     */
    Optional<Usuario> findByCpf(Long cpf);

    /**
     * Autentica usuário por email e senha
     */
    Optional<Usuario> findByEmailAndSenhaAndAtivoTrue(String email, String senha);

    /**
     * Verifica se email já existe (excluindo um ID específico)
     */
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email AND (:id IS NULL OR u.id != :id)")
    boolean existsByEmailExcludingId(@Param("email") String email, @Param("id") Integer id);

    /**
     * Busca todos os usuários ativos
     */
    List<Usuario> findByAtivoTrueOrderByNome();

    /**
     * Busca usuários por papel/role
     */
    List<Usuario> findByPapelOrderByNome(String papel);

    /**
     * Busca usuários ativos por papel
     */
    List<Usuario> findByPapelAndAtivoTrueOrderByNome(String papel);

    /**
     * Busca usuários por nome (busca parcial, case insensitive)
     */
    List<Usuario> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    /**
     * Conta quantos usuários existem por papel
     */
    long countByPapel(String papel);

    /**
     * Verifica se CPF já existe
     */
    boolean existsByCpf(Long cpf);

    /**
     * Verifica se email já existe
     */
    boolean existsByEmail(String email);
}
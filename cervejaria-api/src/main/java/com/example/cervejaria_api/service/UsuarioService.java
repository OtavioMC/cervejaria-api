package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.Usuario;
import com.example.cervejaria_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Lista todos os usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca usuário por ID
     */
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca usuário por email
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca usuário por CPF
     */
    public Optional<Usuario> buscarPorCpf(Long cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    /**
     * Autentica usuário
     */
    public Optional<Usuario> autenticar(String email, String senha) {
        return usuarioRepository.findByEmailAndSenhaAndAtivoTrue(email, senha);
    }

    /**
     * Lista todos os usuários ativos
     */
    public List<Usuario> listarAtivos() {
        return usuarioRepository.findByAtivoTrueOrderByNome();
    }

    /**
     * Lista usuários por papel
     */
    public List<Usuario> listarPorPapel(String papel) {
        return usuarioRepository.findByPapelOrderByNome(papel);
    }

    /**
     * Lista usuários ativos por papel
     */
    public List<Usuario> listarAtivosPorPapel(String papel) {
        return usuarioRepository.findByPapelAndAtivoTrueOrderByNome(papel);
    }

    /**
     * Busca usuários por nome (busca parcial)
     */
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCaseOrderByNome(nome);
    }

    /**
     * Conta usuários por papel
     */
    public long contarPorPapel(String papel) {
        return usuarioRepository.countByPapel(papel);
    }

    /**
     * Cria novo usuário
     */
    @Transactional
    public Usuario criar(Usuario usuario) {
        validarUsuario(usuario, null);
        
        if (usuario.getDataCriacao() == null) {
            usuario.setDataCriacao(LocalDate.now());
        }
        
        if (usuario.getAtivo() == null) {
            usuario.setAtivo(true);
        }
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza usuário existente
     */
    @Transactional
    public Usuario atualizar(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        validarUsuario(usuarioAtualizado, id);

        // Atualiza os campos
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setCpf(usuarioAtualizado.getCpf());
        usuarioExistente.setDataNascimento(usuarioAtualizado.getDataNascimento());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        
        // Só atualiza senha se foi fornecida
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioExistente.setSenha(usuarioAtualizado.getSenha());
        }
        
        usuarioExistente.setPapel(usuarioAtualizado.getPapel());
        usuarioExistente.setAtivo(usuarioAtualizado.getAtivo());
        usuarioExistente.setDataUltimaAlteracao(LocalDate.now());

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Deleta usuário
     */
    @Transactional
    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Altera senha do usuário
     */
    @Transactional
    public boolean alterarSenha(Integer id, String senhaAntiga, String senhaNova) {
        return usuarioRepository.alterarSenha(id, senhaAntiga, senhaNova);
    }

    /**
     * Valida dados do usuário
     */
    private void validarUsuario(Usuario usuario, Integer idExcluir) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }

        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Email inválido");
        }

        // Valida email duplicado
        if (usuarioRepository.existsByEmailExcludingId(usuario.getEmail(), idExcluir)) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Valida CPF duplicado (se informado)
        if (usuario.getCpf() != null) {
            Optional<Usuario> usuarioComCpf = usuarioRepository.findByCpf(usuario.getCpf());
            if (usuarioComCpf.isPresent() && 
                (idExcluir == null || !usuarioComCpf.get().getId().equals(idExcluir))) {
                throw new RuntimeException("CPF já cadastrado");
            }
        }

        // Valida senha no cadastro
        if (idExcluir == null && (usuario.getSenha() == null || usuario.getSenha().length() < 4)) {
            throw new RuntimeException("Senha deve ter no mínimo 4 caracteres");
        }
    }
}
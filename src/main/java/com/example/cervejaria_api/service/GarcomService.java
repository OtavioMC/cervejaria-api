package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.Garcom;
import com.example.cervejaria_api.repository.GarcomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GarcomService {

    @Autowired
    private GarcomRepository garcomRepository;

    /**
     * Lista todos os garçons
     */
    public List<Garcom> listarTodos() {
        return garcomRepository.findAllByOrderByNome();
    }

    /**
     * Busca garçom por ID
     */
    public Optional<Garcom> buscarPorId(Integer id) {
        return garcomRepository.findById(id);
    }

    /**
     * Busca garçom por matrícula
     */
    public Optional<Garcom> buscarPorMatricula(String matricula) {
        return garcomRepository.findByMatricula(matricula);
    }

    /**
     * Lista todos os garçons ativos
     */
    public List<Garcom> listarAtivos() {
        return garcomRepository.findByAtivoTrueOrderByNome();
    }

    /**
     * Lista garçons por turno
     */
    public List<Garcom> listarPorTurno(String turno) {
        return garcomRepository.findByTurnoOrderByNome(turno);
    }

    /**
     * Lista garçons ativos por turno
     */
    public List<Garcom> listarAtivosPorTurno(String turno) {
        return garcomRepository.findByTurnoAndAtivoTrueOrderByNome(turno);
    }

    /**
     * Busca garçons por nome
     */
    public List<Garcom> buscarPorNome(String nome) {
        return garcomRepository.findByNomeContainingIgnoreCaseOrderByNome(nome);
    }

    /**
     * Lista garçons com salário mínimo
     */
    public List<Garcom> listarPorSalarioMinimo(BigDecimal salarioMinimo) {
        return garcomRepository.findBySalarioMinimo(salarioMinimo);
    }

    /**
     * Conta garçons ativos
     */
    public long contarAtivos() {
        return garcomRepository.countByAtivoTrue();
    }

    /**
     * Conta garçons por turno
     */
    public long contarPorTurno(String turno) {
        return garcomRepository.countByTurnoAndAtivoTrue(turno);
    }

    /**
     * Cria novo garçom
     */
    @Transactional
    public Garcom criar(Garcom garcom) {
        validarGarcom(garcom, null);
        
        if (garcom.getDataCriacao() == null) {
            garcom.setDataCriacao(LocalDateTime.now());
        }
        
        if (garcom.getAtivo() == null) {
            garcom.setAtivo(true);
        }
        
        garcom.setDataContratacao(LocalDateTime.now());
        
        return garcomRepository.save(garcom);
    }

    /**
     * Atualiza garçom existente
     */
    @Transactional
    public Garcom atualizar(Integer id, Garcom garcomAtualizado) {
        Garcom garcomExistente = garcomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garçom não encontrado com ID: " + id));

        validarGarcom(garcomAtualizado, id);

        // Atualiza os campos
        garcomExistente.setNome(garcomAtualizado.getNome());
        garcomExistente.setCpf(garcomAtualizado.getCpf());
        if (garcomAtualizado.getDataNascimento() != null) {
            garcomExistente.setDataNascimento(garcomAtualizado.getDataNascimento());
        }
        if (garcomAtualizado.getMatricula() != null) {
            garcomExistente.setMatricula(garcomAtualizado.getMatricula());
        }
        if (garcomAtualizado.getSalario() != null) {
            garcomExistente.setSalario(garcomAtualizado.getSalario());
        }
        if (garcomAtualizado.getTurno() != null) {
            garcomExistente.setTurno(garcomAtualizado.getTurno());
        }
        garcomExistente.setDataUltimaAlteracao(LocalDateTime.now());

        return garcomRepository.save(garcomExistente);
    }

    /**
     * Deleta garçom
     */
    @Transactional
    public void deletar(Integer id) {
        if (!garcomRepository.existsById(id)) {
            throw new RuntimeException("Garçom não encontrado com ID: " + id);
        }
        garcomRepository.deleteById(id);
    }

    /**
     * Altera status ativo/inativo do garçom
     */
    @Transactional
    public void alterarStatus(Integer id, boolean ativo) {
        if (!garcomRepository.existsById(id)) {
            throw new RuntimeException("Garçom não encontrado com ID: " + id);
        }
        garcomRepository.updateAtivoById(id, ativo);
    }

    /**
     * Valida dados do garçom
     */
    private void validarGarcom(Garcom garcom, Integer idExcluir) {
        if (garcom.getNome() == null || garcom.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (garcom.getMatricula() == null || garcom.getMatricula().trim().isEmpty()) {
            throw new RuntimeException("Matrícula é obrigatória");
        }

        // Valida matrícula duplicada
        if (garcomRepository.existsByMatricula(garcom.getMatricula())) {
            throw new RuntimeException("Matrícula já cadastrada");
        }

        // Valida CPF duplicado (se informado)
        if (garcom.getCpf() != null) {
            Optional<Garcom> garcomComCpf = garcomRepository.findById(idExcluir != null ? idExcluir : -1);
            // Implementar validação de CPF se necessário
        }

        // Valida salário
        if (garcom.getSalario() == null || garcom.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Salário não pode ser negativo");
        }
    }

    /**
     * Busca todos os garçons
     */
    public List<Garcom> findAll() {
        return garcomRepository.findAll();
    }

    /**
     * Busca garçons ativos
     */
    public List<Garcom> findAtivos() {
        return garcomRepository.findByAtivoTrue();
    }

    /**
     * Busca garçom por ID
     */
    public Garcom findById(Integer id) {
        return garcomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garçom não encontrado com ID: " + id));
    }

    /**
     * Busca garçom por CPF
     */
    public Optional<Garcom> findByCpf(String cpf) {
        return garcomRepository.findByCpf(cpf);
    }

    /**
     * Busca garçom por email
     */
    public Optional<Garcom> findByEmail(String email) {
        return garcomRepository.findByEmail(email);
    }

    /**
     * Busca garçons por nome
     */
    public List<Garcom> findByNome(String nome) {
        return garcomRepository.findByNomeContaining(nome);
    }

    /**
     * Cria novo garçom
     */
    @Transactional
    public Garcom create(Garcom garcom) {
        if (garcom.getNome() == null || garcom.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do garçom é obrigatório");
        }

        // Valida CPF único
        if (garcom.getCpf() != null && !garcom.getCpf().isEmpty()) {
            Optional<Garcom> garcomExistente = garcomRepository.findByCpf(garcom.getCpf());
            if (garcomExistente.isPresent()) {
                throw new RuntimeException("CPF já cadastrado");
            }
        }

        // Valida Email único
        if (garcom.getEmail() != null && !garcom.getEmail().isEmpty()) {
            Optional<Garcom> garcomExistente = garcomRepository.findByEmail(garcom.getEmail());
            if (garcomExistente.isPresent()) {
                throw new RuntimeException("Email já cadastrado");
            }
        }

        if (garcom.getAtivo() == null) {
            garcom.setAtivo(true);
        }

        return garcomRepository.save(garcom);
    }

    /**
     * Atualiza garçom
     */
    @Transactional
    public Garcom update(Integer id, Garcom garcomAtualizado) {
        Garcom garcomExistente = findById(id);

        if (garcomAtualizado.getNome() != null) {
            garcomExistente.setNome(garcomAtualizado.getNome());
        }
        if (garcomAtualizado.getCpf() != null) {
            // Valida CPF único (exceto o próprio)
            Optional<Garcom> outro = garcomRepository.findByCpf(garcomAtualizado.getCpf());
            if (outro.isPresent() && !outro.get().getId().equals(id)) {
                throw new RuntimeException("CPF já cadastrado");
            }
            garcomExistente.setCpf(garcomAtualizado.getCpf());
        }
        if (garcomAtualizado.getTelefone() != null) {
            garcomExistente.setTelefone(garcomAtualizado.getTelefone());
        }
        if (garcomAtualizado.getEmail() != null) {
            // Valida Email único (exceto o próprio)
            Optional<Garcom> outro = garcomRepository.findByEmail(garcomAtualizado.getEmail());
            if (outro.isPresent() && !outro.get().getId().equals(id)) {
                throw new RuntimeException("Email já cadastrado");
            }
            garcomExistente.setEmail(garcomAtualizado.getEmail());
        }
        if (garcomAtualizado.getAtivo() != null) {
            garcomExistente.setAtivo(garcomAtualizado.getAtivo());
        }

        return garcomRepository.save(garcomExistente);
    }

    /**
     * Deleta garçom (soft delete)
     */
    @Transactional
    public void delete(Integer id) {
        Garcom garcom = findById(id);
        garcom.setAtivo(false);
        garcomRepository.save(garcom);
    }

    /**
     * Deleta garçom permanentemente
     */
    @Transactional
    public void deletePermanente(Integer id) {
        garcomRepository.deleteById(id);
    }
}
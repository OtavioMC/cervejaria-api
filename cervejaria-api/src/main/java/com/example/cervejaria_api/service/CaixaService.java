package com.example.cervejaria_api.service;

import com.example.cervejaria_api.model.Caixa;
import com.example.cervejaria_api.repository.CaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaService {

    @Autowired
    private CaixaRepository caixaRepository;

    /**
     * Lista todos os caixas
     */
    public List<Caixa> listarTodos() {
        return caixaRepository.findAllByOrderByNome();
    }

    /**
     * Busca caixa por ID
     */
    public Optional<Caixa> buscarPorId(Integer id) {
        return caixaRepository.findById(id);
    }

    /**
     * Busca caixa por código
     */
    public Optional<Caixa> buscarPorCodigo(String codigo) {
        return caixaRepository.findByCodigo(codigo);
    }

    /**
     * Lista todos os caixas ativos
     */
    public List<Caixa> listarAtivos() {
        return caixaRepository.findByAtivoTrueOrderByNome();
    }

    /**
     * Busca caixas por nome
     */
    public List<Caixa> buscarPorNome(String nome) {
        return caixaRepository.findByNomeContainingIgnoreCaseOrderByNome(nome);
    }

    /**
     * Busca ranking de vendas
     */
    public List<Caixa> buscarRankingVendas() {
        return caixaRepository.findRankingVendas();
    }

    /**
     * Lista caixas por total vendido mínimo
     */
    public List<Caixa> listarPorTotalVendidoMinimo(BigDecimal valorMinimo) {
        return caixaRepository.findByTotalVendidoMinimo(valorMinimo);
    }

    /**
     * Lista caixas por faixa de salário
     */
    public List<Caixa> listarPorFaixaSalarial(BigDecimal min, BigDecimal max) {
        return caixaRepository.findBySalarioRange(min, max);
    }

    /**
     * Conta caixas ativos
     */
    public long contarAtivos() {
        return caixaRepository.countByAtivoTrue();
    }

    /**
     * Calcula total de vendas geral
     */
    public BigDecimal calcularTotalVendasGeral() {
        BigDecimal total = caixaRepository.calcularTotalVendasGeral();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Cria novo caixa
     */
    @Transactional
    public Caixa criar(Caixa caixa) {
        validarCaixa(caixa, null);
        
        if (caixa.getDataCriacao() == null) {
            caixa.setDataCriacao(LocalDate.now());
        }
        
        if (caixa.getAtivo() == null) {
            caixa.setAtivo(true);
        }
        
        if (caixa.getTotalVendido() == null) {
            caixa.setTotalVendido(BigDecimal.ZERO);
        }
        
        return caixaRepository.save(caixa);
    }

    /**
     * Atualiza caixa existente
     */
    @Transactional
    public Caixa atualizar(Integer id, Caixa caixaAtualizado) {
        Caixa caixaExistente = caixaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado com ID: " + id));

        validarCaixa(caixaAtualizado, id);

        // Atualiza os campos (preserva totalVendido)
        caixaExistente.setNome(caixaAtualizado.getNome());
        caixaExistente.setCpf(caixaAtualizado.getCpf());
        caixaExistente.setDataNascimento(caixaAtualizado.getDataNascimento());
        caixaExistente.setCodigo(caixaAtualizado.getCodigo());
        caixaExistente.setSalario(caixaAtualizado.getSalario());
        caixaExistente.setAtivo(caixaAtualizado.getAtivo());
        caixaExistente.setDataUltimaAlteracao(LocalDate.now());

        return caixaRepository.save(caixaExistente);
    }

    /**
     * Deleta caixa
     */
    @Transactional
    public void deletar(Integer id) {
        if (!caixaRepository.existsById(id)) {
            throw new RuntimeException("Caixa não encontrado com ID: " + id);
        }
        caixaRepository.deleteById(id);
    }

    /**
     * Atualiza total vendido do caixa
     */
    @Transactional
    public void atualizarTotalVendido(Integer id, BigDecimal valorVenda) {
        if (!caixaRepository.existsById(id)) {
            throw new RuntimeException("Caixa não encontrado com ID: " + id);
        }
        
        if (valorVenda == null || valorVenda.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor de venda inválido");
        }
        
        caixaRepository.atualizarTotalVendido(id, valorVenda);
    }

    /**
     * Valida dados do caixa
     */
    private void validarCaixa(Caixa caixa, Integer idExcluir) {
        if (caixa.getNome() == null || caixa.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (caixa.getCodigo() == null || caixa.getCodigo().trim().isEmpty()) {
            throw new RuntimeException("Código é obrigatório");
        }

        // Valida código duplicado
        if (caixaRepository.existsByCodigoExcludingId(caixa.getCodigo(), idExcluir)) {
            throw new RuntimeException("Código já cadastrado");
        }

        // Valida salário
        if (caixa.getSalario() != null && caixa.getSalario().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Salário não pode ser negativo");
        }

        // Valida total vendido
        if (caixa.getTotalVendido() != null && caixa.getTotalVendido().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Total vendido não pode ser negativo");
        }
    }
}
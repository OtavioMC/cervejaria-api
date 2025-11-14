package com.example.cervejaria_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "caixas")
public class Caixa extends Pessoa {

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "total_vendido", precision = 12, scale = 2)
    private BigDecimal totalVendido = BigDecimal.ZERO;

    @Column(name = "valor_atual", precision = 12, scale = 2)
    private BigDecimal valorAtual = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String status = "FECHADO";

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_abertura")
    private LocalDateTime dataAbertura;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;

    // Getters e Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }
}
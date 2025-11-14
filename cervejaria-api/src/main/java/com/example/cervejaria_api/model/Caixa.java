package com.example.cervejaria_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "caixas")
public class Caixa extends Pessoa {

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "total_vendido", precision = 12, scale = 2)
    private BigDecimal totalVendido = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativo = true;

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
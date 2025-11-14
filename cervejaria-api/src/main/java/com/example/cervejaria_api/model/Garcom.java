package com.example.cervejaria_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "garcons")
public class Garcom extends Pessoa {

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(length = 20)
    private String turno;

    // Getters e Setters
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
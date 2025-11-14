package com.example.cervejaria_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "garcons")
public class Garcom extends Entidade {

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome do garçom é obrigatório")
    private String nome;

    @Column(unique = true, length = 11)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    @Column(length = 100)
    @Email(message = "Email inválido")
    private String email;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "matricula", unique = true)
    private String matricula;

    @Column(name = "salario")
    private BigDecimal salario;

    @Column(name = "turno")
    private String turno;

    @Column(name = "data_contratacao")
    private LocalDateTime dataContratacao;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDateTime getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDateTime dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }
}
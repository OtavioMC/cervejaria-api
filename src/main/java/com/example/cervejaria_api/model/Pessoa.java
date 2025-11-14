package com.example.cervejaria_api.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Pessoa extends Entidade {

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(unique = true)
    private Long cpf;

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }
}
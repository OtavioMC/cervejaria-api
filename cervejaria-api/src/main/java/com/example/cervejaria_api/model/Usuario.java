package com.example.cervejaria_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario extends Pessoa {

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String senha;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(length = 20)
    private String papel;

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }
}
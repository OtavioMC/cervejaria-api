package com.example.cervejaria_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido extends Entidade {    @Column(name = "numero_mesa", nullable = false)
    @NotNull(message = "Número da mesa é obrigatório")
    private Integer numeroMesa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "garcom_id", nullable = false)
    @NotNull(message = "Garçom é obrigatório")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Garcom garcom;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"pedido", "hibernateLazyInitializer", "handler"})
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String status = "ABERTO";

    @Column(name = "data_hora_pedido")
    private LocalDateTime dataHoraPedido;

    @Column(name = "data_hora_pagamento")
    private LocalDateTime dataHoraPagamento;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "forma_pagamento", length = 50)
    private String formaPagamento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Getters e Setters
    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Garcom getGarcom() {
        return garcom;
    }

    public void setGarcom(Garcom garcom) {
        this.garcom = garcom;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataHoraPedido() {
        return dataHoraPedido;
    }

    public void setDataHoraPedido(LocalDateTime dataHoraPedido) {
        this.dataHoraPedido = dataHoraPedido;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void calcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        calcularTotal();
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
        calcularTotal();
    }
}

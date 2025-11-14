package com.example.cervejaria_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @NotNull(message = "Pedido é obrigatório")
    @JsonIgnoreProperties({"itens", "hibernateLazyInitializer", "handler"})
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", nullable = false)
    @NotNull(message = "Produto é obrigatório")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Produto produto;

    @Column(nullable = false)
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser no mínimo 1")
    private Integer quantidade;    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Preço unitário é obrigatório")
    private BigDecimal precoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null && this.precoUnitario == null) {
            this.precoUnitario = produto.getPreco();
        }
        calcularSubtotal();
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    private void calcularSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            this.subtotal = precoUnitario.multiply(new BigDecimal(quantidade));
        }
    }
}
package com.empresa.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade que representa um item de pedido no sistema de gestão de pedidos.
 * 
 * Cada item de pedido associa um produto a um pedido com uma quantidade específica
 * e calcula o subtotal baseado no preço do produto e na quantidade.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    /**
     * Identificador único do item de pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pedido ao qual este item pertence.
     * Relacionamento Many-to-One com a entidade Pedido.
     */
    @NotNull(message = "O pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /**
     * Produto associado a este item de pedido.
     * Relacionamento Many-to-One com a entidade Produto.
     */
    @NotNull(message = "O produto é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /**
     * Quantidade do produto no item de pedido.
     * Deve ser maior que zero.
     */
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    /**
     * Subtotal do item de pedido em reais.
     * Calculado como: preço do produto × quantidade.
     */
    @NotNull(message = "O subtotal é obrigatório")
    @DecimalMin(value = "0.01", message = "O subtotal deve ser maior que zero")
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    /**
     * Construtor padrão necessário para o JPA.
     */
    public ItemPedido() {
    }

    /**
     * Construtor para criação de item de pedido com pedido, produto e quantidade.
     * O subtotal é calculado automaticamente.
     * 
     * @param pedido Pedido ao qual o item pertence
     * @param produto Produto do item
     * @param quantidade Quantidade do produto
     */
    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    /**
     * Método executado antes da persistência da entidade.
     * Garante que o subtotal seja calculado se não foi definido.
     */
    @PrePersist
    @PreUpdate
    public void preCalculation() {
        calcularSubtotal();
    }

    /**
     * Obtém o identificador único do item de pedido.
     * 
     * @return ID do item de pedido
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do item de pedido.
     * 
     * @param id ID do item de pedido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o pedido ao qual este item pertence.
     * 
     * @return Pedido do item
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * Define o pedido ao qual este item pertence.
     * 
     * @param pedido Pedido do item
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Obtém o produto associado ao item de pedido.
     * 
     * @return Produto do item
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * Define o produto associado ao item de pedido e recalcula o subtotal.
     * 
     * @param produto Produto do item
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
        calcularSubtotal();
    }

    /**
     * Obtém a quantidade do produto no item de pedido.
     * 
     * @return Quantidade do produto
     */
    public Integer getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade do produto no item de pedido e recalcula o subtotal.
     * 
     * @param quantidade Quantidade do produto
     */
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    /**
     * Obtém o subtotal do item de pedido.
     * 
     * @return Subtotal em BigDecimal
     */
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /**
     * Define o subtotal do item de pedido.
     * Normalmente não deve ser chamado diretamente, use calcularSubtotal().
     * 
     * @param subtotal Subtotal em BigDecimal
     */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Calcula o subtotal do item baseado no preço do produto e na quantidade.
     * Subtotal = preço do produto × quantidade.
     */
    public void calcularSubtotal() {
        if (this.produto != null && this.produto.getPreco() != null && this.quantidade != null) {
            this.subtotal = this.produto.getPreco().multiply(new BigDecimal(this.quantidade));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    /**
     * Obtém o preço unitário do produto no momento do item de pedido.
     * 
     * @return Preço unitário do produto
     */
    public BigDecimal getPrecoUnitario() {
        return this.produto != null ? this.produto.getPreco() : BigDecimal.ZERO;
    }

    /**
     * Verifica se o item de pedido é válido (possui produto e quantidade válida).
     * 
     * @return true se o item é válido, false caso contrário
     */
    public boolean isValido() {
        return this.produto != null && 
               this.quantidade != null && 
               this.quantidade > 0 && 
               this.subtotal != null && 
               this.subtotal.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "null") +
                ", quantidade=" + quantidade +
                ", subtotal=" + subtotal +
                '}';
    }
}

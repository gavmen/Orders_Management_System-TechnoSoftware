package com.empresa.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa um produto no sistema de gestão de pedidos.
 * 
 * Um produto pode estar associado a múltiplos itens de pedidos e possui
 * informações básicas como nome e preço.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Entity
@Table(name = "produto")
public class Produto {

    /**
     * Identificador único do produto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do produto.
     * Campo obrigatório com tamanho entre 2 e 150 caracteres.
     */
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /**
     * Preço unitário do produto em reais.
     * Utilizado para cálculo do valor total dos pedidos.
     */
    @NotNull(message = "O preço do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @Column(name = "preco", nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    /**
     * Lista de itens de pedidos que referenciam este produto.
     * Relacionamento bidirecional com a entidade ItemPedido.
     */
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido = new ArrayList<>();

    /**
     * Construtor padrão necessário para o JPA.
     */
    public Produto() {
    }

    /**
     * Construtor para criação de produto com nome e preço.
     * 
     * @param nome Nome do produto
     * @param preco Preço unitário do produto
     */
    public Produto(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }

    /**
     * Obtém o identificador único do produto.
     * 
     * @return ID do produto
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do produto.
     * 
     * @param id ID do produto
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do produto.
     * 
     * @return Nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     * 
     * @param nome Nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o preço unitário do produto.
     * 
     * @return Preço do produto em BigDecimal
     */
    public BigDecimal getPreco() {
        return preco;
    }

    /**
     * Define o preço unitário do produto.
     * 
     * @param preco Preço do produto em BigDecimal
     */
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    /**
     * Obtém a lista de itens de pedidos associados ao produto.
     * 
     * @return Lista de itens de pedido
     */
    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    /**
     * Define a lista de itens de pedidos associados ao produto.
     * 
     * @param itensPedido Lista de itens de pedido
     */
    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    /**
     * Adiciona um item de pedido à lista de itens associados ao produto.
     * 
     * @param itemPedido Item de pedido a ser adicionado
     */
    public void adicionarItemPedido(ItemPedido itemPedido) {
        this.itensPedido.add(itemPedido);
        itemPedido.setProduto(this);
    }

    /**
     * Remove um item de pedido da lista de itens associados ao produto.
     * 
     * @param itemPedido Item de pedido a ser removido
     */
    public void removerItemPedido(ItemPedido itemPedido) {
        this.itensPedido.remove(itemPedido);
        itemPedido.setProduto(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id != null && Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}

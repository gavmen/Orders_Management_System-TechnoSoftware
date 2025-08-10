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
 * Entidade que representa um cliente no sistema de gestão de pedidos.
 * 
 * Um cliente pode ter múltiplos pedidos associados e possui um limite de crédito
 * que é utilizado para validação de aprovação dos pedidos.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Entity
@Table(name = "cliente")
public class Cliente {

    /**
     * Identificador único do cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do cliente.
     * Campo obrigatório com tamanho entre 2 e 100 caracteres.
     */
    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * Limite de crédito do cliente em reais.
     * Utilizado para validação de aprovação de pedidos.
     */
    @NotNull(message = "O limite de crédito é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O limite de crédito deve ser maior ou igual a zero")
    @Column(name = "limite_credito", nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteCredito;

    /**
     * Lista de pedidos associados ao cliente.
     * Relacionamento bidirecional com a entidade Pedido.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    /**
     * Construtor padrão necessário para o JPA.
     */
    public Cliente() {
    }

    /**
     * Construtor para criação de cliente com nome e limite de crédito.
     * 
     * @param nome Nome do cliente
     * @param limiteCredito Limite de crédito do cliente
     */
    public Cliente(String nome, BigDecimal limiteCredito) {
        this.nome = nome;
        this.limiteCredito = limiteCredito;
    }

    /**
     * Obtém o identificador único do cliente.
     * 
     * @return ID do cliente
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do cliente.
     * 
     * @param id ID do cliente
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do cliente.
     * 
     * @return Nome do cliente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do cliente.
     * 
     * @param nome Nome do cliente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o limite de crédito do cliente.
     * 
     * @return Limite de crédito em BigDecimal
     */
    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    /**
     * Define o limite de crédito do cliente.
     * 
     * @param limiteCredito Limite de crédito em BigDecimal
     */
    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    /**
     * Obtém a lista de pedidos do cliente.
     * 
     * @return Lista de pedidos
     */
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    /**
     * Define a lista de pedidos do cliente.
     * 
     * @param pedidos Lista de pedidos
     */
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    /**
     * Adiciona um pedido à lista de pedidos do cliente.
     * 
     * @param pedido Pedido a ser adicionado
     */
    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setCliente(this);
    }

    /**
     * Remove um pedido da lista de pedidos do cliente.
     * 
     * @param pedido Pedido a ser removido
     */
    public void removerPedido(Pedido pedido) {
        this.pedidos.remove(pedido);
        pedido.setCliente(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", limiteCredito=" + limiteCredito +
                '}';
    }
}

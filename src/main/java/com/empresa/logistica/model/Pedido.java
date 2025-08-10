package com.empresa.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa um pedido no sistema de gestão de pedidos.
 * 
 * Um pedido pertence a um cliente e pode conter múltiplos itens. O status do pedido
 * (APROVADO ou REJEITADO) é determinado pela validação do limite de crédito do cliente.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Entity
@Table(name = "pedido")
public class Pedido {

    /**
     * Identificador único do pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cliente proprietário do pedido.
     * Relacionamento Many-to-One com a entidade Cliente.
     */
    @NotNull(message = "O cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Data e hora de criação do pedido.
     * Preenchida automaticamente no momento da persistência.
     */
    @NotNull(message = "A data do pedido é obrigatória")
    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    /**
     * Valor total do pedido em reais.
     * Calculado com base na soma dos subtotais dos itens do pedido.
     */
    @NotNull(message = "O valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero")
    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Status do pedido (APROVADO ou REJEITADO).
     * Determinado pela validação do limite de crédito do cliente.
     */
    @NotNull(message = "O status do pedido é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPedido status;

    /**
     * Lista de itens que compõem o pedido.
     * Relacionamento bidirecional One-to-Many com cascata completa.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    /**
     * Construtor padrão necessário para o JPA.
     */
    public Pedido() {
        this.dataPedido = LocalDateTime.now();
    }

    /**
     * Construtor para criação de pedido com cliente.
     * 
     * @param cliente Cliente proprietário do pedido
     */
    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
        this.valorTotal = BigDecimal.ZERO;
        this.status = StatusPedido.APROVADO; // Status inicial, será validado posteriormente
    }

    /**
     * Método executado antes da persistência da entidade.
     * Garante que a data do pedido seja preenchida se não foi definida.
     */
    @PrePersist
    public void prePersist() {
        if (this.dataPedido == null) {
            this.dataPedido = LocalDateTime.now();
        }
    }

    /**
     * Obtém o identificador único do pedido.
     * 
     * @return ID do pedido
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do pedido.
     * 
     * @param id ID do pedido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o cliente proprietário do pedido.
     * 
     * @return Cliente do pedido
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Define o cliente proprietário do pedido.
     * 
     * @param cliente Cliente do pedido
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtém a data e hora do pedido.
     * 
     * @return Data do pedido
     */
    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    /**
     * Define a data e hora do pedido.
     * 
     * @param dataPedido Data do pedido
     */
    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    /**
     * Obtém o valor total do pedido.
     * 
     * @return Valor total em BigDecimal
     */
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor total do pedido.
     * 
     * @param valorTotal Valor total em BigDecimal
     */
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * Obtém o status do pedido.
     * 
     * @return Status do pedido
     */
    public StatusPedido getStatus() {
        return status;
    }

    /**
     * Define o status do pedido.
     * 
     * @param status Status do pedido
     */
    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    /**
     * Obtém a lista de itens do pedido.
     * 
     * @return Lista de itens do pedido
     */
    public List<ItemPedido> getItens() {
        return itens;
    }

    /**
     * Define a lista de itens do pedido.
     * 
     * @param itens Lista de itens do pedido
     */
    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    /**
     * Adiciona um item ao pedido e atualiza o valor total.
     * 
     * @param item Item a ser adicionado
     */
    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        item.setPedido(this);
        recalcularValorTotal();
    }

    /**
     * Remove um item do pedido e atualiza o valor total.
     * 
     * @param item Item a ser removido
     */
    public void removerItem(ItemPedido item) {
        this.itens.remove(item);
        item.setPedido(null);
        recalcularValorTotal();
    }

    /**
     * Recalcula o valor total do pedido com base nos subtotais dos itens.
     */
    public void recalcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(ItemPedido::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica se o pedido possui itens.
     * 
     * @return true se o pedido possui itens, false caso contrário
     */
    public boolean possuiItens() {
        return !this.itens.isEmpty();
    }

    /**
     * Obtém a quantidade total de itens no pedido.
     * 
     * @return Quantidade total de itens
     */
    public int getQuantidadeTotalItens() {
        return this.itens.stream()
                .mapToInt(ItemPedido::getQuantidade)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id != null && Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNome() : "null") +
                ", dataPedido=" + dataPedido +
                ", valorTotal=" + valorTotal +
                ", status=" + (status != null ? status.name() : "null") +
                ", quantidadeItens=" + (itens != null ? itens.size() : 0) +
                '}';
    }
}

package com.empresa.logistica.model;

/**
 * Enumeração que representa os possíveis status de um pedido no sistema.
 * 
 * O status do pedido é determinado pela validação do limite de crédito do cliente
 * com base no total de pedidos dos últimos 30 dias.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
public enum StatusPedido {
    
    /**
     * Status indicando que o pedido foi aprovado.
     * 
     * Um pedido é aprovado quando o valor total do pedido somado aos pedidos
     * dos últimos 30 dias não excede o limite de crédito do cliente.
     */
    APROVADO("Aprovado"),
    
    /**
     * Status indicando que o pedido foi rejeitado.
     * 
     * Um pedido é rejeitado quando o valor total do pedido excede o saldo
     * disponível do cliente (limite de crédito menos valores pendentes/utilizados).
     */
    REJEITADO("Rejeitado");

    private final String descricao;

    /**
     * Construtor do enum com descrição.
     * 
     * @param descricao Descrição textual do status
     */
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém a descrição textual do status.
     * 
     * @return Descrição do status
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma string para o enum StatusPedido correspondente.
     * 
     * @param status String representando o status
     * @return Enum StatusPedido correspondente
     * @throws IllegalArgumentException se o status não for válido
     */
    public static StatusPedido fromString(String status) {
        for (StatusPedido statusPedido : StatusPedido.values()) {
            if (statusPedido.name().equalsIgnoreCase(status) || 
                statusPedido.descricao.equalsIgnoreCase(status)) {
                return statusPedido;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}

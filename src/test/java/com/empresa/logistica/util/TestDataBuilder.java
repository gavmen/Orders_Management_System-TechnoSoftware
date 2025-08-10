package com.empresa.logistica.util;

import com.empresa.logistica.model.Cliente;
import com.empresa.logistica.model.Produto;
import com.empresa.logistica.model.Pedido;
import com.empresa.logistica.model.ItemPedido;
import com.empresa.logistica.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Test data builder utility for creating test entities
 */
public final class TestDataBuilder {
    
    private TestDataBuilder() {}
    
    public static Cliente createCliente(String nome, BigDecimal limiteCredito) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setLimiteCredito(limiteCredito);
        return cliente;
    }
    
    public static Produto createProduto(String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        return produto;
    }
    
    public static Pedido createPedido(Cliente cliente, BigDecimal valorTotal, StatusPedido status) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(status);
        pedido.setDataPedido(LocalDateTime.now());
        return pedido;
    }
    
    public static ItemPedido createItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
        return item;
    }
}

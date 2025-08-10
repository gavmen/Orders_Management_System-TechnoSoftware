package com.empresa.logistica.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade ItemPedido.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
class ItemPedidoTest {

    private Cliente cliente;
    private Produto produto;
    private Pedido pedido;
    private ItemPedido itemPedido;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
        produto = new Produto("Notebook Dell", new BigDecimal("2500.00"));
        pedido = new Pedido(cliente);
        itemPedido = new ItemPedido(pedido, produto, 2);
    }

    @Test
    void testCriarItemPedidoComSucesso() {
        assertNotNull(itemPedido);
        assertEquals(pedido, itemPedido.getPedido());
        assertEquals(produto, itemPedido.getProduto());
        assertEquals(2, itemPedido.getQuantidade());
        assertEquals(new BigDecimal("5000.00"), itemPedido.getSubtotal()); // 2500 * 2
    }

    @Test
    void testConstrutorPadrao() {
        ItemPedido itemVazio = new ItemPedido();
        assertNotNull(itemVazio);
        assertNull(itemVazio.getPedido());
        assertNull(itemVazio.getProduto());
        assertNull(itemVazio.getQuantidade());
    }

    @Test
    void testCalcularSubtotal() {
        itemPedido.calcularSubtotal();
        assertEquals(new BigDecimal("5000.00"), itemPedido.getSubtotal());
        
        // Teste com quantidade diferente
        itemPedido.setQuantidade(3);
        itemPedido.calcularSubtotal();
        assertEquals(new BigDecimal("7500.00"), itemPedido.getSubtotal());
    }

    @Test
    void testCalcularSubtotalComValoresNulos() {
        ItemPedido item = new ItemPedido();
        item.calcularSubtotal();
        assertEquals(BigDecimal.ZERO, item.getSubtotal());
        
        item.setProduto(produto);
        item.calcularSubtotal();
        assertEquals(BigDecimal.ZERO, item.getSubtotal()); // quantidade ainda é null
        
        item.setQuantidade(2);
        item.calcularSubtotal();
        assertEquals(new BigDecimal("5000.00"), item.getSubtotal());
    }

    @Test
    void testSetProdutoRecalculaSubtotal() {
        Produto novoProduto = new Produto("Mouse", new BigDecimal("50.00"));
        
        itemPedido.setProduto(novoProduto);
        
        assertEquals(novoProduto, itemPedido.getProduto());
        assertEquals(new BigDecimal("100.00"), itemPedido.getSubtotal()); // 50 * 2
    }

    @Test
    void testSetQuantidadeRecalculaSubtotal() {
        itemPedido.setQuantidade(5);
        
        assertEquals(5, itemPedido.getQuantidade());
        assertEquals(new BigDecimal("12500.00"), itemPedido.getSubtotal()); // 2500 * 5
    }

    @Test
    void testGetPrecoUnitario() {
        assertEquals(new BigDecimal("2500.00"), itemPedido.getPrecoUnitario());
        
        ItemPedido itemSemProduto = new ItemPedido();
        assertEquals(BigDecimal.ZERO, itemSemProduto.getPrecoUnitario());
    }

    @Test
    void testIsValido() {
        assertTrue(itemPedido.isValido());
        
        // Item sem produto
        ItemPedido itemInvalido1 = new ItemPedido();
        itemInvalido1.setQuantidade(2);
        itemInvalido1.setSubtotal(new BigDecimal("100.00"));
        assertFalse(itemInvalido1.isValido());
        
        // Item com quantidade zero
        ItemPedido itemInvalido2 = new ItemPedido();
        itemInvalido2.setProduto(produto);
        itemInvalido2.setQuantidade(0);
        itemInvalido2.setSubtotal(BigDecimal.ZERO);
        assertFalse(itemInvalido2.isValido());
        
        // Item com subtotal zero
        ItemPedido itemInvalido3 = new ItemPedido();
        itemInvalido3.setProduto(produto);
        itemInvalido3.setQuantidade(2);
        itemInvalido3.setSubtotal(BigDecimal.ZERO);
        assertFalse(itemInvalido3.isValido());
    }

    @Test
    void testPreCalculation() {
        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(3);
        
        item.preCalculation();
        
        assertEquals(new BigDecimal("7500.00"), item.getSubtotal());
    }

    @Test
    void testEqualsEHashCode() {
        ItemPedido item1 = new ItemPedido(pedido, produto, 2);
        ItemPedido item2 = new ItemPedido(pedido, produto, 3);
        
        // Sem ID definido, devem ser diferentes
        assertNotEquals(item1, item2);
        
        // Com mesmo ID, devem ser iguais
        item1.setId(1L);
        item2.setId(1L);
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
        
        // Com IDs diferentes, devem ser diferentes
        item2.setId(2L);
        assertNotEquals(item1, item2);
    }

    @Test
    void testToString() {
        itemPedido.setId(1L);
        String resultado = itemPedido.toString();
        
        assertTrue(resultado.contains("ItemPedido"));
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("produto=Notebook Dell"));
        assertTrue(resultado.contains("quantidade=2"));
        assertTrue(resultado.contains("subtotal=5000.00"));
    }

    @Test
    void testSettersEGetters() {
        itemPedido.setId(1L);
        itemPedido.setSubtotal(new BigDecimal("1000.00"));
        
        assertEquals(1L, itemPedido.getId());
        assertEquals(new BigDecimal("1000.00"), itemPedido.getSubtotal());
    }
}

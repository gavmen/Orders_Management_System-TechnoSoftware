package com.empresa.logistica.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Produto.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto("Notebook Dell", new BigDecimal("2500.00"));
    }

    @Test
    void testCriarProdutoComSucesso() {
        assertNotNull(produto);
        assertEquals("Notebook Dell", produto.getNome());
        assertEquals(new BigDecimal("2500.00"), produto.getPreco());
        assertNotNull(produto.getItensPedido());
        assertTrue(produto.getItensPedido().isEmpty());
    }

    @Test
    void testConstrutorPadrao() {
        Produto produtoVazio = new Produto();
        assertNotNull(produtoVazio);
        assertNull(produtoVazio.getNome());
        assertNull(produtoVazio.getPreco());
        assertNotNull(produtoVazio.getItensPedido());
    }

    @Test
    void testAdicionarItemPedido() {
        Cliente cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
        Pedido pedido = new Pedido(cliente);
        ItemPedido item = new ItemPedido(pedido, produto, 2);
        
        produto.adicionarItemPedido(item);
        
        assertEquals(1, produto.getItensPedido().size());
        assertTrue(produto.getItensPedido().contains(item));
        assertEquals(produto, item.getProduto());
    }

    @Test
    void testRemoverItemPedido() {
        Cliente cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
        Pedido pedido = new Pedido(cliente);
        ItemPedido item = new ItemPedido(pedido, produto, 2);
        produto.adicionarItemPedido(item);
        
        produto.removerItemPedido(item);
        
        assertEquals(0, produto.getItensPedido().size());
        assertFalse(produto.getItensPedido().contains(item));
        assertNull(item.getProduto());
    }

    @Test
    void testEqualsEHashCode() {
        Produto produto1 = new Produto("Produto A", new BigDecimal("100.00"));
        Produto produto2 = new Produto("Produto B", new BigDecimal("200.00"));
        
        // Sem ID definido, devem ser diferentes
        assertNotEquals(produto1, produto2);
        
        // Com mesmo ID, devem ser iguais
        produto1.setId(1L);
        produto2.setId(1L);
        assertEquals(produto1, produto2);
        assertEquals(produto1.hashCode(), produto2.hashCode());
        
        // Com IDs diferentes, devem ser diferentes
        produto2.setId(2L);
        assertNotEquals(produto1, produto2);
    }

    @Test
    void testToString() {
        produto.setId(1L);
        String resultado = produto.toString();
        
        assertTrue(resultado.contains("Produto"));
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("nome='Notebook Dell'"));
        assertTrue(resultado.contains("preco=2500.00"));
    }

    @Test
    void testSettersEGetters() {
        produto.setId(1L);
        produto.setNome("Mouse Logitech");
        produto.setPreco(new BigDecimal("89.90"));
        
        assertEquals(1L, produto.getId());
        assertEquals("Mouse Logitech", produto.getNome());
        assertEquals(new BigDecimal("89.90"), produto.getPreco());
    }
}

package com.empresa.logistica.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Pedido.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
class PedidoTest {

    private Cliente cliente;
    private Produto produto;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
        produto = new Produto("Notebook Dell", new BigDecimal("2500.00"));
        pedido = new Pedido(cliente);
    }

    @Test
    void testCriarPedidoComSucesso() {
        assertNotNull(pedido);
        assertEquals(cliente, pedido.getCliente());
        assertNotNull(pedido.getDataPedido());
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
        assertEquals(StatusPedido.APROVADO, pedido.getStatus());
        assertNotNull(pedido.getItens());
        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    void testConstrutorPadrao() {
        Pedido pedidoVazio = new Pedido();
        assertNotNull(pedidoVazio);
        assertNotNull(pedidoVazio.getDataPedido());
        assertNotNull(pedidoVazio.getItens());
    }

    @Test
    void testAdicionarItem() {
        ItemPedido item = new ItemPedido(pedido, produto, 2);
        
        pedido.adicionarItem(item);
        
        assertEquals(1, pedido.getItens().size());
        assertTrue(pedido.getItens().contains(item));
        assertEquals(pedido, item.getPedido());
        assertEquals(new BigDecimal("5000.00"), pedido.getValorTotal()); // 2500 * 2
    }

    @Test
    void testRemoverItem() {
        ItemPedido item = new ItemPedido(pedido, produto, 2);
        pedido.adicionarItem(item);
        
        pedido.removerItem(item);
        
        assertEquals(0, pedido.getItens().size());
        assertFalse(pedido.getItens().contains(item));
        assertNull(item.getPedido());
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void testRecalcularValorTotal() {
        Produto produto2 = new Produto("Mouse", new BigDecimal("50.00"));
        
        ItemPedido item1 = new ItemPedido(pedido, produto, 1); // 2500.00
        ItemPedido item2 = new ItemPedido(pedido, produto2, 2); // 100.00
        
        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);
        
        assertEquals(new BigDecimal("2600.00"), pedido.getValorTotal());
    }

    @Test
    void testPossuiItens() {
        assertFalse(pedido.possuiItens());
        
        ItemPedido item = new ItemPedido(pedido, produto, 1);
        pedido.adicionarItem(item);
        
        assertTrue(pedido.possuiItens());
    }

    @Test
    void testGetQuantidadeTotalItens() {
        assertEquals(0, pedido.getQuantidadeTotalItens());
        
        ItemPedido item1 = new ItemPedido(pedido, produto, 2);
        ItemPedido item2 = new ItemPedido(pedido, produto, 3);
        
        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);
        
        assertEquals(5, pedido.getQuantidadeTotalItens());
    }

    @Test
    void testPrePersist() {
        Pedido novoPedido = new Pedido();
        novoPedido.setDataPedido(null);
        
        novoPedido.prePersist();
        
        assertNotNull(novoPedido.getDataPedido());
    }

    @Test
    void testEqualsEHashCode() {
        Pedido pedido1 = new Pedido(cliente);
        Pedido pedido2 = new Pedido(cliente);
        
        // Sem ID definido, devem ser diferentes
        assertNotEquals(pedido1, pedido2);
        
        // Com mesmo ID, devem ser iguais
        pedido1.setId(1L);
        pedido2.setId(1L);
        assertEquals(pedido1, pedido2);
        assertEquals(pedido1.hashCode(), pedido2.hashCode());
        
        // Com IDs diferentes, devem ser diferentes
        pedido2.setId(2L);
        assertNotEquals(pedido1, pedido2);
    }

    @Test
    void testToString() {
        pedido.setId(1L);
        String resultado = pedido.toString();
        
        assertTrue(resultado.contains("Pedido"));
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("cliente=João Silva"));
        assertTrue(resultado.contains("status=APROVADO")); // Changed to match enum value
        assertTrue(resultado.contains("quantidadeItens=0"));
    }

    @Test
    void testSettersEGetters() {
        LocalDateTime dataEsperada = LocalDateTime.now();
        
        pedido.setId(1L);
        pedido.setDataPedido(dataEsperada);
        pedido.setValorTotal(new BigDecimal("1000.00"));
        pedido.setStatus(StatusPedido.REJEITADO);
        
        assertEquals(1L, pedido.getId());
        assertEquals(dataEsperada, pedido.getDataPedido());
        assertEquals(new BigDecimal("1000.00"), pedido.getValorTotal());
        assertEquals(StatusPedido.REJEITADO, pedido.getStatus());
    }
}

package com.empresa.logistica.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Cliente.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
    }

    @Test
    void testCriarClienteComSucesso() {
        assertNotNull(cliente);
        assertEquals("João Silva", cliente.getNome());
        assertEquals(new BigDecimal("5000.00"), cliente.getLimiteCredito());
        assertNotNull(cliente.getPedidos());
        assertTrue(cliente.getPedidos().isEmpty());
    }

    @Test
    void testConstrutorPadrao() {
        Cliente clienteVazio = new Cliente();
        assertNotNull(clienteVazio);
        assertNull(clienteVazio.getNome());
        assertNull(clienteVazio.getLimiteCredito());
        assertNotNull(clienteVazio.getPedidos());
    }

    @Test
    void testAdicionarPedido() {
        Pedido pedido = new Pedido(cliente);
        
        cliente.adicionarPedido(pedido);
        
        assertEquals(1, cliente.getPedidos().size());
        assertTrue(cliente.getPedidos().contains(pedido));
        assertEquals(cliente, pedido.getCliente());
    }

    @Test
    void testRemoverPedido() {
        Pedido pedido = new Pedido(cliente);
        cliente.adicionarPedido(pedido);
        
        cliente.removerPedido(pedido);
        
        assertEquals(0, cliente.getPedidos().size());
        assertFalse(cliente.getPedidos().contains(pedido));
        assertNull(pedido.getCliente());
    }

    @Test
    void testEqualsEHashCode() {
        Cliente cliente1 = new Cliente("João Silva", new BigDecimal("5000.00"));
        Cliente cliente2 = new Cliente("Maria Santos", new BigDecimal("3000.00"));
        
        // Sem ID definido, devem ser diferentes
        assertNotEquals(cliente1, cliente2);
        
        // Com mesmo ID, devem ser iguais
        cliente1.setId(1L);
        cliente2.setId(1L);
        assertEquals(cliente1, cliente2);
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
        
        // Com IDs diferentes, devem ser diferentes
        cliente2.setId(2L);
        assertNotEquals(cliente1, cliente2);
    }

    @Test
    void testToString() {
        cliente.setId(1L);
        String resultado = cliente.toString();
        
        assertTrue(resultado.contains("Cliente"));
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("nome='João Silva'"));
        assertTrue(resultado.contains("limiteCredito=5000.00"));
    }

    @Test
    void testSettersEGetters() {
        cliente.setId(1L);
        cliente.setNome("Maria Santos");
        cliente.setLimiteCredito(new BigDecimal("7500.00"));
        
        assertEquals(1L, cliente.getId());
        assertEquals("Maria Santos", cliente.getNome());
        assertEquals(new BigDecimal("7500.00"), cliente.getLimiteCredito());
    }
}

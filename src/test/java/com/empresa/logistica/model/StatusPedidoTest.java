package com.empresa.logistica.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o enum StatusPedido.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
class StatusPedidoTest {

    @Test
    void testEnumValues() {
        assertEquals(2, StatusPedido.values().length);
        
        assertEquals(StatusPedido.APROVADO, StatusPedido.valueOf("APROVADO"));
        assertEquals(StatusPedido.REJEITADO, StatusPedido.valueOf("REJEITADO"));
    }

    @Test
    void testDescricoes() {
        assertEquals("Aprovado", StatusPedido.APROVADO.getDescricao());
        assertEquals("Rejeitado", StatusPedido.REJEITADO.getDescricao());
    }

    @Test
    void testToString() {
        assertEquals("Aprovado", StatusPedido.APROVADO.toString());
        assertEquals("Rejeitado", StatusPedido.REJEITADO.toString());
    }

    @Test
    void testFromStringComNomeEnum() {
        assertEquals(StatusPedido.APROVADO, StatusPedido.fromString("APROVADO"));
        assertEquals(StatusPedido.REJEITADO, StatusPedido.fromString("REJEITADO"));
        
        // Teste case-insensitive
        assertEquals(StatusPedido.APROVADO, StatusPedido.fromString("aprovado"));
        assertEquals(StatusPedido.REJEITADO, StatusPedido.fromString("rejeitado"));
    }

    @Test
    void testFromStringComDescricao() {
        assertEquals(StatusPedido.APROVADO, StatusPedido.fromString("Aprovado"));
        assertEquals(StatusPedido.REJEITADO, StatusPedido.fromString("Rejeitado"));
        
        // Teste case-insensitive
        assertEquals(StatusPedido.APROVADO, StatusPedido.fromString("aprovado"));
        assertEquals(StatusPedido.REJEITADO, StatusPedido.fromString("rejeitado"));
    }

    @Test
    void testFromStringComValorInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> StatusPedido.fromString("PENDENTE")
        );
        
        assertEquals("Status inválido: PENDENTE", exception.getMessage());
    }

    @Test
    void testFromStringComValorNulo() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> StatusPedido.fromString(null)
        );
    }

    @Test
    void testFromStringComValorVazio() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> StatusPedido.fromString("")
        );
        
        assertEquals("Status inválido: ", exception.getMessage());
    }
}

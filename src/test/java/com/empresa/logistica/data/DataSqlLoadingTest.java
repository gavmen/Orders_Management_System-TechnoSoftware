package com.empresa.logistica.data;

import com.empresa.logistica.repository.ClienteRepository;
import com.empresa.logistica.repository.ProdutoRepository;
import com.empresa.logistica.repository.PedidoRepository;
import com.empresa.logistica.repository.ItemPedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify that data.sql is loaded correctly at application startup
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.defer-datasource-initialization=true",
    "spring.sql.init.mode=always"
})
@Transactional
class DataSqlLoadingTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Test
    void testDataSqlLoadedSuccessfully() {
        // Verify that data.sql loaded the expected number of records
        
        // Should have 5 customers
        long clienteCount = clienteRepository.count();
        assertEquals(5, clienteCount, "Should have 5 customers loaded from data.sql");
        
        // Should have 5 products
        long produtoCount = produtoRepository.count();
        assertEquals(5, produtoCount, "Should have 5 products loaded from data.sql");
        
        // Should have 3 orders (2 APROVADO + 1 REJEITADO)
        long pedidoCount = pedidoRepository.count();
        assertEquals(3, pedidoCount, "Should have 3 orders loaded from data.sql");
        
        // Should have 8 order items total (3 + 3 + 2)
        long itemCount = itemPedidoRepository.count();
        assertEquals(8, itemCount, "Should have 8 order items loaded from data.sql");
    }

    @Test
    void testSpecificCustomersLoaded() {
        // Verify specific customers are loaded
        var clientes = clienteRepository.findAll();
        
        assertTrue(clientes.stream().anyMatch(c -> c.getNome().contains("João Silva Santos")),
                "João Silva Santos should be loaded");
        assertTrue(clientes.stream().anyMatch(c -> c.getNome().contains("Maria Oliveira Costa")),
                "Maria Oliveira Costa should be loaded");
        assertTrue(clientes.stream().anyMatch(c -> c.getNome().contains("Carlos Eduardo Lima")),
                "Carlos Eduardo Lima should be loaded");
    }

    @Test
    void testSpecificProductsLoaded() {
        // Verify specific products are loaded
        var produtos = produtoRepository.findAll();
        
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().contains("Notebook Dell")),
                "Notebook Dell should be loaded");
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().contains("Mouse Logitech")),
                "Mouse Logitech should be loaded");
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().contains("Monitor Samsung")),
                "Monitor Samsung should be loaded");
    }

    @Test
    void testOrderStatusDistribution() {
        // Verify we have both APROVADO and REJEITADO orders
        long aprovadosCount = pedidoRepository.countByStatus(com.empresa.logistica.model.StatusPedido.APROVADO);
        long rejeitadosCount = pedidoRepository.countByStatus(com.empresa.logistica.model.StatusPedido.REJEITADO);
        
        assertEquals(2, aprovadosCount, "Should have 2 APROVADO orders");
        assertEquals(1, rejeitadosCount, "Should have 1 REJEITADO order");
    }

    @Test
    void testCreditLimitValidationScenario() {
        // Test the 30-day totals calculation with loaded data
        // João Silva Santos should have orders from July 15, 2025 (within 30 days)
        var joaoOpt = clienteRepository.findAll().stream()
                .filter(c -> c.getNome().contains("João Silva Santos"))
                .findFirst();
        
        assertTrue(joaoOpt.isPresent(), "João Silva Santos should exist");
        
        var joao = joaoOpt.get();
        
        // Calculate 30 days ago from current date
        var dataInicio = java.time.LocalDateTime.now().minusDays(30);
        var totalUltimos30Dias = pedidoRepository.totalPedidosUltimos30Dias(joao.getId(), dataInicio);
        
        // João should have approved orders totaling some amount within 30 days
        assertNotNull(totalUltimos30Dias, "30-day total should not be null");
        assertTrue(totalUltimos30Dias.compareTo(java.math.BigDecimal.ZERO) > 0, 
                "30-day total should be greater than zero for João");
    }
}

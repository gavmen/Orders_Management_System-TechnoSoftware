package com.empresa.logistica.repository;

import com.empresa.logistica.model.Cliente;
import com.empresa.logistica.model.Pedido;
import com.empresa.logistica.model.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o PedidoRepository.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
class PedidoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Pedido pedido1;
    private Pedido pedido2;
    private Pedido pedido3;
    private Pedido pedido4;

    @BeforeEach
    void setUp() {
        // Criar clientes
        cliente1 = new Cliente("João Silva", new BigDecimal("5000.00"));
        cliente2 = new Cliente("Maria Santos", new BigDecimal("3000.00"));
        
        entityManager.persistAndFlush(cliente1);
        entityManager.persistAndFlush(cliente2);

        // Criar pedidos com diferentes datas e status
        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime vinteDiasAtras = hoje.minusDays(20);
        LocalDateTime quarentaDiasAtras = hoje.minusDays(40);

        // Pedido recente aprovado do cliente1
        pedido1 = new Pedido(cliente1);
        pedido1.setDataPedido(vinteDiasAtras);
        pedido1.setValorTotal(new BigDecimal("1500.00"));
        pedido1.setStatus(StatusPedido.APROVADO);

        // Pedido recente aprovado do cliente1
        pedido2 = new Pedido(cliente1);
        pedido2.setDataPedido(hoje.minusDays(10));
        pedido2.setValorTotal(new BigDecimal("2000.00"));
        pedido2.setStatus(StatusPedido.APROVADO);

        // Pedido antigo do cliente1 (fora dos 30 dias)
        pedido3 = new Pedido(cliente1);
        pedido3.setDataPedido(quarentaDiasAtras);
        pedido3.setValorTotal(new BigDecimal("800.00"));
        pedido3.setStatus(StatusPedido.APROVADO);

        // Pedido recente rejeitado do cliente1
        pedido4 = new Pedido(cliente1);
        pedido4.setDataPedido(hoje.minusDays(15));
        pedido4.setValorTotal(new BigDecimal("1000.00"));
        pedido4.setStatus(StatusPedido.REJEITADO);

        entityManager.persistAndFlush(pedido1);
        entityManager.persistAndFlush(pedido2);
        entityManager.persistAndFlush(pedido3);
        entityManager.persistAndFlush(pedido4);
    }

    @Test
    void testTotalPedidosUltimos30Dias() {
        LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);
        
        BigDecimal total = pedidoRepository.totalPedidosUltimos30Dias(cliente1.getId(), trintaDiasAtras);
        
        // Deve somar apenas os pedidos APROVADOS dos últimos 30 dias (1500 + 2000 = 3500)
        assertEquals(new BigDecimal("3500.00"), total);
    }

    @Test
    void testTotalPedidosUltimos30DiasClienteSemPedidos() {
        LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);
        
        BigDecimal total = pedidoRepository.totalPedidosUltimos30Dias(cliente2.getId(), trintaDiasAtras);
        
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testTotalPedidosUltimos30DiasApenasPedidosRejeitados() {
        // Criar um cliente que só tem pedidos rejeitados
        Cliente clienteComRejeitados = new Cliente("Cliente Rejeitado", new BigDecimal("1000.00"));
        entityManager.persistAndFlush(clienteComRejeitados);

        Pedido pedidoRejeitado = new Pedido(clienteComRejeitados);
        pedidoRejeitado.setDataPedido(LocalDateTime.now().minusDays(10));
        pedidoRejeitado.setValorTotal(new BigDecimal("500.00"));
        pedidoRejeitado.setStatus(StatusPedido.REJEITADO);
        entityManager.persistAndFlush(pedidoRejeitado);

        LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);
        BigDecimal total = pedidoRepository.totalPedidosUltimos30Dias(clienteComRejeitados.getId(), trintaDiasAtras);
        
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testFindByClienteId() {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(cliente1.getId());
        
        assertEquals(4, pedidos.size());
    }

    @Test
    void testFindByStatus() {
        List<Pedido> aprovados = pedidoRepository.findByStatus(StatusPedido.APROVADO);
        List<Pedido> rejeitados = pedidoRepository.findByStatus(StatusPedido.REJEITADO);
        
        assertEquals(3, aprovados.size());
        assertEquals(1, rejeitados.size());
    }

    @Test
    void testFindByClienteIdAndStatus() {
        List<Pedido> aprovadosCliente1 = pedidoRepository.findByClienteIdAndStatus(cliente1.getId(), StatusPedido.APROVADO);
        List<Pedido> rejeitadosCliente1 = pedidoRepository.findByClienteIdAndStatus(cliente1.getId(), StatusPedido.REJEITADO);
        
        assertEquals(3, aprovadosCliente1.size());
        assertEquals(1, rejeitadosCliente1.size());
    }

    @Test
    void testFindByDataPedidoBetween() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(25);
        LocalDateTime fim = LocalDateTime.now();
        
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(inicio, fim);
        
        assertEquals(3, pedidos.size()); // Exclui o pedido de 40 dias atrás
    }

    @Test
    void testFindByValorTotalGreaterThanEqual() {
        List<Pedido> pedidos = pedidoRepository.findByValorTotalGreaterThanEqual(new BigDecimal("1500.00"));
        
        assertEquals(2, pedidos.size());
    }

    @Test
    void testFindByValorTotalBetween() {
        List<Pedido> pedidos = pedidoRepository.findByValorTotalBetween(
            new BigDecimal("1000.00"), 
            new BigDecimal("2000.00")
        );
        
        assertEquals(3, pedidos.size());
    }

    @Test
    void testCountByClienteId() {
        long count = pedidoRepository.countByClienteId(cliente1.getId());
        
        assertEquals(4, count);
    }

    @Test
    void testCountByStatus() {
        long aprovados = pedidoRepository.countByStatus(StatusPedido.APROVADO);
        long rejeitados = pedidoRepository.countByStatus(StatusPedido.REJEITADO);
        
        assertEquals(3, aprovados);
        assertEquals(1, rejeitados);
    }

    @Test
    void testCalcularTotalPedidosAprovados() {
        BigDecimal total = pedidoRepository.calcularTotalPedidosAprovados();
        
        // 1500 + 2000 + 800 = 4300.00
        assertEquals(new BigDecimal("4300.00"), total);
    }

    @Test
    void testFindByClienteIdOrderByDataPedidoDesc() {
        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByDataPedidoDesc(cliente1.getId());
        
        assertEquals(4, pedidos.size());
        // Deve estar ordenado por data decrescente
        assertTrue(pedidos.get(0).getDataPedido().isAfter(pedidos.get(1).getDataPedido()));
    }

    @Test
    void testClienteTemPedidosRecentes() {
        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(20);
        
        boolean temPedidos = pedidoRepository.clienteTemPedidosRecentes(cliente1.getId(), vinteDiasAtras);
        boolean naoTemPedidos = pedidoRepository.clienteTemPedidosRecentes(cliente2.getId(), vinteDiasAtras);
        
        assertTrue(temPedidos);
        assertFalse(naoTemPedidos);
    }

    @Test
    void testSavePedido() {
        Pedido novoPedido = new Pedido(cliente2);
        novoPedido.setValorTotal(new BigDecimal("1200.00"));
        novoPedido.setStatus(StatusPedido.APROVADO);
        
        Pedido salvo = pedidoRepository.save(novoPedido);
        
        assertNotNull(salvo.getId());
        assertEquals(cliente2.getId(), salvo.getCliente().getId());
        assertEquals(new BigDecimal("1200.00"), salvo.getValorTotal());
        assertEquals(StatusPedido.APROVADO, salvo.getStatus());
    }
}

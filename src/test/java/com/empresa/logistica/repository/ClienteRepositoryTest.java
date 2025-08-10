package com.empresa.logistica.repository;

import com.empresa.logistica.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o ClienteRepository.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Cliente cliente3;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente("João Silva", new BigDecimal("5000.00"));
        cliente2 = new Cliente("Maria Santos", new BigDecimal("3000.00"));
        cliente3 = new Cliente("Pedro João", new BigDecimal("7500.00"));

        entityManager.persistAndFlush(cliente1);
        entityManager.persistAndFlush(cliente2);
        entityManager.persistAndFlush(cliente3);
    }

    @Test
    void testFindByNome() {
        Optional<Cliente> found = clienteRepository.findByNome("João Silva");
        
        assertTrue(found.isPresent());
        assertEquals("João Silva", found.get().getNome());
        assertEquals(new BigDecimal("5000.00"), found.get().getLimiteCredito());
    }

    @Test
    void testFindByNomeNaoEncontrado() {
        Optional<Cliente> found = clienteRepository.findByNome("Cliente Inexistente");
        
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        List<Cliente> found = clienteRepository.findByNomeContainingIgnoreCase("joão");
        
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(c -> c.getNome().equals("João Silva")));
        assertTrue(found.stream().anyMatch(c -> c.getNome().equals("Pedro João")));
    }

    @Test
    void testFindByLimiteCreditoGreaterThanEqual() {
        List<Cliente> found = clienteRepository.findByLimiteCreditoGreaterThanEqual(new BigDecimal("5000.00"));
        
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(c -> c.getLimiteCredito().compareTo(new BigDecimal("5000.00")) >= 0));
    }

    @Test
    void testFindByLimiteCreditoBetween() {
        List<Cliente> found = clienteRepository.findByLimiteCreditoBetween(
            new BigDecimal("3000.00"), 
            new BigDecimal("6000.00")
        );
        
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(c -> c.getNome().equals("João Silva")));
        assertTrue(found.stream().anyMatch(c -> c.getNome().equals("Maria Santos")));
    }

    @Test
    void testExistsByNome() {
        assertTrue(clienteRepository.existsByNome("João Silva"));
        assertFalse(clienteRepository.existsByNome("Cliente Inexistente"));
    }

    @Test
    void testCountByLimiteCreditoMaiorQue() {
        long count = clienteRepository.countByLimiteCreditoMaiorQue(new BigDecimal("4000.00"));
        
        assertEquals(2, count);
    }

    @Test
    void testFindAllOrderByLimiteCreditoDesc() {
        List<Cliente> clientes = clienteRepository.findAllOrderByLimiteCreditoDesc();
        
        assertEquals(3, clientes.size());
        assertEquals("Pedro João", clientes.get(0).getNome()); // 7500.00
        assertEquals("João Silva", clientes.get(1).getNome()); // 5000.00
        assertEquals("Maria Santos", clientes.get(2).getNome()); // 3000.00
    }

    @Test
    void testSaveCliente() {
        Cliente novoCliente = new Cliente("Ana Costa", new BigDecimal("4500.00"));
        
        Cliente salvo = clienteRepository.save(novoCliente);
        
        assertNotNull(salvo.getId());
        assertEquals("Ana Costa", salvo.getNome());
        assertEquals(new BigDecimal("4500.00"), salvo.getLimiteCredito());
    }

    @Test
    void testDeleteCliente() {
        Long clienteId = cliente1.getId();
        
        clienteRepository.deleteById(clienteId);
        
        assertFalse(clienteRepository.existsById(clienteId));
    }

    @Test
    void testUpdateCliente() {
        cliente1.setLimiteCredito(new BigDecimal("6000.00"));
        
        Cliente atualizado = clienteRepository.save(cliente1);
        
        assertEquals(new BigDecimal("6000.00"), atualizado.getLimiteCredito());
    }

    @Test
    void testFindAll() {
        List<Cliente> todos = clienteRepository.findAll();
        
        assertEquals(3, todos.size());
    }

    @Test
    void testCount() {
        long count = clienteRepository.count();
        
        assertEquals(3, count);
    }
}

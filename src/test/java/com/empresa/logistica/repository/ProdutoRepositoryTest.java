package com.empresa.logistica.repository;

import com.empresa.logistica.model.Produto;
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
 * Testes unitários para o ProdutoRepository.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook Dell", new BigDecimal("2500.00"));
        produto2 = new Produto("Mouse Logitech", new BigDecimal("89.90"));
        produto3 = new Produto("Teclado Dell", new BigDecimal("150.00"));

        entityManager.persistAndFlush(produto1);
        entityManager.persistAndFlush(produto2);
        entityManager.persistAndFlush(produto3);
    }

    @Test
    void testFindByNome() {
        Optional<Produto> found = produtoRepository.findByNome("Notebook Dell");
        
        assertTrue(found.isPresent());
        assertEquals("Notebook Dell", found.get().getNome());
        assertEquals(new BigDecimal("2500.00"), found.get().getPreco());
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        List<Produto> found = produtoRepository.findByNomeContainingIgnoreCase("dell");
        
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(p -> p.getNome().equals("Notebook Dell")));
        assertTrue(found.stream().anyMatch(p -> p.getNome().equals("Teclado Dell")));
    }

    @Test
    void testFindByPrecoGreaterThanEqual() {
        List<Produto> found = produtoRepository.findByPrecoGreaterThanEqual(new BigDecimal("150.00"));
        
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(p -> p.getPreco().compareTo(new BigDecimal("150.00")) >= 0));
    }

    @Test
    void testFindByPrecoLessThanEqual() {
        List<Produto> found = produtoRepository.findByPrecoLessThanEqual(new BigDecimal("200.00"));
        
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(p -> p.getNome().equals("Mouse Logitech")));
        assertTrue(found.stream().anyMatch(p -> p.getNome().equals("Teclado Dell")));
    }

    @Test
    void testFindByPrecoBetween() {
        List<Produto> found = produtoRepository.findByPrecoBetween(
            new BigDecimal("100.00"), 
            new BigDecimal("300.00")
        );
        
        assertEquals(1, found.size());
        assertEquals("Teclado Dell", found.get(0).getNome());
    }

    @Test
    void testExistsByNome() {
        assertTrue(produtoRepository.existsByNome("Notebook Dell"));
        assertFalse(produtoRepository.existsByNome("Produto Inexistente"));
    }

    @Test
    void testFindAllByOrderByPrecoAsc() {
        List<Produto> produtos = produtoRepository.findAllByOrderByPrecoAsc();
        
        assertEquals(3, produtos.size());
        assertEquals("Mouse Logitech", produtos.get(0).getNome()); // 89.90
        assertEquals("Teclado Dell", produtos.get(1).getNome()); // 150.00
        assertEquals("Notebook Dell", produtos.get(2).getNome()); // 2500.00
    }

    @Test
    void testFindAllByOrderByPrecoDesc() {
        List<Produto> produtos = produtoRepository.findAllByOrderByPrecoDesc();
        
        assertEquals(3, produtos.size());
        assertEquals("Notebook Dell", produtos.get(0).getNome()); // 2500.00
        assertEquals("Teclado Dell", produtos.get(1).getNome()); // 150.00
        assertEquals("Mouse Logitech", produtos.get(2).getNome()); // 89.90
    }

    @Test
    void testFindAllByOrderByNomeAsc() {
        List<Produto> produtos = produtoRepository.findAllByOrderByNomeAsc();
        
        assertEquals(3, produtos.size());
        assertEquals("Mouse Logitech", produtos.get(0).getNome());
        assertEquals("Notebook Dell", produtos.get(1).getNome());
        assertEquals("Teclado Dell", produtos.get(2).getNome());
    }

    @Test
    void testCountByPrecoMaiorQue() {
        long count = produtoRepository.countByPrecoMaiorQue(new BigDecimal("100.00"));
        
        assertEquals(2, count);
    }

    @Test
    void testCalcularPrecoMedio() {
        BigDecimal precoMedio = produtoRepository.calcularPrecoMedio();
        
        // (2500.00 + 89.90 + 150.00) / 3 = 913.30
        assertNotNull(precoMedio);
        assertEquals(0, new BigDecimal("913.30").compareTo(precoMedio));
    }

    @Test
    void testSaveProduto() {
        Produto novoProduto = new Produto("Monitor LG", new BigDecimal("899.99"));
        
        Produto salvo = produtoRepository.save(novoProduto);
        
        assertNotNull(salvo.getId());
        assertEquals("Monitor LG", salvo.getNome());
        assertEquals(new BigDecimal("899.99"), salvo.getPreco());
    }

    @Test
    void testUpdateProduto() {
        produto1.setPreco(new BigDecimal("2400.00"));
        
        Produto atualizado = produtoRepository.save(produto1);
        
        assertEquals(new BigDecimal("2400.00"), atualizado.getPreco());
    }

    @Test
    void testDeleteProduto() {
        Long produtoId = produto1.getId();
        
        produtoRepository.deleteById(produtoId);
        
        assertFalse(produtoRepository.existsById(produtoId));
    }

    @Test
    void testCount() {
        long count = produtoRepository.count();
        
        assertEquals(3, count);
    }
}

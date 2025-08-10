package com.empresa.logistica.repository;

import com.empresa.logistica.model.Cliente;
import com.empresa.logistica.model.ItemPedido;
import com.empresa.logistica.model.Pedido;
import com.empresa.logistica.model.Produto;
import com.empresa.logistica.model.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o ItemPedidoRepository.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
class ItemPedidoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;
    private Pedido pedido1;
    private Pedido pedido2;
    private ItemPedido item1;
    private ItemPedido item2;
    private ItemPedido item3;

    @BeforeEach
    void setUp() {
        // Criar cliente
        cliente = new Cliente("João Silva", new BigDecimal("5000.00"));
        entityManager.persistAndFlush(cliente);

        // Criar produtos
        produto1 = new Produto("Notebook Dell", new BigDecimal("2500.00"));
        produto2 = new Produto("Mouse Logitech", new BigDecimal("89.90"));
        entityManager.persistAndFlush(produto1);
        entityManager.persistAndFlush(produto2);

        // Criar pedidos
        pedido1 = new Pedido(cliente);
        pedido1.setValorTotal(new BigDecimal("5179.80"));
        pedido1.setStatus(StatusPedido.APROVADO);
        
        pedido2 = new Pedido(cliente);
        pedido2.setValorTotal(new BigDecimal("179.80"));
        pedido2.setStatus(StatusPedido.APROVADO);

        entityManager.persistAndFlush(pedido1);
        entityManager.persistAndFlush(pedido2);

        // Criar itens de pedido
        item1 = new ItemPedido(pedido1, produto1, 2); // 2 * 2500.00 = 5000.00
        item2 = new ItemPedido(pedido1, produto2, 2); // 2 * 89.90 = 179.80
        item3 = new ItemPedido(pedido2, produto2, 2); // 2 * 89.90 = 179.80

        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        entityManager.persistAndFlush(item3);
    }

    @Test
    void testFindByPedidoId() {
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedido1.getId());
        
        assertEquals(2, itens.size());
        assertTrue(itens.stream().anyMatch(i -> i.getProduto().getNome().equals("Notebook Dell")));
        assertTrue(itens.stream().anyMatch(i -> i.getProduto().getNome().equals("Mouse Logitech")));
    }

    @Test
    void testFindByProdutoId() {
        List<ItemPedido> itens = itemPedidoRepository.findByProdutoId(produto2.getId());
        
        assertEquals(2, itens.size());
        assertTrue(itens.stream().allMatch(i -> i.getProduto().getNome().equals("Mouse Logitech")));
    }

    @Test
    void testFindByQuantidade() {
        List<ItemPedido> itens = itemPedidoRepository.findByQuantidade(2);
        
        assertEquals(3, itens.size());
    }

    @Test
    void testFindByQuantidadeGreaterThanEqual() {
        List<ItemPedido> itens = itemPedidoRepository.findByQuantidadeGreaterThanEqual(2);
        
        assertEquals(3, itens.size());
        assertTrue(itens.stream().allMatch(i -> i.getQuantidade() >= 2));
    }

    @Test
    void testFindBySubtotalGreaterThanEqual() {
        List<ItemPedido> itens = itemPedidoRepository.findBySubtotalGreaterThanEqual(new BigDecimal("200.00"));
        
        assertEquals(1, itens.size());
        assertEquals("Notebook Dell", itens.get(0).getProduto().getNome());
    }

    @Test
    void testFindBySubtotalBetween() {
        List<ItemPedido> itens = itemPedidoRepository.findBySubtotalBetween(
            new BigDecimal("100.00"), 
            new BigDecimal("200.00")
        );
        
        assertEquals(2, itens.size());
        assertTrue(itens.stream().allMatch(i -> i.getProduto().getNome().equals("Mouse Logitech")));
    }

    @Test
    void testCountByPedidoId() {
        long count = itemPedidoRepository.countByPedidoId(pedido1.getId());
        
        assertEquals(2, count);
    }

    @Test
    void testCountByProdutoId() {
        long count = itemPedidoRepository.countByProdutoId(produto2.getId());
        
        assertEquals(2, count);
    }

    @Test
    void testCalcularQuantidadeTotalVendida() {
        Long quantidadeTotal = itemPedidoRepository.calcularQuantidadeTotalVendida(produto2.getId());
        
        assertEquals(4L, quantidadeTotal); // 2 + 2 = 4
    }

    @Test
    void testCalcularQuantidadeTotalVendidaProdutoSemVendas() {
        Produto produtoNaoVendido = new Produto("Produto Novo", new BigDecimal("100.00"));
        entityManager.persistAndFlush(produtoNaoVendido);
        
        Long quantidadeTotal = itemPedidoRepository.calcularQuantidadeTotalVendida(produtoNaoVendido.getId());
        
        assertEquals(0L, quantidadeTotal);
    }

    @Test
    void testCalcularValorTotalVendasProduto() {
        BigDecimal valorTotal = itemPedidoRepository.calcularValorTotalVendasProduto(produto2.getId());
        
        // 179.80 + 179.80 = 359.60
        assertEquals(new BigDecimal("359.60"), valorTotal);
    }

    @Test
    void testCalcularSubtotalMedio() {
        BigDecimal subtotalMedio = itemPedidoRepository.calcularSubtotalMedio();
        
        // (5000.00 + 179.80 + 179.80) / 3 = 1786.533333...
        assertNotNull(subtotalMedio);
        // Use scale comparison to handle precision
        assertTrue(subtotalMedio.setScale(2, RoundingMode.HALF_UP)
                  .compareTo(new BigDecimal("1786.53")) == 0);
    }

    @Test
    void testCalcularQuantidadeMedia() {
        Double quantidadeMedia = itemPedidoRepository.calcularQuantidadeMedia();
        
        // (2 + 2 + 2) / 3 = 2.0
        assertEquals(2.0, quantidadeMedia);
    }

    @Test
    void testFindItensPorCliente() {
        List<ItemPedido> itens = itemPedidoRepository.findItensPorCliente(cliente.getId());
        
        assertEquals(3, itens.size());
    }

    @Test
    void testExistsByProdutoId() {
        assertTrue(itemPedidoRepository.existsByProdutoId(produto1.getId()));
        assertTrue(itemPedidoRepository.existsByProdutoId(produto2.getId()));
        
        Produto produtoNaoVendido = new Produto("Produto Novo", new BigDecimal("100.00"));
        entityManager.persistAndFlush(produtoNaoVendido);
        
        assertFalse(itemPedidoRepository.existsByProdutoId(produtoNaoVendido.getId()));
    }

    @Test
    void testSaveItemPedido() {
        Produto novoProduto = new Produto("Teclado", new BigDecimal("200.00"));
        entityManager.persistAndFlush(novoProduto);
        
        ItemPedido novoItem = new ItemPedido(pedido2, novoProduto, 1);
        
        ItemPedido salvo = itemPedidoRepository.save(novoItem);
        
        assertNotNull(salvo.getId());
        assertEquals(pedido2.getId(), salvo.getPedido().getId());
        assertEquals(novoProduto.getId(), salvo.getProduto().getId());
        assertEquals(1, salvo.getQuantidade());
        assertEquals(new BigDecimal("200.00"), salvo.getSubtotal());
    }

    @Test
    void testUpdateItemPedido() {
        item1.setQuantidade(3);
        item1.calcularSubtotal(); // Recalcular subtotal
        
        ItemPedido atualizado = itemPedidoRepository.save(item1);
        
        assertEquals(3, atualizado.getQuantidade());
        assertEquals(new BigDecimal("7500.00"), atualizado.getSubtotal()); // 3 * 2500.00
    }

    @Test
    void testDeleteItemPedido() {
        Long itemId = item1.getId();
        
        itemPedidoRepository.deleteById(itemId);
        
        assertFalse(itemPedidoRepository.existsById(itemId));
    }
}

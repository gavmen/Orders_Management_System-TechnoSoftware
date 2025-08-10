package com.empresa.logistica.repository;

import com.empresa.logistica.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositório para operações de persistência da entidade ItemPedido.
 * 
 * Fornece operações CRUD básicas através do JpaRepository e métodos
 * customizados para consultas específicas relacionadas aos itens de pedidos.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    /**
     * Busca itens de pedido por ID do pedido.
     * 
     * @param pedidoId ID do pedido
     * @return Lista de itens do pedido especificado
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);

    /**
     * Busca itens de pedido por ID do produto.
     * 
     * @param produtoId ID do produto
     * @return Lista de itens que contêm o produto especificado
     */
    List<ItemPedido> findByProdutoId(Long produtoId);

    /**
     * Busca itens de pedido por quantidade.
     * 
     * @param quantidade Quantidade específica
     * @return Lista de itens com a quantidade especificada
     */
    List<ItemPedido> findByQuantidade(Integer quantidade);

    /**
     * Busca itens com quantidade maior ou igual ao valor especificado.
     * 
     * @param quantidade Quantidade mínima
     * @return Lista de itens que atendem ao critério
     */
    List<ItemPedido> findByQuantidadeGreaterThanEqual(Integer quantidade);

    /**
     * Busca itens com subtotal maior ou igual ao valor especificado.
     * 
     * @param subtotal Valor mínimo do subtotal
     * @return Lista de itens que atendem ao critério
     */
    List<ItemPedido> findBySubtotalGreaterThanEqual(BigDecimal subtotal);

    /**
     * Busca itens com subtotal entre os valores especificados.
     * 
     * @param subtotalMinimo Valor mínimo do subtotal
     * @param subtotalMaximo Valor máximo do subtotal
     * @return Lista de itens que atendem ao critério
     */
    List<ItemPedido> findBySubtotalBetween(BigDecimal subtotalMinimo, BigDecimal subtotalMaximo);

    /**
     * Conta o número de itens de um pedido específico.
     * 
     * @param pedidoId ID do pedido
     * @return Número de itens do pedido
     */
    long countByPedidoId(Long pedidoId);

    /**
     * Conta o número de vezes que um produto foi pedido.
     * 
     * @param produtoId ID do produto
     * @return Número de vezes que o produto foi pedido
     */
    long countByProdutoId(Long produtoId);

    /**
     * Calcula a quantidade total de um produto vendido.
     * 
     * @param produtoId ID do produto
     * @return Quantidade total vendida do produto
     */
    @Query("SELECT COALESCE(SUM(i.quantidade), 0) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    Long calcularQuantidadeTotalVendida(@Param("produtoId") Long produtoId);

    /**
     * Calcula o valor total de vendas de um produto.
     * 
     * @param produtoId ID do produto
     * @return Valor total das vendas do produto
     */
    @Query("SELECT COALESCE(SUM(i.subtotal), 0) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    BigDecimal calcularValorTotalVendasProduto(@Param("produtoId") Long produtoId);

    /**
     * Busca os produtos mais vendidos (por quantidade).
     * 
     * @param limite Número máximo de produtos a serem retornados
     * @return Lista de arrays com [produto_id, quantidade_total] ordenada por quantidade
     */
    @Query("SELECT i.produto.id, SUM(i.quantidade) as total FROM ItemPedido i GROUP BY i.produto.id ORDER BY total DESC")
    List<Object[]> findProdutosMaisVendidosPorQuantidade(@Param("limite") int limite);

    /**
     * Busca os produtos mais lucrativos (por valor).
     * 
     * @param limite Número máximo de produtos a serem retornados
     * @return Lista de arrays com [produto_id, valor_total] ordenada por valor
     */
    @Query("SELECT i.produto.id, SUM(i.subtotal) as total FROM ItemPedido i GROUP BY i.produto.id ORDER BY total DESC")
    List<Object[]> findProdutosMaisLucrativosPorValor(@Param("limite") int limite);

    /**
     * Calcula o subtotal médio dos itens de pedido.
     * 
     * @return Subtotal médio dos itens ou null se não houver itens
     */
    @Query("SELECT AVG(i.subtotal) FROM ItemPedido i")
    BigDecimal calcularSubtotalMedio();

    /**
     * Calcula a quantidade média dos itens de pedido.
     * 
     * @return Quantidade média dos itens ou null se não houver itens
     */
    @Query("SELECT AVG(i.quantidade) FROM ItemPedido i")
    Double calcularQuantidadeMedia();

    /**
     * Busca itens de pedido de um cliente específico através do relacionamento.
     * 
     * @param clienteId ID do cliente
     * @return Lista de itens de pedidos do cliente
     */
    @Query("SELECT i FROM ItemPedido i WHERE i.pedido.cliente.id = :clienteId")
    List<ItemPedido> findItensPorCliente(@Param("clienteId") Long clienteId);

    /**
     * Busca os itens com maior subtotal limitando o número de resultados.
     * 
     * @param limite Número máximo de itens a serem retornados
     * @return Lista dos itens com maior subtotal
     */
    @Query("SELECT i FROM ItemPedido i ORDER BY i.subtotal DESC")
    List<ItemPedido> findItensMaiorSubtotal(@Param("limite") int limite);

    /**
     * Remove todos os itens de um pedido específico.
     * 
     * @param pedidoId ID do pedido
     */
    void deleteByPedidoId(Long pedidoId);

    /**
     * Verifica se existe algum item para um produto específico.
     * 
     * @param produtoId ID do produto
     * @return true se existir algum item com o produto, false caso contrário
     */
    boolean existsByProdutoId(Long produtoId);
}

package com.empresa.logistica.repository;

import com.empresa.logistica.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência da entidade Produto.
 * 
 * Fornece operações CRUD básicas através do JpaRepository e métodos
 * customizados para consultas específicas relacionadas aos produtos.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca um produto pelo nome exato.
     * 
     * @param nome Nome do produto a ser buscado
     * @return Optional contendo o produto encontrado ou empty se não existir
     */
    Optional<Produto> findByNome(String nome);

    /**
     * Busca produtos cujo nome contenha a string fornecida (case-insensitive).
     * 
     * @param nome Parte do nome a ser buscada
     * @return Lista de produtos que atendem ao critério
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca produtos com preço maior ou igual ao valor especificado.
     * 
     * @param preco Valor mínimo do preço
     * @return Lista de produtos que atendem ao critério
     */
    List<Produto> findByPrecoGreaterThanEqual(BigDecimal preco);

    /**
     * Busca produtos com preço menor ou igual ao valor especificado.
     * 
     * @param preco Valor máximo do preço
     * @return Lista de produtos que atendem ao critério
     */
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    /**
     * Busca produtos com preço entre os valores especificados.
     * 
     * @param precoMinimo Valor mínimo do preço
     * @param precoMaximo Valor máximo do preço
     * @return Lista de produtos que atendem ao critério
     */
    List<Produto> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    /**
     * Verifica se existe um produto com o nome especificado.
     * 
     * @param nome Nome do produto a ser verificado
     * @return true se existir um produto com o nome, false caso contrário
     */
    boolean existsByNome(String nome);

    /**
     * Busca produtos ordenados por preço em ordem crescente.
     * 
     * @return Lista de produtos ordenada por preço (menor para maior)
     */
    List<Produto> findAllByOrderByPrecoAsc();

    /**
     * Busca produtos ordenados por preço em ordem decrescente.
     * 
     * @return Lista de produtos ordenada por preço (maior para menor)
     */
    List<Produto> findAllByOrderByPrecoDesc();

    /**
     * Busca produtos ordenados por nome em ordem alfabética.
     * 
     * @return Lista de produtos ordenada por nome
     */
    List<Produto> findAllByOrderByNomeAsc();

    /**
     * Conta o número de produtos com preço maior que o valor especificado.
     * 
     * @param preco Valor do preço para comparação
     * @return Número de produtos que atendem ao critério
     */
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.preco > :preco")
    long countByPrecoMaiorQue(@Param("preco") BigDecimal preco);

    /**
     * Calcula o preço médio de todos os produtos.
     * 
     * @return Preço médio dos produtos ou null se não houver produtos
     */
    @Query("SELECT AVG(p.preco) FROM Produto p")
    BigDecimal calcularPrecoMedio();

    /**
     * Busca os produtos mais caros limitando o número de resultados.
     * 
     * @param limite Número máximo de produtos a serem retornados
     * @return Lista dos produtos mais caros
     */
    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> findTopProdutosMaisCaros(@Param("limite") int limite);

    /**
     * Busca os produtos mais baratos limitando o número de resultados.
     * 
     * @param limite Número máximo de produtos a serem retornados
     * @return Lista dos produtos mais baratos
     */
    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> findTopProdutosMaisBaratos(@Param("limite") int limite);
    
    /**
     * Busca produtos por nome (busca parcial, case insensitive) com paginação.
     * 
     * @param nome Parte do nome do produto a ser buscado
     * @param pageable Configuração de paginação
     * @return Página de produtos que contém o termo no nome
     */
    org.springframework.data.domain.Page<Produto> findByNomeContainingIgnoreCase(
        String nome, org.springframework.data.domain.Pageable pageable);
    
    /**
     * Busca produtos com preço dentro de uma faixa com paginação.
     * 
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @param pageable Configuração de paginação
     * @return Página de produtos dentro da faixa de preço
     */
    org.springframework.data.domain.Page<Produto> findByPrecoBetween(
        BigDecimal precoMin, BigDecimal precoMax, org.springframework.data.domain.Pageable pageable);
    
    /**
     * Busca produtos com preço maior ou igual ao valor especificado com paginação.
     * 
     * @param preco Valor mínimo do preço
     * @param pageable Configuração de paginação
     * @return Página de produtos que atendem ao critério
     */
    org.springframework.data.domain.Page<Produto> findByPrecoGreaterThanEqual(
        BigDecimal preco, org.springframework.data.domain.Pageable pageable);
    
    /**
     * Busca produtos com preço menor ou igual ao valor especificado com paginação.
     * 
     * @param preco Valor máximo do preço
     * @param pageable Configuração de paginação
     * @return Página de produtos que atendem ao critério
     */
    org.springframework.data.domain.Page<Produto> findByPrecoLessThanEqual(
        BigDecimal preco, org.springframework.data.domain.Pageable pageable);
}

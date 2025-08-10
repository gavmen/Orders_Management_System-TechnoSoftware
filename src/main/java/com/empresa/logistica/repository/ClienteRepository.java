package com.empresa.logistica.repository;

import com.empresa.logistica.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência da entidade Cliente.
 * 
 * Fornece operações CRUD básicas através do JpaRepository e métodos
 * customizados para consultas específicas relacionadas aos clientes.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca um cliente pelo nome exato.
     * 
     * @param nome Nome do cliente a ser buscado
     * @return Optional contendo o cliente encontrado ou empty se não existir
     */
    Optional<Cliente> findByNome(String nome);

    /**
     * Busca clientes cujo nome contenha a string fornecida (case-insensitive).
     * 
     * @param nome Parte do nome a ser buscada
     * @return Lista de clientes que atendem ao critério
     */
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca clientes com limite de crédito maior ou igual ao valor especificado.
     * 
     * @param limiteCredito Valor mínimo do limite de crédito
     * @return Lista de clientes que atendem ao critério
     */
    List<Cliente> findByLimiteCreditoGreaterThanEqual(BigDecimal limiteCredito);

    /**
     * Busca clientes com limite de crédito entre os valores especificados.
     * 
     * @param limiteMinimo Valor mínimo do limite de crédito
     * @param limiteMaximo Valor máximo do limite de crédito
     * @return Lista de clientes que atendem ao critério
     */
    List<Cliente> findByLimiteCreditoBetween(BigDecimal limiteMinimo, BigDecimal limiteMaximo);

    /**
     * Verifica se existe um cliente com o nome especificado.
     * 
     * @param nome Nome do cliente a ser verificado
     * @return true se existir um cliente com o nome, false caso contrário
     */
    boolean existsByNome(String nome);

    /**
     * Conta o número de clientes com limite de crédito maior que o valor especificado.
     * 
     * @param limiteCredito Valor do limite de crédito para comparação
     * @return Número de clientes que atendem ao critério
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.limiteCredito > :limiteCredito")
    long countByLimiteCreditoMaiorQue(@Param("limiteCredito") BigDecimal limiteCredito);

    /**
     * Busca clientes ordenados por limite de crédito em ordem decrescente.
     * 
     * @return Lista de clientes ordenada por limite de crédito (maior para menor)
     */
    @Query("SELECT c FROM Cliente c ORDER BY c.limiteCredito DESC")
    List<Cliente> findAllOrderByLimiteCreditoDesc();

    /**
     * Busca os clientes com os maiores limites de crédito.
     * 
     * @param limite Número máximo de clientes a serem retornados
     * @return Lista dos clientes com maiores limites de crédito
     */
    @Query("SELECT c FROM Cliente c ORDER BY c.limiteCredito DESC")
    List<Cliente> findTopClientesPorLimiteCredito(@Param("limite") int limite);
    
    /**
     * Busca clientes por nome (busca parcial, case insensitive) com paginação.
     * 
     * @param nome Parte do nome do cliente a ser buscado
     * @param pageable Configuração de paginação
     * @return Página de clientes que contém o termo no nome
     */
    org.springframework.data.domain.Page<Cliente> findByNomeContainingIgnoreCase(
        String nome, org.springframework.data.domain.Pageable pageable);
}

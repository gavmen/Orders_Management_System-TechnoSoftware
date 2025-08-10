package com.empresa.logistica.repository;

import com.empresa.logistica.model.Pedido;
import com.empresa.logistica.model.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência da entidade Pedido.
 * 
 * Fornece operações CRUD básicas através do JpaRepository e métodos
 * customizados para consultas específicas relacionadas aos pedidos,
 * incluindo validações de limite de crédito.
 * 
 * @author Gabriel Mendonca
 * @version 1.0
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Calcula o total de pedidos de um cliente nos últimos 30 dias.
     * 
     * Esta query é fundamental para a validação do limite de crédito,
     * calculando a soma dos valores dos pedidos aprovados do cliente
     * a partir da data especificada.
     * 
     * @param clienteId ID do cliente
     * @param dataInicio Data de início para o cálculo (30 dias atrás)
     * @return Soma total dos pedidos ou BigDecimal.ZERO se não houver pedidos
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataPedido >= :dataInicio AND p.status = 'APROVADO'")
    BigDecimal totalPedidosUltimos30Dias(@Param("clienteId") Long clienteId, @Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Busca pedidos de um cliente específico.
     * 
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente
     */
    List<Pedido> findByClienteId(Long clienteId);
    
    /**
     * Busca um pedido com seus itens carregados (evita LazyInitializationException)
     * 
     * @param id ID do pedido
     * @return Optional com pedido e itens carregados
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens LEFT JOIN FETCH p.cliente WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);
    
    /**
     * Busca todos os pedidos com paginação e joins otimizados
     * 
     * @param pageable configuração de paginação
     * @return Page com pedidos e dados relacionados
     */
    @Query(value = "SELECT p FROM Pedido p LEFT JOIN FETCH p.cliente", 
           countQuery = "SELECT COUNT(p) FROM Pedido p")
    Page<Pedido> findAllWithCliente(Pageable pageable);
    
    /**
     * Busca pedidos de um cliente específico com paginação.
     * 
     * @param clienteId ID do cliente
     * @param pageable Configuração de paginação
     * @return Página de pedidos do cliente
     */
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    /**
     * Busca pedidos por status.
     * 
     * @param status Status dos pedidos a serem buscados
     * @return Lista de pedidos com o status especificado
     */
    List<Pedido> findByStatus(StatusPedido status);
    
    /**
     * Busca pedidos por status com paginação.
     * 
     * @param status Status dos pedidos a serem buscados
     * @param pageable Configuração de paginação
     * @return Página de pedidos com o status especificado
     */
    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);

    /**
     * Busca pedidos de um cliente com status específico.
     * 
     * @param clienteId ID do cliente
     * @param status Status dos pedidos
     * @return Lista de pedidos que atendem aos critérios
     */
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    /**
     * Busca pedidos por período de data.
     * 
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de pedidos no período especificado
     */
    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca pedidos com valor total maior ou igual ao especificado.
     * 
     * @param valorMinimo Valor mínimo do pedido
     * @return Lista de pedidos que atendem ao critério
     */
    List<Pedido> findByValorTotalGreaterThanEqual(BigDecimal valorMinimo);

    /**
     * Busca pedidos com valor total entre os valores especificados.
     * 
     * @param valorMinimo Valor mínimo do pedido
     * @param valorMaximo Valor máximo do pedido
     * @return Lista de pedidos que atendem ao critério
     */
    List<Pedido> findByValorTotalBetween(BigDecimal valorMinimo, BigDecimal valorMaximo);

    /**
     * Busca pedidos ordenados por data em ordem decrescente.
     * 
     * @return Lista de pedidos ordenada por data (mais recente primeiro)
     */
    List<Pedido> findAllByOrderByDataPedidoDesc();

    /**
     * Busca pedidos de um cliente ordenados por data em ordem decrescente.
     * 
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente ordenada por data
     */
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

    /**
     * Conta o número de pedidos de um cliente.
     * 
     * @param clienteId ID do cliente
     * @return Número de pedidos do cliente
     */
    long countByClienteId(Long clienteId);

    /**
     * Conta o número de pedidos por status.
     * 
     * @param status Status dos pedidos
     * @return Número de pedidos com o status especificado
     */
    long countByStatus(StatusPedido status);

    /**
     * Calcula o valor total de todos os pedidos aprovados.
     * 
     * @return Soma total dos pedidos aprovados
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.status = 'APROVADO'")
    BigDecimal calcularTotalPedidosAprovados();

    /**
     * Calcula o valor médio dos pedidos aprovados.
     * 
     * @return Valor médio dos pedidos aprovados ou null se não houver pedidos
     */
    @Query("SELECT AVG(p.valorTotal) FROM Pedido p WHERE p.status = 'APROVADO'")
    BigDecimal calcularValorMedioPedidos();

    /**
     * Busca pedidos de um cliente em um período específico.
     * 
     * @param clienteId ID do cliente
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de pedidos que atendem aos critérios
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataPedido BETWEEN :dataInicio AND :dataFim ORDER BY p.dataPedido DESC")
    List<Pedido> findPedidosClientePorPeriodo(@Param("clienteId") Long clienteId, 
                                             @Param("dataInicio") LocalDateTime dataInicio, 
                                             @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca os maiores pedidos aprovados limitando o número de resultados.
     * 
     * @param limite Número máximo de pedidos a serem retornados
     * @return Lista dos maiores pedidos aprovados
     */
    @Query("SELECT p FROM Pedido p WHERE p.status = 'APROVADO' ORDER BY p.valorTotal DESC")
    List<Pedido> findMaioresPedidosAprovados(@Param("limite") int limite);

    /**
     * Verifica se um cliente possui pedidos pendentes (aprovados) nos últimos dias.
     * 
     * @param clienteId ID do cliente
     * @param diasAtras Número de dias atrás para verificar
     * @return true se o cliente possui pedidos no período, false caso contrário
     */
    @Query("SELECT COUNT(p) > 0 FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataPedido >= :dataInicio AND p.status = 'APROVADO'")
    boolean clienteTemPedidosRecentes(@Param("clienteId") Long clienteId, @Param("dataInicio") LocalDateTime dataInicio);
    
    /**
     * Calcula o total de pedidos de um cliente em um período específico.
     * 
     * @param clienteId ID do cliente
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Total dos pedidos aprovados no período ou BigDecimal.ZERO se não houver pedidos
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataPedido BETWEEN :dataInicio AND :dataFim AND p.status = 'APROVADO'")
    BigDecimal totalPedidosPorClienteEPeriodo(@Param("clienteId") Long clienteId, 
                                              @Param("dataInicio") LocalDateTime dataInicio, 
                                              @Param("dataFim") LocalDateTime dataFim);
}

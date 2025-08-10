package com.empresa.logistica.service;

import com.empresa.logistica.dto.PedidoDTO;
import com.empresa.logistica.dto.request.CriarPedidoRequest;
import com.empresa.logistica.model.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service interface for Pedido business logic
 */
public interface PedidoService {
    
    /**
     * Creates a new order with credit limit validation
     */
    PedidoDTO criarPedido(CriarPedidoRequest request);
    
    /**
     * Find order by ID
     */
    PedidoDTO buscarPorId(Long id);
    
    /**
     * List all orders with pagination
     */
    Page<PedidoDTO> listarPedidos(Pageable pageable);
    
    /**
     * List orders by customer with pagination
     */
    Page<PedidoDTO> listarPorCliente(Long clienteId, Pageable pageable);
    
    /**
     * List orders by status with pagination
     */
    Page<PedidoDTO> listarPorStatus(StatusPedido status, Pageable pageable);
    
    /**
     * Calculate total orders for customer in date range
     */
    BigDecimal calcularTotalPorCliente(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim);
}

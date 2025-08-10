package com.empresa.logistica.service.impl;

import com.empresa.logistica.config.ApplicationConstants;
import com.empresa.logistica.dto.PedidoDTO;
import com.empresa.logistica.dto.request.CriarPedidoRequest;
import com.empresa.logistica.mapper.ClienteMapper;
import com.empresa.logistica.mapper.ProdutoMapper;
import com.empresa.logistica.model.*;
import com.empresa.logistica.repository.ClienteRepository;
import com.empresa.logistica.repository.PedidoRepository;
import com.empresa.logistica.repository.ProdutoRepository;
import com.empresa.logistica.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for Pedido business logic
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    
    @Override
    public PedidoDTO criarPedido(CriarPedidoRequest request) {
        log.info("Criando pedido para cliente {}", request.getClienteId());
        
        // 1. Validar cliente existe
        Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException(ApplicationConstants.CLIENTE_NAO_ENCONTRADO + request.getClienteId()));
        
        // 2. Buscar todos os produtos em uma única query para evitar N+1
        List<Long> produtoIds = request.getItens().stream()
            .map(item -> item.getProdutoId())
            .distinct()
            .toList();
        
        List<Produto> produtos = produtoRepository.findAllById(produtoIds);
        if (produtos.size() != produtoIds.size()) {
            throw new EntityNotFoundException("Um ou mais produtos não foram encontrados");
        }
        
        // Criar mapa para acesso O(1) aos produtos
        Map<Long, Produto> produtoMap = produtos.stream()
            .collect(Collectors.toMap(Produto::getId, produto -> produto));
        
        // 3. Calcular valor total do pedido
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (var item : request.getItens()) {
            Produto produto = produtoMap.get(item.getProdutoId());
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);
        }
        
        // 3. Validar limite de crédito - Saldo disponível
        // Buscar pedidos APROVADOS pendentes (não pagos/entregues) - simulamos como últimos 30 dias
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        BigDecimal valorPendente = pedidoRepository.totalPedidosUltimos30Dias(
            cliente.getId(), dataLimite);
        
        // Calcular saldo disponível
        BigDecimal saldoDisponivel = cliente.getLimiteCredito().subtract(valorPendente);
        StatusPedido status = valorTotal.compareTo(saldoDisponivel) <= 0 
            ? StatusPedido.APROVADO : StatusPedido.REJEITADO;
        
        // 4. Criar pedido
        Pedido pedido = new Pedido(cliente);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(status);
        
        // 5. Criar itens do pedido
        for (var itemDTO : request.getItens()) {
            Produto produto = produtoMap.get(itemDTO.getProdutoId());
            ItemPedido item = new ItemPedido(pedido, produto, itemDTO.getQuantidade());
            item.setSubtotal(itemDTO.getSubtotal());
            pedido.getItens().add(item);
        }
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        log.info("Pedido {} criado com status {}", pedidoSalvo.getId(), status);
        
        return mapToDTO(pedidoSalvo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithItens(id)
            .orElseThrow(() -> new EntityNotFoundException(ApplicationConstants.PEDIDO_NAO_ENCONTRADO + id));
        return mapToDTO(pedido);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PedidoDTO> listarPedidos(Pageable pageable) {
        return pedidoRepository.findAllWithCliente(pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PedidoDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        return pedidoRepository.findByClienteId(clienteId, pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PedidoDTO> listarPorStatus(StatusPedido status, Pageable pageable) {
        return pedidoRepository.findByStatus(status, pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPorCliente(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoRepository.totalPedidosPorClienteEPeriodo(clienteId, dataInicio, dataFim);
    }
    
    private PedidoDTO mapToDTO(Pedido pedido) {
        // Calcular valores de crédito para o DTO
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        BigDecimal valorPendente = pedidoRepository.totalPedidosUltimos30Dias(
            pedido.getCliente().getId(), dataLimite);
        BigDecimal saldoDisponivel = pedido.getCliente().getLimiteCredito().subtract(valorPendente);
        
        return PedidoDTO.builder()
            .id(pedido.getId())
            .clienteId(pedido.getCliente().getId())
            .clienteNome(pedido.getCliente().getNome())
            .dataPedido(pedido.getDataPedido())
            .status(pedido.getStatus())
            .valorTotal(pedido.getValorTotal())
            .itens(pedido.getItens().stream()
                .map(item -> com.empresa.logistica.dto.ItemPedidoDTO.builder()
                    .id(item.getId())
                    .pedidoId(item.getPedido().getId())
                    .produtoId(item.getProduto().getId())
                    .produtoNome(item.getProduto().getNome())
                    .quantidade(item.getQuantidade())
                    .subtotal(item.getSubtotal())
                    .precoUnitario(item.getProduto().getPreco())
                    .build())
                .collect(Collectors.toList()))
            .limiteCredito(pedido.getCliente().getLimiteCredito())
            .valorJaUtilizado(valorPendente)
            .saldoDisponivel(saldoDisponivel)
            .build();
    }
}

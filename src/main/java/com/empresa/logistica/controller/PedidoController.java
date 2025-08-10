package com.empresa.logistica.controller;

import com.empresa.logistica.dto.PedidoDTO;
import com.empresa.logistica.dto.request.CriarPedidoRequest;
import com.empresa.logistica.model.StatusPedido;
import com.empresa.logistica.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * REST Controller for Order management
 */
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        log.info("Criando pedido para cliente {}", request.getClienteId());
        PedidoDTO pedido = pedidoService.criarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando pedido {}", id);
        PedidoDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    @GetMapping
    public ResponseEntity<Page<PedidoDTO>> listarPedidos(
            @PageableDefault(size = 20, sort = "dataPedido", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        log.info("Listando pedidos - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<PedidoDTO> pedidos = pedidoService.listarPedidos(pageable);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoDTO>> listarPorCliente(
            @PathVariable Long clienteId,
            @PageableDefault(size = 20, sort = "dataPedido", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        log.info("Listando pedidos do cliente {} - página: {}", clienteId, pageable.getPageNumber());
        Page<PedidoDTO> pedidos = pedidoService.listarPorCliente(clienteId, pageable);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PedidoDTO>> listarPorStatus(
            @PathVariable StatusPedido status,
            @PageableDefault(size = 20, sort = "dataPedido", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        log.info("Listando pedidos com status {} - página: {}", status, pageable.getPageNumber());
        Page<PedidoDTO> pedidos = pedidoService.listarPorStatus(status, pageable);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/cliente/{clienteId}/total")
    public ResponseEntity<BigDecimal> calcularTotalPorCliente(
            @PathVariable Long clienteId,
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim) {
        
        log.info("Calculando total do cliente {} de {} até {}", clienteId, dataInicio, dataFim);
        BigDecimal total = pedidoService.calcularTotalPorCliente(clienteId, dataInicio, dataFim);
        return ResponseEntity.ok(total);
    }
}

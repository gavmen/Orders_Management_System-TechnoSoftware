package com.empresa.logistica.controller;

import com.empresa.logistica.dto.ClienteDTO;
import com.empresa.logistica.mapper.ClienteMapper;
import com.empresa.logistica.model.Cliente;
import com.empresa.logistica.repository.ClienteRepository;
import com.empresa.logistica.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * REST Controller for Customer management
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class ClienteController {
    
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PedidoRepository pedidoRepository;
    
    /**
     * GET /clientes - List all customers with pagination
     */
    @GetMapping
    public ResponseEntity<Page<ClienteDTO>> listarClientes(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        
        log.info("Listando clientes - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        Page<ClienteDTO> clientesDTO = clientes.map(clienteMapper::toDTO);
        
        return ResponseEntity.ok(clientesDTO);
    }
    
    /**
     * GET /clientes/all - List all customers without pagination (for dropdowns)
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClienteDTO>> listarTodosClientes() {
        log.info("Listando todos os clientes");
        
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clientesDTO = clientes.stream()
            .map(clienteMapper::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(clientesDTO);
    }
    
    /**
     * GET /clientes/{id} - Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando cliente {}", id);
        
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
        
        ClienteDTO clienteDTO = clienteMapper.toDTO(cliente);
        return ResponseEntity.ok(clienteDTO);
    }
    
    /**
     * GET /clientes/search - Search customers by name
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ClienteDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        
        log.info("Buscando clientes por nome: {}", nome);
        
        Page<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome, pageable);
        Page<ClienteDTO> clientesDTO = clientes.map(clienteMapper::toDTO);
        
        return ResponseEntity.ok(clientesDTO);
    }
    
    /**
     * GET /clientes/{id}/credito - Get real-time credit balance information
     */
    @GetMapping("/{id}/credito")
    public ResponseEntity<Map<String, Object>> getCreditoBalance(@PathVariable Long id) {
        log.info("Buscando informações de crédito do cliente {}", id);
        
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
        
        // Calculate used credit in last 30 days
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        BigDecimal valorUtilizado = pedidoRepository.totalPedidosUltimos30Dias(id, dataLimite);
        BigDecimal saldoDisponivel = cliente.getLimiteCredito().subtract(valorUtilizado);
        
        Map<String, Object> creditInfo = new HashMap<>();
        creditInfo.put("clienteId", cliente.getId());
        creditInfo.put("clienteNome", cliente.getNome());
        creditInfo.put("limiteCredito", cliente.getLimiteCredito());
        creditInfo.put("valorUtilizado", valorUtilizado);
        creditInfo.put("saldoDisponivel", saldoDisponivel);
        
        return ResponseEntity.ok(creditInfo);
    }
}

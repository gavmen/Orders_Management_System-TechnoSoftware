package com.empresa.logistica.mapper;

import com.empresa.logistica.dto.ClienteDTO;
import com.empresa.logistica.model.Cliente;
import org.springframework.stereotype.Component;

/**
 * Manual mapper for Cliente entity and DTO conversion with credit calculations
 */
@Component
public class ClienteMapper {
    
    public ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .limiteCredito(cliente.getLimiteCredito())
                // For now, set calculated fields to null - they will be calculated when needed
                .valorUtilizado(null)
                .saldoDisponivel(null)
                .totalPedidos(null)
                .build();
    }
    
    public Cliente toEntity(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.getId());
        cliente.setNome(clienteDTO.getNome());
        cliente.setLimiteCredito(clienteDTO.getLimiteCredito());
        return cliente;
    }
}

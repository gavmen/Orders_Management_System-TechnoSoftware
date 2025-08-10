package com.empresa.logistica.dto;

import com.empresa.logistica.model.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Pedido (Order)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    
    private Long id;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    private String clienteNome;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPedido;
    
    private StatusPedido status;
    
    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor total deve ser positivo")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal valorTotal;
    
    @Valid
    private List<ItemPedidoDTO> itens;
    
    // Campos para validação de crédito
    private BigDecimal limiteCredito;
    private BigDecimal valorJaUtilizado;
    private BigDecimal saldoDisponivel;
}

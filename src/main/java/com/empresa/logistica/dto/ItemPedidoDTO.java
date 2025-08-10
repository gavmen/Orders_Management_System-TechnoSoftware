package com.empresa.logistica.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Data Transfer Object for ItemPedido (Order Item)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    
    private Long id;
    
    private Long pedidoId;
    
    @NotNull(message = "Produto é obrigatório")
    @Positive(message = "ID do produto deve ser positivo")
    private Long produtoId;
    
    private String produtoNome;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal subtotal;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal precoUnitario;
}

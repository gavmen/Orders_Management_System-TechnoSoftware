package com.empresa.logistica.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Cliente (Customer)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Limite de crédito é obrigatório")
    @Positive(message = "Limite de crédito deve ser positivo")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal limiteCredito;
    
    // Campos calculados para relatórios
    private BigDecimal valorUtilizado;
    private BigDecimal saldoDisponivel;
    private Integer totalPedidos;
}

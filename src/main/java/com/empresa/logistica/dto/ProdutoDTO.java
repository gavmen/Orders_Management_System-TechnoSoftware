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
 * Data Transfer Object for Produto (Product)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal preco;
}

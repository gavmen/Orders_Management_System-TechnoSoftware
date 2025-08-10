package com.empresa.logistica.dto.request;

import com.empresa.logistica.dto.ItemPedidoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating new orders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarPedidoRequest {
    
    @NotNull(message = "Cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser positivo")
    private Long clienteId;
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid
    private List<ItemPedidoDTO> itens;
}

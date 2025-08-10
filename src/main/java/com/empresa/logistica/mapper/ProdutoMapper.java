package com.empresa.logistica.mapper;

import com.empresa.logistica.dto.ProdutoDTO;
import com.empresa.logistica.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for Produto entity and DTO conversion
 */
@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    
    ProdutoDTO toDTO(Produto produto);
    
    Produto toEntity(ProdutoDTO produtoDTO);
}

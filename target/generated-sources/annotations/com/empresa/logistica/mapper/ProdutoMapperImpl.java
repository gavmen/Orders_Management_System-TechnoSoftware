package com.empresa.logistica.mapper;

import com.empresa.logistica.dto.ProdutoDTO;
import com.empresa.logistica.model.Produto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-09T23:12:44-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public ProdutoDTO toDTO(Produto produto) {
        if ( produto == null ) {
            return null;
        }

        ProdutoDTO.ProdutoDTOBuilder produtoDTO = ProdutoDTO.builder();

        produtoDTO.id( produto.getId() );
        produtoDTO.nome( produto.getNome() );
        produtoDTO.preco( produto.getPreco() );

        return produtoDTO.build();
    }

    @Override
    public Produto toEntity(ProdutoDTO produtoDTO) {
        if ( produtoDTO == null ) {
            return null;
        }

        Produto produto = new Produto();

        produto.setId( produtoDTO.getId() );
        produto.setNome( produtoDTO.getNome() );
        produto.setPreco( produtoDTO.getPreco() );

        return produto;
    }
}

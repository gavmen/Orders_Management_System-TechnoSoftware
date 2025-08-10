package com.empresa.logistica.controller;

import com.empresa.logistica.dto.ProdutoDTO;
import com.empresa.logistica.mapper.ProdutoMapper;
import com.empresa.logistica.model.Produto;
import com.empresa.logistica.repository.ProdutoRepository;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Product management
 */
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class ProdutoController {
    
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    
    /**
     * GET /produtos - List all products with pagination
     */
    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listarProdutos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        
        log.info("Listando produtos - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        Page<ProdutoDTO> produtosDTO = produtos.map(produtoMapper::toDTO);
        
        return ResponseEntity.ok(produtosDTO);
    }
    
    /**
     * GET /produtos/all - List all products without pagination (for dropdowns)
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        log.info("Listando todos os produtos");
        
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoDTO> produtosDTO = produtos.stream()
            .map(produtoMapper::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(produtosDTO);
    }
    
    /**
     * GET /produtos/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando produto {}", id);
        
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
        
        ProdutoDTO produtoDTO = produtoMapper.toDTO(produto);
        return ResponseEntity.ok(produtoDTO);
    }
    
    /**
     * GET /produtos/search - Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProdutoDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        
        log.info("Buscando produtos por nome: {}", nome);
        
        Page<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        Page<ProdutoDTO> produtosDTO = produtos.map(produtoMapper::toDTO);
        
        return ResponseEntity.ok(produtosDTO);
    }
    
    /**
     * GET /produtos/preco - Filter products by price range
     */
    @GetMapping("/preco")
    public ResponseEntity<Page<ProdutoDTO>> buscarPorPreco(
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @PageableDefault(size = 20, sort = "preco", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        
        log.info("Buscando produtos por preço entre {} e {}", precoMin, precoMax);
        
        Page<Produto> produtos;
        
        if (precoMin != null && precoMax != null) {
            produtos = produtoRepository.findByPrecoBetween(precoMin, precoMax, pageable);
        } else if (precoMin != null) {
            produtos = produtoRepository.findByPrecoGreaterThanEqual(precoMin, pageable);
        } else if (precoMax != null) {
            produtos = produtoRepository.findByPrecoLessThanEqual(precoMax, pageable);
        } else {
            produtos = produtoRepository.findAll(pageable);
        }
        
        Page<ProdutoDTO> produtosDTO = produtos.map(produtoMapper::toDTO);
        return ResponseEntity.ok(produtosDTO);
    }
}

package com.empresa.logistica.config;

/**
 * Application constants for business rules and configuration
 */
public final class ApplicationConstants {
    
    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // Business Logic Constants
    public static final int CREDIT_LIMIT_DAYS = 30;
    public static final String DEFAULT_PAGINATION_SIZE = "20";
    public static final String DEFAULT_SORT_FIELD = "dataPedido";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    
    // Validation Messages
    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado: ";
    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado: ";
    public static final String PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado: ";
    
    // Database Constraints
    public static final int NOME_MAX_LENGTH = 100;
    public static final int STATUS_MAX_LENGTH = 20;
    public static final int DECIMAL_PRECISION = 15;
    public static final int DECIMAL_SCALE = 2;
}

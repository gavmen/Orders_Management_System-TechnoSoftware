-- =============================================================================
-- V1__Create_initial_schema.sql
-- Customer Orders Management System - Production Schema
-- =============================================================================

-- Create Cliente table
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    limite_credito DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_limite_credito CHECK (limite_credito >= 0)
);

-- Create Produto table
CREATE TABLE produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    preco DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_preco CHECK (preco >= 0)
);

-- Create Pedido table
CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor_total DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT chk_valor_total CHECK (valor_total >= 0),
    CONSTRAINT chk_status CHECK (status IN ('APROVADO', 'REJEITADO', 'PENDENTE'))
);

-- Create Item_Pedido table
CREATE TABLE item_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES produto(id),
    CONSTRAINT chk_quantidade CHECK (quantidade > 0),
    CONSTRAINT chk_subtotal CHECK (subtotal >= 0),
    CONSTRAINT uk_pedido_produto UNIQUE (pedido_id, produto_id)
);

-- =============================================================================
-- Performance Indexes
-- =============================================================================

-- Index for credit limit queries
CREATE INDEX idx_cliente_limite_credito ON cliente(limite_credito);

-- Index for order queries by client and date (critical for credit validation)
CREATE INDEX idx_pedido_cliente_data ON pedido(cliente_id, data_pedido);

-- Index for order status filtering
CREATE INDEX idx_pedido_status ON pedido(status);

-- Index for order date queries
CREATE INDEX idx_pedido_data ON pedido(data_pedido);

-- Index for product name searches
CREATE INDEX idx_produto_nome ON produto(nome);

-- Index for product price range queries
CREATE INDEX idx_produto_preco ON produto(preco);

-- Composite index for item queries
CREATE INDEX idx_item_pedido_produto ON item_pedido(pedido_id, produto_id);

-- =============================================================================
-- Comments for Documentation
-- =============================================================================

COMMENT ON TABLE cliente IS 'Customer information with credit limits';
COMMENT ON TABLE produto IS 'Product catalog with pricing';
COMMENT ON TABLE pedido IS 'Customer orders with status tracking';
COMMENT ON TABLE item_pedido IS 'Order line items';

COMMENT ON COLUMN cliente.limite_credito IS 'Credit limit for customer orders';
COMMENT ON COLUMN pedido.status IS 'Order status: APROVADO, REJEITADO, PENDENTE';
COMMENT ON COLUMN pedido.valor_total IS 'Total order value for credit validation';

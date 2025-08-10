-- Performance indexes for credit limit queries
-- Add composite index for cliente_id + data_pedido for 30-day credit queries
CREATE INDEX IF NOT EXISTS idx_pedido_cliente_data_status 
ON pedido (cliente_id, data_pedido, status);

-- Add index for produto_id in item_pedido for product lookups
CREATE INDEX IF NOT EXISTS idx_item_pedido_produto_id 
ON item_pedido (produto_id);

-- Add index for pedido_id in item_pedido for cascade operations
CREATE INDEX IF NOT EXISTS idx_item_pedido_pedido_id 
ON item_pedido (pedido_id);

-- Data integrity improvements
-- Clean up duplicate customer names first
DELETE FROM cliente 
WHERE id NOT IN (
    SELECT MIN(id) 
    FROM (SELECT id, nome FROM cliente) AS temp 
    GROUP BY nome
);

-- Add unique constraint for customer name (business rule)
ALTER TABLE cliente ADD CONSTRAINT uk_cliente_nome UNIQUE (nome);

-- Add check constraints for business rules
ALTER TABLE cliente ADD CONSTRAINT chk_limite_credito_positivo CHECK (limite_credito >= 0);
ALTER TABLE pedido ADD CONSTRAINT chk_valor_total_positivo CHECK (valor_total > 0);
ALTER TABLE item_pedido ADD CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0);
ALTER TABLE item_pedido ADD CONSTRAINT chk_subtotal_positivo CHECK (subtotal > 0);
ALTER TABLE produto ADD CONSTRAINT chk_preco_positivo CHECK (preco > 0);

-- =============================================================================
-- Task 5 - Database Initial Data Seeding
-- Customer Orders Management System
-- =============================================================================

-- Insert 5 Clientes (Customers)
INSERT INTO cliente (nome, limite_credito) VALUES
('João Silva Santos', 15000.00),
('Maria Oliveira Costa', 25000.00),
('Carlos Eduardo Lima', 10000.00),
('Ana Paula Ferreira', 30000.00),
('Roberto Almeida Souza', 20000.00);

-- Insert 5 Produtos (Products)
INSERT INTO produto (nome, preco) VALUES
('Notebook Dell Inspiron 15', 2800.00),
('Mouse Logitech MX Master 3', 450.00),
('Teclado Mecânico Corsair K95', 750.00),
('Monitor Samsung 27" 4K', 1200.00),
('Impressora HP LaserJet Pro', 680.00);

-- Insert 2 Pedidos (Orders) with multiple items each

-- Pedido 1: João Silva Santos (cliente_id=1) - Status APROVADO
-- Data: 2025-07-15 (dentro dos últimos 30 dias para teste de validação)
INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) VALUES
(1, '2025-07-15 10:30:00', 'APROVADO', 4200.00);

-- Items do Pedido 1
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, subtotal) VALUES
(1, 1, 1, 2800.00),  -- 1x Notebook Dell = 2800.00
(1, 2, 2, 900.00),   -- 2x Mouse Logitech = 450.00 * 2 = 900.00
(1, 3, 1, 750.00);   -- 1x Teclado Corsair = 750.00
-- Total Pedido 1: 2800 + 900 + 750 = 4450.00

-- Pedido 2: Maria Oliveira Costa (cliente_id=2) - Status APROVADO
-- Data: 2025-08-01 (dentro dos últimos 30 dias para teste de validação)
INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) VALUES
(2, '2025-08-01 14:20:00', 'APROVADO', 2330.00);

-- Items do Pedido 2
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, subtotal) VALUES
(2, 4, 1, 1200.00),  -- 1x Monitor Samsung = 1200.00
(2, 2, 1, 450.00),   -- 1x Mouse Logitech = 450.00
(2, 5, 1, 680.00);   -- 1x Impressora HP = 680.00
-- Total Pedido 2: 1200 + 450 + 680 = 2330.00

-- =============================================================================
-- Dados de Teste Adicionais para Validação de Regras de Negócio
-- =============================================================================

-- Pedido 3: Carlos Eduardo Lima (cliente_id=3) - Status REJEITADO
-- Este pedido foi rejeitado por exceder limite de crédito
INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) VALUES
(3, '2025-08-05 16:45:00', 'REJEITADO', 12000.00);

-- Items do Pedido 3 (rejeitado)
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, subtotal) VALUES
(3, 1, 4, 11200.00), -- 4x Notebook Dell = 2800.00 * 4 = 11200.00
(3, 4, 1, 1200.00);  -- 1x Monitor Samsung = 1200.00
-- Total Pedido 3: 11200 + 1200 = 12400.00 (excede limite de 10000.00)

-- =============================================================================
-- Resumo dos Dados Inseridos:
--
-- CLIENTES:
-- 1. João Silva Santos - Limite: R$ 15.000,00
-- 2. Maria Oliveira Costa - Limite: R$ 25.000,00  
-- 3. Carlos Eduardo Lima - Limite: R$ 10.000,00
-- 4. Ana Paula Ferreira - Limite: R$ 30.000,00
-- 5. Roberto Almeida Souza - Limite: R$ 20.000,00
--
-- PRODUTOS:
-- 1. Notebook Dell Inspiron 15 - R$ 2.800,00
-- 2. Mouse Logitech MX Master 3 - R$ 450,00
-- 3. Teclado Mecânico Corsair K95 - R$ 750,00
-- 4. Monitor Samsung 27" 4K - R$ 1.200,00
-- 5. Impressora HP LaserJet Pro - R$ 680,00
--
-- PEDIDOS:
-- 1. João (APROVADO) - R$ 4.450,00 - 3 itens
-- 2. Maria (APROVADO) - R$ 2.330,00 - 3 itens  
-- 3. Carlos (REJEITADO) - R$ 12.400,00 - 2 itens
--
-- VALIDAÇÃO DE NEGÓCIO:
-- - Pedidos 1 e 2 estão APROVADOS e dentro dos últimos 30 dias
-- - Pedido 3 foi REJEITADO por exceder limite de crédito do cliente
-- - Todos os cálculos de subtotais estão corretos
-- - Dados permitem testar a função totalPedidosUltimos30Dias()
-- =============================================================================

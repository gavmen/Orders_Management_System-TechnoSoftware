# ER Diagram - Orders Management System

## Entity-Relationship Diagram

```mermaid
erDiagram
    CLIENTE {
        bigint id PK
        varchar nome
        decimal limite_credito
    }
    
    PRODUTO {
        bigint id PK
        varchar nome
        decimal preco
    }
    
    PEDIDO {
        bigint id PK
        bigint cliente_id FK
        timestamp data_pedido
        varchar status
        decimal valor_total
    }
    
    ITEM_PEDIDO {
        bigint id PK
        bigint pedido_id FK
        bigint produto_id FK
        integer quantidade
        decimal subtotal
        decimal preco_unitario
    }

    CLIENTE ||--o{ PEDIDO : "has"
    PEDIDO ||--o{ ITEM_PEDIDO : "contains"
    PRODUTO ||--o{ ITEM_PEDIDO : "is used in"
```

## Entity Descriptions

### 1. CLIENTE
- **id**: Primary key, unique client identifier
- **nome**: Full client name (VARCHAR, 2-150 characters)
- **limite_credito**: Available credit limit for client (DECIMAL 15,2)

### 2. PRODUTO  
- **id**: Primary key, unique product identifier
- **nome**: Product name (VARCHAR, 2-150 characters)
- **preco**: Unit price of product (DECIMAL 15,2)

### 3. PEDIDO
- **id**: Primary key, unique order identifier
- **cliente_id**: Foreign key referencing CLIENTE
- **data_pedido**: Order creation date and time (TIMESTAMP)
- **status**: Order status ('APROVADO' or 'REJEITADO')
- **valor_total**: Calculated total order value (DECIMAL 15,2)

### 4. ITEM_PEDIDO
- **id**: Primary key, unique item identifier
- **pedido_id**: Foreign key referencing PEDIDO
- **produto_id**: Foreign key referencing PRODUTO
- **quantidade**: Product quantity in item (INTEGER >= 1)
- **subtotal**: Calculated subtotal (quantity × unit_price)
- **preco_unitario**: Product unit price at time of order

## Relationships

1. **CLIENTE → PEDIDO**: One client can have many orders (1:N)
2. **PEDIDO → ITEM_PEDIDO**: One order can have many items (1:N)
3. **PRODUTO → ITEM_PEDIDO**: One product can be in many order items (1:N)

## Business Rules

### Credit Validation
- System calculates used credit based on approved orders from last 30 days
- When an order is created:
  - Calculates valor_utilizado = sum of approved orders from last 30 days
  - Calculates saldo_disponivel = limite_credito - valor_utilizado
  - If valor_total_pedido ≤ saldo_disponivel: status = 'APROVADO'
  - If valor_total_pedido > saldo_disponivel: status = 'REJEITADO'
- Rejected orders are stored for audit but don't affect credit calculation
- 30-day window is calculated from current date (rolling window)

### Real-time Balance Query
- Endpoint `/clientes/{id}/credito` provides updated information
- Dynamic calculation based on current date
- Returns: limite_credito, valor_utilizado, saldo_disponivel

### Automatic Calculations
- **subtotal** = quantidade × preco_unitario
- **valor_total** = sum of all item subtotals in order
- **preco_unitario** is copied from product at creation time to maintain history
- **valor_utilizado** = sum of approved order values from last 30 days

### Constraints and Validations
- All mandatory fields have NOT NULL validation
- Prices and values must be > 0.01
- Quantities must be >= 1
- Names must be between 2 and 150 characters
- Order status is controlled by application (enum: APROVADO, REJEITADO)

## Suggested Indexes

```sql
-- Indexes for better performance
CREATE INDEX idx_pedido_cliente_id ON pedido(cliente_id);
CREATE INDEX idx_pedido_data ON pedido(data_pedido);
CREATE INDEX idx_pedido_status ON pedido(status);
CREATE INDEX idx_item_pedido_pedido_id ON item_pedido(pedido_id);
CREATE INDEX idx_item_pedido_produto_id ON item_pedido(produto_id);
```

## Sample Data

```sql
-- Client example
INSERT INTO cliente (nome, limite_credito) VALUES 
('João Silva Santos', 15000.00);

-- Product example
INSERT INTO produto (nome, preco) VALUES 
('Notebook Dell Inspiron 15', 2800.00);

-- Approved order example
INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) VALUES 
(1, '2025-08-09 10:30:00', 'APROVADO', 2800.00);

-- Order item example
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, subtotal, preco_unitario) VALUES 
(1, 1, 1, 2800.00, 2800.00);
```

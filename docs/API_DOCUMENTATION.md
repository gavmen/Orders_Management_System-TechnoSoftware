# API Documentation - Orders Management System

## General Information

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **CORS**: Enabled for `http://localhost:3000`
- **Date Format**: ISO 8601 (`YYYY-MM-DDTHH:mm:ss`)

## Health Check

### GET `/health`
Checks application status.

**Response:**
```
Orders Management System is running successfully!
```

---

## Clients API

### GET `/clientes`
Lists all clients with pagination.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: "nome")

**Response Example:**
```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva Santos",
      "limiteCredito": "15000.00",
      "valorUtilizado": null,
      "saldoDisponivel": null,
      "totalPedidos": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 8,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### GET `/clientes/{id}`
Finds specific client by ID.

**Path Parameters:**
- `id`: Client ID (Long)

**Response Example:**
```json
{
  "id": 1,
  "nome": "João Silva Santos",
  "limiteCredito": "15000.00",
  "valorUtilizado": null,
  "saldoDisponivel": null,
  "totalPedidos": null
}
```

**Error Responses:**
- `404 Not Found`: Client not found

### GET `/clientes/{id}/credito`
Gets real-time credit information for a specific client.

**Path Parameters:**
- `id`: Client ID (Long)

**Response Example:**
```json
{
  "clienteId": 1,
  "clienteNome": "João Silva Santos",
  "limiteCredito": 15000.00,
  "valorUtilizado": 10530.00,
  "saldoDisponivel": 4470.00
}
```

**Business Logic:**
- `valorUtilizado`: Sum of all approved orders from last 30 days
- `saldoDisponivel`: Credit limit minus used value
- Calculation based on order date (last 30 days from current date)

**Error Responses:**
- `404 Not Found`: Client not found

### POST `/clientes`
Creates a new client.

**Request Body:**
```json
{
  "nome": "New Client",
  "limiteCredito": 20000.00
}
```

**Validation Rules:**
- `nome`: Required, 2-150 characters
- `limiteCredito`: Required, > 0.01

**Response:** Client created (201 Created)

### PUT `/clientes/{id}`
Updates an existing client.

**Path Parameters:**
- `id`: Client ID

**Request Body:**
```json
{
  "nome": "Updated Name",
  "limiteCredito": 25000.00
}
```

**Response:** Client updated (200 OK)

### DELETE `/clientes/{id}`
Removes a client.

**Path Parameters:**
- `id`: Client ID

**Response:** 204 No Content

---

## Products API

### GET `/produtos`
Lists all products with pagination.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: "nome")

**Response Example:**
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Notebook Dell Inspiron 15",
      "preco": "2800.00"
    },
    {
      "id": 2,
      "nome": "Mouse Logitech MX Master 3",
      "preco": "450.00"
    }
  ],
  "totalElements": 10,
  "totalPages": 1
}
```

### GET `/produtos/{id}`
Finds specific product by ID.

**Path Parameters:**
- `id`: Product ID (Long)

**Response Example:**
```json
{
  "id": 1,
  "nome": "Notebook Dell Inspiron 15",
  "preco": "2800.00"
}
```

### POST `/produtos`
Creates a new product.

**Request Body:**
```json
{
  "nome": "Smartphone Samsung Galaxy",
  "preco": 1200.00
}
```

**Validation Rules:**
- `nome`: Required, 2-150 characters
- `preco`: Required, > 0.01

### PUT `/produtos/{id}`
Updates an existing product.

### DELETE `/produtos/{id}`
Removes a product.

---

## Orders API

### GET `/pedidos`
Lists all orders with pagination and complete details.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: "dataPedido,desc")

**Response Example:**
```json
{
  "content": [
    {
      "id": 13,
      "clienteId": 4,
      "clienteNome": "Ana Paula Ferreira",
      "dataPedido": "2025-08-09 21:26:53",
      "status": "APROVADO",
      "valorTotal": "680.00",
      "itens": [
        {
          "id": 19,
          "pedidoId": 13,
          "produtoId": 5,
          "produtoNome": "Impressora HP LaserJet Pro",
          "quantidade": 1,
          "subtotal": "680.00",
          "precoUnitario": "680.00"
        }
      ],
      "limiteCredito": 30000.00,
      "valorJaUtilizado": null,
      "saldoDisponivel": null
    }
  ],
  "totalElements": 13,
  "totalPages": 1
}
```

### GET `/pedidos/{id}`
Finds specific order with all details.

**Path Parameters:**
- `id`: Order ID (Long)

**Response:** Complete order data including items

### POST `/pedidos`
Creates a new order with automatic credit validation.

**Request Body:**
```json
{
  "clienteId": 4,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    },
    {
      "produtoId": 2,
      "quantidade": 1
    }
  ]
}
```

**Validation Rules:**
- `clienteId`: Required, must exist
- `itens`: Required array, not empty
- `itens[].produtoId`: Required, must exist
- `itens[].quantidade`: Required, >= 1

**Business Rules:**
1. System calculates total value automatically
2. Compares with client credit limit
3. Sets status:
   - `APROVADO`: if total_value ≤ credit_limit
   - `REJEITADO`: if total_value > credit_limit
4. Stores order regardless of status

**Response Examples:**

**Approved Order:**
```json
{
  "id": 14,
  "clienteId": 4,
  "clienteNome": "Ana Paula Ferreira",
  "dataPedido": "2025-08-09 21:30:15",
  "status": "APROVADO",
  "valorTotal": "3250.00",
  "itens": [
    {
      "id": 20,
      "pedidoId": 14,
      "produtoId": 1,
      "produtoNome": "Notebook Dell Inspiron 15",
      "quantidade": 1,
      "subtotal": "2800.00",
      "precoUnitario": "2800.00"
    },
    {
      "id": 21,
      "pedidoId": 14,
      "produtoId": 2,
      "produtoNome": "Mouse Logitech MX Master 3",
      "quantidade": 1,
      "subtotal": "450.00",
      "precoUnitario": "450.00"
    }
  ],
  "limiteCredito": 30000.00
}
```

**Rejected Order:**
```json
{
  "id": 15,
  "clienteId": 17,
  "clienteNome": "Maria Santos",
  "dataPedido": "2025-08-09 21:30:30",
  "status": "REJEITADO",
  "valorTotal": "8400.00",
  "itens": [
    {
      "id": 22,
      "pedidoId": 15,
      "produtoId": 1,
      "produtoNome": "Notebook Dell Inspiron 15",
      "quantidade": 3,
      "subtotal": "8400.00",
      "precoUnitario": "2800.00"
    }
  ],
  "limiteCredito": 1000.00
}
```

**Error Responses:**
- `400 Bad Request`: Invalid data
- `404 Not Found`: Client or product not found
- `422 Unprocessable Entity`: Business rules violated

### PUT `/pedidos/{id}`
Updates an existing order (limited).

**Note:** Only some fields can be updated after creation.

### DELETE `/pedidos/{id}`
Removes an order.

---

## HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Successful request |
| 201 | Created | Resource successfully created |
| 204 | No Content | Resource successfully removed |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Resource not found |
| 422 | Unprocessable Entity | Business rule violated |
| 500 | Internal Server Error | Internal server error |

## Error Formats

### Validation Error (400)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "nome",
      "message": "Name is required"
    }
  ]
}
```

### Business Error (422)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Client not found"
}
```

### Not Found Error (404)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Client with ID 999 not found"
}
```

## Usage Examples with cURL

### List Clients
```bash
curl -X GET http://localhost:8080/api/clientes
```

### Create Client
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "New Client", "limiteCredito": 15000.00}'
```

### List Products
```bash
curl -X GET http://localhost:8080/api/produtos
```

### Create Order
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:3000" \
  -d '{
    "clienteId": 4,
    "itens": [
      {"produtoId": 1, "quantidade": 1},
      {"produtoId": 2, "quantidade": 2}
    ]
  }'
```

### Find Order by ID
```bash
curl -X GET http://localhost:8080/api/pedidos/1
```

### Health Check
```bash
curl -X GET http://localhost:8080/api/health
```

## Filtering and Sorting

### Pagination
```bash
# Page 2, 10 items per page
curl "http://localhost:8080/api/clientes?page=1&size=10"
```

### Sorting
```bash
# Sort clients by credit limit (descending)
curl "http://localhost:8080/api/clientes?sort=limiteCredito,desc"

# Sort orders by date (most recent first)
curl "http://localhost:8080/api/pedidos?sort=dataPedido,desc"
```

### Multiple Parameters
```bash
curl "http://localhost:8080/api/pedidos?page=0&size=5&sort=dataPedido,desc"
```

## Testing the API

### 1. Success Scenario - Approved Order
```bash
# 1. List clients to see limits
curl http://localhost:8080/api/clientes

# 2. List products to see prices  
curl http://localhost:8080/api/produtos

# 3. Create order within limit (Ana Paula - R$ 30,000)
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 4,
    "itens": [{"produtoId": 2, "quantidade": 1}]
  }'
# Response: status "APROVADO" 
```

### 2. Rejection Scenario - Rejected Order
```bash
# Create order above limit (Maria Santos - R$ 1,000)
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 17,
    "itens": [{"produtoId": 1, "quantidade": 2}]
  }'
# Response: status "REJEITADO"
```

### 3. Error Scenario - Invalid Data
```bash
# Try to create order without client
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "itens": [{"produtoId": 1, "quantidade": 1}]
  }'
# Response: 400 Bad Request
```

## Monitoring and Logs

### Detailed Health Check
The `/health` endpoint provides information about:
- Application status
- Database connectivity
- Response time

### Application Logs
Logs include:
- All HTTP requests
- Executed SQL queries
- Errors and exceptions
- Order transactions

To view logs in real time:
```bash
# If running with mvn spring-boot:run
tail -f nohup.out

# Or check console where application is running
```

---

## Configuration for Different Environments

### Development
```properties
# application-dev.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.com.empresa.logistica=DEBUG
```

### Production
```properties  
# application-prod.properties
spring.jpa.show-sql=false
logging.level.com.empresa.logistica=INFO
server.port=8080
```

### Testing
```properties
# application-test.properties  
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

---

**For more information, see the [complete documentation](../README.md) of the project.**

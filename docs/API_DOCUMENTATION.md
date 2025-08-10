#  API Documentation - Sistema de Gerenciamento de Pedidos

##  Informações Gerais

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **CORS**: Habilitado para `http://localhost:3000`
- **Formato de Data**: ISO 8601 (`YYYY-MM-DDTHH:mm:ss`)

## 🏥 Health Check

### GET `/health`
Verifica o status da aplicação.

**Response:**
```
Orders Management System is running successfully!
```

---

##  Clientes API

### GET `/clientes`
Lista todos os clientes com paginação.

**Query Parameters:**
- `page` (optional): Número da página (default: 0)
- `size` (optional): Tamanho da página (default: 20)
- `sort` (optional): Campo para ordenação (default: "nome")

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
Busca um cliente específico por ID.

**Path Parameters:**
- `id`: ID do cliente (Long)

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
- `404 Not Found`: Cliente não encontrado

### POST `/clientes`
Cria um novo cliente.

**Request Body:**
```json
{
  "nome": "Novo Cliente",
  "limiteCredito": 20000.00
}
```

**Validation Rules:**
- `nome`: Obrigatório, 2-150 caracteres
- `limiteCredito`: Obrigatório, > 0.01

**Response:** Cliente criado (201 Created)

### PUT `/clientes/{id}`
Atualiza um cliente existente.

**Path Parameters:**
- `id`: ID do cliente

**Request Body:**
```json
{
  "nome": "Nome Atualizado",
  "limiteCredito": 25000.00
}
```

**Response:** Cliente atualizado (200 OK)

### DELETE `/clientes/{id}`
Remove um cliente.

**Path Parameters:**
- `id`: ID do cliente

**Response:** 204 No Content

---

##  Produtos API

### GET `/produtos`
Lista todos os produtos com paginação.

**Query Parameters:**
- `page` (optional): Número da página (default: 0)
- `size` (optional): Tamanho da página (default: 20)
- `sort` (optional): Campo para ordenação (default: "nome")

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
Busca um produto específico por ID.

**Path Parameters:**
- `id`: ID do produto (Long)

**Response Example:**
```json
{
  "id": 1,
  "nome": "Notebook Dell Inspiron 15",
  "preco": "2800.00"
}
```

### POST `/produtos`
Cria um novo produto.

**Request Body:**
```json
{
  "nome": "Smartphone Samsung Galaxy",
  "preco": 1200.00
}
```

**Validation Rules:**
- `nome`: Obrigatório, 2-150 caracteres
- `preco`: Obrigatório, > 0.01

### PUT `/produtos/{id}`
Atualiza um produto existente.

### DELETE `/produtos/{id}`
Remove um produto.

---

##  Pedidos API

### GET `/pedidos`
Lista todos os pedidos com paginação e detalhes completos.

**Query Parameters:**
- `page` (optional): Número da página (default: 0)
- `size` (optional): Tamanho da página (default: 20)
- `sort` (optional): Campo para ordenação (default: "dataPedido,desc")

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
Busca um pedido específico com todos os detalhes.

**Path Parameters:**
- `id`: ID do pedido (Long)

**Response:** Dados completos do pedido incluindo itens

### POST `/pedidos`
Cria um novo pedido com validação automática de crédito.

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
- `clienteId`: Obrigatório, deve existir
- `itens`: Array obrigatório, não vazio
- `itens[].produtoId`: Obrigatório, deve existir
- `itens[].quantidade`: Obrigatório, >= 1

**Business Rules:**
1. Sistema calcula valor total standardamente
2. Compara com limite de crédito do cliente
3. Define status:
   - `APROVADO`: se valor_total ≤ limite_credito
   - `REJEITADO`: se valor_total > limite_credito
4. Armazena pedido independente do status

**Response Examples:**

**Pedido Aprovado:**
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

**Pedido Rejeitado:**
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
- `400 Bad Request`: Dados inválidos
- `404 Not Found`: Cliente ou produto não encontrado
- `422 Unprocessable Entity`: Regras de negócio violadas

### PUT `/pedidos/{id}`
Atualiza um pedido existente (limitado).

**Note:** Apenas alguns campos podem ser atualizados após criação.

### DELETE `/pedidos/{id}`
Remove um pedido.

---

## 🚨 Códigos de Status HTTP

| Código | Significado | Descrição |
|--------|-------------|-----------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Recurso removido com sucesso |
| 400 | Bad Request | Dados de entrada inválidos |
| 404 | Not Found | Recurso não encontrado |
| 422 | Unprocessable Entity | Regra de negócio violada |
| 500 | Internal Server Error | Erro interno do servidor |

##  Formatos de Erro

### Erro de Validação (400)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "nome",
      "message": "O nome é obrigatório"
    }
  ]
}
```

### Erro de Negócio (422)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Cliente não encontrado"
}
```

### Erro de Não Encontrado (404)
```json
{
  "timestamp": "2025-08-09T21:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente com ID 999 não encontrado"
}
```

##  Exemplos de Uso com cURL

### Listar Clientes
```bash
curl -X GET http://localhost:8080/api/clientes
```

### Criar Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Novo Cliente", "limiteCredito": 15000.00}'
```

### Listar Produtos
```bash
curl -X GET http://localhost:8080/api/produtos
```

### Criar Pedido
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

### Buscar Pedido por ID
```bash
curl -X GET http://localhost:8080/api/pedidos/1
```

### Health Check
```bash
curl -X GET http://localhost:8080/api/health
```

##  Filtros e Ordenação

### Paginação
```bash
# Página 2, 10 itens por página
curl "http://localhost:8080/api/clientes?page=1&size=10"
```

### Ordenação
```bash
# Ordenar clientes por limite de crédito (decrescente)
curl "http://localhost:8080/api/clientes?sort=limiteCredito,desc"

# Ordenar pedidos por data (mais recentes primeiro)
curl "http://localhost:8080/api/pedidos?sort=dataPedido,desc"
```

### Múltiplos Parâmetros
```bash
curl "http://localhost:8080/api/pedidos?page=0&size=5&sort=dataPedido,desc"
```

##  Testando a API

### 1. Cenário de Sucesso - Pedido Aprovado
```bash
# 1. Listar clientes para ver limites
curl http://localhost:8080/api/clientes

# 2. Listar produtos para ver preços  
curl http://localhost:8080/api/produtos

# 3. Criar pedido dentro do limite (Ana Paula - R$ 30.000)
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 4,
    "itens": [{"produtoId": 2, "quantidade": 1}]
  }'
# Resposta: status "APROVADO" 
```

### 2. Cenário de Rejeição - Pedido Rejeitado
```bash
# Criar pedido acima do limite (Maria Santos - R$ 1.000)
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 17,
    "itens": [{"produtoId": 1, "quantidade": 2}]
  }'
# Resposta: status "REJEITADO"
```

### 3. Cenário de Erro - Dados Inválidos
```bash
# Tentar criar pedido sem cliente
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "itens": [{"produtoId": 1, "quantidade": 1}]
  }'
# Resposta: 400 Bad Request
```

##  Monitoramento e Logs

### Health Check Detalhado
O endpoint `/health` fornece informações sobre:
- Status da aplicação
- Conectividade com banco de dados
- Tempo de resposta

### Logs da Aplicação
Os logs incluem:
- Todas as requisições HTTP
- Queries SQL executadas
- Erros e exceções
- Transações de pedidos

Para visualizar logs em tempo real:
```bash
# Se rodando com mvn spring-boot:run
tail -f nohup.out

# Ou verificar console onde a aplicação está rodando
```

---

##  Configuração para Diferentes Ambientes

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

** Para mais informações, consulte a [documentação completa](../README.md) do projeto.**

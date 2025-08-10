# Technical Architecture - Orders Management System

## Architecture Overview

The Orders Management System follows a three-tier architecture (3-tier) with clear separation of responsibilities:

```
┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                         │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   React App     │  │  Material-UI    │  │   Axios HTTP    │ │
│  │   (Frontend)    │  │  Components     │  │    Client       │ │
│  │   Port: 3000    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │ HTTP/REST API
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        BUSINESS LAYER                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │  Spring Boot    │  │  REST Controllers│  │   Service Layer │ │
│  │   Backend       │  │                 │  │                 │ │
│  │   Port: 8080    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │ JPA/Hibernate
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   PostgreSQL    │  │   JPA Entities  │  │  Repository     │ │
│  │   Database      │  │                 │  │    Layer       │ │
│  │   Port: 5432    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Technology Stack

### Frontend (Presentation Layer)
- **React 19.1.1**: JavaScript framework for UI
- **Material-UI 7.3.1**: UI components library
- **Axios 1.11.0**: HTTP client for API communication
- **Emotion**: CSS-in-JS library for styling

### Backend (Business Layer)
- **Spring Boot 3.2.0**: Java framework for web applications
- **Spring Data JPA**: ORM and data abstraction
- **Spring Web MVC**: REST controllers
- **Java 17**: Programming language
- **Maven**: Dependency manager

### Database (Data Layer)
- **PostgreSQL 14+**: Relational database
- **HikariCP**: Connection pool
- **Flyway**: Database migration (future)

## Data Model

### Entity-Relationship Diagram

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│     CLIENTE     │         │     PEDIDO      │         │    PRODUTO      │
├─────────────────┤         ├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────┤ cliente_id (FK) │         │ id (PK)         │
│ nome            │         │ id (PK)         │         │ nome            │
│ limite_credito  │         │ data_pedido     │         │ preco           │
└─────────────────┘         │ status          │         └─────────────────┘
                            │ valor_total     │                 ▲
                            └─────────────────┘                 │
                                     │                          │
                                     ▼                          │
                            ┌─────────────────┐                 │
                            │   ITEM_PEDIDO   │                 │
                            ├─────────────────┤                 │
                            │ id (PK)         │                 │
                            │ pedido_id (FK)  │─────────────────┘
                            │ produto_id (FK) │
                            │ quantidade      │
                            │ subtotal        │
                            └─────────────────┘
```

### Table Schema

#### Table: CLIENTE
```sql
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    limite_credito DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_limite_credito CHECK (limite_credito >= 0)
);
```

#### Table: PRODUTO
```sql
CREATE TABLE produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    preco DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_preco CHECK (preco >= 0)
);
```

#### Table: PEDIDO
```sql
CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor_total DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT chk_valor_total CHECK (valor_total >= 0),
    CONSTRAINT chk_status CHECK (status IN ('APROVADO', 'REJEITADO'))
);
```

#### Table: ITEM_PEDIDO
```sql
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
```

## Data Flow

### 1. Order Creation Flow

```
Frontend                Backend                Database
    │                      │                      │
    │ 1. Load clients      │                      │
    ├─────────────────────►│                      │
    │                      │ 2. SELECT clients    │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 3. Return list       │
    │                      │                      │
    │ 4. Load products     │                      │
    ├─────────────────────►│                      │
    │                      │ 5. SELECT products   │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 6. Return list       │
    │                      │                      │
    │ 7. Create order      │                      │
    ├─────────────────────►│                      │
    │                      │ 8. Validate credit   │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │                      │ 9. INSERT order      │
    │                      ├─────────────────────►│
    │                      │ 10. INSERT items     │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 11. Confirm          │
    │                      │                      │
```

### 2. Credit Validation

```sql
-- Available credit calculation with 30-day window
SELECT 
    c.limite_credito,
    COALESCE(SUM(p.valor_total), 0) as valor_utilizado,
    (c.limite_credito - COALESCE(SUM(p.valor_total), 0)) as saldo_disponivel
FROM cliente c
LEFT JOIN pedido p ON c.id = p.cliente_id 
    AND p.status = 'APROVADO'
    AND p.data_pedido >= (CURRENT_DATE - INTERVAL '30 days')
WHERE c.id = ?
GROUP BY c.id, c.limite_credito;
```

## REST API Endpoints

### Clients
```
GET    /api/clientes          - List all clients
GET    /api/clientes/{id}     - Get specific client
GET    /api/clientes/{id}/credito - Get real-time credit information
POST   /api/clientes          - Create new client
PUT    /api/clientes/{id}     - Update client
DELETE /api/clientes/{id}     - Delete client
```

### Products
```
GET    /api/produtos          - List all products
GET    /api/produtos/{id}     - Get specific product
POST   /api/produtos          - Create new product
PUT    /api/produtos/{id}     - Update product
DELETE /api/produtos/{id}     - Delete product
```

### Orders
```
GET    /api/pedidos           - List all orders
GET    /api/pedidos/{id}      - Get specific order
POST   /api/pedidos           - Create new order
PUT    /api/pedidos/{id}      - Update order
DELETE /api/pedidos/{id}      - Cancel order
GET    /api/pedidos/cliente/{clienteId} - Orders by client
```

### System
```
GET    /api/health            - System health check
GET    /api/version           - Application version
```

## Architectural Patterns

### 1. Model-View-Controller (MVC)
- **Model**: JPA Entities (Cliente, Produto, Pedido)
- **View**: React Components
- **Controller**: Spring REST Controllers

### 2. Repository Pattern
```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
```

### 3. Service Layer Pattern
```java
@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
        // Business logic
    }
}
```

### 4. Data Transfer Object (DTO) Pattern
```java
public class PedidoRequestDTO {
    private Long clienteId;
    private List<ItemPedidoDTO> itens;
    // getters/setters
}

public class PedidoResponseDTO {
    private Long id;
    private String clienteNome;
    private BigDecimal total;
    // getters/setters
}
```

## Security

### 1. CORS Configuration
```java
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ClienteController {
    // endpoints
}
```

### 2. Data Validation
```java
@Valid
public ResponseEntity<PedidoResponseDTO> criarPedido(
    @RequestBody @Valid PedidoRequestDTO request) {
    // processing
}
```

### 3. Exception Handling
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CreditoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleCreditoInsuficiente(
            CreditoInsuficienteException ex) {
        // handling
    }
}
```

## Performance and Scalability

### 1. Connection Pool
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
```

### 2. Cache (Future)
```java
@Cacheable("produtos")
public List<Produto> listarProdutos() {
    return produtoRepository.findAll();
}
```

### 3. Pagination
```java
public Page<Pedido> listarPedidos(Pageable pageable) {
    return pedidoRepository.findAll(pageable);
}
```

## Testing Strategies

### 1. Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    
    @Mock
    private PedidoRepository pedidoRepository;
    
    @InjectMocks
    private PedidoService pedidoService;
    
    @Test
    void deveCriarPedidoComSucesso() {
        // test
    }
}
```

### 2. Integration Tests
```java
@SpringBootTest
@Transactional
class PedidoControllerIntegrationTest {
    
    @Test
    void deveRetornarListaDePedidos() {
        // integration test
    }
}
```

### 3. Frontend Tests
```javascript
import { render, screen } from '@testing-library/react';
import PedidoForm from './PedidoForm';

test('renders order form', () => {
  render(<PedidoForm />);
  const linkElement = screen.getByText(/create order/i);
  expect(linkElement).toBeInTheDocument();
});
```

## Deployment and DevOps

### 1. Containerization (Docker)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/orders-management-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 2. Docker Compose
```yaml
version: '3.8'
services:
  database:
    image: postgres:14
    environment:
      POSTGRES_DB: logistica_pedidos
      POSTGRES_USER: logistica_user
      POSTGRES_PASSWORD: logistica2025
    
  backend:
    build: .
    depends_on:
      - database
    environment:
      DB_PASSWORD: logistica2025
    ports:
      - "8080:8080"
  
  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
```

## Monitoring

### 1. Application Metrics
```java
@RestController
public class HealthController {
    
    @GetMapping("/api/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        return status;
    }
}
```

### 2. Structured Logs
```java
private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
    logger.info("Creating order for client: {}", request.getClienteId());
    // logic
    logger.info("Order created successfully. ID: {}", pedido.getId());
}
```

## API Versioning

### 1. Header-based Versioning
```java
@GetMapping(value = "/api/pedidos", headers = "API-Version=1")
public ResponseEntity<List<PedidoResponseDTO>> listarPedidosV1() {
    // v1 implementation
}
```

### 2. URL-based Versioning
```java
@GetMapping("/api/v1/pedidos")
public ResponseEntity<List<PedidoResponseDTO>> listarPedidosV1() {
    // v1 implementation
}
```

## API Documentation

### 1. OpenAPI/Swagger (Future)
```java
@Configuration
@EnableOpenApi
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Orders Management System API")
                .version("1.0.0"));
    }
}
```

---

**Last updated**: August 9, 2025  
**Version**: 1.0.0  
**Author**: Gabriel Mendonça

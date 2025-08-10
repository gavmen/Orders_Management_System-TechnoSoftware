# Arquitetura Técnica - Sistema de Gerenciamento de Pedidos

## Visão Geral da Arquitetura

O Sistema de Gerenciamento de Pedidos segue uma arquitetura de três camadas (3-tier) com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────────┐
│                        CAMADA DE APRESENTAÇÃO                  │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   React App     │  │  Material-UI    │  │   Axios HTTP    │ │
│  │   (Frontend)    │  │  Components     │  │    Client       │ │
│  │   Port: 3000    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │ HTTP/REST API
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         CAMADA DE NEGÓCIO                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │  Spring Boot    │  │  REST Controllers│  │   Service Layer │ │
│  │   Backend       │  │                 │  │                 │ │
│  │   Port: 8080    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │ JPA/Hibernate
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         CAMADA DE DADOS                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   PostgreSQL    │  │   JPA Entities  │  │  Repository     │ │
│  │   Database      │  │                 │  │    Layer       │ │
│  │   Port: 5432    │  │                 │  │                 │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Stack Tecnológico

### Frontend (Camada de Apresentação)
- **React 19.1.1**: Framework JavaScript para UI
- **Material-UI 7.3.1**: Biblioteca de componentes UI
- **Axios 1.11.0**: Cliente HTTP para comunicação com API
- **Emotion**: Biblioteca CSS-in-JS para estilização

### Backend (Camada de Negócio)
- **Spring Boot 3.2.0**: Framework Java para aplicações web
- **Spring Data JPA**: ORM e abstração de dados
- **Spring Web MVC**: Controladores REST
- **Java 17**: Linguagem de programação
- **Maven**: Gerenciador de dependências

### Banco de Dados (Camada de Dados)
- **PostgreSQL 14+**: Banco de dados relacional
- **HikariCP**: Pool de conexões
- **Flyway**: Migração de banco de dados (futuro)

## Modelo de Dados

### Diagrama Entidade-Relacionamento

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

### Esquema de Tabelas

#### Tabela: CLIENTE
```sql
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    limite_credito DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_limite_credito CHECK (limite_credito >= 0)
);
```

#### Tabela: PRODUTO
```sql
CREATE TABLE produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    preco DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_preco CHECK (preco >= 0)
);
```

#### Tabela: PEDIDO
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

#### Tabela: ITEM_PEDIDO
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

## Fluxo de Dados

### 1. Fluxo de Criação de Pedido

```
Frontend                Backend                Database
    │                      │                      │
    │ 1. Carregar clientes │                      │
    ├─────────────────────►│                      │
    │                      │ 2. SELECT clientes   │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 3. Retornar lista    │
    │                      │                      │
    │ 4. Carregar produtos │                      │
    ├─────────────────────►│                      │
    │                      │ 5. SELECT produtos   │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 6. Retornar lista    │
    │                      │                      │
    │ 7. Criar pedido      │                      │
    ├─────────────────────►│                      │
    │                      │ 8. Validar crédito   │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │                      │ 9. INSERT pedido     │
    │                      ├─────────────────────►│
    │                      │ 10. INSERT itens     │
    │                      ├─────────────────────►│
    │                      │◄─────────────────────┤
    │◄─────────────────────┤ 11. Confirmar        │
    │                      │                      │
```

### 2. Validação de Crédito

```sql
-- Cálculo do crédito disponível com janela de 30 dias
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

## API REST Endpoints

### Clientes
```
GET    /api/clientes          - Listar todos os clientes
GET    /api/clientes/{id}     - Obter cliente específico
GET    /api/clientes/{id}/credito - Obter informações de crédito em tempo real
POST   /api/clientes          - Criar novo cliente
PUT    /api/clientes/{id}     - Atualizar cliente
DELETE /api/clientes/{id}     - Excluir cliente
```

### Produtos
```
GET    /api/produtos          - Listar todos os produtos
GET    /api/produtos/{id}     - Obter produto específico
POST   /api/produtos          - Criar novo produto
PUT    /api/produtos/{id}     - Atualizar produto
DELETE /api/produtos/{id}     - Excluir produto
```

### Pedidos
```
GET    /api/pedidos           - Listar todos os pedidos
GET    /api/pedidos/{id}      - Obter pedido específico
POST   /api/pedidos           - Criar novo pedido
PUT    /api/pedidos/{id}      - Atualizar pedido
DELETE /api/pedidos/{id}      - Cancelar pedido
GET    /api/pedidos/cliente/{clienteId} - Pedidos por cliente
```

### Sistema
```
GET    /api/health            - Health check do sistema
GET    /api/version           - Versão da aplicação
```

## Padrões Arquiteturais

### 1. Model-View-Controller (MVC)
- **Model**: Entidades JPA (Cliente, Produto, Pedido)
- **View**: Componentes React
- **Controller**: Controllers Spring REST

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
        // Lógica de negócio
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

## Segurança

### 1. Configuração CORS
```java
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ClienteController {
    // endpoints
}
```

### 2. Validação de Dados
```java
@Valid
public ResponseEntity<PedidoResponseDTO> criarPedido(
    @RequestBody @Valid PedidoRequestDTO request) {
    // processamento
}
```

### 3. Tratamento de Exceções
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CreditoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleCreditoInsuficiente(
            CreditoInsuficienteException ex) {
        // tratamento
    }
}
```

## Performance e Escalabilidade

### 1. Pool de Conexões
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
```

### 2. Cache (Futuro)
```java
@Cacheable("produtos")
public List<Produto> listarProdutos() {
    return produtoRepository.findAll();
}
```

### 3. Paginação
```java
public Page<Pedido> listarPedidos(Pageable pageable) {
    return pedidoRepository.findAll(pageable);
}
```

## Estratégias de Teste

### 1. Testes Unitários
```java
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    
    @Mock
    private PedidoRepository pedidoRepository;
    
    @InjectMocks
    private PedidoService pedidoService;
    
    @Test
    void deveCriarPedidoComSucesso() {
        // teste
    }
}
```

### 2. Testes de Integração
```java
@SpringBootTest
@Transactional
class PedidoControllerIntegrationTest {
    
    @Test
    void deveRetornarListaDePedidos() {
        // teste de integração
    }
}
```

### 3. Testes de Frontend
```javascript
import { render, screen } from '@testing-library/react';
import PedidoForm from './PedidoForm';

test('renders pedido form', () => {
  render(<PedidoForm />);
  const linkElement = screen.getByText(/criar pedido/i);
  expect(linkElement).toBeInTheDocument();
});
```

## Deployment e DevOps

### 1. Containerização (Docker)
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

## Monitoramento

### 1. Métricas de Aplicação
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

### 2. Logs Estruturados
```java
private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
    logger.info("Criando pedido para cliente: {}", request.getClienteId());
    // lógica
    logger.info("Pedido criado com sucesso. ID: {}", pedido.getId());
}
```

## Versionamento da API

### 1. Versionamento por Header
```java
@GetMapping(value = "/api/pedidos", headers = "API-Version=1")
public ResponseEntity<List<PedidoResponseDTO>> listarPedidosV1() {
    // implementação v1
}
```

### 2. Versionamento por URL
```java
@GetMapping("/api/v1/pedidos")
public ResponseEntity<List<PedidoResponseDTO>> listarPedidosV1() {
    // implementação v1
}
```

## Documentação da API

### 1. OpenAPI/Swagger (Futuro)
```java
@Configuration
@EnableOpenApi
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sistema de Gerenciamento de Pedidos API")
                .version("1.0.0"));
    }
}
```

---

**Última atualização**: 09 de Agosto de 2025  
**Versão**: 1.0.0  
**Autor**: Gabriel Mendonça

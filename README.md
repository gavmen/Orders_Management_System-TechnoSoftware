# Sistema de Gerenciamento de Pedidos

Um sistema completo para gerenciamento de pedidos com validação automática de limite de crédito, desenvolvido com **Spring Boot 3.2** e **React 19**.

![Status](https://img.shields.io/badge/Status-Production_Ready-green)
![Backend](https://img.shields.io/badge/Backend-Spring_Boot_3.2-brightgreen)
![Frontend](https://img.shields.io/badge/Frontend-React_19-blue)
![Database](https://img.shields.io/badge/Database-PostgreSQL_14-blue)

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Executar](#como-executar)
- [API Endpoints](#api-endpoints)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)
- [Documentação Adicional](#documentação-adicional)

## Sobre o Projeto

Este sistema foi desenvolvido para gerenciar pedidos de clientes com validação automática de limite de crédito. O sistema processa pedidos em tempo real, aprovando ou rejeitando com base no limite de crédito disponível do cliente.

### Características Principais:
- **Validação Automática de Crédito**: Pedidos são aprovados/rejeitados standardamente
- **Interface Moderna**: Frontend em React com Material-UI
- **API RESTful**: Backend robusto com Spring Boot
- **Auditoria Completa**: Todos os pedidos são armazenados para auditoria
- **Performance Otimizada**: Consultas otimizadas e cache implementado
- **Monitoramento**: Health checks e logs detalhados

## Funcionalidades

### Frontend (React)
- **Formulário de Pedidos**: Interface intuitiva para criação de pedidos
- **Gestão de Clientes**: Visualização de clientes e seus limites de crédito
- **Catálogo de Produtos**: Lista de produtos disponíveis com preços
- **Feedback em Tempo Real**: Notificações Snackbar para todas as operações
- **Estados de Loading**: Indicadores visuais para todas as operações
- **Detecção de Conectividade**: Monitora status da conexão com o servidor
- **Design Responsivo**: Funciona perfeitamente em desktop e mobile

### Backend (Spring Boot)
- **Arquitetura em Camadas**: Controller → Service → Repository
- **Validação de Dados**: Validações automáticas com Bean Validation
- **Transações**: Controle transacional para integridade dos dados
- **Auditoria**: Logs detalhados de todas as operações
- 🏥 **Health Checks**: Monitoramento de saúde da aplicação
-  **CORS Configurado**: Suporte a requisições cross-origin

##  Tecnologias

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Mapeamento objeto-relacional
- **PostgreSQL 14** - Banco de dados principal
- **Flyway** - Controle de versão do banco
- **Maven** - Gerenciamento de dependências
- **Jakarta Validation** - Validação de dados

### Frontend  
- **React 19.1.1**
- **Material-UI 7.3.1** - Componentes e design system
- **Axios 1.11.0** - Cliente HTTP
- **JavaScript ES6+**
- **CSS3** com temas customizados
- **NPM** - Gerenciamento de pacotes

### Infraestrutura
- **PostgreSQL 14** - Banco de dados
- **Git** - Controle de versão
- **Docker** (opcional) - Containerização

##  Pré-requisitos

Antes de executar o projeto, você precisará ter instalado:

### Essenciais
- **Java 17+** - [Download OpenJDK](https://adoptium.net/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download Node.js](https://nodejs.org/)
- **NPM 9+** (incluído com Node.js)
- **PostgreSQL 14+** - [Download PostgreSQL](https://www.postgresql.org/download/)

### Verificar Instalações
```bash
# Verificar Java
java -version
# Deve mostrar: openjdk version "17.x.x"

# Verificar Maven  
mvn -version
# Deve mostrar: Apache Maven 3.8.x

# Verificar Node.js
node --version
# Deve mostrar: v18.x.x ou superior

# Verificar NPM
npm --version
# Deve mostrar: 9.x.x ou superior

# Verificar PostgreSQL
psql --version
# Deve mostrar: psql (PostgreSQL) 14.x
```

## Instalação e Configuração

### 1. Clonar o Repositório
```bash
git clone <repository-url>
cd technoSoftware
```

### 2. Configurar o Banco de Dados

#### 2.1 Instalar PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# Verificar se o serviço está rodando
sudo systemctl status postgresql
```

#### 2.2 Criar Banco e Usuário
```bash
# Conectar como usuário postgres
sudo -u postgres psql

# Criar banco de dados
CREATE DATABASE logistica_pedidos;

# Criar usuário
CREATE USER logistica_user WITH PASSWORD 'logistica2025';

# Conceder permissões
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;

# Sair do psql
\q
```

#### 2.3 Testar Conexão
```bash
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos -c "SELECT current_database();"
```

### 3. Configurar Variáveis de Ambiente

#### 3.1 Backend
```bash
# Criar arquivo .env na raiz do projeto
echo "DB_PASSWORD=logistica2025" > .env
echo "DB_USERNAME=logistica_user" >> .env
```

#### 3.2 Frontend
```bash
# Navegar para o diretório frontend
cd frontend

# Criar arquivo .env
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env
echo "REACT_APP_ENV=development" >> .env
```

##  Como Executar

### Opção 1: Execução Manual (Recomendado para Desenvolvimento)

#### 1. Iniciar o Backend
```bash
# Na raiz do projeto (technoSoftware/)
export DB_PASSWORD='logistica2025'
mvn spring-boot:run

# O backend estará disponível em: http://localhost:8080
# Health check: http://localhost:8080/api/health
```

#### 2. Iniciar o Frontend (em outro terminal)
```bash
# Navegar para o diretório frontend
cd frontend

# Instalar dependências (primeira vez)
npm install

# Iniciar o desenvolvimento
npm start

# O frontend estará disponível em: http://localhost:3000
```

### Opção 2: Execução em Background
```bash
# Backend em background
export DB_PASSWORD='logistica2025'
nohup mvn spring-boot:run > backend.log 2>&1 &

# Frontend em background
cd frontend
nohup npm start > frontend.log 2>&1 &
```

### Opção 3: Usando Screen (Recomendado para Servidores)
```bash
# Backend
screen -dmS backend bash -c "export DB_PASSWORD='logistica2025' && mvn spring-boot:run"

# Frontend  
screen -dmS frontend bash -c "cd frontend && npm start"

# Verificar sessões
screen -ls

# Conectar a uma sessão
screen -r backend  # ou screen -r frontend
```

##  Verificar se tudo está funcionando

### 1. Verificar Backend
```bash
curl http://localhost:8080/api/health
# Resposta esperada: "Orders Management System is running successfully!"
```

### 2. Verificar Frontend
```bash
curl -s http://localhost:3000 > /dev/null && echo "Frontend OK" || echo "Frontend Error"
# Resposta esperada: "Frontend OK"
```

### 3. Testar API de Clientes
```bash
curl http://localhost:8080/api/clientes
# Deve retornar lista de clientes em JSON
```

### 4. Abrir no Navegador
- **Frontend**: [http://localhost:3000](http://localhost:3000)
- **API Health**: [http://localhost:8080/api/health](http://localhost:8080/api/health)

##  Interface do Usuário

Acesse o sistema em [http://localhost:3000](http://localhost:3000) e você verá:

1. **Formulário de Criação de Pedidos**
   - Seleção de cliente (com limite de crédito visível)
   - Adição de produtos com quantidade
   - Visualização de itens adicionados
   - Cálculo automático do valor total

2. **Recursos da Interface**
   -  Notificações em tempo real (Snackbar)
   -  Indicadores de loading para todas as operações
   -  Status de conectividade com o servidor
   -  Botão de recarregar dados
   -  Design responsivo para mobile

##  Solução de Problemas

### Problemas Comuns

#### Backend não inicia
```bash
# Verificar se o PostgreSQL está rodando
sudo systemctl status postgresql

# Verificar conexão com o banco
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Verificar porta 8080
sudo lsof -i :8080
```

#### Frontend não inicia
```bash
# Limpar cache do npm
npm cache clean --force

# Reinstalar dependências
rm -rf node_modules package-lock.json
npm install

# Verificar porta 3000
sudo lsof -i :3000
```

#### Erro de CORS
- Verifique se o backend está rodando na porta 8080
- Confirme se as anotações @CrossOrigin estão presentes nos controllers

#### Erro de Conexão com o Banco
```bash
# Verificar se o usuário e senha estão corretos
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Verificar se o banco existe
sudo -u postgres psql -c "\l" | grep logistica
```

##  Dados de Teste

O sistema já vem com dados de teste pré-carregados:

### Clientes
- João Silva Santos (Limite: R$ 15.000,00)
- Maria Oliveira Costa (Limite: R$ 25.000,00)
- Carlos Eduardo Lima (Limite: R$ 10.000,00)
- Ana Paula Ferreira (Limite: R$ 30.000,00)

### Produtos
- Notebook Dell Inspiron 15 (R$ 2.800,00)
- Mouse Logitech MX Master 3 (R$ 450,00)
- Teclado Mecânico Corsair K95 (R$ 750,00)
- Monitor Samsung 27" 4K (R$ 1.200,00)
- Impressora HP LaserJet Pro (R$ 680,00)

### Exemplo de Uso
1. Selecione "Ana Paula Ferreira" (limite R$ 30.000)
2. Adicione "Notebook Dell" + "Mouse Logitech" = R$ 3.250
3. Submeta o pedido → **APROVADO** 

ou

1. Selecione "Maria Santos" (limite R$ 1.000) 
2. Adicione "Notebook Dell" = R$ 2.800
3. Submeta o pedido → **REJEITADO** 

##  Estrutura do Projeto

```
technoSoftware/
├── src/main/java/com/empresa/logistica/
│   ├── controller/          # Controladores REST
│   ├── service/             # Regras de negócio
│   ├── repository/          # Acesso a dados
│   ├── model/               # Entidades JPA
│   ├── dto/                 # Data Transfer Objects
│   └── config/              # Configurações
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/        # Scripts Flyway
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/      # Componentes React
│   │   ├── pages/           # Páginas da aplicação
│   │   ├── api/             # Serviços de API
│   │   └── utils/           # Utilitários
│   ├── package.json
│   └── .env
├── docs/                    # Documentação
├── pom.xml                  # Dependências Maven
└── README.md               # Este arquivo
```

##  Próximos Passos

Para expandir o sistema, considere implementar:

-  **Autenticação e Autorização** com Spring Security
-  **Dashboard Analytics** com gráficos de vendas
-  **Notificações por Email** para pedidos aprovados/rejeitados
-  **Gestão de Estoque** com controle de quantidade
-  **Integração de Pagamentos** com gateways
-  **App Mobile** com React Native
-  **Containerização** com Docker
-  **Deploy na Nuvem** (AWS, Azure, GCP)

##  Suporte

Se você encontrar problemas:

1. Verifique a seção [Solução de Problemas](#solução-de-problemas)
2. Consulte os logs:
   - Backend: Terminal onde executou `mvn spring-boot:run`
   - Frontend: Terminal onde executou `npm start`
3. Verifique se todos os serviços estão rodando:
   ```bash
   curl http://localhost:8080/api/health
   curl http://localhost:3000
   ```

---

** Sistema desenvolvido com Spring Boot 3.2 + React 19 + PostgreSQL 14**

*Última atualização: Agosto 2025*

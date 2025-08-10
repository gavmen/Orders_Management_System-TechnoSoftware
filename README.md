# Sistema de Gerenciamento de Pedidos

Sistema completo para gerenciamento de pedidos com validação automática de limite de crédito, desenvolvido com Spring Boot 3.2 e React 19.

![Status](https://img.shields.io/badge/Status-Production_Ready-green)
![Backend](https://img.shields.io/badge/Backend-Spring_Boot_3.2-brightgreen)
![Frontend](https://img.shields.io/badge/Frontend-React_19-blue)
![Database](https://img.shields.io/badge/Database-PostgreSQL_14-blue)

## Visão Geral

Este sistema processa pedidos de clientes com validação automática de limite de crédito baseada no histórico dos últimos 30 dias. A arquitetura segue padrões modernos com API REST no backend e interface responsiva no frontend.

### Funcionalidades Principais

- **Validação de Crédito**: Aprovação/rejeição automática baseada em limites de crédito
- **Interface Responsiva**: Frontend moderno com Material-UI
- **API RESTful**: Backend robusto com documentação OpenAPI
- **Auditoria Completa**: Registro de todos os pedidos para análise
- **Monitoramento**: Health checks e logs estruturados
- **Tempo Real**: Consulta de saldo de crédito atualizada dinamicamente

## Stack Tecnológico

### Backend
- **Java 17** com Spring Boot 3.2.0
- **Spring Data JPA** para persistência de dados
- **PostgreSQL 14** como banco de dados
- **Flyway** para migração de schema
- **Maven** para gerenciamento de dependências

### Frontend
- **React 19.1.1** com hooks modernos
- **Material-UI 7.3.1** para componentes UI
- **Axios 1.11.0** para comunicação HTTP
- **NPM** para gerenciamento de pacotes

## Pré-requisitos

- **Java 17+** - [Download OpenJDK](https://adoptium.net/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download Node.js](https://nodejs.org/)
- **PostgreSQL 14+** - [Download PostgreSQL](https://www.postgresql.org/download/)

### Verificar Instalações

```bash
java -version    # Deve mostrar: openjdk version "17.x.x"
mvn -version     # Deve mostrar: Apache Maven 3.8.x
node --version   # Deve mostrar: v18.x.x ou superior
psql --version   # Deve mostrar: psql (PostgreSQL) 14.x
```
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

## Documentação

### Guias Principais
- [Quick Start Guide](QUICK_START.md) - Setup rápido em 5 minutos
- [API Documentation](docs/API_DOCUMENTATION.md) - Referência completa da API REST
- [Technical Architecture](docs/TECHNICAL_ARCHITECTURE.md) - Arquitetura e design do sistema
- [Project Structure](docs/PROJECT_STRUCTURE.md) - Organização do código
- [Deployment Guide](docs/DEPLOYMENT_GUIDE.md) - Implantação em produção
- [Frontend Documentation](frontend/README.md) - Documentação específica do React

### Índice Completo
Ver [Documentation Index](docs/DOCUMENTATION_INDEX.md) para uma visão completa de toda a documentação disponível.

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
│   ├── src/
│   │   ├── pages/           # Componentes de página
│   │   ├── api/             # Serviços de API
│   │   └── components/      # Componentes reutilizáveis
│   └── package.json
├── docs/                    # Documentação técnica
├── scripts/                 # Scripts de gerenciamento
└── README.md               # Este arquivo
```

## Suporte e Manutenção

### Verificação de Saúde do Sistema
```bash
# Backend
curl http://localhost:8080/api/health

# Frontend  
curl http://localhost:3000
```

### Logs da Aplicação
- **Backend**: Disponível no terminal onde executou `mvn spring-boot:run`
- **Frontend**: Disponível no terminal onde executou `npm start`
- **Arquivos de Log**: Diretório `logs/` na raiz do projeto

### Scripts de Gerenciamento
O diretório `scripts/` contém utilitários para gerenciamento do sistema:
- Inicialização e parada do sistema
- Monitoramento de status
- Gerenciamento de banco de dados
- Visualização de logs

Para mais informações, consulte [scripts/SCRIPTS_README.md](scripts/SCRIPTS_README.md).

---

**Sistema desenvolvido para TechnoSoftware Assessment**  
*Última atualização: Agosto 2025*

*Última atualização: Agosto 2025*

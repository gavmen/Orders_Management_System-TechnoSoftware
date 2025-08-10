# Sistema de Gerenciamento de Pedidos - Guia de Implantação

## Visão Geral do Projeto

**Sistema de Gerenciamento de Pedidos** é uma aplicação web completa desenvolvida para empresas de logística que necessitam gerenciar pedidos com validação de limite de crédito dos clientes.

### Arquitetura do Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   PostgreSQL    │
│   React 19      │◄──►│  Spring Boot    │◄──►│   Database      │
│   Material-UI   │    │   Java 17       │    │      14+        │
│   Port: 3000    │    │   Port: 8080    │    │   Port: 5432    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Funcionalidades Principais

- **Gestão de Clientes**: Cadastro com limites de crédito individualizados
- **Catálogo de Produtos**: Sistema completo de produtos com preços
- **Criação de Pedidos**: Interface intuitiva para criação de pedidos
- **Validação de Crédito**: Verificação automática de limite de crédito
- **Controle de Estoque**: Gestão de quantidades por produto
- **Histórico de Pedidos**: Visualização completa dos pedidos realizados
- **API RESTful**: Endpoints completos para integração

---

## Requisitos do Sistema

### Requisitos de Hardware (Mínimos)
- **CPU**: 2 cores
- **RAM**: 4GB
- **Armazenamento**: 20GB de espaço livre
- **Rede**: Conexão com internet para download de dependências

### Requisitos de Hardware (Recomendados)
- **CPU**: 4+ cores
- **RAM**: 8GB+
- **Armazenamento**: 50GB+ SSD
- **Rede**: Conexão de banda larga

### Requisitos de Software

#### Sistema Operacional
- **Ubuntu**: 20.04 LTS ou superior
- **CentOS**: 7+ ou Rocky Linux 8+
- **Debian**: 10+
- **Windows**: 10+ (com WSL2 recomendado)
- **macOS**: 10.15+

#### Dependências Essenciais
```bash
# Java Development Kit
OpenJDK 17 ou Oracle JDK 17

# Apache Maven
Maven 3.8.0+

# Node.js & npm
Node.js 18.0+ 
npm 8.0+

# PostgreSQL
PostgreSQL 14.0+

# Git
Git 2.25+
```

---

## Instalação e Configuração

### 1. Preparação do Ambiente

#### Ubuntu/Debian
```bash
# Atualização do sistema
sudo apt update && sudo apt upgrade -y

# Instalação das dependências
sudo apt install -y openjdk-17-jdk maven nodejs npm postgresql postgresql-contrib git curl

# Verificação das versões
java -version
mvn -version
node -version
npm -version
psql --version
```

#### CentOS/RHEL/Rocky Linux
```bash
# Atualização do sistema
sudo dnf update -y

# Instalação das dependências
sudo dnf install -y java-17-openjdk java-17-openjdk-devel maven nodejs npm postgresql postgresql-server postgresql-contrib git curl

# Inicialização do PostgreSQL
sudo postgresql-setup --initdb
sudo systemctl enable --now postgresql

# Verificação das versões
java -version
mvn -version
node -version
npm -version
psql --version
```

### 2. Configuração do Banco de Dados

```bash
# Acesso ao PostgreSQL como usuário postgres
sudo -u postgres psql

# Dentro do PostgreSQL, execute:
CREATE DATABASE logistica_pedidos;
CREATE USER logistica_user WITH PASSWORD 'logistica2025';
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;
ALTER USER logistica_user CREATEDB;
\q

# Teste da conexão
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos -c "SELECT current_database();"
```

### 3. Clone e Configuração do Projeto

```bash
# Clone do repositório
git clone https://github.com/gavmen/TechnoSoftware-Assetment
cd technoSoftware

# Configuração das variáveis de ambiente
echo "DB_PASSWORD=logistica2025" > .env

# Verificação da estrutura
ls -la
```

### 4. Instalação das Dependências

#### Backend (Spring Boot)
```bash
# Na raiz do projeto
mvn clean install -DskipTests

# Teste de compilação
mvn clean compile
```

#### Frontend (React)
```bash
# Navegue para o diretório frontend
cd frontend

# Instalação das dependências
npm install

# Verificação da instalação
npm list --depth=0
```

---

## Execução da Aplicação

### Opção 1: Execução Manual (Desenvolvimento)

#### Terminal 1 - Backend
```bash
cd /path/to/technoSoftware
export DB_PASSWORD='logistica2025'
mvn spring-boot:run

# O backend estará disponível em: http://localhost:8080
# Health check: http://localhost:8080/api/health
```

#### Terminal 2 - Frontend
```bash
cd /path/to/technoSoftware/frontend
npm start

# O frontend estará disponível em: http://localhost:3000
# Aceite executar em uma porta alternativa se necessário
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

# Para acessar as sessões:
screen -r backend  # Ctrl+A+D para sair
screen -r frontend # Ctrl+A+D para sair

# Listar sessões ativas
screen -ls
```

---

## Teste da Aplicação

### 1. Verificação dos Serviços

```bash
# Verificar se os serviços estão rodando
curl http://localhost:8080/api/health
curl -s http://localhost:3000 > /dev/null && echo "Frontend OK" || echo "Frontend Erro"

# Verificar portas
sudo lsof -i :8080 -i :3000
```

### 2. População do Banco com Dados de Teste

```bash
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos << EOF
-- Inserção de clientes de teste
INSERT INTO cliente (nome, limite_credito) VALUES 
('João Silva Santos', 15000.00),
('Maria Santos Oliveira', 1000.00),
('Pedro Oliveira Costa', 10000.00),
('Ana Paula Ferreira', 5000.00),
('Carlos Eduardo Lima', 8000.00);

-- Inserção de produtos de teste
INSERT INTO produto (nome, preco) VALUES 
('Notebook Dell Inspiron 15', 2800.00),
('Mouse Logitech MX Master', 450.00),
('Teclado Corsair Mechanical', 750.00),
('Monitor Samsung 24"', 1200.00),
('Impressora HP LaserJet', 680.00),
('Webcam Logitech C920', 320.00),
('SSD Kingston 500GB', 280.00),
('Smartphone Samsung Galaxy', 1800.00);

-- Verificação dos dados inseridos
SELECT 'CLIENTES CADASTRADOS:' as info;
SELECT id, nome, limite_credito FROM cliente ORDER BY id;
SELECT 'PRODUTOS CADASTRADOS:' as info;  
SELECT id, nome, preco FROM produto ORDER BY id;
EOF
```

### 3. Teste das Funcionalidades

1. **Acesse o Frontend**: http://localhost:3000
2. **Selecione um Cliente**: Escolha um cliente da lista
3. **Visualize o Crédito**: Veja limite, valor utilizado e saldo disponível
4. **Adicione Produtos**: Selecione produtos e quantidades
5. **Teste Validação**: Tente criar pedidos que excedam o limite
6. **Finalize Pedidos**: Complete pedidos dentro do limite

### 4. Teste da API (Opcional)

```bash
# Listar clientes
curl http://localhost:8080/api/clients

# Listar produtos  
curl http://localhost:8080/api/products

# Criar pedido (exemplo)
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "itens": [
      {"produtoId": 1, "quantidade": 1}
    ]
  }'
```

---

## Segurança e Configuração

### Variáveis de Ambiente

```bash
# Variáveis essenciais
DB_PASSWORD=logistica2025
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# Variáveis opcionais
DB_HOST=localhost
DB_PORT=5432
DB_NAME=logistica_pedidos
DB_USER=logistica_user
```

### Configuração de Produção

#### 1. Configuração do PostgreSQL para Produção
```bash
# Edite o arquivo de configuração
sudo nano /etc/postgresql/14/main/postgresql.conf

# Configurações recomendadas:
shared_buffers = 256MB
effective_cache_size = 1GB
maintenance_work_mem = 64MB
checkpoint_completion_target = 0.9
wal_buffers = 16MB
default_statistics_target = 100
```

#### 2. Configuração de Firewall
```bash
# Ubuntu/Debian
sudo ufw allow 8080/tcp
sudo ufw allow 3000/tcp
sudo ufw enable

# CentOS/RHEL
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=3000/tcp
sudo firewall-cmd --reload
```

#### 3. Configuração de Proxy Reverso (Nginx)
```bash
# Instalação do Nginx
sudo apt install nginx

# Configuração
sudo nano /etc/nginx/sites-available/pedidos-app

# Conteúdo da configuração:
server {
    listen 80;
    server_name seu-dominio.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

# Ativação da configuração
sudo ln -s /etc/nginx/sites-available/pedidos-app /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## Solução de Problemas

### Backend não inicia

```bash
# Verificar se o PostgreSQL está rodando
sudo systemctl status postgresql

# Verificar conexão com o banco
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Verificar porta 8080
sudo lsof -i :8080

# Verificar logs
tail -f application.log
```

### Frontend não inicia

```bash
# Verificar versão do Node.js
node -version

# Limpar cache do npm
npm cache clean --force

# Reinstalar dependências
rm -rf node_modules package-lock.json
npm install

# Verificar porta 3000
sudo lsof -i :3000
```

### Erro de Conexão CORS

- Verifique se o backend está rodando
- Confirme se as anotações @CrossOrigin estão presentes nos controllers

### Erro de Conexão com o Banco

```bash
# Verificar se o usuário e senha estão corretos
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Verificar se o banco existe
sudo -u postgres psql -c "\l" | grep logistica

# Recriar usuário se necessário
sudo -u postgres psql -c "DROP USER IF EXISTS logistica_user;"
sudo -u postgres psql -c "CREATE USER logistica_user WITH PASSWORD 'logistica2025';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;"
```

---

## Monitoramento e Logs

### Localização dos Logs

```bash
# Logs do Backend
tail -f application.log
tail -f server.log

# Logs do Frontend (se usando nohup)
tail -f frontend.log

# Logs do PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-14-main.log
```

### Monitoramento de Recursos

```bash
# Uso de CPU e Memória
htop
top

# Uso de disco
df -h
du -sh /path/to/technoSoftware

# Conexões de rede
netstat -tulpn | grep -E '(8080|3000|5432)'
```

---

## Backup e Manutenção

### Backup do Banco de Dados

```bash
# Backup diário
PGPASSWORD='logistica2025' pg_dump -h localhost -U logistica_user logistica_pedidos > backup_$(date +%Y%m%d).sql

# Backup automatizado via crontab
crontab -e
# Adicione a linha:
0 2 * * * PGPASSWORD='logistica2025' pg_dump -h localhost -U logistica_user logistica_pedidos > /backup/logistica_$(date +\%Y\%m\%d).sql
```

### Restauração do Banco

```bash
# Restaurar backup
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user logistica_pedidos < backup_20250809.sql
```

### Atualizações do Sistema

```bash
# Parar os serviços
screen -S backend -X quit
screen -S frontend -X quit

# Atualizar código
git pull origin main

# Atualizar dependências
mvn clean install -DskipTests
cd frontend && npm update

# Reiniciar serviços
screen -dmS backend bash -c "export DB_PASSWORD='logistica2025' && mvn spring-boot:run"
screen -dmS frontend bash -c "cd frontend && npm start"
```

---

## Suporte Técnico

### Informações de Contato

- **Desenvolvedor**: Gabriel Mendonça
- **Email**: gabriel@empresa.com
- **Repositório**: https://github.com/gavmen/TechnoSoftware-Assetment
- **Documentação**: https://github.com/gavmen/TechnoSoftware-Assetment/wiki

### Relatório de Bugs

Para relatar bugs, por favor inclua:

1. **Versão do Sistema**: Frontend e Backend
2. **Sistema Operacional**: Versão e distribuição
3. **Navegador**: Se aplicável
4. **Passos para Reproduzir**: Sequência detalhada
5. **Comportamento Esperado**: O que deveria acontecer
6. **Comportamento Atual**: O que está acontecendo
7. **Logs**: Logs relevantes do backend/frontend
8. **Screenshots**: Se aplicável

---

## Licença e Créditos

**Sistema de Gerenciamento de Pedidos**
- **Versão**: 1.0.0  
- **Data**: Agosto 2025
- **Desenvolvedor**: Gabriel Mendonça
- **Tecnologias**: Spring Boot 3.2, React 19, Material-UI 7.3, PostgreSQL 14

### Tecnologias Utilizadas

- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL
- **Frontend**: React 19, Material-UI 7.3.1, Axios
- **Ferramentas**: Maven, npm, Git
- **Infraestrutura**: Ubuntu/Linux, Nginx (opcional)

---

**Última atualização**: 09 de Agosto de 2025  
**Versão do documento**: 1.0.0

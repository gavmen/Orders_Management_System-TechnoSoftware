#  Quick Start Guide - Sistema de Gerenciamento de Pedidos

##  Início Rápido (5 Minutos)

### Pré-requisitos
- Java 17+
- Node.js 18+  
- PostgreSQL 14+

### 1. Setup do Banco de Dados
```bash
# Criar banco e usuário
sudo -u postgres psql << EOF
CREATE DATABASE logistica_pedidos;
CREATE USER logistica_user WITH PASSWORD 'logistica2025';
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;
\q
EOF
```

### 2. Iniciar Backend
```bash
export DB_PASSWORD='logistica2025'
mvn spring-boot:run
```
 Backend rodando em http://localhost:8080

### 3. Iniciar Frontend
```bash
cd frontend
npm install && npm start
```
 Frontend rodando em http://localhost:3000

##  Testar o Sistema

### Acesse: http://localhost:3000

1. **Selecione um cliente** (ex: Ana Paula Ferreira - Limite R$ 30.000)
2. **Adicione produtos** (ex: Mouse Logitech - R$ 450)
3. **Submeta o pedido** → Verá notificação de  APROVADO

### Testar Rejeição
1. **Selecione cliente com limite baixo** (Maria Santos - R$ 1.000)
2. **Adicione produto caro** (Notebook Dell - R$ 2.800)
3. **Submeta o pedido** → Verá notificação de  REJEITADO

##  Próximos Passos

-  [README completo](../README.md) - Documentação detalhada
-  [API Documentation](docs/API_DOCUMENTATION.md) - Referência da API
-  [Deployment Guide](docs/DEPLOYMENT_GUIDE.md) - Deploy em produção

## Problemas?

```bash
# Verificar serviços
curl http://localhost:8080/api/health  # Backend
curl http://localhost:3000             # Frontend

# Verificar logs
# Backend: Terminal onde rodou mvn spring-boot:run
# Frontend: Terminal onde rodou npm start
```

---

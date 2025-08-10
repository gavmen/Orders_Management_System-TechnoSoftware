# Quick Start Guide - Orders Management System

## Quick Start (5 Minutes)

### Prerequisites
- Java 17+
- Node.js 18+  
- PostgreSQL 14+

### 1. Database Setup
```bash
# Create database and user
sudo -u postgres psql << EOF
CREATE DATABASE logistica_pedidos;
CREATE USER logistica_user WITH PASSWORD 'logistica2025';
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;
\q
EOF
```

### 2. Start Backend
```bash
export DB_PASSWORD='logistica2025'
mvn spring-boot:run
```
Backend running at http://localhost:8080

### 3. Start Frontend
```bash
cd frontend
npm install && npm start
```
Frontend running at http://localhost:3000

## Test the System

### Access: http://localhost:3000

1. **Select a client** (e.g.: Ana Paula Ferreira - Limit R$ 30,000)
2. **Add products** (e.g.: Mouse Logitech - R$ 450)
3. **Submit order** → You'll see APPROVED notification

### Test Rejection
1. **Select client with low limit** (Maria Santos - R$ 1,000)
2. **Add expensive product** (Notebook Dell - R$ 2,800)
3. **Submit order** → You'll see REJECTED notification

## Next Steps

- [Complete README](../README.md) - Detailed documentation
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

# Orders Management System

Complete orders management system with automatic credit limit validation, built with Spring Boot 3.2 and React 19.

![Status](https://img.shields.io/badge/Status-Production_Ready-green)
![Backend](https://img.shields.io/badge/Backend-Spring_Boot_3.2-brightgreen)
![Frontend](https://img.shields.io/badge/Frontend-React_19-blue)
![Database](https://img.shields.io/badge/Database-PostgreSQL_14-blue)

## Overview

This system processes customer orders with automatic credit limit validation based on the last 30 days history. The architecture follows modern patterns with REST API on the backend and responsive interface on the frontend.

### Main Features

- **Credit Validation**: Automatic approval/rejection based on credit limits
- **Responsive Interface**: Modern frontend with Material-UI
- **RESTful API**: Robust backend with OpenAPI documentation
- **Complete Audit**: Recording of all orders for analysis
- **Monitoring**: Health checks and structured logs
- **Real Time**: Dynamically updated credit balance queries

## Technology Stack

### Backend
- **Java 17** with Spring Boot 3.2.0
- **Spring Data JPA** for data persistence
- **PostgreSQL 14** as database
- **Flyway** for schema migration
- **Maven** for dependency management

### Frontend
- **React 19.1.1** with modern hooks
- **Material-UI 7.3.1** for UI components
- **Axios 1.11.0** for HTTP communication
- **NPM** for package management

## Prerequisites

- **Java 17+** - [Download OpenJDK](https://adoptium.net/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download Node.js](https://nodejs.org/)
- **PostgreSQL 14+** - [Download PostgreSQL](https://www.postgresql.org/download/)

### Verify Installations

```bash
java -version    # Should return: openjdk version "17.x.x"
mvn -version     # Should return: Apache Maven 3.8.x
node --version   # Should return: v18.x.x or higher
psql --version   # Should return: psql (PostgreSQL) 14.x
```

## Installation and Setup

### 1. Clone Repository
```bash
git clone <repository-url>
cd technoSoftware
```

### 2. Configure Database

#### 2.1 Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# Verify service is running
sudo systemctl status postgresql
```

#### 2.2 Create Database and User
```bash
# Connect as postgres user
sudo -u postgres psql

# Create database
CREATE DATABASE logistica_pedidos;

# Create user
CREATE USER logistica_user WITH PASSWORD 'logistica2025';

# Grant permissions
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;

# Exit psql
\q
```

#### 2.3 Test Connection
```bash
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos -c "SELECT current_database();"
```

### 3. Configure Environment Variables

#### 3.1 Backend
```bash
# Create .env file in project root
echo "DB_PASSWORD=logistica2025" > .env
echo "DB_USERNAME=logistica_user" >> .env
```

#### 3.2 Frontend
```bash
# Navigate to frontend directory
cd frontend

# Create .env file
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env
echo "REACT_APP_ENV=development" >> .env
```

## How to Run

### Option 1: Manual Execution (Recommended for Development)

#### 1. Start Backend
```bash
# In project root (Orders_Management_System-TechnoSoftware/)
export DB_PASSWORD='logistica2025'
mvn spring-boot:run

# Backend will be available at: http://localhost:8080
# Health check: http://localhost:8080/api/health
```

#### 2. Start Frontend (in another terminal)
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies (first time)
npm install

# Start development
npm start

# Frontend will be available at: http://localhost:3000
```

### Option 2: Background Execution
```bash
# Backend in background
export DB_PASSWORD='logistica2025'
nohup mvn spring-boot:run > backend.log 2>&1 &

# Frontend in background
cd frontend
nohup npm start > frontend.log 2>&1 &
```

### Option 3: Using Screen (Recommended for Servers)
```bash
# Backend
screen -dmS backend bash -c "export DB_PASSWORD='logistica2025' && mvn spring-boot:run"

# Frontend  
screen -dmS frontend bash -c "cd frontend && npm start"

# Check sessions
screen -ls

# Connect to a session
screen -r backend  # or screen -r frontend
```

## Verify Everything is Working

### 1. Check Backend
```bash
curl http://localhost:8080/api/health
# Expected response: "Orders Management System is running successfully!"
```

### 2. Check Frontend
```bash
curl -s http://localhost:3000 > /dev/null && echo "Frontend OK" || echo "Frontend Error"
# Expected response: "Frontend OK"
```

### 3. Test Clients API
```bash
curl http://localhost:8080/api/clientes
# Should return client list in JSON
```

### 4. Open in Browser
- **Frontend**: [http://localhost:3000](http://localhost:3000)
- **API Health**: [http://localhost:8080/api/health](http://localhost:8080/api/health)

## User Interface

Access the system at [http://localhost:3000](http://localhost:3000) and you will see:

1. **Order Creation Form**
   - Client selection (with visible credit limit)
   - Product addition with quantity
   - View of added items
   - Automatic total value calculation

2. **Interface Features**
   - Real-time notifications (Snackbar)
   - Loading indicators for all operations
   - Server connectivity status
   - Data reload button
   - Responsive design for mobile

## Troubleshooting

### Common Issues

#### Backend won't start
```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Check database connection
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Check port 8080
sudo lsof -i :8080
```

#### Frontend won't start
```bash
# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install

# Check port 3000
sudo lsof -i :3000
```

#### CORS Error
- Verify backend is running on port 8080
- Confirm @CrossOrigin annotations are present in controllers

#### Database Connection Error
```bash
# Check if user and password are correct
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Check if database exists
sudo -u postgres psql -c "\l" | grep logistica
```

## Test Data

The system comes with pre-loaded test data:

### Clients
- João Silva Santos (Limit: R$ 15,000.00)
- Maria Oliveira Costa (Limit: R$ 25,000.00)
- Carlos Eduardo Lima (Limit: R$ 10,000.00)
- Ana Paula Ferreira (Limit: R$ 30,000.00)

### Products
- Notebook Dell Inspiron 15 (R$ 2,800.00)
- Mouse Logitech MX Master 3 (R$ 450.00)
- Teclado Mecânico Corsair K95 (R$ 750.00)
- Monitor Samsung 27" 4K (R$ 1,200.00)
- Impressora HP LaserJet Pro (R$ 680.00)

### Usage Example
1. Select "Ana Paula Ferreira" (limit R$ 30,000)
2. Add "Notebook Dell" + "Mouse Logitech" = R$ 3,250
3. Submit order → **APROVADO** 

or

1. Select "Maria Santos" (limit R$ 1,000) 
2. Add "Notebook Dell" = R$ 2,800
3. Submit order → **REJEITADO** 

## Documentation

### Main Guides
- [Quick Start Guide](QUICK_START.md) - Quick setup in 5 minutes
- [API Documentation](docs/API_DOCUMENTATION.md) - Complete REST API reference
- [Technical Architecture](docs/TECHNICAL_ARCHITECTURE.md) - System architecture and design
- [Project Structure](docs/PROJECT_STRUCTURE.md) - Code organization
- [Deployment Guide](docs/DEPLOYMENT_GUIDE.md) - Production deployment
- [Frontend Documentation](frontend/README.md) - React specific documentation

### Complete Index
See [Documentation Index](docs/DOCUMENTATION_INDEX.md) for a complete overview of all available documentation.

## Project Structure

```
Orders_Management_System-TechnoSoftware/
├── src/main/java/com/empresa/logistica/
│   ├── controller/          # REST Controllers
│   ├── service/             # Business Logic
│   ├── repository/          # Data Access
│   ├── model/               # JPA Entities
│   ├── dto/                 # Data Transfer Objects
│   └── config/              # Configurations
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/        # Flyway Scripts
├── frontend/
│   ├── src/
│   │   ├── pages/           # Page Components
│   │   ├── api/             # API Services
│   │   └── components/      # Reusable Components
│   └── package.json
├── docs/                    # Technical Documentation
├── scripts/                 # Management Scripts
└── README.md               # This file
```

## Support and Maintenance

### System Health Check
```bash
# Backend
curl http://localhost:8080/api/health

# Frontend  
curl http://localhost:3000
```

### Application Logs
- **Backend**: run `mvn spring-boot:run`
- **Frontend**: run `npm start`
- **Log Files**: `logs/` directory in project root

---

**System developed for TechnoSoftware**  
*Last updated: August 2025*

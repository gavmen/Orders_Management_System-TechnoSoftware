# Orders Management System - Deployment Guide

## Project Overview

**Orders Management System** is a complete web application developed for logistics companies that need to manage orders with client credit limit validation.

### System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   PostgreSQL    │
│   React 19      │◄──►│  Spring Boot    │◄──►│   Database      │
│   Material-UI   │    │   Java 17       │    │      14+        │
│   Port: 3000    │    │   Port: 8080    │    │   Port: 5432    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Main Features

- **Client Management**: Registration with individualized credit limits
- **Product Catalog**: Complete product system with prices
- **Order Creation**: Intuitive interface for order creation
- **Credit Validation**: Automatic credit limit verification
- **Stock Control**: Quantity management per product
- **Order History**: Complete view of placed orders
- **RESTful API**: Complete endpoints for integration

---

## System Requirements

### Hardware Requirements (Minimum)
- **CPU**: 2 cores
- **RAM**: 4GB
- **Storage**: 20GB free space
- **Network**: Internet connection for dependency downloads

### Hardware Requirements (Recommended)
- **CPU**: 4+ cores
- **RAM**: 8GB+
- **Storage**: 50GB+ SSD
- **Network**: Broadband connection

### Software Requirements

#### Operating System
- **Ubuntu**: 20.04 LTS or higher
- **CentOS**: 7+ or Rocky Linux 8+
- **Debian**: 10+
- **Windows**: 10+ (WSL2 recommended)
- **macOS**: 10.15+

#### Essential Dependencies
```bash
# Java Development Kit
OpenJDK 17 or Oracle JDK 17

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

## Installation and Configuration

### 1. Environment Preparation

#### Ubuntu/Debian
```bash
# System update
sudo apt update && sudo apt upgrade -y

# Dependencies installation
sudo apt install -y openjdk-17-jdk maven nodejs npm postgresql postgresql-contrib git curl

# Version verification
java -version
mvn -version
node -version
npm -version
psql --version
```

#### CentOS/RHEL/Rocky Linux
```bash
# System update
sudo dnf update -y

# Dependencies installation
sudo dnf install -y java-17-openjdk java-17-openjdk-devel maven nodejs npm postgresql postgresql-server postgresql-contrib git curl

# PostgreSQL initialization
sudo postgresql-setup --initdb
sudo systemctl enable --now postgresql

# Version verification
java -version
mvn -version
node -version
npm -version
psql --version
```

### 2. Database Configuration

```bash
# Access PostgreSQL as postgres user
sudo -u postgres psql

# Inside PostgreSQL, execute:
CREATE DATABASE logistica_pedidos;
CREATE USER logistica_user WITH PASSWORD 'logistica2025';
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;
ALTER USER logistica_user CREATEDB;
\q

# Connection test
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos -c "SELECT current_database();"
```

### 3. Project Clone and Configuration

```bash
# Repository clone
git clone https://github.com/gavmen/TechnoSoftware-Assetment
cd technoSoftware

# Environment variables configuration
echo "DB_PASSWORD=logistica2025" > .env

# Structure verification
ls -la
```

### 4. Dependencies Installation

#### Backend (Spring Boot)
```bash
# In project root
mvn clean install -DskipTests

# Compilation test
mvn clean compile
```

#### Frontend (React)
```bash
# Navigate to frontend directory
cd frontend

# Dependencies installation
npm install

# Installation verification
npm list --depth=0
```

---

## Application Execution

### Option 1: Manual Execution (Development)

#### Terminal 1 - Backend
```bash
cd /path/to/technoSoftware
export DB_PASSWORD='logistica2025'
mvn spring-boot:run

# Backend will be available at: http://localhost:8080
# Health check: http://localhost:8080/api/health
```

#### Terminal 2 - Frontend
```bash
cd /path/to/technoSoftware/frontend
npm start

# Frontend will be available at: http://localhost:3000
# Accept running on alternative port if necessary
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

# To access sessions:
screen -r backend  # Ctrl+A+D to exit
screen -r frontend # Ctrl+A+D to exit

# List active sessions
screen -ls
```

---

## Application Testing

### 1. Services Verification

```bash
# Check if services are running
curl http://localhost:8080/api/health
curl -s http://localhost:3000 > /dev/null && echo "Frontend OK" || echo "Frontend Error"

# Check ports
sudo lsof -i :8080 -i :3000
```

### 2. Database Population with Test Data

```bash
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos << EOF
-- Test clients insertion
INSERT INTO cliente (nome, limite_credito) VALUES 
('João Silva Santos', 15000.00),
('Maria Santos Oliveira', 1000.00),
('Pedro Oliveira Costa', 10000.00),
('Ana Paula Ferreira', 5000.00),
('Carlos Eduardo Lima', 8000.00);

-- Test products insertion
INSERT INTO produto (nome, preco) VALUES 
('Notebook Dell Inspiron 15', 2800.00),
('Mouse Logitech MX Master', 450.00),
('Teclado Corsair Mechanical', 750.00),
('Monitor Samsung 24"', 1200.00),
('Impressora HP LaserJet', 680.00),
('Webcam Logitech C920', 320.00),
('SSD Kingston 500GB', 280.00),
('Smartphone Samsung Galaxy', 1800.00);

-- Verification of inserted data
SELECT 'REGISTERED CLIENTS:' as info;
SELECT id, nome, limite_credito FROM cliente ORDER BY id;
SELECT 'REGISTERED PRODUCTS:' as info;  
SELECT id, nome, preco FROM produto ORDER BY id;
EOF
```

### 3. Functionality Testing

1. **Access Frontend**: http://localhost:3000
2. **Select a Client**: Choose a client from the list
3. **View Credit**: See limit, used amount, and available balance
4. **Add Products**: Select products and quantities
5. **Test Validation**: Try to create orders that exceed the limit
6. **Complete Orders**: Complete orders within the limit

### 4. API Testing (Optional)

```bash
# List clients
curl http://localhost:8080/api/clientes

# Get real-time credit information
curl http://localhost:8080/api/clientes/1/credito

# List products  
curl http://localhost:8080/api/produtos

# Create order (example)
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

## Security and Configuration

### Environment Variables

```bash
# Essential variables
DB_PASSWORD=logistica2025
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# Optional variables
DB_HOST=localhost
DB_PORT=5432
DB_NAME=logistica_pedidos
DB_USER=logistica_user
```

### Production Configuration

#### 1. PostgreSQL Production Configuration
```bash
# Edit configuration file
sudo nano /etc/postgresql/14/main/postgresql.conf

# Recommended settings:
shared_buffers = 256MB
effective_cache_size = 1GB
maintenance_work_mem = 64MB
checkpoint_completion_target = 0.9
wal_buffers = 16MB
default_statistics_target = 100
```

#### 2. Firewall Configuration
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

#### 3. Reverse Proxy Configuration (Nginx)
```bash
# Nginx installation
sudo apt install nginx

# Configuration
sudo nano /etc/nginx/sites-available/orders-app

# Configuration content:
server {
    listen 80;
    server_name your-domain.com;

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

# Configuration activation
sudo ln -s /etc/nginx/sites-available/orders-app /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## Troubleshooting

### Backend won't start

```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Check database connection
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Check port 8080
sudo lsof -i :8080

# Check logs
tail -f application.log
```

### Frontend won't start

```bash
# Check Node.js version
node -version

# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install

# Check port 3000
sudo lsof -i :3000
```

### CORS Connection Error

- Check if backend is running
- Confirm @CrossOrigin annotations are present in controllers

### Database Connection Error

```bash
# Check if user and password are correct
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos

# Check if database exists
sudo -u postgres psql -c "\l" | grep logistica

# Recreate user if necessary
sudo -u postgres psql -c "DROP USER IF EXISTS logistica_user;"
sudo -u postgres psql -c "CREATE USER logistica_user WITH PASSWORD 'logistica2025';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;"
```

---

## Monitoring and Logs

### Log Locations

```bash
# Backend logs
tail -f application.log
tail -f server.log

# Frontend logs (if using nohup)
tail -f frontend.log

# PostgreSQL logs
sudo tail -f /var/log/postgresql/postgresql-14-main.log
```

### Resource Monitoring

```bash
# CPU and Memory usage
htop
top

# Disk usage
df -h
du -sh /path/to/technoSoftware

# Network connections
netstat -tulpn | grep -E '(8080|3000|5432)'
```

---

## Backup and Maintenance

### Database Backup

```bash
# Daily backup
PGPASSWORD='logistica2025' pg_dump -h localhost -U logistica_user logistica_pedidos > backup_$(date +%Y%m%d).sql

# Automated backup via crontab
crontab -e
# Add line:
0 2 * * * PGPASSWORD='logistica2025' pg_dump -h localhost -U logistica_user logistica_pedidos > /backup/logistica_$(date +\%Y\%m\%d).sql
```

### Database Restoration

```bash
# Restore backup
PGPASSWORD='logistica2025' psql -h localhost -U logistica_user logistica_pedidos < backup_20250809.sql
```

### System Updates

```bash
# Stop services
screen -S backend -X quit
screen -S frontend -X quit

# Update code
git pull origin main

# Update dependencies
mvn clean install -DskipTests
cd frontend && npm update

# Restart services
screen -dmS backend bash -c "export DB_PASSWORD='logistica2025' && mvn spring-boot:run"
screen -dmS frontend bash -c "cd frontend && npm start"
```

---

## Technical Support

### Contact Information

- **Developer**: Gabriel Mendonça
- **Email**: dev.gabrielmendonca@gmail.com
- **Repository**: https://github.com/gavmen/TechnoSoftware-Assetment
- **Documentation**: https://github.com/gavmen/TechnoSoftware-Assetment/wiki

### Bug Reports

To report bugs, please include:

1. **System Version**: Frontend and Backend
2. **Operating System**: Version and distribution
3. **Browser**: If applicable
4. **Steps to Reproduce**: Detailed sequence
5. **Expected Behavior**: What should happen
6. **Current Behavior**: What is happening
7. **Logs**: Relevant backend/frontend logs
8. **Screenshots**: If applicable

---

## License and Credits

**Orders Management System**
- **Version**: 1.0.0  
- **Date**: August 2025
- **Developer**: Gabriel Mendonça
- **Technologies**: Spring Boot 3.2, React 19, Material-UI 7.3, PostgreSQL 14

### Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL
- **Frontend**: React 19, Material-UI 7.3.1, Axios
- **Tools**: Maven, npm, Git
- **Infrastructure**: Ubuntu/Linux, Nginx (optional)

---

**Last updated**: August 9, 2025  
**Document version**: 1.0.0

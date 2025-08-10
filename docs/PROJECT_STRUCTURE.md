# Project Structure - Orders Management System

## Overview

This document describes the complete structure of the Customer Orders Management System, including backend services, frontend application, database schema, and supporting scripts.

## Root Directory Structure

```
technoSoftware/
├── src/                        # Backend source code (Spring Boot)
├── frontend/                   # Frontend application (React)
├── docs/                       # Project documentation
├── scripts/                    # System management scripts
├── logs/                       # Application log files
├── target/                     # Maven build output
├── pom.xml                     # Maven project configuration
├── README.md                   # Main project documentation
├── QUICK_START.md             # Quick setup guide
├── docker-compose.yml         # Docker services configuration
├── Dockerfile                 # Backend container definition
└── *.log                      # Application log files
```

## Backend Structure (src/)

### Source Code Organization

```
src/main/java/com/empresa/logistica/
├── OrdersManagementSystemApplication.java  # Main Spring Boot application
├── controller/                              # REST API controllers
│   ├── ClienteController.java              # Customer management API
│   ├── PedidoController.java               # Order management API
│   ├── ProdutoController.java              # Product management API
│   └── HealthController.java               # Health check endpoint
├── service/                                 # Business logic layer
│   ├── PedidoService.java                  # Order service interface
│   └── impl/                               # Service implementations
│       └── PedidoServiceImpl.java          # Order business logic
├── repository/                              # Data access layer
│   ├── BaseRepository.java                 # Base repository interface
│   ├── ClienteRepository.java              # Customer data access
│   ├── PedidoRepository.java               # Order data access
│   ├── ProdutoRepository.java              # Product data access
│   └── ItemPedidoRepository.java           # Order item data access
├── model/                                   # JPA entities
│   ├── BaseEntity.java                     # Base entity with common fields
│   ├── Cliente.java                        # Customer entity
│   ├── Pedido.java                         # Order entity
│   ├── Produto.java                        # Product entity
│   ├── ItemPedido.java                     # Order item entity
│   └── StatusPedido.java                   # Order status enumeration
├── dto/                                     # Data transfer objects
│   ├── ClienteDTO.java                     # Customer DTO
│   ├── PedidoDTO.java                      # Order DTO
│   ├── ProdutoDTO.java                     # Product DTO
│   └── request/                            # Request DTOs
│       ├── CriarPedidoRequest.java         # Create order request
│       └── ItemPedidoRequest.java          # Order item request
├── mapper/                                  # Entity/DTO mapping
│   ├── ClienteMapper.java                  # Customer mapper
│   └── ProdutoMapper.java                  # Product mapper
├── config/                                  # Configuration classes
│   ├── ApplicationConstants.java           # Application constants
│   └── WebConfig.java                      # Web configuration (CORS, etc.)
└── exception/                               # Exception handling
    ├── GlobalExceptionHandler.java         # Global exception handler
    └── ErrorResponse.java                  # Error response structure
```

### Resources Structure

```
src/main/resources/
├── application.properties         # Main application configuration
├── application-production.properties  # Production configuration
├── data.sql                      # Initial data loading
└── db/migration/                 # Flyway database migrations
    ├── V1__Create_initial_schema.sql
    ├── V2__Add_Performance_Indexes.sql
    └── V3__Add_Data_Integrity_Constraints.sql
```

### Test Structure

```
src/test/java/com/empresa/logistica/
├── OrdersManagementSystemApplicationTests.java
├── controller/                   # Controller tests
├── model/                        # Entity tests
└── service/                      # Service layer tests
```

## Frontend Structure (frontend/)

### Application Structure

```
frontend/
├── public/                       # Static assets
│   ├── index.html               # HTML template
│   ├── favicon.ico              # Application icon
│   └── manifest.json            # PWA manifest
├── src/                         # Source code
│   ├── App.js                   # Root component
│   ├── App.css                  # Global styles
│   ├── index.js                 # Application entry point
│   ├── index.css                # Base CSS
│   ├── theme.js                 # Material-UI theme
│   ├── api/                     # API layer
│   │   ├── api.js               # Axios configuration
│   │   └── services.js          # API service functions
│   ├── components/              # Reusable UI components
│   ├── pages/                   # Page components
│   │   └── PedidoForm.js        # Main order form
│   ├── services/                # Business logic services
│   └── utils/                   # Utility functions
├── build/                       # Production build output
├── package.json                 # NPM dependencies
└── README.md                    # Frontend documentation
```

## Database Schema

### Table Relationships

```
Cliente (1) ----< (N) Pedido (1) ----< (N) ItemPedido >---- (1) Produto
```

### Table Structure

- **cliente**: Customer information and credit limits
- **produto**: Product catalog with pricing
- **pedido**: Order records with status and totals
- **item_pedido**: Order line items with quantities and subtotals

## Scripts Directory (scripts/)

### Management Scripts

```
scripts/
├── SCRIPTS_README.md           # Scripts documentation
├── config.env                  # Environment configuration
├── techno.sh                   # Master control script
├── core/                       # Core system operations
│   ├── start-system.sh        # System startup
│   └── stop-system.sh         # System shutdown
├── monitoring/                 # System monitoring
│   ├── status-system.sh       # Status checking
│   └── show-logs.sh           # Log management
├── database/                   # Database operations
│   └── manage-database.sh     # Database management
├── utils/                      # Utilities
│   └── setup.sh               # Initial setup
└── lib/                       # Shared libraries
    └── common.sh              # Common functions
```

## Configuration Files

### Backend Configuration

- **application.properties**: Database connection, JPA settings, server configuration
- **application-production.properties**: Production-specific settings
- **pom.xml**: Maven dependencies and build configuration

### Frontend Configuration

- **package.json**: NPM dependencies and scripts
- **src/api/api.js**: API base URL and Axios configuration

## Build Artifacts

### Backend (target/)

- Compiled Java classes
- JAR files for deployment
- Test reports
- Maven build metadata

### Frontend (build/)

- Optimized production bundle
- Static assets
- Build manifests

## Documentation (docs/)

- **API_DOCUMENTATION.md**: REST API reference
- **TECHNICAL_ARCHITECTURE.md**: System architecture details
- **DEPLOYMENT_GUIDE.md**: Deployment instructions
- **ER_DIAGRAM.md**: Database design documentation
- **PROJECT_STRUCTURE.md**: This file

This structure supports a scalable, maintainable application with clear separation of concerns between presentation, business logic, and data layers.

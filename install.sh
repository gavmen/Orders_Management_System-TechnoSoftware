#!/bin/bash

# ============================================
# Sistema de Gerenciamento de Pedidos
# Script de Instalação Automática
# Versão: 1.0.0
# Data: Agosto 2025
# ============================================

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================================${NC}"
    echo -e "${BLUE} $1 ${NC}"
    echo -e "${BLUE}================================================${NC}"
}

# Check if running as root
if [[ $EUID -eq 0 ]]; then
   print_error "Este script não deve ser executado como root"
   exit 1
fi

print_header "SISTEMA DE GERENCIAMENTO DE PEDIDOS - INSTALAÇÃO"

print_status "Iniciando instalação do Sistema de Gerenciamento de Pedidos..."

# Detect OS
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    if [[ -f /etc/debian_version ]]; then
        OS="debian"
        print_status "Sistema detectado: Debian/Ubuntu"
    elif [[ -f /etc/redhat-release ]]; then
        OS="redhat"
        print_status "Sistema detectado: RedHat/CentOS/Fedora"
    else
        print_warning "Sistema Linux não identificado. Assumindo Debian/Ubuntu."
        OS="debian"
    fi
elif [[ "$OSTYPE" == "darwin"* ]]; then
    OS="macos"
    print_status "Sistema detectado: macOS"
else
    print_error "Sistema operacional não suportado: $OSTYPE"
    exit 1
fi

# Function to install dependencies
install_dependencies() {
    print_header "INSTALAÇÃO DE DEPENDÊNCIAS"
    
    if [[ "$OS" == "debian" ]]; then
        print_status "Atualizando lista de pacotes..."
        sudo apt update
        
        print_status "Instalando dependências..."
        sudo apt install -y openjdk-17-jdk maven nodejs npm postgresql postgresql-contrib curl wget git
        
    elif [[ "$OS" == "redhat" ]]; then
        print_status "Atualizando sistema..."
        sudo dnf update -y
        
        print_status "Instalando dependências..."
        sudo dnf install -y java-17-openjdk java-17-openjdk-devel maven nodejs npm postgresql postgresql-server postgresql-contrib curl wget git
        
        print_status "Inicializando PostgreSQL..."
        sudo postgresql-setup --initdb 2>/dev/null || true
        sudo systemctl enable postgresql
        sudo systemctl start postgresql
        
    elif [[ "$OS" == "macos" ]]; then
        if ! command -v brew &> /dev/null; then
            print_error "Homebrew não encontrado. Por favor, instale o Homebrew primeiro:"
            print_error "https://brew.sh"
            exit 1
        fi
        
        print_status "Instalando dependências via Homebrew..."
        brew install openjdk@17 maven node postgresql git
        brew services start postgresql
    fi
}

# Function to verify installations
verify_installations() {
    print_header "VERIFICAÇÃO DE INSTALAÇÕES"
    
    # Java
    if java -version 2>&1 | grep -q "17\|21"; then
        print_status "✓ Java 17+ detectado"
        java -version
    else
        print_error "✗ Java 17+ não encontrado"
        exit 1
    fi
    
    # Maven
    if command -v mvn &> /dev/null; then
        print_status "✓ Maven detectado"
        mvn --version | head -1
    else
        print_error "✗ Maven não encontrado"
        exit 1
    fi
    
    # Node.js
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node --version)
        print_status "✓ Node.js detectado: $NODE_VERSION"
    else
        print_error "✗ Node.js não encontrado"
        exit 1
    fi
    
    # npm
    if command -v npm &> /dev/null; then
        NPM_VERSION=$(npm --version)
        print_status "✓ npm detectado: $NPM_VERSION"
    else
        print_error "✗ npm não encontrado"
        exit 1
    fi
    
    # PostgreSQL
    if command -v psql &> /dev/null; then
        PSQL_VERSION=$(psql --version)
        print_status "✓ PostgreSQL detectado: $PSQL_VERSION"
    else
        print_error "✗ PostgreSQL não encontrado"
        exit 1
    fi
}

# Function to setup database
setup_database() {
    print_header "CONFIGURAÇÃO DO BANCO DE DADOS"
    
    print_status "Configurando banco de dados PostgreSQL..."
    
    # Create database and user
    sudo -u postgres psql << EOF
CREATE DATABASE logistica_pedidos;
CREATE USER logistica_user WITH PASSWORD 'logistica2025';
GRANT ALL PRIVILEGES ON DATABASE logistica_pedidos TO logistica_user;
ALTER USER logistica_user CREATEDB;
\q
EOF

    print_status "Testando conexão com o banco..."
    
    if PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos -c "SELECT current_database();" > /dev/null 2>&1; then
        print_status "✓ Conexão com banco de dados bem-sucedida"
    else
        print_error "✗ Falha na conexão com o banco de dados"
        exit 1
    fi
}

# Function to build project
build_project() {
    print_header "COMPILAÇÃO DO PROJETO"
    
    print_status "Compilando backend..."
    mvn clean compile -q
    
    if [[ $? -eq 0 ]]; then
        print_status "✓ Backend compilado com sucesso"
    else
        print_error "✗ Falha na compilação do backend"
        exit 1
    fi
    
    print_status "Instalando dependências do frontend..."
    cd frontend
    npm install --silent
    
    if [[ $? -eq 0 ]]; then
        print_status "✓ Dependências do frontend instaladas"
    else
        print_error "✗ Falha na instalação das dependências do frontend"
        exit 1
    fi
    
    cd ..
}

# Function to create environment file
create_env_file() {
    print_header "CRIAÇÃO DE ARQUIVO DE AMBIENTE"
    
    if [[ ! -f .env ]]; then
        print_status "Criando arquivo .env..."
        echo "DB_PASSWORD=logistica2025" > .env
        print_status "✓ Arquivo .env criado"
    else
        print_warning "Arquivo .env já existe, pulando..."
    fi
}

# Function to populate test data
populate_test_data() {
    print_header "POPULAÇÃO DE DADOS DE TESTE"
    
    read -p "Deseja popular o banco com dados de teste? (s/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        print_status "Populando banco com dados de teste..."
        
        PGPASSWORD='logistica2025' psql -h localhost -U logistica_user -d logistica_pedidos << EOF
-- Inserção de clientes de teste
INSERT INTO cliente (nome, limite_credito) VALUES 
('João Silva Santos', 15000.00),
('Maria Santos Oliveira', 1000.00),
('Pedro Oliveira Costa', 10000.00),
('Ana Paula Ferreira', 5000.00),
('Carlos Eduardo Lima', 8000.00)
ON CONFLICT DO NOTHING;

-- Inserção de produtos de teste
INSERT INTO produto (nome, preco) VALUES 
('Notebook Dell Inspiron 15', 2800.00),
('Mouse Logitech MX Master', 450.00),
('Teclado Corsair Mechanical', 750.00),
('Monitor Samsung 24"', 1200.00),
('Impressora HP LaserJet', 680.00),
('Webcam Logitech C920', 320.00),
('SSD Kingston 500GB', 280.00),
('Smartphone Samsung Galaxy', 1800.00)
ON CONFLICT DO NOTHING;
EOF

        print_status "✓ Dados de teste inseridos"
    else
        print_status "Pulando inserção de dados de teste"
    fi
}

# Function to create start scripts
create_start_scripts() {
    print_header "CRIAÇÃO DE SCRIPTS DE INICIALIZAÇÃO"
    
    # Backend start script
    cat > start_backend.sh << 'EOF'
#!/bin/bash
echo "Iniciando backend..."
export DB_PASSWORD='logistica2025'
mvn spring-boot:run
EOF
    chmod +x start_backend.sh
    
    # Frontend start script
    cat > start_frontend.sh << 'EOF'
#!/bin/bash
echo "Iniciando frontend..."
cd frontend
npm start
EOF
    chmod +x start_frontend.sh
    
    # Combined start script
    cat > start_all.sh << 'EOF'
#!/bin/bash
echo "Iniciando Sistema de Gerenciamento de Pedidos..."

# Start backend in background
echo "Iniciando backend..."
screen -dmS backend bash -c "export DB_PASSWORD='logistica2025' && mvn spring-boot:run"

# Wait a bit for backend to start
sleep 10

# Start frontend
echo "Iniciando frontend..."
screen -dmS frontend bash -c "cd frontend && npm start"

echo "Sistema iniciado!"
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:3000"
echo ""
echo "Para acessar os logs:"
echo "  Backend:  screen -r backend"
echo "  Frontend: screen -r frontend"
echo ""
echo "Para sair do screen: Ctrl+A+D"
EOF
    chmod +x start_all.sh
    
    print_status "✓ Scripts de inicialização criados"
}

# Main installation process
main() {
    # Check if we're in the right directory
    if [[ ! -f pom.xml ]] || [[ ! -d frontend ]]; then
        print_error "Execute este script na raiz do projeto technoSoftware"
        print_error "Estrutura esperada: pom.xml e diretório frontend/"
        exit 1
    fi
    
    install_dependencies
    verify_installations
    setup_database
    create_env_file
    build_project
    populate_test_data
    create_start_scripts
    
    print_header "INSTALAÇÃO CONCLUÍDA"
    print_status "✓ Sistema de Gerenciamento de Pedidos instalado com sucesso!"
    echo
    print_status "Para iniciar a aplicação:"
    print_status "  • Manualmente: ./start_backend.sh (terminal 1) e ./start_frontend.sh (terminal 2)"
    print_status "  • Automaticamente: ./start_all.sh"
    echo
    print_status "URLs de acesso:"
    print_status "  • Frontend: http://localhost:3000"
    print_status "  • Backend API: http://localhost:8080"
    print_status "  • Health Check: http://localhost:8080/api/health"
    echo
    print_status "Para suporte técnico, consulte o arquivo DEPLOYMENT_GUIDE.md"
    echo
    print_warning "IMPORTANTE: Altere a senha do banco de dados em produção!"
}

# Run main function
main "$@"

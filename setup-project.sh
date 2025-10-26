#!/bin/bash

# 🚀 Script d'Installation et Configuration Multi-Langages
# Usage: ./setup-project.sh <language> <project-name>

set -e

LANGUAGE=${1:-java}
PROJECT_NAME=${2:-my-crud-app}
API_URL="http://localhost:8080"

echo "🚀 Configuration Projet CRUD - $LANGUAGE"
echo "========================================"

# Vérifier les prérequis
check_prerequisites() {
    echo "🔍 Vérification des prérequis..."
    
    case $LANGUAGE in
        java)
            command -v java >/dev/null 2>&1 || { echo "❌ Java requis"; exit 1; }
            command -v mvn >/dev/null 2>&1 || { echo "❌ Maven requis"; exit 1; }
            ;;
        python)
            command -v python3 >/dev/null 2>&1 || { echo "❌ Python3 requis"; exit 1; }
            command -v pip3 >/dev/null 2>&1 || { echo "❌ pip3 requis"; exit 1; }
            ;;
        csharp)
            command -v dotnet >/dev/null 2>&1 || { echo "❌ .NET SDK requis"; exit 1; }
            ;;
        typescript)
            command -v node >/dev/null 2>&1 || { echo "❌ Node.js requis"; exit 1; }
            command -v npm >/dev/null 2>&1 || { echo "❌ npm requis"; exit 1; }
            ;;
        php)
            command -v php >/dev/null 2>&1 || { echo "❌ PHP requis"; exit 1; }
            command -v composer >/dev/null 2>&1 || { echo "❌ Composer requis"; exit 1; }
            ;;
    esac
    echo "✅ Prérequis OK"
}

# Démarrer l'API si nécessaire
start_api() {
    if ! curl -s "$API_URL/actuator/health" >/dev/null 2>&1; then
        echo "🚀 Démarrage de l'API..."
        mvn spring-boot:run > api.log 2>&1 &
        API_PID=$!
        echo $API_PID > api.pid
        
        # Attendre que l'API soit prête
        for i in {1..30}; do
            if curl -s "$API_URL/actuator/health" >/dev/null 2>&1; then
                echo "✅ API démarrée"
                break
            fi
            sleep 2
        done
    else
        echo "✅ API déjà active"
    fi
}

# Générer le projet
generate_project() {
    echo "📦 Génération du projet $LANGUAGE..."
    
    # Créer la requête JSON
    cat > temp-request.json << EOF
{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +validateEmail()\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n    }\n    User \"1\" --> \"*\" Product",
    "language": "$LANGUAGE",
    "packageName": "com.example.$PROJECT_NAME"
}
EOF

    # Générer le code
    curl -s -X POST "$API_URL/api/generate/crud" \
        -H "Content-Type: application/json" \
        -d @temp-request.json \
        -o "$PROJECT_NAME-$LANGUAGE.zip"
    
    rm temp-request.json
    
    if [ ! -f "$PROJECT_NAME-$LANGUAGE.zip" ] || [ ! -s "$PROJECT_NAME-$LANGUAGE.zip" ]; then
        echo "❌ Échec de la génération"
        exit 1
    fi
    
    echo "✅ Code généré: $PROJECT_NAME-$LANGUAGE.zip"
}

# Extraire et configurer le projet
setup_project() {
    echo "📁 Configuration du projet..."
    
    mkdir -p "$PROJECT_NAME"
    unzip -q "$PROJECT_NAME-$LANGUAGE.zip" -d "$PROJECT_NAME"
    cd "$PROJECT_NAME"
    
    case $LANGUAGE in
        java)
            setup_java
            ;;
        python)
            setup_python
            ;;
        csharp)
            setup_csharp
            ;;
        typescript)
            setup_typescript
            ;;
        php)
            setup_php
            ;;
    esac
}

# Configuration Java
setup_java() {
    echo "☕ Configuration Java Spring Boot..."
    
    # Créer pom.xml si manquant
    if [ ! -f "pom.xml" ]; then
        cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>crud-app</artifactId>
    <version>1.0.0</version>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
EOF
    fi
    
    # Créer Application.java
    mkdir -p src/main/java/com/example
    cat > src/main/java/com/example/Application.java << 'EOF'
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
EOF
    
    # Installer dépendances
    echo "📦 Installation des dépendances Maven..."
    mvn clean compile -q
    
    echo "🚀 Démarrage: mvn spring-boot:run"
}

# Configuration Python
setup_python() {
    echo "🐍 Configuration Python FastAPI..."
    
    # Créer requirements.txt
    cat > requirements.txt << 'EOF'
fastapi==0.104.1
uvicorn==0.24.0
sqlalchemy==2.0.23
pydantic==2.5.0
python-multipart==0.0.6
EOF
    
    # Créer main.py
    cat > main.py << 'EOF'
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(title="Generated CRUD API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def read_root():
    return {"message": "CRUD API is running"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
EOF
    
    # Créer environnement virtuel
    python3 -m venv venv
    source venv/bin/activate
    
    # Installer dépendances
    echo "📦 Installation des dépendances Python..."
    pip install -r requirements.txt -q
    
    echo "🚀 Démarrage: source venv/bin/activate && python main.py"
}

# Configuration C#
setup_csharp() {
    echo "🔷 Configuration C# .NET..."
    
    # Créer projet .NET
    dotnet new webapi -n CrudApp --force
    cd CrudApp
    
    # Ajouter packages
    echo "📦 Installation des packages NuGet..."
    dotnet add package Microsoft.EntityFrameworkCore.InMemory -q
    dotnet add package Microsoft.EntityFrameworkCore.Tools -q
    
    # Build
    dotnet build -q
    
    echo "🚀 Démarrage: dotnet run"
}

# Configuration TypeScript
setup_typescript() {
    echo "🟦 Configuration TypeScript Node.js..."
    
    # Créer package.json
    cat > package.json << 'EOF'
{
  "name": "crud-app",
  "version": "1.0.0",
  "scripts": {
    "start": "node dist/index.js",
    "dev": "ts-node src/index.ts",
    "build": "tsc"
  },
  "dependencies": {
    "express": "^4.18.2",
    "cors": "^2.8.5"
  },
  "devDependencies": {
    "@types/express": "^4.17.21",
    "@types/cors": "^2.8.17",
    "typescript": "^5.3.2",
    "ts-node": "^10.9.1"
  }
}
EOF
    
    # Créer tsconfig.json
    cat > tsconfig.json << 'EOF'
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "commonjs",
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true
  }
}
EOF
    
    # Créer index.ts
    mkdir -p src
    cat > src/index.ts << 'EOF'
import express from 'express';
import cors from 'cors';

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
  res.json({ message: 'CRUD API is running' });
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
EOF
    
    # Installer dépendances
    echo "📦 Installation des dépendances npm..."
    npm install -q
    
    echo "🚀 Démarrage: npm run dev"
}

# Configuration PHP
setup_php() {
    echo "🐘 Configuration PHP Laravel..."
    
    # Créer composer.json
    cat > composer.json << 'EOF'
{
    "require": {
        "slim/slim": "^4.12",
        "slim/psr7": "^1.6",
        "php-di/php-di": "^7.0"
    },
    "autoload": {
        "psr-4": {
            "App\\": "src/"
        }
    }
}
EOF
    
    # Créer index.php
    cat > index.php << 'EOF'
<?php
require 'vendor/autoload.php';

use Slim\Factory\AppFactory;

$app = AppFactory::create();

$app->get('/', function ($request, $response) {
    $response->getBody()->write('{"message": "CRUD API is running"}');
    return $response->withHeader('Content-Type', 'application/json');
});

$app->run();
EOF
    
    # Installer dépendances
    echo "📦 Installation des dépendances Composer..."
    composer install -q
    
    echo "🚀 Démarrage: php -S localhost:8080 index.php"
}

# Créer script de démarrage et configuration
create_start_script() {
    case $LANGUAGE in
        java)
            echo "mvn spring-boot:run" > start.sh
            ;;
        python)
            echo "source venv/bin/activate && python main.py" > start.sh
            ;;
        csharp)
            echo "cd CrudApp && dotnet run" > start.sh
            ;;
        typescript)
            echo "npm run dev" > start.sh
            ;;
        php)
            echo "php -S localhost:8080 index.php" > start.sh
            ;;
    esac
    chmod +x start.sh
    
    # Créer configuration du projet
    cat > .project-config << EOF
PROJECT_NAME=$PROJECT_NAME
LANGUAGE=$LANGUAGE
PACKAGE_NAME=com.example.$PROJECT_NAME
CREATED_DATE=$(date)
EOF
    
    # Copier les scripts de mise à jour
    cp ../update-project.sh . 2>/dev/null || echo "⚠️  Script update-project.sh non trouvé"
    cp ../update-project.bat . 2>/dev/null || echo "⚠️  Script update-project.bat non trouvé"
    chmod +x update-project.sh 2>/dev/null || true
}

# Nettoyage
cleanup() {
    if [ -f "../api.pid" ]; then
        kill $(cat ../api.pid) 2>/dev/null || true
        rm ../api.pid
    fi
}

# Exécution principale
main() {
    check_prerequisites
    start_api
    generate_project
    setup_project
    create_start_script
    
    echo ""
    echo "✅ Projet $PROJECT_NAME configuré avec succès!"
    echo "📁 Dossier: $PROJECT_NAME"
    echo "🚀 Démarrage: cd $PROJECT_NAME && ./start.sh"
    echo ""
    
    cleanup
}

# Gestion des signaux
trap cleanup EXIT

# Aide
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "Usage: $0 <language> [project-name]"
    echo "Languages: java, python, csharp, typescript, php"
    echo "Example: $0 python my-api"
    exit 0
fi

main
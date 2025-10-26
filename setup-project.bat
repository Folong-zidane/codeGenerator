@echo off
REM 🚀 Script d'Installation et Configuration Multi-Langages (Windows)
REM Usage: setup-project.bat <language> <project-name>

setlocal enabledelayedexpansion

set LANGUAGE=%1
set PROJECT_NAME=%2
set API_URL=http://localhost:8080

if "%LANGUAGE%"=="" set LANGUAGE=java
if "%PROJECT_NAME%"=="" set PROJECT_NAME=my-crud-app

echo 🚀 Configuration Projet CRUD - %LANGUAGE%
echo ========================================

REM Vérifier les prérequis
call :check_prerequisites
if errorlevel 1 exit /b 1

REM Démarrer l'API
call :start_api
if errorlevel 1 exit /b 1

REM Générer le projet
call :generate_project
if errorlevel 1 exit /b 1

REM Configurer le projet
call :setup_project
if errorlevel 1 exit /b 1

REM Créer script de démarrage
call :create_start_script

echo.
echo ✅ Projet %PROJECT_NAME% configuré avec succès!
echo 📁 Dossier: %PROJECT_NAME%
echo 🚀 Démarrage: cd %PROJECT_NAME% ^&^& start.bat
echo.

goto :eof

:check_prerequisites
echo 🔍 Vérification des prérequis...

if "%LANGUAGE%"=="java" (
    java -version >nul 2>&1 || (echo ❌ Java requis & exit /b 1)
    mvn -version >nul 2>&1 || (echo ❌ Maven requis & exit /b 1)
)

if "%LANGUAGE%"=="python" (
    python --version >nul 2>&1 || (echo ❌ Python requis & exit /b 1)
    pip --version >nul 2>&1 || (echo ❌ pip requis & exit /b 1)
)

if "%LANGUAGE%"=="csharp" (
    dotnet --version >nul 2>&1 || (echo ❌ .NET SDK requis & exit /b 1)
)

if "%LANGUAGE%"=="typescript" (
    node --version >nul 2>&1 || (echo ❌ Node.js requis & exit /b 1)
    npm --version >nul 2>&1 || (echo ❌ npm requis & exit /b 1)
)

if "%LANGUAGE%"=="php" (
    php --version >nul 2>&1 || (echo ❌ PHP requis & exit /b 1)
    composer --version >nul 2>&1 || (echo ❌ Composer requis & exit /b 1)
)

echo ✅ Prérequis OK
goto :eof

:start_api
curl -s "%API_URL%/actuator/health" >nul 2>&1
if errorlevel 1 (
    echo 🚀 Démarrage de l'API...
    start /b mvn spring-boot:run > api.log 2>&1
    
    REM Attendre que l'API soit prête
    for /l %%i in (1,1,30) do (
        timeout /t 2 >nul
        curl -s "%API_URL%/actuator/health" >nul 2>&1
        if not errorlevel 1 (
            echo ✅ API démarrée
            goto :eof
        )
    )
    echo ❌ Échec du démarrage de l'API
    exit /b 1
) else (
    echo ✅ API déjà active
)
goto :eof

:generate_project
echo 📦 Génération du projet %LANGUAGE%...

REM Créer la requête JSON
(
echo {
echo     "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +validateEmail()\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n    }\n    User \"1\" --> \"*\" Product",
echo     "language": "%LANGUAGE%",
echo     "packageName": "com.example.%PROJECT_NAME%"
echo }
) > temp-request.json

REM Générer le code
curl -s -X POST "%API_URL%/api/generate/crud" -H "Content-Type: application/json" -d @temp-request.json -o "%PROJECT_NAME%-%LANGUAGE%.zip"

del temp-request.json

if not exist "%PROJECT_NAME%-%LANGUAGE%.zip" (
    echo ❌ Échec de la génération
    exit /b 1
)

echo ✅ Code généré: %PROJECT_NAME%-%LANGUAGE%.zip
goto :eof

:setup_project
echo 📁 Configuration du projet...

mkdir "%PROJECT_NAME%" 2>nul
powershell -command "Expand-Archive -Path '%PROJECT_NAME%-%LANGUAGE%.zip' -DestinationPath '%PROJECT_NAME%' -Force"
cd "%PROJECT_NAME%"

if "%LANGUAGE%"=="java" call :setup_java
if "%LANGUAGE%"=="python" call :setup_python
if "%LANGUAGE%"=="csharp" call :setup_csharp
if "%LANGUAGE%"=="typescript" call :setup_typescript
if "%LANGUAGE%"=="php" call :setup_php

goto :eof

:setup_java
echo ☕ Configuration Java Spring Boot...

if not exist "pom.xml" (
    (
    echo ^<?xml version="1.0" encoding="UTF-8"?^>
    echo ^<project xmlns="http://maven.apache.org/POM/4.0.0"^>
    echo     ^<modelVersion^>4.0.0^</modelVersion^>
    echo     ^<parent^>
    echo         ^<groupId^>org.springframework.boot^</groupId^>
    echo         ^<artifactId^>spring-boot-starter-parent^</artifactId^>
    echo         ^<version^>3.2.0^</version^>
    echo     ^</parent^>
    echo     ^<groupId^>com.example^</groupId^>
    echo     ^<artifactId^>crud-app^</artifactId^>
    echo     ^<version^>1.0.0^</version^>
    echo     ^<dependencies^>
    echo         ^<dependency^>
    echo             ^<groupId^>org.springframework.boot^</groupId^>
    echo             ^<artifactId^>spring-boot-starter-web^</artifactId^>
    echo         ^</dependency^>
    echo         ^<dependency^>
    echo             ^<groupId^>org.springframework.boot^</groupId^>
    echo             ^<artifactId^>spring-boot-starter-data-jpa^</artifactId^>
    echo         ^</dependency^>
    echo         ^<dependency^>
    echo             ^<groupId^>com.h2database^</groupId^>
    echo             ^<artifactId^>h2^</artifactId^>
    echo             ^<scope^>runtime^</scope^>
    echo         ^</dependency^>
    echo     ^</dependencies^>
    echo ^</project^>
    ) > pom.xml
)

mkdir "src\main\java\com\example" 2>nul
(
echo package com.example;
echo.
echo import org.springframework.boot.SpringApplication;
echo import org.springframework.boot.autoconfigure.SpringBootApplication;
echo.
echo @SpringBootApplication
echo public class Application {
echo     public static void main(String[] args) {
echo         SpringApplication.run(Application.class, args);
echo     }
echo }
) > "src\main\java\com\example\Application.java"

echo 📦 Installation des dépendances Maven...
mvn clean compile -q

echo 🚀 Démarrage: mvn spring-boot:run
goto :eof

:setup_python
echo 🐍 Configuration Python FastAPI...

(
echo fastapi==0.104.1
echo uvicorn==0.24.0
echo sqlalchemy==2.0.23
echo pydantic==2.5.0
echo python-multipart==0.0.6
) > requirements.txt

(
echo from fastapi import FastAPI
echo from fastapi.middleware.cors import CORSMiddleware
echo.
echo app = FastAPI(title="Generated CRUD API"^)
echo.
echo app.add_middleware(
echo     CORSMiddleware,
echo     allow_origins=["*"],
echo     allow_methods=["*"],
echo     allow_headers=["*"],
echo ^)
echo.
echo @app.get("/"^)
echo def read_root(^):
echo     return {"message": "CRUD API is running"}
echo.
echo if __name__ == "__main__":
echo     import uvicorn
echo     uvicorn.run(app, host="0.0.0.0", port=8000^)
) > main.py

python -m venv venv
call venv\Scripts\activate.bat

echo 📦 Installation des dépendances Python...
pip install -r requirements.txt -q

echo 🚀 Démarrage: venv\Scripts\activate.bat ^&^& python main.py
goto :eof

:setup_csharp
echo 🔷 Configuration C# .NET...

dotnet new webapi -n CrudApp --force
cd CrudApp

echo 📦 Installation des packages NuGet...
dotnet add package Microsoft.EntityFrameworkCore.InMemory -q
dotnet add package Microsoft.EntityFrameworkCore.Tools -q

dotnet build -q

echo 🚀 Démarrage: dotnet run
goto :eof

:setup_typescript
echo 🟦 Configuration TypeScript Node.js...

(
echo {
echo   "name": "crud-app",
echo   "version": "1.0.0",
echo   "scripts": {
echo     "start": "node dist/index.js",
echo     "dev": "ts-node src/index.ts",
echo     "build": "tsc"
echo   },
echo   "dependencies": {
echo     "express": "^4.18.2",
echo     "cors": "^2.8.5"
echo   },
echo   "devDependencies": {
echo     "@types/express": "^4.17.21",
echo     "@types/cors": "^2.8.17",
echo     "typescript": "^5.3.2",
echo     "ts-node": "^10.9.1"
echo   }
echo }
) > package.json

(
echo {
echo   "compilerOptions": {
echo     "target": "ES2020",
echo     "module": "commonjs",
echo     "outDir": "./dist",
echo     "rootDir": "./src",
echo     "strict": true,
echo     "esModuleInterop": true
echo   }
echo }
) > tsconfig.json

mkdir src 2>nul
(
echo import express from 'express';
echo import cors from 'cors';
echo.
echo const app = express(^);
echo const port = 3000;
echo.
echo app.use(cors(^)^);
echo app.use(express.json(^)^);
echo.
echo app.get('/', (req, res^) =^> {
echo   res.json({ message: 'CRUD API is running' }^);
echo }^);
echo.
echo app.listen(port, (^) =^> {
echo   console.log(`Server running at http://localhost:${port}`^);
echo }^);
) > src\index.ts

echo 📦 Installation des dépendances npm...
npm install -q

echo 🚀 Démarrage: npm run dev
goto :eof

:setup_php
echo 🐘 Configuration PHP...

(
echo {
echo     "require": {
echo         "slim/slim": "^4.12",
echo         "slim/psr7": "^1.6",
echo         "php-di/php-di": "^7.0"
echo     },
echo     "autoload": {
echo         "psr-4": {
echo             "App\\": "src/"
echo         }
echo     }
echo }
) > composer.json

(
echo ^<?php
echo require 'vendor/autoload.php';
echo.
echo use Slim\Factory\AppFactory;
echo.
echo $app = AppFactory::create(^);
echo.
echo $app-^>get('/', function ($request, $response^) {
echo     $response-^>getBody(^)-^>write('{"message": "CRUD API is running"}'^);
echo     return $response-^>withHeader('Content-Type', 'application/json'^);
echo }^);
echo.
echo $app-^>run(^);
) > index.php

echo 📦 Installation des dépendances Composer...
composer install -q

echo 🚀 Démarrage: php -S localhost:8080 index.php
goto :eof

:create_start_script
if "%LANGUAGE%"=="java" echo mvn spring-boot:run > start.bat
if "%LANGUAGE%"=="python" echo call venv\Scripts\activate.bat ^&^& python main.py > start.bat
if "%LANGUAGE%"=="csharp" echo cd CrudApp ^&^& dotnet run > start.bat
if "%LANGUAGE%"=="typescript" echo npm run dev > start.bat
if "%LANGUAGE%"=="php" echo php -S localhost:8080 index.php > start.bat
goto :eof
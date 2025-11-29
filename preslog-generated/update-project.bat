@echo off
setlocal enabledelayedexpansion

set MERMAID_FILE=%1
set API_URL=https://codegenerator-cpyh.onrender.com
set PROJECT_CONFIG=.project-config

if "%MERMAID_FILE%"=="" set MERMAID_FILE=model.mermaid

echo 🔄 Mise à Jour Continue du Projet

if not exist "%PROJECT_CONFIG%" (
    echo ❌ Pas un projet généré
    exit /b 1
)

for /f "tokens=1,2 delims==" %%a in (%PROJECT_CONFIG%) do (
    if "%%a"=="PROJECT_NAME" set PROJECT_NAME=%%b
    if "%%a"=="LANGUAGE" set LANGUAGE=%%b
    if "%%a"=="PACKAGE_NAME" set PACKAGE_NAME=%%b
)

echo 📋 Projet: %PROJECT_NAME% (%LANGUAGE%)

if not exist "%MERMAID_FILE%" (
    echo ❌ Fichier Mermaid introuvable: %MERMAID_FILE%
    exit /b 1
)

echo 🔄 Génération du code mis à jour...
(
echo {
echo     "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
echo     "language": "%LANGUAGE%",
echo     "packageName": "%PACKAGE_NAME%"
echo }
) > temp-request.json

curl -s -X POST "%API_URL%/api/generate/crud" -H "Content-Type: application/json" -d @temp-request.json -o "update.zip"
del temp-request.json

if exist "update.zip" (
    mkdir temp-update 2>nul
    powershell -command "Expand-Archive -Path 'update.zip' -DestinationPath 'temp-update' -Force"
    xcopy "temp-update\*" "." /E /Y /Q >nul 2>&1
    rmdir /s /q temp-update 2>nul
    del "update.zip" 2>nul
    echo ✅ Mise à jour terminée
) else (
    echo ❌ Échec de la génération
)

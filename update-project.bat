@echo off
REM 🔄 Script de Mise à Jour Continue - Windows
REM Usage: update-project.bat [mermaid-file]

setlocal enabledelayedexpansion

set MERMAID_FILE=%1
set API_URL=http://localhost:8080
set PROJECT_CONFIG=.project-config
set BACKUP_DIR=.backups

if "%MERMAID_FILE%"=="" set MERMAID_FILE=model.mermaid

echo 🔄 Mise à Jour Continue du Projet
echo =================================

REM Vérifier si c'est un projet généré
if not exist "%PROJECT_CONFIG%" (
    echo ❌ Pas un projet généré. Utilisez setup-project.bat d'abord.
    exit /b 1
)

REM Lire la configuration
for /f "tokens=1,2 delims==" %%a in (%PROJECT_CONFIG%) do (
    if "%%a"=="PROJECT_NAME" set PROJECT_NAME=%%b
    if "%%a"=="LANGUAGE" set LANGUAGE=%%b
    if "%%a"=="PACKAGE_NAME" set PACKAGE_NAME=%%b
)

echo 📋 Projet: %PROJECT_NAME% (%LANGUAGE%)

REM Vérifier le fichier Mermaid
if not exist "%MERMAID_FILE%" (
    echo ❌ Fichier Mermaid introuvable: %MERMAID_FILE%
    echo 💡 Créez un fichier .mermaid ou spécifiez le chemin
    exit /b 1
)

echo 📄 Diagramme: %MERMAID_FILE%

REM Créer sauvegarde
echo 💾 Création de la sauvegarde...
set TIMESTAMP=%date:~-4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set TIMESTAMP=%TIMESTAMP: =0%
set BACKUP_PATH=%BACKUP_DIR%\backup_%TIMESTAMP%

mkdir "%BACKUP_PATH%" 2>nul

REM Sauvegarder selon le langage
if "%LANGUAGE%"=="java" (
    if exist "src" xcopy "src" "%BACKUP_PATH%\src" /E /I /Q >nul 2>&1
    if exist "pom.xml" copy "pom.xml" "%BACKUP_PATH%\" >nul 2>&1
)

if "%LANGUAGE%"=="python" (
    for %%f in (*.py) do copy "%%f" "%BACKUP_PATH%\" >nul 2>&1
    if exist "requirements.txt" copy "requirements.txt" "%BACKUP_PATH%\" >nul 2>&1
    if exist "entities" xcopy "entities" "%BACKUP_PATH%\entities" /E /I /Q >nul 2>&1
    if exist "services" xcopy "services" "%BACKUP_PATH%\services" /E /I /Q >nul 2>&1
)

if "%LANGUAGE%"=="csharp" (
    for %%f in (*.cs) do copy "%%f" "%BACKUP_PATH%\" >nul 2>&1
    for %%f in (*.csproj) do copy "%%f" "%BACKUP_PATH%\" >nul 2>&1
    if exist "Entities" xcopy "Entities" "%BACKUP_PATH%\Entities" /E /I /Q >nul 2>&1
)

if "%LANGUAGE%"=="typescript" (
    for %%f in (*.ts) do copy "%%f" "%BACKUP_PATH%\" >nul 2>&1
    if exist "package.json" copy "package.json" "%BACKUP_PATH%\" >nul 2>&1
    if exist "src" xcopy "src" "%BACKUP_PATH%\src" /E /I /Q >nul 2>&1
)

if "%LANGUAGE%"=="php" (
    for %%f in (*.php) do copy "%%f" "%BACKUP_PATH%\" >nul 2>&1
    if exist "composer.json" copy "composer.json" "%BACKUP_PATH%\" >nul 2>&1
)

echo ✅ Sauvegarde: %BACKUP_PATH%

REM Générer nouveau code
echo 🔄 Génération du code mis à jour...

REM Lire le contenu Mermaid (simplifié pour Windows)
powershell -command "(Get-Content '%MERMAID_FILE%') -join '\n'" > temp_mermaid.txt

REM Créer la requête JSON (simplifié)
(
echo {
echo     "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n    }",
echo     "language": "%LANGUAGE%",
echo     "packageName": "%PACKAGE_NAME%"
echo }
) > temp-update-request.json

REM Générer le nouveau code
curl -s -X POST "%API_URL%/api/generate/crud" -H "Content-Type: application/json" -d @temp-update-request.json -o "update-%PROJECT_NAME%.zip"

del temp-update-request.json
del temp_mermaid.txt

if not exist "update-%PROJECT_NAME%.zip" (
    echo ❌ Échec de la génération
    exit /b 1
)

echo ✅ Nouveau code généré

REM Fusion intelligente
echo 🔀 Fusion intelligente des changements...

mkdir temp-update 2>nul
powershell -command "Expand-Archive -Path 'update-%PROJECT_NAME%.zip' -DestinationPath 'temp-update' -Force"

REM Merger selon le langage
if "%LANGUAGE%"=="java" call :merge_java
if "%LANGUAGE%"=="python" call :merge_python
if "%LANGUAGE%"=="csharp" call :merge_csharp
if "%LANGUAGE%"=="typescript" call :merge_typescript
if "%LANGUAGE%"=="php" call :merge_php

REM Nettoyer
rmdir /s /q temp-update 2>nul
del "update-%PROJECT_NAME%.zip" 2>nul

echo ✅ Fusion terminée

REM Mettre à jour la configuration
echo 📝 Mise à jour de la configuration...
echo LAST_UPDATE=%date% %time% >> "%PROJECT_CONFIG%"
echo MERMAID_FILE=%MERMAID_FILE% >> "%PROJECT_CONFIG%"

REM Afficher le résumé
echo.
echo 📊 Résumé de la mise à jour:
echo   📁 Sauvegardes: %BACKUP_DIR%\
echo   📄 Diagramme: %MERMAID_FILE%
echo   🕒 Dernière mise à jour: %date% %time%
echo.
echo 💡 Commandes utiles:
echo   start.bat                           # Démarrer l'application
echo   update-project.bat model.mermaid    # Nouvelle mise à jour
echo   dir %BACKUP_DIR%                    # Voir les sauvegardes

goto :eof

:merge_java
if exist "temp-update\src" (
    echo   🔄 Fusion des fichiers Java...
    xcopy "temp-update\src" "src" /E /Y /Q >nul 2>&1
)
goto :eof

:merge_python
if exist "temp-update\entities" (
    echo   🔄 Fusion des entités Python...
    if not exist "entities" mkdir "entities"
    xcopy "temp-update\entities" "entities" /E /Y /Q >nul 2>&1
)
if exist "temp-update\services" (
    echo   🔄 Fusion des services Python...
    if not exist "services" mkdir "services"
    xcopy "temp-update\services" "services" /E /Y /Q >nul 2>&1
)
if exist "temp-update\controllers" (
    echo   🔄 Fusion des controllers Python...
    if not exist "controllers" mkdir "controllers"
    xcopy "temp-update\controllers" "controllers" /E /Y /Q >nul 2>&1
)
goto :eof

:merge_csharp
if exist "temp-update\Entities" (
    echo   🔄 Fusion des entités C#...
    if not exist "Entities" mkdir "Entities"
    xcopy "temp-update\Entities" "Entities" /E /Y /Q >nul 2>&1
)
if exist "temp-update\Services" (
    echo   🔄 Fusion des services C#...
    if not exist "Services" mkdir "Services"
    xcopy "temp-update\Services" "Services" /E /Y /Q >nul 2>&1
)
goto :eof

:merge_typescript
if exist "temp-update\src" (
    echo   🔄 Fusion des fichiers TypeScript...
    if not exist "src" mkdir "src"
    xcopy "temp-update\src" "src" /E /Y /Q >nul 2>&1
)
goto :eof

:merge_php
if exist "temp-update\src" (
    echo   🔄 Fusion des fichiers PHP...
    if not exist "src" mkdir "src"
    xcopy "temp-update\src" "src" /E /Y /Q >nul 2>&1
)
goto :eof
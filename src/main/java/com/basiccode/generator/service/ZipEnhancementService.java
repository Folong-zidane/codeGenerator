package com.basiccode.generator.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ZipEnhancementService {
    
    public void addDevelopmentScripts(Path projectDir, String umlContent, String packageName, String language) throws IOException {
        String projectName = extractProjectName(packageName);
        String createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // Scripts de mise à jour
        Files.writeString(projectDir.resolve("update-project.sh"), generateUpdateScript("sh", language, packageName));
        Files.writeString(projectDir.resolve("update-project.bat"), generateUpdateScript("bat", language, packageName));
        
        // Scripts de démarrage
        Files.writeString(projectDir.resolve("start.sh"), generateStartScript("sh", language));
        Files.writeString(projectDir.resolve("start.bat"), generateStartScript("bat", language));
        
        // Configuration du projet
        Files.writeString(projectDir.resolve(".project-config"), 
            generateProjectConfig(projectName, language, packageName, createdDate));
        
        // README personnalisé
        Files.writeString(projectDir.resolve("README.md"), 
            generateReadme(projectName, language, packageName, createdDate));
        
        // Dossier de sauvegardes
        Files.createDirectories(projectDir.resolve(".backups"));
        
        // Exemple de diagramme
        Files.writeString(projectDir.resolve("model.mermaid"), generateExampleMermaid());
    }
    
    private String generateUpdateScript(String type, String language, String packageName) {
        if ("sh".equals(type)) {
            return """
                #!/bin/bash
                set -e
                
                MERMAID_FILE=${1:-"model.mermaid"}
                API_URL="https://codegenerator-cpyh.onrender.com"
                PROJECT_CONFIG=".project-config"
                BACKUP_DIR=".backups"
                
                echo "🔄 Mise à Jour Continue du Projet"
                
                if [ ! -f "$PROJECT_CONFIG" ]; then
                    echo "❌ Pas un projet généré"
                    exit 1
                fi
                
                source "$PROJECT_CONFIG"
                echo "📋 Projet: $PROJECT_NAME ($LANGUAGE)"
                
                if [ ! -f "$MERMAID_FILE" ]; then
                    echo "❌ Fichier Mermaid introuvable: $MERMAID_FILE"
                    exit 1
                fi
                
                # Sauvegarde
                TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
                BACKUP_PATH="$BACKUP_DIR/backup_$TIMESTAMP"
                mkdir -p "$BACKUP_PATH"
                
                find . -name "*.java" -o -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" | \\
                    grep -v ".backups" | \\
                    xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
                
                echo "✅ Sauvegarde: $BACKUP_PATH"
                
                # Génération
                MERMAID_CONTENT=$(cat "$MERMAID_FILE" | sed 's/"/\\\\"/g' | tr '\\n' '\\\\n')
                
                cat > temp-request.json << EOF
                {
                    "umlContent": "$MERMAID_CONTENT",
                    "language": "$LANGUAGE",
                    "packageName": "$PACKAGE_NAME"
                }
                EOF
                
                curl -s -X POST "$API_URL/api/generate/crud" \\
                    -H "Content-Type: application/json" \\
                    -d @temp-request.json \\
                    -o "update.zip"
                
                rm temp-request.json
                
                if [ ! -f "update.zip" ] || [ ! -s "update.zip" ]; then
                    echo "❌ Échec de la génération"
                    exit 1
                fi
                
                # Fusion intelligente
                mkdir -p temp-update
                unzip -q "update.zip" -d temp-update
                
                find temp-update -type f \\( -name "*.java" -o -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" \\) | while read file; do
                    relative_path=${file#temp-update/}
                    target_file="$relative_path"
                    
                    if [[ "$file" == *"Entity"* ]] || [[ "$file" == *"entity"* ]]; then
                        if [ ! -f "$target_file" ]; then
                            mkdir -p "$(dirname "$target_file")"
                            cp "$file" "$target_file"
                            echo "  ➕ Nouvelle entité: $target_file"
                        fi
                    else
                        mkdir -p "$(dirname "$target_file")"
                        cp "$file" "$target_file"
                        echo "  🔄 Mis à jour: $target_file"
                    fi
                done
                
                rm -rf temp-update update.zip
                echo "LAST_UPDATE=$(date)" >> "$PROJECT_CONFIG"
                echo "✅ Mise à jour terminée"
                """;
        } else {
            return """
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
                echo     "umlContent": "classDiagram\\n    class User {\\n        +UUID id\\n        +String username\\n    }",
                echo     "language": "%LANGUAGE%",
                echo     "packageName": "%PACKAGE_NAME%"
                echo }
                ) > temp-request.json
                
                curl -s -X POST "%API_URL%/api/generate/crud" -H "Content-Type: application/json" -d @temp-request.json -o "update.zip"
                del temp-request.json
                
                if exist "update.zip" (
                    mkdir temp-update 2>nul
                    powershell -command "Expand-Archive -Path 'update.zip' -DestinationPath 'temp-update' -Force"
                    xcopy "temp-update\\*" "." /E /Y /Q >nul 2>&1
                    rmdir /s /q temp-update 2>nul
                    del "update.zip" 2>nul
                    echo ✅ Mise à jour terminée
                ) else (
                    echo ❌ Échec de la génération
                )
                """;
        }
    }
    
    private String generateStartScript(String type, String language) {
        if ("sh".equals(type)) {
            return switch (language.toLowerCase()) {
                case "java" -> "#!/bin/bash\necho \"🚀 Démarrage Java...\"\nmvn spring-boot:run\n";
                case "python" -> "#!/bin/bash\necho \"🚀 Démarrage Python...\"\npython3 -m venv venv 2>/dev/null || true\nsource venv/bin/activate\npip install -r requirements.txt -q\npython main.py\n";
                case "django" -> "#!/bin/bash\necho \"🚀 Démarrage Django...\"\npython3 -m venv venv 2>/dev/null || true\nsource venv/bin/activate\npip install -r requirements.txt -q\npython manage.py migrate\npython manage.py runserver\n";
                case "csharp" -> "#!/bin/bash\necho \"🚀 Démarrage C#...\"\ndotnet run\n";
                case "typescript" -> "#!/bin/bash\necho \"🚀 Démarrage TypeScript...\"\nnpm install -q\nnpm run dev\n";
                case "php" -> "#!/bin/bash\necho \"🚀 Démarrage PHP...\"\ncomposer install -q\nphp -S localhost:8080 index.php\n";
                default -> "#!/bin/bash\necho \"🚀 Démarrage...\"\n";
            };
        } else {
            return switch (language.toLowerCase()) {
                case "java" -> "@echo off\necho 🚀 Démarrage Java...\nmvn spring-boot:run\n";
                case "python" -> "@echo off\necho 🚀 Démarrage Python...\nif not exist venv python -m venv venv\ncall venv\\Scripts\\activate.bat\npip install -r requirements.txt -q\npython main.py\n";
                case "django" -> "@echo off\necho 🚀 Démarrage Django...\nif not exist venv python -m venv venv\ncall venv\\Scripts\\activate.bat\npip install -r requirements.txt -q\npython manage.py migrate\npython manage.py runserver\n";
                case "csharp" -> "@echo off\necho 🚀 Démarrage C#...\ndotnet run\n";
                case "typescript" -> "@echo off\necho 🚀 Démarrage TypeScript...\nnpm install -q\nnpm run dev\n";
                case "php" -> "@echo off\necho 🚀 Démarrage PHP...\ncomposer install -q\nphp -S localhost:8080 index.php\n";
                default -> "@echo off\necho 🚀 Démarrage...\n";
            };
        }
    }
    
    private String generateProjectConfig(String projectName, String language, String packageName, String createdDate) {
        return String.format("PROJECT_NAME=%s\nLANGUAGE=%s\nPACKAGE_NAME=%s\nCREATED_DATE=%s\n", 
            projectName, language, packageName, createdDate);
    }
    
    private String generateReadme(String projectName, String language, String packageName, String createdDate) {
        return String.format("""
            # 🚀 Projet %s - %s
            
            **Généré le**: %s  
            **Package**: %s
            
            ## 🚀 Démarrage
            ```bash
            ./start.sh    # Linux/macOS
            start.bat     # Windows
            ```
            
            ## 🔄 Mise à jour
            ```bash
            ./update-project.sh model.mermaid
            ```
            
            ## 📁 Structure
            - `src/` - Code généré
            - `.backups/` - Sauvegardes automatiques
            - `model.mermaid` - Diagramme UML
            - `update-project.*` - Scripts de mise à jour
            
            ## 🛠️ Personnalisation
            Votre code sera préservé lors des mises à jour !
            
            ## 📚 Commandes
            - `./start.sh` - Démarrer l'application
            - `./update-project.sh new-model.mermaid` - Mettre à jour
            - `ls .backups/` - Voir les sauvegardes
            """, projectName, language, createdDate, packageName);
    }
    
    private String generateExampleMermaid() {
        return """
            classDiagram
                class User {
                    +UUID id
                    +String username
                    +String email
                    +Boolean active
                    +validateEmail()
                }
                
                class Product {
                    +UUID id
                    +String name
                    +Float price
                    +Integer stock
                    +calculateDiscount()
                }
                
                class Order {
                    +UUID id
                    +UUID userId
                    +Float totalAmount
                    +String status
                    +processOrder()
                }
                
                User "1" --> "*" Order
                Order "*" --> "*" Product
            """;
    }
    
    private String extractProjectName(String packageName) {
        if (packageName == null || packageName.isEmpty()) return "crud-app";
        String[] parts = packageName.split("\\.");
        return parts[parts.length - 1];
    }
}
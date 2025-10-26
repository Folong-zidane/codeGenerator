#!/bin/bash

# 📦 Script pour Intégrer les Scripts dans le ZIP Généré
# À intégrer dans le générateur Java

SCRIPTS_DIR="scripts"
README_TEMPLATE="README-PROJECT.md"

# Créer le dossier des scripts à inclure
create_embedded_scripts() {
    local project_name=$1
    local language=$2
    local package_name=$3
    local created_date=$(date)
    
    mkdir -p "$SCRIPTS_DIR"
    
    # Copier les scripts de mise à jour
    cp update-project.sh "$SCRIPTS_DIR/"
    cp update-project.bat "$SCRIPTS_DIR/"
    
    # Créer le README personnalisé
    sed -e "s/{{PROJECT_NAME}}/$project_name/g" \
        -e "s/{{LANGUAGE}}/$language/g" \
        -e "s/{{PACKAGE_NAME}}/$package_name/g" \
        -e "s/{{CREATED_DATE}}/$created_date/g" \
        "$README_TEMPLATE" > "$SCRIPTS_DIR/README.md"
    
    # Créer la configuration du projet
    cat > "$SCRIPTS_DIR/.project-config" << EOF
PROJECT_NAME=$project_name
LANGUAGE=$language
PACKAGE_NAME=$package_name
CREATED_DATE=$created_date
EOF
    
    # Créer les scripts de démarrage selon le langage
    case $language in
        java)
            echo "mvn spring-boot:run" > "$SCRIPTS_DIR/start.sh"
            echo "mvn spring-boot:run" > "$SCRIPTS_DIR/start.bat"
            ;;
        python)
            echo "source venv/bin/activate && python main.py" > "$SCRIPTS_DIR/start.sh"
            echo "call venv\\Scripts\\activate.bat && python main.py" > "$SCRIPTS_DIR/start.bat"
            ;;
        csharp)
            echo "dotnet run" > "$SCRIPTS_DIR/start.sh"
            echo "dotnet run" > "$SCRIPTS_DIR/start.bat"
            ;;
        typescript)
            echo "npm run dev" > "$SCRIPTS_DIR/start.sh"
            echo "npm run dev" > "$SCRIPTS_DIR/start.bat"
            ;;
        php)
            echo "php -S localhost:8080 index.php" > "$SCRIPTS_DIR/start.sh"
            echo "php -S localhost:8080 index.php" > "$SCRIPTS_DIR/start.bat"
            ;;
    esac
    
    chmod +x "$SCRIPTS_DIR/start.sh"
    chmod +x "$SCRIPTS_DIR/update-project.sh"
    
    echo "✅ Scripts préparés dans $SCRIPTS_DIR/"
}

# Exemple d'utilisation
create_embedded_scripts "my-crud-app" "java" "com.example.mycrudapp"
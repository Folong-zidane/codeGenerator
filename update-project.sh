#!/bin/bash

# 🔄 Script de Mise à Jour Continue - Développement Incrémental
# Usage: ./update-project.sh [mermaid-file]

set -e

MERMAID_FILE=${1:-"model.mermaid"}
API_URL="http://localhost:8080"
PROJECT_CONFIG=".project-config"
BACKUP_DIR=".backups"

echo "🔄 Mise à Jour Continue du Projet"
echo "================================="

# Vérifier si c'est un projet généré
check_project() {
    if [ ! -f "$PROJECT_CONFIG" ]; then
        echo "❌ Pas un projet généré. Utilisez setup-project.sh d'abord."
        exit 1
    fi
    
    source "$PROJECT_CONFIG"
    echo "📋 Projet: $PROJECT_NAME ($LANGUAGE)"
}

# Vérifier le fichier Mermaid
check_mermaid() {
    if [ ! -f "$MERMAID_FILE" ]; then
        echo "❌ Fichier Mermaid introuvable: $MERMAID_FILE"
        echo "💡 Créez un fichier .mermaid ou spécifiez le chemin"
        exit 1
    fi
    echo "📄 Diagramme: $MERMAID_FILE"
}

# Créer sauvegarde
create_backup() {
    echo "💾 Création de la sauvegarde..."
    
    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    BACKUP_PATH="$BACKUP_DIR/backup_$TIMESTAMP"
    
    mkdir -p "$BACKUP_PATH"
    
    # Sauvegarder les fichiers modifiés par l'utilisateur
    case $LANGUAGE in
        java)
            find src -name "*.java" -newer "$PROJECT_CONFIG" 2>/dev/null | xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
            [ -f "pom.xml" ] && cp pom.xml "$BACKUP_PATH/"
            ;;
        python)
            find . -name "*.py" -newer "$PROJECT_CONFIG" 2>/dev/null | xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
            [ -f "requirements.txt" ] && cp requirements.txt "$BACKUP_PATH/"
            [ -f "main.py" ] && cp main.py "$BACKUP_PATH/"
            ;;
        csharp)
            find . -name "*.cs" -newer "$PROJECT_CONFIG" 2>/dev/null | xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
            [ -f "*.csproj" ] && cp *.csproj "$BACKUP_PATH/" 2>/dev/null || true
            ;;
        typescript)
            find . -name "*.ts" -newer "$PROJECT_CONFIG" 2>/dev/null | xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
            [ -f "package.json" ] && cp package.json "$BACKUP_PATH/"
            [ -f "tsconfig.json" ] && cp tsconfig.json "$BACKUP_PATH/"
            ;;
        php)
            find . -name "*.php" -newer "$PROJECT_CONFIG" 2>/dev/null | xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true
            [ -f "composer.json" ] && cp composer.json "$BACKUP_PATH/"
            ;;
    esac
    
    echo "✅ Sauvegarde: $BACKUP_PATH"
}

# Générer nouveau code
generate_update() {
    echo "🔄 Génération du code mis à jour..."
    
    # Lire le contenu Mermaid
    MERMAID_CONTENT=$(cat "$MERMAID_FILE" | sed 's/"/\\"/g' | tr '\n' '\\n')
    
    # Créer la requête JSON
    cat > temp-update-request.json << EOF
{
    "umlContent": "$MERMAID_CONTENT",
    "language": "$LANGUAGE",
    "packageName": "$PACKAGE_NAME"
}
EOF

    # Générer le nouveau code
    curl -s -X POST "$API_URL/api/generate/crud" \
        -H "Content-Type: application/json" \
        -d @temp-update-request.json \
        -o "update-$PROJECT_NAME.zip"
    
    rm temp-update-request.json
    
    if [ ! -f "update-$PROJECT_NAME.zip" ] || [ ! -s "update-$PROJECT_NAME.zip" ]; then
        echo "❌ Échec de la génération"
        exit 1
    fi
    
    echo "✅ Nouveau code généré"
}

# Merger intelligemment
smart_merge() {
    echo "🔀 Fusion intelligente des changements..."
    
    # Extraire le nouveau code dans un dossier temporaire
    mkdir -p temp-update
    unzip -q "update-$PROJECT_NAME.zip" -d temp-update
    
    # Merger selon le langage
    case $LANGUAGE in
        java)
            merge_java
            ;;
        python)
            merge_python
            ;;
        csharp)
            merge_csharp
            ;;
        typescript)
            merge_typescript
            ;;
        php)
            merge_php
            ;;
    esac
    
    # Nettoyer
    rm -rf temp-update
    rm "update-$PROJECT_NAME.zip"
    
    echo "✅ Fusion terminée"
}

# Fusion Java
merge_java() {
    # Merger les entités (nouvelles seulement)
    if [ -d "temp-update/src/main/java" ]; then
        find temp-update/src/main/java -name "*Entity.java" -o -name "*Model.java" | while read file; do
            relative_path=${file#temp-update/}
            target_file="$relative_path"
            
            if [ ! -f "$target_file" ]; then
                mkdir -p "$(dirname "$target_file")"
                cp "$file" "$target_file"
                echo "  ➕ Nouvelle entité: $target_file"
            fi
        done
        
        # Merger les repositories/services/controllers (mise à jour)
        find temp-update/src/main/java -name "*Repository.java" -o -name "*Service.java" -o -name "*Controller.java" | while read file; do
            relative_path=${file#temp-update/}
            target_file="$relative_path"
            
            mkdir -p "$(dirname "$target_file")"
            cp "$file" "$target_file"
            echo "  🔄 Mis à jour: $target_file"
        done
    fi
}

# Fusion Python
merge_python() {
    # Merger les entités
    if [ -d "temp-update/entities" ]; then
        find temp-update/entities -name "*.py" | while read file; do
            relative_path=${file#temp-update/}
            target_file="$relative_path"
            
            if [ ! -f "$target_file" ]; then
                mkdir -p "$(dirname "$target_file")"
                cp "$file" "$target_file"
                echo "  ➕ Nouvelle entité: $target_file"
            fi
        done
    fi
    
    # Merger repositories/services/controllers
    for dir in repositories services controllers; do
        if [ -d "temp-update/$dir" ]; then
            find "temp-update/$dir" -name "*.py" | while read file; do
                relative_path=${file#temp-update/}
                target_file="$relative_path"
                
                mkdir -p "$(dirname "$target_file")"
                cp "$file" "$target_file"
                echo "  🔄 Mis à jour: $target_file"
            done
        fi
    done
}

# Fusion C#
merge_csharp() {
    for dir in Entities Repositories Services Controllers; do
        if [ -d "temp-update/$dir" ]; then
            find "temp-update/$dir" -name "*.cs" | while read file; do
                relative_path=${file#temp-update/}
                target_file="$relative_path"
                
                if [[ "$dir" == "Entities" ]] && [ -f "$target_file" ]; then
                    echo "  ⚠️  Entité existante ignorée: $target_file"
                else
                    mkdir -p "$(dirname "$target_file")"
                    cp "$file" "$target_file"
                    echo "  🔄 Mis à jour: $target_file"
                fi
            done
        fi
    done
}

# Fusion TypeScript
merge_typescript() {
    for dir in entities repositories services controllers; do
        if [ -d "temp-update/$dir" ]; then
            find "temp-update/$dir" -name "*.ts" | while read file; do
                relative_path=${file#temp-update/}
                target_file="$relative_path"
                
                if [[ "$dir" == "entities" ]] && [ -f "$target_file" ]; then
                    echo "  ⚠️  Entité existante ignorée: $target_file"
                else
                    mkdir -p "$(dirname "$target_file")"
                    cp "$file" "$target_file"
                    echo "  🔄 Mis à jour: $target_file"
                fi
            done
        fi
    done
}

# Fusion PHP
merge_php() {
    for dir in entities repositories services controllers; do
        if [ -d "temp-update/$dir" ]; then
            find "temp-update/$dir" -name "*.php" | while read file; do
                relative_path=${file#temp-update/}
                target_file="$relative_path"
                
                if [[ "$dir" == "entities" ]] && [ -f "$target_file" ]; then
                    echo "  ⚠️  Entité existante ignorée: $target_file"
                else
                    mkdir -p "$(dirname "$target_file")"
                    cp "$file" "$target_file"
                    echo "  🔄 Mis à jour: $target_file"
                fi
            done
        fi
    done
}

# Mettre à jour la configuration
update_config() {
    echo "📝 Mise à jour de la configuration..."
    
    # Mettre à jour le timestamp
    echo "LAST_UPDATE=$(date)" >> "$PROJECT_CONFIG"
    echo "MERMAID_FILE=$MERMAID_FILE" >> "$PROJECT_CONFIG"
    
    touch "$PROJECT_CONFIG"  # Mettre à jour le timestamp du fichier
}

# Afficher le résumé
show_summary() {
    echo ""
    echo "📊 Résumé de la mise à jour:"
    echo "  📁 Sauvegardes: $BACKUP_DIR/"
    echo "  📄 Diagramme: $MERMAID_FILE"
    echo "  🕒 Dernière mise à jour: $(date)"
    echo ""
    echo "💡 Commandes utiles:"
    echo "  ./start.sh                    # Démarrer l'application"
    echo "  ./update-project.sh model.mermaid  # Nouvelle mise à jour"
    echo "  ls $BACKUP_DIR/               # Voir les sauvegardes"
}

# Exécution principale
main() {
    check_project
    check_mermaid
    create_backup
    generate_update
    smart_merge
    update_config
    show_summary
}

# Aide
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "Usage: $0 [mermaid-file]"
    echo ""
    echo "Mise à jour continue d'un projet généré:"
    echo "  - Sauvegarde automatique des modifications"
    echo "  - Génération du nouveau code"
    echo "  - Fusion intelligente sans perte"
    echo ""
    echo "Exemple: $0 updated-model.mermaid"
    exit 0
fi

main
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

find . -name "*.java" -o -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" | \
    grep -v ".backups" | \
    xargs -I {} cp --parents {} "$BACKUP_PATH/" 2>/dev/null || true

echo "✅ Sauvegarde: $BACKUP_PATH"

# Génération
MERMAID_CONTENT=$(cat "$MERMAID_FILE" | sed 's/"/\\"/g' | tr '\n' '\\n')

cat > temp-request.json << EOF
{
    "umlContent": "$MERMAID_CONTENT",
    "language": "$LANGUAGE",
    "packageName": "$PACKAGE_NAME"
}
EOF

curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d @temp-request.json \
    -o "update.zip"

rm temp-request.json

if [ ! -f "update.zip" ] || [ ! -s "update.zip" ]; then
    echo "❌ Échec de la génération"
    exit 1
fi

# Fusion intelligente
mkdir -p temp-update
unzip -q "update.zip" -d temp-update

find temp-update -type f \( -name "*.java" -o -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" \) | while read file; do
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

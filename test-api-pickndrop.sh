#!/bin/bash

# Script pour tester l'API avec le diagramme PicknDrop

API_URL="https://codegenerator-cpyh.onrender.com"
MERMAID_FILE="examples/pickndrop-model.mermaid"

echo "🚀 Test de l'API de génération de code"
echo "====================================="

# Lire le contenu du fichier Mermaid
if [ ! -f "$MERMAID_FILE" ]; then
    echo "❌ Fichier $MERMAID_FILE non trouvé"
    exit 1
fi

MERMAID_CONTENT=$(cat "$MERMAID_FILE")

echo "📖 Contenu UML lu depuis $MERMAID_FILE"
echo "📦 Package: com.pickndrop"
echo "🌐 API URL: $API_URL"
echo ""

# Test 1: Validation UML
echo "🔍 Test 1: Validation du diagramme UML"
echo "======================================"

curl -X POST "$API_URL/api/generate/validate" \
  -H "Content-Type: application/json" \
  -d "$MERMAID_CONTENT" \
  | jq '.'

echo -e "\n"

# Test 2: Génération CRUD (ZIP)
echo "⚙️  Test 2: Génération CRUD complète (ZIP)"
echo "========================================="

curl -X POST "$API_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": $(echo "$MERMAID_CONTENT" | jq -R -s '.'),
    \"packageName\": \"com.pickndrop\"
  }" \
  --output "generated-pickndrop.zip"

if [ $? -eq 0 ]; then
    echo "✅ Code généré avec succès!"
    echo "📁 Fichier: generated-pickndrop.zip"
    
    # Extraire et afficher la structure
    echo ""
    echo "📂 Structure du projet généré:"
    unzip -l generated-pickndrop.zip | head -20
    
    # Extraire le ZIP
    echo ""
    echo "📦 Extraction du projet..."
    unzip -o generated-pickndrop.zip -d extracted-pickndrop/
    echo "✅ Projet extrait dans: extracted-pickndrop/"
    
    # Afficher la structure des dossiers
    echo ""
    echo "🌳 Structure des dossiers:"
    tree extracted-pickndrop/ -L 4 2>/dev/null || find extracted-pickndrop/ -type d | head -10
    
else
    echo "❌ Erreur lors de la génération"
fi

echo ""
echo "🎉 Test terminé!"
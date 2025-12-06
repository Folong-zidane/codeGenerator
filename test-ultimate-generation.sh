#!/bin/bash

# Test de génération ULTIME avec tous les diagrammes disponibles

BASE_URL="http://localhost:8080"
OUTPUT_DIR="generated-projects"

# Lire tous les diagrammes
CLASS_DIAGRAM=$(cat diagrams/complex/class-diagram.mermaid)
SEQUENCE_DIAGRAM=$(cat diagrams/complex/sequence-diagram.mermaid)
STATE_DIAGRAM=$(cat diagrams/complex/state-diagram.mermaid)
ACTIVITY_DIAGRAM=$(cat diagrams/complex/activity-diagram.mermaid)
OBJECT_DIAGRAM=$(cat diagrams/complex/object-diagram.mermaid)

echo "🎯 TEST DE GÉNÉRATION ULTIME"
echo "============================"

# Vérifier si l'API supporte la génération avec 5 diagrammes
echo "🔍 Test de l'API Ultimate (5 diagrammes)..."

# Essayer l'endpoint ultimate s'il existe
curl -s -X POST "$BASE_URL/api/ultimate/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagramContent\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagramContent\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"activityDiagramContent\": $(echo "$ACTIVITY_DIAGRAM" | jq -Rs .),
    \"objectDiagramContent\": $(echo "$OBJECT_DIAGRAM" | jq -Rs .),
    \"packageName\": \"com.ecommerce.ultimate\",
    \"language\": \"java\"
  }" \
  -w "\nHTTP_CODE:%{http_code}" > ultimate_response.txt

http_code=$(tail -1 ultimate_response.txt | cut -d: -f2)

if [ "$http_code" = "200" ]; then
    echo "✅ API Ultimate disponible - Génération réussie"
    head -n -1 ultimate_response.txt > "$OUTPUT_DIR/ecommerce-ultimate.zip"
else
    echo "❌ API Ultimate non disponible (HTTP $http_code)"
fi

# Essayer l'endpoint perfect s'il existe
echo "🔍 Test de l'API Perfect (6 diagrammes)..."
curl -s -X POST "$BASE_URL/api/perfect/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagramContent\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagramContent\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"activityDiagramContent\": $(echo "$ACTIVITY_DIAGRAM" | jq -Rs .),
    \"objectDiagramContent\": $(echo "$OBJECT_DIAGRAM" | jq -Rs .),
    \"componentDiagramContent\": \"componentDiagram\\n    component WebLayer\\n    component ServiceLayer\\n    WebLayer --> ServiceLayer\",
    \"packageName\": \"com.ecommerce.perfect\",
    \"language\": \"java\"
  }" \
  -w "\nHTTP_CODE:%{http_code}" > perfect_response.txt

http_code=$(tail -1 perfect_response.txt | cut -d: -f2)

if [ "$http_code" = "200" ]; then
    echo "✅ API Perfect disponible - Génération réussie"
    head -n -1 perfect_response.txt > "$OUTPUT_DIR/ecommerce-perfect.zip"
else
    echo "❌ API Perfect non disponible (HTTP $http_code)"
fi

# Test avec l'API moderne en ajoutant plus d'options
echo "🔍 Test de génération moderne avancée..."
curl -s -X POST "$BASE_URL/api/modern/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-advanced\",
    \"packageName\": \"com.ecommerce.advanced\",
    \"language\": \"java\",
    \"classDiagram\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagram\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagram\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"outputPath\": \"/tmp/advanced\",
    \"options\": {
      \"javaVersion\": \"17\",
      \"springBootVersion\": \"3.2.0\",
      \"includeTests\": \"true\",
      \"includeDocumentation\": \"true\",
      \"includeDocker\": \"true\"
    }
  }" > "$OUTPUT_DIR/ecommerce-advanced-response.json"

echo "📊 Résultats des tests avancés:"
echo "==============================="

if [ -f "$OUTPUT_DIR/ecommerce-ultimate.zip" ]; then
    size=$(du -h "$OUTPUT_DIR/ecommerce-ultimate.zip" | cut -f1)
    echo "✅ Ultimate: $size"
fi

if [ -f "$OUTPUT_DIR/ecommerce-perfect.zip" ]; then
    size=$(du -h "$OUTPUT_DIR/ecommerce-perfect.zip" | cut -f1)
    echo "✅ Perfect: $size"
fi

if [ -f "$OUTPUT_DIR/ecommerce-advanced-response.json" ]; then
    success=$(jq -r '.success // false' "$OUTPUT_DIR/ecommerce-advanced-response.json" 2>/dev/null)
    if [ "$success" = "true" ]; then
        echo "✅ Advanced Modern: Génération réussie"
    else
        echo "⚠️ Advanced Modern: Réponse reçue mais statut incertain"
    fi
fi

# Nettoyer les fichiers temporaires
rm -f ultimate_response.txt perfect_response.txt

echo ""
echo "🎯 RÉSUMÉ FINAL"
echo "==============="
echo "Tous les projets générés sont disponibles dans: $OUTPUT_DIR/"
ls -la "$OUTPUT_DIR/" | grep -E "\.(zip|json)$"
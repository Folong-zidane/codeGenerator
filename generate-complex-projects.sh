#!/bin/bash

# Script pour générer des projets avec différentes combinaisons de diagrammes
BASE_URL="http://localhost:8080"
OUTPUT_DIR="generated-projects"

# Créer le dossier de sortie
mkdir -p $OUTPUT_DIR

# Lire les diagrammes
CLASS_DIAGRAM=$(cat diagrams/complex/class-diagram.mermaid)
SEQUENCE_DIAGRAM=$(cat diagrams/complex/sequence-diagram.mermaid)
STATE_DIAGRAM=$(cat diagrams/complex/state-diagram.mermaid)
ACTIVITY_DIAGRAM=$(cat diagrams/complex/activity-diagram.mermaid)
OBJECT_DIAGRAM=$(cat diagrams/complex/object-diagram.mermaid)

echo "🚀 GÉNÉRATION DE PROJETS AVEC DIAGRAMMES COMPLEXES"
echo "=================================================="

# 1. GÉNÉRATION CLASSIQUE (Classe uniquement)
echo "1️⃣ Génération avec CLASSE uniquement..."
curl -X POST "$BASE_URL/api/v1/generate/java/download" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-class-only\",
    \"packageName\": \"com.ecommerce.classonly\",
    \"diagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .)
  }" \
  -o "$OUTPUT_DIR/ecommerce-class-only.zip"

# 2. GÉNÉRATION COMPREHENSIVE (Classe + Séquence + État)
echo "2️⃣ Génération COMPREHENSIVE (Classe + Séquence + État)..."
curl -X POST "$BASE_URL/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagram\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagram\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagram\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"packageName\": \"com.ecommerce.comprehensive\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/ecommerce-comprehensive.zip"

# 3. GÉNÉRATION STREAMING (Asynchrone)
echo "3️⃣ Génération STREAMING (Asynchrone)..."
GENERATION_ID=$(curl -s -X POST "$BASE_URL/api/v2/stream/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagram\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagram\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagram\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"packageName\": \"com.ecommerce.streaming\",
    \"language\": \"java\"
  }" | jq -r '.generationId')

echo "   Generation ID: $GENERATION_ID"
echo "   Attente de la génération..."
sleep 5

curl -X GET "$BASE_URL/api/v2/stream/download/$GENERATION_ID" \
  -o "$OUTPUT_DIR/ecommerce-streaming.zip"

# 4. GÉNÉRATION MODERNE
echo "4️⃣ Génération MODERNE..."
curl -X POST "$BASE_URL/api/modern/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-modern\",
    \"packageName\": \"com.ecommerce.modern\",
    \"language\": \"java\",
    \"classDiagram\": $(echo "$CLASS_DIAGRAM" | jq -Rs .),
    \"sequenceDiagram\": $(echo "$SEQUENCE_DIAGRAM" | jq -Rs .),
    \"stateDiagram\": $(echo "$STATE_DIAGRAM" | jq -Rs .),
    \"outputPath\": \"/tmp/modern\",
    \"options\": {\"javaVersion\": \"17\", \"springBootVersion\": \"3.2.0\"}
  }" > "$OUTPUT_DIR/ecommerce-modern-response.json"

# 5. GÉNÉRATION MULTI-LANGAGES
echo "5️⃣ Génération MULTI-LANGAGES..."

# Python
curl -X POST "$BASE_URL/api/v1/generate/python/download" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-python\",
    \"packageName\": \"ecommerce.python\",
    \"diagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .)
  }" \
  -o "$OUTPUT_DIR/ecommerce-python.zip"

# C#
curl -X POST "$BASE_URL/api/v1/generate/csharp/download" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-csharp\",
    \"packageName\": \"Ecommerce.CSharp\",
    \"diagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .)
  }" \
  -o "$OUTPUT_DIR/ecommerce-csharp.zip"

# TypeScript
curl -X POST "$BASE_URL/api/v1/generate/typescript/download" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-typescript\",
    \"packageName\": \"ecommerce.typescript\",
    \"diagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .)
  }" \
  -o "$OUTPUT_DIR/ecommerce-typescript.zip"

# PHP
curl -X POST "$BASE_URL/api/v1/generate/php/download" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-php\",
    \"packageName\": \"Ecommerce\\\\Php\",
    \"diagramContent\": $(echo "$CLASS_DIAGRAM" | jq -Rs .)
  }" \
  -o "$OUTPUT_DIR/ecommerce-php.zip"

echo ""
echo "✅ GÉNÉRATION TERMINÉE"
echo "======================"
echo "📁 Projets générés dans: $OUTPUT_DIR/"
ls -la $OUTPUT_DIR/

echo ""
echo "📊 ANALYSE DES FICHIERS GÉNÉRÉS:"
echo "================================"
for file in $OUTPUT_DIR/*.zip; do
    if [ -f "$file" ]; then
        size=$(du -h "$file" | cut -f1)
        echo "📦 $(basename "$file"): $size"
    fi
done

echo ""
echo "🔍 EXTRACTION ET ANALYSE:"
echo "========================="
cd $OUTPUT_DIR
for zip_file in *.zip; do
    if [ -f "$zip_file" ]; then
        project_name=$(basename "$zip_file" .zip)
        echo "📂 Extraction de $project_name..."
        unzip -q "$zip_file" -d "$project_name"
        file_count=$(find "$project_name" -type f | wc -l)
        echo "   Fichiers générés: $file_count"
    fi
done
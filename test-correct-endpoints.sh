#!/bin/bash

# Script de test avec les vrais endpoints de l'application
BASE_URL="http://localhost:8080"
OUTPUT_DIR="./test-results-$(date +%Y%m%d-%H%M%S)"

mkdir -p "$OUTPUT_DIR"

echo "🚀 Test des vrais endpoints de l'application"
echo "📁 Résultats dans: $OUTPUT_DIR"
echo ""

# Diagramme UML de test
UML_CONTENT='classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +UserStatus status
    }
    class Product {
        +UUID id
        +String name
        +Float price
        +ProductStatus status
    }
    class Order {
        +UUID id
        +UUID userId
        +UUID productId
        +Float total
    }
    User "1" --> "*" Order
    Product "1" --> "*" Order'

# Test de base - vérifier si l'app répond
echo "🔍 Test de base..."
curl -s "$BASE_URL/" --write-out "Status: %{http_code}\n" || echo "❌ Application non disponible"

echo ""
echo "🎯 Test des endpoints de génération..."

# 1. Test Java
echo "☕ Test Java..."
curl -X POST "$BASE_URL/api/v1/generate/java" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-java\",
    \"packageName\": \"com.example.ecommerce\",
    \"diagramContent\": \"$UML_CONTENT\"
  }" \
  -o "$OUTPUT_DIR/java-response.json" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 2. Test Python
echo "🐍 Test Python..."
curl -X POST "$BASE_URL/api/v1/generate/python" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-python\",
    \"packageName\": \"ecommerce_api\",
    \"diagramContent\": \"$UML_CONTENT\"
  }" \
  -o "$OUTPUT_DIR/python-response.json" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 3. Test C#
echo "🔷 Test C#..."
curl -X POST "$BASE_URL/api/v1/generate/csharp" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"EcommerceApi\",
    \"packageName\": \"EcommerceApi\",
    \"diagramContent\": \"$UML_CONTENT\"
  }" \
  -o "$OUTPUT_DIR/csharp-response.json" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 4. Test TypeScript
echo "📘 Test TypeScript..."
curl -X POST "$BASE_URL/api/v1/generate/typescript" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-ts\",
    \"packageName\": \"ecommerce-api\",
    \"diagramContent\": \"$UML_CONTENT\"
  }" \
  -o "$OUTPUT_DIR/typescript-response.json" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 5. Test PHP
echo "🐘 Test PHP..."
curl -X POST "$BASE_URL/api/v1/generate/php" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"ecommerce-php\",
    \"packageName\": \"EcommerceApi\",
    \"diagramContent\": \"$UML_CONTENT\"
  }" \
  -o "$OUTPUT_DIR/php-response.json" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

echo ""
echo "📊 Analyse des réponses..."

# Analyser les réponses JSON
for file in "$OUTPUT_DIR"/*.json; do
    if [ -f "$file" ]; then
        echo ""
        echo "📄 $(basename "$file"):"
        if command -v jq >/dev/null 2>&1; then
            jq . "$file" 2>/dev/null || cat "$file"
        else
            cat "$file"
        fi
    fi
done

echo ""
echo "🎉 Tests terminés!"
echo "📁 Résultats dans: $OUTPUT_DIR"
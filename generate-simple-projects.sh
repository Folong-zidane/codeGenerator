#!/bin/bash

API_URL="http://localhost:8080"
DIAGRAMS_DIR="/home/folongzidane/Documents/Projet/basicCode/diagrams/simple"
OUTPUT_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-simple-projects"

mkdir -p "$OUTPUT_DIR"

CLASS_DIAGRAM=$(cat "$DIAGRAMS_DIR/class-diagram.mermaid")

echo "🚀 Génération de projets avec diagramme de classes..."

generate_project() {
    local language=$1
    local project_name=$2
    local package_name=$3
    
    echo "📝 Génération du projet $language..."
    
    json_payload=$(cat <<EOF
{
    "projectName": "$project_name",
    "packageName": "$package_name",
    "diagramContent": "$CLASS_DIAGRAM"
}
EOF
)
    
    response=$(curl -s -X POST "$API_URL/api/v1/generate/$language" \
        -H "Content-Type: application/json" \
        -d "$json_payload" \
        -w "HTTPSTATUS:%{http_code}")
    
    http_code=$(echo "$response" | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    response_body=$(echo "$response" | sed 's/HTTPSTATUS:[0-9]*$//')
    
    if [ "$http_code" = "200" ]; then
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-response.json"
        echo "✅ $language: Projet généré avec succès"
    else
        echo "❌ $language: Erreur HTTP $http_code"
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-error.log"
    fi
}

# Générer pour chaque langage
generate_project "java" "ecommerce-java" "com.ecommerce"
generate_project "csharp" "ecommerce-csharp" "ECommerce"
generate_project "python" "ecommerce-python" "ecommerce"
generate_project "django" "ecommerce-django" "ecommerce"
generate_project "typescript" "ecommerce-typescript" "ecommerce"
generate_project "php" "ecommerce-php" "ECommerce"

echo "🎯 Génération terminée ! Résultats dans: $OUTPUT_DIR"
ls -la "$OUTPUT_DIR"
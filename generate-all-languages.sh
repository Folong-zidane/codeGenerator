#!/bin/bash

API_URL="http://localhost:8080"
OUTPUT_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-all-languages"

mkdir -p "$OUTPUT_DIR"

# Diagramme de classes (échappé correctement)
CLASS_DIAGRAM='classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User "1" -- "*" Order : places\n    Order "*" -- "*" Product : contains'

echo "🚀 Génération de projets e-commerce pour tous les langages..."

generate_project() {
    local language=$1
    local project_name=$2
    local package_name=$3
    
    echo "📝 Génération du projet $language..."
    
    response=$(curl -s -X POST "$API_URL/api/v1/generate/$language" \
        -H "Content-Type: application/json" \
        -d "{
            \"projectName\": \"$project_name\",
            \"packageName\": \"$package_name\",
            \"diagramContent\": \"$CLASS_DIAGRAM\"
        }" \
        -w "HTTPSTATUS:%{http_code}")
    
    http_code=$(echo "$response" | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    response_body=$(echo "$response" | sed 's/HTTPSTATUS:[0-9]*$//')
    
    if [ "$http_code" = "200" ]; then
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-response.json"
        echo "✅ $language: Projet généré avec succès"
        
        # Extraire les informations du projet
        generated_files=$(echo "$response_body" | grep -o '"generatedFiles":\[[^]]*\]' | head -1)
        echo "   📁 Fichiers générés: $generated_files"
    else
        echo "❌ $language: Erreur HTTP $http_code"
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-error.log"
    fi
}

# Générer pour chaque langage supporté
generate_project "java" "ecommerce-java" "com.ecommerce"
generate_project "csharp" "ecommerce-csharp" "ECommerce"
generate_project "python" "ecommerce-python" "ecommerce"
generate_project "django" "ecommerce-django" "ecommerce"
generate_project "typescript" "ecommerce-typescript" "ecommerce"
generate_project "php" "ecommerce-php" "ECommerce"

echo ""
echo "🎯 Génération terminée ! Résultats dans: $OUTPUT_DIR"
echo ""
echo "📊 Résumé des générations:"
for file in "$OUTPUT_DIR"/*-response.json; do
    if [ -f "$file" ]; then
        project=$(basename "$file" -response.json)
        language=$(echo "$project" | cut -d- -f2)
        files_count=$(grep -o '"generatedFiles":\[[^]]*\]' "$file" | grep -o ',' | wc -l)
        files_count=$((files_count + 1))
        echo "   ✅ $language: $files_count fichiers générés"
    fi
done

for file in "$OUTPUT_DIR"/*-error.log; do
    if [ -f "$file" ]; then
        project=$(basename "$file" -error.log)
        language=$(echo "$project" | cut -d- -f2)
        echo "   ❌ $language: Échec de génération"
    fi
done
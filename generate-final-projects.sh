#!/bin/bash

API_URL="http://localhost:8080"
OUTPUT_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-final-projects"

mkdir -p "$OUTPUT_DIR"

echo "🚀 Génération de projets e-commerce complets pour tous les langages..."

# Créer les fichiers JSON de requête
create_request_files() {
    # Java
    cat > request-java.json << 'EOF'
{
    "projectName": "ecommerce-java-complete",
    "packageName": "com.ecommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF

    # C#
    cat > request-csharp.json << 'EOF'
{
    "projectName": "ecommerce-csharp-complete",
    "packageName": "ECommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF

    # Python
    cat > request-python.json << 'EOF'
{
    "projectName": "ecommerce-python-complete",
    "packageName": "ecommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF

    # Django
    cat > request-django.json << 'EOF'
{
    "projectName": "ecommerce-django-complete",
    "packageName": "ecommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF

    # TypeScript
    cat > request-typescript.json << 'EOF'
{
    "projectName": "ecommerce-typescript-complete",
    "packageName": "ecommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF

    # PHP
    cat > request-php.json << 'EOF'
{
    "projectName": "ecommerce-php-complete",
    "packageName": "ECommerce",
    "diagramContent": "classDiagram\n    class User {\n        id: String\n        name: String\n        email: String\n        +getProfile()\n        +updateProfile()\n    }\n    \n    class Order {\n        id: String\n        userId: String\n        date: Date\n        total: Double\n        +calculateTotal()\n        +getStatus()\n    }\n    \n    class Product {\n        id: String\n        name: String\n        price: Double\n        stock: Integer\n        +decreaseStock()\n        +isAvailable()\n    }\n    \n    User \"1\" -- \"*\" Order : places\n    Order \"*\" -- \"*\" Product : contains"
}
EOF
}

generate_project() {
    local language=$1
    local request_file=$2
    
    echo "📝 Génération du projet $language..."
    
    response=$(curl -s -X POST "$API_URL/api/v1/generate/$language" \
        -H "Content-Type: application/json" \
        -d @"$request_file" \
        -w "HTTPSTATUS:%{http_code}")
    
    http_code=$(echo "$response" | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    response_body=$(echo "$response" | sed 's/HTTPSTATUS:[0-9]*$//')
    
    if [ "$http_code" = "200" ]; then
        echo "$response_body" > "$OUTPUT_DIR/${language}-result.json"
        echo "✅ $language: Projet généré avec succès"
        
        # Compter les fichiers générés
        files_count=$(echo "$response_body" | grep -o ',' | wc -l)
        files_count=$((files_count + 1))
        echo "   📁 $files_count fichiers générés"
    else
        echo "❌ $language: Erreur HTTP $http_code"
        echo "$response_body" > "$OUTPUT_DIR/${language}-error.log"
    fi
}

# Créer les fichiers de requête
create_request_files

# Générer tous les projets
generate_project "java" "request-java.json"
generate_project "csharp" "request-csharp.json"
generate_project "python" "request-python.json"
generate_project "django" "request-django.json"
generate_project "typescript" "request-typescript.json"
generate_project "php" "request-php.json"

# Nettoyer les fichiers temporaires
rm -f request-*.json

echo ""
echo "🎯 Génération terminée ! Résultats dans: $OUTPUT_DIR"
echo ""
echo "📊 Résumé final:"
success_count=0
error_count=0

for file in "$OUTPUT_DIR"/*-result.json; do
    if [ -f "$file" ]; then
        language=$(basename "$file" -result.json)
        project_name=$(grep -o '"projectName":"[^"]*"' "$file" | cut -d'"' -f4)
        files_count=$(grep -o '"generatedFiles":\[[^]]*\]' "$file" | tr ',' '\n' | wc -l)
        echo "   ✅ $language: $project_name ($files_count fichiers)"
        success_count=$((success_count + 1))
    fi
done

for file in "$OUTPUT_DIR"/*-error.log; do
    if [ -f "$file" ]; then
        language=$(basename "$file" -error.log)
        echo "   ❌ $language: Échec de génération"
        error_count=$((error_count + 1))
    fi
done

echo ""
echo "📈 Statistiques: $success_count succès, $error_count échecs"
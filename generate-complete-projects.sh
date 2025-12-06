#!/bin/bash

# Script pour générer des projets complets avec les 5 diagrammes
# pour chaque langage supporté

API_URL="http://localhost:8080"
DIAGRAMS_DIR="/home/folongzidane/Documents/Projet/basicCode/diagrams/simple"
OUTPUT_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-complete-projects"

# Créer le dossier de sortie
mkdir -p "$OUTPUT_DIR"

# Lire les diagrammes
CLASS_DIAGRAM=$(cat "$DIAGRAMS_DIR/class-diagram.mermaid")
SEQUENCE_DIAGRAM=$(cat "$DIAGRAMS_DIR/sequence-diagram.mermaid")
STATE_DIAGRAM=$(cat "$DIAGRAMS_DIR/state-diagram.mermaid")
ACTIVITY_DIAGRAM=$(cat "$DIAGRAMS_DIR/activity-diagram.mermaid")
OBJECT_DIAGRAM=$(cat "$DIAGRAMS_DIR/object-diagram.mermaid")

echo "🚀 Génération de projets complets avec 5 diagrammes UML..."

# Fonction pour générer un projet
generate_project() {
    local language=$1
    local project_name=$2
    local package_name=$3
    
    echo "📝 Génération du projet $language..."
    
    # Préparer le JSON avec les diagrammes principaux
    json_payload=$(cat <<EOF
{
    "projectName": "$project_name",
    "packageName": "$package_name",
    "language": "$language",
    "classDiagram": "$CLASS_DIAGRAM",
    "sequenceDiagram": "$SEQUENCE_DIAGRAM",
    "stateDiagram": "$STATE_DIAGRAM",
    "outputPath": "$OUTPUT_DIR/$project_name",
    "options": {}
}
EOF
)
    
    # Appeler l'API moderne
    response=$(curl -s -X POST "$API_URL/api/modern/generate" \
        -H "Content-Type: application/json" \
        -d "$json_payload" \
        -w "HTTPSTATUS:%{http_code}")
    
    # Extraire le code de statut
    http_code=$(echo "$response" | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    response_body=$(echo "$response" | sed 's/HTTPSTATUS:[0-9]*$//')
    
    if [ "$http_code" = "200" ]; then
        # Sauvegarder la réponse
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-response.zip"
        echo "✅ $language: Projet généré avec succès"
    else
        echo "❌ $language: Erreur HTTP $http_code"
        echo "$response_body" > "$OUTPUT_DIR/${project_name}-error.log"
    fi
}

# Générer pour chaque langage
generate_project "java" "ecommerce-java-complete" "com.ecommerce.complete"
generate_project "csharp" "ecommerce-csharp-complete" "ECommerce.Complete"
generate_project "python" "ecommerce-python-complete" "ecommerce_complete"
generate_project "django" "ecommerce-django-complete" "ecommerce_complete"
generate_project "typescript" "ecommerce-typescript-complete" "ecommerce-complete"
generate_project "php" "ecommerce-php-complete" "ECommerce\\Complete"

echo "🎯 Génération terminée ! Résultats dans: $OUTPUT_DIR"
ls -la "$OUTPUT_DIR"
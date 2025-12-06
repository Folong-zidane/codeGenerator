#!/bin/bash

echo "🚀 Génération de projets complexes pour tous les langages..."

API_URL="http://localhost:8080"
OUTPUT_DIR="complex-projects"
mkdir -p "$OUTPUT_DIR"

# Lire le diagramme complexe
COMPLEX_DIAGRAM=$(cat diagrams/complex/class-diagram.mermaid)

generate_complex_project() {
    local language=$1
    local project_name="ecommerce-complex-$language"
    local package_name
    
    case $language in
        "java") package_name="com.ecommerce.complex" ;;
        "csharp") package_name="ECommerce.Complex" ;;
        "python") package_name="ecommerce_complex" ;;
        "django") package_name="ecommerce_complex" ;;
        "typescript") package_name="ecommerce-complex" ;;
        "php") package_name="ECommerce\\Complex" ;;
    esac
    
    echo "📝 Génération du projet complexe $language..."
    
    # Créer le fichier JSON temporaire
    cat > temp-request.json << EOF
{
    "projectName": "$project_name",
    "packageName": "$package_name",
    "diagramContent": "$COMPLEX_DIAGRAM"
}
EOF
    
    # Appeler l'API
    response=$(curl -s -X POST "$API_URL/api/v1/generate/$language/download" \
        -H "Content-Type: application/json" \
        -d @temp-request.json \
        -o "$OUTPUT_DIR/$project_name.zip" \
        -w "HTTPSTATUS:%{http_code}")
    
    http_code=$(echo "$response" | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    
    if [ "$http_code" = "200" ]; then
        file_size=$(stat -c%s "$OUTPUT_DIR/$project_name.zip" 2>/dev/null || echo "0")
        echo "✅ $language: Projet généré avec succès ($file_size bytes)"
        
        # Vérifier le contenu du ZIP
        echo "   📁 Contenu du ZIP:"
        unzip -l "$OUTPUT_DIR/$project_name.zip" | tail -n +4 | head -n -2 | awk '{print "     - " $4}' | head -5
        if [ $(unzip -l "$OUTPUT_DIR/$project_name.zip" | wc -l) -gt 10 ]; then
            echo "     ... et $(( $(unzip -l "$OUTPUT_DIR/$project_name.zip" | tail -n +4 | head -n -2 | wc -l) - 5 )) autres fichiers"
        fi
    else
        echo "❌ $language: Erreur HTTP $http_code"
        rm -f "$OUTPUT_DIR/$project_name.zip"
    fi
    
    rm -f temp-request.json
}

# Générer pour chaque langage
generate_complex_project "java"
generate_complex_project "csharp" 
generate_complex_project "python"
generate_complex_project "typescript"
generate_complex_project "php"

echo ""
echo "🎯 Génération terminée ! Projets dans: $OUTPUT_DIR/"
echo ""
echo "📊 Résumé:"
for zip_file in "$OUTPUT_DIR"/*.zip; do
    if [ -f "$zip_file" ]; then
        filename=$(basename "$zip_file" .zip)
        language=$(echo "$filename" | cut -d- -f3)
        size=$(stat -c%s "$zip_file" 2>/dev/null || echo "0")
        files_count=$(unzip -l "$zip_file" | tail -n +4 | head -n -2 | wc -l)
        echo "   ✅ $language: $files_count fichiers ($size bytes)"
    fi
done
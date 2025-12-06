#!/bin/bash

echo "🚀 Génération de projets e-commerce complexes pour tous les langages..."

generate_language() {
    local language=$1
    local package_name=$2
    
    echo "📝 Génération du projet complexe $language..."
    
    # Modifier le package dans le fichier JSON
    sed "s/\"language\": \"python\"/\"language\": \"$language\"/" complex-request.json | \
    sed "s/\"packageName\": \"ecommerce_complex\"/\"packageName\": \"$package_name\"/" > temp-$language.json
    
    curl -X POST "http://localhost:8080/api/v1/generate/$language/download" \
      -H "Content-Type: application/json" \
      -d @temp-$language.json \
      -o "ecommerce-complex-$language.zip" \
      --write-out "$language Status: %{http_code}, Size: %{size_download} bytes\n"
    
    rm temp-$language.json
}

# Générer pour chaque langage
generate_language "java" "com.ecommerce.complex"
generate_language "csharp" "ECommerce.Complex" 
generate_language "typescript" "ecommerce-complex"
generate_language "php" "ECommerce\\\\Complex"

echo ""
echo "🎯 Génération terminée !"
echo ""
echo "📊 Projets générés:"
for zip_file in ecommerce-complex-*.zip; do
    if [ -f "$zip_file" ]; then
        language=$(echo "$zip_file" | cut -d- -f3 | cut -d. -f1)
        size=$(stat -c%s "$zip_file")
        files_count=$(unzip -l "$zip_file" | tail -n +4 | head -n -2 | wc -l)
        echo "   ✅ $language: $files_count fichiers ($(($size / 1024)) KB)"
    fi
done
#!/bin/bash

echo "🔍 Vérification des projets générés..."

RESULTS_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-final-projects"

echo ""
echo "📊 Analyse détaillée des projets générés:"
echo "=========================================="

for result_file in "$RESULTS_DIR"/*-result.json; do
    if [ -f "$result_file" ]; then
        language=$(basename "$result_file" -result.json)
        
        echo ""
        echo "🔸 **$language**"
        echo "   Project: $(grep -o '"projectName":"[^"]*"' "$result_file" | cut -d'"' -f4)"
        echo "   Status: $(grep -o '"status":"[^"]*"' "$result_file" | cut -d'"' -f4)"
        echo "   Output: $(grep -o '"outputPath":"[^"]*"' "$result_file" | cut -d'"' -f4)"
        
        # Extraire et afficher les fichiers générés
        echo "   Files generated:"
        grep -o '"generatedFiles":\[[^]]*\]' "$result_file" | \
        sed 's/"generatedFiles":\[//; s/\]//; s/","/\n/g; s/"//g' | \
        while read -r file; do
            if [ -n "$file" ]; then
                echo "     - $file"
            fi
        done
    fi
done

echo ""
echo "❌ Échecs:"
for error_file in "$RESULTS_DIR"/*-error.log; do
    if [ -f "$error_file" ]; then
        language=$(basename "$error_file" -error.log)
        echo "   - $language: $(cat "$error_file")"
    fi
done

echo ""
echo "📈 Statistiques finales:"
success_count=$(ls "$RESULTS_DIR"/*-result.json 2>/dev/null | wc -l)
error_count=$(ls "$RESULTS_DIR"/*-error.log 2>/dev/null | wc -l)
total=$((success_count + error_count))

echo "   ✅ Succès: $success_count/$total"
echo "   ❌ Échecs: $error_count/$total"
echo "   📊 Taux de réussite: $(( success_count * 100 / total ))%"

echo ""
echo "🎯 Projets prêts pour utilisation dans le dossier 'generated/'"
#!/bin/bash

# Script de test de génération de code pour tous les langages
echo "🚀 Test de génération de code - Tous langages"
echo "=============================================="

# Configuration
API_URL="http://localhost:8080"
OUTPUT_DIR="generated-tests"
DIAGRAMS_DIR="diagrams"

# Langages à tester
LANGUAGES=("java" "python" "django" "csharp" "typescript" "php")

# Diagrammes à tester
DIAGRAMS=("simple-ecommerce" "blog-system" "library-management")

# Créer le répertoire de sortie
mkdir -p $OUTPUT_DIR

# Fonction pour tester un diagramme avec un langage
test_generation() {
    local diagram=$1
    local language=$2
    local diagram_file="$DIAGRAMS_DIR/${diagram}.mermaid"
    
    echo "📋 Test: $diagram -> $language"
    
    if [ ! -f "$diagram_file" ]; then
        echo "❌ Diagramme non trouvé: $diagram_file"
        return 1
    fi
    
    # Lire le contenu du diagramme
    local uml_content=$(cat "$diagram_file")
    
    # Préparer la requête JSON
    local json_payload=$(jq -n \
        --arg uml "$uml_content" \
        --arg lang "$language" \
        --arg pkg "com.test.${diagram//-/}" \
        '{
            umlContent: $uml,
            language: $lang,
            packageName: $pkg
        }')
    
    # Envoyer la requête
    local output_file="$OUTPUT_DIR/${diagram}-${language}.zip"
    
    echo "🔄 Génération en cours..."
    curl -s -X POST "$API_URL/api/generate/crud" \
        -H "Content-Type: application/json" \
        -d "$json_payload" \
        -o "$output_file"
    
    # Vérifier le résultat
    if [ -f "$output_file" ] && [ -s "$output_file" ]; then
        # Vérifier si c'est un ZIP valide
        if unzip -t "$output_file" >/dev/null 2>&1; then
            echo "✅ Génération réussie: $output_file"
            
            # Extraire et analyser
            local extract_dir="$OUTPUT_DIR/${diagram}-${language}"
            mkdir -p "$extract_dir"
            unzip -q "$output_file" -d "$extract_dir"
            
            # Compter les fichiers générés
            local file_count=$(find "$extract_dir" -type f | wc -l)
            echo "📁 Fichiers générés: $file_count"
            
            # Lister les types de fichiers
            echo "📄 Types de fichiers:"
            find "$extract_dir" -type f -name "*.*" | sed 's/.*\.//' | sort | uniq -c | sort -nr
            
            return 0
        else
            echo "❌ Fichier ZIP invalide"
            return 1
        fi
    else
        echo "❌ Échec de génération"
        return 1
    fi
}

# Fonction pour vérifier si l'API est disponible
check_api() {
    echo "🔍 Vérification de l'API..."
    if curl -s "$API_URL/actuator/health" >/dev/null 2>&1; then
        echo "✅ API disponible"
        return 0
    else
        echo "❌ API non disponible. Démarrez l'application d'abord."
        return 1
    fi
}

# Fonction principale
main() {
    # Vérifier l'API
    if ! check_api; then
        exit 1
    fi
    
    echo ""
    echo "🧪 Début des tests de génération"
    echo "================================"
    
    local total_tests=0
    local successful_tests=0
    
    # Tester chaque combinaison diagramme/langage
    for diagram in "${DIAGRAMS[@]}"; do
        for language in "${LANGUAGES[@]}"; do
            echo ""
            total_tests=$((total_tests + 1))
            
            if test_generation "$diagram" "$language"; then
                successful_tests=$((successful_tests + 1))
            fi
            
            echo "---"
        done
    done
    
    echo ""
    echo "📊 Résultats des tests"
    echo "====================="
    echo "Total: $total_tests"
    echo "Réussis: $successful_tests"
    echo "Échecs: $((total_tests - successful_tests))"
    echo "Taux de réussite: $(( successful_tests * 100 / total_tests ))%"
    
    if [ $successful_tests -eq $total_tests ]; then
        echo "🎉 Tous les tests ont réussi!"
    else
        echo "⚠️  Certains tests ont échoué"
    fi
}

# Exécuter le script principal
main "$@"
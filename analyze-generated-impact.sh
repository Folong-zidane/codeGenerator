#!/bin/bash

# Script d'analyse de l'impact des différents diagrammes sur la génération

OUTPUT_DIR="generated-projects"
cd $OUTPUT_DIR

echo "🔍 ANALYSE DE L'IMPACT DES DIAGRAMMES"
echo "====================================="

# Fonction pour analyser un projet
analyze_project() {
    local project_dir=$1
    local project_name=$2
    
    echo ""
    echo "📂 ANALYSE: $project_name"
    echo "$(printf '=%.0s' {1..50})"
    
    if [ -d "$project_dir" ]; then
        # Compter les fichiers par type
        java_files=$(find "$project_dir" -name "*.java" | wc -l)
        py_files=$(find "$project_dir" -name "*.py" | wc -l)
        cs_files=$(find "$project_dir" -name "*.cs" | wc -l)
        ts_files=$(find "$project_dir" -name "*.ts" | wc -l)
        php_files=$(find "$project_dir" -name "*.php" | wc -l)
        
        # Analyser la structure
        controllers=$(find "$project_dir" -name "*Controller*" | wc -l)
        services=$(find "$project_dir" -name "*Service*" | wc -l)
        entities=$(find "$project_dir" -name "*Entity*" -o -name "*Model*" | wc -l)
        repositories=$(find "$project_dir" -name "*Repository*" | wc -l)
        
        # Analyser les méthodes spéciales
        state_methods=$(grep -r "transition\|state\|status" "$project_dir" 2>/dev/null | wc -l)
        sequence_methods=$(grep -r "process\|workflow\|step" "$project_dir" 2>/dev/null | wc -l)
        
        echo "📊 Statistiques:"
        echo "   Fichiers Java: $java_files"
        echo "   Fichiers Python: $py_files"
        echo "   Fichiers C#: $cs_files"
        echo "   Fichiers TypeScript: $ts_files"
        echo "   Fichiers PHP: $php_files"
        echo ""
        echo "🏗️ Architecture:"
        echo "   Controllers: $controllers"
        echo "   Services: $services"
        echo "   Entities/Models: $entities"
        echo "   Repositories: $repositories"
        echo ""
        echo "🔄 Logique métier:"
        echo "   Méthodes d'état: $state_methods"
        echo "   Méthodes de séquence: $sequence_methods"
        
        # Analyser des fichiers spécifiques
        echo ""
        echo "📋 Fichiers clés générés:"
        find "$project_dir" -name "*.java" -o -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" | head -10 | while read file; do
            echo "   $(basename "$file")"
        done
    else
        echo "❌ Projet non extrait"
    fi
}

# Analyser chaque projet
analyze_project "ecommerce-class-only" "CLASSE UNIQUEMENT"
analyze_project "ecommerce-comprehensive" "COMPREHENSIVE (Classe+Séquence+État)"
analyze_project "ecommerce-streaming" "STREAMING (Asynchrone)"
analyze_project "ecommerce-python" "PYTHON (FastAPI)"
analyze_project "ecommerce-csharp" "C# (.NET Core)"
analyze_project "ecommerce-typescript" "TYPESCRIPT (Express)"
analyze_project "ecommerce-php" "PHP (Slim)"

echo ""
echo "🔍 COMPARAISON DÉTAILLÉE"
echo "========================"

# Comparer les tailles de fichiers
echo "📏 Tailles des projets générés:"
for zip_file in *.zip; do
    if [ -f "$zip_file" ]; then
        size=$(du -h "$zip_file" | cut -f1)
        project_name=$(basename "$zip_file" .zip)
        echo "   $project_name: $size"
    fi
done

echo ""
echo "🎯 IMPACT DES DIAGRAMMES"
echo "========================"

# Analyser les différences entre classe seule et comprehensive
if [ -d "ecommerce-class-only" ] && [ -d "ecommerce-comprehensive" ]; then
    echo "📊 Comparaison Classe vs Comprehensive:"
    
    class_only_files=$(find ecommerce-class-only -type f | wc -l)
    comprehensive_files=$(find ecommerce-comprehensive -type f | wc -l)
    
    echo "   Classe uniquement: $class_only_files fichiers"
    echo "   Comprehensive: $comprehensive_files fichiers"
    echo "   Différence: $((comprehensive_files - class_only_files)) fichiers"
    
    # Chercher des fichiers spécifiques aux diagrammes de séquence/état
    echo ""
    echo "🔄 Fichiers spécifiques au diagramme de séquence:"
    find ecommerce-comprehensive -name "*Workflow*" -o -name "*Process*" -o -name "*Step*" 2>/dev/null | head -5
    
    echo ""
    echo "🏛️ Fichiers spécifiques au diagramme d'état:"
    find ecommerce-comprehensive -name "*State*" -o -name "*Status*" -o -name "*Transition*" 2>/dev/null | head -5
fi

echo ""
echo "🌐 COMPARAISON MULTI-LANGAGES"
echo "============================="

for lang_project in ecommerce-python ecommerce-csharp ecommerce-typescript ecommerce-php; do
    if [ -d "$lang_project" ]; then
        files_count=$(find "$lang_project" -type f | wc -l)
        main_files=$(find "$lang_project" -name "*.py" -o -name "*.cs" -o -name "*.ts" -o -name "*.php" | wc -l)
        echo "📂 $lang_project: $files_count fichiers total, $main_files fichiers code"
    fi
done
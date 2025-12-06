#!/bin/bash

# Script d'analyse de la qualité du code généré
echo "🔍 Analyse de la qualité du code généré"
echo "======================================="

OUTPUT_DIR="generated-tests"
ANALYSIS_DIR="code-analysis"

# Créer le répertoire d'analyse
mkdir -p $ANALYSIS_DIR

# Fonction pour analyser un projet généré
analyze_project() {
    local project_dir=$1
    local project_name=$(basename "$project_dir")
    local language=$(echo "$project_name" | cut -d'-' -f3)
    
    echo "📊 Analyse: $project_name"
    echo "Langage: $language"
    
    local analysis_file="$ANALYSIS_DIR/${project_name}-analysis.md"
    
    {
        echo "# Analyse du code généré: $project_name"
        echo "**Langage:** $language"
        echo "**Date:** $(date)"
        echo ""
        
        # Structure du projet
        echo "## Structure du projet"
        echo '```'
        find "$project_dir" -type f | head -20
        echo '```'
        echo ""
        
        # Statistiques des fichiers
        echo "## Statistiques"
        echo "- **Total fichiers:** $(find "$project_dir" -type f | wc -l)"
        echo "- **Répertoires:** $(find "$project_dir" -type d | wc -l)"
        
        # Types de fichiers
        echo ""
        echo "### Types de fichiers"
        find "$project_dir" -type f -name "*.*" | sed 's/.*\.//' | sort | uniq -c | sort -nr | while read count ext; do
            echo "- **.$ext:** $count fichiers"
        done
        
        echo ""
        
        # Analyse spécifique par langage
        case $language in
            "java")
                analyze_java_project "$project_dir"
                ;;
            "python"|"django")
                analyze_python_project "$project_dir"
                ;;
            "csharp")
                analyze_csharp_project "$project_dir"
                ;;
            "typescript")
                analyze_typescript_project "$project_dir"
                ;;
            "php")
                analyze_php_project "$project_dir"
                ;;
        esac
        
    } > "$analysis_file"
    
    echo "✅ Analyse sauvegardée: $analysis_file"
}

# Analyse spécifique Java
analyze_java_project() {
    local project_dir=$1
    
    echo "## Analyse Java"
    
    # Vérifier la présence des fichiers essentiels
    echo "### Fichiers essentiels"
    [ -f "$project_dir/pom.xml" ] && echo "- ✅ pom.xml présent" || echo "- ❌ pom.xml manquant"
    [ -f "$project_dir/src/main/java"* ] && echo "- ✅ Structure Maven présente" || echo "- ❌ Structure Maven manquante"
    
    # Compter les classes par type
    echo ""
    echo "### Classes générées"
    echo "- **Entités:** $(find "$project_dir" -name "*Entity.java" -o -name "*Model.java" | wc -l)"
    echo "- **Repositories:** $(find "$project_dir" -name "*Repository.java" | wc -l)"
    echo "- **Services:** $(find "$project_dir" -name "*Service.java" | wc -l)"
    echo "- **Controllers:** $(find "$project_dir" -name "*Controller.java" | wc -l)"
    
    # Vérifier les annotations Spring
    echo ""
    echo "### Annotations Spring"
    local spring_annotations=$(find "$project_dir" -name "*.java" -exec grep -l "@Entity\|@Repository\|@Service\|@RestController" {} \; | wc -l)
    echo "- **Fichiers avec annotations Spring:** $spring_annotations"
}

# Analyse spécifique Python
analyze_python_project() {
    local project_dir=$1
    
    echo "## Analyse Python"
    
    # Vérifier les fichiers essentiels
    echo "### Fichiers essentiels"
    [ -f "$project_dir/requirements.txt" ] && echo "- ✅ requirements.txt présent" || echo "- ❌ requirements.txt manquant"
    [ -f "$project_dir/main.py" -o -f "$project_dir/manage.py" ] && echo "- ✅ Point d'entrée présent" || echo "- ❌ Point d'entrée manquant"
    
    # Compter les modules
    echo ""
    echo "### Modules Python"
    echo "- **Fichiers .py:** $(find "$project_dir" -name "*.py" | wc -l)"
    echo "- **Modèles:** $(find "$project_dir" -name "*model*.py" -o -name "models.py" | wc -l)"
    echo "- **Vues/Routes:** $(find "$project_dir" -name "*view*.py" -o -name "*route*.py" -o -name "views.py" | wc -l)"
}

# Analyse spécifique C#
analyze_csharp_project() {
    local project_dir=$1
    
    echo "## Analyse C#"
    
    echo "### Fichiers essentiels"
    [ -f "$project_dir"/*.csproj ] && echo "- ✅ Fichier .csproj présent" || echo "- ❌ Fichier .csproj manquant"
    
    echo ""
    echo "### Classes C#"
    echo "- **Fichiers .cs:** $(find "$project_dir" -name "*.cs" | wc -l)"
    echo "- **Modèles:** $(find "$project_dir" -name "*Model.cs" -o -name "*Entity.cs" | wc -l)"
    echo "- **Controllers:** $(find "$project_dir" -name "*Controller.cs" | wc -l)"
}

# Analyse spécifique TypeScript
analyze_typescript_project() {
    local project_dir=$1
    
    echo "## Analyse TypeScript"
    
    echo "### Fichiers essentiels"
    [ -f "$project_dir/package.json" ] && echo "- ✅ package.json présent" || echo "- ❌ package.json manquant"
    [ -f "$project_dir/tsconfig.json" ] && echo "- ✅ tsconfig.json présent" || echo "- ❌ tsconfig.json manquant"
    
    echo ""
    echo "### Fichiers TypeScript"
    echo "- **Fichiers .ts:** $(find "$project_dir" -name "*.ts" | wc -l)"
    echo "- **Modèles:** $(find "$project_dir" -name "*model*.ts" -o -name "*entity*.ts" | wc -l)"
    echo "- **Routes:** $(find "$project_dir" -name "*route*.ts" -o -name "*controller*.ts" | wc -l)"
}

# Analyse spécifique PHP
analyze_php_project() {
    local project_dir=$1
    
    echo "## Analyse PHP"
    
    echo "### Fichiers essentiels"
    [ -f "$project_dir/composer.json" ] && echo "- ✅ composer.json présent" || echo "- ❌ composer.json manquant"
    
    echo ""
    echo "### Fichiers PHP"
    echo "- **Fichiers .php:** $(find "$project_dir" -name "*.php" | wc -l)"
    echo "- **Modèles:** $(find "$project_dir" -name "*Model.php" -o -name "*Entity.php" | wc -l)"
    echo "- **Controllers:** $(find "$project_dir" -name "*Controller.php" | wc -l)"
}

# Fonction principale
main() {
    if [ ! -d "$OUTPUT_DIR" ]; then
        echo "❌ Répertoire $OUTPUT_DIR non trouvé. Exécutez d'abord test-generation.sh"
        exit 1
    fi
    
    echo "🔍 Recherche des projets générés..."
    
    local project_count=0
    
    # Analyser chaque projet extrait
    for project_dir in "$OUTPUT_DIR"/*; do
        if [ -d "$project_dir" ] && [ ! -f "$project_dir" ]; then
            analyze_project "$project_dir"
            project_count=$((project_count + 1))
            echo ""
        fi
    done
    
    echo "📊 Résumé de l'analyse"
    echo "====================="
    echo "Projets analysés: $project_count"
    echo "Rapports générés dans: $ANALYSIS_DIR/"
    
    # Créer un rapport de synthèse
    {
        echo "# Rapport de synthèse - Qualité du code généré"
        echo "**Date:** $(date)"
        echo "**Projets analysés:** $project_count"
        echo ""
        
        echo "## Projets par langage"
        for lang in java python django csharp typescript php; do
            local count=$(ls -1 "$ANALYSIS_DIR" | grep -c "$lang")
            if [ $count -gt 0 ]; then
                echo "- **$lang:** $count projets"
            fi
        done
        
    } > "$ANALYSIS_DIR/synthesis-report.md"
    
    echo "✅ Rapport de synthèse: $ANALYSIS_DIR/synthesis-report.md"
}

# Exécuter l'analyse
main "$@"
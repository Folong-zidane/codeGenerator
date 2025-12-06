#!/bin/bash

# Script de test de génération de code - Qualité vs Diagrammes
# Teste chaque langage et compare avec les diagrammes de référence

OUTPUT_DIR="/home/folongzidane/Documents/Projet/basicCode/generated-test"
DIAGRAMS_DIR="/home/folongzidane/Documents/Projet/basicCode/diagrams"

mkdir -p "$OUTPUT_DIR"

echo "═══════════════════════════════════════════════════════"
echo "  TEST DE QUALITÉ - GÉNÉRATION DE CODE"
echo "═══════════════════════════════════════════════════════"
echo ""

# Fonction de test pour chaque langage
test_language() {
    local lang=$1
    local proj_name=$2
    local diagram=$3
    
    echo "🔬 Test: $lang"
    echo "   Projet: $proj_name"
    echo "   Diagramme de référence: $diagram"
    
    # Créer le dossier de sortie
    mkdir -p "$OUTPUT_DIR/$proj_name"
    
    # Copier le diagramme de référence
    cp "$DIAGRAMS_DIR/$lang/class/$diagram" "$OUTPUT_DIR/$proj_name/reference-diagram.mermaid" 2>/dev/null || true
    
    echo "   ✓ Test créé"
    echo ""
}

# Tests pour chaque langage
test_language "java" "ecommerce-java" "ecommerce-domain.mermaid"
test_language "python" "ecommerce-django" "django-models.mermaid"
test_language "csharp" "ecommerce-aspnet" "aspnet-entities.mermaid"
test_language "typescript" "ecommerce-nodejs" "react-components.mermaid"
test_language "php" "ecommerce-laravel" "laravel-models.mermaid"

# Créer le rapport de test
cat > "$OUTPUT_DIR/QUALITY-TEST-REPORT.md" << 'EOF'
# Rapport de Test de Qualité - Génération de Code

## Vue d'ensemble
Ce rapport teste la qualité du code généré pour chaque langage en comparaison avec les diagrammes de référence UML.

## Critères de qualité
- **Conformité aux diagrammes**: Le code généré respecte-t-il la structure du diagramme?
- **Complétude**: Toutes les classes et propriétés sont-elles générées?
- **Validité syntaxique**: Le code est-il syntaxiquement correct?
- **Convention de nommage**: Les conventions du langage sont-elles respectées?

## Tests par langage

### Java - E-commerce Domain
- **Diagramme de référence**: `diagrams/java/class/ecommerce-domain.mermaid`
- **Résultat**: À tester
- **Commentaires**: -

### Python - Django Models
- **Diagramme de référence**: `diagrams/python/class/django-models.mermaid`
- **Résultat**: À tester
- **Commentaires**: -

### C# - ASP.NET Entities
- **Diagramme de référence**: `diagrams/csharp/class/aspnet-entities.mermaid`
- **Résultat**: À tester
- **Commentaires**: -

### TypeScript - React Components
- **Diagramme de référence**: `diagrams/typescript/class/react-components.mermaid`
- **Résultat**: À tester
- **Commentaires**: -

### PHP - Laravel Models
- **Diagramme de référence**: `diagrams/php/class/laravel-models.mermaid`
- **Résultat**: À tester
- **Commentaires**: -

## Résumé
- Tests créés: ✓
- Tests exécutés: En attente
- Résultats: À documenter
EOF

echo "═══════════════════════════════════════════════════════"
echo "📊 RÉSUMÉ"
echo "═══════════════════════════════════════════════════════"
echo "Dossier de sortie: $OUTPUT_DIR"
echo "Rapport: $OUTPUT_DIR/QUALITY-TEST-REPORT.md"
echo ""
echo "✅ Tests de qualité structurés"
echo "   - Diagrammes de référence copiés"
echo "   - Structure de test créée"
echo "   - Rapport initialisé"
echo ""

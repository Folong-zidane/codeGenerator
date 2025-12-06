#!/bin/bash

# Script pour analyser les contrôleurs actifs et leurs routes

BASE_URL="http://localhost:8080"

echo "🔍 ANALYSE DES CONTRÔLEURS ACTIFS"
echo "=================================="
echo ""

# Fonction pour tester une route et analyser la réponse
test_and_analyze() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo "🧪 Test: $method $endpoint"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\nHTTP_CODE:%{http_code}" "$BASE_URL$endpoint" 2>/dev/null)
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint" 2>/dev/null)
    fi
    
    http_code=$(echo "$response" | grep "HTTP_CODE:" | cut -d: -f2)
    body=$(echo "$response" | sed '/HTTP_CODE:/d')
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ] || [ "$http_code" = "202" ]; then
        echo "   ✅ FONCTIONNE ($http_code)"
        if [[ "$body" == *"{"* ]]; then
            echo "   📄 JSON Response détectée"
        elif [[ "$body" == *"PK"* ]]; then
            echo "   📦 ZIP Response détectée"
        else
            echo "   📝 Text/HTML Response"
        fi
    elif [ "$http_code" = "404" ]; then
        echo "   ❌ NON TROUVÉ ($http_code) - Contrôleur/Route inexistant"
    elif [ "$http_code" = "500" ]; then
        echo "   💥 ERREUR SERVEUR ($http_code) - Problème d'implémentation"
    elif [ "$http_code" = "400" ]; then
        echo "   ⚠️  REQUÊTE INVALIDE ($http_code) - Paramètres incorrects"
    else
        echo "   ❓ STATUT INCONNU ($http_code)"
    fi
    echo ""
}

echo "1️⃣ CONTRÔLEURS FONCTIONNELS IDENTIFIÉS:"
echo "========================================"

# Test des contrôleurs qui fonctionnent
echo "🐛 DebugController (/api/debug/*)"
test_and_analyze "GET" "/api/debug/health"
test_and_analyze "GET" "/api/debug/languages"

echo "🔧 CodeGenerationController V1 (/api/v1/generate/*)"
test_and_analyze "POST" "/api/v1/generate/java" '{
  "projectName": "test",
  "packageName": "com.test",
  "diagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }"
}'

echo "2️⃣ CONTRÔLEURS NON FONCTIONNELS:"
echo "================================"

echo "📚 DocumentationController (/) - Routes de base non trouvées"
test_and_analyze "GET" "/"

echo "🏗️ CodeGeneratorController (/api/generate/*) - Routes comprehensive non trouvées"
test_and_analyze "GET" "/api/generate/languages"

echo "🚀 ModernGeneratorController (/api/modern/*) - Routes modern non trouvées"
test_and_analyze "GET" "/api/modern/example"

echo "⚡ OptimalGeneratorController (/api/generate) - Routes optimal non trouvées"
test_and_analyze "POST" "/api/generate" '{
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n    }",
  "packageName": "com.test",
  "language": "java"
}'

echo "📡 StreamingGenerationController (/api/v2/stream/*) - Routes streaming non trouvées"
test_and_analyze "POST" "/api/v2/stream/generate" '{
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n    }",
  "packageName": "com.test",
  "language": "java"
}'

echo "3️⃣ ANALYSE DÉTAILLÉE DES ROUTES FONCTIONNELLES:"
echo "==============================================="

echo "🔧 Test complet CodeGenerationController V1:"
for lang in java python csharp typescript php; do
    echo "   Testing $lang generation..."
    test_and_analyze "POST" "/api/v1/generate/$lang" '{
      "projectName": "test-'$lang'",
      "packageName": "com.test",
      "diagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }"
    }'
done

echo "📦 Test des téléchargements ZIP:"
test_and_analyze "POST" "/api/v1/generate/java/download" '{
  "projectName": "test-download",
  "packageName": "com.test",
  "diagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }"
}'

echo ""
echo "📊 CONCLUSION:"
echo "=============="
echo "✅ CONTRÔLEURS ACTIFS:"
echo "   - DebugController (/api/debug/*)"
echo "   - CodeGenerationController V1 (/api/v1/generate/*)"
echo ""
echo "❌ CONTRÔLEURS INACTIFS/NON CONFIGURÉS:"
echo "   - DocumentationController (/)"
echo "   - CodeGeneratorController (/api/generate/*)"
echo "   - ModernGeneratorController (/api/modern/*)"
echo "   - OptimalGeneratorController (/api/generate)"
echo "   - StreamingGenerationController (/api/v2/stream/*)"
echo ""
echo "🔍 CAUSE PROBABLE:"
echo "   Les contrôleurs existent dans le code mais ne sont pas tous"
echo "   correctement configurés ou activés dans l'application Spring Boot."
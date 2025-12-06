#!/bin/bash

# Script de test complet pour analyser toutes les routes des contrôleurs
# Port: 8080 (supposé)

BASE_URL="http://localhost:8080"
RESULTS_FILE="route-test-results.txt"

echo "🧪 ANALYSE COMPLÈTE DES ROUTES - $(date)" > $RESULTS_FILE
echo "=========================================" >> $RESULTS_FILE
echo "" >> $RESULTS_FILE

# Fonction pour tester une route
test_route() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo "Testing: $method $endpoint - $description"
    echo "🔍 $method $endpoint - $description" >> $RESULTS_FILE
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\nHTTP_CODE:%{http_code}" "$BASE_URL$endpoint" 2>/dev/null)
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint" 2>/dev/null)
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X DELETE "$BASE_URL$endpoint" 2>/dev/null)
    fi
    
    http_code=$(echo "$response" | grep "HTTP_CODE:" | cut -d: -f2)
    body=$(echo "$response" | sed '/HTTP_CODE:/d')
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ] || [ "$http_code" = "202" ]; then
        echo "   ✅ SUCCESS ($http_code)" >> $RESULTS_FILE
    elif [ "$http_code" = "404" ]; then
        echo "   ❌ NOT FOUND ($http_code)" >> $RESULTS_FILE
    elif [ "$http_code" = "500" ]; then
        echo "   💥 SERVER ERROR ($http_code)" >> $RESULTS_FILE
    elif [ "$http_code" = "400" ]; then
        echo "   ⚠️  BAD REQUEST ($http_code)" >> $RESULTS_FILE
    else
        echo "   ❓ UNKNOWN ($http_code)" >> $RESULTS_FILE
    fi
    
    if [ ${#body} -lt 200 ]; then
        echo "   Response: $body" >> $RESULTS_FILE
    else
        echo "   Response: ${body:0:100}..." >> $RESULTS_FILE
    fi
    echo "" >> $RESULTS_FILE
}

# Vérifier si le serveur est accessible
echo "🔍 Vérification de la connectivité du serveur..."
if ! curl -s "$BASE_URL" > /dev/null 2>&1; then
    echo "❌ ERREUR: Serveur non accessible sur $BASE_URL"
    echo "   Assurez-vous que l'application Spring Boot est démarrée sur le port 8080"
    exit 1
fi

echo "✅ Serveur accessible, début des tests..."
echo ""

# 1. DOCUMENTATION CONTROLLER
echo "📚 DOCUMENTATION CONTROLLER" >> $RESULTS_FILE
echo "=========================" >> $RESULTS_FILE
test_route "GET" "/" "Documentation home page"
test_route "GET" "/docs" "Documentation page"
test_route "GET" "/examples" "Examples page"

# 2. DEBUG CONTROLLER
echo "🐛 DEBUG CONTROLLER" >> $RESULTS_FILE
echo "==================" >> $RESULTS_FILE
test_route "GET" "/api/debug/health" "Health check"
test_route "GET" "/api/debug/languages" "Available languages"

# 3. CODE GENERATOR CONTROLLER (Comprehensive)
echo "🏗️ CODE GENERATOR CONTROLLER" >> $RESULTS_FILE
echo "============================" >> $RESULTS_FILE
test_route "GET" "/api/generate/languages" "Supported languages"
test_route "GET" "/api/generate/versions" "Latest versions"

# Test génération comprehensive
COMPREHENSIVE_DATA='{
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n    }",
  "sequenceDiagram": "sequenceDiagram\n    Client->>UserService: createUser()",
  "stateDiagram": "stateDiagram-v2\n    [*] --> ACTIVE",
  "packageName": "com.test",
  "language": "java"
}'
test_route "POST" "/api/generate/comprehensive" "$COMPREHENSIVE_DATA" "Comprehensive generation"

# 4. MODERN GENERATOR CONTROLLER
echo "🚀 MODERN GENERATOR CONTROLLER" >> $RESULTS_FILE
echo "==============================" >> $RESULTS_FILE
test_route "GET" "/api/modern/example" "Example request"
test_route "GET" "/api/modern/initializers/status" "Initializers status"
test_route "GET" "/api/modern/initializers/java/available" "Java framework availability"

MODERN_DATA='{
  "projectName": "test-app",
  "packageName": "com.test",
  "language": "java",
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
  "sequenceDiagram": "",
  "stateDiagram": "",
  "outputPath": "/tmp/test",
  "options": {}
}'
test_route "POST" "/api/modern/generate" "$MODERN_DATA" "Modern generation"

# 5. OPTIMAL GENERATOR CONTROLLER
echo "⚡ OPTIMAL GENERATOR CONTROLLER" >> $RESULTS_FILE
echo "===============================" >> $RESULTS_FILE

OPTIMAL_DATA='{
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
  "packageName": "com.test",
  "language": "java"
}'
test_route "POST" "/api/generate/analyze" "$OPTIMAL_DATA" "Diagram analysis"
test_route "POST" "/api/generate" "$OPTIMAL_DATA" "Optimal generation"

# 6. STREAMING GENERATION CONTROLLER
echo "📡 STREAMING GENERATION CONTROLLER" >> $RESULTS_FILE
echo "==================================" >> $RESULTS_FILE

STREAMING_DATA='{
  "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
  "sequenceDiagram": "",
  "stateDiagram": "",
  "packageName": "com.test",
  "language": "java"
}'
test_route "POST" "/api/v2/stream/generate" "$STREAMING_DATA" "Initiate streaming generation"

# Test avec un ID fictif pour les autres endpoints
test_route "GET" "/api/v2/stream/status/test-id" "Generation status"
test_route "GET" "/api/v2/stream/download/test-id" "Download generation"
test_route "DELETE" "/api/v2/stream/cleanup/test-id" "Cleanup generation"

# 7. CODE GENERATION CONTROLLER (V1)
echo "🔧 CODE GENERATION CONTROLLER V1" >> $RESULTS_FILE
echo "=================================" >> $RESULTS_FILE

V1_DATA='{
  "projectName": "test-project",
  "packageName": "com.test",
  "diagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }"
}'

test_route "POST" "/api/v1/generate/java" "$V1_DATA" "Generate Java code"
test_route "POST" "/api/v1/generate/python" "$V1_DATA" "Generate Python code"
test_route "POST" "/api/v1/generate/csharp" "$V1_DATA" "Generate C# code"
test_route "POST" "/api/v1/generate/typescript" "$V1_DATA" "Generate TypeScript code"
test_route "POST" "/api/v1/generate/php" "$V1_DATA" "Generate PHP code"

test_route "POST" "/api/v1/generate/java/download" "$V1_DATA" "Download Java ZIP"
test_route "POST" "/api/v1/generate/python/download" "$V1_DATA" "Download Python ZIP"

# 8. ROUTES COMMUNES SPRING BOOT
echo "🌱 SPRING BOOT ACTUATOR" >> $RESULTS_FILE
echo "=======================" >> $RESULTS_FILE
test_route "GET" "/actuator/health" "Actuator health"
test_route "GET" "/actuator/info" "Actuator info"

# RÉSUMÉ FINAL
echo "" >> $RESULTS_FILE
echo "📊 RÉSUMÉ DES TESTS" >> $RESULTS_FILE
echo "==================" >> $RESULTS_FILE

success_count=$(grep -c "✅ SUCCESS" $RESULTS_FILE)
error_count=$(grep -c "❌\|💥" $RESULTS_FILE)
not_found_count=$(grep -c "❌ NOT FOUND" $RESULTS_FILE)
bad_request_count=$(grep -c "⚠️  BAD REQUEST" $RESULTS_FILE)

echo "✅ Succès: $success_count" >> $RESULTS_FILE
echo "❌ Erreurs: $error_count" >> $RESULTS_FILE
echo "🔍 Non trouvées: $not_found_count" >> $RESULTS_FILE
echo "⚠️  Requêtes invalides: $bad_request_count" >> $RESULTS_FILE

echo ""
echo "🎯 ANALYSE TERMINÉE"
echo "📄 Résultats détaillés dans: $RESULTS_FILE"
echo ""
echo "📊 RÉSUMÉ:"
echo "✅ Succès: $success_count"
echo "❌ Erreurs: $error_count"
echo "🔍 Non trouvées: $not_found_count"
echo "⚠️  Requêtes invalides: $bad_request_count"

# Afficher les principales erreurs
echo ""
echo "🚨 PRINCIPALES ERREURS DÉTECTÉES:"
grep -A1 "❌\|💥" $RESULTS_FILE | head -20
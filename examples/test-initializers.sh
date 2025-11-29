#!/bin/bash

echo "🚀 Test du Système d'Initializers"
echo "=================================="

# Test des endpoints d'initializers
BASE_URL="http://localhost:8080/api/generate"

echo "1. Test des langages supportés..."
curl -s "$BASE_URL/languages" | jq '.' || echo "❌ Erreur langages"

echo -e "\n2. Test des versions..."
curl -s "$BASE_URL/versions" | jq '.' || echo "❌ Erreur versions"

echo -e "\n3. Test génération avec template Spring Boot..."
curl -X POST "$BASE_URL/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "sequenceDiagram": "sequenceDiagram\n    Client->>UserService: createUser()",
    "stateDiagram": "stateDiagram-v2\n    [*] --> ACTIVE",
    "packageName": "com.example.test",
    "language": "java"
  }' \
  -o test-spring-template.zip

if [ -f "test-spring-template.zip" ]; then
    SIZE=$(stat -c%s "test-spring-template.zip")
    echo "✅ Spring Boot généré: ${SIZE} bytes"
else
    echo "❌ Erreur génération Spring Boot"
fi

echo -e "\n4. Test génération Django..."
curl -X POST "$BASE_URL/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "sequenceDiagram": "sequenceDiagram\n    Client->>UserService: createUser()",
    "stateDiagram": "stateDiagram-v2\n    [*] --> ACTIVE",
    "packageName": "com.example.test",
    "language": "django"
  }' \
  -o test-django-template.zip

if [ -f "test-django-template.zip" ]; then
    SIZE=$(stat -c%s "test-django-template.zip")
    echo "✅ Django généré: ${SIZE} bytes"
else
    echo "❌ Erreur génération Django"
fi

echo -e "\n📊 Résumé des Tests:"
echo "==================="
ls -la test-*-template.zip 2>/dev/null || echo "Aucun fichier généré"

echo -e "\n🎯 Système d'Initializers testé!"
#!/bin/bash

echo "🔍 Test simple de l'application"

# Test avec un payload minimal
echo "☕ Test Java minimal..."
curl -X POST "http://localhost:8080/api/v1/generate/java" \
  -H "Content-Type: application/json" \
  -d '{
    "projectName": "test",
    "packageName": "com.test",
    "diagramContent": "classDiagram\n    class User {\n        +String name\n    }"
  }' \
  -v

echo ""
echo "🔍 Test avec tous les champs..."
curl -X POST "http://localhost:8080/api/v1/generate/java" \
  -H "Content-Type: application/json" \
  -d '{
    "projectName": "test-complete",
    "packageName": "com.test.complete",
    "description": "Test project",
    "diagramContent": "classDiagram\n    class User {\n        +String name\n        +String email\n    }",
    "includeTests": true,
    "generateDocs": true
  }' \
  -v
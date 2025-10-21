#!/bin/bash

echo "🧪 Testing UML Code Generator API"
echo "================================="

# Test validation endpoint
echo "📝 Testing validation..."
curl -X POST http://localhost:8080/api/generate/validate \
  -H "Content-Type: text/plain" \
  -d "classDiagram
    class User {
        +UUID id
        +String username
    }" \
  --silent | jq '.'

echo -e "\n🔧 Testing code generation..."
curl -X POST http://localhost:8080/api/generate/crud \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n    }\n    class Order {\n        +UUID id\n        +Float totalAmount\n    }\n    User \"1\" --> \"*\" Order",
    "packageName": "com.test"
  }' \
  --output generated-test.zip

if [ -f "generated-test.zip" ]; then
    echo "✅ Code generation successful - generated-test.zip created"
    unzip -l generated-test.zip | head -20
else
    echo "❌ Code generation failed"
fi
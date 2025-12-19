#!/bin/bash

# 🧪 Test du nouveau système de génération unifié

echo "🚀 Testing Unified Diagram Generation System"
echo "============================================="

# Configuration
BACKEND_URL="http://localhost:8080"
TEST_DIR="./test-diagrams"

# Créer le répertoire de test
mkdir -p "$TEST_DIR"

# 1. Test Class Diagram
echo "📊 Testing Class Diagram Generation..."
cat > "$TEST_DIR/class-test.json" << 'EOF'
{
  "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +login()\n        +logout()\n    }\n    class Post {\n        +UUID id\n        +String title\n        +UUID userId\n        +publish()\n    }\n    User \"1\" --> \"*\" Post",
  "language": "java",
  "packageName": "com.test.unified",
  "projectName": "class-test"
}
EOF

curl -X POST "$BACKEND_URL/api/unified/validate" \
  -H "Content-Type: application/json" \
  -d @"$TEST_DIR/class-test.json" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'

# 2. Test Multiple Diagrams
echo -e "\n📈 Testing Multiple Diagrams Generation..."
cat > "$TEST_DIR/multi-test.json" << 'EOF'
{
  "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +login()\n    }",
  "sequenceDiagramContent": "sequenceDiagram\n    participant U as User\n    participant S as System\n    U->>S: login()\n    S-->>U: success",
  "stateDiagramContent": "stateDiagram-v2\n    [*] --> Active\n    Active --> Inactive\n    Inactive --> [*]",
  "activityDiagramContent": "flowchart TD\n    A[Start] --> B[Process]\n    B --> C[End]",
  "language": "java",
  "packageName": "com.test.multi",
  "projectName": "multi-test"
}
EOF

curl -X POST "$BACKEND_URL/api/unified/validate" \
  -H "Content-Type: application/json" \
  -d @"$TEST_DIR/multi-test.json" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'

# 3. Test Health Check
echo -e "\n🏥 Testing Health Check..."
curl -X GET "$BACKEND_URL/api/unified/health" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s | jq '.'

# 4. Test Different Languages
echo -e "\n🌍 Testing Different Languages..."
for lang in "java" "python" "csharp" "typescript" "php"; do
    echo "Testing $lang..."
    
    cat > "$TEST_DIR/lang-$lang.json" << EOF
{
  "classDiagramContent": "classDiagram\n    class Product {\n        +UUID id\n        +String name\n        +Double price\n    }",
  "language": "$lang",
  "packageName": "com.test.$lang",
  "projectName": "test-$lang"
}
EOF
    
    curl -X POST "$BACKEND_URL/api/unified/validate" \
      -H "Content-Type: application/json" \
      -d @"$TEST_DIR/lang-$lang.json" \
      -w "\nStatus: %{http_code}\n" \
      -s | jq '.valid'
done

# 5. Test ZIP Generation (si le serveur est disponible)
echo -e "\n📦 Testing ZIP Generation..."
if curl -s "$BACKEND_URL/api/unified/health" > /dev/null; then
    echo "Server is available, testing ZIP generation..."
    
    curl -X POST "$BACKEND_URL/api/unified/generate/zip" \
      -H "Content-Type: application/json" \
      -d @"$TEST_DIR/class-test.json" \
      -o "$TEST_DIR/generated-test.zip" \
      -w "\nZIP Status: %{http_code}\n"
    
    if [ -f "$TEST_DIR/generated-test.zip" ]; then
        echo "✅ ZIP file generated successfully!"
        unzip -l "$TEST_DIR/generated-test.zip" | head -20
    else
        echo "❌ ZIP generation failed"
    fi
else
    echo "⚠️ Server not available, skipping ZIP test"
fi

# Nettoyage
echo -e "\n🧹 Cleaning up test files..."
rm -rf "$TEST_DIR"

echo -e "\n✅ Unified Generation System Test Complete!"
echo "============================================="
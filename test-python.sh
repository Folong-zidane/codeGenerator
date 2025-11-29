#!/bin/bash

echo "🐍 Testing Python Generation..."

echo "📝 Test 1: Simple User Model"
curl -X POST http://localhost:8080/api/generate/comprehensive \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "class User { +Long id +String username +String email }",
    "packageName": "users",
    "language": "python"
  }' | jq '.files."models/User.py"' 2>/dev/null || echo "❌ Error in Test 1"

echo -e "\n📝 Test 2: E-commerce with State Management"
curl -X POST http://localhost:8080/api/generate/comprehensive \
  -H "Content-Type: application/json" \
  -d @examples/python-ecommerce.json | jq '.files."models/Order.py"' 2>/dev/null || echo "❌ Error in Test 2"

echo -e "\n📝 Test 3: Minimal Model"
curl -X POST http://localhost:8080/api/generate/comprehensive \
  -H "Content-Type: application/json" \
  -d @examples/python-minimal.json | jq '.files."models/Task.py"' 2>/dev/null || echo "❌ Error in Test 3"

echo -e "\n✅ Python generation tests completed!"
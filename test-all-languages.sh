#!/bin/bash

# Test script for all supported languages
echo "🚀 Testing code generation for all supported languages..."

API_URL="http://localhost:8080/api/generate/comprehensive"

# Test C#
echo "📝 Testing C# generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/csharp-ecommerce.json \
  -o csharp-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ C# generation successful"
else
    echo "❌ C# generation failed"
fi

# Test TypeScript
echo "📝 Testing TypeScript generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/typescript-ecommerce.json \
  -o typescript-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ TypeScript generation successful"
else
    echo "❌ TypeScript generation failed"
fi

# Test PHP
echo "📝 Testing PHP generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/php-ecommerce.json \
  -o php-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ PHP generation successful"
else
    echo "❌ PHP generation failed"
fi

# Test Django
echo "📝 Testing Django generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/django-ecommerce.json \
  -o django-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ Django generation successful"
else
    echo "❌ Django generation failed"
fi

# Test existing Java
echo "📝 Testing Java generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/ecommerce.json \
  -o java-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ Java generation successful"
else
    echo "❌ Java generation failed"
fi

# Test existing Python
echo "📝 Testing Python generation..."
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d @examples/python-ecommerce.json \
  -o python-test-output.zip

if [ $? -eq 0 ]; then
    echo "✅ Python generation successful"
else
    echo "❌ Python generation failed"
fi

echo "🎯 All language tests completed!"
echo "Generated files:"
ls -la *-test-output.zip 2>/dev/null || echo "No output files found"
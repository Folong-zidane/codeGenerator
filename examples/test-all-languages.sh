#!/bin/bash

echo "🚀 Testing All Language Generators - Final Validation"
echo "====================================================="

BASE_URL="http://localhost:8080"

# Test all supported languages
languages=("java" "django" "python" "csharp" "typescript" "php")

for lang in "${languages[@]}"; do
    echo -e "\n📋 Testing $lang..."
    
    curl -X POST "$BASE_URL/api/generate/comprehensive" \
      -H "Content-Type: application/json" \
      -d "{
        \"classDiagram\": \"classDiagram\\n    class User {\\n        +UUID id\\n        +String username\\n        +String email\\n        +UserStatus status\\n    }\",
        \"packageName\": \"com.example.$lang\",
        \"language\": \"$lang\"
      }" \
      -o "test-$lang.zip" -s
    
    if [ -f "test-$lang.zip" ]; then
        size=$(stat -c%s "test-$lang.zip")
        if [ $size -gt 1000 ]; then
            echo "✅ $lang: SUCCESS ($size bytes)"
        else
            echo "❌ $lang: FAILED ($(head -1 test-$lang.zip))"
        fi
    else
        echo "❌ $lang: NO FILE GENERATED"
    fi
done

echo -e "\n🎉 Final Results:"
ls -la test-*.zip | awk '{print $9 ": " $5 " bytes"}'
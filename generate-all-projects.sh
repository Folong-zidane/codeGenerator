#!/bin/bash

API_URL="https://codegenerator-cpyh.onrender.com"
UML_FILE="$1"
PACKAGE_NAME="${2:-com.example}"
OUTPUT_BASE="${3:-./generated}"

if [ -z "$UML_FILE" ]; then
    echo "Usage: $0 <uml_file> [package_name] [output_base_dir]"
    echo "Example: $0 diagram.mermaid com.myapp ./projects"
    exit 1
fi

if [ ! -f "$UML_FILE" ]; then
    echo "Error: UML file '$UML_FILE' not found"
    exit 1
fi

UML_CONTENT=$(cat "$UML_FILE" | jq -Rs .)

echo "🚀 Generating projects for all frameworks..."

# 1. Java Spring Boot (ZIP)
echo "📦 Generating Java Spring Boot..."
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "{\"umlContent\": $UML_CONTENT, \"packageName\": \"$PACKAGE_NAME\"}" \
    -o "$OUTPUT_BASE/java-springboot.zip"

# 2. Python FastAPI
echo "🐍 Generating Python FastAPI..."
curl -s -X POST "$API_URL/api/v2/generate/files" \
    -H "Content-Type: application/json" \
    -d "{
      \"umlContent\": $UML_CONTENT,
      \"packageName\": \"$PACKAGE_NAME\",
      \"language\": \"python\",
      \"framework\": \"fastapi\",
      \"outputDirectory\": \"$OUTPUT_BASE/python-fastapi\"
    }"

# 3. Python Django
echo "🐍 Generating Python Django..."
curl -s -X POST "$API_URL/api/v2/generate/files" \
    -H "Content-Type: application/json" \
    -d "{
      \"umlContent\": $UML_CONTENT,
      \"packageName\": \"$PACKAGE_NAME\",
      \"language\": \"python\",
      \"framework\": \"django\",
      \"outputDirectory\": \"$OUTPUT_BASE/python-django\"
    }"

# 4. Python Flsfask
echo "🐍 Generating Python Flask..."
curl -s -X POST "$API_URL/api/v2/generate/files" \
    -H "Content-Type: application/json" \
    -d "{
      \"umlContent\": $UML_CONTENT,
      \"packageName\": \"$PACKAGE_NAME\",
      \"language\": \"python\",
      \"framework\": \"flask\",
      \"outputDirectory\": \"$OUTPUT_BASE/python-flask\"
    }"

# 5. C# .NET
echo "⚡ Generating C# .NET..."
curl -s -X POST "$API_URL/api/v2/generate/files" \
    -H "Content-Type: application/json" \
    -d "{
      \"umlContent\": $UML_CONTENT,
      \"packageName\": \"$PACKAGE_NAME\",
      \"language\": \"csharp\",
      \"framework\": \"dotnet\",
      \"outputDirectory\": \"$OUTPUT_BASE/csharp-dotnet\"
    }"

echo "✅ All projects generated in: $OUTPUT_BASE"
echo ""
echo "📁 Generated projects:"
echo "  - Java Spring Boot: $OUTPUT_BASE/java-springboot.zip"
echo "  - Python FastAPI: $OUTPUT_BASE/python-fastapi/"
echo "  - Python Django: $OUTPUT_BASE/python-django/"
echo "  - Python Flask: $OUTPUT_BASE/python-flask/"
echo "  - C# .NET: $OUTPUT_BASE/csharp-dotnet/"
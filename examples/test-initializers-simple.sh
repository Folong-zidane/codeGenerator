#!/bin/bash

echo "🧪 Test Simple des Initializers"
echo "==============================="

# Test direct des initializers
cd /home/folongzidane/Documents/Projet/basicCode

echo "1. Test compilation..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur de compilation"
    exit 1
fi

echo -e "\n2. Test des initializers disponibles..."
echo "Languages supportés:"
echo "- java (Spring Boot)"
echo "- django (Django REST)"
echo "- python (FastAPI)"
echo "- typescript (Express)"
echo "- php (Laravel)"

echo -e "\n3. Test génération avec chaque initializer..."

# Test Java
echo "🔸 Test Java/Spring Boot..."
curl -X POST "http://localhost:8080/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example.test",
    "language": "java"
  }' \
  -o test-java-init.zip 2>/dev/null

if [ -f "test-java-init.zip" ]; then
    SIZE=$(stat -c%s "test-java-init.zip")
    echo "✅ Java: ${SIZE} bytes"
else
    echo "❌ Java: Erreur"
fi

# Test Django
echo "🔸 Test Django..."
curl -X POST "http://localhost:8080/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example.test",
    "language": "django"
  }' \
  -o test-django-init.zip 2>/dev/null

if [ -f "test-django-init.zip" ]; then
    SIZE=$(stat -c%s "test-django-init.zip")
    echo "✅ Django: ${SIZE} bytes"
else
    echo "❌ Django: Erreur"
fi

# Test PHP
echo "🔸 Test PHP/Laravel..."
curl -X POST "http://localhost:8080/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example.test",
    "language": "php"
  }' \
  -o test-php-init.zip 2>/dev/null

if [ -f "test-php-init.zip" ]; then
    SIZE=$(stat -c%s "test-php-init.zip")
    echo "✅ PHP: ${SIZE} bytes"
else
    echo "❌ PHP: Erreur"
fi

echo -e "\n📊 Résumé:"
echo "=========="
ls -la test-*-init.zip 2>/dev/null | awk '{print $9 ": " $5 " bytes"}' || echo "Aucun fichier généré"

echo -e "\n🎯 Test des initializers terminé!"
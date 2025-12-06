#!/bin/bash

# Script de test pour tous les langages supportés
# Suppose que l'application tourne sur le port 8080

BASE_URL="http://localhost:8080"
OUTPUT_DIR="./test-results-$(date +%Y%m%d-%H%M%S)"

# Créer le répertoire de sortie
mkdir -p "$OUTPUT_DIR"

echo "🚀 Test de génération pour tous les langages"
echo "📁 Résultats dans: $OUTPUT_DIR"
echo "🌐 URL de base: $BASE_URL"
echo ""

# Diagramme UML de test simple
UML_CONTENT='classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +String password
        +UserStatus status
        +validateEmail()
        +changePassword()
    }
    class Product {
        +UUID id
        +String name
        +String description
        +Float price
        +Integer stock
        +ProductStatus status
        +updateStock()
        +applyDiscount()
    }
    class Order {
        +UUID id
        +UUID userId
        +UUID productId
        +Integer quantity
        +Float total
        +OrderStatus status
        +calculateTotal()
        +processPayment()
    }
    User "1" --> "*" Order : places
    Product "1" --> "*" Order : contains'

# Test de santé de l'API
echo "🔍 Test de santé de l'API..."
curl -s "$BASE_URL/actuator/health" | jq . || echo "❌ API non disponible"
echo ""

# 1. Test Java Spring Boot
echo "☕ Test Java Spring Boot..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"com.example.ecommerce\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 2. Test Python FastAPI
echo "🐍 Test Python FastAPI..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce_api\",
    \"language\": \"python\"
  }" \
  -o "$OUTPUT_DIR/python-fastapi-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 3. Test Django
echo "🎸 Test Django..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce_django\",
    \"language\": \"django\"
  }" \
  -o "$OUTPUT_DIR/django-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 4. Test C# .NET
echo "🔷 Test C# .NET..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"EcommerceApi\",
    \"language\": \"csharp\"
  }" \
  -o "$OUTPUT_DIR/csharp-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 5. Test TypeScript Node.js
echo "📘 Test TypeScript Node.js..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce-api\",
    \"language\": \"typescript\"
  }" \
  -o "$OUTPUT_DIR/typescript-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 6. Test PHP
echo "🐘 Test PHP..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"EcommerceApi\",
    \"language\": \"php\"
  }" \
  -o "$OUTPUT_DIR/php-ecommerce.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

echo ""
echo "🎯 Tests de génération comportementale..."

# Diagramme de séquence pour les tests comportementaux
SEQUENCE_CONTENT='sequenceDiagram
    participant Client
    participant UserController
    participant UserService
    participant UserRepository
    participant Database
    
    Client->>UserController: POST /api/users/register
    UserController->>UserService: createUser(userData)
    UserService->>UserService: validateEmail(email)
    UserService->>UserRepository: findByEmail(email)
    UserRepository->>Database: SELECT * FROM users WHERE email = ?
    Database-->>UserRepository: null
    UserRepository-->>UserService: null
    UserService->>UserRepository: save(user)
    UserRepository->>Database: INSERT INTO users
    Database-->>UserRepository: User created
    UserRepository-->>UserService: User
    UserService-->>UserController: User
    UserController-->>Client: 201 Created'

# 7. Test génération comportementale Java
echo "🧠 Test génération comportementale Java..."
curl -X POST "$BASE_URL/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"packageName\": \"com.example.behavioral\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-behavioral.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 8. Test génération comportementale Python
echo "🧠 Test génération comportementale Python..."
curl -X POST "$BASE_URL/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"packageName\": \"behavioral_api\",
    \"language\": \"python\"
  }" \
  -o "$OUTPUT_DIR/python-behavioral.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

echo ""
echo "🔥 Tests de génération complète (avec états)..."

# Diagramme d'état
STATE_CONTENT='stateDiagram-v2
    [*] --> INACTIVE
    INACTIVE --> ACTIVE : activate()
    ACTIVE --> SUSPENDED : suspend()
    SUSPENDED --> ACTIVE : reactivate()
    ACTIVE --> INACTIVE : deactivate()
    SUSPENDED --> INACTIVE : deactivate()'

# 9. Test génération complète Java
echo "🔥 Test génération complète Java..."
curl -X POST "$BASE_URL/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"stateDiagramContent\": \"$STATE_CONTENT\",
    \"packageName\": \"com.example.comprehensive\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-comprehensive.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

# 10. Test génération complète Django
echo "🔥 Test génération complète Django..."
curl -X POST "$BASE_URL/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"stateDiagramContent\": \"$STATE_CONTENT\",
    \"packageName\": \"comprehensive_django\",
    \"language\": \"django\"
  }" \
  -o "$OUTPUT_DIR/django-comprehensive.zip" \
  --write-out "Status: %{http_code}, Temps: %{time_total}s\n"

echo ""
echo "📊 Analyse des résultats..."

# Analyser les fichiers générés
echo "📁 Fichiers générés:"
ls -lh "$OUTPUT_DIR"/*.zip 2>/dev/null || echo "Aucun fichier ZIP généré"

echo ""
echo "📈 Tailles des fichiers:"
for file in "$OUTPUT_DIR"/*.zip; do
    if [ -f "$file" ]; then
        size=$(stat -c%s "$file" 2>/dev/null || echo "0")
        if [ "$size" -gt 1000 ]; then
            echo "✅ $(basename "$file"): ${size} bytes"
        else
            echo "❌ $(basename "$file"): ${size} bytes (trop petit)"
        fi
    fi
done

echo ""
echo "🔍 Test des endpoints de validation..."

# Test validation UML
echo "📝 Test validation UML..."
curl -X POST "$BASE_URL/api/generate/validate" \
  -H "Content-Type: text/plain" \
  -d "$UML_CONTENT" \
  --write-out "Status: %{http_code}\n"

# Test validation comportementale
echo "🧠 Test validation comportementale..."
curl -X POST "$BASE_URL/api/behavioral/validate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\"
  }" \
  --write-out "Status: %{http_code}\n"

echo ""
echo "🎉 Tests terminés!"
echo "📁 Résultats disponibles dans: $OUTPUT_DIR"
echo ""
echo "🚀 Pour extraire et tester un projet:"
echo "   cd $OUTPUT_DIR"
echo "   unzip java-ecommerce.zip"
echo "   cd java-ecommerce"
echo "   ./start.sh"
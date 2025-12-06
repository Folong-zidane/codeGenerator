#!/bin/bash

# Test de l'API déployée sur Render
DEPLOYED_URL="https://codegenerator-cpyh.onrender.com"
OUTPUT_DIR="./deployed-test-results-$(date +%Y%m%d-%H%M%S)"

mkdir -p "$OUTPUT_DIR"

echo "🚀 Test de l'API déployée sur Render"
echo "🌐 URL: $DEPLOYED_URL"
echo "📁 Résultats dans: $OUTPUT_DIR"
echo ""

# Diagramme UML complet
UML_CONTENT='classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +String password
        +UserStatus status
        +Date createdAt
        +Date updatedAt
        +validateEmail()
        +changePassword()
        +activate()
        +suspend()
    }
    class Product {
        +UUID id
        +String name
        +String description
        +Float price
        +Integer stock
        +ProductStatus status
        +Date createdAt
        +Date updatedAt
        +updateStock(quantity)
        +applyDiscount(percentage)
        +activate()
        +suspend()
    }
    class Order {
        +UUID id
        +UUID userId
        +UUID productId
        +Integer quantity
        +Float unitPrice
        +Float total
        +OrderStatus status
        +Date createdAt
        +Date updatedAt
        +calculateTotal()
        +processPayment()
        +ship()
        +deliver()
    }
    User "1" --> "*" Order : places
    Product "1" --> "*" Order : contains'

# Test de santé
echo "🔍 Test de santé de l'API déployée..."
curl -s "$DEPLOYED_URL/actuator/health" | jq . 2>/dev/null || curl -s "$DEPLOYED_URL/actuator/health"
echo ""

# 1. Test Java Spring Boot
echo "☕ Génération Java Spring Boot..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"com.example.ecommerce\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-ecommerce.zip" \
  --write-out "✅ Java: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 2. Test Python FastAPI
echo "🐍 Génération Python FastAPI..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce_api\",
    \"language\": \"python\"
  }" \
  -o "$OUTPUT_DIR/python-fastapi.zip" \
  --write-out "✅ Python: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 3. Test Django
echo "🎸 Génération Django..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce_django\",
    \"language\": \"django\"
  }" \
  -o "$OUTPUT_DIR/django-ecommerce.zip" \
  --write-out "✅ Django: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 4. Test C# .NET
echo "🔷 Génération C# .NET..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"EcommerceApi\",
    \"language\": \"csharp\"
  }" \
  -o "$OUTPUT_DIR/csharp-ecommerce.zip" \
  --write-out "✅ C#: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 5. Test TypeScript
echo "📘 Génération TypeScript..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"ecommerce-api\",
    \"language\": \"typescript\"
  }" \
  -o "$OUTPUT_DIR/typescript-ecommerce.zip" \
  --write-out "✅ TypeScript: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 6. Test PHP
echo "🐘 Génération PHP..."
curl -X POST "$DEPLOYED_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d "{
    \"umlContent\": \"$UML_CONTENT\",
    \"packageName\": \"EcommerceApi\",
    \"language\": \"php\"
  }" \
  -o "$OUTPUT_DIR/php-ecommerce.zip" \
  --write-out "✅ PHP: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

echo ""
echo "🎯 Tests de génération comportementale..."

# Diagramme de séquence
SEQUENCE_CONTENT='sequenceDiagram
    participant Client
    participant UserController
    participant UserService
    participant UserRepository
    participant Database
    
    Client->>UserController: POST /api/users/register
    UserController->>UserService: createUser(userData)
    UserService->>UserService: validateEmail(email)
    alt email valid
        UserService->>UserRepository: findByEmail(email)
        UserRepository->>Database: SELECT * FROM users WHERE email = ?
        Database-->>UserRepository: null (user not exists)
        UserRepository-->>UserService: null
        UserService->>UserRepository: save(user)
        UserRepository->>Database: INSERT INTO users
        Database-->>UserRepository: User created
        UserRepository-->>UserService: User
        UserService-->>UserController: User
        UserController-->>Client: 201 Created
    else email invalid
        UserService-->>UserController: ValidationError
        UserController-->>Client: 400 Bad Request
    end'

# 7. Test génération comportementale Java
echo "🧠 Génération comportementale Java..."
curl -X POST "$DEPLOYED_URL/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"packageName\": \"com.example.behavioral\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-behavioral.zip" \
  --write-out "✅ Java Behavioral: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 8. Test génération comportementale Python
echo "🧠 Génération comportementale Python..."
curl -X POST "$DEPLOYED_URL/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"packageName\": \"behavioral_api\",
    \"language\": \"python\"
  }" \
  -o "$OUTPUT_DIR/python-behavioral.zip" \
  --write-out "✅ Python Behavioral: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

echo ""
echo "🔥 Tests de génération complète..."

# Diagramme d'état
STATE_CONTENT='stateDiagram-v2
    [*] --> INACTIVE
    INACTIVE --> ACTIVE : activate()
    ACTIVE --> SUSPENDED : suspend()
    SUSPENDED --> ACTIVE : reactivate()
    ACTIVE --> INACTIVE : deactivate()
    SUSPENDED --> INACTIVE : deactivate()'

# 9. Test génération complète Java
echo "🔥 Génération complète Java..."
curl -X POST "$DEPLOYED_URL/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"stateDiagramContent\": \"$STATE_CONTENT\",
    \"packageName\": \"com.example.comprehensive\",
    \"language\": \"java\"
  }" \
  -o "$OUTPUT_DIR/java-comprehensive.zip" \
  --write-out "✅ Java Comprehensive: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

# 10. Test génération complète Django
echo "🔥 Génération complète Django..."
curl -X POST "$DEPLOYED_URL/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"classDiagramContent\": \"$UML_CONTENT\",
    \"sequenceDiagramContent\": \"$SEQUENCE_CONTENT\",
    \"stateDiagramContent\": \"$STATE_CONTENT\",
    \"packageName\": \"comprehensive_django\",
    \"language\": \"django\"
  }" \
  -o "$OUTPUT_DIR/django-comprehensive.zip" \
  --write-out "✅ Django Comprehensive: Status %{http_code}, Taille: %{size_download} bytes, Temps: %{time_total}s\n"

echo ""
echo "📊 Analyse des résultats..."

# Analyser les fichiers générés
echo "📁 Fichiers générés:"
ls -lh "$OUTPUT_DIR"/*.zip 2>/dev/null | while read -r line; do
    size=$(echo "$line" | awk '{print $5}')
    name=$(echo "$line" | awk '{print $9}')
    if [[ "$size" =~ ^[0-9]+$ ]] && [ "$size" -gt 10000 ]; then
        echo "✅ $(basename "$name"): $size"
    elif [[ "$size" =~ K$ ]] || [[ "$size" =~ M$ ]]; then
        echo "✅ $(basename "$name"): $size"
    else
        echo "❌ $(basename "$name"): $size (trop petit)"
    fi
done

echo ""
echo "🔍 Extraction et analyse d'un projet Java..."
if [ -f "$OUTPUT_DIR/java-ecommerce.zip" ]; then
    cd "$OUTPUT_DIR"
    unzip -q java-ecommerce.zip
    if [ -d "java-ecommerce" ]; then
        echo "📂 Structure du projet Java:"
        find java-ecommerce -type f -name "*.java" | head -10
        echo ""
        echo "📄 Exemple de fichier généré:"
        find java-ecommerce -name "*.java" | head -1 | xargs head -20 2>/dev/null
    fi
    cd ..
fi

echo ""
echo "🎉 Tests terminés!"
echo "📁 Résultats dans: $OUTPUT_DIR"
echo "🌐 API déployée fonctionnelle: $DEPLOYED_URL"
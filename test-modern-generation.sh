#!/bin/bash

echo "🚀 Test de la Génération Moderne avec Initialiseurs Natifs"
echo "=========================================================="

# Configuration
API_URL="http://localhost:8080"
OUTPUT_DIR="/tmp/modern-generated"

# Créer le répertoire de sortie
mkdir -p "$OUTPUT_DIR"

echo ""
echo "1️⃣ Vérification du statut des initialiseurs..."
curl -s "$API_URL/api/modern/initializers/status" | jq '.'

echo ""
echo "2️⃣ Test de disponibilité Spring Boot..."
curl -s "$API_URL/api/modern/initializers/spring_boot/available" | jq '.'

echo ""
echo "3️⃣ Génération d'un projet Spring Boot moderne..."

# Données de test pour Spring Boot
SPRING_BOOT_REQUEST='{
  "projectName": "modern-ecommerce",
  "packageName": "com.example.ecommerce",
  "language": "java",
  "classDiagram": "classDiagram\n    class User {\n        +Long id\n        +String username\n        +String email\n        +UserStatus status\n    }\n    class Product {\n        +Long id\n        +String name\n        +Float price\n        +ProductStatus status\n    }\n    class Order {\n        +Long id\n        +Long userId\n        +Float total\n        +OrderStatus status\n    }\n    User \"1\" --> \"*\" Order\n    Product \"*\" --> \"*\" Order",
  "sequenceDiagram": "sequenceDiagram\n    Client->>UserController: POST /api/users/register\n    UserController->>UserService: createUser(userData)\n    UserService->>UserRepository: save(user)\n    UserRepository-->>UserService: User created\n    UserService-->>UserController: Success\n    UserController-->>Client: 201 Created",
  "stateDiagram": "stateDiagram-v2\n    [*] --> INACTIVE\n    INACTIVE --> ACTIVE : activate()\n    ACTIVE --> SUSPENDED : suspend()\n    SUSPENDED --> ACTIVE : reactivate()\n    ACTIVE --> INACTIVE : deactivate()",
  "outputPath": "'$OUTPUT_DIR'",
  "options": {
    "javaVersion": "17",
    "springBootVersion": "3.2.0"
  }
}'

curl -X POST "$API_URL/api/modern/generate" \
  -H "Content-Type: application/json" \
  -d "$SPRING_BOOT_REQUEST" | jq '.'

echo ""
echo "4️⃣ Test de génération Django..."

DJANGO_REQUEST='{
  "projectName": "modern-blog",
  "packageName": "com.example.blog",
  "language": "django",
  "classDiagram": "classDiagram\n    class Author {\n        +Long id\n        +String name\n        +String email\n    }\n    class Post {\n        +Long id\n        +String title\n        +String content\n        +Long authorId\n        +PostStatus status\n    }\n    Author \"1\" --> \"*\" Post",
  "sequenceDiagram": "sequenceDiagram\n    Client->>PostController: POST /api/posts\n    PostController->>PostService: createPost(postData)\n    PostService->>PostRepository: save(post)",
  "stateDiagram": "stateDiagram-v2\n    [*] --> DRAFT\n    DRAFT --> PUBLISHED : publish()\n    PUBLISHED --> ARCHIVED : archive()",
  "outputPath": "'$OUTPUT_DIR'",
  "options": {
    "pythonVersion": "3.11"
  }
}'

curl -X POST "$API_URL/api/modern/generate" \
  -H "Content-Type: application/json" \
  -d "$DJANGO_REQUEST" | jq '.'

echo ""
echo "5️⃣ Test de génération FastAPI..."

FASTAPI_REQUEST='{
  "projectName": "modern-api",
  "packageName": "com.example.api",
  "language": "python",
  "classDiagram": "classDiagram\n    class Task {\n        +Long id\n        +String title\n        +String description\n        +TaskStatus status\n    }",
  "sequenceDiagram": "sequenceDiagram\n    Client->>TaskController: POST /api/tasks\n    TaskController->>TaskService: createTask(taskData)",
  "stateDiagram": "stateDiagram-v2\n    [*] --> TODO\n    TODO --> IN_PROGRESS : start()\n    IN_PROGRESS --> DONE : complete()",
  "outputPath": "'$OUTPUT_DIR'",
  "options": {}
}'

curl -X POST "$API_URL/api/modern/generate" \
  -H "Content-Type: application/json" \
  -d "$FASTAPI_REQUEST" | jq '.'

echo ""
echo "6️⃣ Vérification des projets générés..."
echo "Projets dans $OUTPUT_DIR:"
ls -la "$OUTPUT_DIR/"

echo ""
echo "7️⃣ Structure du projet Spring Boot moderne:"
if [ -d "$OUTPUT_DIR/modern-ecommerce" ]; then
    echo "✅ Projet Spring Boot généré avec succès!"
    tree "$OUTPUT_DIR/modern-ecommerce" -L 3 2>/dev/null || find "$OUTPUT_DIR/modern-ecommerce" -type d | head -20
    
    echo ""
    echo "📄 Contenu du README.md:"
    cat "$OUTPUT_DIR/modern-ecommerce/README.md" 2>/dev/null || echo "README.md non trouvé"
    
    echo ""
    echo "🚀 Script de démarrage:"
    cat "$OUTPUT_DIR/modern-ecommerce/start.sh" 2>/dev/null || echo "start.sh non trouvé"
else
    echo "❌ Projet Spring Boot non généré"
fi

echo ""
echo "8️⃣ Comparaison avec l'ancienne méthode..."
echo "Avantages de la nouvelle approche:"
echo "✅ Structure de projet toujours à jour"
echo "✅ Dépendances modernes automatiques"
echo "✅ Configuration optimale du framework"
echo "✅ Compatibilité avec les outils natifs"
echo "✅ Pas d'obsolescence lors des évolutions"

echo ""
echo "🎉 Test de génération moderne terminé!"
echo "Les projets sont disponibles dans: $OUTPUT_DIR"
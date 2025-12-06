#!/bin/bash

echo "🧪 Test Extension VSCode - basicCode Generator"
echo "============================================="

# Créer projet de test
TEST_DIR="/tmp/test-basiccode-extension"
echo "📁 Création du projet de test dans $TEST_DIR"

rm -rf "$TEST_DIR"
mkdir -p "$TEST_DIR/src/diagrams"

# Créer diagramme de test
cat > "$TEST_DIR/src/diagrams/class.mmd" << 'EOF'
classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +validateEmail()
    }
    class Order {
        +UUID id
        +UUID userId
        +Float total
        +OrderStatus status
    }
    User "1" --> "*" Order
EOF

cat > "$TEST_DIR/src/diagrams/sequence.mmd" << 'EOF'
sequenceDiagram
    Client->>UserController: POST /api/users
    UserController->>UserService: createUser(userData)
    UserService->>UserRepository: save(user)
    UserRepository-->>UserService: User created
    UserService-->>UserController: Success
    UserController-->>Client: 201 Created
EOF

echo "✅ Diagrammes créés :"
echo "   - $TEST_DIR/src/diagrams/class.mmd"
echo "   - $TEST_DIR/src/diagrams/sequence.mmd"

echo ""
echo "🚀 PRÊT POUR LE TEST !"
echo ""
echo "📋 Étapes suivantes :"
echo "1. Ouvrir VSCode : code '$TEST_DIR'"
echo "2. Configurer l'extension :"
echo "   - Ctrl+, → Rechercher 'basiccode'"
echo "   - Backend: https://codegenerator-cpyh.onrender.com"
echo "   - Language: java"
echo "   - Package: com.example.test"
echo "3. Générer le projet : Ctrl+Shift+G"
echo ""
echo "🎯 Résultat attendu :"
echo "   - Projet Spring Boot complet généré"
echo "   - Entités User et Order"
echo "   - Controllers, Services, Repositories"
echo "   - Configuration base de données"
echo ""

# Ouvrir automatiquement VSCode
if command -v code &> /dev/null; then
    echo "🔧 Ouverture automatique de VSCode..."
    code "$TEST_DIR"
else
    echo "⚠️  VSCode CLI non trouvé. Ouvrez manuellement : code '$TEST_DIR'"
fi
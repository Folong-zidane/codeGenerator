#!/bin/bash

# 🔨 Build et test de l'extension VSCode v1.3.0

echo "🚀 Building basicCode Generator Extension v1.3.0"
echo "================================================="

cd "$(dirname "$0")"

# 1. Vérifier les dépendances
echo "📦 Checking dependencies..."
if ! command -v npm &> /dev/null; then
    echo "❌ npm not found. Please install Node.js"
    exit 1
fi

if ! command -v vsce &> /dev/null; then
    echo "📥 Installing vsce..."
    npm install -g @vscode/vsce
fi

# 2. Installer les dépendances du projet
echo "📥 Installing project dependencies..."
npm install

# 3. Compiler TypeScript
echo "🔨 Compiling TypeScript..."
npm run compile

if [ $? -ne 0 ]; then
    echo "❌ TypeScript compilation failed"
    exit 1
fi

# 4. Construire l'extension
echo "📦 Building extension package..."
vsce package --out basiccode-generator-1.3.0.vsix

if [ $? -ne 0 ]; then
    echo "❌ Extension packaging failed"
    exit 1
fi

echo "✅ Extension built successfully: basiccode-generator-1.3.0.vsix"

# 5. Vérifier le contenu du package
echo "📋 Package contents:"
vsce ls

# 6. Test de l'extension (si VSCode est disponible)
if command -v code &> /dev/null; then
    echo "🧪 Testing extension installation..."
    
    # Désinstaller l'ancienne version
    code --uninstall-extension basiccode-generator 2>/dev/null || true
    
    # Installer la nouvelle version
    code --install-extension basiccode-generator-1.3.0.vsix
    
    if [ $? -eq 0 ]; then
        echo "✅ Extension installed successfully in VSCode"
        echo "🎯 You can now test the extension with Ctrl+Shift+G"
    else
        echo "⚠️ Extension installation failed, but package was built"
    fi
else
    echo "⚠️ VSCode not found, skipping installation test"
fi

# 7. Créer un projet de test
echo "🧪 Creating test project..."
TEST_DIR="./test-project"
mkdir -p "$TEST_DIR/src/diagrams"

# Créer des diagrammes de test
cat > "$TEST_DIR/src/diagrams/class-diagram.mmd" << 'EOF'
classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +Date createdAt
        +login()
        +logout()
    }
    class Product {
        +UUID id
        +String name
        +Double price
        +String category
        +Boolean available
        +updatePrice()
    }
    class Order {
        +UUID id
        +UUID userId
        +Date orderDate
        +String status
        +Double total
        +addProduct()
        +calculateTotal()
    }
    User "1" --> "*" Order : places
    Order "*" --> "*" Product : contains
EOF

cat > "$TEST_DIR/src/diagrams/sequence-diagram.mmd" << 'EOF'
sequenceDiagram
    participant U as User
    participant S as System
    participant DB as Database
    participant P as PaymentService
    
    U->>S: login(username, password)
    S->>DB: validateUser(username, password)
    DB-->>S: userValid
    S-->>U: loginSuccess
    
    U->>S: createOrder(products)
    S->>DB: saveOrder(order)
    S->>P: processPayment(amount)
    P-->>S: paymentSuccess
    DB-->>S: orderSaved
    S-->>U: orderCreated
EOF

cat > "$TEST_DIR/src/diagrams/state-diagram.mmd" << 'EOF'
stateDiagram-v2
    [*] --> Pending
    Pending --> Processing : process
    Processing --> Paid : payment_success
    Processing --> Failed : payment_failed
    Paid --> Shipped : ship
    Shipped --> Delivered : deliver
    Failed --> Pending : retry
    Delivered --> [*]
EOF

cat > "$TEST_DIR/src/diagrams/activity-diagram.mmd" << 'EOF'
flowchart TD
    A[User Login] --> B{Credentials Valid?}
    B -->|Yes| C[Show Dashboard]
    B -->|No| D[Show Error]
    D --> A
    C --> E[Browse Products]
    E --> F{Add to Cart?}
    F -->|Yes| G[Add Product]
    F -->|No| E
    G --> H[View Cart]
    H --> I{Checkout?}
    I -->|Yes| J[Process Payment]
    I -->|No| E
    J --> K{Payment Success?}
    K -->|Yes| L[Order Confirmed]
    K -->|No| M[Payment Failed]
    M --> H
    L --> N[End]
EOF

echo "✅ Test project created in $TEST_DIR"
echo "📁 Test diagrams:"
ls -la "$TEST_DIR/src/diagrams/"

# 8. Instructions finales
echo ""
echo "🎉 Build Complete!"
echo "=================="
echo "📦 Extension package: basiccode-generator-1.3.0.vsix"
echo "🧪 Test project: $TEST_DIR"
echo ""
echo "🚀 Next steps:"
echo "1. Open VSCode in the test project: code $TEST_DIR"
echo "2. Press Ctrl+Shift+G to generate code"
echo "3. Configure backend URL if needed"
echo ""
echo "🔧 Backend endpoints:"
echo "- Production: https://codegenerator-cpyh.onrender.com"
echo "- Local: http://localhost:8080"
echo ""
echo "📚 Documentation: UNIFIED-FEATURES.md"
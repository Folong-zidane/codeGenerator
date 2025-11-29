#!/bin/bash

echo "🚀 Testing Code Generator API - Fixed Endpoints"
echo "==============================================="

BASE_URL="http://localhost:8080"

# Test 1: Health Check (basic endpoint test)
echo "1. Testing basic endpoint..."
curl -s "$BASE_URL/" | head -5 || echo "Root endpoint not available"

# Test 2: Generate Comprehensive Code (Classes + Sequences + States)
echo -e "\n2. Generating Comprehensive Java Code..."
curl -X POST "$BASE_URL/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n        +validateEmail()\n    }\n    class Post {\n        +UUID id\n        +UUID authorId\n        +String title\n        +String content\n        +publish()\n    }\n    User \"1\" --> \"*\" Post",
    "sequenceDiagram": "sequenceDiagram\n    Client->>UserController: POST /api/users\n    UserController->>UserService: createUser(userData)\n    UserService->>UserRepository: save(user)\n    UserRepository-->>UserService: User created\n    UserService-->>UserController: Success\n    UserController-->>Client: 201 Created",
    "stateDiagram": "stateDiagram-v2\n    [*] --> ACTIVE\n    ACTIVE --> SUSPENDED : suspend()\n    SUSPENDED --> ACTIVE : activate()",
    "packageName": "com.example.blog",
    "language": "java"
  }' \
  -o comprehensive-java.zip

if [ -f "comprehensive-java.zip" ]; then
    echo "✅ Comprehensive Java code generated"
    file comprehensive-java.zip
    if file comprehensive-java.zip | grep -q "Zip archive"; then
        echo "📦 Valid ZIP file created"
        unzip -l comprehensive-java.zip | head -10
    else
        echo "❌ Invalid ZIP file, checking content:"
        head -5 comprehensive-java.zip
    fi
else
    echo "❌ Comprehensive Java code generation failed"
fi

# Test 3: Generate Django Code
echo -e "\n3. Generating Comprehensive Django Code..."
curl -X POST "$BASE_URL/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n        +ProductStatus status\n    }\n    User \"1\" --> \"*\" Product",
    "sequenceDiagram": "sequenceDiagram\n    Client->>UserController: POST /api/users\n    UserController->>UserService: createUser(userData)\n    UserService->>UserRepository: save(user)",
    "stateDiagram": "stateDiagram-v2\n    [*] --> ACTIVE\n    ACTIVE --> INACTIVE : deactivate()\n    INACTIVE --> ACTIVE : activate()",
    "packageName": "com.example.ecommerce",
    "language": "django"
  }' \
  -o comprehensive-django.zip

if [ -f "comprehensive-django.zip" ]; then
    echo "✅ Comprehensive Django code generated"
    file comprehensive-django.zip
    if file comprehensive-django.zip | grep -q "Zip archive"; then
        echo "📦 Valid ZIP file created"
        unzip -l comprehensive-django.zip | head -10
    else
        echo "❌ Invalid ZIP file, checking content:"
        head -5 comprehensive-django.zip
    fi
else
    echo "❌ Comprehensive Django code generation failed"
fi

# Test 4: Generate Python FastAPI Code
echo -e "\n4. Generating Comprehensive Python Code..."
curl -X POST "$BASE_URL/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class Order {\n        +UUID id\n        +String customerEmail\n        +Float total\n        +OrderStatus status\n        +calculateTotal()\n    }",
    "sequenceDiagram": "sequenceDiagram\n    Client->>OrderController: POST /api/orders\n    OrderController->>OrderService: createOrder(orderData)\n    OrderService->>OrderRepository: save(order)",
    "stateDiagram": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : confirm()\n    CONFIRMED --> SHIPPED : ship()\n    SHIPPED --> DELIVERED : deliver()",
    "packageName": "com.example.orders",
    "language": "python"
  }' \
  -o comprehensive-python.zip

if [ -f "comprehensive-python.zip" ]; then
    echo "✅ Comprehensive Python code generated"
    file comprehensive-python.zip
    if file comprehensive-python.zip | grep -q "Zip archive"; then
        echo "📦 Valid ZIP file created"
        unzip -l comprehensive-python.zip | head -10
    else
        echo "❌ Invalid ZIP file, checking content:"
        head -5 comprehensive-python.zip
    fi
else
    echo "❌ Comprehensive Python code generation failed"
fi

# Test 5: Test with minimal data
echo -e "\n5. Testing with minimal class diagram..."
curl -X POST "$BASE_URL/api/generate/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagram": "classDiagram\n    class User {\n        +UUID id\n        +String name\n    }",
    "packageName": "com.example.minimal",
    "language": "java"
  }' \
  -o minimal-java.zip

if [ -f "minimal-java.zip" ]; then
    echo "✅ Minimal Java code generated"
    file minimal-java.zip
else
    echo "❌ Minimal Java code generation failed"
fi

echo -e "\n🎉 Test completed! Generated files:"
ls -la *.zip 2>/dev/null || echo "No ZIP files generated"
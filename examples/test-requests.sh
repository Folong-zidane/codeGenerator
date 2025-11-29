#!/bin/bash

echo "🚀 Testing Code Generator API"
echo "=============================="

BASE_URL="http://localhost:8080"

# Test 1: Health Check
echo "1. Health Check..."
curl -s "$BASE_URL/actuator/health" | jq . || echo "Health endpoint not available"

# Test 2: Generate Java Spring Boot Code
echo -e "\n2. Generating Java Spring Boot Code..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n        +validateEmail()\n    }\n    class Post {\n        +UUID id\n        +UUID authorId\n        +String title\n        +String content\n        +publish()\n    }\n    User \"1\" --> \"*\" Post",
    "packageName": "com.example.blog",
    "language": "java"
  }' \
  -o java-blog.zip

if [ -f "java-blog.zip" ]; then
    echo "✅ Java code generated successfully (java-blog.zip)"
    unzip -l java-blog.zip | head -20
else
    echo "❌ Java code generation failed"
fi

# Test 3: Generate Django Code
echo -e "\n3. Generating Django Code..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n        +ProductStatus status\n    }\n    User \"1\" --> \"*\" Product",
    "packageName": "com.example.ecommerce",
    "language": "django"
  }' \
  -o django-ecommerce.zip

if [ -f "django-ecommerce.zip" ]; then
    echo "✅ Django code generated successfully (django-ecommerce.zip)"
    unzip -l django-ecommerce.zip | head -20
else
    echo "❌ Django code generation failed"
fi

# Test 4: Generate Python FastAPI Code
echo -e "\n4. Generating Python FastAPI Code..."
curl -X POST "$BASE_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class Order {\n        +UUID id\n        +String customerEmail\n        +Float total\n        +OrderStatus status\n        +calculateTotal()\n    }",
    "packageName": "com.example.orders",
    "language": "python"
  }' \
  -o python-orders.zip

if [ -f "python-orders.zip" ]; then
    echo "✅ Python FastAPI code generated successfully (python-orders.zip)"
    unzip -l python-orders.zip | head -20
else
    echo "❌ Python FastAPI code generation failed"
fi

# Test 5: Behavioral Generation (Classes + Sequences)
echo -e "\n5. Generating Behavioral Code (Classes + Sequences)..."
curl -X POST "$BASE_URL/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +validateEmail()\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>UserController: POST /api/users\n    UserController->>UserService: createUser(userData)\n    UserService->>UserRepository: save(user)\n    UserRepository-->>UserService: User created\n    UserService-->>UserController: Success\n    UserController-->>Client: 201 Created",
    "packageName": "com.example.behavioral",
    "language": "java"
  }' \
  -o behavioral-java.zip

if [ -f "behavioral-java.zip" ]; then
    echo "✅ Behavioral code generated successfully (behavioral-java.zip)"
    unzip -l behavioral-java.zip | head -20
else
    echo "❌ Behavioral code generation failed"
fi

# Test 6: Comprehensive Generation (Classes + Sequences + States)
echo -e "\n6. Generating Comprehensive Code (Classes + Sequences + States)..."
curl -X POST "$BASE_URL/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class Order {\n        +UUID id\n        +String customerEmail\n        +Float total\n        +OrderStatus status\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>OrderController: POST /api/orders\n    OrderController->>OrderService: createOrder(orderData)\n    OrderService->>OrderRepository: save(order)",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : confirm()\n    CONFIRMED --> SHIPPED : ship()\n    SHIPPED --> DELIVERED : deliver()",
    "packageName": "com.example.comprehensive",
    "language": "java"
  }' \
  -o comprehensive-java.zip

if [ -f "comprehensive-java.zip" ]; then
    echo "✅ Comprehensive code generated successfully (comprehensive-java.zip)"
    unzip -l comprehensive-java.zip | head -20
else
    echo "❌ Comprehensive code generation failed"
fi

echo -e "\n🎉 Test completed! Check the generated ZIP files."
echo "Generated files:"
ls -la *.zip 2>/dev/null || echo "No ZIP files generated"
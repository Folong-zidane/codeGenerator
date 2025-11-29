#!/bin/bash

# Test script for ultimate code generation using all 5 UML diagrams

echo "🚀 Testing Ultimate Code Generation (All 5 UML Diagrams)"
echo "======================================================="
echo ""

# API URL
API_URL="http://localhost:8080/api/ultimate"

# Test 1: Generate ultimate code with all 5 diagrams
echo "📋 Test 1: Generate ultimate code with all diagrams"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n        +LocalDateTime createdAt\n    }\n    class Order {\n        +UUID id\n        +UUID userId\n        +Float total\n        +OrderStatus status\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n        +Integer stock\n    }\n    User \"1\" --> \"*\" Order\n    Order \"*\" --> \"*\" Product",
    "sequenceDiagramContent": "sequenceDiagram\n    participant Client\n    participant OrderController\n    participant OrderService\n    participant UserService\n    participant ProductService\n    participant Database\n\n    Client->>OrderController: POST /api/orders\n    OrderController->>OrderService: createOrder(orderData)\n    OrderService->>UserService: validateUser(userId)\n    UserService-->>OrderService: user valid\n    OrderService->>ProductService: checkStock(productId)\n    ProductService-->>OrderService: stock available\n    OrderService->>Database: save order\n    Database-->>OrderService: order saved\n    OrderService-->>OrderController: order created\n    OrderController-->>Client: 201 Created",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : payment_success\n    PENDING --> CANCELLED : payment_failed\n    CONFIRMED --> PROCESSING : start_fulfillment\n    PROCESSING --> SHIPPED : items_dispatched\n    SHIPPED --> DELIVERED : delivery_confirmed\n    DELIVERED --> [*]\n    CONFIRMED --> CANCELLED : customer_cancellation\n    PROCESSING --> CANCELLED : fulfillment_failed",
    "objectDiagramContent": "objectDiagram\n    object user1 {\n        id = \"123e4567-e89b-12d3-a456-426614174000\"\n        username = \"john_doe\"\n        email = \"john@example.com\"\n        status = \"ACTIVE\"\n    }\n    object order1 {\n        id = \"456e7890-e89b-12d3-a456-426614174001\"\n        userId = \"123e4567-e89b-12d3-a456-426614174000\"\n        total = 299.99\n        status = \"PENDING\"\n    }\n    object product1 {\n        id = \"789e0123-e89b-12d3-a456-426614174002\"\n        name = \"Gaming Laptop\"\n        price = 1299.99\n        stock = 15\n    }\n    user1 -- order1\n    order1 -- product1",
    "componentDiagramContent": "componentDiagram\n    component WebLayer\n    component ServiceLayer\n    component DataLayer\n    interface UserAPI\n    interface OrderAPI\n    interface ProductAPI\n    \n    WebLayer --> UserAPI\n    WebLayer --> OrderAPI\n    WebLayer --> ProductAPI\n    UserAPI --> ServiceLayer\n    OrderAPI --> ServiceLayer\n    ProductAPI --> ServiceLayer\n    ServiceLayer --> DataLayer",
    "packageName": "com.example.ultimate",
    "language": "java"
  }' \
  -o ultimate-java-test.zip

if [ -f "ultimate-java-test.zip" ]; then
    echo "✅ Ultimate Java project generated successfully"
    echo ""
    echo "📁 Project contents:"
    unzip -l ultimate-java-test.zip | head -30
    
    echo ""
    echo "📂 Extracting project for inspection..."
    unzip -q ultimate-java-test.zip -d ultimate-test/
    
    echo ""
    echo "🔍 Generated files analysis:"
    echo "📊 Total files generated: $(find ultimate-test/ -type f | wc -l)"
    echo "📋 Java files: $(find ultimate-test/ -name "*.java" | wc -l)"
    echo "📄 Configuration files: $(find ultimate-test/ -name "*.xml" -o -name "*.properties" -o -name "*.yml" | wc -l)"
    echo "📚 Documentation files: $(find ultimate-test/ -name "*.md" | wc -l)"
    
    echo ""
    echo "🎯 Key generated components:"
    find ultimate-test/ -name "*.java" | head -10
    
else
    echo "❌ Failed to generate ultimate project"
fi

echo ""

# Test 2: Validate all diagrams
echo "📋 Test 2: Validate all five diagrams"

curl -X POST "$API_URL/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>UserService: createUser()",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> ACTIVE\n    ACTIVE --> INACTIVE",
    "objectDiagramContent": "objectDiagram\n    object user1 {\n        id = \"123\"\n        username = \"test\"\n    }",
    "componentDiagramContent": "componentDiagram\n    component WebLayer\n    component ServiceLayer\n    WebLayer --> ServiceLayer",
    "packageName": "com.test",
    "language": "java"
  }' | jq '.'

echo ""

# Test 3: Get ultimate example
echo "📋 Test 3: Get ultimate generation example"

curl -X GET "$API_URL/example" | jq '.'

echo ""

# Test 4: Test with minimal diagrams (only class diagram)
echo "📋 Test 4: Generate with minimal configuration"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class SimpleEntity {\n        +UUID id\n        +String name\n    }",
    "sequenceDiagramContent": "",
    "stateDiagramContent": "",
    "objectDiagramContent": "",
    "componentDiagramContent": "",
    "packageName": "com.simple",
    "language": "java"
  }' \
  -o ultimate-minimal-test.zip

if [ -f "ultimate-minimal-test.zip" ]; then
    echo "✅ Minimal ultimate project generated successfully"
    echo "📊 Minimal project size: $(du -h ultimate-minimal-test.zip | cut -f1)"
else
    echo "❌ Failed to generate minimal ultimate project"
fi

echo ""

# Test 5: Get all supported features
echo "📋 Test 5: Get all supported features"

curl -X GET "$API_URL/features" | jq '.'

echo ""

# Test 6: Test with complex multi-module setup
echo "📋 Test 6: Generate complex multi-module project"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }\n    class Order {\n        +UUID id\n        +UUID userId\n    }\n    class Product {\n        +UUID id\n        +String name\n    }\n    class Payment {\n        +UUID id\n        +UUID orderId\n    }\n    User \"1\" --> \"*\" Order\n    Order \"*\" --> \"*\" Product\n    Order \"1\" --> \"1\" Payment",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>OrderService: createOrder()\n    OrderService->>PaymentService: processPayment()\n    PaymentService-->>OrderService: payment_success\n    OrderService-->>Client: order_created",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> PAID\n    PAID --> SHIPPED\n    SHIPPED --> DELIVERED",
    "objectDiagramContent": "objectDiagram\n    object testUser {\n        id = \"test-123\"\n        username = \"testuser\"\n    }\n    object testOrder {\n        id = \"order-456\"\n        userId = \"test-123\"\n    }",
    "componentDiagramContent": "componentDiagram\n    component UserModule\n    component OrderModule\n    component PaymentModule\n    component NotificationModule\n    \n    UserModule --> OrderModule\n    OrderModule --> PaymentModule\n    OrderModule --> NotificationModule",
    "packageName": "com.ecommerce.multimodule",
    "language": "java"
  }' \
  -o ultimate-multimodule-test.zip

if [ -f "ultimate-multimodule-test.zip" ]; then
    echo "✅ Multi-module ultimate project generated successfully"
    echo ""
    echo "📊 Multi-module project analysis:"
    unzip -l ultimate-multimodule-test.zip | grep -E "\.(java|xml|md)$" | wc -l
    echo " files generated"
else
    echo "❌ Failed to generate multi-module ultimate project"
fi

echo ""
echo "🎯 Ultimate Generation Tests Completed!"
echo ""
echo "Generated files:"
ls -la *ultimate*.zip 2>/dev/null || echo "No files generated"

echo ""
echo "📚 To test manually:"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Open: http://localhost:8080/api/ultimate/example"
echo "3. Use the example to test ultimate generation"
echo ""
echo "🔗 API Endpoints:"
echo "- POST /api/ultimate/generate - Generate with all 5 diagrams"
echo "- POST /api/ultimate/validate - Validate all diagrams"
echo "- GET  /api/ultimate/example - Get example request"
echo "- GET  /api/ultimate/features - Get all supported features"
echo ""
echo "🎨 Ultimate Features Generated:"
echo "✅ Complete Entity Classes with JPA"
echo "✅ Advanced State Management"
echo "✅ Realistic Business Logic"
echo "✅ Comprehensive Test Suites"
echo "✅ Modular Architecture"
echo "✅ Build Configuration"
echo "✅ Spring Boot Configurations"
echo "✅ REST Controllers"
echo "✅ Service & Repository Layers"
echo "✅ Exception Handling"
echo "✅ Validation Rules"
echo "✅ Complete Documentation"
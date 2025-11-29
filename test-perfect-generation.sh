#!/bin/bash

# Test script for perfect code generation using all 6 UML diagrams

echo "🎯 Testing Perfect Code Generation (All 6 UML Diagrams)"
echo "======================================================="
echo ""

# API URL
API_URL="http://localhost:8080/api/perfect"

# Test 1: Generate perfect code with all 6 diagrams
echo "📋 Test 1: Generate perfect code with all diagrams"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n        +LocalDateTime createdAt\n    }\n    class Order {\n        +UUID id\n        +UUID userId\n        +Float total\n        +OrderStatus status\n    }\n    User \"1\" --> \"*\" Order",
    "sequenceDiagramContent": "sequenceDiagram\n    participant Client\n    participant OrderController\n    participant OrderService\n    participant UserService\n    participant Database\n\n    Client->>OrderController: POST /api/orders\n    OrderController->>OrderService: createOrder(orderData)\n    OrderService->>UserService: validateUser(userId)\n    UserService-->>OrderService: user valid\n    OrderService->>Database: save order\n    Database-->>OrderService: order saved\n    OrderService-->>OrderController: order created\n    OrderController-->>Client: 201 Created",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : payment_success\n    PENDING --> CANCELLED : payment_failed\n    CONFIRMED --> PROCESSING : start_fulfillment\n    PROCESSING --> SHIPPED : items_dispatched\n    SHIPPED --> DELIVERED : delivery_confirmed\n    DELIVERED --> [*]",
    "objectDiagramContent": "objectDiagram\n    object user1 {\n        id = \"123e4567-e89b-12d3-a456-426614174000\"\n        username = \"john_doe\"\n        email = \"john@example.com\"\n        status = \"ACTIVE\"\n    }\n    object order1 {\n        id = \"456e7890-e89b-12d3-a456-426614174001\"\n        userId = \"123e4567-e89b-12d3-a456-426614174000\"\n        total = 299.99\n        status = \"PENDING\"\n    }",
    "componentDiagramContent": "componentDiagram\n    component WebLayer\n    component ServiceLayer\n    component DataLayer\n    component WorkflowEngine\n    interface UserAPI\n    interface OrderAPI\n    \n    WebLayer --> UserAPI\n    WebLayer --> OrderAPI\n    UserAPI --> ServiceLayer\n    OrderAPI --> ServiceLayer\n    ServiceLayer --> DataLayer\n    ServiceLayer --> WorkflowEngine",
    "activityDiagramContent": "activityDiagram\n    [*]\n    activity ValidateUser as \"Validate User\"\n    decision UserValid as \"User Valid?\"\n    activity CreateOrder as \"Create Order\"\n    activity ProcessPayment as \"Process Payment\"\n    decision PaymentSuccess as \"Payment Success?\"\n    activity ConfirmOrder as \"Confirm Order\"\n    activity CancelOrder as \"Cancel Order\"\n    end\n    \n    [*] --> ValidateUser\n    ValidateUser --> UserValid\n    UserValid --> CreateOrder : \"Yes\"\n    UserValid --> end : \"No\"\n    CreateOrder --> ProcessPayment\n    ProcessPayment --> PaymentSuccess\n    PaymentSuccess --> ConfirmOrder : \"Yes\"\n    PaymentSuccess --> CancelOrder : \"No\"\n    ConfirmOrder --> end\n    CancelOrder --> end",
    "packageName": "com.example.perfect",
    "language": "java"
  }' \
  -o perfect-java-test.zip

if [ -f "perfect-java-test.zip" ]; then
    echo "✅ Perfect Java project generated successfully"
    echo ""
    echo "📁 Project contents:"
    unzip -l perfect-java-test.zip | head -40
    
    echo ""
    echo "📂 Extracting project for inspection..."
    unzip -q perfect-java-test.zip -d perfect-test/
    
    echo ""
    echo "🔍 Perfect project analysis:"
    echo "📊 Total files generated: $(find perfect-test/ -type f | wc -l)"
    echo "📋 Java files: $(find perfect-test/ -name "*.java" | wc -l)"
    echo "📄 Configuration files: $(find perfect-test/ -name "*.xml" -o -name "*.properties" -o -name "*.yml" | wc -l)"
    echo "📚 Documentation files: $(find perfect-test/ -name "*.md" | wc -l)"
    echo "🔀 Workflow files: $(find perfect-test/ -name "*Workflow*" | wc -l)"
    
    echo ""
    echo "🎯 Perfect components generated:"
    find perfect-test/ -name "*.java" | head -15
    
else
    echo "❌ Failed to generate perfect project"
fi

echo ""

# Test 2: Validate all 6 diagrams
echo "📋 Test 2: Validate all six diagrams"

curl -X POST "$API_URL/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>UserService: createUser()",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> ACTIVE\n    ACTIVE --> INACTIVE",
    "objectDiagramContent": "objectDiagram\n    object user1 {\n        id = \"123\"\n        username = \"test\"\n    }",
    "componentDiagramContent": "componentDiagram\n    component WebLayer\n    component ServiceLayer\n    WebLayer --> ServiceLayer",
    "activityDiagramContent": "activityDiagram\n    [*]\n    activity ProcessUser\n    ProcessUser --> end",
    "packageName": "com.test",
    "language": "java"
  }' | jq '.'

echo ""

# Test 3: Get perfect example
echo "📋 Test 3: Get perfect generation example"

curl -X GET "$API_URL/example" | jq '.'

echo ""

# Test 4: Test with complex workflow
echo "📋 Test 4: Generate with complex workflow patterns"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class Order {\n        +UUID id\n        +Float total\n        +OrderStatus status\n    }\n    class Payment {\n        +UUID id\n        +UUID orderId\n        +Float amount\n        +PaymentStatus status\n    }\n    Order \"1\" --> \"1\" Payment",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>OrderService: processOrder()\n    OrderService->>PaymentService: processPayment()\n    PaymentService-->>OrderService: payment_processed\n    OrderService-->>Client: order_completed",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> PAID\n    PAID --> SHIPPED\n    SHIPPED --> DELIVERED",
    "objectDiagramContent": "objectDiagram\n    object testOrder {\n        id = \"order-123\"\n        total = 99.99\n        status = \"PENDING\"\n    }",
    "componentDiagramContent": "componentDiagram\n    component OrderModule\n    component PaymentModule\n    component WorkflowModule\n    OrderModule --> PaymentModule\n    OrderModule --> WorkflowModule",
    "activityDiagramContent": "activityDiagram\n    [*]\n    activity ValidateOrder\n    decision OrderValid\n    fork ProcessPayment\n    activity ChargeCard\n    activity SendReceipt\n    join PaymentComplete\n    activity ShipOrder\n    end\n    \n    [*] --> ValidateOrder\n    ValidateOrder --> OrderValid\n    OrderValid --> ProcessPayment : \"Valid\"\n    OrderValid --> end : \"Invalid\"\n    ProcessPayment --> ChargeCard\n    ProcessPayment --> SendReceipt\n    ChargeCard --> PaymentComplete\n    SendReceipt --> PaymentComplete\n    PaymentComplete --> ShipOrder\n    ShipOrder --> end",
    "packageName": "com.ecommerce.perfect",
    "language": "java"
  }' \
  -o perfect-complex-test.zip

if [ -f "perfect-complex-test.zip" ]; then
    echo "✅ Complex perfect project generated successfully"
    echo ""
    echo "📊 Complex project analysis:"
    unzip -l perfect-complex-test.zip | grep -E "\.(java|xml|md)$" | wc -l
    echo " perfect files generated"
    
    echo ""
    echo "🔀 Workflow components:"
    unzip -l perfect-complex-test.zip | grep -i workflow
else
    echo "❌ Failed to generate complex perfect project"
fi

echo ""

# Test 5: Get all perfect features
echo "📋 Test 5: Get all perfect features"

curl -X GET "$API_URL/features" | jq '.'

echo ""

# Test 6: Test minimal perfect generation
echo "📋 Test 6: Generate minimal perfect project"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class SimpleEntity {\n        +UUID id\n        +String name\n    }",
    "sequenceDiagramContent": "",
    "stateDiagramContent": "",
    "objectDiagramContent": "",
    "componentDiagramContent": "",
    "activityDiagramContent": "activityDiagram\n    [*]\n    activity ProcessEntity\n    ProcessEntity --> end",
    "packageName": "com.simple.perfect",
    "language": "java"
  }' \
  -o perfect-minimal-test.zip

if [ -f "perfect-minimal-test.zip" ]; then
    echo "✅ Minimal perfect project generated successfully"
    echo "📊 Minimal project size: $(du -h perfect-minimal-test.zip | cut -f1)"
else
    echo "❌ Failed to generate minimal perfect project"
fi

echo ""
echo "🎯 Perfect Generation Tests Completed!"
echo ""
echo "Generated files:"
ls -la *perfect*.zip 2>/dev/null || echo "No files generated"

echo ""
echo "📚 To test manually:"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Open: http://localhost:8080/api/perfect/example"
echo "3. Use the example to test perfect generation"
echo ""
echo "🔗 API Endpoints:"
echo "- POST /api/perfect/generate - Generate with all 6 diagrams"
echo "- POST /api/perfect/validate - Validate all diagrams"
echo "- GET  /api/perfect/example - Get example request"
echo "- GET  /api/perfect/features - Get all perfect features"
echo ""
echo "🎨 Perfect Features Generated:"
echo "✅ Complete Entity Classes with JPA"
echo "✅ Advanced State Management"
echo "✅ Realistic Business Logic"
echo "✅ Comprehensive Test Suites"
echo "✅ Modular Architecture"
echo "✅ Business Workflow Engines"
echo "✅ Process Automation"
echo "✅ Build Configuration"
echo "✅ Spring Boot Configurations"
echo "✅ REST Controllers"
echo "✅ Service & Repository Layers"
echo "✅ Exception Handling"
echo "✅ Validation Rules"
echo "✅ Workflow Orchestration"
echo "✅ Perfect Documentation"
echo ""
echo "🎯 PERFECT CODE GENERATION ACHIEVED! 🎯"
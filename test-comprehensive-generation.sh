#!/bin/bash

# Test script for comprehensive code generation using all three diagrams

echo "🎯 Testing Comprehensive Code Generation (Class + Sequence + State)"
echo "=================================================================="
echo ""

# API URL
API_URL="http://localhost:8080/api/comprehensive"

# Test 1: Generate comprehensive code with all three diagrams
echo "📋 Test 1: Generate comprehensive code with all diagrams"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +UserStatus status\n        +LocalDateTime createdAt\n        +validateEmail()\n        +activate()\n        +suspend()\n    }\n    class UserProfile {\n        +UUID id\n        +UUID userId\n        +String firstName\n        +String lastName\n    }\n    User \"1\" --> \"1\" UserProfile",
    "sequenceDiagramContent": "sequenceDiagram\n    participant Client\n    participant UserController\n    participant UserService\n    participant UserRepository\n    participant Database\n\n    Client->>+UserController: POST /api/users/activate\n    UserController->>+UserService: activateUser(userId)\n    \n    alt User exists and inactive\n        UserService->>+UserRepository: findById(userId)\n        UserRepository->>+Database: SELECT * FROM users WHERE id = ?\n        Database-->>-UserRepository: User data\n        UserRepository-->>-UserService: User entity\n        UserService->>UserService: changeStatus(ACTIVE)\n        UserService->>+UserRepository: save(user)\n        UserRepository-->>-UserService: Updated user\n        UserService-->>-UserController: Activation success\n        UserController-->>-Client: 200 OK\n    else User not found or already active\n        UserService-->>UserController: ValidationException\n        UserController-->>Client: 400 Bad Request\n    end",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> INACTIVE\n    INACTIVE --> PENDING : activate()\n    PENDING --> ACTIVE : email_verified\n    PENDING --> SUSPENDED : verification_failed\n    ACTIVE --> SUSPENDED : suspend()\n    ACTIVE --> DELETED : delete()\n    SUSPENDED --> ACTIVE : reactivate()\n    SUSPENDED --> DELETED : delete()\n    DELETED --> [*]",
    "packageName": "com.example.comprehensive",
    "language": "java"
  }' \
  -o comprehensive-java-test.zip

if [ -f "comprehensive-java-test.zip" ]; then
    echo "✅ Comprehensive Java project generated successfully"
    echo ""
    echo "📁 Project contents:"
    unzip -l comprehensive-java-test.zip
    
    echo ""
    echo "📂 Extracting project for inspection..."
    unzip -q comprehensive-java-test.zip -d comprehensive-test/
    
    echo ""
    echo "🔍 Generated entity with state management:"
    if [ -f "comprehensive-test/User.java" ]; then
        echo "--- User.java (first 50 lines) ---"
        head -50 comprehensive-test/User.java
    fi
    
    echo ""
    echo "🔍 Generated state enum:"
    if [ -f "comprehensive-test/UserStatus.java" ]; then
        echo "--- UserStatus.java ---"
        cat comprehensive-test/UserStatus.java
    fi
    
else
    echo "❌ Failed to generate comprehensive project"
fi

echo ""

# Test 2: Validate all diagrams
echo "📋 Test 2: Validate all three diagrams"

curl -X POST "$API_URL/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class Order {\n        +UUID id\n        +OrderStatus status\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>OrderService: createOrder()",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : payment_success",
    "packageName": "com.test",
    "language": "java"
  }' | jq '.'

echo ""

# Test 3: Get comprehensive example
echo "📋 Test 3: Get comprehensive generation example"

curl -X GET "$API_URL/example" | jq '.'

echo ""

# Test 4: Test with complex state diagram
echo "📋 Test 4: Generate with complex state patterns"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class Order {\n        +UUID id\n        +String customerEmail\n        +Float total\n        +OrderStatus status\n        +processPayment()\n        +ship()\n        +deliver()\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>OrderService: processPayment(orderId)\n    OrderService->>PaymentService: charge(amount)\n    PaymentService-->>OrderService: payment_success\n    OrderService->>Order: changeStatus(CONFIRMED)\n    Order-->>OrderService: status_changed\n    OrderService-->>Client: payment_processed",
    "stateDiagramContent": "stateDiagram-v2\n    [*] --> PENDING\n    PENDING --> CONFIRMED : payment_success\n    PENDING --> CANCELLED : payment_failed\n    CONFIRMED --> PROCESSING : start_fulfillment\n    PROCESSING --> SHIPPED : items_dispatched\n    SHIPPED --> DELIVERED : delivery_confirmed\n    DELIVERED --> [*]\n    \n    state choice1 <<choice>>\n    CONFIRMED --> choice1 : customer_action\n    choice1 --> CANCELLED : [wants_to_cancel]\n    choice1 --> PROCESSING : [proceed_with_order]",
    "packageName": "com.ecommerce.comprehensive",
    "language": "java"
  }' \
  -o comprehensive-ecommerce-test.zip

if [ -f "comprehensive-ecommerce-test.zip" ]; then
    echo "✅ Complex comprehensive project generated successfully"
    echo ""
    echo "📊 Analysis of generated features:"
    unzip -l comprehensive-ecommerce-test.zip | grep -E "\.(java|md)$"
else
    echo "❌ Failed to generate complex comprehensive project"
fi

echo ""

# Test 5: Test supported features
echo "📋 Test 5: Get supported features"

curl -X GET "$API_URL/features" | jq '.'

echo ""
echo "🎯 Comprehensive Generation Tests Completed!"
echo ""
echo "Generated files:"
ls -la *comprehensive*.zip 2>/dev/null || echo "No files generated"

echo ""
echo "📚 To test manually:"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Open: http://localhost:8080/api/comprehensive/example"
echo "3. Use the example to test comprehensive generation"
echo ""
echo "🔗 API Endpoints:"
echo "- POST /api/comprehensive/generate - Generate with all 3 diagrams"
echo "- POST /api/comprehensive/validate - Validate all diagrams"
echo "- GET  /api/comprehensive/example - Get example request"
echo "- GET  /api/comprehensive/features - Get supported features"
echo ""
echo "🎨 Generated Features:"
echo "✅ State Management with Enums"
echo "✅ State Transition Methods"
echo "✅ Business Logic from Sequences"
echo "✅ State Validation Rules"
echo "✅ Comprehensive Documentation"
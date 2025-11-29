#!/bin/bash

# Test script for behavioral code generation using both class and sequence diagrams

echo "🚀 Testing Behavioral Code Generation"
echo "======================================"

# API URL
API_URL="http://localhost:8080/api/behavioral"

# Test 1: Generate code with both diagrams
echo "📋 Test 1: Generate enhanced code with both diagrams"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +String password\n        +validateEmail()\n        +hashPassword()\n    }\n    class Order {\n        +UUID id\n        +UUID userId\n        +Float total\n        +OrderStatus status\n        +calculateTotal()\n    }\n    User \"1\" --> \"*\" Order",
    "sequenceDiagramContent": "sequenceDiagram\n    participant Client\n    participant UserController\n    participant UserService\n    participant UserRepository\n    participant Database\n\n    Client->>+UserController: POST /api/users/register\n    UserController->>+UserService: createUser(userData)\n    \n    alt Valid user data\n        UserService->>+UserRepository: save(user)\n        UserRepository->>+Database: INSERT INTO users\n        Database-->>-UserRepository: User created\n        UserRepository-->>-UserService: User entity\n        UserService-->>-UserController: Created user\n        UserController-->>-Client: 201 Created\n    else Invalid data\n        UserService-->>UserController: ValidationException\n        UserController-->>Client: 400 Bad Request\n    end",
    "packageName": "com.example.behavioral",
    "language": "java"
  }' \
  -o behavioral-java-test.zip

if [ -f "behavioral-java-test.zip" ]; then
    echo "✅ Behavioral Java project generated successfully"
    unzip -l behavioral-java-test.zip | head -20
else
    echo "❌ Failed to generate behavioral Java project"
fi

echo ""

# Test 2: Validate diagrams
echo "📋 Test 2: Validate both diagrams"

curl -X POST "$API_URL/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +String name\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>Server: request",
    "packageName": "com.test",
    "language": "java"
  }' | jq '.'

echo ""

# Test 3: Get example
echo "📋 Test 3: Get behavioral generation example"

curl -X GET "$API_URL/example" | jq '.'

echo ""

# Test 4: Test with complex sequence diagram
echo "📋 Test 4: Generate with complex sequence patterns"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n        +Integer stock\n    }\n    class Order {\n        +UUID id\n        +UUID productId\n        +Integer quantity\n        +Float total\n    }\n    Product \"1\" --> \"*\" Order",
    "sequenceDiagramContent": "sequenceDiagram\n    participant Client\n    participant OrderController\n    participant OrderService\n    participant ProductService\n    participant Database\n\n    Client->>+OrderController: POST /api/orders\n    OrderController->>+OrderService: createOrder(orderData)\n    \n    loop Validate each product\n        OrderService->>+ProductService: validateProduct(productId)\n        ProductService->>+Database: SELECT * FROM products WHERE id = ?\n        Database-->>-ProductService: Product data\n        \n        opt Product exists\n            ProductService->>ProductService: checkStock(quantity)\n            alt Stock available\n                ProductService-->>OrderService: Product valid\n            else Insufficient stock\n                ProductService-->>OrderService: StockException\n            end\n        end\n    end\n    \n    critical Calculate total\n        OrderService->>OrderService: calculateTotal()\n        OrderService->>+Database: INSERT INTO orders\n        Database-->>-OrderService: Order created\n    option Database error\n        OrderService->>OrderService: rollbackTransaction()\n    end\n    \n    OrderService-->>-OrderController: Created order\n    OrderController-->>-Client: 201 Created",
    "packageName": "com.ecommerce.advanced",
    "language": "java"
  }' \
  -o behavioral-complex-test.zip

if [ -f "behavioral-complex-test.zip" ]; then
    echo "✅ Complex behavioral project generated successfully"
    echo "📁 Contents:"
    unzip -l behavioral-complex-test.zip
else
    echo "❌ Failed to generate complex behavioral project"
fi

echo ""

# Test 5: Test different languages
echo "📋 Test 5: Test behavioral generation for Python"

curl -X POST "$API_URL/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "classDiagramContent": "classDiagram\n    class User {\n        +str username\n        +str email\n    }",
    "sequenceDiagramContent": "sequenceDiagram\n    Client->>UserService: create_user(data)\n    UserService->>Database: save(user)\n    Database-->>UserService: user_created\n    UserService-->>Client: success",
    "packageName": "behavioral_app",
    "language": "python"
  }' \
  -o behavioral-python-test.zip

if [ -f "behavioral-python-test.zip" ]; then
    echo "✅ Behavioral Python project generated successfully"
else
    echo "❌ Failed to generate behavioral Python project"
fi

echo ""
echo "🎯 Behavioral Generation Tests Completed!"
echo ""
echo "Generated files:"
ls -la *behavioral*.zip 2>/dev/null || echo "No files generated"

echo ""
echo "📚 To test manually:"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Open: http://localhost:8080/api/behavioral/example"
echo "3. Use the example to test behavioral generation"
echo ""
echo "🔗 API Endpoints:"
echo "- POST /api/behavioral/generate - Generate behavioral code"
echo "- POST /api/behavioral/validate - Validate diagrams"
echo "- GET  /api/behavioral/example - Get example request"
echo "- GET  /api/behavioral/languages - Get supported languages"
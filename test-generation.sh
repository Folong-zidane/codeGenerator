#!/bin/bash

echo "🚀 Starting Spring Boot server..."
mvn spring-boot:run &
SERVER_PID=$!

echo "⏳ Waiting for server to start..."
sleep 10

echo "📝 Testing simple user generation..."
curl -X POST http://localhost:8080/api/generate/comprehensive \
  -H "Content-Type: application/json" \
  -d @examples/simple-user.json \
  | jq '.'

echo -e "\n📦 Testing ecommerce generation..."
curl -X POST http://localhost:8080/api/generate/comprehensive \
  -H "Content-Type: application/json" \
  -d @examples/ecommerce.json \
  | jq '.'

echo -e "\n🛑 Stopping server..."
kill $SERVER_PID
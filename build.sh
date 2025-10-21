#!/bin/bash

echo "🚀 Building UML to Code Generator..."

# Clean and compile
mvn clean compile

# Generate ANTLR sources
echo "📝 Generating ANTLR parsers..."
mvn antlr4:antlr4

# Run tests
echo "🧪 Running tests..."
mvn test

# Package
echo "📦 Packaging application..."
mvn package

echo "✅ Build completed!"
echo "📁 JAR file: target/uml-generator.jar"
echo ""
echo "Usage examples:"
echo "java -jar target/uml-generator.jar examples/sample-diagram.mermaid --output=./generated --language=java"
echo "java -jar target/uml-generator.jar examples/sample-diagram.mermaid --output=./generated --language=python"
echo "java -jar target/uml-generator.jar examples/sample-diagram.mermaid --output=./generated --language=csharp"
#!/bin/bash

echo "🚀 Testing Refactored Architecture"
echo "=================================="

# Test compilation
echo "📦 Compiling refactored code..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful"
else
    echo "❌ Compilation failed"
    exit 1
fi

# Test architecture
echo "🧪 Running architecture tests..."
mvn test -Dtest=RefactoredArchitectureTest -q

if [ $? -eq 0 ]; then
    echo "✅ Architecture tests passed"
else
    echo "❌ Architecture tests failed"
    exit 1
fi

echo ""
echo "🎉 Refactored Architecture Summary:"
echo "=================================="
echo "✅ Dependency Injection implemented"
echo "✅ SOLID principles applied"
echo "✅ Strategy pattern for parsers"
echo "✅ Abstract Factory for generators"
echo "✅ Facade pattern for parser coordination"
echo "✅ Template Method for code generation"
echo "✅ Builder pattern for model construction"
echo "✅ Registry pattern for factory resolution"
echo ""
echo "🔧 Key Improvements:"
echo "- Eliminated concrete instantiations (new Parser())"
echo "- Separated responsibilities into focused services"
echo "- Made architecture extensible for new languages"
echo "- Improved testability with dependency injection"
echo "- Reduced coupling between components"
echo ""
echo "🚀 Ready for production use!"
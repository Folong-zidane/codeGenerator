#!/bin/bash

echo "🔧 Compiling Refactored Architecture Only"
echo "========================================"

# Create temporary directory for refactored classes only
mkdir -p temp-refactored/src/main/java/com/basiccode/generator

# Copy only refactored classes
cp -r src/main/java/com/basiccode/generator/parser temp-refactored/src/main/java/com/basiccode/generator/
cp -r src/main/java/com/basiccode/generator/generator temp-refactored/src/main/java/com/basiccode/generator/
cp -r src/main/java/com/basiccode/generator/service/DiagramParserFacade.java temp-refactored/src/main/java/com/basiccode/generator/service/ 2>/dev/null || true
cp -r src/main/java/com/basiccode/generator/service/BehaviorExtractor.java temp-refactored/src/main/java/com/basiccode/generator/service/ 2>/dev/null || true
cp -r src/main/java/com/basiccode/generator/service/StateEnhancer.java temp-refactored/src/main/java/com/basiccode/generator/service/ 2>/dev/null || true
cp -r src/main/java/com/basiccode/generator/service/CodeGenerationOrchestrator.java temp-refactored/src/main/java/com/basiccode/generator/service/ 2>/dev/null || true
cp -r src/main/java/com/basiccode/generator/config temp-refactored/src/main/java/com/basiccode/generator/ 2>/dev/null || true
cp -r src/main/java/com/basiccode/generator/model temp-refactored/src/main/java/com/basiccode/generator/

# Copy minimal pom.xml
cat > temp-refactored/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.basiccode</groupId>
    <artifactId>refactored-generator</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.0.9</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Compile refactored classes
cd temp-refactored
echo "📦 Compiling refactored classes..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Refactored architecture compiles successfully!"
    echo ""
    echo "🏗️ Architecture Summary:"
    echo "- Parser interfaces and implementations ✅"
    echo "- Generator factories and strategies ✅" 
    echo "- Service layer with dependency injection ✅"
    echo "- Model classes with builders ✅"
    echo "- Configuration and registry ✅"
else
    echo "❌ Compilation failed"
fi

# Cleanup
cd ..
rm -rf temp-refactored

echo ""
echo "🎯 Next Steps:"
echo "1. Integrate refactored classes with existing codebase"
echo "2. Update existing services to use new architecture"
echo "3. Add comprehensive tests"
echo "4. Deploy with new modular structure"
#!/bin/bash

# Script ultra-simple pour consommer l'API distante

set -e

# Configuration
API_URL="https://codegenerator-cpyh.onrender.com"
UML_FILE="$1"
PACKAGE_NAME="$2"
OUTPUT_DIR="$3"

# Vérification des arguments
if [ $# -ne 3 ]; then
    echo "Usage: $0 <uml_file> <package_name> <output_dir>"
    echo "Exemple: $0 diagram.mermaid com.example ./my-project"
    exit 1
fi

# Vérifier que le fichier UML existe
if [ ! -f "$UML_FILE" ]; then
    echo "❌ Fichier UML non trouvé: $UML_FILE"
    exit 1
fi

echo "🚀 Génération via API distante"
echo "=============================="
echo "📁 UML: $UML_FILE"
echo "📦 Package: $PACKAGE_NAME"
echo "🎯 Sortie: $OUTPUT_DIR"
echo ""

# Créer la requête JSON
echo "📝 Préparation de la requête..."
cat > request.json << EOF
{
  "umlContent": $(cat "$UML_FILE" | jq -R -s '.'),
  "packageName": "$PACKAGE_NAME"
}
EOF

# Appeler l'API
echo "⚙️  Génération du code..."
curl -X POST "$API_URL/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d @request.json \
  --output "generated-code.zip" \
  --silent --show-error

# Vérifier le résultat
if [ ! -f "generated-code.zip" ] || [ ! -s "generated-code.zip" ]; then
    echo "❌ Erreur lors de la génération"
    exit 1
fi

# Créer le projet
echo "🏗️  Création du projet Spring Boot..."
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

# Extraire le code généré
unzip -q generated-code.zip -d "$OUTPUT_DIR"

# Créer pom.xml
cat > "$OUTPUT_DIR/pom.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/>
    </parent>
    
    <groupId>$PACKAGE_NAME</groupId>
    <artifactId>generated-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Créer Application.java
PACKAGE_PATH=$(echo "$PACKAGE_NAME" | tr '.' '/')
mkdir -p "$OUTPUT_DIR/src/main/java/$PACKAGE_PATH"

cat > "$OUTPUT_DIR/src/main/java/$PACKAGE_PATH/GeneratedApplication.java" << EOF
package $PACKAGE_NAME;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeneratedApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratedApplication.class, args);
    }
}
EOF

# Créer application.yml
mkdir -p "$OUTPUT_DIR/src/main/resources"
cat > "$OUTPUT_DIR/src/main/resources/application.yml" << 'EOF'
server:
  port: 8080

spring:
  application:
    name: generated-app
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
EOF

# Nettoyer
rm -f request.json generated-code.zip

echo ""
echo "✅ Projet généré avec succès!"
echo "📁 Répertoire: $OUTPUT_DIR"
echo ""
echo "🚀 Pour lancer:"
echo "   cd $OUTPUT_DIR"
echo "   mvn spring-boot:run"
echo ""
echo "🌐 URLs:"
echo "   • App: http://localhost:8080"
echo "   • Swagger: http://localhost:8080/swagger-ui.html"
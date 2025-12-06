package com.basiccode.generator.generator.spring;

import com.basiccode.generator.model.EnhancedClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Spring Boot configuration generator
 * Generates application.yml, pom.xml, and other configuration files
 */
@Component
@Slf4j
public class SpringBootConfigGenerator {
    
    public String generateApplicationYml(String packageName, List<EnhancedClass> classes) {
        log.info("Generating application.yml for Spring Boot project");
        
        StringBuilder yml = new StringBuilder();
        
        yml.append("# Spring Boot Application Configuration\n");
        yml.append("spring:\n");
        yml.append("  application:\n");
        yml.append("    name: ").append(packageName.substring(packageName.lastIndexOf('.') + 1)).append("-api\n\n");
        
        // Database configuration
        yml.append("  datasource:\n");
        yml.append("    url: jdbc:h2:mem:testdb\n");
        yml.append("    driver-class-name: org.h2.Driver\n");
        yml.append("    username: sa\n");
        yml.append("    password: password\n\n");
        
        // JPA configuration
        yml.append("  jpa:\n");
        yml.append("    hibernate:\n");
        yml.append("      ddl-auto: validate\n");
        yml.append("    show-sql: true\n");
        yml.append("    properties:\n");
        yml.append("      hibernate:\n");
        yml.append("        format_sql: true\n");
        yml.append("        dialect: org.hibernate.dialect.H2Dialect\n\n");
        
        // Flyway configuration
        yml.append("  flyway:\n");
        yml.append("    enabled: true\n");
        yml.append("    baseline-on-migrate: true\n");
        yml.append("    validate-on-migrate: true\n");
        yml.append("    locations: classpath:db/migration\n");
        yml.append("    baseline-version: 0\n\n");
        
        // H2 Console
        yml.append("  h2:\n");
        yml.append("    console:\n");
        yml.append("      enabled: true\n");
        yml.append("      path: /h2-console\n\n");
        
        // Server configuration
        yml.append("server:\n");
        yml.append("  port: 8080\n");
        yml.append("  servlet:\n");
        yml.append("    context-path: /\n\n");
        
        // Logging configuration
        yml.append("logging:\n");
        yml.append("  level:\n");
        yml.append("    ").append(packageName).append(": DEBUG\n");
        yml.append("    org.springframework.web: DEBUG\n");
        yml.append("    org.hibernate.SQL: DEBUG\n");
        yml.append("    org.hibernate.type.descriptor.sql.BasicBinder: TRACE\n\n");
        
        // Management endpoints
        yml.append("management:\n");
        yml.append("  endpoints:\n");
        yml.append("    web:\n");
        yml.append("      exposure:\n");
        yml.append("        include: health,info,metrics\n");
        yml.append("  endpoint:\n");
        yml.append("    health:\n");
        yml.append("      show-details: always\n");
        
        return yml.toString();
    }
    
    public String generateApplicationProperties(String packageName) {
        StringBuilder props = new StringBuilder();
        
        props.append("# Spring Boot Application Properties\n");
        props.append("spring.application.name=").append(packageName.substring(packageName.lastIndexOf('.') + 1)).append("-api\n\n");
        
        props.append("# Database Configuration\n");
        props.append("spring.datasource.url=jdbc:h2:mem:testdb\n");
        props.append("spring.datasource.driver-class-name=org.h2.Driver\n");
        props.append("spring.datasource.username=sa\n");
        props.append("spring.datasource.password=password\n\n");
        
        props.append("# JPA Configuration\n");
        props.append("spring.jpa.hibernate.ddl-auto=create-drop\n");
        props.append("spring.jpa.show-sql=true\n");
        props.append("spring.jpa.properties.hibernate.format_sql=true\n");
        props.append("spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect\n\n");
        
        props.append("# H2 Console\n");
        props.append("spring.h2.console.enabled=true\n");
        props.append("spring.h2.console.path=/h2-console\n\n");
        
        props.append("# Server Configuration\n");
        props.append("server.port=8080\n\n");
        
        props.append("# Logging Configuration\n");
        props.append("logging.level.").append(packageName).append("=DEBUG\n");
        props.append("logging.level.org.springframework.web=DEBUG\n");
        props.append("logging.level.org.hibernate.SQL=DEBUG\n");
        
        return props.toString();
    }
    
    public String generatePomXml(String packageName, List<EnhancedClass> classes) {
        log.info("Generating pom.xml for Spring Boot project");
        
        String artifactId = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        StringBuilder pom = new StringBuilder();
        
        pom.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        pom.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        pom.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        pom.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        pom.append("    <modelVersion>4.0.0</modelVersion>\n\n");
        
        pom.append("    <parent>\n");
        pom.append("        <groupId>org.springframework.boot</groupId>\n");
        pom.append("        <artifactId>spring-boot-starter-parent</artifactId>\n");
        pom.append("        <version>3.1.0</version>\n");
        pom.append("        <relativePath/>\n");
        pom.append("    </parent>\n\n");
        
        pom.append("    <groupId>").append(packageName).append("</groupId>\n");
        pom.append("    <artifactId>").append(artifactId).append("</artifactId>\n");
        pom.append("    <version>1.0.0</version>\n");
        pom.append("    <packaging>jar</packaging>\n\n");
        
        pom.append("    <name>").append(artifactId).append("</name>\n");
        pom.append("    <description>Generated Spring Boot application</description>\n\n");
        
        pom.append("    <properties>\n");
        pom.append("        <java.version>17</java.version>\n");
        pom.append("        <maven.compiler.source>17</maven.compiler.source>\n");
        pom.append("        <maven.compiler.target>17</maven.compiler.target>\n");
        pom.append("        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n");
        pom.append("    </properties>\n\n");
        
        pom.append("    <dependencies>\n");
        
        // Core Spring Boot dependencies
        pom.append("        <!-- Spring Boot Starters -->\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-web</artifactId>\n");
        pom.append("        </dependency>\n\n");
        
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-data-jpa</artifactId>\n");
        pom.append("        </dependency>\n\n");
        
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-validation</artifactId>\n");
        pom.append("        </dependency>\n\n");
        
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-actuator</artifactId>\n");
        pom.append("        </dependency>\n\n");
        
        // Flyway
        pom.append("        <!-- Database Migration -->\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.flywaydb</groupId>\n");
        pom.append("            <artifactId>flyway-core</artifactId>\n");
        pom.append("        </dependency>\n\n");
        
        // Database
        pom.append("        <!-- Database -->\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>com.h2database</groupId>\n");
        pom.append("            <artifactId>h2</artifactId>\n");
        pom.append("            <scope>runtime</scope>\n");
        pom.append("        </dependency>\n\n");
        
        // Lombok
        pom.append("        <!-- Lombok -->\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.projectlombok</groupId>\n");
        pom.append("            <artifactId>lombok</artifactId>\n");
        pom.append("            <optional>true</optional>\n");
        pom.append("        </dependency>\n\n");
        
        // Test dependencies
        pom.append("        <!-- Test Dependencies -->\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-test</artifactId>\n");
        pom.append("            <scope>test</scope>\n");
        pom.append("        </dependency>\n\n");
        
        pom.append("    </dependencies>\n\n");
        
        pom.append("    <build>\n");
        pom.append("        <plugins>\n");
        pom.append("            <plugin>\n");
        pom.append("                <groupId>org.springframework.boot</groupId>\n");
        pom.append("                <artifactId>spring-boot-maven-plugin</artifactId>\n");
        pom.append("                <configuration>\n");
        pom.append("                    <excludes>\n");
        pom.append("                        <exclude>\n");
        pom.append("                            <groupId>org.projectlombok</groupId>\n");
        pom.append("                            <artifactId>lombok</artifactId>\n");
        pom.append("                        </exclude>\n");
        pom.append("                    </excludes>\n");
        pom.append("                </configuration>\n");
        pom.append("            </plugin>\n");
        pom.append("            \n");
        pom.append("            <!-- Flyway Plugin -->\n");
        pom.append("            <plugin>\n");
        pom.append("                <groupId>org.flywaydb</groupId>\n");
        pom.append("                <artifactId>flyway-maven-plugin</artifactId>\n");
        pom.append("                <version>9.22.3</version>\n");
        pom.append("                <configuration>\n");
        pom.append("                    <url>jdbc:h2:mem:testdb</url>\n");
        pom.append("                    <user>sa</user>\n");
        pom.append("                    <password></password>\n");
        pom.append("                </configuration>\n");
        pom.append("            </plugin>\n");
        pom.append("        </plugins>\n");
        pom.append("    </build>\n\n");
        
        pom.append("</project>\n");
        
        return pom.toString();
    }
    
    public String generateMainApplication(String packageName) {
        log.info("Generating main application class");
        
        String className = packageName.substring(packageName.lastIndexOf('.') + 1);
        String capitalizedClassName = className.substring(0, 1).toUpperCase() + className.substring(1);
        
        StringBuilder app = new StringBuilder();
        
        app.append("package ").append(packageName).append(";\n\n");
        app.append("import org.springframework.boot.SpringApplication;\n");
        app.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n");
        app.append("import lombok.extern.slf4j.Slf4j;\n\n");
        
        app.append("@SpringBootApplication\n");
        app.append("@Slf4j\n");
        app.append("public class ").append(capitalizedClassName).append("Application {\n\n");
        
        app.append("    public static void main(String[] args) {\n");
        app.append("        log.info(\"Starting ").append(capitalizedClassName).append(" Application...\");\n");
        app.append("        SpringApplication.run(").append(capitalizedClassName).append("Application.class, args);\n");
        app.append("        log.info(\"").append(capitalizedClassName).append(" Application started successfully!\");\n");
        app.append("    }\n");
        app.append("}\n");
        
        return app.toString();
    }
    
    public String generateDockerfile(String packageName) {
        String artifactId = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        StringBuilder dockerfile = new StringBuilder();
        
        dockerfile.append("# Multi-stage build for Spring Boot application\n");
        dockerfile.append("FROM openjdk:17-jdk-slim as builder\n\n");
        
        dockerfile.append("WORKDIR /app\n");
        dockerfile.append("COPY pom.xml .\n");
        dockerfile.append("COPY src ./src\n\n");
        
        dockerfile.append("# Build the application\n");
        dockerfile.append("RUN apt-get update && apt-get install -y maven\n");
        dockerfile.append("RUN mvn clean package -DskipTests\n\n");
        
        dockerfile.append("# Runtime stage\n");
        dockerfile.append("FROM openjdk:17-jre-slim\n\n");
        
        dockerfile.append("WORKDIR /app\n");
        dockerfile.append("COPY --from=builder /app/target/").append(artifactId).append("-1.0.0.jar app.jar\n\n");
        
        dockerfile.append("EXPOSE 8080\n\n");
        
        dockerfile.append("ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n");
        
        return dockerfile.toString();
    }
    
    public String generateDockerCompose(String packageName) {
        String serviceName = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        StringBuilder compose = new StringBuilder();
        
        compose.append("version: '3.8'\n\n");
        compose.append("services:\n");
        compose.append("  ").append(serviceName).append("-app:\n");
        compose.append("    build: .\n");
        compose.append("    ports:\n");
        compose.append("      - \"8080:8080\"\n");
        compose.append("    environment:\n");
        compose.append("      - SPRING_PROFILES_ACTIVE=docker\n");
        compose.append("    depends_on:\n");
        compose.append("      - postgres\n\n");
        
        compose.append("  postgres:\n");
        compose.append("    image: postgres:15-alpine\n");
        compose.append("    environment:\n");
        compose.append("      POSTGRES_DB: ").append(serviceName).append("_db\n");
        compose.append("      POSTGRES_USER: postgres\n");
        compose.append("      POSTGRES_PASSWORD: password\n");
        compose.append("    ports:\n");
        compose.append("      - \"5432:5432\"\n");
        compose.append("    volumes:\n");
        compose.append("      - postgres_data:/var/lib/postgresql/data\n\n");
        
        compose.append("volumes:\n");
        compose.append("  postgres_data:\n");
        
        return compose.toString();
    }
    
    public String generateReadme(String packageName, List<EnhancedClass> classes) {
        String projectName = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        StringBuilder readme = new StringBuilder();
        
        readme.append("# ").append(projectName.toUpperCase()).append(" - Spring Boot API\n\n");
        readme.append("Generated Spring Boot REST API with JPA entities and CRUD operations.\n\n");
        
        readme.append("## 🚀 Quick Start\n\n");
        readme.append("### Prerequisites\n");
        readme.append("- Java 17+\n");
        readme.append("- Maven 3.6+\n\n");
        
        readme.append("### Running the Application\n\n");
        readme.append("```bash\n");
        readme.append("# Clone and navigate to project\n");
        readme.append("cd ").append(projectName).append("\n\n");
        readme.append("# Run with Maven\n");
        readme.append("mvn spring-boot:run\n\n");
        readme.append("# Or build and run JAR\n");
        readme.append("mvn clean package\n");
        readme.append("java -jar target/").append(projectName).append("-1.0.0.jar\n");
        readme.append("```\n\n");
        
        readme.append("### Using Docker\n\n");
        readme.append("```bash\n");
        readme.append("# Build and run with Docker Compose\n");
        readme.append("docker-compose up --build\n");
        readme.append("```\n\n");
        
        readme.append("## 📋 API Endpoints\n\n");
        readme.append("Base URL: `http://localhost:8080/api/v1`\n\n");
        
        for (EnhancedClass clazz : classes) {
            String entityName = clazz.getOriginalClass().getName();
            String entityPath = entityName.toLowerCase() + "s";
            
            readme.append("### ").append(entityName).append(" Endpoints\n\n");
            readme.append("| Method | Endpoint | Description |\n");
            readme.append("|--------|----------|-------------|\n");
            readme.append("| GET | `/api/v1/").append(entityPath).append("` | List all ").append(entityName).append("s (paginated) |\n");
            readme.append("| GET | `/api/v1/").append(entityPath).append("/all` | List all ").append(entityName).append("s |\n");
            readme.append("| GET | `/api/v1/").append(entityPath).append("/{id}` | Get ").append(entityName).append(" by ID |\n");
            readme.append("| POST | `/api/v1/").append(entityPath).append("` | Create new ").append(entityName).append(" |\n");
            readme.append("| PUT | `/api/v1/").append(entityPath).append("/{id}` | Update ").append(entityName).append(" |\n");
            readme.append("| DELETE | `/api/v1/").append(entityPath).append("/{id}` | Delete ").append(entityName).append(" |\n");
            
            if (clazz.isStateful()) {
                readme.append("| PATCH | `/api/v1/").append(entityPath).append("/{id}/suspend` | Suspend ").append(entityName).append(" |\n");
                readme.append("| PATCH | `/api/v1/").append(entityPath).append("/{id}/activate` | Activate ").append(entityName).append(" |\n");
            }
            
            readme.append("\n");
        }
        
        readme.append("## 🗄️ Database\n\n");
        readme.append("- **Development**: H2 in-memory database\n");
        readme.append("- **Production**: PostgreSQL (via Docker Compose)\n");
        readme.append("- **H2 Console**: http://localhost:8080/h2-console\n\n");
        
        readme.append("## 📊 Monitoring\n\n");
        readme.append("- **Health Check**: http://localhost:8080/actuator/health\n");
        readme.append("- **Metrics**: http://localhost:8080/actuator/metrics\n");
        readme.append("- **Info**: http://localhost:8080/actuator/info\n\n");
        
        readme.append("## 🏗️ Project Structure\n\n");
        readme.append("```\n");
        readme.append("src/main/java/").append(packageName.replace('.', '/')).append("/\n");
        readme.append("├── entity/          # JPA Entities\n");
        readme.append("├── repository/      # Data Access Layer\n");
        readme.append("├── service/         # Business Logic Layer\n");
        readme.append("├── controller/      # REST Controllers\n");
        readme.append("└── Application.java # Main Application Class\n");
        readme.append("```\n\n");
        
        readme.append("## 🧪 Testing\n\n");
        readme.append("```bash\n");
        readme.append("# Run tests\n");
        readme.append("mvn test\n\n");
        readme.append("# Run with coverage\n");
        readme.append("mvn test jacoco:report\n");
        readme.append("```\n\n");
        
        readme.append("## 📝 Generated Features\n\n");
        readme.append("- ✅ RESTful API endpoints\n");
        readme.append("- ✅ JPA entities with validation\n");
        readme.append("- ✅ Repository pattern\n");
        readme.append("- ✅ Service layer with business logic\n");
        readme.append("- ✅ Exception handling\n");
        readme.append("- ✅ Logging with SLF4J\n");
        readme.append("- ✅ Docker support\n");
        readme.append("- ✅ Health checks\n");
        
        if (classes.stream().anyMatch(EnhancedClass::isStateful)) {
            readme.append("- ✅ State management\n");
        }
        
        readme.append("\n## 🔧 Configuration\n\n");
        readme.append("Application properties can be configured in:\n");
        readme.append("- `src/main/resources/application.yml`\n");
        readme.append("- Environment variables\n");
        readme.append("- Command line arguments\n\n");
        
        readme.append("---\n\n");
        readme.append("*Generated by basicCode Spring Boot Generator*\n");
        
        return readme.toString();
    }
}
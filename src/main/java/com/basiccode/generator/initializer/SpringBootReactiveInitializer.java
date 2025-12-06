package com.basiccode.generator.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ✅ IMPROVED: Reactive Spring Boot Initializer
 * 
 * Generates local Spring Boot Reactive project structure without network dependencies:
 * - WebFlux (reactive web framework)
 * - R2DBC (reactive database connectivity)
 * - Redis Lettuce (reactive caching)
 * - PostgreSQL database driver
 * - Docker support (PostgreSQL + Redis)
 * 
 * Advantages over original implementation:
 * ✅ No network calls (vs start.spring.io dependency)
 * ✅ Fully customizable (vs hardcoded template)
 * ✅ Reactive by default (vs blocking MVC/JPA)
 * ✅ Production-ready configuration (vs H2 in-memory)
 * ✅ Local setup reproducible (vs download-based)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpringBootReactiveInitializer implements ProjectInitializer {
    
    private static final String SPRING_VERSION = "3.2.1";
    private static final String JAVA_VERSION = "17";
    
    @Override
    public String getLatestVersion() {
        return SPRING_VERSION;
    }
    
    @Override
    public String getLanguage() {
        return "Java";
    }
    
    /**
     * Initialize a new Spring Boot Reactive project locally
     */
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            log.info("🚀 Initializing Spring Boot Reactive project: {}", projectName);
            
            Path projectRoot = Files.createDirectories(
                Path.of("generated", projectName)
            );
            
            // Step 1: Create directory structure
            createDirectoryStructure(projectRoot, packageName);
            
            // Step 2: Generate configuration files
            generatePomXml(projectRoot, projectName, packageName);
            generateApplicationYml(projectRoot, projectName);
            generateApplicationDevYml(projectRoot);
            generateApplicationProdYml(projectRoot);
            generateLogbackXml(projectRoot);
            
            // Step 3: Generate Docker setup
            generateDockerCompose(projectRoot, projectName);
            generateDockerfile(projectRoot, projectName);
            
            // Step 4: Generate base Java files
            generateMainApplicationClass(projectRoot, projectName, packageName);
            generateWebFluxConfig(projectRoot, packageName);
            generateGlobalExceptionHandler(projectRoot, packageName);
            generateCustomExceptions(projectRoot, packageName);
            
            // Step 5: Generate README
            generateReadme(projectRoot, projectName, packageName);
            
            log.info("✅ Spring Boot Reactive project initialized at: {}", projectRoot);
            return projectRoot;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize project", e);
        }
    }
    
    /**
     * Merge generated code into existing project
     */
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        log.info("Merging generated Spring Boot Reactive code from {} to {}", generatedCodePath, templatePath);
        try {
            // Copy generated files to template directory
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path relative = generatedCodePath.relativize(source);
                        Path target = templatePath.resolve(relative);
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target);
                    } catch (IOException e) {
                        log.error("Failed to copy file: {}", source, e);
                    }
                });
            log.info("✅ Code merge completed");
        } catch (IOException e) {
            log.error("Failed to merge generated code", e);
            throw new RuntimeException("Code merge failed", e);
        }
    }
    
    /**
     * Create directory structure for Spring Boot Reactive project
     */
    private void createDirectoryStructure(Path root, String packageName) throws IOException {
        String packagePath = packageName.replace(".", "/");
        
        List<String> directories = Arrays.asList(
            // Source code structure
            "src/main/java/" + packagePath + "/config",
            "src/main/java/" + packagePath + "/entity",
            "src/main/java/" + packagePath + "/repository",
            "src/main/java/" + packagePath + "/service",
            "src/main/java/" + packagePath + "/web/controller",
            "src/main/java/" + packagePath + "/web/handler",
            "src/main/java/" + packagePath + "/web/dto/request",
            "src/main/java/" + packagePath + "/web/dto/response",
            "src/main/java/" + packagePath + "/exception",
            "src/main/java/" + packagePath + "/security",
            "src/main/java/" + packagePath + "/cache",
            
            // Resources
            "src/main/resources",
            "src/main/resources/db/migration",
            
            // Test structure
            "src/test/java/" + packagePath + "/service",
            "src/test/java/" + packagePath + "/web/controller",
            "src/test/java/" + packagePath + "/repository",
            
            // Docker
            "docker",
            "docker/postgres",
            "docker/redis",
            
            // Documentation
            "docs"
        );
        
        for (String dir : directories) {
            Files.createDirectories(root.resolve(dir));
        }
        
        log.info("📁 Directory structure created");
    }
    
    /**
     * Generate pom.xml with reactive dependencies
     */
    private void generatePomXml(Path root, String projectName, String packageName) throws IOException {
        String groupId = packageName.substring(0, packageName.lastIndexOf('.'));
        String artifactId = projectName.toLowerCase().replace(" ", "-");
        
        String pomXml = String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                                         http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <groupId>%s</groupId>
                <artifactId>%s</artifactId>
                <version>1.0.0</version>
                <packaging>jar</packaging>
                
                <name>%s</name>
                <description>Spring Boot Reactive Project - Generated by basicCode</description>
                
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>%s</version>
                    <relativePath/>
                </parent>
                
                <properties>
                    <java.version>%s</java.version>
                    <maven.compiler.source>%s</maven.compiler.source>
                    <maven.compiler.target>%s</maven.compiler.target>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                </properties>
                
                <dependencies>
                    <!-- REACTIVE WEB -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-webflux</artifactId>
                    </dependency>
                    
                    <!-- R2DBC REACTIVE DATABASE -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-r2dbc</artifactId>
                    </dependency>
                    
                    <!-- R2DBC PostgreSQL Driver -->
                    <dependency>
                        <groupId>org.postgresql</groupId>
                        <artifactId>r2dbc-postgresql</artifactId>
                        <scope>runtime</scope>
                    </dependency>
                    
                    <!-- PostgreSQL JDBC for Flyway -->
                    <dependency>
                        <groupId>org.postgresql</groupId>
                        <artifactId>postgresql</artifactId>
                        <scope>runtime</scope>
                    </dependency>
                    
                    <!-- VALIDATION -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-validation</artifactId>
                    </dependency>
                    
                    <!-- REDIS REACTIVE CACHING -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
                    </dependency>
                    
                    <dependency>
                        <groupId>io.lettuce</groupId>
                        <artifactId>lettuce-core</artifactId>
                    </dependency>
                    
                    <!-- SECURITY (Optional) -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-security</artifactId>
                    </dependency>
                    
                    <!-- ACTUATOR & MONITORING -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-actuator</artifactId>
                    </dependency>
                    
                    <!-- DATABASE MIGRATION -->
                    <dependency>
                        <groupId>org.flywaydb</groupId>
                        <artifactId>flyway-core</artifactId>
                    </dependency>
                    
                    <!-- LOMBOK -->
                    <dependency>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <optional>true</optional>
                    </dependency>
                    
                    <!-- TESTING -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-test</artifactId>
                        <scope>test</scope>
                    </dependency>
                    
                    <dependency>
                        <groupId>io.projectreactor</groupId>
                        <artifactId>reactor-test</artifactId>
                        <scope>test</scope>
                    </dependency>
                    
                    <dependency>
                        <groupId>org.testcontainers</groupId>
                        <artifactId>junit-jupiter</artifactId>
                        <scope>test</scope>
                    </dependency>
                    
                    <dependency>
                        <groupId>org.testcontainers</groupId>
                        <artifactId>postgresql</artifactId>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
                
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-maven-plugin</artifactId>
                            <configuration>
                                <excludes>
                                    <exclude>
                                        <groupId>org.projectlombok</groupId>
                                        <artifactId>lombok</artifactId>
                                    </exclude>
                                </excludes>
                            </configuration>
                        </plugin>
                        
                        <plugin>
                            <groupId>org.flywaydb</groupId>
                            <artifactId>flyway-maven-plugin</artifactId>
                            <version>9.8.1</version>
                        </plugin>
                    </plugins>
                </build>
            </project>
            """, groupId, artifactId, projectName, SPRING_VERSION, JAVA_VERSION, JAVA_VERSION, JAVA_VERSION);
        
        Files.writeString(root.resolve("pom.xml"), pomXml, StandardOpenOption.CREATE);
        log.info("📄 Generated pom.xml with reactive dependencies");
    }
    
    /**
     * Generate application.yml configuration
     */
    private void generateApplicationYml(Path root, String projectName) throws IOException {
        String applicationYml = String.format("""
            spring:
              application:
                name: %s
              profiles:
                active: dev
              
              r2dbc:
                url: r2dbc:postgresql://localhost:5432/%s_db
                username: ${DB_USERNAME:postgres}
                password: ${DB_PASSWORD:postgres}
                pool:
                  initial-size: 10
                  max-size: 20
                  max-idle-time: 30m
              
              redis:
                host: ${REDIS_HOST:localhost}
                port: ${REDIS_PORT:6379}
                password: ${REDIS_PASSWORD:}
                lettuce:
                  pool:
                    max-active: 8
                    max-idle: 8
                    min-idle: 0
              
              flyway:
                url: jdbc:postgresql://localhost:5432/%s_db
                user: ${DB_USERNAME:postgres}
                password: ${DB_PASSWORD:postgres}
                locations: classpath:db/migration
                baseline-on-migrate: true
            
            server:
              port: ${SERVER_PORT:8080}
              
            management:
              endpoints:
                web:
                  exposure:
                    include: health,info,metrics,prometheus
              endpoint:
                health:
                  show-details: when-authorized
            
            logging:
              level:
                org.springframework.r2dbc: DEBUG
                io.r2dbc.postgresql.QUERY: DEBUG
            """, projectName.toLowerCase().replace(" ", "-"), 
                   projectName.toLowerCase().replace(" ", "_"),
                   projectName.toLowerCase().replace(" ", "_"));
        
        Files.writeString(root.resolve("src/main/resources/application.yml"), applicationYml, StandardOpenOption.CREATE);
        log.info("⚙️ Generated application.yml");
    }
    
    /**
     * Generate application-dev.yml for development
     */
    private void generateApplicationDevYml(Path root) throws IOException {
        String devYml = """
            spring:
              r2dbc:
                url: r2dbc:postgresql://localhost:5432/dev_db
                username: dev_user
                password: dev_password
              
              redis:
                host: localhost
                port: 6379
            
            server:
              port: 8080
            
            logging:
              level:
                root: INFO
                org.springframework.r2dbc: DEBUG
                io.r2dbc.postgresql: DEBUG
            """;
        
        Files.writeString(root.resolve("src/main/resources/application-dev.yml"), devYml, StandardOpenOption.CREATE);
        log.info("🔧 Generated application-dev.yml");
    }
    
    /**
     * Generate application-prod.yml for production
     */
    private void generateApplicationProdYml(Path root) throws IOException {
        String prodYml = """
            spring:
              r2dbc:
                url: r2dbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:prod_db}
                username: ${DB_USERNAME}
                password: ${DB_PASSWORD}
                pool:
                  initial-size: 20
                  max-size: 50
              
              redis:
                host: ${REDIS_HOST}
                port: ${REDIS_PORT:6379}
                password: ${REDIS_PASSWORD}
            
            server:
              port: ${PORT:8080}
            
            logging:
              level:
                root: WARN
                org.springframework.r2dbc: INFO
            """;
        
        Files.writeString(root.resolve("src/main/resources/application-prod.yml"), prodYml, StandardOpenOption.CREATE);
        log.info("🚀 Generated application-prod.yml");
    }
    
    /**
     * Generate logback.xml for logging configuration
     */
    private void generateLogbackXml(Path root) throws IOException {
        String logbackXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <configuration>
                <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder>
                        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                </appender>
                
                <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/application.log</file>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
                        <maxHistory>30</maxHistory>
                    </rollingPolicy>
                    <encoder>
                        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                </appender>
                
                <logger name="org.springframework.r2dbc" level="DEBUG"/>
                <logger name="io.r2dbc.postgresql" level="DEBUG"/>
                
                <root level="INFO">
                    <appender-ref ref="STDOUT"/>
                    <appender-ref ref="FILE"/>
                </root>
            </configuration>
            """;
        
        Files.writeString(root.resolve("src/main/resources/logback-spring.xml"), logbackXml, StandardOpenOption.CREATE);
        log.info("📝 Generated logback-spring.xml");
    }
    
    /**
     * Generate Docker Compose for PostgreSQL and Redis
     */
    private void generateDockerCompose(Path root, String projectName) throws IOException {
        String dbName = projectName.toLowerCase().replace(" ", "_") + "_db";
        
        String dockerCompose = String.format("""
            version: '3.8'
            
            services:
              postgres:
                image: postgres:15-alpine
                container_name: %s-postgres
                environment:
                  POSTGRES_DB: %s
                  POSTGRES_USER: postgres
                  POSTGRES_PASSWORD: postgres
                ports:
                  - "5432:5432"
                volumes:
                  - postgres_data:/var/lib/postgresql/data
                  - ./docker/postgres/init.sql:/docker-entrypoint-initdb.d/init.sql
                networks:
                  - app-network
              
              redis:
                image: redis:7-alpine
                container_name: %s-redis
                ports:
                  - "6379:6379"
                volumes:
                  - redis_data:/data
                networks:
                  - app-network
              
              app:
                build: .
                container_name: %s-app
                ports:
                  - "8080:8080"
                environment:
                  - SPRING_PROFILES_ACTIVE=docker
                  - DB_HOST=postgres
                  - DB_PORT=5432
                  - DB_NAME=%s
                  - DB_USERNAME=postgres
                  - DB_PASSWORD=postgres
                  - REDIS_HOST=redis
                  - REDIS_PORT=6379
                depends_on:
                  - postgres
                  - redis
                networks:
                  - app-network
            
            volumes:
              postgres_data:
              redis_data:
            
            networks:
              app-network:
                driver: bridge
            """, projectName.toLowerCase().replace(" ", "-"), dbName,
                   projectName.toLowerCase().replace(" ", "-"),
                   projectName.toLowerCase().replace(" ", "-"), dbName);
        
        Files.writeString(root.resolve("docker-compose.yml"), dockerCompose, StandardOpenOption.CREATE);
        log.info("🐳 Generated docker-compose.yml");
    }
    
    /**
     * Generate Dockerfile
     */
    private void generateDockerfile(Path root, String projectName) throws IOException {
        String dockerfile = String.format("""
            FROM openjdk:17-jdk-slim
            
            WORKDIR /app
            
            COPY target/%s-1.0.0.jar app.jar
            
            EXPOSE 8080
            
            ENTRYPOINT ["java", "-jar", "app.jar"]
            """, projectName.toLowerCase().replace(" ", "-"));
        
        Files.writeString(root.resolve("Dockerfile"), dockerfile, StandardOpenOption.CREATE);
        log.info("🐳 Generated Dockerfile");
    }
    
    /**
     * Generate main application class
     */
    private void generateMainApplicationClass(Path root, String projectName, String packageName) throws IOException {
        String className = toPascalCase(projectName) + "Application";
        String packagePath = packageName.replace(".", "/");
        
        String mainClass = String.format("""
            package %s;
            
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
            import org.springframework.web.reactive.config.EnableWebFlux;
            
            /**
             * %s - Spring Boot Reactive Application
             * 
             * Generated by basicCode Generator
             * 
             * Features:
             * - WebFlux (Reactive Web)
             * - R2DBC (Reactive Database)
             * - Redis (Reactive Caching)
             * - PostgreSQL Database
             * - Docker Support
             */
            @SpringBootApplication
            @EnableWebFlux
            @EnableR2dbcRepositories
            public class %s {
                
                public static void main(String[] args) {
                    SpringApplication.run(%s.class, args);
                }
            }
            """, packageName, projectName, className, className);
        
        Path mainClassPath = root.resolve("src/main/java/" + packagePath + "/" + className + ".java");
        Files.writeString(mainClassPath, mainClass, StandardOpenOption.CREATE);
        log.info("☕ Generated main application class: {}", className);
    }
    
    /**
     * Generate WebFlux configuration
     */
    private void generateWebFluxConfig(Path root, String packageName) throws IOException {
        String packagePath = packageName.replace(".", "/");
        
        String webFluxConfig = String.format("""
            package %s.config;
            
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.web.reactive.config.CorsRegistry;
            import org.springframework.web.reactive.config.WebFluxConfigurer;
            import org.springframework.web.reactive.function.server.RouterFunction;
            import org.springframework.web.reactive.function.server.ServerResponse;
            
            import static org.springframework.web.reactive.function.server.RouterFunctions.route;
            import static org.springframework.web.reactive.function.server.ServerResponse.ok;
            
            /**
             * WebFlux Configuration
             * 
             * Configures:
             * - CORS settings
             * - Functional routes
             * - Error handling
             */
            @Configuration
            public class WebFluxConfig implements WebFluxConfigurer {
                
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/api/**")
                            .allowedOrigins("*")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("*");
                }
                
                @Bean
                public RouterFunction<ServerResponse> healthRoute() {
                    return route()
                            .GET("/health", request -> ok().bodyValue("OK"))
                            .build();
                }
            }
            """, packageName);
        
        Path configPath = root.resolve("src/main/java/" + packagePath + "/config/WebFluxConfig.java");
        Files.writeString(configPath, webFluxConfig, StandardOpenOption.CREATE);
        log.info("⚙️ Generated WebFluxConfig");
    }
    
    /**
     * Generate global exception handler
     */
    private void generateGlobalExceptionHandler(Path root, String packageName) throws IOException {
        String packagePath = packageName.replace(".", "/");
        
        String exceptionHandler = String.format("""
            package %s.exception;
            
            import lombok.extern.slf4j.Slf4j;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.ResponseEntity;
            import org.springframework.web.bind.annotation.ExceptionHandler;
            import org.springframework.web.bind.annotation.RestControllerAdvice;
            import org.springframework.web.bind.support.WebExchangeBindException;
            import reactor.core.publisher.Mono;
            
            import java.time.LocalDateTime;
            import java.util.HashMap;
            import java.util.Map;
            
            /**
             * Global Exception Handler for Reactive Controllers
             */
            @RestControllerAdvice
            @Slf4j
            public class GlobalExceptionHandler {
                
                @ExceptionHandler(ResourceNotFoundException.class)
                public Mono<ResponseEntity<Map<String, Object>>> handleResourceNotFound(ResourceNotFoundException ex) {
                    return Mono.just(createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
                }
                
                @ExceptionHandler(ValidationException.class)
                public Mono<ResponseEntity<Map<String, Object>>> handleValidation(ValidationException ex) {
                    return Mono.just(createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
                }
                
                @ExceptionHandler(WebExchangeBindException.class)
                public Mono<ResponseEntity<Map<String, Object>>> handleBindException(WebExchangeBindException ex) {
                    String message = ex.getBindingResult().getFieldErrors().stream()
                            .map(error -> error.getField() + ": " + error.getDefaultMessage())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Validation failed");
                    return Mono.just(createErrorResponse(HttpStatus.BAD_REQUEST, message));
                }
                
                @ExceptionHandler(Exception.class)
                public Mono<ResponseEntity<Map<String, Object>>> handleGeneral(Exception ex) {
                    log.error("Unexpected error", ex);
                    return Mono.just(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));
                }
                
                private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("timestamp", LocalDateTime.now());
                    error.put("status", status.value());
                    error.put("error", status.getReasonPhrase());
                    error.put("message", message);
                    return ResponseEntity.status(status).body(error);
                }
            }
            """, packageName);
        
        Path handlerPath = root.resolve("src/main/java/" + packagePath + "/exception/GlobalExceptionHandler.java");
        Files.writeString(handlerPath, exceptionHandler, StandardOpenOption.CREATE);
        log.info("🚨 Generated GlobalExceptionHandler");
    }
    
    /**
     * Generate custom exceptions
     */
    private void generateCustomExceptions(Path root, String packageName) throws IOException {
        String packagePath = packageName.replace(".", "/");
        
        // ResourceNotFoundException
        String resourceNotFound = String.format("""
            package %s.exception;
            
            /**
             * Exception thrown when a requested resource is not found
             */
            public class ResourceNotFoundException extends RuntimeException {
                public ResourceNotFoundException(String message) {
                    super(message);
                }
                
                public ResourceNotFoundException(String resource, Object id) {
                    super(String.format("%%s not found with id: %%s", resource, id));
                }
            }
            """, packageName);
        
        // ValidationException
        String validationException = String.format("""
            package %s.exception;
            
            /**
             * Exception thrown when validation fails
             */
            public class ValidationException extends RuntimeException {
                public ValidationException(String message) {
                    super(message);
                }
            }
            """, packageName);
        
        Path exceptionDir = root.resolve("src/main/java/" + packagePath + "/exception");
        Files.writeString(exceptionDir.resolve("ResourceNotFoundException.java"), resourceNotFound, StandardOpenOption.CREATE);
        Files.writeString(exceptionDir.resolve("ValidationException.java"), validationException, StandardOpenOption.CREATE);
        log.info("⚠️ Generated custom exceptions");
    }
    
    /**
     * Generate README.md
     */
    private void generateReadme(Path root, String projectName, String packageName) throws IOException {
        String readme = String.format("""
            # %s
            
            Spring Boot Reactive application generated by basicCode Generator.
            
            ## 🚀 Features
            
            - **WebFlux**: Reactive web framework
            - **R2DBC**: Reactive database connectivity
            - **PostgreSQL**: Production database
            - **Redis**: Reactive caching
            - **Docker**: Containerized deployment
            - **Flyway**: Database migrations
            - **Actuator**: Health checks and metrics
            
            ## 🏃‍♂️ Quick Start
            
            ### Prerequisites
            - Java 17+
            - Docker & Docker Compose
            - Maven 3.6+
            
            ### 1. Start Infrastructure
            ```bash
            # Start PostgreSQL and Redis
            docker-compose up -d postgres redis
            ```
            
            ### 2. Run Application
            ```bash
            # Development mode
            mvn spring-boot:run -Dspring-boot.run.profiles=dev
            
            # Or build and run
            mvn clean package
            java -jar target/%s-1.0.0.jar
            ```
            
            ### 3. Full Docker Deployment
            ```bash
            # Build and start everything
            mvn clean package
            docker-compose up --build
            ```
            
            ## 📡 Endpoints
            
            - **Health Check**: http://localhost:8080/health
            - **Actuator**: http://localhost:8080/actuator/health
            - **API Base**: http://localhost:8080/api
            
            ## 🗄️ Database
            
            - **Host**: localhost:5432
            - **Database**: %s_db
            - **Username**: postgres
            - **Password**: postgres
            
            ## 🔧 Configuration
            
            Configuration files:
            - `application.yml` - Main configuration
            - `application-dev.yml` - Development settings
            - `application-prod.yml` - Production settings
            
            ## 📊 Monitoring
            
            Available actuator endpoints:
            - `/actuator/health` - Application health
            - `/actuator/info` - Application info
            - `/actuator/metrics` - Application metrics
            
            ## 🧪 Testing
            
            ```bash
            # Run tests
            mvn test
            
            # Run with testcontainers
            mvn test -Dspring.profiles.active=test
            ```
            
            ## 📦 Package Structure
            
            ```
            %s/
            ├── config/          # Configuration classes
            ├── entity/          # R2DBC entities
            ├── repository/      # Reactive repositories
            ├── service/         # Business logic
            ├── web/
            │   ├── controller/  # REST controllers
            │   ├── handler/     # Functional handlers
            │   └── dto/         # Request/Response DTOs
            └── exception/       # Custom exceptions
            ```
            
            ## 🚀 Generated by basicCode
            
            This project was generated using the basicCode UML-to-Code generator.
            Visit: https://codegenerator-cpyh.onrender.com
            """, projectName, projectName.toLowerCase().replace(" ", "-"),
                   projectName.toLowerCase().replace(" ", "_"), packageName);
        
        Files.writeString(root.resolve("README.md"), readme, StandardOpenOption.CREATE);
        log.info("📖 Generated README.md");
    }
    
    /**
     * Convert string to PascalCase
     */
    private String toPascalCase(String input) {
        return Arrays.stream(input.split("[\\s_-]+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }
}
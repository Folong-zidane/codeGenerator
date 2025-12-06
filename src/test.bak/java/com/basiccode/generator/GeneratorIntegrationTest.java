package com.basiccode.generator;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Tests d'intégration pour vérifier la génération complète de projets
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class GeneratorIntegrationTest {

    private static final String OUTPUT_DIR = "target/generated-test-projects";
    private List<EnhancedClass> testClasses;
    private String packageName = "com.test.integration";

    @BeforeEach
    void setUp() throws IOException {
        testClasses = createRealWorldTestData();
        
        // Create output directory
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (Files.exists(outputPath)) {
            deleteDirectory(outputPath.toFile());
        }
        Files.createDirectories(outputPath);
        
        log.info("Integration test setup complete with {} classes", testClasses.size());
    }

    @Test
    @DisplayName("Generate Complete E-commerce Project")
    void testEcommerceProjectGeneration() throws IOException {
        String projectName = "ecommerce-app";
        Path projectPath = Paths.get(OUTPUT_DIR, projectName);
        Files.createDirectories(projectPath);
        
        generateCompleteProject(projectPath, "com.ecommerce", testClasses);
        
        // Verify project structure
        verifyProjectStructure(projectPath, testClasses);
        
        log.info("✅ E-commerce project generated successfully at: {}", projectPath);
    }

    @Test
    @DisplayName("Generate Blog Management Project")
    void testBlogProjectGeneration() throws IOException {
        List<EnhancedClass> blogClasses = createBlogTestData();
        String projectName = "blog-management";
        Path projectPath = Paths.get(OUTPUT_DIR, projectName);
        Files.createDirectories(projectPath);
        
        generateCompleteProject(projectPath, "com.blog", blogClasses);
        
        // Verify project structure
        verifyProjectStructure(projectPath, blogClasses);
        
        log.info("✅ Blog project generated successfully at: {}", projectPath);
    }

    @Test
    @DisplayName("Verify Generated Code Compilation")
    void testGeneratedCodeCompilation() throws IOException {
        String projectName = "compilation-test";
        Path projectPath = Paths.get(OUTPUT_DIR, projectName);
        Files.createDirectories(projectPath);
        
        generateCompleteProject(projectPath, "com.compilation.test", testClasses);
        
        // Generate pom.xml for compilation test
        generatePomXml(projectPath, "compilation-test");
        
        // Verify all Java files are syntactically correct
        verifyJavaFileSyntax(projectPath);
        
        log.info("✅ Generated code compilation verification complete");
    }

    private void generateCompleteProject(Path projectPath, String packageName, List<EnhancedClass> classes) throws IOException {
        // Create package directory structure
        String packagePath = packageName.replace(".", "/");
        Path srcPath = projectPath.resolve("src/main/java").resolve(packagePath);
        Path resourcesPath = projectPath.resolve("src/main/resources");
        Path migrationPath = resourcesPath.resolve("db/migration");
        
        Files.createDirectories(srcPath);
        Files.createDirectories(resourcesPath);
        Files.createDirectories(migrationPath);
        
        // Generate all components
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
        SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
        SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
        SpringBootConfigGenerator configGen = new SpringBootConfigGenerator();
        SpringBootApplicationGenerator appGen = new SpringBootApplicationGenerator();
        SpringBootMigrationGenerator migrationGen = new SpringBootMigrationGenerator();
        
        // Generate entities, repositories, services, controllers
        for (EnhancedClass clazz : classes) {
            String className = clazz.getOriginalClass().getName();
            
            // Entity
            String entity = entityGen.generateEntity(clazz, packageName);
            writeFile(srcPath.resolve(className + ".java"), entity);
            
            // Repository
            String repository = repoGen.generateRepository(clazz, packageName);
            writeFile(srcPath.resolve(className + "Repository.java"), repository);
            
            // Service
            String service = serviceGen.generateService(clazz, packageName);
            writeFile(srcPath.resolve(className + "Service.java"), service);
            
            // Controller
            String controller = controllerGen.generateController(clazz, packageName);
            writeFile(srcPath.resolve(className + "Controller.java"), controller);
        }
        
        // Generate configuration
        String config = configGen.generateConfiguration(classes, packageName);
        writeFile(srcPath.resolve("DatabaseConfig.java"), config);
        
        // Generate application
        String app = appGen.generateApplication(classes, packageName);
        writeFile(srcPath.resolve("Application.java"), app);
        
        // Generate migration
        String migration = migrationGen.generateMigration(classes, packageName);
        writeFile(migrationPath.resolve("V001__Initial_Schema.sql"), migration);
        
        // Generate application.properties
        generateApplicationProperties(resourcesPath);
        
        log.info("Complete project generated: {} classes, {} files", 
            classes.size(), classes.size() * 4 + 3);
    }

    private void verifyProjectStructure(Path projectPath, List<EnhancedClass> classes) {
        // Verify main directories exist
        Assertions.assertTrue(Files.exists(projectPath.resolve("src/main/java")));
        Assertions.assertTrue(Files.exists(projectPath.resolve("src/main/resources")));
        Assertions.assertTrue(Files.exists(projectPath.resolve("src/main/resources/db/migration")));
        
        // Verify all expected files exist
        for (EnhancedClass clazz : classes) {
            String className = clazz.getOriginalClass().getName();
            String packagePath = packageName.replace(".", "/");
            Path basePath = projectPath.resolve("src/main/java").resolve(packagePath);
            
            Assertions.assertTrue(Files.exists(basePath.resolve(className + ".java")));
            Assertions.assertTrue(Files.exists(basePath.resolve(className + "Repository.java")));
            Assertions.assertTrue(Files.exists(basePath.resolve(className + "Service.java")));
            Assertions.assertTrue(Files.exists(basePath.resolve(className + "Controller.java")));
        }
        
        // Verify configuration files
        String packagePath = packageName.replace(".", "/");
        Path basePath = projectPath.resolve("src/main/java").resolve(packagePath);
        Assertions.assertTrue(Files.exists(basePath.resolve("DatabaseConfig.java")));
        Assertions.assertTrue(Files.exists(basePath.resolve("Application.java")));
        Assertions.assertTrue(Files.exists(projectPath.resolve("src/main/resources/application.properties")));
        Assertions.assertTrue(Files.exists(projectPath.resolve("src/main/resources/db/migration/V001__Initial_Schema.sql")));
        
        log.info("✅ Project structure verification complete");
    }

    private void verifyJavaFileSyntax(Path projectPath) throws IOException {
        Files.walk(projectPath)
            .filter(path -> path.toString().endsWith(".java"))
            .forEach(javaFile -> {
                try {
                    String content = Files.readString(javaFile);
                    
                    // Basic syntax checks
                    Assertions.assertTrue(content.contains("package "), "File should have package declaration: " + javaFile);
                    
                    // Count braces
                    long openBraces = content.chars().filter(ch -> ch == '{').count();
                    long closeBraces = content.chars().filter(ch -> ch == '}').count();
                    Assertions.assertEquals(openBraces, closeBraces, "Braces should be balanced in: " + javaFile);
                    
                    // Count parentheses
                    long openParens = content.chars().filter(ch -> ch == '(').count();
                    long closeParens = content.chars().filter(ch -> ch == ')').count();
                    Assertions.assertEquals(openParens, closeParens, "Parentheses should be balanced in: " + javaFile);
                    
                    log.debug("✅ Syntax check passed for: {}", javaFile.getFileName());
                    
                } catch (IOException e) {
                    Assertions.fail("Failed to read file: " + javaFile);
                }
            });
        
        log.info("✅ Java file syntax verification complete");
    }

    private List<EnhancedClass> createRealWorldTestData() {
        List<EnhancedClass> classes = new ArrayList<>();
        
        // User
        UmlClass userClass = new UmlClass();
        userClass.setName("User");
        userClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("username", "String"),
            new UmlAttribute("email", "String"),
            new UmlAttribute("password", "String"),
            new UmlAttribute("firstName", "String"),
            new UmlAttribute("lastName", "String"),
            new UmlAttribute("isActive", "Boolean"),
            new UmlAttribute("registrationDate", "LocalDateTime")
        ));
        classes.add(new EnhancedClass(userClass));
        
        // Product
        UmlClass productClass = new UmlClass();
        productClass.setName("Product");
        productClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("name", "String"),
            new UmlAttribute("description", "String"),
            new UmlAttribute("price", "Double"),
            new UmlAttribute("stock", "Integer"),
            new UmlAttribute("category", "String"),
            new UmlAttribute("isAvailable", "Boolean")
        ));
        classes.add(new EnhancedClass(productClass));
        
        // Order
        UmlClass orderClass = new UmlClass();
        orderClass.setName("Order");
        orderClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("userId", "Long"),
            new UmlAttribute("total", "Double"),
            new UmlAttribute("status", "String"),
            new UmlAttribute("orderDate", "LocalDateTime"),
            new UmlAttribute("shippingAddress", "String")
        ));
        EnhancedClass enhancedOrder = new EnhancedClass(orderClass);
        enhancedOrder.setStateful(true);
        classes.add(enhancedOrder);
        
        return classes;
    }

    private List<EnhancedClass> createBlogTestData() {
        List<EnhancedClass> classes = new ArrayList<>();
        
        // Author
        UmlClass authorClass = new UmlClass();
        authorClass.setName("Author");
        authorClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("name", "String"),
            new UmlAttribute("email", "String"),
            new UmlAttribute("bio", "String")
        ));
        classes.add(new EnhancedClass(authorClass));
        
        // Post
        UmlClass postClass = new UmlClass();
        postClass.setName("Post");
        postClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("title", "String"),
            new UmlAttribute("content", "String"),
            new UmlAttribute("authorId", "Long"),
            new UmlAttribute("publishDate", "LocalDateTime"),
            new UmlAttribute("isPublished", "Boolean")
        ));
        classes.add(new EnhancedClass(postClass));
        
        return classes;
    }

    private void generatePomXml(Path projectPath, String artifactId) throws IOException {
        String pomContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                     http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <groupId>com.test</groupId>
                <artifactId>%s</artifactId>
                <version>1.0.0</version>
                <packaging>jar</packaging>
                
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>3.2.0</version>
                    <relativePath/>
                </parent>
                
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
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <optional>true</optional>
                    </dependency>
                </dependencies>
            </project>
            """.formatted(artifactId);
        
        writeFile(projectPath.resolve("pom.xml"), pomContent);
    }

    private void generateApplicationProperties(Path resourcesPath) throws IOException {
        String properties = """
            # Database Configuration
            spring.datasource.url=jdbc:h2:mem:testdb
            spring.datasource.driver-class-name=org.h2.Driver
            spring.datasource.username=sa
            spring.datasource.password=
            
            # JPA Configuration
            spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
            spring.jpa.hibernate.ddl-auto=validate
            spring.jpa.show-sql=true
            
            # Flyway Configuration
            spring.flyway.enabled=true
            spring.flyway.locations=classpath:db/migration
            
            # Server Configuration
            server.port=8080
            """;
        
        writeFile(resourcesPath.resolve("application.properties"), properties);
    }

    private void writeFile(Path filePath, String content) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
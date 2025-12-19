package com.basiccode.generator.service;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.generator.django.DjangoUltraPurGenerator;
import com.basiccode.generator.generator.csharp.*;
import com.basiccode.generator.generator.php.*;
import com.basiccode.generator.generator.typescript.*;
import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLRelationship;
import com.basiccode.generator.model.metadata.UMLMetadata;
import com.basiccode.generator.parser.metadata.UMLMetadataParser;
import com.basiccode.generator.parser.metadata.UMLMetadataParserImpl;
import com.basiccode.generator.parser.SimpleUMLParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataAwareGenerationOrchestrator {
    
    private final UMLMetadataParser metadataParser = new UMLMetadataParserImpl();
    private final SimpleUMLParser classParser = new SimpleUMLParser();
    private final MetadataAwareSpringBootGeneratorFactory generatorFactory = new MetadataAwareSpringBootGeneratorFactory();
    private final DjangoUltraPurGenerator djangoGenerator = new DjangoUltraPurGenerator();
    
    public Map<String, String> generateCompleteApplication(String diagramContent, String packageName) {
        return generateCompleteApplication(diagramContent, packageName, "java");
    }
    
    public Map<String, String> generateCompleteApplication(String diagramContent, String packageName, String language) {
        log.info("Starting metadata-aware complete application generation for package: {} in language: {}", packageName, language);
        
        Map<String, String> generatedFiles = new HashMap<>();
        
        try {
            // Parse metadata if available
            UMLMetadata metadata = null;
            if (metadataParser.hasMetadata(diagramContent)) {
                metadata = metadataParser.parseMetadata(diagramContent);
                log.info("Metadata parsed successfully");
            }
            
            // Parse UML classes and relationships
            String cleanDiagram = metadata != null ? metadataParser.extractDiagramContent(diagramContent) : diagramContent;
            List<UMLClass> classes = classParser.parseClasses(cleanDiagram);
            List<UMLRelationship> relationships = classParser.parseRelationships(cleanDiagram);
            log.info("Parsed {} UML classes and {} relationships", classes.size(), relationships.size());
            
            if (classes.isEmpty()) {
                log.warn("No classes found in diagram content: {}", cleanDiagram.substring(0, Math.min(200, cleanDiagram.length())));
                return generatedFiles;
            }
            
            // Route to appropriate generator based on language
            switch (language.toLowerCase()) {
                case "python":
                case "django":
                    return generateDjangoApplication(classes, packageName, metadata);
                case "csharp":
                case "c#":
                    return generateCSharpApplication(classes, packageName, metadata);
                case "php":
                    return generatePhpApplication(classes, packageName, metadata);
                case "typescript":
                case "ts":
                    return generateTypeScriptApplication(classes, packageName, metadata);
                default:
                    // Continue with Java/Spring Boot generation
                    break;
            }
            
            // Generate entities with relationships
            var entityGenerator = generatorFactory.createEntityGenerator();
            for (UMLClass umlClass : classes) {
                String entityCode;
                if (entityGenerator instanceof MetadataAwareSpringBootEntityGenerator) {
                    entityCode = ((MetadataAwareSpringBootEntityGenerator) entityGenerator)
                        .generateEntity(umlClass, packageName, metadata, relationships);
                } else {
                    EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
                    entityCode = entityGenerator.generateEntity(enhancedClass, packageName);
                }
                String fileName = "entity/" + umlClass.getName() + ".java";
                generatedFiles.put(fileName, entityCode);
                log.debug("Generated entity with relationships: {}", fileName);
            }
            
            // Generate repositories
            var repositoryGenerator = generatorFactory.createRepositoryGenerator();
            for (UMLClass umlClass : classes) {
                EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
                String repositoryCode = repositoryGenerator.generateRepository(enhancedClass, packageName);
                String fileName = "repository/" + umlClass.getName() + "Repository.java";
                generatedFiles.put(fileName, repositoryCode);
                log.debug("Generated repository: {}", fileName);
            }
            
            // Generate services
            var serviceGenerator = generatorFactory.createServiceGenerator();
            for (UMLClass umlClass : classes) {
                EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
                String serviceCode = serviceGenerator.generateService(enhancedClass, packageName);
                String fileName = "service/" + umlClass.getName() + "Service.java";
                generatedFiles.put(fileName, serviceCode);
                log.debug("Generated service: {}", fileName);
            }
            
            // Generate controllers
            var controllerGenerator = generatorFactory.createControllerGenerator();
            for (UMLClass umlClass : classes) {
                EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
                String controllerCode = controllerGenerator.generateController(enhancedClass, packageName);
                String fileName = "controller/" + umlClass.getName() + "Controller.java";
                generatedFiles.put(fileName, controllerCode);
                log.debug("Generated controller: {}", fileName);
            }
            
            // Generate application configuration
            generateApplicationFiles(generatedFiles, packageName, metadata);
            
            log.info("Complete application generation finished. Generated {} files", generatedFiles.size());
            
        } catch (Exception e) {
            log.error("Error during metadata-aware generation", e);
            throw new RuntimeException("Generation failed: " + e.getMessage(), e);
        }
        
        return generatedFiles;
    }
    
    private Map<String, String> generateDjangoApplication(List<UMLClass> classes, String packageName, UMLMetadata metadata) {
        log.info("Generating Django ultra-pure application with {} classes", classes.size());
        
        try {
            // Reconstruct diagram content for metadata parsing
            StringBuilder diagramContent = new StringBuilder();
            diagramContent.append("classDiagram\n");
            for (UMLClass umlClass : classes) {
                diagramContent.append("    class ").append(umlClass.getName()).append(" {\n");
                if (umlClass.getAttributes() != null) {
                    for (var attr : umlClass.getAttributes()) {
                        diagramContent.append("        +").append(attr.getType()).append(" ").append(attr.getName()).append("\n");
                    }
                }
                diagramContent.append("    }\n");
            }
            
            // Generate complete Django application with metadata
            String projectName = metadata != null && metadata.getProject() != null ? 
                metadata.getProject().getName().toLowerCase().replace(" ", "-") : "django-app";
            
            Map<String, String> djangoFiles = djangoGenerator.generateCompleteAppWithMetadata(diagramContent.toString(), packageName, projectName);
            
            log.info("Django ultra-pure application generated successfully with {} files", djangoFiles.size());
            return djangoFiles;
            
        } catch (Exception e) {
            log.error("Error generating Django application", e);
            throw new RuntimeException("Django generation failed: " + e.getMessage(), e);
        }
    }
    
    private void generateApplicationFiles(Map<String, String> generatedFiles, String packageName, UMLMetadata metadata) {
        // Generate main application class
        String mainClass = generateMainApplicationClass(packageName);
        generatedFiles.put("Application.java", mainClass);
        
        // Generate application.yml
        String applicationYml = generateApplicationYml(metadata);
        generatedFiles.put("application.yml", applicationYml);
        
        // Generate pom.xml
        String pomXml = generatePomXml(packageName, metadata);
        generatedFiles.put("pom.xml", pomXml);
    }
    
    private String generateMainApplicationClass(String packageName) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import org.springframework.boot.SpringApplication;\n");
        sb.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n");
        sb.append("import org.springframework.data.jpa.repository.config.EnableJpaRepositories;\n\n");
        sb.append("@SpringBootApplication\n");
        sb.append("@EnableJpaRepositories\n");
        sb.append("public class Application {\n\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        SpringApplication.run(Application.class, args);\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }
    
    private String generateApplicationYml(UMLMetadata metadata) {
        StringBuilder sb = new StringBuilder();
        sb.append("spring:\n");
        sb.append("  application:\n");
        sb.append("    name: generated-app\n");
        sb.append("  datasource:\n");
        sb.append("    url: jdbc:h2:mem:testdb\n");
        sb.append("    driver-class-name: org.h2.Driver\n");
        sb.append("    username: sa\n");
        sb.append("    password: \n");
        sb.append("  jpa:\n");
        sb.append("    hibernate:\n");
        sb.append("      ddl-auto: create-drop\n");
        sb.append("    show-sql: true\n");
        sb.append("    properties:\n");
        sb.append("      hibernate:\n");
        sb.append("        format_sql: true\n");
        sb.append("  h2:\n");
        sb.append("    console:\n");
        sb.append("      enabled: true\n");
        sb.append("logging:\n");
        sb.append("  level:\n");
        sb.append("    root: INFO\n");
        sb.append("    org.springframework: DEBUG\n");
        return sb.toString();
    }
    
    private String generatePomXml(String packageName, UMLMetadata metadata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        sb.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        sb.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        sb.append("    <modelVersion>4.0.0</modelVersion>\n\n");
        sb.append("    <groupId>").append(packageName).append("</groupId>\n");
        
        if (metadata != null && metadata.getProject() != null) {
            sb.append("    <artifactId>").append(metadata.getProject().getName().toLowerCase().replace(" ", "-")).append("</artifactId>\n");
            sb.append("    <version>").append(metadata.getProject().getVersion()).append("</version>\n");
            sb.append("    <name>").append(metadata.getProject().getName()).append("</name>\n");
            sb.append("    <description>").append(metadata.getProject().getDescription()).append("</description>\n");
        } else {
            sb.append("    <artifactId>generated-app</artifactId>\n");
            sb.append("    <version>1.0.0</version>\n");
        }
        
        sb.append("    <packaging>jar</packaging>\n\n");
        sb.append("    <parent>\n");
        sb.append("        <groupId>org.springframework.boot</groupId>\n");
        sb.append("        <artifactId>spring-boot-starter-parent</artifactId>\n");
        
        String springVersion = "3.2.0";
        if (metadata != null && metadata.getTechStack() != null && metadata.getTechStack().getBackendVersion() != null) {
            springVersion = metadata.getTechStack().getBackendVersion();
        }
        sb.append("        <version>").append(springVersion).append("</version>\n");
        sb.append("        <relativePath/>\n");
        sb.append("    </parent>\n\n");
        sb.append("    <properties>\n");
        
        String javaVersion = "17";
        if (metadata != null && metadata.getTechStack() != null && metadata.getTechStack().getJavaVersion() != null) {
            javaVersion = metadata.getTechStack().getJavaVersion();
        }
        sb.append("        <java.version>").append(javaVersion).append("</java.version>\n");
        sb.append("    </properties>\n\n");
        sb.append("    <dependencies>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-web</artifactId>\n");
        sb.append("        </dependency>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-data-jpa</artifactId>\n");
        sb.append("        </dependency>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>com.h2database</groupId>\n");
        sb.append("            <artifactId>h2</artifactId>\n");
        sb.append("            <scope>runtime</scope>\n");
        sb.append("        </dependency>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.projectlombok</groupId>\n");
        sb.append("            <artifactId>lombok</artifactId>\n");
        sb.append("            <optional>true</optional>\n");
        sb.append("        </dependency>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-validation</artifactId>\n");
        sb.append("        </dependency>\n");
        sb.append("    </dependencies>\n\n");
        sb.append("    <build>\n");
        sb.append("        <plugins>\n");
        sb.append("            <plugin>\n");
        sb.append("                <groupId>org.springframework.boot</groupId>\n");
        sb.append("                <artifactId>spring-boot-maven-plugin</artifactId>\n");
        sb.append("            </plugin>\n");
        sb.append("        </plugins>\n");
        sb.append("    </build>\n");
        sb.append("</project>\n");
        return sb.toString();
    }
    
    private Map<String, String> generateCSharpApplication(List<UMLClass> classes, String packageName, UMLMetadata metadata) {
        log.info("Generating C#/.NET application with {} classes", classes.size());
        Map<String, String> generatedFiles = new HashMap<>();
        
        CSharpEntityGenerator entityGen = new CSharpEntityGenerator();
        CSharpServiceGenerator serviceGen = new CSharpServiceGenerator();
        CSharpControllerGenerator controllerGen = new CSharpControllerGenerator();
        CSharpRepositoryGenerator repoGen = new CSharpRepositoryGenerator();
        
        for (UMLClass umlClass : classes) {
            EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
            
            // Generate C# files
            generatedFiles.put(entityGen.getEntityDirectory() + "/" + umlClass.getName() + entityGen.getFileExtension(),
                entityGen.generateEntity(enhancedClass, packageName));
            generatedFiles.put(serviceGen.getServiceDirectory() + "/" + umlClass.getName() + "Service" + serviceGen.getFileExtension(),
                serviceGen.generateService(enhancedClass, packageName));
            generatedFiles.put(controllerGen.getControllerDirectory() + "/" + umlClass.getName() + "Controller" + controllerGen.getFileExtension(),
                controllerGen.generateController(enhancedClass, packageName));
            generatedFiles.put(repoGen.getRepositoryDirectory() + "/" + umlClass.getName() + "Repository" + repoGen.getFileExtension(),
                repoGen.generateRepository(enhancedClass, packageName));
        }
        
        log.info("C# application generated with {} files", generatedFiles.size());
        return generatedFiles;
    }
    
    private Map<String, String> generatePhpApplication(List<UMLClass> classes, String packageName, UMLMetadata metadata) {
        log.info("Generating PHP/Laravel application with {} classes", classes.size());
        Map<String, String> generatedFiles = new HashMap<>();
        
        PhpEntityGenerator entityGen = new PhpEntityGenerator();
        PhpServiceGenerator serviceGen = new PhpServiceGenerator();
        PhpControllerGenerator controllerGen = new PhpControllerGenerator();
        PhpRepositoryGenerator repoGen = new PhpRepositoryGenerator();
        
        for (UMLClass umlClass : classes) {
            EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
            
            // Generate PHP files
            generatedFiles.put(entityGen.getEntityDirectory() + "/" + umlClass.getName() + entityGen.getFileExtension(),
                entityGen.generateEntity(enhancedClass, packageName));
            generatedFiles.put("Services/" + umlClass.getName() + "Service.php",
                serviceGen.generateService(enhancedClass, packageName));
            generatedFiles.put("Controllers/" + umlClass.getName() + "Controller.php",
                controllerGen.generateController(enhancedClass, packageName));
            generatedFiles.put("Repositories/" + umlClass.getName() + "Repository.php",
                repoGen.generateRepository(enhancedClass, packageName));
        }
        
        log.info("PHP application generated with {} files", generatedFiles.size());
        return generatedFiles;
    }
    
    private Map<String, String> generateTypeScriptApplication(List<UMLClass> classes, String packageName, UMLMetadata metadata) {
        log.info("Generating TypeScript/NestJS application with {} classes", classes.size());
        Map<String, String> generatedFiles = new HashMap<>();
        
        TypeScriptEntityGenerator entityGen = new TypeScriptEntityGenerator();
        TypeScriptServiceGenerator serviceGen = new TypeScriptServiceGenerator();
        TypeScriptControllerGenerator controllerGen = new TypeScriptControllerGenerator();
        TypeScriptRepositoryGenerator repoGen = new TypeScriptRepositoryGenerator();
        
        for (UMLClass umlClass : classes) {
            EnhancedClass enhancedClass = convertToEnhancedClass(umlClass);
            
            // Generate TypeScript files
            generatedFiles.put(entityGen.getEntityDirectory() + "/" + umlClass.getName().toLowerCase() + ".entity" + entityGen.getFileExtension(),
                entityGen.generateEntity(enhancedClass, packageName));
            generatedFiles.put("services/" + umlClass.getName().toLowerCase() + ".service.ts",
                serviceGen.generateService(enhancedClass, packageName));
            generatedFiles.put("controllers/" + umlClass.getName().toLowerCase() + ".controller.ts",
                controllerGen.generateController(enhancedClass, packageName));
            generatedFiles.put("repositories/" + umlClass.getName().toLowerCase() + ".repository.ts",
                repoGen.generateRepository(enhancedClass, packageName));
        }
        
        log.info("TypeScript application generated with {} files", generatedFiles.size());
        return generatedFiles;
    }
    
    private EnhancedClass convertToEnhancedClass(UMLClass umlClass) {
        return EnhancedClass.builder()
            .originalClass(umlClass)
            .build();
    }
}
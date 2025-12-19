package com.basiccode.generator.service;

import com.basiccode.generator.adapter.UmlClassAdapter;
import com.basiccode.generator.model.*;
import com.basiccode.generator.model.metadata.UMLMetadata;
import com.basiccode.generator.parser.metadata.UMLMetadataParser;
import com.basiccode.generator.parser.UMLClassDiagramParser;
import com.basiccode.generator.generator.spring.SpringBootEntityGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service unifié de génération de code avec support des métadonnées
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnifiedCodeGenerationService {
    
    private final UMLMetadataParser metadataParser;
    private final UMLClassDiagramParser classParser;
    private final SpringBootEntityGenerator entityGenerator;
    private final UmlClassAdapter adapter;
    
    /**
     * Génération unifiée avec détection automatique des métadonnées
     */
    public Map<String, String> generate(String diagramContent) {
        log.info("🚀 Génération de code unifiée");
        
        if (metadataParser.hasMetadata(diagramContent)) {
            return generateWithMetadata(diagramContent);
        } else {
            return generateWithoutMetadata(diagramContent);
        }
    }
    
    /**
     * Génération avec métadonnées UML
     */
    public Map<String, String> generateWithMetadata(String diagramContent) {
        log.info("📊 Génération avec métadonnées UML");
        
        // Parser les métadonnées
        UMLMetadata metadata = metadataParser.parseMetadata(diagramContent);
        
        // Extraire le diagramme pur
        String pureDiagram = metadataParser.extractDiagramContent(diagramContent);
        
        // Parser les classes du diagramme pur
        List<UMLClass> classes = classParser.parseClasses(pureDiagram);
        List<UMLRelationship> relationships = classParser.parseRelationships(pureDiagram);
        List<UMLEnum> enums = classParser.parseEnums(pureDiagram);
        
        Map<String, String> files = new HashMap<>();
        
        String basePackage = metadata.getArchitecture().getBasePackage();
        
        // Générer les entités avec le parser complet
        for (UMLClass umlClass : classes) {
            EnhancedClass enhancedClass = convertToEnhancedClass(umlClass, relationships);
            String entityCode = entityGenerator.generateEntity(enhancedClass, basePackage);
            String entityPath = "src/main/java/" + basePackage.replace(".", "/") + "/entity/" + umlClass.getName() + ".java";
            files.put(entityPath, entityCode);
        }
        
        // Générer les énumérations
        for (UMLEnum umlEnum : enums) {
            String enumCode = generateEnumCode(umlEnum, basePackage);
            String enumPath = "src/main/java/" + basePackage.replace(".", "/") + "/entity/" + umlEnum.getName() + ".java";
            files.put(enumPath, enumCode);
        }
        
        // Générer les fichiers de configuration
        files.put("README.md", generateReadme(metadata));
        files.put("pom.xml", generatePom(metadata));
        files.put("src/main/resources/application.yml", generateApplicationYml(metadata));
        
        log.info("✅ Génération avec métadonnées terminée: {} fichiers ({} classes, {} enums)", 
            files.size(), classes.size(), enums.size());
        return files;
    }
    
    /**
     * Génération simple sans métadonnées
     */
    public Map<String, String> generateWithoutMetadata(String diagramContent) {
        log.info("🔄 Génération sans métadonnées (mode simple)");
        
        // Parser les classes avec valeurs par défaut
        List<UMLClass> classes = classParser.parseClasses(diagramContent);
        List<UMLRelationship> relationships = classParser.parseRelationships(diagramContent);
        List<UMLEnum> enums = classParser.parseEnums(diagramContent);
        
        Map<String, String> files = new HashMap<>();
        String defaultPackage = "com.example.generated";
        
        // Générer les entités
        for (UMLClass umlClass : classes) {
            EnhancedClass enhancedClass = convertToEnhancedClass(umlClass, relationships);
            String entityCode = entityGenerator.generateEntity(enhancedClass, defaultPackage);
            String entityPath = "src/main/java/" + defaultPackage.replace(".", "/") + "/entity/" + umlClass.getName() + ".java";
            files.put(entityPath, entityCode);
        }
        
        // Générer les énumérations
        for (UMLEnum umlEnum : enums) {
            String enumCode = generateEnumCode(umlEnum, defaultPackage);
            String enumPath = "src/main/java/" + defaultPackage.replace(".", "/") + "/entity/" + umlEnum.getName() + ".java";
            files.put(enumPath, enumCode);
        }
        
        // Générer la configuration basique
        files.put("README.md", generateSimpleReadme());
        files.put("pom.xml", generateSimplePom(defaultPackage));
        
        log.info("✅ Génération simple terminée: {} fichiers ({} classes, {} enums)", 
            files.size(), classes.size(), enums.size());
        return files;
    }
    
    private String generateReadme(UMLMetadata metadata) {
        return String.format(
            "# %s\n\n" +
            "Version: %s\n" +
            "Description: %s\n" +
            "Author: %s\n\n" +
            "## Tech Stack\n" +
            "- Language: %s\n" +
            "- Framework: %s %s\n" +
            "- Database: %s\n" +
            "- ORM: %s\n" +
            "- Build Tool: %s\n" +
            "- Java Version: %s\n\n" +
            "## Architecture\n" +
            "- Organization: %s\n" +
            "- Base Package: %s\n" +
            "- Module Detection: %s\n\n" +
            "Generated by Enhanced UML Generator with complete parsing support.\n",
            metadata.getProject().getName(),
            metadata.getProject().getVersion(),
            metadata.getProject().getDescription(),
            "Unknown", // Author placeholder
            metadata.getTechStack().getBackendLanguage(),
            metadata.getTechStack().getBackendFramework(),
            metadata.getTechStack().getBackendVersion(),
            metadata.getTechStack().getDatabaseType(),
            metadata.getTechStack().getOrmFramework(),
            "Maven", // Build tool placeholder
            metadata.getTechStack().getJavaVersion(),
            metadata.getArchitecture().getOrganizationStrategy(),
            metadata.getArchitecture().getBasePackage(),
            metadata.getArchitecture().getModuleDetection()
        );
    }
    
    private String generatePom(UMLMetadata metadata) {
        return String.format(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n\n" +
            "    <groupId>%s</groupId>\n" +
            "    <artifactId>%s</artifactId>\n" +
            "    <version>%s</version>\n" +
            "    <packaging>jar</packaging>\n\n" +
            "    <name>%s</name>\n" +
            "    <description>%s</description>\n\n" +
            "    <properties>\n" +
            "        <java.version>%s</java.version>\n" +
            "        <spring-boot.version>%s</spring-boot.version>\n" +
            "    </properties>\n\n" +
            "    <!-- Generated by UML Generator -->\n" +
            "</project>\n",
            metadata.getArchitecture().getBasePackage(),
            metadata.getProject().getName().toLowerCase().replace(" ", "-"),
            metadata.getProject().getVersion(),
            metadata.getProject().getName(),
            metadata.getProject().getDescription(),
            metadata.getTechStack().getJavaVersion(),
            metadata.getTechStack().getBackendVersion()
        );
    }
    
    private EnhancedClass convertToEnhancedClass(UMLClass umlClass, List<UMLRelationship> relationships) {
        // Convertir UMLClass vers EnhancedClass pour compatibilité
        return EnhancedClass.builder()
            .originalClass(umlClass)
            .build();
    }
    
    private String generateEnumCode(UMLEnum umlEnum, String packageName) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".entity;\n\n");
        code.append("public enum ").append(umlEnum.getName()).append(" {\n");
        
        for (int i = 0; i < umlEnum.getValues().size(); i++) {
            UMLEnumValue value = umlEnum.getValues().get(i);
            code.append("    ").append(value.getName());
            if (i < umlEnum.getValues().size() - 1) {
                code.append(",");
            }
            code.append("\n");
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    private String generateApplicationYml(UMLMetadata metadata) {
        return String.format(
            "spring:\n" +
            "  application:\n" +
            "    name: %s\n" +
            "  datasource:\n" +
            "    url: jdbc:%s://localhost:5432/%s\n" +
            "    username: ${DB_USERNAME:admin}\n" +
            "    password: ${DB_PASSWORD:password}\n" +
            "  jpa:\n" +
            "    hibernate:\n" +
            "      ddl-auto: create-drop\n" +
            "    show-sql: true\n",
            metadata.getProject().getName().toLowerCase().replace(" ", "-"),
            metadata.getTechStack().getDatabaseType(),
            metadata.getProject().getName().toLowerCase().replace(" ", "_")
        );
    }
    
    private String generateSimpleReadme() {
        return "# Generated Project\n\nGenerated from UML class diagram.\n";
    }
    
    private String generateSimplePom(String packageName) {
        return String.format(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <groupId>%s</groupId>\n" +
            "    <artifactId>generated-app</artifactId>\n" +
            "    <version>1.0.0</version>\n" +
            "    <packaging>jar</packaging>\n\n" +
            "    <parent>\n" +
            "        <groupId>org.springframework.boot</groupId>\n" +
            "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
            "        <version>3.2.0</version>\n" +
            "    </parent>\n\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-web</artifactId>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.projectlombok</groupId>\n" +
            "            <artifactId>lombok</artifactId>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "</project>\n",
            packageName
        );
    }
}

package com.basiccode.generator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Orchestrateur de génération comprehensive utilisant tous les diagrammes UML
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComprehensiveGenerationOrchestrator {
    
    /**
     * Génère du code complet utilisant tous les diagrammes UML
     */
    public Map<String, String> generateComprehensiveCode(
            String classDiagram,
            String sequenceDiagram, 
            String stateDiagram,
            String language,
            String packageName) {
        
        log.info("Starting comprehensive generation with ALL diagrams - Language: {}", language);
        
        Map<String, String> allFiles = new HashMap<>();
        
        // Génération basique pour éviter les dépendances manquantes
        generateBasicFiles(classDiagram, sequenceDiagram, stateDiagram, language, packageName, allFiles);
        
        log.info("Generated {} files using comprehensive approach", allFiles.size());
        return allFiles;
    }
    
    /**
     * Génère les fichiers de base
     */
    private void generateBasicFiles(
            String classDiagram,
            String sequenceDiagram, 
            String stateDiagram,
            String language,
            String packageName,
            Map<String, String> allFiles) {
        
        // Générer les entités
        if (classDiagram != null && !classDiagram.trim().isEmpty()) {
            generateEntitiesFromClassDiagram(classDiagram, packageName, allFiles);
        }
        
        // Générer les services
        if (sequenceDiagram != null && !sequenceDiagram.trim().isEmpty()) {
            generateServicesFromSequence(sequenceDiagram, packageName, allFiles);
        }
        
        // Générer les contrôleurs d'état
        if (stateDiagram != null && !stateDiagram.trim().isEmpty()) {
            generateStateControllers(stateDiagram, packageName, allFiles);
        }
        
        // Générer la configuration
        generateConfiguration(packageName, language, allFiles);
    }
    
    /**
     * Génère les entités à partir du diagramme de classes
     */
    private void generateEntitiesFromClassDiagram(String classDiagram, String packageName, Map<String, String> allFiles) {
        // Extraction simple des classes
        String[] lines = classDiagram.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("class ")) {
                String className = extractClassName(line);
                if (className != null) {
                    String entityCode = generateEntityCode(className, packageName);
                    String entityPath = "src/main/java/" + packageName.replace(".", "/") + "/entity/" + className + ".java";
                    allFiles.put(entityPath, entityCode);
                    log.debug("Generated entity: {}", className);
                }
            }
        }
    }
    
    /**
     * Génère les services à partir du diagramme de séquence
     */
    private void generateServicesFromSequence(String sequenceDiagram, String packageName, Map<String, String> allFiles) {
        // Extraction simple des services
        String[] lines = sequenceDiagram.split("\n");
        Set<String> services = new HashSet<>();
        
        for (String line : lines) {
            if (line.contains("->") && line.contains("Service")) {
                String serviceName = extractServiceName(line);
                if (serviceName != null) {
                    services.add(serviceName);
                }
            }
        }
        
        for (String serviceName : services) {
            String serviceCode = generateServiceCode(serviceName, packageName);
            String servicePath = "src/main/java/" + packageName.replace(".", "/") + "/service/" + serviceName + ".java";
            allFiles.put(servicePath, serviceCode);
            log.debug("Generated service: {}", serviceName);
        }
    }
    
    /**
     * Génère les contrôleurs d'état
     */
    private void generateStateControllers(String stateDiagram, String packageName, Map<String, String> allFiles) {
        // Extraction simple des états
        String[] lines = stateDiagram.split("\n");
        Set<String> states = new HashSet<>();
        
        for (String line : lines) {
            if (line.contains("-->") || line.contains(":")) {
                String stateName = extractStateName(line);
                if (stateName != null) {
                    states.add(stateName);
                }
            }
        }
        
        if (!states.isEmpty()) {
            String stateControllerCode = generateStateControllerCode(states, packageName);
            String controllerPath = "src/main/java/" + packageName.replace(".", "/") + "/controller/StateController.java";
            allFiles.put(controllerPath, stateControllerCode);
            log.debug("Generated state controller with {} states", states.size());
        }
    }
    
    /**
     * Génère la configuration
     */
    private void generateConfiguration(String packageName, String language, Map<String, String> allFiles) {
        // Générer pom.xml pour Java
        if ("java".equalsIgnoreCase(language)) {
            allFiles.put("pom.xml", generatePomXml(packageName));
            allFiles.put("src/main/resources/application.yml", generateApplicationYml());
        }
        
        // Générer README
        allFiles.put("README.md", generateReadme(packageName, language));
    }
    
    // Méthodes utilitaires d'extraction
    
    private String extractClassName(String line) {
        String[] parts = line.trim().split("\\s+");
        for (int i = 0; i < parts.length - 1; i++) {
            if ("class".equals(parts[i])) {
                return parts[i + 1].replaceAll("[{:].*", "");
            }
        }
        return null;
    }
    
    private String extractServiceName(String line) {
        if (line.contains("Service")) {
            String[] parts = line.split("[->>]+");
            for (String part : parts) {
                if (part.contains("Service")) {
                    return part.trim().split("\\s+")[0];
                }
            }
        }
        return null;
    }
    
    private String extractStateName(String line) {
        String[] parts = line.split("[->>:]+");
        if (parts.length > 1) {
            String state = parts[1].trim();
            return state.replaceAll("[()\\[\\]]", "");
        }
        return null;
    }
    
    // Méthodes de génération de code
    
    private String generateEntityCode(String className, String packageName) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".entity;\n\n");
        code.append("import javax.persistence.*;\n");
        code.append("import lombok.Data;\n\n");
        code.append("@Entity\n");
        code.append("@Data\n");
        code.append("public class ").append(className).append(" {\n");
        code.append("    @Id\n");
        code.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
        code.append("    private Long id;\n");
        code.append("    \n");
        code.append("    private String name;\n");
        code.append("}\n");
        return code.toString();
    }
    
    private String generateServiceCode(String serviceName, String packageName) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".service;\n\n");
        code.append("import org.springframework.stereotype.Service;\n");
        code.append("import lombok.RequiredArgsConstructor;\n\n");
        code.append("@Service\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("public class ").append(serviceName).append(" {\n");
        code.append("    \n");
        code.append("    public void processRequest() {\n");
        code.append("        // Generated service method\n");
        code.append("    }\n");
        code.append("}\n");
        return code.toString();
    }
    
    private String generateStateControllerCode(Set<String> states, String packageName) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".controller;\n\n");
        code.append("import org.springframework.web.bind.annotation.*;\n");
        code.append("import org.springframework.stereotype.Controller;\n\n");
        code.append("@Controller\n");
        code.append("@RequestMapping(\"/api/state\")\n");
        code.append("public class StateController {\n");
        
        for (String state : states) {
            if (state != null && !state.trim().isEmpty()) {
                code.append("    \n");
                code.append("    @PostMapping(\"/").append(state.toLowerCase()).append("\")\n");
                code.append("    public String ").append(state.toLowerCase()).append("() {\n");
                code.append("        return \"State: ").append(state).append("\";\n");
                code.append("    }\n");
            }
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    private String generatePomXml(String packageName) {
        StringBuilder pom = new StringBuilder();
        pom.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        pom.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\">\n");
        pom.append("    <modelVersion>4.0.0</modelVersion>\n");
        pom.append("    <groupId>").append(packageName).append("</groupId>\n");
        pom.append("    <artifactId>generated-app</artifactId>\n");
        pom.append("    <version>1.0.0</version>\n");
        pom.append("    <packaging>jar</packaging>\n");
        pom.append("    \n");
        pom.append("    <parent>\n");
        pom.append("        <groupId>org.springframework.boot</groupId>\n");
        pom.append("        <artifactId>spring-boot-starter-parent</artifactId>\n");
        pom.append("        <version>2.7.0</version>\n");
        pom.append("    </parent>\n");
        pom.append("    \n");
        pom.append("    <dependencies>\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-web</artifactId>\n");
        pom.append("        </dependency>\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.springframework.boot</groupId>\n");
        pom.append("            <artifactId>spring-boot-starter-data-jpa</artifactId>\n");
        pom.append("        </dependency>\n");
        pom.append("        <dependency>\n");
        pom.append("            <groupId>org.projectlombok</groupId>\n");
        pom.append("            <artifactId>lombok</artifactId>\n");
        pom.append("        </dependency>\n");
        pom.append("    </dependencies>\n");
        pom.append("</project>\n");
        return pom.toString();
    }
    
    private String generateApplicationYml() {
        StringBuilder yml = new StringBuilder();
        yml.append("spring:\n");
        yml.append("  application:\n");
        yml.append("    name: generated-comprehensive-app\n");
        yml.append("  datasource:\n");
        yml.append("    url: jdbc:h2:mem:testdb\n");
        yml.append("    driver-class-name: org.h2.Driver\n");
        yml.append("  jpa:\n");
        yml.append("    hibernate:\n");
        yml.append("      ddl-auto: create-drop\n");
        return yml.toString();
    }
    
    private String generateReadme(String packageName, String language) {
        StringBuilder readme = new StringBuilder();
        readme.append("# Generated Comprehensive Application\n\n");
        readme.append("Package: ").append(packageName).append("\n");
        readme.append("Language: ").append(language).append("\n");
        readme.append("Generated with comprehensive UML diagrams support\n\n");
        readme.append("## Features\n");
        readme.append("- Entities from class diagrams\n");
        readme.append("- Services from sequence diagrams\n");
        readme.append("- State controllers from state diagrams\n");
        return readme.toString();
    }
}
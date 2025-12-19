// ============================================
// MODÈLES DE DONNÉES
// ============================================

// 1. Enums
package com.umlgenerator.model;

public enum DiagramFormat {
    MERMAID,
    PLANTUML,
    XMI,
    VISUAL_PARADIGM,
    UNKNOWN
}

public enum DiagramType {
    CLASS,
    SEQUENCE,
    STATE,
    USECASE,
    COMPONENT,
    ACTIVITY,
    DEPLOYMENT,
    UNKNOWN
}

public enum RelationshipType {
    INHERITANCE,
    COMPOSITION,
    AGGREGATION,
    ASSOCIATION,
    DEPENDENCY,
    REALIZATION
}

public enum MessageType {
    SYNCHRONOUS,
    ASYNCHRONOUS,
    RETURN,
    SELF
}

public enum FragmentType {
    ALT,
    OPT,
    LOOP,
    PAR,
    BREAK,
    CRITICAL
}

// ============================================
// 2. Métadonnées
// ============================================
package com.umlgenerator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectMetadata {
    private String name;
    private String version;
    private String description;
    private String projectType;
    private String architectureStyle;
}

@Data
@Builder
public class TechStackMetadata {
    private String backendLanguage;
    private String backendFramework;
    private String backendVersion;
    private String ormFramework;
    private String databaseType;
    private String databaseVersion;
    private String cacheLayer;
    private String messageQueue;
}

@Data
@Builder
public class ArchitectureMetadata {
    private String organizationStrategy;
    private String basePackage;
    private String moduleDetection;
    private List<String> moduleStrategy;
    private String springStructure;
    private List<String> perFeatureStructure;
    private String sharedModuleName;
}

// ============================================
// 3. UMLDiagram - Interface de base
// ============================================
package com.umlgenerator.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class UMLDiagram {
    private DiagramFormat format;
    private DiagramType type;
    private ProjectMetadata projectMetadata;
    private TechStackMetadata techStackMetadata;
    private ArchitectureMetadata architectureMetadata;
}

// ============================================
// 4. ClassDiagram
// ============================================
package com.umlgenerator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ClassDiagram extends UMLDiagram {
    private List<UMLClass> classes;
    private List<UMLRelationship> relationships;
    private Map<String, List<UMLClass>> modules;
    private String persistenceLayer;
    private String idGenerationStrategy;
    private boolean auditFields;
    private boolean softDelete;
}

@Data
@Builder
public class UMLClass {
    private String name;
    private String stereotype;
    private List<UMLAttribute> attributes;
    private List<UMLMethod> methods;
    private String module; // Module détecté
}

@Data
@Builder
public class UMLAttribute {
    private String name;
    private String type;
    private String visibility;
    private boolean nullable;
    private String defaultValue;
}

@Data
@Builder
public class UMLMethod {
    private String name;
    private String returnType;
    private String visibility;
    private List<UMLParameter> parameters;
}

@Data
@AllArgsConstructor
public class UMLParameter {
    private String name;
    private String type;
}

// ============================================
// 5. UMLRelationship
// ============================================
package com.umlgenerator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UMLRelationship {
    private RelationshipType type;
    private String source;
    private String target;
    private Multiplicity sourceMultiplicity;
    private Multiplicity targetMultiplicity;
    private String label;
    
    // Héritage
    private String inheritanceStrategy;
    
    // Composition/Agrégation
    private String cascade;
    private boolean orphanRemoval;
    private String fetchType;
    
    // Association
    private boolean bidirectional;
    private String ownerSide;
}

@Data
@AllArgsConstructor
public class Multiplicity {
    private int min;
    private int max;
    
    public boolean isOne() {
        return min == 1 && max == 1;
    }
    
    public boolean isMany() {
        return max > 1 || max == Integer.MAX_VALUE;
    }
    
    public boolean isOptional() {
        return min == 0;
    }
    
    @Override
    public String toString() {
        if (min == max) return String.valueOf(min);
        if (max == Integer.MAX_VALUE) return min + "..*";
        return min + ".." + max;
    }
}

// ============================================
// 6. SequenceDiagram
// ============================================
package com.umlgenerator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SequenceDiagram extends UMLDiagram {
    private List<String> participants;
    private List<SequenceMessage> messages;
    private List<CombinedFragment> fragments;
    private boolean transactionSupport;
    private String errorHandling;
    private String asyncFramework;
}

@Data
@Builder
public class SequenceMessage {
    private String from;
    private String to;
    private String message;
    private MessageType type;
    private String returnType;
    private List<String> parameters;
}

@Data
@Builder
public class CombinedFragment {
    private FragmentType type;
    private String condition;
    private List<SequenceMessage> messages;
}

// ============================================
// 7. StateDiagram
// ============================================
package com.umlgenerator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StateDiagram extends UMLDiagram {
    private List<State> states;
    private List<StateTransition> transitions;
    private String stateMachineFramework;
    private boolean eventSourcing;
}

@Data
@Builder
public class State {
    private String name;
    private boolean isInitial;
    private boolean isFinal;
    private String entryAction;
    private String exitAction;
    private String doActivity;
}

@Data
@Builder
public class StateTransition {
    private String from;
    private String to;
    private String event;
    private String guard;
    private String action;
}

// ============================================
// 8. Configuration Spring Boot
// ============================================
package com.umlgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UMLParserConfig {
    
    @Bean
    public UMLMetadataParser umlMetadataParser(
            ProjectMetadataParser projectMetadataParser,
            TechStackMetadataParser techStackMetadataParser,
            ArchitectureMetadataParser architectureMetadataParser,
            ClassDiagramParser classDiagramParser,
            SequenceDiagramParser sequenceDiagramParser,
            StateDiagramParser stateDiagramParser,
            RelationshipParser relationshipParser) {
        
        return new UMLMetadataParserImpl(
            projectMetadataParser,
            techStackMetadataParser,
            architectureMetadataParser,
            classDiagramParser,
            sequenceDiagramParser,
            stateDiagramParser,
            relationshipParser
        );
    }
}

// ============================================
// 9. Service Principal
// ============================================
package com.umlgenerator.service;

import com.umlgenerator.model.*;
import com.umlgenerator.parser.UMLMetadataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class UMLParserService {
    
    private final UMLMetadataParser parser;
    
    /**
     * Parse un fichier UML
     */
    public UMLDiagram parseFile(Path filePath) throws IOException {
        log.info("Parsing UML file: {}", filePath);
        String content = Files.readString(filePath);
        return parser.parse(content);
    }
    
    /**
     * Parse du contenu UML
     */
    public UMLDiagram parseContent(String content) throws IOException {
        log.info("Parsing UML content");
        return parser.parse(content);
    }
    
    /**
     * Valide un diagramme
     */
    public ValidationResult validate(UMLDiagram diagram) {
        ValidationResult result = new ValidationResult();
        
        if (diagram instanceof ClassDiagram classDiagram) {
            validateClassDiagram(classDiagram, result);
        }
        
        return result;
    }
    
    private void validateClassDiagram(ClassDiagram diagram, ValidationResult result) {
        // Vérifier que toutes les relations pointent vers des classes existantes
        Set<String> classNames = diagram.getClasses().stream()
            .map(UMLClass::getName)
            .collect(Collectors.toSet());
        
        for (UMLRelationship rel : diagram.getRelationships()) {
            if (!classNames.contains(rel.getSource())) {
                result.addError("Class not found: " + rel.getSource());
            }
            if (!classNames.contains(rel.getTarget())) {
                result.addError("Class not found: " + rel.getTarget());
            }
        }
        
        // Vérifier les modules
        if (diagram.getModules().isEmpty()) {
            result.addWarning("No modules detected");
        }
    }
}

@Data
public class ValidationResult {
    private List<String> errors = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    
    public void addError(String error) {
        errors.add(error);
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public boolean isValid() {
        return errors.isEmpty();
    }
}

// ============================================
// 10. Controller REST
// ============================================
package com.umlgenerator.controller;

import com.umlgenerator.model.*;
import com.umlgenerator.service.UMLParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/api/uml")
@RequiredArgsConstructor
public class UMLParserController {
    
    private final UMLParserService parserService;
    
    /**
     * Parse un fichier UML uploadé
     */
    @PostMapping("/parse/file")
    public ResponseEntity<UMLDiagram> parseFile(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarder temporairement
            Path tempFile = Files.createTempFile("uml-", ".mmd");
            file.transferTo(tempFile);
            
            // Parser
            UMLDiagram diagram = parserService.parseFile(tempFile);
            
            // Nettoyer
            Files.deleteIfExists(tempFile);
            
            return ResponseEntity.ok(diagram);
            
        } catch (IOException e) {
            log.error("Error parsing file", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Parse du contenu UML brut
     */
    @PostMapping("/parse/content")
    public ResponseEntity<UMLDiagram> parseContent(@RequestBody String content) {
        try {
            UMLDiagram diagram = parserService.parseContent(content);
            return ResponseEntity.ok(diagram);
        } catch (IOException e) {
            log.error("Error parsing content", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Valide un diagramme
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validate(@RequestBody String content) {
        try {
            UMLDiagram diagram = parserService.parseContent(content);
            ValidationResult result = parserService.validate(diagram);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("Error validating", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

// ============================================
// 11. Exemple d'Utilisation - Main Class
// ============================================
package com.umlgenerator;

import com.umlgenerator.model.*;
import com.umlgenerator.parser.UMLMetadataParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class UMLGeneratorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UMLGeneratorApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner demo(UMLMetadataParser parser) {
        return args -> {
            log.info("=== UML Parser Demo ===");
            
            String umlContent = """
                %% @project
                %% name: blog-cms
                %% version: 1.0.0
                %% @end-project
                
                %% @tech-stack
                %% backend-language: java
                %% backend-framework: spring-boot
                %% database-type: postgresql
                %% @end-tech-stack
                
                %% @project-architecture
                %% organization-strategy: package-by-feature
                %% base-package: com.example.blog
                %% module-detection: auto
                %% @end-project-architecture
                
                %% @class-relationships
                %% jpa-inheritance-strategy: JOINED
                %% composition-cascade: ALL
                %% @end-class-relationships
                
                classDiagram
                    class User {
                        <<Entity>>
                        +UUID id
                        +String username
                        +String email
                    }
                    
                    class Author {
                        <<Entity>>
                        +String bio
                    }
                    
                    User <|-- Author
                    
                    class Article {
                        <<Entity>>
                        +UUID id
                        +String titre
                    }
                    
                    class BlocContenu {
                        <<Entity>>
                        +UUID id
                        +String contenu
                    }
                    
                    Article *-- "0..*" BlocContenu : contient
                """;
            
            try {
                UMLDiagram diagram = parser.parse(umlContent);
                
                if (diagram instanceof ClassDiagram classDiagram) {
                    log.info("=== Parsed Class Diagram ===");
                    log.info("Format: {}", classDiagram.getFormat());
                    log.info("Project: {}", classDiagram.getProjectMetadata().getName());
                    log.info("Classes found: {}", classDiagram.getClasses().size());
                    
                    for (UMLClass umlClass : classDiagram.getClasses()) {
                        log.info("  - {} (stereotype: {})", 
                            umlClass.getName(), 
                            umlClass.getStereotype());
                    }
                    
                    log.info("Relationships found: {}", classDiagram.getRelationships().size());
                    for (UMLRelationship rel : classDiagram.getRelationships()) {
                        log.info("  - {} {} {}", 
                            rel.getSource(), 
                            rel.getType(), 
                            rel.getTarget());
                    }
                    
                    log.info("Modules detected: {}", classDiagram.getModules().keySet());
                }
                
            } catch (Exception e) {
                log.error("Error parsing UML", e);
            }
        };
    }
}
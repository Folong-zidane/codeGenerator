// ============================================
// ARCHITECTURE DU PARSER UML
// ============================================

// 1. UMLMetadataParser.java - Interface principale
package com.umlgenerator.parser;

import com.umlgenerator.model.*;
import java.io.IOException;

public interface UMLMetadataParser {
    
    /**
     * Parse un diagramme UML complet avec toutes ses métadonnées
     */
    UMLDiagram parse(String diagramContent) throws IOException;
    
    /**
     * Détecte automatiquement le format du diagramme
     */
    DiagramFormat detectFormat(String content);
    
    /**
     * Détecte automatiquement le type de diagramme
     */
    DiagramType detectDiagramType(String content);
}

// ============================================
// 2. UMLMetadataParserImpl.java - Implémentation
// ============================================
package com.umlgenerator.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UMLMetadataParserImpl implements UMLMetadataParser {
    
    private final ProjectMetadataParser projectMetadataParser;
    private final TechStackMetadataParser techStackMetadataParser;
    private final ArchitectureMetadataParser architectureMetadataParser;
    private final ClassDiagramParser classDiagramParser;
    private final SequenceDiagramParser sequenceDiagramParser;
    private final StateDiagramParser stateDiagramParser;
    private final RelationshipParser relationshipParser;
    
    @Override
    public UMLDiagram parse(String diagramContent) throws IOException {
        log.info("Starting UML diagram parsing...");
        
        // 1. Détection automatique
        DiagramFormat format = detectFormat(diagramContent);
        DiagramType type = detectDiagramType(diagramContent);
        
        log.info("Detected format: {}, type: {}", format, type);
        
        // 2. Parsing des métadonnées globales
        ProjectMetadata projectMetadata = projectMetadataParser.parse(diagramContent);
        TechStackMetadata techStackMetadata = techStackMetadataParser.parse(diagramContent);
        ArchitectureMetadata architectureMetadata = architectureMetadataParser.parse(diagramContent);
        
        // 3. Parsing du diagramme spécifique
        UMLDiagram diagram = switch (type) {
            case CLASS -> classDiagramParser.parse(diagramContent);
            case SEQUENCE -> sequenceDiagramParser.parse(diagramContent);
            case STATE -> stateDiagramParser.parse(diagramContent);
            case USECASE -> throw new UnsupportedOperationException("UseCase not yet implemented");
            case COMPONENT -> throw new UnsupportedOperationException("Component not yet implemented");
            case ACTIVITY -> throw new UnsupportedOperationException("Activity not yet implemented");
            default -> throw new IllegalArgumentException("Unknown diagram type: " + type);
        };
        
        // 4. Enrichissement avec métadonnées
        diagram.setFormat(format);
        diagram.setType(type);
        diagram.setProjectMetadata(projectMetadata);
        diagram.setTechStackMetadata(techStackMetadata);
        diagram.setArchitectureMetadata(architectureMetadata);
        
        log.info("UML diagram parsing completed successfully");
        return diagram;
    }
    
    @Override
    public DiagramFormat detectFormat(String content) {
        if (content.contains("classDiagram") || content.contains("sequenceDiagram")) {
            return DiagramFormat.MERMAID;
        } else if (content.contains("@startuml")) {
            return DiagramFormat.PLANTUML;
        } else if (content.contains("<?xml") && content.contains("XMI")) {
            return DiagramFormat.XMI;
        }
        
        // Recherche dans métadonnées
        Pattern pattern = Pattern.compile("%% format: (\\w+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return DiagramFormat.valueOf(matcher.group(1).toUpperCase());
        }
        
        return DiagramFormat.UNKNOWN;
    }
    
    @Override
    public DiagramType detectDiagramType(String content) {
        // Détection basée sur mot-clé
        if (content.contains("classDiagram")) return DiagramType.CLASS;
        if (content.contains("sequenceDiagram")) return DiagramType.SEQUENCE;
        if (content.contains("stateDiagram")) return DiagramType.STATE;
        if (content.contains("usecaseDiagram")) return DiagramType.USECASE;
        
        // Détection basée sur métadonnées
        Pattern pattern = Pattern.compile("%% diagram-type: (\\w+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return DiagramType.valueOf(matcher.group(1).toUpperCase());
        }
        
        return DiagramType.UNKNOWN;
    }
}

// ============================================
// 3. MetadataBlockParser.java - Parseur de blocs
// ============================================
package com.umlgenerator.parser;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MetadataBlockParser {
    
    private static final Pattern METADATA_BLOCK_PATTERN = 
        Pattern.compile("%% @(\\w+)(.*?)%% @end-\\1", Pattern.DOTALL);
    
    private static final Pattern KEY_VALUE_PATTERN = 
        Pattern.compile("%%\\s*([\\w-]+):\\s*(.+)");
    
    /**
     * Extrait tous les blocs de métadonnées
     */
    public Map<String, Map<String, String>> parseAllBlocks(String content) {
        Map<String, Map<String, String>> allBlocks = new HashMap<>();
        
        Matcher matcher = METADATA_BLOCK_PATTERN.matcher(content);
        while (matcher.find()) {
            String blockName = matcher.group(1);
            String blockContent = matcher.group(2);
            
            Map<String, String> metadata = parseBlockContent(blockContent);
            allBlocks.put(blockName, metadata);
        }
        
        return allBlocks;
    }
    
    /**
     * Parse le contenu d'un bloc de métadonnées
     */
    private Map<String, String> parseBlockContent(String blockContent) {
        Map<String, String> metadata = new HashMap<>();
        
        String[] lines = blockContent.split("\\n");
        for (String line : lines) {
            Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
            if (matcher.find()) {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim();
                metadata.put(key, value);
            }
        }
        
        return metadata;
    }
    
    /**
     * Extrait un bloc spécifique
     */
    public Map<String, String> parseBlock(String content, String blockName) {
        Pattern pattern = Pattern.compile(
            "%% @" + blockName + "(.*?)%% @end-" + blockName, 
            Pattern.DOTALL
        );
        
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return parseBlockContent(matcher.group(1));
        }
        
        return new HashMap<>();
    }
}

// ============================================
// 4. ProjectMetadataParser.java
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.ProjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProjectMetadataParser {
    
    private final MetadataBlockParser blockParser;
    
    public ProjectMetadata parse(String content) {
        Map<String, String> metadata = blockParser.parseBlock(content, "project");
        
        return ProjectMetadata.builder()
            .name(metadata.getOrDefault("name", "unknown-project"))
            .version(metadata.getOrDefault("version", "1.0.0"))
            .description(metadata.get("description"))
            .projectType(metadata.getOrDefault("project-type", "web-application"))
            .architectureStyle(metadata.getOrDefault("architecture-style", "monolith"))
            .build();
    }
}

// ============================================
// 5. TechStackMetadataParser.java
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.TechStackMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TechStackMetadataParser {
    
    private final MetadataBlockParser blockParser;
    
    public TechStackMetadata parse(String content) {
        Map<String, String> metadata = blockParser.parseBlock(content, "tech-stack");
        
        return TechStackMetadata.builder()
            .backendLanguage(metadata.getOrDefault("backend-language", "java"))
            .backendFramework(metadata.getOrDefault("backend-framework", "spring-boot"))
            .backendVersion(metadata.getOrDefault("backend-version", "3.2.0"))
            .ormFramework(metadata.getOrDefault("orm-framework", "jpa"))
            .databaseType(metadata.getOrDefault("database-type", "postgresql"))
            .databaseVersion(metadata.get("database-version"))
            .cacheLayer(metadata.getOrDefault("cache-layer", "none"))
            .messageQueue(metadata.getOrDefault("message-queue", "none"))
            .build();
    }
}

// ============================================
// 6. ArchitectureMetadataParser.java
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.ArchitectureMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ArchitectureMetadataParser {
    
    private final MetadataBlockParser blockParser;
    
    public ArchitectureMetadata parse(String content) {
        Map<String, String> metadata = blockParser.parseBlock(content, "project-architecture");
        
        return ArchitectureMetadata.builder()
            .organizationStrategy(metadata.getOrDefault("organization-strategy", "package-by-feature"))
            .basePackage(metadata.getOrDefault("base-package", "com.example.app"))
            .moduleDetection(metadata.getOrDefault("module-detection", "auto"))
            .moduleStrategy(parseList(metadata.get("module-strategy")))
            .springStructure(metadata.getOrDefault("spring-structure", "feature"))
            .perFeatureStructure(parseList(metadata.get("per-feature-structure")))
            .sharedModuleName(metadata.getOrDefault("shared-module-name", "common"))
            .build();
    }
    
    private List<String> parseList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
            .map(String::trim)
            .toList();
    }
}

// ============================================
// 7. ClassDiagramParser.java - Parser de diagramme de classes
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClassDiagramParser {
    
    private final MetadataBlockParser blockParser;
    private final RelationshipParser relationshipParser;
    
    // Pattern pour extraire les classes
    private static final Pattern CLASS_PATTERN = 
        Pattern.compile("class\\s+(\\w+)\\s*\\{([^}]*)\\}", Pattern.DOTALL);
    
    // Pattern pour les stéréotypes
    private static final Pattern STEREOTYPE_PATTERN = 
        Pattern.compile("<<([^>]+)>>");
    
    // Pattern pour les attributs/méthodes
    private static final Pattern MEMBER_PATTERN = 
        Pattern.compile("([+\\-#~])\\s*(\\w+)\\s+(\\w+)(?:\\(([^)]*)\\))?");
    
    public ClassDiagram parse(String content) {
        log.info("Parsing class diagram...");
        
        // 1. Métadonnées du diagramme
        Map<String, String> diagramMetadata = blockParser.parseBlock(content, "class-diagram");
        Map<String, String> relationshipMetadata = blockParser.parseBlock(content, "class-relationships");
        
        // 2. Extraction des classes
        List<UMLClass> classes = extractClasses(content);
        
        // 3. Extraction des relations
        List<UMLRelationship> relationships = relationshipParser.parseRelationships(content, relationshipMetadata);
        
        // 4. Détection automatique des modules
        Map<String, List<UMLClass>> modules = detectModules(classes, relationships);
        
        return ClassDiagram.builder()
            .classes(classes)
            .relationships(relationships)
            .modules(modules)
            .persistenceLayer(diagramMetadata.getOrDefault("persistence-layer", "jpa"))
            .idGenerationStrategy(diagramMetadata.getOrDefault("id-generation-strategy", "uuid"))
            .auditFields(parseBoolean(diagramMetadata.get("audit-fields")))
            .softDelete(parseBoolean(diagramMetadata.get("soft-delete")))
            .build();
    }
    
    /**
     * Extrait toutes les classes du diagramme
     */
    private List<UMLClass> extractClasses(String content) {
        List<UMLClass> classes = new ArrayList<>();
        
        Matcher matcher = CLASS_PATTERN.matcher(content);
        while (matcher.find()) {
            String className = matcher.group(1);
            String classBody = matcher.group(2);
            
            UMLClass umlClass = UMLClass.builder()
                .name(className)
                .stereotype(extractStereotype(classBody))
                .attributes(extractAttributes(classBody))
                .methods(extractMethods(classBody))
                .build();
            
            classes.add(umlClass);
            log.debug("Extracted class: {}", className);
        }
        
        return classes;
    }
    
    /**
     * Extrait le stéréotype d'une classe
     */
    private String extractStereotype(String classBody) {
        Matcher matcher = STEREOTYPE_PATTERN.matcher(classBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    /**
     * Extrait les attributs
     */
    private List<UMLAttribute> extractAttributes(String classBody) {
        List<UMLAttribute> attributes = new ArrayList<>();
        String[] lines = classBody.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("<<")) continue;
            
            // Attribut: +String name ou +name: String
            if (!line.contains("(")) {
                UMLAttribute attr = parseAttribute(line);
                if (attr != null) {
                    attributes.add(attr);
                }
            }
        }
        
        return attributes;
    }
    
    /**
     * Extrait les méthodes
     */
    private List<UMLMethod> extractMethods(String classBody) {
        List<UMLMethod> methods = new ArrayList<>();
        String[] lines = classBody.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.contains("(")) {
                UMLMethod method = parseMethod(line);
                if (method != null) {
                    methods.add(method);
                }
            }
        }
        
        return methods;
    }
    
    /**
     * Parse un attribut
     */
    private UMLAttribute parseAttribute(String line) {
        // Format: +UUID id ou +id: UUID
        Pattern pattern = Pattern.compile("([+\\-#~])\\s*(?:(\\w+)\\s+(\\w+)|(\\w+):\\s*(\\w+))");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            String visibility = parseVisibility(matcher.group(1));
            String type = matcher.group(2) != null ? matcher.group(2) : matcher.group(5);
            String name = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
            
            return UMLAttribute.builder()
                .name(name)
                .type(type)
                .visibility(visibility)
                .build();
        }
        
        return null;
    }
    
    /**
     * Parse une méthode
     */
    private UMLMethod parseMethod(String line) {
        // Format: +createUser() ou +createUser(String username)
        Pattern pattern = Pattern.compile("([+\\-#~])\\s*(\\w+)\\s+(\\w+)\\(([^)]*)\\)");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            String visibility = parseVisibility(matcher.group(1));
            String returnType = matcher.group(2);
            String name = matcher.group(3);
            String params = matcher.group(4);
            
            return UMLMethod.builder()
                .name(name)
                .returnType(returnType)
                .visibility(visibility)
                .parameters(parseParameters(params))
                .build();
        }
        
        return null;
    }
    
    /**
     * Parse les paramètres d'une méthode
     */
    private List<UMLParameter> parseParameters(String params) {
        if (params == null || params.isBlank()) {
            return List.of();
        }
        
        List<UMLParameter> parameters = new ArrayList<>();
        String[] parts = params.split(",");
        
        for (String part : parts) {
            part = part.trim();
            String[] tokens = part.split("\\s+");
            if (tokens.length == 2) {
                parameters.add(new UMLParameter(tokens[1], tokens[0]));
            }
        }
        
        return parameters;
    }
    
    /**
     * Convertit le symbole de visibilité
     */
    private String parseVisibility(String symbol) {
        return switch (symbol) {
            case "+" -> "public";
            case "-" -> "private";
            case "#" -> "protected";
            case "~" -> "package";
            default -> "public";
        };
    }
    
    /**
     * Détection automatique des modules
     */
    private Map<String, List<UMLClass>> detectModules(List<UMLClass> classes, List<UMLRelationship> relationships) {
        Map<String, List<UMLClass>> modules = new HashMap<>();
        
        // Stratégie 1: Hiérarchie de classes
        for (UMLClass umlClass : classes) {
            String moduleName = detectModuleByHierarchy(umlClass, relationships);
            modules.computeIfAbsent(moduleName, k -> new ArrayList<>()).add(umlClass);
        }
        
        return modules;
    }
    
    /**
     * Détecte le module par hiérarchie
     */
    private String detectModuleByHierarchy(UMLClass umlClass, List<UMLRelationship> relationships) {
        // Si la classe a des enfants (héritage), elle définit un module
        for (UMLRelationship rel : relationships) {
            if (rel.getType() == RelationshipType.INHERITANCE && 
                rel.getSource().equals(umlClass.getName())) {
                return umlClass.getName().toLowerCase();
            }
        }
        
        // Sinon, chercher le parent
        for (UMLRelationship rel : relationships) {
            if (rel.getType() == RelationshipType.INHERITANCE && 
                rel.getTarget().equals(umlClass.getName())) {
                return rel.getSource().toLowerCase();
            }
        }
        
        // Par défaut, classe isolée = son propre module
        return umlClass.getName().toLowerCase();
    }
    
    private boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value);
    }
}

// ============================================
// 8. RelationshipParser.java - Parser de relations
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RelationshipParser {
    
    // Patterns pour différentes relations (Mermaid)
    private static final Pattern INHERITANCE_PATTERN = 
        Pattern.compile("(\\w+)\\s+<\\|--\\s+(\\w+)");
    
    private static final Pattern COMPOSITION_PATTERN = 
        Pattern.compile("(\\w+)\\s+\\*--\\s+\"([^\"]+)\"\\s+(\\w+)(?:\\s*:\\s*(.+))?");
    
    private static final Pattern AGGREGATION_PATTERN = 
        Pattern.compile("(\\w+)\\s+o--\\s+\"([^\"]+)\"\\s+(\\w+)(?:\\s*:\\s*(.+))?");
    
    private static final Pattern ASSOCIATION_PATTERN = 
        Pattern.compile("(\\w+)\\s+\"([^\"]+)\"\\s+--\\s+\"([^\"]+)\"\\s+(\\w+)(?:\\s*:\\s*(.+))?");
    
    private static final Pattern DEPENDENCY_PATTERN = 
        Pattern.compile("(\\w+)\\s+\\.\\.>\\s+(\\w+)(?:\\s*:\\s*(.+))?");
    
    public List<UMLRelationship> parseRelationships(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        
        relationships.addAll(parseInheritance(content, metadata));
        relationships.addAll(parseComposition(content, metadata));
        relationships.addAll(parseAggregation(content, metadata));
        relationships.addAll(parseAssociation(content, metadata));
        relationships.addAll(parseDependency(content, metadata));
        
        log.info("Parsed {} relationships", relationships.size());
        return relationships;
    }
    
    /**
     * Parse les relations d'héritage
     */
    private List<UMLRelationship> parseInheritance(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Matcher matcher = INHERITANCE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String parent = matcher.group(1);
            String child = matcher.group(2);
            
            UMLRelationship rel = UMLRelationship.builder()
                .type(RelationshipType.INHERITANCE)
                .source(parent)
                .target(child)
                .inheritanceStrategy(metadata.getOrDefault("jpa-inheritance-strategy", "JOINED"))
                .build();
            
            relationships.add(rel);
            log.debug("Found inheritance: {} <|-- {}", parent, child);
        }
        
        return relationships;
    }
    
    /**
     * Parse les compositions
     */
    private List<UMLRelationship> parseComposition(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Matcher matcher = COMPOSITION_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String source = matcher.group(1);
            String targetMultiplicity = matcher.group(2);
            String target = matcher.group(3);
            String label = matcher.group(4);
            
            UMLRelationship rel = UMLRelationship.builder()
                .type(RelationshipType.COMPOSITION)
                .source(source)
                .target(target)
                .targetMultiplicity(parseMultiplicity(targetMultiplicity))
                .label(label)
                .cascade(metadata.getOrDefault("composition-cascade", "ALL"))
                .orphanRemoval(true)
                .fetchType(metadata.getOrDefault("composition-fetch-type", "LAZY"))
                .build();
            
            relationships.add(rel);
            log.debug("Found composition: {} *-- {} {}", source, targetMultiplicity, target);
        }
        
        return relationships;
    }
    
    /**
     * Parse les agrégations
     */
    private List<UMLRelationship> parseAggregation(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Matcher matcher = AGGREGATION_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String source = matcher.group(1);
            String targetMultiplicity = matcher.group(2);
            String target = matcher.group(3);
            String label = matcher.group(4);
            
            UMLRelationship rel = UMLRelationship.builder()
                .type(RelationshipType.AGGREGATION)
                .source(source)
                .target(target)
                .targetMultiplicity(parseMultiplicity(targetMultiplicity))
                .label(label)
                .cascade(metadata.getOrDefault("aggregation-cascade", "PERSIST,MERGE"))
                .orphanRemoval(false)
                .fetchType(metadata.getOrDefault("aggregation-fetch-type", "LAZY"))
                .build();
            
            relationships.add(rel);
            log.debug("Found aggregation: {} o-- {} {}", source, targetMultiplicity, target);
        }
        
        return relationships;
    }
    
    /**
     * Parse les associations
     */
    private List<UMLRelationship> parseAssociation(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Matcher matcher = ASSOCIATION_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String source = matcher.group(1);
            String sourceMultiplicity = matcher.group(2);
            String targetMultiplicity = matcher.group(3);
            String target = matcher.group(4);
            String label = matcher.group(5);
            
            UMLRelationship rel = UMLRelationship.builder()
                .type(RelationshipType.ASSOCIATION)
                .source(source)
                .target(target)
                .sourceMultiplicity(parseMultiplicity(sourceMultiplicity))
                .targetMultiplicity(parseMultiplicity(targetMultiplicity))
                .label(label)
                .bidirectional(true)
                .build();
            
            relationships.add(rel);
            log.debug("Found association: {} {} -- {} {}", source, sourceMultiplicity, targetMultiplicity, target);
        }
        
        return relationships;
    }
    
    /**
     * Parse les dépendances
     */
    private List<UMLRelationship> parseDependency(String content, Map<String, String> metadata) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Matcher matcher = DEPENDENCY_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String source = matcher.group(1);
            String target = matcher.group(2);
            String label = matcher.group(3);
            
            UMLRelationship rel = UMLRelationship.builder()
                .type(RelationshipType.DEPENDENCY)
                .source(source)
                .target(target)
                .label(label)
                .build();
            
            relationships.add(rel);
            log.debug("Found dependency: {} ..> {}", source, target);
        }
        
        return relationships;
    }
    
    /**
     * Parse la multiplicité UML
     */
    private Multiplicity parseMultiplicity(String mult) {
        mult = mult.trim();
        
        return switch (mult) {
            case "1" -> new Multiplicity(1, 1);
            case "0..1" -> new Multiplicity(0, 1);
            case "0..*", "*" -> new Multiplicity(0, Integer.MAX_VALUE);
            case "1..*" -> new Multiplicity(1, Integer.MAX_VALUE);
            default -> {
                // Format: 5..10
                if (mult.contains("..")) {
                    String[] parts = mult.split("\\.\\.");
                    yield new Multiplicity(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
                yield new Multiplicity(0, Integer.MAX_VALUE);
            }
        };
    }
}

// ============================================
// 9. SequenceDiagramParser.java
// ============================================
package com.umlgenerator.parser;

import com.umlgenerator.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class SequenceDiagramParser {
    
    private final MetadataBlockParser blockParser;
    
    // Pattern pour les participants
    private static final Pattern PARTICIPANT_PATTERN = 
        Pattern.compile("participant\\s+(\\w+)");
    
    // Pattern pour les messages
    private static final Pattern MESSAGE_PATTERN = 
        Pattern.compile("(\\w+)\\s*(->>|-->>|-\\)\\))\\s*(\\w+)\\s*:\\s*(.+)");
    
    // Pattern pour les fragments alt
    private static final Pattern ALT_PATTERN = 
        Pattern.compile("alt\\s+(.+)");
    
    // Pattern pour les fragments loop
    private static final Pattern LOOP_PATTERN = 
        Pattern.compile("loop\\s+(.+)");
    
    public SequenceDiagram parse(String content) {
        log.info("Parsing sequence diagram...");
        
        Map<String, String> metadata = blockParser.parseBlock(content, "sequence-diagram");
        
        List<String> participants = extractParticipants(content);
        List<SequenceMessage> messages = extractMessages(content);
        List<CombinedFragment> fragments = extractFragments(content);
        
        return SequenceDiagram.builder()
            .participants(participants)
            .messages(messages)
            .fragments(fragments)
            .transactionSupport(parseBoolean(metadata.get("transaction-support")))
            .errorHandling(metadata.getOrDefault("error-handling", "try-catch"))
            .asyncFramework(metadata.get("async-framework"))
            .build();
    }
    
    private List<String> extractParticipants(String content) {
        List<String> participants = new ArrayList<>();
        Matcher matcher = PARTICIPANT_PATTERN.matcher(content);
        
        while (matcher.find()) {
            participants.add(matcher.group(1));
        }
        
        return participants;
    }
    
    private List<SequenceMessage> extractMessages(String content) {
        List<SequenceMessage> messages = new ArrayList<>();
        Matcher matcher = MESSAGE_PATTERN.matcher(content);
        
        while (matcher.find()) {
            String from = matcher.group(1);
            String arrow = matcher.group(2);
            String to = matcher.group(3);
            String message = matcher.group(4);
            
            MessageType type = arrow.equals("->>") ? MessageType.SYNCHRONOUS : MessageType.RETURN;
            
            messages.add(SequenceMessage.builder()
                .from(from)
                .to(to)
                .message(message)
                .type(type)
                .build());
        }
        
        return messages;
    }
    
    private List<CombinedFragment> extractFragments(String content) {
        List<CombinedFragment> fragments = new ArrayList<>();
        
        // ALT fragments
        Matcher altMatcher = ALT_PATTERN.matcher(content);
        while (altMatcher.find()) {
            fragments.add(CombinedFragment.builder()
                .type(FragmentType.ALT)
                .condition(altMatcher.group(1))
                .build());
        }
        
        // LOOP fragments
        Matcher loopMatcher = LOOP_PATTERN.matcher(content);
        while (loopMatcher.find()) {
            fragments.add(CombinedFragment.builder()
                .type(FragmentType.LOOP)
                .condition(loopMatcher.group(1))
                .build());
        }
        
        return fragments;
    }
    
    private boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value);
    }
}
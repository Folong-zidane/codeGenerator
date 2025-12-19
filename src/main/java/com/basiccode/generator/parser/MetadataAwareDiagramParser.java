package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parser avancé qui utilise les métadonnées pour générer du code ultra-précis
 */
public class MetadataAwareDiagramParser {
    
    private final Map<String, String> metadata = new HashMap<>();
    private final Map<String, Map<String, String>> entityMetadata = new HashMap<>();
    
    public ParsedDiagram parseWithMetadata(String diagramContent) {
        // Extract metadata
        extractMetadata(diagramContent);
        
        // Parse diagram based on type
        String diagramType = metadata.getOrDefault("diagram-type", "class");
        
        switch (diagramType) {
            case "class":
                return parseClassDiagram(diagramContent);
            case "sequence":
                return parseSequenceDiagram(diagramContent);
            case "state":
                return parseStateDiagram(diagramContent);
            default:
                return parseClassDiagram(diagramContent);
        }
    }
    
    private void extractMetadata(String content) {
        // Extract global metadata
        Pattern metadataPattern = Pattern.compile("%%\\s+([^:]+):\\s*(.+)");
        Matcher matcher = metadataPattern.matcher(content);
        
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            metadata.put(key, value);
        }
        
        // Extract entity-specific metadata
        Pattern entityMetadataPattern = Pattern.compile("\\{([^}]+)\\}");
        Pattern classPattern = Pattern.compile("class\\s+(\\w+)\\s*\\{");
        
        Matcher classMatcher = classPattern.matcher(content);
        while (classMatcher.find()) {
            String className = classMatcher.group(1);
            
            // Find metadata for this class
            int classStart = classMatcher.start();
            int classEnd = findClassEnd(content, classStart);
            String classContent = content.substring(classStart, classEnd);
            
            Matcher entityMatcher = entityMetadataPattern.matcher(classContent);
            if (entityMatcher.find()) {
                Map<String, String> classMetadata = parseEntityMetadata(entityMatcher.group(1));
                entityMetadata.put(className, classMetadata);
            }
        }
    }
    
    private Map<String, String> parseEntityMetadata(String metadataString) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = metadataString.split(",");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return result;
    }
    
    private ParsedDiagram parseClassDiagram(String content) {
        List<EnhancedClass> classes = new ArrayList<>();
        List<UMLRelationship> relationships = new ArrayList<>();
        
        // Parse classes with metadata
        Pattern classPattern = Pattern.compile("class\\s+(\\w+)\\s*\\{([^}]+)\\}");
        Matcher matcher = classPattern.matcher(content);
        
        while (matcher.find()) {
            String className = matcher.group(1);
            String classBody = matcher.group(2);
            
            UMLClass umlClass = parseClassWithMetadata(className, classBody);
            EnhancedClass enhancedClass = EnhancedClass.builder()
                .originalClass(umlClass)
                .metadata(entityMetadata.get(className))
                .build();
            
            classes.add(enhancedClass);
        }
        
        // Parse relationships
        relationships = parseRelationships(content);
        
        return new ParsedDiagram(classes, relationships, metadata);
    }
    
    private UMLClass parseClassWithMetadata(String className, String classBody) {
        UMLClass umlClass = UMLClass.builder().name(className).build();
        
        // Extract stereotypes
        Pattern stereotypePattern = Pattern.compile("<<([^>]+)>>");
        Matcher stereotypeMatcher = stereotypePattern.matcher(classBody);
        while (stereotypeMatcher.find()) {
            umlClass.addStereotype(stereotypeMatcher.group(1));
        }
        
        // Parse attributes with metadata-aware types
        String[] lines = classBody.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("+") || line.startsWith("-") || line.startsWith("#")) {
                UMLAttribute attr = parseAttributeWithMetadata(line);
                if (attr != null) {
                    umlClass.addAttribute(attr);
                }
            }
        }
        
        return umlClass;
    }
    
    private UMLAttribute parseAttributeWithMetadata(String line) {
        // Remove visibility modifier
        String cleanLine = line.substring(1).trim();
        
        // Parse type and name
        String[] parts = cleanLine.split("\\s+");
        if (parts.length >= 2) {
            String type = parts[0];
            String name = parts[1];
            
            // Apply metadata transformations
            type = applyMetadataTypeMapping(type);
            name = applyMetadataNamingConvention(name);
            
            UMLAttribute attr = UMLAttribute.builder().name(name).type(type).build();
            
            // Apply metadata constraints
            applyMetadataConstraints(attr);
            
            return attr;
        }
        
        return null;
    }
    
    private String applyMetadataTypeMapping(String type) {
        String persistenceLayer = metadata.getOrDefault("persistence-layer", "jpa");
        String idType = metadata.getOrDefault("id-type", "UUID");
        
        // Apply type mappings based on metadata
        if ("UUID".equals(type) && "sqlalchemy".equals(persistenceLayer)) {
            return "String";  // SQLAlchemy uses String for UUID
        }
        
        if ("LocalDateTime".equals(type)) {
            String auditStrategy = metadata.getOrDefault("audit-strategy", "created-updated");
            if ("event-sourcing".equals(auditStrategy)) {
                return "Instant";  // Event sourcing prefers Instant
            }
        }
        
        return type;
    }
    
    private String applyMetadataNamingConvention(String name) {
        String columnNaming = metadata.getOrDefault("column-naming-convention", "snake_case");
        
        switch (columnNaming) {
            case "snake_case":
                return toSnakeCase(name);
            case "camelCase":
                return toCamelCase(name);
            case "PascalCase":
                return toPascalCase(name);
            default:
                return name;
        }
    }
    
    private void applyMetadataConstraints(UMLAttribute attr) {
        // Apply validation based on metadata
        boolean nullSafety = Boolean.parseBoolean(metadata.getOrDefault("null-safety", "true"));
        if (nullSafety) {
            attr.addConstraint("@NotNull");
        }
        
        // Apply audit fields
        boolean auditFields = Boolean.parseBoolean(metadata.getOrDefault("audit-fields", "true"));
        if (auditFields && isAuditField(attr.getName())) {
            attr.addConstraint("@CreationTimestamp");
        }
        
        // Apply cache annotations
        String cacheStrategy = metadata.getOrDefault("cache-strategy", "none");
        if (!"none".equals(cacheStrategy)) {
            attr.addConstraint("@Cacheable");
        }
    }
    
    private List<UMLRelationship> parseRelationships(String content) {
        List<UMLRelationship> relationships = new ArrayList<>();
        
        // Parse different relationship types based on metadata
        boolean associationSemantic = Boolean.parseBoolean(metadata.getOrDefault("association-semantic", "true"));
        
        if (associationSemantic) {
            // Parse with full UML semantics
            relationships.addAll(parseComposition(content));
            relationships.addAll(parseAggregation(content));
            relationships.addAll(parseAssociation(content));
            relationships.addAll(parseInheritance(content));
        }
        
        return relationships;
    }
    
    private List<UMLRelationship> parseComposition(String content) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*\\*--\\s*(\\w+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String from = matcher.group(1);
            String to = matcher.group(2);
            
            UMLRelationship rel = new UMLRelationship(from, to, "COMPOSITION");
            
            // Apply metadata for composition
            String ownership = metadata.getOrDefault("composition-ownership", "strong");
            rel.addProperty("ownership", ownership);
            
            boolean orphanRemoval = Boolean.parseBoolean(metadata.getOrDefault("orphan-removal", "true"));
            rel.addProperty("orphanRemoval", String.valueOf(orphanRemoval));
            
            relationships.add(rel);
        }
        
        return relationships;
    }
    
    private List<UMLRelationship> parseAggregation(String content) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*o--\\s*(\\w+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String from = matcher.group(1);
            String to = matcher.group(2);
            
            UMLRelationship rel = new UMLRelationship(from, to, "AGGREGATION");
            
            // Apply metadata for aggregation
            String ownership = metadata.getOrDefault("aggregation-ownership", "weak");
            rel.addProperty("ownership", ownership);
            
            relationships.add(rel);
        }
        
        return relationships;
    }
    
    private List<UMLRelationship> parseAssociation(String content) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*--\\s*(\\w+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String from = matcher.group(1);
            String to = matcher.group(2);
            
            UMLRelationship rel = new UMLRelationship(from, to, "ASSOCIATION");
            
            // Apply bidirectional mapping from metadata
            boolean bidirectional = Boolean.parseBoolean(metadata.getOrDefault("bidirectional-mapping", "true"));
            rel.addProperty("bidirectional", String.valueOf(bidirectional));
            
            relationships.add(rel);
        }
        
        return relationships;
    }
    
    private List<UMLRelationship> parseInheritance(String content) {
        List<UMLRelationship> relationships = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*<\\|--\\s*(\\w+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String parent = matcher.group(1);
            String child = matcher.group(2);
            
            UMLRelationship rel = new UMLRelationship(child, parent, "INHERITANCE");
            
            // Apply inheritance strategy from metadata
            String strategy = metadata.getOrDefault("inheritance-strategy", "single-table");
            rel.addProperty("strategy", strategy);
            
            String discriminatorColumn = metadata.getOrDefault("discriminator-column-name", "dtype");
            rel.addProperty("discriminatorColumn", discriminatorColumn);
            
            relationships.add(rel);
        }
        
        return relationships;
    }
    
    private ParsedDiagram parseSequenceDiagram(String content) {
        // Parse sequence diagram with metadata for transaction and async support
        return new ParsedDiagram(new ArrayList<>(), new ArrayList<>(), metadata);
    }
    
    private ParsedDiagram parseStateDiagram(String content) {
        // Parse state diagram with metadata for state machine generation
        return new ParsedDiagram(new ArrayList<>(), new ArrayList<>(), metadata);
    }
    
    // Utility methods
    private int findClassEnd(String content, int start) {
        int braceCount = 0;
        for (int i = start; i < content.length(); i++) {
            if (content.charAt(i) == '{') braceCount++;
            if (content.charAt(i) == '}') {
                braceCount--;
                if (braceCount == 0) return i + 1;
            }
        }
        return content.length();
    }
    
    private boolean isAuditField(String fieldName) {
        String createdAtField = metadata.getOrDefault("created-at-field", "createdAt");
        String updatedAtField = metadata.getOrDefault("updated-at-field", "updatedAt");
        return fieldName.equals(createdAtField) || fieldName.equals(updatedAtField);
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private String toCamelCase(String snake_case) {
        String[] parts = snake_case.split("_");
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return result.toString();
    }
    
    private String toPascalCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public Map<String, Map<String, String>> getEntityMetadata() {
        return entityMetadata;
    }
}


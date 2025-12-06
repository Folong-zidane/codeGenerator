package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import org.springframework.stereotype.Component;
import lombok.Data;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * TypeScriptModelParser: Parses UML class diagrams and extracts TypeScript models
 * with advanced type support, constraints, and relationships.
 * 
 * Supports:
 * - 20+ TypeScript types (UUID, Email, URL, Phone, Slug, JSON, Decimal, etc.)
 * - 8+ constraint types (required, unique, max_length, min_length, pattern, etc.)
 * - 3 relationship types (OneToOne, OneToMany, ManyToMany)
 * - Validation decorators (class-validator, class-transformer)
 * 
 * Phase 3 Implementation: WEEK 1 - CRITICAL FOUNDATION
 */
@Component
public class TypeScriptModelParser {
    
    // ==================== REGEX PATTERNS ====================
    
    private static final String CLASS_PATTERN = "^class\\s+([A-Z][a-zA-Z0-9]*)\\s*\\{([^}]*?)\\}";
    private static final String ATTRIBUTE_PATTERN = "([+#-]?)\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*:\\s*([A-Za-z<>\\[\\],\\s|?]+?)(?:\\s*\\[([^\\]]+)\\])?$";
    private static final String METHOD_PATTERN = "([+#-]?)\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)(?:\\s*:\\s*([A-Za-z<>\\[\\],\\s|?]+))?";
    private static final String RELATIONSHIP_PATTERN = "([A-Z][a-zA-Z0-9]*)\\s+([\"']1[\"']|[\"']\\*[\"'])\\s+--+>?\\s+([\"']1[\"']|[\"']\\*[\"'])\\s+([A-Z][a-zA-Z0-9]*)";
    private static final String CONSTRAINT_PATTERN = "@constraint\\(([^)]+)\\)|\\[([^\\]]+)\\]";
    
    // ==================== TYPE MAPPING ====================
    
    /**
     * Maps UML types to TypeScript types (20+ types supported)
     */
    private static final Map<String, String> TYPE_MAPPING = new LinkedHashMap<>();
    
    static {
        // Primitive types
        TYPE_MAPPING.put("string", "string");
        TYPE_MAPPING.put("String", "string");
        TYPE_MAPPING.put("int", "number");
        TYPE_MAPPING.put("integer", "number");
        TYPE_MAPPING.put("Integer", "number");
        TYPE_MAPPING.put("number", "number");
        TYPE_MAPPING.put("Number", "number");
        TYPE_MAPPING.put("float", "number");
        TYPE_MAPPING.put("Float", "number");
        TYPE_MAPPING.put("double", "number");
        TYPE_MAPPING.put("Double", "number");
        TYPE_MAPPING.put("boolean", "boolean");
        TYPE_MAPPING.put("Boolean", "boolean");
        TYPE_MAPPING.put("bool", "boolean");
        
        // Date/Time types
        TYPE_MAPPING.put("Date", "Date");
        TYPE_MAPPING.put("date", "Date");
        TYPE_MAPPING.put("DateTime", "Date");
        TYPE_MAPPING.put("datetime", "Date");
        TYPE_MAPPING.put("LocalDateTime", "Date");
        TYPE_MAPPING.put("Timestamp", "Date");
        
        // Advanced types
        TYPE_MAPPING.put("UUID", "string"); // UUID as branded string type
        TYPE_MAPPING.put("uuid", "string");
        TYPE_MAPPING.put("Email", "string");
        TYPE_MAPPING.put("email", "string");
        TYPE_MAPPING.put("URL", "string");
        TYPE_MAPPING.put("url", "string");
        TYPE_MAPPING.put("URI", "string");
        TYPE_MAPPING.put("Phone", "string");
        TYPE_MAPPING.put("phone", "string");
        TYPE_MAPPING.put("Slug", "string");
        TYPE_MAPPING.put("slug", "string");
        TYPE_MAPPING.put("JSON", "Record<string, any>");
        TYPE_MAPPING.put("json", "Record<string, any>");
        TYPE_MAPPING.put("Decimal", "number");
        TYPE_MAPPING.put("decimal", "number");
        TYPE_MAPPING.put("BigInt", "bigint");
        TYPE_MAPPING.put("bigint", "bigint");
        TYPE_MAPPING.put("Buffer", "Buffer");
        TYPE_MAPPING.put("buffer", "Buffer");
        
        // Collection types
        TYPE_MAPPING.put("List", "any[]");
        TYPE_MAPPING.put("list", "any[]");
        TYPE_MAPPING.put("Array", "any[]");
        TYPE_MAPPING.put("array", "any[]");
        TYPE_MAPPING.put("Set", "Set<any>");
        TYPE_MAPPING.put("set", "Set<any>");
        TYPE_MAPPING.put("Map", "Map<string, any>");
        TYPE_MAPPING.put("map", "Map<string, any>");
    }
    
    // ==================== CONSTRAINT TYPES ====================
    
    /**
     * Represents a field constraint (validation rule)
     */
    @Data
    public static class FieldConstraint {
        private String name;
        private String value;
        private String decorator; // class-validator decorator name
        
        public FieldConstraint(String name, String value) {
            this.name = name;
            this.value = value;
            this.decorator = mapConstraintToDecorator(name);
        }
        
        private static String mapConstraintToDecorator(String constraint) {
            return switch(constraint.toLowerCase()) {
                case "required", "notnull" -> "@IsNotEmpty()";
                case "unique" -> "@Unique()";
                case "max_length", "maxlength" -> "@MaxLength()";
                case "min_length", "minlength" -> "@MinLength()";
                case "pattern", "regex" -> "@Matches()";
                case "email" -> "@IsEmail()";
                case "url" -> "@IsUrl()";
                case "min" -> "@Min()";
                case "max" -> "@Max()";
                case "default" -> "@Default()";
                case "enum" -> "@IsEnum()";
                case "validate" -> "@Validate()";
                default -> "";
            };
        }
    }
    
    // ==================== RELATIONSHIP TYPES ====================
    
    /**
     * Represents a relationship between two entities
     */
    @Data
    public static class TypeScriptRelationship {
        private String fromEntity;
        private String toEntity;
        private RelationType type; // OneToOne, OneToMany, ManyToMany
        private String fromMultiplicity; // "1" or "*"
        private String toMultiplicity;
        private boolean isForeign; // Is this a foreign key relationship?
        
        public TypeScriptRelationship(String from, String to, String fromMult, String toMult) {
            this.fromEntity = from;
            this.toEntity = to;
            this.fromMultiplicity = fromMult;
            this.toMultiplicity = toMult;
            this.type = determineRelationType(fromMult, toMult);
            this.isForeign = toMult.equals("*");
        }
        
        private RelationType determineRelationType(String from, String to) {
            if (from.equals("1") && to.equals("1")) return RelationType.ONE_TO_ONE;
            if (from.equals("1") && to.equals("*")) return RelationType.ONE_TO_MANY;
            if (from.equals("*") && to.equals("*")) return RelationType.MANY_TO_MANY;
            return RelationType.ONE_TO_MANY; // Default
        }
    }
    
    public enum RelationType {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_MANY
    }
    
    // ==================== PARSED MODEL DTO ====================
    
    /**
     * Data Transfer Object for a parsed TypeScript model/entity
     */
    @Data
    public static class TypeScriptModel {
        private String name;
        private String packageName;
        private List<TypeScriptField> fields = new ArrayList<>();
        private List<TypeScriptMethod> methods = new ArrayList<>();
        private List<TypeScriptRelationship> relationships = new ArrayList<>();
        private boolean stateful = false;
        private String stateEnumName;
        private List<String> imports = new ArrayList<>();
        private List<String> exports = new ArrayList<>();
        private String description;
        
        public void addField(TypeScriptField field) {
            this.fields.add(field);
        }
        
        public void addMethod(TypeScriptMethod method) {
            this.methods.add(method);
        }
        
        public void addRelationship(TypeScriptRelationship rel) {
            this.relationships.add(rel);
        }
        
        public void addImport(String importStatement) {
            this.imports.add(importStatement);
        }
    }
    
    @Data
    public static class TypeScriptField {
        private String name;
        private String type;
        private boolean required = true;
        private boolean unique = false;
        private String defaultValue;
        private List<FieldConstraint> constraints = new ArrayList<>();
        private String validator; // class-validator decorator
        private boolean isPrimaryKey = false;
        private boolean isTimestamp = false; // createdAt, updatedAt
        private String description;
        
        public TypeScriptField(String name, String type) {
            this.name = name;
            this.type = type;
        }
        
        public void addConstraint(FieldConstraint constraint) {
            this.constraints.add(constraint);
        }
    }
    
    @Data
    public static class TypeScriptMethod {
        private String name;
        private String returnType;
        private List<MethodParameter> parameters = new ArrayList<>();
        private String businessLogic;
        private String description;
        
        public TypeScriptMethod(String name, String returnType) {
            this.name = name;
            this.returnType = returnType;
        }
        
        public void addParameter(MethodParameter param) {
            this.parameters.add(param);
        }
    }
    
    @Data
    public static class MethodParameter {
        private String name;
        private String type;
        
        public MethodParameter(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }
    
    // ==================== PARSING METHODS ====================
    
    /**
     * Parse UML class diagram text and extract TypeScript models
     */
    public List<TypeScriptModel> parseClassDiagram(String mermaidDiagram) {
        List<TypeScriptModel> models = new ArrayList<>();
        
        // Split by class definitions
        Pattern classPattern = Pattern.compile(CLASS_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher classMatcher = classPattern.matcher(mermaidDiagram);
        
        while (classMatcher.find()) {
            String className = classMatcher.group(1);
            String classBody = classMatcher.group(2);
            
            TypeScriptModel model = parseClass(className, classBody);
            models.add(model);
        }
        
        // Parse relationships
        parseRelationships(mermaidDiagram, models);
        
        return models;
    }
    
    /**
     * Parse a single class definition
     */
    private TypeScriptModel parseClass(String className, String classBody) {
        TypeScriptModel model = new TypeScriptModel();
        model.setName(className);
        model.setPackageName("entities");
        
        String[] lines = classBody.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) continue;
            
            // Try to parse as method first
            if (line.contains("(")) {
                TypeScriptMethod method = parseMethod(line);
                if (method != null) {
                    model.addMethod(method);
                }
            }
            // Otherwise parse as attribute
            else {
                TypeScriptField field = parseAttribute(line);
                if (field != null) {
                    model.addField(field);
                }
            }
        }
        
        // Detect if stateful
        boolean hasStatus = model.getFields().stream()
            .anyMatch(f -> f.getName().toLowerCase().contains("status"));
        model.setStateful(hasStatus);
        if (hasStatus) {
            model.setStateEnumName(className + "Status");
        }
        
        return model;
    }
    
    /**
     * Parse attribute line: [visibility] name : type [constraints]
     */
    private TypeScriptField parseAttribute(String line) {
        Pattern attrPattern = Pattern.compile(ATTRIBUTE_PATTERN);
        Matcher matcher = attrPattern.matcher(line);
        
        if (!matcher.find()) return null;
        
        String visibility = matcher.group(1);
        String fieldName = matcher.group(2);
        String fieldType = matcher.group(3).trim();
        String constraints = matcher.group(4);
        
        // Map type
        String tsType = mapType(fieldType);
        TypeScriptField field = new TypeScriptField(fieldName, tsType);
        
        // Handle primary key
        if (fieldName.equals("id")) {
            field.setPrimaryKey(true);
            field.setType("UUID");
        }
        
        // Handle timestamps
        if (fieldName.equals("createdAt") || fieldName.equals("updatedAt")) {
            field.setTimestamp(true);
            field.setRequired(true);
        }
        
        // Parse constraints
        if (constraints != null && !constraints.isEmpty()) {
            parseFieldConstraints(field, constraints);
        }
        
        return field;
    }
    
    /**
     * Parse method line: [visibility] methodName(params) : returnType
     */
    private TypeScriptMethod parseMethod(String line) {
        Pattern methodPattern = Pattern.compile(METHOD_PATTERN);
        Matcher matcher = methodPattern.matcher(line);
        
        if (!matcher.find()) return null;
        
        String visibility = matcher.group(1);
        String methodName = matcher.group(2);
        String params = matcher.group(3);
        String returnType = matcher.group(4);
        
        if (returnType == null || returnType.isEmpty()) {
            returnType = "void";
        }
        
        TypeScriptMethod method = new TypeScriptMethod(methodName, mapType(returnType));
        
        // Parse parameters
        if (params != null && !params.isEmpty()) {
            String[] paramParts = params.split(",");
            for (String param : paramParts) {
                MethodParameter mp = parseMethodParameter(param.trim());
                if (mp != null) {
                    method.addParameter(mp);
                }
            }
        }
        
        return method;
    }
    
    /**
     * Parse method parameter: name : type
     */
    private MethodParameter parseMethodParameter(String param) {
        String[] parts = param.split(":");
        if (parts.length != 2) return null;
        
        String name = parts[0].trim();
        String type = mapType(parts[1].trim());
        
        return new MethodParameter(name, type);
    }
    
    /**
     * Parse field constraints: [constraint1, constraint2, ...]
     */
    private void parseFieldConstraints(TypeScriptField field, String constraintStr) {
        String[] constraints = constraintStr.split(",");
        
        for (String constraint : constraints) {
            constraint = constraint.trim();
            
            // Handle key=value constraints
            if (constraint.contains("=")) {
                String[] parts = constraint.split("=");
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "";
                
                FieldConstraint fc = new FieldConstraint(key, value);
                field.addConstraint(fc);
                
                // Special handling
                if (key.equalsIgnoreCase("required") || key.equalsIgnoreCase("notnull")) {
                    field.setRequired(true);
                } else if (key.equalsIgnoreCase("unique")) {
                    field.setUnique(true);
                } else if (key.equalsIgnoreCase("default")) {
                    field.setDefaultValue(value);
                }
            } else {
                // Boolean constraints
                FieldConstraint fc = new FieldConstraint(constraint, "true");
                field.addConstraint(fc);
                
                if (constraint.equalsIgnoreCase("required")) {
                    field.setRequired(true);
                } else if (constraint.equalsIgnoreCase("unique")) {
                    field.setUnique(true);
                }
            }
        }
    }
    
    /**
     * Parse relationships from diagram
     */
    private void parseRelationships(String mermaidDiagram, List<TypeScriptModel> models) {
        Pattern relPattern = Pattern.compile(RELATIONSHIP_PATTERN, Pattern.MULTILINE);
        Matcher matcher = relPattern.matcher(mermaidDiagram);
        
        while (matcher.find()) {
            String fromEntity = matcher.group(1);
            String fromMult = matcher.group(2).replaceAll("[\"']", "");
            String toMult = matcher.group(3).replaceAll("[\"']", "");
            String toEntity = matcher.group(4);
            
            TypeScriptRelationship rel = new TypeScriptRelationship(
                fromEntity, toEntity, fromMult, toMult
            );
            
            // Add to source model
            for (TypeScriptModel model : models) {
                if (model.getName().equals(fromEntity)) {
                    model.addRelationship(rel);
                }
            }
        }
    }
    
    // ==================== TYPE MAPPING ====================
    
    /**
     * Map UML/Generic type to TypeScript type
     */
    public String mapType(String umlType) {
        if (umlType == null || umlType.isEmpty()) {
            return "any";
        }
        
        // Remove whitespace
        umlType = umlType.trim();
        
        // Handle generic types like List<String>, Optional<T>, etc.
        if (umlType.contains("<")) {
            return mapGenericType(umlType);
        }
        
        // Handle union types (Type1|Type2)
        if (umlType.contains("|")) {
            return umlType; // Keep as-is for union types
        }
        
        // Handle optional types (Type?)
        if (umlType.endsWith("?")) {
            String baseType = mapType(umlType.substring(0, umlType.length() - 1));
            return baseType + " | null";
        }
        
        // Direct lookup
        return TYPE_MAPPING.getOrDefault(umlType, umlType);
    }
    
    /**
     * Map generic types like List<String>, Optional<T>, Array<T>, etc.
     */
    private String mapGenericType(String genericType) {
        if (genericType.startsWith("List<") || genericType.startsWith("list<")) {
            String innerType = extractGenericInner(genericType);
            return mapType(innerType) + "[]";
        } else if (genericType.startsWith("Optional<") || genericType.startsWith("optional<")) {
            String innerType = extractGenericInner(genericType);
            return mapType(innerType) + " | null";
        } else if (genericType.startsWith("Array<") || genericType.startsWith("array<")) {
            String innerType = extractGenericInner(genericType);
            return mapType(innerType) + "[]";
        } else if (genericType.startsWith("Set<") || genericType.startsWith("set<")) {
            String innerType = extractGenericInner(genericType);
            return "Set<" + mapType(innerType) + ">";
        } else if (genericType.startsWith("Map<") || genericType.startsWith("map<")) {
            String innerType = extractGenericInner(genericType);
            return "Map<string, " + mapType(innerType) + ">";
        } else if (genericType.startsWith("Promise<") || genericType.startsWith("promise<")) {
            String innerType = extractGenericInner(genericType);
            return "Promise<" + mapType(innerType) + ">";
        }
        
        return genericType; // Return as-is if unknown generic
    }
    
    /**
     * Extract inner type from generic: List<String> -> String
     */
    private String extractGenericInner(String genericType) {
        int start = genericType.indexOf('<');
        int end = genericType.lastIndexOf('>');
        if (start >= 0 && end > start) {
            return genericType.substring(start + 1, end).trim();
        }
        return "any";
    }
    
    // ==================== VALIDATION DECORATOR GENERATION ====================
    
    /**
     * Get class-validator import statements needed
     */
    public List<String> getValidatorImports(TypeScriptModel model) {
        Set<String> imports = new LinkedHashSet<>();
        
        for (TypeScriptField field : model.getFields()) {
            for (FieldConstraint constraint : field.getConstraints()) {
                String decorator = constraint.getDecorator();
                if (!decorator.isEmpty()) {
                    // Extract class name from decorator: @IsEmail() -> IsEmail
                    String className = decorator.replaceAll("[@()]", "");
                    if (!className.isEmpty()) {
                        imports.add(className);
                    }
                }
            }
        }
        
        if (!imports.isEmpty()) {
            return new ArrayList<>(imports);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Generate TypeORM decorator for a field
     */
    public String generateTypeormDecorator(TypeScriptField field) {
        StringBuilder decorator = new StringBuilder();
        
        if (field.isPrimaryKey()) {
            decorator.append("@PrimaryGeneratedColumn('uuid')");
        } else if (field.isTimestamp()) {
            if (field.getName().equals("createdAt")) {
                decorator.append("@CreateDateColumn()");
            } else if (field.getName().equals("updatedAt")) {
                decorator.append("@UpdateDateColumn()");
            } else {
                decorator.append("@Column()");
            }
        } else {
            decorator.append("@Column({\n");
            decorator.append("  type: '").append(getSqlType(field.getType())).append("'");
            
            if (field.isUnique()) {
                decorator.append(",\n  unique: true");
            }
            if (field.isRequired()) {
                decorator.append(",\n  nullable: false");
            } else {
                decorator.append(",\n  nullable: true");
            }
            if (field.getDefaultValue() != null) {
                decorator.append(",\n  default: ").append(field.getDefaultValue());
            }
            
            decorator.append("\n})");
        }
        
        return decorator.toString();
    }
    
    /**
     * Map TypeScript type to SQL column type
     */
    private String getSqlType(String tsType) {
        return switch(tsType) {
            case "string" -> "varchar";
            case "number" -> "int";
            case "boolean" -> "boolean";
            case "bigint" -> "bigint";
            case "Date" -> "timestamp";
            case "UUID" -> "uuid";
            default -> "varchar";
        };
    }
    
    /**
     * Check if type is a custom entity (relationship)
     */
    public boolean isCustomType(String type) {
        return !TYPE_MAPPING.containsKey(type) && 
               !type.contains("[") && 
               !type.contains("<") &&
               Character.isUpperCase(type.charAt(0));
    }
}

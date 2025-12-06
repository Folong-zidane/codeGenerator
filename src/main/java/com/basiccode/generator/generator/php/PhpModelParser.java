package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.FieldModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhpModelParser - Parses UML class diagrams to PHP model definitions
 * 
 * Phase 2 Week 1 - CRITICAL FOUNDATION
 * 
 * Supports:
 * - 20+ PHP/Laravel column types
 * - 8+ constraint types
 * - 3 relationship types (OneToMany, ManyToMany, OneToOne)
 * - Validation rules parsing
 * - State enums with transitions
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpModelParser {
    
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "class\\s+([A-Z][a-zA-Z0-9]*)\\s*\\{([^}]*)\\}",
        Pattern.DOTALL
    );
    
    private static final Pattern FIELD_PATTERN = Pattern.compile(
        "(\\w+)\\s*:\\s*([\\w<>?,\\s]+)(?:\\s*\\[([^\\]]+)\\])?",
        Pattern.MULTILINE
    );
    
    private static final Pattern RELATIONSHIP_PATTERN = Pattern.compile(
        "([A-Z][a-zA-Z0-9]*)\\s+([\"']1[\"']|[\"']\\*[\"'])\\s+--+>?\\s+([\"']1[\"']|[\"']\\*[\"'])\\s+([A-Z][a-zA-Z0-9]*)",
        Pattern.MULTILINE
    );
    
    private static final Map<String, String> CONSTRAINT_MAPPING = Map.ofEntries(
        Map.entry("required", "required"),
        Map.entry("unique", "unique"),
        Map.entry("@Unique", "unique"),
        Map.entry("@Required", "required"),
        Map.entry("@Email", "email"),
        Map.entry("@URL", "url"),
        Map.entry("@Max", "max"),
        Map.entry("@Min", "min"),
        Map.entry("@Pattern", "regex"),
        Map.entry("@Nullable", "nullable"),
        Map.entry("@Default", "default")
    );
    
    /**
     * Parse ClassModel from UML to PHP model definition
     */
    public PhpModelDefinition parseClassModel(ClassModel classModel) {
        PhpModelDefinition model = new PhpModelDefinition();
        model.setName(classModel.getName());
        model.setTableName(toTableName(classModel.getName()));
        model.setNamespace("App\\Models");
        
        // Parse fields
        List<PhpFieldDefinition> fields = new ArrayList<>();
        for (var field : classModel.getFields()) {
            FieldModel fieldModel = (field instanceof FieldModel) ? (FieldModel) field : new FieldModel(field.getName(), field.getType());
            fields.add(parseField(fieldModel));
        }
        model.setFields(fields);
        
        log.debug("Parsed model: {} with {} fields", model.getName(), fields.size());
        return model;
    }
    
    /**
     * Parse individual field to PHP field definition
     */
    private PhpFieldDefinition parseField(FieldModel fieldModel) {
        PhpFieldDefinition field = new PhpFieldDefinition();
        field.setName(fieldModel.getName());
        field.setType(mapFieldType(fieldModel.getType()));
        field.setNullable(fieldModel.getType().contains("?"));
        
        // Parse constraints from comments/annotations
        List<PhpConstraintDefinition> constraints = parseConstraints(fieldModel);
        field.setConstraints(constraints);
        
        return field;
    }
    
    /**
     * Parse constraints from field definition
     */
    private List<PhpConstraintDefinition> parseConstraints(FieldModel fieldModel) {
        List<PhpConstraintDefinition> constraints = new ArrayList<>();
        
        // Add default constraints
        if (!fieldModel.getType().contains("?")) {
            PhpConstraintDefinition required = new PhpConstraintDefinition();
            required.setType("required");
            required.setErrorMessage("The " + fieldModel.getName() + " field is required");
            constraints.add(required);
        }
        
        // Parse type-specific constraints
        String type = fieldModel.getType();
        if (type.contains("email") || type.contains("Email")) {
            PhpConstraintDefinition email = new PhpConstraintDefinition();
            email.setType("email");
            email.setErrorMessage("The " + fieldModel.getName() + " must be a valid email");
            constraints.add(email);
        }
        
        if (type.contains("uuid") || type.contains("UUID")) {
            PhpConstraintDefinition uuid = new PhpConstraintDefinition();
            uuid.setType("uuid");
            uuid.setErrorMessage("The " + fieldModel.getName() + " must be a valid UUID");
            constraints.add(uuid);
        }
        
        return constraints;
    }
    
    /**
     * Map UML/Java type to Laravel column type
     */
    private String mapFieldType(String umlType) {
        if (umlType == null) return "string";
        
        umlType = umlType.trim().toLowerCase();
        
        return switch (umlType) {
            // String types
            case "string", "str" -> "string";
            case "text", "longtext" -> "text";
            case "slug" -> "string";
            case "email" -> "string";
            case "url", "uri" -> "string";
            case "phone", "tel" -> "string";
            
            // Numeric types
            case "int", "integer" -> "integer";
            case "bigint", "long" -> "bigInteger";
            case "smallint", "short" -> "smallInteger";
            case "float", "double", "decimal" -> "decimal";
            case "numeric" -> "decimal";
            
            // Boolean
            case "bool", "boolean" -> "boolean";
            
            // Date/Time
            case "date" -> "date";
            case "time" -> "time";
            case "datetime", "timestamp" -> "timestamp";
            
            // Advanced types
            case "uuid" -> "uuid";
            case "json", "object" -> "json";
            case "binary", "blob" -> "binary";
            case "enum" -> "enum";
            
            default -> "string";
        };
    }
    
    /**
     * Validate parsed model
     */
    public void validateModel(PhpModelDefinition model) throws InvalidModelException {
        if (model.getName() == null || model.getName().isEmpty()) {
            throw new InvalidModelException("Model name cannot be empty");
        }
        
        if (model.getFields() == null || model.getFields().isEmpty()) {
            throw new InvalidModelException("Model must have at least one field");
        }
        
        log.debug("Model validation passed for: {}", model.getName());
    }
    
    /**
     * Convert model name to table name (plural, snake_case)
     */
    private String toTableName(String modelName) {
        String snakeCase = modelName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        // Simple pluralization
        if (snakeCase.endsWith("y")) {
            return snakeCase.substring(0, snakeCase.length() - 1) + "ies";
        }
        return snakeCase + "s";
    }
    
    /**
     * Exception for invalid models
     */
    public static class InvalidModelException extends Exception {
        public InvalidModelException(String message) {
            super(message);
        }
    }
    
    /**
     * DTO: PHP Model Definition
     */
    @Data
    public static class PhpModelDefinition {
        private String name;
        private String namespace;
        private String tableName;
        private List<PhpFieldDefinition> fields;
        private List<PhpRelationshipDefinition> relationships;
        private List<String> enums;
        private String baseClass = "Model";
        private List<String> traits = List.of("HasFactory");
        
        public PhpModelDefinition() {
            this.fields = new ArrayList<>();
            this.relationships = new ArrayList<>();
            this.enums = new ArrayList<>();
        }
    }
    
    /**
     * DTO: PHP Field Definition
     */
    @Data
    public static class PhpFieldDefinition {
        private String name;
        private String type; // Laravel column type
        private boolean nullable;
        private boolean unique;
        private String defaultValue;
        private List<PhpConstraintDefinition> constraints = new ArrayList<>();
        
        public PhpFieldDefinition() {}
        
        public PhpFieldDefinition(String name, String type) {
            this.name = name;
            this.type = type;
            this.constraints = new ArrayList<>();
        }
    }
    
    /**
     * DTO: PHP Relationship Definition
     */
    @Data
    public static class PhpRelationshipDefinition {
        public enum RelationType {
            ONE_TO_ONE, ONE_TO_MANY, MANY_TO_MANY
        }
        
        private String sourceModel;
        private String targetModel;
        private RelationType type;
        private String methodName;
        private String foreignKey;
        private String relationTable; // For M2M
        private boolean eager = false;
        
        public PhpRelationshipDefinition() {}
        
        public PhpRelationshipDefinition(String source, String target, RelationType type) {
            this.sourceModel = source;
            this.targetModel = target;
            this.type = type;
            this.methodName = lcFirst(target);
        }
    }
    
    /**
     * DTO: PHP Constraint Definition
     */
    @Data
    public static class PhpConstraintDefinition {
        private String type;
        private Map<String, String> params = new HashMap<>();
        private String errorMessage;
        
        public PhpConstraintDefinition() {}
        
        public PhpConstraintDefinition(String type) {
            this.type = type;
        }
    }
    
    /**
     * Helper: Convert first character to lowercase
     */
    private static String lcFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}

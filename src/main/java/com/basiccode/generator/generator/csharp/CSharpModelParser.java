package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSharpModelParser - Parse UML diagrams to C# model definitions
 * Supports Entity Framework Core 8 (.NET 8) data annotations and configurations
 * 
 * Phase 2 Week 1 - C# PARSING
 * 
 * Generates:
 * - Model definition data structures
 * - Relationship metadata
 * - Constraint definitions (DataAnnotations)
 * - Type mappings to C# types
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpModelParser {

    /**
     * C# Model Definition - Core data structure
     */
    public static class CSharpModelDefinition {
        private final String modelName;
        private final String tableName;
        private final List<CSharpFieldDefinition> fields;
        private final List<CSharpRelationshipDefinition> relationships;
        private final boolean isAuditable;
        private final String dbSetName;

        public CSharpModelDefinition(String modelName, List<CSharpFieldDefinition> fields,
                                    List<CSharpRelationshipDefinition> relationships) {
            this.modelName = modelName;
            this.tableName = toTableName(modelName);
            this.fields = fields;
            this.relationships = relationships;
            this.isAuditable = true;
            this.dbSetName = pluralize(modelName);
        }

        // Getters
        public String getModelName() { return modelName; }
        public String getTableName() { return tableName; }
        public List<CSharpFieldDefinition> getFields() { return fields; }
        public List<CSharpRelationshipDefinition> getRelationships() { return relationships; }
        public boolean isAuditable() { return isAuditable; }
        public String getDbSetName() { return dbSetName; }

        private static String toTableName(String modelName) {
            return modelName.toLowerCase() + "s";
        }

        private static String pluralize(String name) {
            return name + "s";
        }
    }

    /**
     * C# Field Definition - Field metadata with constraints
     */
    public static class CSharpFieldDefinition {
        private final String fieldName;
        private final String csharpType;
        private final boolean required;
        private final Integer maxLength;
        private final Integer minLength;
        private final String pattern;
        private final boolean isKey;
        private final boolean isConcurrencyToken;
        private final List<String> dataAnnotations;

        public CSharpFieldDefinition(String fieldName, String csharpType, boolean required) {
            this.fieldName = fieldName;
            this.csharpType = csharpType;
            this.required = required;
            this.maxLength = null;
            this.minLength = null;
            this.pattern = null;
            this.isKey = false;
            this.isConcurrencyToken = false;
            this.dataAnnotations = generateAnnotations(required, null, null);
        }

        public CSharpFieldDefinition(String fieldName, String csharpType, boolean required,
                                    Integer maxLength, Integer minLength, String pattern) {
            this.fieldName = fieldName;
            this.csharpType = csharpType;
            this.required = required;
            this.maxLength = maxLength;
            this.minLength = minLength;
            this.pattern = pattern;
            this.isKey = false;
            this.isConcurrencyToken = false;
            this.dataAnnotations = generateAnnotations(required, maxLength, minLength);
        }

        // Getters
        public String getFieldName() { return fieldName; }
        public String getCSharpType() { return csharpType; }
        public boolean isRequired() { return required; }
        public Integer getMaxLength() { return maxLength; }
        public Integer getMinLength() { return minLength; }
        public String getPattern() { return pattern; }
        public boolean isKey() { return isKey; }
        public boolean isConcurrencyToken() { return isConcurrencyToken; }
        public List<String> getDataAnnotations() { return dataAnnotations; }

        private static List<String> generateAnnotations(boolean required, Integer maxLength, Integer minLength) {
            List<String> annotations = new java.util.ArrayList<>();
            if (required) {
                annotations.add("[Required]");
            }
            if (maxLength != null) {
                annotations.add("[StringLength(" + maxLength + ")]");
            }
            return annotations;
        }
    }

    /**
     * C# Relationship Definition
     */
    public static class CSharpRelationshipDefinition {
        public enum RelationType {
            ONE_TO_MANY,
            MANY_TO_MANY,
            ONE_TO_ONE
        }

        private final String sourceModel;
        private final String targetModel;
        private final RelationType type;
        private final String navigationProperty;
        private final String foreignKeyName;
        private final String joinTableName;

        public CSharpRelationshipDefinition(String sourceModel, String targetModel, RelationType type) {
            this.sourceModel = sourceModel;
            this.targetModel = targetModel;
            this.type = type;
            this.navigationProperty = pluralize(targetModel);
            this.foreignKeyName = sourceModel + "Id";
            this.joinTableName = sourceModel + targetModel;
        }

        // Getters
        public String getSourceModel() { return sourceModel; }
        public String getTargetModel() { return targetModel; }
        public RelationType getType() { return type; }
        public String getNavigationProperty() { return navigationProperty; }
        public String getForeignKeyName() { return foreignKeyName; }
        public String getJoinTableName() { return joinTableName; }

        private static String pluralize(String name) {
            return name + "s";
        }
    }

    /**
     * C# Constraint Definition
     */
    public static class CSharpConstraintDefinition {
        private final String fieldName;
        private final String constraintType;
        private final String annotation;

        public CSharpConstraintDefinition(String fieldName, String constraintType, String annotation) {
            this.fieldName = fieldName;
            this.constraintType = constraintType;
            this.annotation = annotation;
        }

        // Getters
        public String getFieldName() { return fieldName; }
        public String getConstraintType() { return constraintType; }
        public String getAnnotation() { return annotation; }
    }

    /**
     * Parse UML EnhancedClass to C# Model Definition
     */
    public CSharpModelDefinition parseModel(EnhancedClass enhancedClass) {
        String modelName = enhancedClass.getOriginalClass().getName();
        
        // Parse fields
        List<CSharpFieldDefinition> fields = parseFields(enhancedClass);
        
        // Parse relationships
        List<CSharpRelationshipDefinition> relationships = parseRelationships(enhancedClass);
        
        return new CSharpModelDefinition(modelName, fields, relationships);
    }

    /**
     * Parse fields with constraints
     */
    private List<CSharpFieldDefinition> parseFields(EnhancedClass enhancedClass) {
        List<CSharpFieldDefinition> fields = new java.util.ArrayList<>();
        
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            String fieldName = attr.getName();
            String csharpType = mapToCSharpType(attr.getType());
            boolean required = !attr.isNullable();
            
            CSharpFieldDefinition field = new CSharpFieldDefinition(
                fieldName,
                csharpType,
                required
            );
            fields.add(field);
        }
        
        return fields;
    }

    /**
     * Parse relationships
     */
    private List<CSharpRelationshipDefinition> parseRelationships(EnhancedClass enhancedClass) {
        List<CSharpRelationshipDefinition> relationships = new java.util.ArrayList<>();
        
        if (enhancedClass.getOriginalClass().getRelationships() != null) {
            for (var rel : enhancedClass.getOriginalClass().getRelationships()) {
                String sourceModel = enhancedClass.getOriginalClass().getName();
                String targetModel = rel.getTargetClass();
                
                CSharpRelationshipDefinition.RelationType type = 
                    parseRelationshipType(String.valueOf(rel.getSourceMultiplicity()), String.valueOf(rel.getTargetMultiplicity()));
                
                relationships.add(new CSharpRelationshipDefinition(sourceModel, targetModel, type));
            }
        }
        
        return relationships;
    }

    /**
     * Parse relationship type from multiplicity
     */
    private CSharpRelationshipDefinition.RelationType parseRelationshipType(String source, String target) {
        if (source.equals("1") && target.equals("*")) {
            return CSharpRelationshipDefinition.RelationType.ONE_TO_MANY;
        } else if (source.equals("*") && target.equals("1")) {
            return CSharpRelationshipDefinition.RelationType.ONE_TO_MANY;
        } else if (source.equals("*") && target.equals("*")) {
            return CSharpRelationshipDefinition.RelationType.MANY_TO_MANY;
        } else if (source.equals("1") && target.equals("1")) {
            return CSharpRelationshipDefinition.RelationType.ONE_TO_ONE;
        }
        return CSharpRelationshipDefinition.RelationType.ONE_TO_MANY;
    }

    /**
     * Map UML type to C# type
     */
    private String mapToCSharpType(String umlType) {
        return switch (umlType.toLowerCase()) {
            case "string", "text" -> "string";
            case "int", "integer" -> "int";
            case "long", "bigint" -> "long";
            case "decimal", "float", "double" -> "decimal";
            case "boolean", "bool" -> "bool";
            case "date" -> "DateTime";
            case "datetime", "timestamp" -> "DateTime";
            case "uuid" -> "Guid";
            case "json" -> "string";
            default -> "string";
        };
    }

    /**
     * Validate model definition
     */
    public boolean validateModel(CSharpModelDefinition model) {
        return model.getModelName() != null && !model.getModelName().isEmpty()
                && model.getFields() != null && !model.getFields().isEmpty();
    }
}

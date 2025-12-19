package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLAttribute;
import java.util.Map;

/**
 * Générateur d'entités C# avec Entity Framework
 */
public class CSharpEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        return generateEntity(enhancedClass, packageName, null);
    }
    
    public String generateEntity(EnhancedClass enhancedClass, String packageName, Map<String, String> metadata) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder entity = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Using statements
        entity.append("using System;\n");
        entity.append("using System.ComponentModel.DataAnnotations;\n");
        entity.append("using System.ComponentModel.DataAnnotations.Schema;\n");
        entity.append("using System.Collections.Generic;\n\n");
        
        // Namespace
        entity.append("namespace ").append(packageName).append(".Entities\n{\n");
        
        // Table attribute
        entity.append("    [Table(\"").append(toSnakeCase(className)).append("\")]\n");
        
        // Class definition
        entity.append("    public class ").append(className).append("\n    {\n");
        
        // Primary key
        entity.append("        [Key]\n");
        entity.append("        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]\n");
        entity.append("        public long Id { get; set; }\n\n");
        
        // Generate properties from attributes
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                if (!"id".equalsIgnoreCase(attr.getName())) {
                    entity.append(generateCSharpProperty(attr, metadata));
                }
            }
        }
        
        // Audit fields
        entity.append("        [Column(\"created_at\")]\n");
        entity.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n\n");
        entity.append("        [Column(\"updated_at\")]\n");
        entity.append("        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;\n\n");
        
        // Navigation properties placeholder
        entity.append("        // Navigation properties\n");
        entity.append("        // public virtual ICollection<RelatedEntity> RelatedEntities { get; set; }\n\n");
        
        entity.append("    }\n");
        entity.append("}\n");
        
        return entity.toString();
    }
    
    @Override
    public String getEntityDirectory() {
        return "Entities";
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null) {
            return "";
        }
        
        StringBuilder enumCode = new StringBuilder();
        String className = enhancedClass.getOriginalClass() != null ? 
            enhancedClass.getOriginalClass().getName() : "State";
        
        enumCode.append("using System;\n\n");
        enumCode.append("namespace ").append(packageName).append(".Enums\n{\n");
        enumCode.append("    public enum ").append(className).append("Status\n    {\n");
        enumCode.append("        Active = 1,\n");
        enumCode.append("        Inactive = 2,\n");
        enumCode.append("        Pending = 3,\n");
        enumCode.append("        Completed = 4\n");
        enumCode.append("    }\n");
        enumCode.append("}\n");
        
        return enumCode.toString();
    }
    
    private String generateCSharpProperty(UMLAttribute attr, Map<String, String> metadata) {
        StringBuilder property = new StringBuilder();
        
        String propertyName = toPascalCase(attr.getName());
        String columnName = toSnakeCase(attr.getName());
        String csharpType = mapToCSharpType(attr);
        
        // Column attribute
        property.append("        [Column(\"").append(columnName).append("\")]\n");
        
        // Validation attributes
        property.append(generateValidationAttributes(attr));
        
        // Property declaration
        property.append("        public ").append(csharpType).append(" ").append(propertyName);
        property.append(" { get; set; }");
        
        // Default value
        String defaultValue = getDefaultValue(attr);
        if (!defaultValue.isEmpty()) {
            property.append(" = ").append(defaultValue).append(";");
        }
        
        property.append("\n\n");
        return property.toString();
    }
    
    private String generateValidationAttributes(UMLAttribute attr) {
        StringBuilder validation = new StringBuilder();
        String type = attr.getType();
        String name = attr.getName().toLowerCase();
        
        if ("string".equalsIgnoreCase(type)) {
            if (name.contains("email")) {
                validation.append("        [EmailAddress]\n");
                validation.append("        [Required]\n");
            } else if (name.contains("name") || name.contains("title")) {
                validation.append("        [Required]\n");
                validation.append("        [StringLength(255)]\n");
            } else {
                validation.append("        [StringLength(500)]\n");
            }
        } else if ("integer".equalsIgnoreCase(type) || "int".equalsIgnoreCase(type)) {
            if (name.contains("price") || name.contains("amount")) {
                validation.append("        [Range(0, double.MaxValue)]\n");
            }
        }
        
        return validation.toString();
    }
    
    private String mapToCSharpType(UMLAttribute attr) {
        String umlType = attr.getType();
        if (umlType == null) return "string";
        
        switch (umlType.toLowerCase()) {
            case "string":
            case "str":
                return "string";
            case "integer":
            case "int":
                return "int";
            case "long":
                return "long";
            case "float":
                return "float";
            case "double":
                return "double";
            case "boolean":
            case "bool":
                return "bool";
            case "bigdecimal":
            case "decimal":
                return "decimal";
            case "localdatetime":
            case "datetime":
                return "DateTime";
            case "localdate":
            case "date":
                return "DateTime";
            case "uuid":
                return "Guid";
            default:
                return "string";
        }
    }
    
    private String getDefaultValue(UMLAttribute attr) {
        String type = attr.getType();
        String name = attr.getName().toLowerCase();
        
        if ("boolean".equalsIgnoreCase(type) || "bool".equalsIgnoreCase(type)) {
            if (name.contains("active") || name.contains("enabled")) {
                return "true";
            }
            return "false";
        } else if ("uuid".equalsIgnoreCase(type)) {
            return "Guid.NewGuid()";
        }
        
        return "";
    }
    
    private String toPascalCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
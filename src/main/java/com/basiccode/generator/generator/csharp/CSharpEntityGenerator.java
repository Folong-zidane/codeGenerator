package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

/**
 * C# Entity generator for .NET Core
 * Generates Entity Framework Core entities with proper annotations
 */
public class CSharpEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Using statements
        code.append("using System.ComponentModel.DataAnnotations;\n");
        code.append("using System.ComponentModel.DataAnnotations.Schema;\n");
        code.append("using System;\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("using ").append(packageName).append(".Enums;\n\n");
        }
        
        // Namespace
        code.append("namespace ").append(packageName).append(".Models\n");
        code.append("{\n");
        
        // Entity class with Table attribute
        code.append("    [Table(\"").append(className.toLowerCase()).append("s\")]\n");
        code.append("    public class ").append(className).append("\n");
        code.append("    {\n");
        
        // Generate properties
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if ("id".equalsIgnoreCase(attr.getName())) {
                code.append("        [Key]\n");
                code.append("        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]\n");
            }
            code.append("        public ").append(mapType(attr.getType())).append(" ");
            code.append(capitalize(attr.getName())).append(" { get; set; }\n\n");
        }
        
        // Add state property if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("        [Column(\"status\")]\n");
            code.append("        public ").append(enumName).append(" Status { get; set; }\n\n");
        }
        
        // Add audit properties
        code.append("        [Column(\"created_at\")]\n");
        code.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n\n");
        
        code.append("        [Column(\"updated_at\")]\n");
        code.append("        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;\n\n");
        
        // Add state transition methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateTransitionMethods(code, enhancedClass);
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        if (!enhancedClass.isStateful()) return "";
        
        StringBuilder code = new StringBuilder();
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        code.append("namespace ").append(packageName).append(".Enums\n");
        code.append("{\n");
        code.append("    public enum ").append(enumName).append("\n");
        code.append("    {\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (int i = 0; i < enhancedClass.getStateEnum().getValues().size(); i++) {
                var value = enhancedClass.getStateEnum().getValues().get(i);
                code.append("        ").append(value.getName());
                if (i < enhancedClass.getStateEnum().getValues().size() - 1) {
                    code.append(",");
                }
                code.append("\n");
            }
        } else {
            code.append("        ACTIVE,\n");
            code.append("        INACTIVE,\n");
            code.append("        SUSPENDED\n");
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
    
    @Override
    public String getEntityDirectory() {
        return "Models";
    }
    
    private String mapType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long" -> "long";
            case "integer", "int" -> "int";
            case "float" -> "float";
            case "double" -> "double";
            case "boolean" -> "bool";
            case "date", "datetime" -> "DateTime";
            default -> "string";
        };
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private void generateStateTransitionMethods(StringBuilder code, EnhancedClass enhancedClass) {
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        // Suspend method
        code.append("        public void Suspend()\n");
        code.append("        {\n");
        code.append("            if (Status != ").append(enumName).append(".ACTIVE)\n");
        code.append("            {\n");
        code.append("                throw new InvalidOperationException($\"Cannot suspend entity in state: {Status}\");\n");
        code.append("            }\n");
        code.append("            Status = ").append(enumName).append(".SUSPENDED;\n");
        code.append("            UpdatedAt = DateTime.UtcNow;\n");
        code.append("        }\n\n");
        
        // Activate method
        code.append("        public void Activate()\n");
        code.append("        {\n");
        code.append("            if (Status != ").append(enumName).append(".SUSPENDED)\n");
        code.append("            {\n");
        code.append("                throw new InvalidOperationException($\"Cannot activate entity in state: {Status}\");\n");
        code.append("            }\n");
        code.append("            Status = ").append(enumName).append(".ACTIVE;\n");
        code.append("            UpdatedAt = DateTime.UtcNow;\n");
        code.append("        }\n\n");
    }
}
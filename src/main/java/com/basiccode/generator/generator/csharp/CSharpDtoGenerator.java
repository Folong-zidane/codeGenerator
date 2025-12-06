package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.UmlAttribute;

/**
 * C# DTO Generator with complete validation
 */
public class CSharpDtoGenerator {
    
    public String generateCreateDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        
        // DTO class
        code.append("    /// <summary>\n");
        code.append("    /// DTO for creating ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("CreateDto\n");
        code.append("    {\n");
        
        // Properties (exclude ID)
        for (com.basiccode.generator.model.Field field : classModel.getFields()) {
            if ("id".equalsIgnoreCase(field.getName())) {
                continue; // Skip ID in create DTO
            }
            
            UmlAttribute attr = new UmlAttribute(field.getName(), field.getType());
            generatePropertyWithValidation(code, attr);
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateUpdateDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        
        // DTO class
        code.append("    /// <summary>\n");
        code.append("    /// DTO for updating ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("UpdateDto\n");
        code.append("    {\n");
        
        // ID property (required for updates)
        code.append("        /// <summary>\n");
        code.append("        /// Entity ID\n");
        code.append("        /// </summary>\n");
        code.append("        [Required]\n");
        code.append("        public Guid Id { get; set; }\n\n");
        
        // Properties
        for (com.basiccode.generator.model.Field field : classModel.getFields()) {
            if ("id".equalsIgnoreCase(field.getName())) {
                continue; // ID already added
            }
            
            UmlAttribute attr = new UmlAttribute(field.getName(), field.getType());
            generatePropertyWithValidation(code, attr);
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateReadDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        
        // DTO class
        code.append("    /// <summary>\n");
        code.append("    /// DTO for reading ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("ReadDto\n");
        code.append("    {\n");
        
        // ID property
        code.append("        /// <summary>\n");
        code.append("        /// Entity ID\n");
        code.append("        /// </summary>\n");
        code.append("        public Guid Id { get; set; }\n\n");
        
        // Properties (no validation needed for read)
        for (com.basiccode.generator.model.Field field : classModel.getFields()) {
            if ("id".equalsIgnoreCase(field.getName())) {
                continue; // ID already added
            }
            
            UmlAttribute attr = new UmlAttribute(field.getName(), field.getType());
            code.append("        /// <summary>\n");
            code.append("        /// ").append(capitalize(attr.getName())).append("\n");
            code.append("        /// </summary>\n");
            code.append("        public ").append(mapType(attr.getType())).append(" ");
            code.append(capitalize(attr.getName())).append(" { get; set; }\n\n");
        }
        
        // Audit properties
        code.append("        /// <summary>\n");
        code.append("        /// Creation timestamp\n");
        code.append("        /// </summary>\n");
        code.append("        public DateTime CreatedAt { get; set; }\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Last update timestamp\n");
        code.append("        /// </summary>\n");
        code.append("        public DateTime? UpdatedAt { get; set; }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private void generatePropertyWithValidation(StringBuilder code, UmlAttribute attr) {
        String type = mapType(attr.getType());
        String name = capitalize(attr.getName());
        
        code.append("        /// <summary>\n");
        code.append("        /// ").append(name).append("\n");
        code.append("        /// </summary>\n");
        
        // Add validation attributes based on type
        if ("string".equals(type)) {
            code.append("        [Required]\n");
            
            if ("email".equalsIgnoreCase(attr.getName()) || attr.getName().toLowerCase().contains("email")) {
                code.append("        [EmailAddress]\n");
                code.append("        [StringLength(255)]\n");
            } else if ("phone".equalsIgnoreCase(attr.getName()) || attr.getName().toLowerCase().contains("phone")) {
                code.append("        [Phone]\n");
                code.append("        [StringLength(20)]\n");
            } else if ("url".equalsIgnoreCase(attr.getName()) || attr.getName().toLowerCase().contains("url")) {
                code.append("        [Url]\n");
                code.append("        [StringLength(500)]\n");
            } else if ("password".equalsIgnoreCase(attr.getName()) || attr.getName().toLowerCase().contains("password")) {
                code.append("        [StringLength(100, MinimumLength = 8)]\n");
                code.append("        [RegularExpression(@\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\", \n");
                code.append("            ErrorMessage = \"Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character\")]\n");
            } else {
                code.append("        [StringLength(255)]\n");
            }
        } else if ("int".equals(type) || "long".equals(type)) {
            code.append("        [Range(0, int.MaxValue)]\n");
        } else if ("decimal".equals(type) || "float".equals(type) || "double".equals(type)) {
            code.append("        [Range(0.0, double.MaxValue)]\n");
        }
        
        code.append("        public ").append(type).append(" ").append(name).append(" { get; set; }\n\n");
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
            case "uuid", "guid" -> "Guid";
            case "decimal" -> "decimal";
            default -> "string";
        };
    }
    
    private String convertToNetNamespace(String javaPackage) {
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalize(parts[1]) + ".Application";
        }
        return capitalize(javaPackage.replace(".", "")) + ".Application";
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
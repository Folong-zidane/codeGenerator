package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.UmlAttribute;

/**
 * Fixed C# DTO Generator - minimal working version
 */
public class CSharpDtoGeneratorFixed {
    
    public String generateCreateDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using System;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        code.append("    public class ").append(className).append("CreateDto\n");
        code.append("    {\n");
        
        // Add basic properties with validation
        code.append("        [Required]\n");
        code.append("        [StringLength(255)]\n");
        code.append("        public string Name { get; set; }\n\n");
        
        code.append("        [StringLength(500)]\n");
        code.append("        public string Description { get; set; }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateUpdateDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using System;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        code.append("    public class ").append(className).append("UpdateDto\n");
        code.append("    {\n");
        
        code.append("        [Required]\n");
        code.append("        public Guid Id { get; set; }\n\n");
        
        code.append("        [Required]\n");
        code.append("        [StringLength(255)]\n");
        code.append("        public string Name { get; set; }\n\n");
        
        code.append("        [StringLength(500)]\n");
        code.append("        public string Description { get; set; }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateReadDto(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using System;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".DTOs\n");
        code.append("{\n");
        code.append("    public class ").append(className).append("ReadDto\n");
        code.append("    {\n");
        
        code.append("        public Guid Id { get; set; }\n");
        code.append("        public string Name { get; set; }\n");
        code.append("        public string Description { get; set; }\n");
        code.append("        public DateTime CreatedAt { get; set; }\n");
        code.append("        public DateTime? UpdatedAt { get; set; }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
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
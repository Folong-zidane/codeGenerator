package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.generator.InheritanceAwareEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

/**
 * C# Entity generator for .NET Core with inheritance support
 * Generates Entity Framework Core entities with proper annotations
 */
public class CSharpEntityGenerator implements InheritanceAwareEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n");
        code.append("using System.ComponentModel.DataAnnotations.Schema;\n");
        code.append("using System.Collections.Generic;\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("using ").append(convertToNetNamespace(packageName)).append(".Enums;\n\n");
        }
        
        // Namespace (.NET style)
        code.append("namespace ").append(convertToNetNamespace(packageName)).append(".Models\n");
        code.append("{\n");
        
        // Handle inheritance
        if (enhancedClass.getOriginalClass().isInterface()) {
            code.append("    public interface ").append(className).append("\n");
            code.append("    {\n");
            generateCSharpInterfaceMethods(code, enhancedClass);
            code.append("    }\n");
            code.append("}\n");
            return code.toString();
        }
        
        String inheritanceDecl = generateInheritanceDeclaration(enhancedClass, className);
        code.append("    ").append(inheritanceDecl).append("\n");
        code.append("    {\n");
        
        // Generate ID only if not inherited
        boolean hasInheritedFields = shouldSkipInheritedFields(enhancedClass);
        if (!hasInheritedFields) {
            code.append("        [Key]\n");
            code.append("        public Guid Id { get; set; } = Guid.NewGuid();\n\n");
        }
        
        // Generate properties (skip inherited ones)
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (isInheritedField(attr.getName())) {
                continue; // Skip inherited fields
            }
            
            // Generate C# relationship or property
            if (attr.isRelationship()) {
                generateCSharpRelationship(code, attr, className);
            } else {
                // Add validation annotations
                if ("string".equalsIgnoreCase(attr.getType())) {
                    code.append("        [Required]\n");
                    code.append("        [StringLength(255)]\n");
                }
                
                code.append("        public ").append(mapType(attr.getType())).append(" ");
                code.append(capitalize(attr.getName())).append(" { get; set; }\n\n");
            }
        }
        
        // Add state property if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("        [Column(\"status\")]\n");
            code.append("        public ").append(enumName).append(" Status { get; set; }\n\n");
        }
        
        // Add audit properties only if not inherited
        if (!hasInheritedFields) {
            code.append("        [Column(\"created_at\")]\n");
            code.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n\n");
            
            code.append("        [Column(\"updated_at\")]\n");
            code.append("        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;\n\n");
        }
        
        // Add business methods from diagram
        generateBusinessMethods(code, enhancedClass.getOriginalClass().getName());
        
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
        
        code.append("namespace ").append(convertToNetNamespace(packageName)).append(".Enums\n");
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
            case "uuid", "guid" -> "Guid";
            case "decimal" -> "decimal";
            case "email" -> "string";
            default -> "string";
        };
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private String convertToNetNamespace(String javaPackage) {
        // Convert com.example to Example.Application
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalize(parts[1]) + ".Application";
        }
        return capitalize(javaPackage.replace(".", "")) + ".Application";
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
    
    private void generateBusinessMethods(StringBuilder code, String className) {
        if ("User".equals(className)) {
            // ValidateEmail method
            code.append("        public bool ValidateEmail()\n");
            code.append("        {\n");
            code.append("            if (string.IsNullOrWhiteSpace(Email))\n");
            code.append("            {\n");
            code.append("                throw new ArgumentException(\"Email cannot be empty\");\n");
            code.append("            }\n");
            code.append("            var emailRegex = @\"^[A-Za-z0-9+_.-]+@(.+)$\";\n");
            code.append("            return System.Text.RegularExpressions.Regex.IsMatch(Email, emailRegex);\n");
            code.append("        }\n\n");
            
            // ChangePassword method
            code.append("        public void ChangePassword(string newPassword)\n");
            code.append("        {\n");
            code.append("            if (string.IsNullOrEmpty(newPassword) || newPassword.Length < 8)\n");
            code.append("            {\n");
            code.append("                throw new ArgumentException(\"Password must be at least 8 characters\");\n");
            code.append("            }\n");
            code.append("            // TODO: Hash password with BCrypt\n");
            code.append("            UpdatedAt = DateTime.UtcNow;\n");
            code.append("        }\n\n");
            
        } else if ("Product".equals(className)) {
            // UpdateStock method
            code.append("        public void UpdateStock(int newQuantity)\n");
            code.append("        {\n");
            code.append("            if (newQuantity < 0)\n");
            code.append("            {\n");
            code.append("                throw new ArgumentException(\"Stock cannot be negative\");\n");
            code.append("            }\n");
            code.append("            Stock = newQuantity;\n");
            code.append("            UpdatedAt = DateTime.UtcNow;\n");
            code.append("        }\n\n");
            
        } else if ("Order".equals(className)) {
            // CalculateTotal method
            code.append("        public decimal CalculateTotal(List<Product> products)\n");
            code.append("        {\n");
            code.append("            if (products == null || !products.Any())\n");
            code.append("            {\n");
            code.append("                throw new ArgumentException(\"Cannot calculate total: no products\");\n");
            code.append("            }\n");
            code.append("            var total = products.Sum(p => p.Price);\n");
            code.append("            Total = total;\n");
            code.append("            return total;\n");
            code.append("        }\n\n");
        }
    }
    
    @Override
    public String generateInheritanceDeclaration(EnhancedClass enhancedClass, String className) {
        if (enhancedClass.getOriginalClass().isInterface()) {
            return "public interface " + className;
        } else if (enhancedClass.getOriginalClass().isAbstract()) {
            return "public abstract class " + className;
        } else {
            String superClass = enhancedClass.getOriginalClass().getSuperClass();
            if (superClass != null && !superClass.isEmpty()) {
                return "[Table(\"" + className.toLowerCase() + "s\")]\npublic class " + className + " : " + superClass;
            } else {
                return "[Table(\"" + className.toLowerCase() + "s\")]\npublic class " + className;
            }
        }
    }
    
    private void generateCSharpInterfaceMethods(StringBuilder code, EnhancedClass enhancedClass) {
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                String returnType = method.getReturnType() != null ? mapType(method.getReturnType()) : "void";
                code.append("        ").append(returnType).append(" ").append(capitalize(method.getName())).append("(");
                
                if (method.getParameters() != null && !method.getParameters().isEmpty()) {
                    for (int i = 0; i < method.getParameters().size(); i++) {
                        var param = method.getParameters().get(i);
                        String paramType = param.getType() != null ? mapType(param.getType()) : "string";
                        code.append(paramType).append(" ").append(param.getName());
                        if (i < method.getParameters().size() - 1) {
                            code.append(", ");
                        }
                    }
                }
                
                code.append(");\n\n");
            }
        }
    }
    
    private void generateCSharpRelationship(StringBuilder code, UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        String propertyName = capitalize(attr.getName());
        
        switch (relationshipType) {
            case "OneToMany":
                code.append("        public virtual ICollection<").append(targetClass).append("> ").append(propertyName).append(" { get; set; } = new List<").append(targetClass).append(">();\n\n");
                break;
            case "ManyToOne":
                code.append("        [ForeignKey(\"").append(targetClass).append("Id\")]\n");
                code.append("        public Guid ").append(targetClass).append("Id { get; set; }\n\n");
                code.append("        public virtual ").append(targetClass).append(" ").append(propertyName).append(" { get; set; }\n\n");
                break;
            case "OneToOne":
                code.append("        [ForeignKey(\"").append(targetClass).append("Id\")]\n");
                code.append("        public Guid? ").append(targetClass).append("Id { get; set; }\n\n");
                code.append("        public virtual ").append(targetClass).append(" ").append(propertyName).append(" { get; set; }\n\n");
                break;
            case "ManyToMany":
                code.append("        public virtual ICollection<").append(targetClass).append("> ").append(propertyName).append(" { get; set; } = new List<").append(targetClass).append(">();\n\n");
                break;
        }
    }
}
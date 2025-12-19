package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import org.springframework.stereotype.Component;

/**
 * Générateur d'entités Spring Boot classique
 */
@Component
public class SpringBootEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Package
        code.append("package ").append(packageName).append(".entity;\n\n");
        
        // Imports
        code.append("import javax.persistence.*;\n");
        code.append("import lombok.Data;\n");
        code.append("import lombok.NoArgsConstructor;\n");
        code.append("import lombok.AllArgsConstructor;\n");
        code.append("import java.time.LocalDateTime;\n\n");
        
        // Annotations de classe
        code.append("@Entity\n");
        code.append("@Table(name = \"").append(toSnakeCase(className)).append("\")\n");
        code.append("@Data\n");
        code.append("@NoArgsConstructor\n");
        code.append("@AllArgsConstructor\n");
        
        // Déclaration de classe
        code.append("public class ").append(className).append(" {\n\n");
        
        // ID par défaut
        code.append("    @Id\n");
        code.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
        code.append("    private Long id;\n\n");
        
        // Attributs
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
                if (!"id".equals(attr.getName())) {
                    code.append("    @Column(name = \"").append(toSnakeCase(attr.getName())).append("\")\n");
                    code.append("    private ").append(mapToJavaType(attr.getType())).append(" ")
                        .append(attr.getName()).append(";\n\n");
                }
            });
        }
        
        // Timestamps
        code.append("    @Column(name = \"created_at\")\n");
        code.append("    private LocalDateTime createdAt;\n\n");
        
        code.append("    @Column(name = \"updated_at\")\n");
        code.append("    private LocalDateTime updatedAt;\n\n");
        
        // Méthodes de callback
        code.append("    @PrePersist\n");
        code.append("    protected void onCreate() {\n");
        code.append("        createdAt = LocalDateTime.now();\n");
        code.append("        updatedAt = LocalDateTime.now();\n");
        code.append("    }\n\n");
        
        code.append("    @PreUpdate\n");
        code.append("    protected void onUpdate() {\n");
        code.append("        updatedAt = LocalDateTime.now();\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    private String mapToJavaType(String umlType) {
        switch (umlType.toLowerCase()) {
            case "string":
                return "String";
            case "int":
            case "integer":
                return "Integer";
            case "long":
                return "Long";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "boolean":
                return "Boolean";
            case "date":
                return "java.time.LocalDate";
            case "datetime":
                return "java.time.LocalDateTime";
            case "uuid":
                return "java.util.UUID";
            default:
                return "String";
        }
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        return "";
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getEntityDirectory() {
        return "entity";
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
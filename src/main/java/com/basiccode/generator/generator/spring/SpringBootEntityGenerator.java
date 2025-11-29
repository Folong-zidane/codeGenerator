package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

/**
 * Spring Boot specific entity generator
 */
public class SpringBootEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("package ").append(packageName).append(".entity;\n\n");
        code.append("import javax.persistence.*;\n");
        code.append("import java.time.LocalDateTime;\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("import ").append(packageName).append(".enums.").append(enumName).append(";\n\n");
        }
        
        code.append("@Entity\n");
        code.append("@Table(name = \"").append(className.toLowerCase()).append("s\")\n");
        code.append("public class ").append(className).append(" {\n\n");
        
        // Generate fields
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if ("id".equals(attr.getName())) {
                code.append("    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            code.append("    @Column\n");
            code.append("    private ").append(attr.getType()).append(" ").append(attr.getName()).append(";\n\n");
        }
        
        // Add state field if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    @Enumerated(EnumType.STRING)\n");
            code.append("    @Column(name = \"status\")\n");
            code.append("    private ").append(enumName).append(" status;\n\n");
        }
        
        // Add audit fields
        code.append("    @Column(name = \"created_at\")\n");
        code.append("    private LocalDateTime createdAt;\n\n");
        code.append("    @Column(name = \"updated_at\")\n");
        code.append("    private LocalDateTime updatedAt;\n\n");
        
        // Generate getters and setters
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            generateGetterSetter(code, attr.getType(), attr.getName());
        }
        
        // Add state getters/setters if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            generateGetterSetter(code, enumName, "status");
        }
        
        // Add audit field getters/setters
        generateGetterSetter(code, "LocalDateTime", "createdAt");
        generateGetterSetter(code, "LocalDateTime", "updatedAt");
        
        // Add state transition methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateTransitionMethods(code, enhancedClass);
        }
        
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
        
        code.append("package ").append(packageName).append(".enums;\n\n");
        code.append("public enum ").append(enumName).append(" {\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (int i = 0; i < enhancedClass.getStateEnum().getValues().size(); i++) {
                var value = enhancedClass.getStateEnum().getValues().get(i);
                code.append("    ").append(value.getName());
                if (i < enhancedClass.getStateEnum().getValues().size() - 1) {
                    code.append(",");
                }
                code.append("\n");
            }
        } else {
            code.append("    ACTIVE,\n");
            code.append("    INACTIVE,\n");
            code.append("    SUSPENDED\n");
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getEntityDirectory() {
        return "entity";
    }
    
    private void generateGetterSetter(StringBuilder code, String type, String fieldName) {
        String capitalizedName = capitalize(fieldName);
        
        // Getter
        code.append("    public ").append(type).append(" get").append(capitalizedName).append("() {\n");
        code.append("        return ").append(fieldName).append(";\n");
        code.append("    }\n\n");
        
        // Setter
        code.append("    public void set").append(capitalizedName).append("(").append(type).append(" ").append(fieldName).append(") {\n");
        code.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        code.append("    }\n\n");
    }
    
    private void generateStateTransitionMethods(StringBuilder code, EnhancedClass enhancedClass) {
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        // Add suspend method
        code.append("    public void suspend() {\n");
        code.append("        if (this.status != ").append(enumName).append(".ACTIVE) {\n");
        code.append("            throw new IllegalStateException(\"Cannot suspend user in state: \" + this.status);\n");
        code.append("        }\n");
        code.append("        this.status = ").append(enumName).append(".SUSPENDED;\n");
        code.append("    }\n\n");
        
        // Add activate method
        code.append("    public void activate() {\n");
        code.append("        if (this.status != ").append(enumName).append(".SUSPENDED) {\n");
        code.append("            throw new IllegalStateException(\"Cannot activate user in state: \" + this.status);\n");
        code.append("        }\n");
        code.append("        this.status = ").append(enumName).append(".ACTIVE;\n");
        code.append("    }\n\n");
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
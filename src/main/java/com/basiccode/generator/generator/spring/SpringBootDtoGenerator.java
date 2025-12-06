package com.basiccode.generator.generator.spring;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Spring Boot DTO generator
 * Generates Create, Read, and Update DTOs for entities
 */
@Component
@Slf4j
public class SpringBootDtoGenerator {
    
    public String generateCreateDto(EnhancedClass enhancedClass, String packageName) {
        log.info("Generating Create DTO for {}", enhancedClass.getOriginalClass().getName());
        
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String dtoName = className + "CreateDto";
        
        code.append("package ").append(packageName).append(".dto;").append("\n\n");
        code.append("import javax.validation.constraints.*;").append("\n");
        code.append("import lombok.Data;").append("\n");
        code.append("import lombok.NoArgsConstructor;").append("\n");
        code.append("import lombok.AllArgsConstructor;").append("\n\n");
        
        code.append("@Data").append("\n");
        code.append("@NoArgsConstructor").append("\n");
        code.append("@AllArgsConstructor").append("\n");
        code.append("public class ").append(dtoName).append(" {").append("\n\n");
        
        // Generate fields (exclude id, createdAt, updatedAt)
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!isAuditField(attr.getName())) {
                generateDtoField(code, attr, true);
            }
        }
        
        // Add state field if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    private ").append(enumName).append(" status;").append("\n\n");
        }
        
        code.append("}").append("\n");
        return code.toString();
    }
    
    public String generateReadDto(EnhancedClass enhancedClass, String packageName) {
        log.info("Generating Read DTO for {}", enhancedClass.getOriginalClass().getName());
        
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String dtoName = className + "ReadDto";
        
        code.append("package ").append(packageName).append(".dto;").append("\n\n");
        code.append("import lombok.Data;").append("\n");
        code.append("import lombok.NoArgsConstructor;").append("\n");
        code.append("import lombok.AllArgsConstructor;").append("\n");
        code.append("import java.time.LocalDateTime;").append("\n\n");
        
        code.append("@Data").append("\n");
        code.append("@NoArgsConstructor").append("\n");
        code.append("@AllArgsConstructor").append("\n");
        code.append("public class ").append(dtoName).append(" {").append("\n\n");
        
        // Include all fields for read DTO
        code.append("    private Long id;").append("\n\n");
        
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!"id".equals(attr.getName())) {
                generateDtoField(code, attr, false);
            }
        }
        
        // Add state field if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    private ").append(enumName).append(" status;").append("\n\n");
        }
        
        // Add audit fields
        code.append("    private LocalDateTime createdAt;").append("\n\n");
        code.append("    private LocalDateTime updatedAt;").append("\n\n");
        
        code.append("}").append("\n");
        return code.toString();
    }
    
    public String generateUpdateDto(EnhancedClass enhancedClass, String packageName) {
        log.info("Generating Update DTO for {}", enhancedClass.getOriginalClass().getName());
        
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String dtoName = className + "UpdateDto";
        
        code.append("package ").append(packageName).append(".dto;").append("\n\n");
        code.append("import javax.validation.constraints.*;").append("\n");
        code.append("import lombok.Data;").append("\n");
        code.append("import lombok.NoArgsConstructor;").append("\n");
        code.append("import lombok.AllArgsConstructor;").append("\n\n");
        
        code.append("@Data").append("\n");
        code.append("@NoArgsConstructor").append("\n");
        code.append("@AllArgsConstructor").append("\n");
        code.append("public class ").append(dtoName).append(" {").append("\n\n");
        
        // Include ID for updates
        code.append("    @NotNull").append("\n");
        code.append("    private Long id;").append("\n\n");
        
        // Generate fields (exclude audit fields)
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!isAuditField(attr.getName()) && !"id".equals(attr.getName())) {
                generateDtoField(code, attr, true);
            }
        }
        
        // Add state field if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    private ").append(enumName).append(" status;").append("\n\n");
        }
        
        code.append("}").append("\n");
        return code.toString();
    }
    
    private void generateDtoField(StringBuilder code, UmlAttribute attr, boolean includeValidation) {
        if (includeValidation) {
            // Add validation annotations
            if ("String".equals(attr.getType()) && isRequiredField(attr.getName())) {
                code.append("    @NotBlank").append("\n");
            }
            if ("email".equals(attr.getName().toLowerCase())) {
                code.append("    @Email").append("\n");
            }
            if ("String".equals(attr.getType())) {
                code.append("    @Size(max = 255)").append("\n");
            }
        }
        
        code.append("    private ").append(attr.getType()).append(" ").append(attr.getName()).append(";").append("\n\n");
    }
    
    private boolean isAuditField(String fieldName) {
        Set<String> auditFields = Set.of("createdAt", "updatedAt", "created_at", "updated_at");
        return auditFields.contains(fieldName);
    }
    
    private boolean isRequiredField(String fieldName) {
        Set<String> requiredFields = Set.of("name", "title", "email", "username", "firstName", "lastName");
        return requiredFields.contains(fieldName.toLowerCase());
    }
    
    public String getDtoDirectory() {
        return "dto";
    }
}
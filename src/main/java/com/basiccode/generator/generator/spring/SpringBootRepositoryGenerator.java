package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Spring Boot repository generator
 */
public class SpringBootRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("package ").append(packageName).append(".repository;\n\n");
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        code.append("import org.springframework.stereotype.Repository;\n\n");
        
        code.append("@Repository\n");
        code.append("public interface ").append(className).append("Repository extends JpaRepository<").append(className).append(", Long> {\n");
        code.append("    // Custom query methods can be added here\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "repository";
    }
}
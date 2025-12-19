package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de services Spring Boot classique
 */
public class SpringBootServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String serviceName = className + "Service";
        
        // Package
        code.append("package ").append(packageName).append(".service;\n\n");
        
        // Imports
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import ").append(packageName).append(".repository.").append(className).append("Repository;\n");
        code.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        code.append("import org.springframework.stereotype.Service;\n");
        code.append("import org.springframework.transaction.annotation.Transactional;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n\n");
        
        // Annotations de classe
        code.append("@Service\n");
        code.append("@Transactional\n");
        
        // Déclaration de classe
        code.append("public class ").append(serviceName).append(" {\n\n");
        
        // Repository
        code.append("    @Autowired\n");
        code.append("    private ").append(className).append("Repository repository;\n\n");
        
        // Méthodes CRUD
        
        // Create
        code.append("    public ").append(className).append(" create(").append(className).append(" entity) {\n");
        code.append("        return repository.save(entity);\n");
        code.append("    }\n\n");
        
        // Read All
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public List<").append(className).append("> findAll() {\n");
        code.append("        return repository.findAll();\n");
        code.append("    }\n\n");
        
        // Read By ID
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public Optional<").append(className).append("> findById(Long id) {\n");
        code.append("        return repository.findById(id);\n");
        code.append("    }\n\n");
        
        // Update
        code.append("    public ").append(className).append(" update(Long id, ").append(className).append(" entity) {\n");
        code.append("        entity.setId(id);\n");
        code.append("        return repository.save(entity);\n");
        code.append("    }\n\n");
        
        // Delete
        code.append("    public void delete(Long id) {\n");
        code.append("        repository.deleteById(id);\n");
        code.append("    }\n\n");
        
        // Exists
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public boolean exists(Long id) {\n");
        code.append("        return repository.existsById(id);\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getServiceDirectory() {
        return "service";
    }
}
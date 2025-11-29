package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.BusinessMethod;

/**
 * Spring Boot service generator
 */
public class SpringBootServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("package ").append(packageName).append(".service;\n\n");
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import ").append(packageName).append(".repository.").append(className).append("Repository;\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import org.springframework.stereotype.Service;\n");
        code.append("import org.springframework.transaction.annotation.Transactional;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("import ").append(packageName).append(".enums.").append(enumName).append(";\n\n");
        }
        
        code.append("@Service\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("@Transactional\n");
        code.append("public class ").append(className).append("Service {\n\n");
        
        code.append("    private final ").append(className).append("Repository repository;\n\n");
        
        // Generate CRUD methods
        code.append("    public List<").append(className).append("> findAll() {\n");
        code.append("        return repository.findAll();\n");
        code.append("    }\n\n");
        
        code.append("    public Optional<").append(className).append("> findById(Long id) {\n");
        code.append("        return repository.findById(id);\n");
        code.append("    }\n\n");
        
        code.append("    public ").append(className).append(" save(").append(className).append(" entity) {\n");
        code.append("        return repository.save(entity);\n");
        code.append("    }\n\n");
        
        code.append("    public void deleteById(Long id) {\n");
        code.append("        repository.deleteById(id);\n");
        code.append("    }\n\n");
        
        // Add state management methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementMethods(code, className, enhancedClass);
        }
        
        // Generate behavioral methods if available
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("    public ").append(method.getReturnType()).append(" ").append(method.getName()).append("() {\n");
                code.append("        // Generated from sequence diagram\n");
                for (String logic : method.getBusinessLogic()) {
                    code.append("        ").append(logic).append("\n");
                }
                code.append("    }\n\n");
            }
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    @Override
    public String getServiceDirectory() {
        return "service";
    }
    
    private void generateStateManagementMethods(StringBuilder code, String className, EnhancedClass enhancedClass) {
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : className + "Status";
        
        // Suspend method
        code.append("    public ").append(className).append(" suspend").append(className).append("(Long id) {\n");
        code.append("        ").append(className).append(" entity = repository.findById(id)\n");
        code.append("            .orElseThrow(() -> new RuntimeException(\"").append(className).append(" not found with id: \" + id));\n");
        code.append("        entity.suspend();\n");
        code.append("        return repository.save(entity);\n");
        code.append("    }\n\n");
        
        // Activate method
        code.append("    public ").append(className).append(" activate").append(className).append("(Long id) {\n");
        code.append("        ").append(className).append(" entity = repository.findById(id)\n");
        code.append("            .orElseThrow(() -> new RuntimeException(\"").append(className).append(" not found with id: \" + id));\n");
        code.append("        entity.activate();\n");
        code.append("        return repository.save(entity);\n");
        code.append("    }\n\n");
    }
}
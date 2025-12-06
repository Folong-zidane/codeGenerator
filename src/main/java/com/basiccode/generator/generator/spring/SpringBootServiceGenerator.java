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
        code.append("import ").append(packageName).append(".dto.").append(className).append("CreateDto;\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("ReadDto;\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("UpdateDto;\n");
        code.append("import ").append(packageName).append(".exception.EntityNotFoundException;\n");
        code.append("import ").append(packageName).append(".exception.ValidationException;\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.stereotype.Service;\n");
        code.append("import org.springframework.transaction.annotation.Transactional;\n");
        code.append("import org.springframework.data.domain.Page;\n");
        code.append("import org.springframework.data.domain.Pageable;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n");
        code.append("import java.util.stream.Collectors;\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("import ").append(packageName).append(".enums.").append(enumName).append(";\n\n");
        }
        
        code.append("@Service\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("@Transactional\n");
        code.append("@Slf4j\n");
        code.append("public class ").append(className).append("Service {\n\n");
        
        code.append("    private final ").append(className).append("Repository repository;\n\n");
        
        // Generate CRUD methods with DTOs
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public List<").append(className).append("ReadDto> findAll() {\n");
        code.append("        log.debug(\"Finding all ").append(className).append("s\");\n");
        code.append("        return repository.findAll().stream()\n");
        code.append("            .map(this::convertToReadDto)\n");
        code.append("            .collect(Collectors.toList());\n");
        code.append("    }\n\n");
        
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public Optional<").append(className).append("ReadDto> findById(Long id) {\n");
        code.append("        log.debug(\"Finding ").append(className).append(" by id: {}\", id);\n");
        code.append("        return repository.findById(id)\n");
        code.append("            .map(this::convertToReadDto);\n");
        code.append("    }\n\n");
        
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public ").append(className).append("ReadDto getById(Long id) {\n");
        code.append("        log.debug(\"Getting ").append(className).append(" by id: {}\", id);\n");
        code.append("        return repository.findById(id)\n");
        code.append("            .map(this::convertToReadDto)\n");
        code.append("            .orElseThrow(() -> new EntityNotFoundException(\"").append(className).append("\", id));\n");
        code.append("    }\n\n");
        
        code.append("    public ").append(className).append("ReadDto create(").append(className).append("CreateDto createDto) {\n");
        code.append("        log.info(\"Creating new ").append(className).append(": {}\", createDto);\n");
        code.append("        ").append(className).append(" entity = convertToEntity(createDto);\n");
        code.append("        ").append(className).append(" saved = repository.save(entity);\n");
        code.append("        return convertToReadDto(saved);\n");
        code.append("    }\n\n");
        
        code.append("    public ").append(className).append("ReadDto update(Long id, ").append(className).append("UpdateDto updateDto) {\n");
        code.append("        log.info(\"Updating ").append(className).append(" with id: {}\", id);\n");
        code.append("        ").append(className).append(" existing = repository.findById(id)\n");
        code.append("            .orElseThrow(() -> new EntityNotFoundException(\"").append(className).append("\", id));\n");
        code.append("        \n");
        code.append("        updateEntityFromDto(existing, updateDto);\n");
        code.append("        ").append(className).append(" updated = repository.save(existing);\n");
        code.append("        return convertToReadDto(updated);\n");
        code.append("    }\n\n");
        
        code.append("    public void deleteById(Long id) {\n");
        code.append("        log.info(\"Deleting ").append(className).append(" with id: {}\", id);\n");
        code.append("        if (!repository.existsById(id)) {\n");
        code.append("            throw new EntityNotFoundException(\"").append(className).append("\", id);\n");
        code.append("        }\n");
        code.append("        repository.deleteById(id);\n");
        code.append("    }\n\n");
        
        // Add pagination support
        code.append("    @Transactional(readOnly = true)\n");
        code.append("    public Page<").append(className).append("ReadDto> findAll(Pageable pageable) {\n");
        code.append("        log.debug(\"Finding all ").append(className).append(" with pagination\");\n");
        code.append("        return repository.findAll(pageable)\n");
        code.append("            .map(this::convertToReadDto);\n");
        code.append("    }\n\n");
        
        // Add DTO conversion methods
        code.append("    // DTO Conversion Methods\n");
        code.append("    private ").append(className).append("ReadDto convertToReadDto(").append(className).append(" entity) {\n");
        code.append("        // TODO: Implement mapping logic or use MapStruct\n");
        code.append("        return new ").append(className).append("ReadDto();\n");
        code.append("    }\n\n");
        
        code.append("    private ").append(className).append(" convertToEntity(").append(className).append("CreateDto dto) {\n");
        code.append("        // TODO: Implement mapping logic or use MapStruct\n");
        code.append("        return new ").append(className).append("();\n");
        code.append("    }\n\n");
        
        code.append("    private void updateEntityFromDto(").append(className).append(" entity, ").append(className).append("UpdateDto dto) {\n");
        code.append("        // TODO: Implement mapping logic or use MapStruct\n");
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
        code.append("    public ").append(className).append("ReadDto suspend").append(className).append("(Long id) {\n");
        code.append("        log.info(\"Suspending ").append(className).append(" with id: {}\", id);\n");
        code.append("        ").append(className).append(" entity = repository.findById(id)\n");
        code.append("            .orElseThrow(() -> new EntityNotFoundException(\"").append(className).append("\", id));\n");
        code.append("        entity.suspend();\n");
        code.append("        ").append(className).append(" updated = repository.save(entity);\n");
        code.append("        return convertToReadDto(updated);\n");
        code.append("    }\n\n");
        
        // Activate method
        code.append("    public ").append(className).append("ReadDto activate").append(className).append("(Long id) {\n");
        code.append("        log.info(\"Activating ").append(className).append(" with id: {}\", id);\n");
        code.append("        ").append(className).append(" entity = repository.findById(id)\n");
        code.append("            .orElseThrow(() -> new EntityNotFoundException(\"").append(className).append("\", id));\n");
        code.append("        entity.activate();\n");
        code.append("        ").append(className).append(" updated = repository.save(entity);\n");
        code.append("        return convertToReadDto(updated);\n");
        code.append("    }\n\n");
    }
}
package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Spring Boot controller generator
 */
public class SpringBootControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("package ").append(packageName).append(".controller;\n\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("CreateDto;\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("ReadDto;\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("UpdateDto;\n");
        code.append("import ").append(packageName).append(".service.").append(className).append("Service;\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.http.ResponseEntity;\n");
        code.append("import org.springframework.http.HttpStatus;\n");
        code.append("import org.springframework.web.bind.annotation.*;\n");
        code.append("import org.springframework.data.domain.Page;\n");
        code.append("import org.springframework.data.domain.Pageable;\n");
        code.append("import javax.validation.Valid;\n");
        code.append("import java.util.List;\n\n");
        
        code.append("@RestController\n");
        code.append("@RequestMapping(\"/api/v1/").append(className.toLowerCase()).append("s\")\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("@Slf4j\n");
        code.append("public class ").append(className).append("Controller {\n\n");
        
        code.append("    private final ").append(className).append("Service service;\n\n");
        
        // Generate REST endpoints with DTOs
        code.append("    @GetMapping\n");
        code.append("    public ResponseEntity<Page<").append(className).append("ReadDto>> findAll(Pageable pageable) {\n");
        code.append("        log.info(\"GET /api/v1/").append(className.toLowerCase()).append("s - Finding all ").append(className).append("s\");\n");
        code.append("        Page<").append(className).append("ReadDto> result = service.findAll(pageable);\n");
        code.append("        return ResponseEntity.ok(result);\n");
        code.append("    }\n\n");
        
        code.append("    @GetMapping(\"/all\")\n");
        code.append("    public ResponseEntity<List<").append(className).append("ReadDto>> findAllList() {\n");
        code.append("        log.info(\"GET /api/v1/").append(className.toLowerCase()).append("s/all - Finding all ").append(className).append("s as list\");\n");
        code.append("        List<").append(className).append("ReadDto> result = service.findAll();\n");
        code.append("        return ResponseEntity.ok(result);\n");
        code.append("    }\n\n");
        
        code.append("    @GetMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("ReadDto> findById(@PathVariable Long id) {\n");
        code.append("        log.info(\"GET /api/v1/").append(className.toLowerCase()).append("s/{} - Finding ").append(className).append(" by id\", id);\n");
        code.append("        ").append(className).append("ReadDto dto = service.getById(id);\n");
        code.append("        return ResponseEntity.ok(dto);\n");
        code.append("    }\n\n");
        
        code.append("    @PostMapping\n");
        code.append("    public ResponseEntity<").append(className).append("ReadDto> create(@Valid @RequestBody ").append(className).append("CreateDto createDto) {\n");
        code.append("        log.info(\"POST /api/v1/").append(className.toLowerCase()).append("s - Creating new ").append(className).append(": {}\", createDto);\n");
        code.append("        ").append(className).append("ReadDto created = service.create(createDto);\n");
        code.append("        return ResponseEntity.status(HttpStatus.CREATED).body(created);\n");
        code.append("    }\n\n");
        
        code.append("    @PutMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("ReadDto> update(@PathVariable Long id, @Valid @RequestBody ").append(className).append("UpdateDto updateDto) {\n");
        code.append("        log.info(\"PUT /api/v1/").append(className.toLowerCase()).append("s/{} - Updating ").append(className).append(": {}\", id, updateDto);\n");
        code.append("        ").append(className).append("ReadDto updated = service.update(id, updateDto);\n");
        code.append("        return ResponseEntity.ok(updated);\n");
        code.append("    }\n\n");
        
        code.append("    @DeleteMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<Void> delete(@PathVariable Long id) {\n");
        code.append("        log.info(\"DELETE /api/v1/").append(className.toLowerCase()).append("s/{} - Deleting ").append(className).append("\", id);\n");
        code.append("        service.deleteById(id);\n");
        code.append("        return ResponseEntity.noContent().build();\n");
        code.append("    }\n\n");
        
        // Add state management endpoints if stateful
        if (enhancedClass.isStateful()) {
            generateStateEndpoints(code, className);
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    @Override
    public String getControllerDirectory() {
        return "controller";
    }
    
    private void generateStateEndpoints(StringBuilder code, String className) {
        // Suspend endpoint
        code.append("    @PatchMapping(\"/{id}/suspend\")\n");
        code.append("    public ResponseEntity<").append(className).append("ReadDto> suspend(@PathVariable Long id) {\n");
        code.append("        log.info(\"PATCH /api/v1/").append(className.toLowerCase()).append("s/{}/suspend - Suspending ").append(className).append("\", id);\n");
        code.append("        ").append(className).append("ReadDto suspended = service.suspend").append(className).append("(id);\n");
        code.append("        return ResponseEntity.ok(suspended);\n");
        code.append("    }\n\n");
        
        // Activate endpoint
        code.append("    @PatchMapping(\"/{id}/activate\")\n");
        code.append("    public ResponseEntity<").append(className).append("ReadDto> activate(@PathVariable Long id) {\n");
        code.append("        log.info(\"PATCH /api/v1/").append(className.toLowerCase()).append("s/{}/activate - Activating ").append(className).append("\", id);\n");
        code.append("        ").append(className).append("ReadDto activated = service.activate").append(className).append("(id);\n");
        code.append("        return ResponseEntity.ok(activated);\n");
        code.append("    }\n\n");
    }
}
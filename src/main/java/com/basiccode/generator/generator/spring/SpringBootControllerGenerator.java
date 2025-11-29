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
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import ").append(packageName).append(".service.").append(className).append("Service;\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import org.springframework.http.ResponseEntity;\n");
        code.append("import org.springframework.http.HttpStatus;\n");
        code.append("import org.springframework.web.bind.annotation.*;\n");
        code.append("import java.util.List;\n\n");
        
        code.append("@RestController\n");
        code.append("@RequestMapping(\"/api/").append(className.toLowerCase()).append("s\")\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("public class ").append(className).append("Controller {\n\n");
        
        code.append("    private final ").append(className).append("Service service;\n\n");
        
        // Generate REST endpoints
        code.append("    @GetMapping\n");
        code.append("    public List<").append(className).append("> findAll() {\n");
        code.append("        return service.findAll();\n");
        code.append("    }\n\n");
        
        code.append("    @GetMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("> findById(@PathVariable Long id) {\n");
        code.append("        return service.findById(id)\n");
        code.append("            .map(ResponseEntity::ok)\n");
        code.append("            .orElse(ResponseEntity.notFound().build());\n");
        code.append("    }\n\n");
        
        code.append("    @PostMapping\n");
        code.append("    @ResponseStatus(HttpStatus.CREATED)\n");
        code.append("    public ").append(className).append(" create(@RequestBody ").append(className).append(" entity) {\n");
        code.append("        return service.save(entity);\n");
        code.append("    }\n\n");
        
        code.append("    @PutMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("> update(@PathVariable Long id, @RequestBody ").append(className).append(" entity) {\n");
        code.append("        if (service.findById(id).isPresent()) {\n");
        code.append("            return ResponseEntity.ok(service.save(entity));\n");
        code.append("        }\n");
        code.append("        return ResponseEntity.notFound().build();\n");
        code.append("    }\n\n");
        
        code.append("    @DeleteMapping(\"/{id}\")\n");
        code.append("    @ResponseStatus(HttpStatus.NO_CONTENT)\n");
        code.append("    public void delete(@PathVariable Long id) {\n");
        code.append("        service.deleteById(id);\n");
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
        code.append("    public ResponseEntity<").append(className).append("> suspend(@PathVariable Long id) {\n");
        code.append("        return ResponseEntity.ok(service.suspend").append(className).append("(id));\n");
        code.append("    }\n\n");
        
        // Activate endpoint
        code.append("    @PatchMapping(\"/{id}/activate\")\n");
        code.append("    public ResponseEntity<").append(className).append("> activate(@PathVariable Long id) {\n");
        code.append("        return ResponseEntity.ok(service.activate").append(className).append("(id));\n");
        code.append("    }\n\n");
    }
}
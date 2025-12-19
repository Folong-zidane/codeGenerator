package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de contrôleurs Spring Boot classique
 */
public class SpringBootControllerGenerator implements IControllerGenerator {
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getControllerDirectory() {
        return "controller";
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String controllerName = className + "Controller";
        String serviceName = className + "Service";
        String entityVar = className.toLowerCase();
        
        // Package
        code.append("package ").append(packageName).append(".controller;\n\n");
        
        // Imports
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import ").append(packageName).append(".service.").append(serviceName).append(";\n");
        code.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        code.append("import org.springframework.http.ResponseEntity;\n");
        code.append("import org.springframework.web.bind.annotation.*;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n\n");
        
        // Annotations de classe
        code.append("@RestController\n");
        code.append("@RequestMapping(\"/api/").append(entityVar).append("s\")\n");
        code.append("@CrossOrigin(origins = \"*\")\n");
        
        // Déclaration de classe
        code.append("public class ").append(controllerName).append(" {\n\n");
        
        // Service
        code.append("    @Autowired\n");
        code.append("    private ").append(serviceName).append(" service;\n\n");
        
        // GET All
        code.append("    @GetMapping\n");
        code.append("    public ResponseEntity<List<").append(className).append(">> getAll() {\n");
        code.append("        List<").append(className).append("> entities = service.findAll();\n");
        code.append("        return ResponseEntity.ok(entities);\n");
        code.append("    }\n\n");
        
        // GET By ID
        code.append("    @GetMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("> getById(@PathVariable Long id) {\n");
        code.append("        Optional<").append(className).append("> entity = service.findById(id);\n");
        code.append("        return entity.map(ResponseEntity::ok)\n");
        code.append("                     .orElse(ResponseEntity.notFound().build());\n");
        code.append("    }\n\n");
        
        // POST Create
        code.append("    @PostMapping\n");
        code.append("    public ResponseEntity<").append(className).append("> create(@RequestBody ").append(className).append(" ").append(entityVar).append(") {\n");
        code.append("        ").append(className).append(" created = service.create(").append(entityVar).append(");\n");
        code.append("        return ResponseEntity.ok(created);\n");
        code.append("    }\n\n");
        
        // PUT Update
        code.append("    @PutMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<").append(className).append("> update(@PathVariable Long id, @RequestBody ").append(className).append(" ").append(entityVar).append(") {\n");
        code.append("        if (!service.exists(id)) {\n");
        code.append("            return ResponseEntity.notFound().build();\n");
        code.append("        }\n");
        code.append("        ").append(className).append(" updated = service.update(id, ").append(entityVar).append(");\n");
        code.append("        return ResponseEntity.ok(updated);\n");
        code.append("    }\n\n");
        
        // DELETE
        code.append("    @DeleteMapping(\"/{id}\")\n");
        code.append("    public ResponseEntity<Void> delete(@PathVariable Long id) {\n");
        code.append("        if (!service.exists(id)) {\n");
        code.append("            return ResponseEntity.notFound().build();\n");
        code.append("        }\n");
        code.append("        service.delete(id);\n");
        code.append("        return ResponseEntity.noContent().build();\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
}
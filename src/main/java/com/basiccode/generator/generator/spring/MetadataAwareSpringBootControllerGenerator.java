package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMetadataAwareControllerGenerator;
import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.metadata.UMLMetadata;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataAwareSpringBootControllerGenerator implements IMetadataAwareControllerGenerator {
    
    public String generateController(UMLClass umlClass, String packageName, UMLMetadata metadata) {
        System.out.println("Generating metadata-aware Spring Boot controller for class: " + umlClass.getName());
        
        StringBuilder sb = new StringBuilder();
        
        // Package et imports
        sb.append("package ").append(packageName).append(".controller;\n\n");
        sb.append("import ").append(packageName).append(".entity.").append(umlClass.getName()).append(";\n");
        sb.append("import ").append(packageName).append(".service.").append(umlClass.getName()).append("Service;\n");
        sb.append("import lombok.RequiredArgsConstructor;\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\n");
        sb.append("import org.springframework.http.HttpStatus;\n");
        sb.append("import org.springframework.http.ResponseEntity;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import jakarta.validation.Valid;\n");
        sb.append("import java.util.UUID;\n");
        sb.append("import java.util.List;\n\n");
        
        // Classe controller
        sb.append("@Slf4j\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/api/").append(umlClass.getName().toLowerCase()).append("s\")\n");
        sb.append("@RequiredArgsConstructor\n");
        if (shouldAddCrossOrigin(metadata)) {
            sb.append("@CrossOrigin(origins = \"*\")\n");
        }
        sb.append("public class ").append(umlClass.getName()).append("Controller {\n\n");
        
        // Injection du service
        sb.append("    private final ").append(umlClass.getName()).append("Service service;\n\n");
        
        // Méthodes CRUD
        generateCrudEndpoints(sb, umlClass, metadata);
        
        // Endpoints métier basés sur métadonnées
        if (shouldGenerateBusinessEndpoints(metadata)) {
            generateBusinessEndpoints(sb, umlClass, metadata);
        }
        
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private void generateCrudEndpoints(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        String className = umlClass.getName();
        String entityVar = className.toLowerCase();
        
        // POST - Create
        sb.append("    @PostMapping\n");
        sb.append("    public ResponseEntity<").append(className).append("> create(@Valid @RequestBody ")
          .append(className).append(" ").append(entityVar).append(") {\n");
        sb.append("        log.info(\"Creating new ").append(className).append("\");\n");
        if (shouldAddValidation(metadata)) {
            sb.append("        if (!service.validate").append(className).append("(").append(entityVar).append(")) {\n");
            sb.append("            log.warn(\"Validation failed for ").append(className).append("\");\n");
            sb.append("            return ResponseEntity.badRequest().build();\n");
            sb.append("        }\n");
        }
        sb.append("        ").append(className).append(" created = service.create(").append(entityVar).append(");\n");
        sb.append("        return ResponseEntity.status(HttpStatus.CREATED).body(created);\n");
        sb.append("    }\n\n");
        
        // GET - Read by ID
        sb.append("    @GetMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<").append(className).append("> findById(@PathVariable UUID id) {\n");
        sb.append("        log.debug(\"Finding ").append(className).append(" by id: {}\", id);\n");
        sb.append("        return service.findById(id)\n");
        sb.append("                .map(entity -> ResponseEntity.ok().body(entity))\n");
        sb.append("                .orElse(ResponseEntity.notFound().build());\n");
        sb.append("    }\n\n");
        
        // GET - Read all
        sb.append("    @GetMapping\n");
        sb.append("    public ResponseEntity<List<").append(className).append(">> findAll() {\n");
        sb.append("        log.debug(\"Finding all ").append(className).append(" entities\");\n");
        sb.append("        List<").append(className).append("> entities = service.findAll();\n");
        sb.append("        return ResponseEntity.ok(entities);\n");
        sb.append("    }\n\n");
        
        // PUT - Update
        sb.append("    @PutMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<").append(className).append("> update(@PathVariable UUID id, ")
          .append("@Valid @RequestBody ").append(className).append(" ").append(entityVar).append(") {\n");
        sb.append("        log.info(\"Updating ").append(className).append(" with id: {}\", id);\n");
        sb.append("        try {\n");
        sb.append("            ").append(className).append(" updated = service.update(id, ").append(entityVar).append(");\n");
        sb.append("            return ResponseEntity.ok(updated);\n");
        sb.append("        } catch (RuntimeException e) {\n");
        sb.append("            return ResponseEntity.notFound().build();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
        
        // DELETE
        sb.append("    @DeleteMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {\n");
        sb.append("        log.info(\"Deleting ").append(className).append(" with id: {}\", id);\n");
        sb.append("        try {\n");
        sb.append("            service.deleteById(id);\n");
        sb.append("            return ResponseEntity.noContent().build();\n");
        sb.append("        } catch (RuntimeException e) {\n");
        sb.append("            return ResponseEntity.notFound().build();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }
    
    private void generateBusinessEndpoints(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        String className = umlClass.getName();
        
        // Endpoint pour les entités récentes
        sb.append("    @GetMapping(\"/recent\")\n");
        sb.append("    public ResponseEntity<List<").append(className).append(">> findRecent() {\n");
        sb.append("        log.debug(\"Finding recent ").append(className).append(" entities\");\n");
        sb.append("        List<").append(className).append("> entities = service.findRecent();\n");
        sb.append("        return ResponseEntity.ok(entities);\n");
        sb.append("    }\n\n");
        
        // Endpoint de validation
        sb.append("    @PostMapping(\"/validate\")\n");
        sb.append("    public ResponseEntity<Boolean> validate(@RequestBody ").append(className).append(" entity) {\n");
        sb.append("        log.debug(\"Validating ").append(className).append("\");\n");
        sb.append("        boolean isValid = service.validate").append(className).append("(entity);\n");
        sb.append("        return ResponseEntity.ok(isValid);\n");
        sb.append("    }\n\n");
        
        // Endpoint pour compter les entités récentes
        sb.append("    @GetMapping(\"/count/recent\")\n");
        sb.append("    public ResponseEntity<Long> countRecent() {\n");
        sb.append("        log.debug(\"Counting recent ").append(className).append(" entities\");\n");
        sb.append("        Long count = service.countRecent();\n");
        sb.append("        return ResponseEntity.ok(count);\n");
        sb.append("    }\n\n");
        
        // Endpoint de recherche par période
        sb.append("    @GetMapping(\"/search/date-range\")\n");
        sb.append("    public ResponseEntity<List<").append(className).append(">> findByDateRange(\n");
        sb.append("            @RequestParam LocalDateTime start,\n");
        sb.append("            @RequestParam LocalDateTime end) {\n");
        sb.append("        log.debug(\"Finding ").append(className).append(" entities by date range\");\n");
        sb.append("        List<").append(className).append("> entities = service.findByDateRange(start, end);\n");
        sb.append("        return ResponseEntity.ok(entities);\n");
        sb.append("    }\n\n");
    }
    
    private boolean shouldAddCrossOrigin(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private boolean shouldAddValidation(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private boolean shouldGenerateBusinessEndpoints(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    @Override
    public String generateControllerWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateController(umlClass, packageName, (UMLMetadata) metadata);
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateController(umlClass, packageName, null);
    }
    
    @Override
    public String generateControllers(List<UMLClass> classes, String packageName, Object metadata) {
        return classes.stream()
                .map(clazz -> generateController(clazz, packageName, (UMLMetadata) metadata))
                .collect(Collectors.joining("\n\n"));
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getControllerDirectory() {
        return "controller";
    }
    
    @Override
    public String getControllerDirectoryWithMetadata(Object metadata) {
        return "controller";
    }
    
    private UMLClass convertToUMLClass(EnhancedClass enhancedClass) {
        UMLClass umlClass = new UMLClass();
        umlClass.setName(enhancedClass.getName());
        umlClass.setAttributes(enhancedClass.getAttributes());
        umlClass.setMethods(enhancedClass.getMethods());
        return umlClass;
    }
}
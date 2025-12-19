package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMetadataAwareServiceGenerator;
import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.metadata.UMLMetadata;
import com.basiccode.generator.parser.metadata.UMLMetadataParser;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataAwareSpringBootServiceGenerator implements IMetadataAwareServiceGenerator {
    
    private final UMLMetadataParser metadataParser;
    
    public MetadataAwareSpringBootServiceGenerator(UMLMetadataParser metadataParser) {
        this.metadataParser = metadataParser;
    }
    
    public String generateService(UMLClass umlClass, String packageName, UMLMetadata metadata) {
        System.out.println("Generating metadata-aware Spring Boot service for class: " + umlClass.getName());
        
        StringBuilder sb = new StringBuilder();
        
        // Package et imports
        sb.append("package ").append(packageName).append(".service;\n\n");
        sb.append("import ").append(packageName).append(".entity.").append(umlClass.getName()).append(";\n");
        sb.append("import ").append(packageName).append(".repository.").append(umlClass.getName()).append("Repository;\n");
        sb.append("import lombok.RequiredArgsConstructor;\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\n");
        sb.append("import java.util.UUID;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Optional;\n");
        sb.append("import java.time.LocalDateTime;\n\n");
        
        // Classe service
        sb.append("@Slf4j\n");
        sb.append("@Service\n");
        sb.append("@RequiredArgsConstructor\n");
        if (shouldAddTransactional(metadata)) {
            sb.append("@Transactional\n");
        }
        sb.append("public class ").append(umlClass.getName()).append("Service {\n\n");
        
        // Injection du repository
        sb.append("    private final ").append(umlClass.getName()).append("Repository repository;\n\n");
        
        // Méthodes CRUD
        generateCrudMethods(sb, umlClass, metadata);
        
        // Méthodes métier basées sur métadonnées
        if (shouldGenerateBusinessMethods(metadata)) {
            generateBusinessMethods(sb, umlClass, metadata);
        }
        
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private void generateCrudMethods(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        String className = umlClass.getName();
        
        // Create
        sb.append("    public ").append(className).append(" create(").append(className).append(" entity) {\n");
        sb.append("        log.info(\"Creating new ").append(className).append("\");\n");
        // Les timestamps sont gérés automatiquement par @CreationTimestamp et @UpdateTimestamp
        sb.append("        return repository.save(entity);\n");
        sb.append("    }\n\n");
        
        // Read
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public Optional<").append(className).append("> findById(UUID id) {\n");
        sb.append("        log.debug(\"Finding ").append(className).append(" by id: {}\", id);\n");
        sb.append("        return repository.findById(id);\n");
        sb.append("    }\n\n");
        
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public List<").append(className).append("> findAll() {\n");
        sb.append("        log.debug(\"Finding all ").append(className).append(" entities\");\n");
        sb.append("        return repository.findAll();\n");
        sb.append("    }\n\n");
        
        // Update
        sb.append("    public ").append(className).append(" update(UUID id, ").append(className).append(" entity) {\n");
        sb.append("        log.info(\"Updating ").append(className).append(" with id: {}\", id);\n");
        sb.append("        return repository.findById(id)\n");
        sb.append("                .map(existing -> {\n");
        // L'updatedAt est géré automatiquement par @UpdateTimestamp
        sb.append("                    entity.setId(id);\n");
        sb.append("                    return repository.save(entity);\n");
        sb.append("                })\n");
        sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(className).append(" not found with id: \" + id));\n");
        sb.append("    }\n\n");
        
        // Delete
        sb.append("    public void deleteById(UUID id) {\n");
        sb.append("        log.info(\"Deleting ").append(className).append(" with id: {}\", id);\n");
        sb.append("        if (!repository.existsById(id)) {\n");
        sb.append("            throw new RuntimeException(\"").append(className).append(" not found with id: \" + id);\n");
        sb.append("        }\n");
        sb.append("        repository.deleteById(id);\n");
        sb.append("    }\n\n");
    }
    
    private void generateBusinessMethods(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        String className = umlClass.getName();
        
        // Méthode de validation complète
        sb.append("    public boolean validate").append(className).append("(").append(className).append(" entity) {\n");
        sb.append("        log.debug(\"Validating ").append(className).append("\");\n");
        sb.append("        if (entity == null) return false;\n");
        
        for (var attribute : umlClass.getAttributes()) {
            if ("String".equals(attribute.getType())) {
                sb.append("        if (entity.get").append(capitalize(attribute.getName()))
                  .append("() == null || entity.get").append(capitalize(attribute.getName()))
                  .append("().trim().isEmpty()) {\n");
                sb.append("            log.warn(\"Validation failed: ").append(attribute.getName()).append(" is required\");\n");
                sb.append("            return false;\n");
                sb.append("        }\n");
            }
        }
        
        sb.append("        return true;\n");
        sb.append("    }\n\n");
        
        // Méthode de recherche récente
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public List<").append(className).append("> findRecent() {\n");
        sb.append("        log.debug(\"Finding recent ").append(className).append(" entities\");\n");
        sb.append("        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);\n");
        sb.append("        return repository.findRecentEntities(oneWeekAgo);\n");
        sb.append("    }\n\n");
        
        // Méthode de recherche par période
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public List<").append(className).append("> findByDateRange(LocalDateTime start, LocalDateTime end) {\n");
        sb.append("        log.debug(\"Finding ").append(className).append(" entities between {} and {}\", start, end);\n");
        sb.append("        return repository.findByDateRange(start, end);\n");
        sb.append("    }\n\n");
        
        // Méthode de comptage
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public Long countRecent() {\n");
        sb.append("        log.debug(\"Counting recent ").append(className).append(" entities\");\n");
        sb.append("        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);\n");
        sb.append("        return repository.countRecentEntities(oneWeekAgo);\n");
        sb.append("    }\n\n");
    }
    
    private boolean shouldAddTransactional(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private boolean shouldAddTimestamps(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private boolean shouldGenerateBusinessMethods(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    @Override
    public String generateServices(List<UMLClass> classes, String packageName, Object metadata) {
        return classes.stream()
                .map(clazz -> generateService(clazz, packageName, (UMLMetadata) metadata))
                .collect(Collectors.joining("\n\n"));
    }
    
    @Override
    public String generateServiceWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateService(umlClass, packageName, (UMLMetadata) metadata);
    }
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateService(umlClass, packageName, null);
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getServiceDirectory() {
        return "service";
    }
    
    @Override
    public String getServiceDirectoryWithMetadata(Object metadata) {
        return "service";
    }
    
    private UMLClass convertToUMLClass(EnhancedClass enhancedClass) {
        UMLClass umlClass = new UMLClass();
        umlClass.setName(enhancedClass.getName());
        umlClass.setAttributes(enhancedClass.getAttributes());
        umlClass.setMethods(enhancedClass.getMethods());
        return umlClass;
    }
}

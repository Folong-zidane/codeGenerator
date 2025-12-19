package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMetadataAwareRepositoryGenerator;
import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.metadata.UMLMetadata;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataAwareSpringBootRepositoryGenerator implements IMetadataAwareRepositoryGenerator {
    
    public String generateRepository(UMLClass umlClass, String packageName, UMLMetadata metadata) {
        System.out.println("Generating metadata-aware Spring Boot repository for class: " + umlClass.getName());
        
        StringBuilder sb = new StringBuilder();
        
        // Package et imports
        sb.append("package ").append(packageName).append(".repository;\n\n");
        sb.append("import ").append(packageName).append(".entity.").append(umlClass.getName()).append(";\n");
        sb.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        sb.append("import org.springframework.data.jpa.repository.Query;\n");
        sb.append("import org.springframework.data.repository.query.Param;\n");
        sb.append("import org.springframework.stereotype.Repository;\n");
        sb.append("import java.util.UUID;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Optional;\n");
        sb.append("import java.time.LocalDateTime;\n\n");
        
        // Interface repository
        sb.append("@Repository\n");
        sb.append("public interface ").append(umlClass.getName()).append("Repository extends JpaRepository<")
          .append(umlClass.getName()).append(", UUID> {\n\n");
        
        // Méthodes de recherche basées sur les attributs
        generateFinderMethods(sb, umlClass, metadata);
        
        // Méthodes personnalisées avec métadonnées
        if (shouldGenerateCustomMethods(metadata)) {
            generateCustomMethods(sb, umlClass, metadata);
        }
        
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private void generateFinderMethods(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        // Génère des méthodes findBy pour chaque attribut String
        // Méthodes findBy standard pour les attributs String
        umlClass.getAttributes().stream()
                .filter(attr -> "String".equals(attr.getType()))
                .forEach(attr -> {
                    String methodName = "findBy" + capitalize(attr.getName());
                    sb.append("    Optional<").append(umlClass.getName()).append("> ")
                      .append(methodName).append("(String ").append(attr.getName()).append(");\n\n");
                    
                    // Méthode de recherche partielle
                    sb.append("    List<").append(umlClass.getName()).append("> ")
                      .append(methodName).append("Containing(String ").append(attr.getName()).append(");\n\n");
                });
        
        // Méthodes findBy pour les autres types
        umlClass.getAttributes().stream()
                .filter(attr -> !"String".equals(attr.getType()))
                .forEach(attr -> {
                    String methodName = "findBy" + capitalize(attr.getName());
                    String javaType = mapToJavaType(attr.getType());
                    sb.append("    List<").append(umlClass.getName()).append("> ")
                      .append(methodName).append("(").append(javaType).append(" ").append(attr.getName()).append(");\n\n");
                });
    }
    
    private void generateCustomMethods(StringBuilder sb, UMLClass umlClass, UMLMetadata metadata) {
        String className = umlClass.getName();
        
        // Méthode pour trouver les entités récentes
        sb.append("    @Query(\"SELECT e FROM ").append(className)
          .append(" e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC\")\n");
        sb.append("    List<").append(className).append("> findRecentEntities(@Param(\"date\") LocalDateTime date);\n\n");
        
        // Méthode pour compter les entités récentes
        sb.append("    @Query(\"SELECT COUNT(e) FROM ").append(className).append(" e WHERE e.createdAt >= :date\")\n");
        sb.append("    Long countRecentEntities(@Param(\"date\") LocalDateTime date);\n\n");
        
        // Méthode de recherche par période
        sb.append("    @Query(\"SELECT e FROM ").append(className)
          .append(" e WHERE e.updatedAt >= :startDate AND e.updatedAt <= :endDate ORDER BY e.updatedAt DESC\")\n");
        sb.append("    List<").append(className).append("> findByDateRange(@Param(\"startDate\") LocalDateTime startDate, ")
          .append("@Param(\"endDate\") LocalDateTime endDate);\n\n");
        
        // Méthodes de recherche par attributs String
        for (var attribute : umlClass.getAttributes()) {
            if ("String".equals(attribute.getType())) {
                String attrName = capitalize(attribute.getName());
                sb.append("    @Query(\"SELECT e FROM ").append(className)
                  .append(" e WHERE LOWER(e.").append(attribute.getName())
                  .append(") LIKE LOWER(CONCAT('%', :value, '%'))\")\n");
                sb.append("    List<").append(className).append("> findBy").append(attrName)
                  .append("ContainingIgnoreCase(@Param(\"value\") String value);\n\n");
            }
        }
        
        // Méthode pour les entités actives (non supprimées)
        sb.append("    @Query(\"SELECT e FROM ").append(className)
          .append(" e WHERE e.deletedAt IS NULL ORDER BY e.createdAt DESC\")\n");
        sb.append("    List<").append(className).append("> findActiveEntities();\n\n");
    }
    
    private boolean shouldGenerateCustomMethods(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private String mapToJavaType(String umlType) {
        switch (umlType.toLowerCase()) {
            case "string": return "String";
            case "int": case "integer": return "Integer";
            case "long": return "Long";
            case "double": return "Double";
            case "boolean": return "Boolean";
            case "date": case "datetime": case "localdatetime": return "LocalDateTime";
            case "localdate": return "LocalDate";
            case "bigdecimal": return "BigDecimal";
            case "uuid": return "UUID";
            default: return "String";
        }
    }
    
    @Override
    public String generateRepositoryWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateRepository(umlClass, packageName, (UMLMetadata) metadata);
    }
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateRepository(umlClass, packageName, null);
    }
    
    @Override
    public String generateRepositories(List<EnhancedClass> classes, String packageName, Object metadata) {
        return classes.stream()
                .map(clazz -> generateRepository(convertToUMLClass(clazz), packageName, (UMLMetadata) metadata))
                .collect(Collectors.joining("\n\n"));
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "repository";
    }
    
    @Override
    public String getRepositoryDirectoryWithMetadata(Object metadata) {
        return "repository";
    }
    
    private UMLClass convertToUMLClass(EnhancedClass enhancedClass) {
        UMLClass umlClass = new UMLClass();
        umlClass.setName(enhancedClass.getName());
        umlClass.setAttributes(enhancedClass.getAttributes());
        umlClass.setMethods(enhancedClass.getMethods());
        return umlClass;
    }
}
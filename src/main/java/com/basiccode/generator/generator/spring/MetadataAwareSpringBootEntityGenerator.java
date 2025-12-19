package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMetadataAwareEntityGenerator;
import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLAttribute;
import com.basiccode.generator.model.UMLRelationship;
import com.basiccode.generator.model.metadata.UMLMetadata;
import com.basiccode.generator.model.metadata.ClassDiagramMetadata;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MetadataAwareSpringBootEntityGenerator implements IMetadataAwareEntityGenerator {
    
    public String generateEntity(UMLClass umlClass, String packageName, UMLMetadata metadata) {
        return generateEntity(umlClass, packageName, metadata, null);
    }
    
    public String generateEntity(UMLClass umlClass, String packageName, UMLMetadata metadata, List<UMLRelationship> relationships) {
        System.out.println("Generating metadata-aware Spring Boot entity for class: " + umlClass.getName());
        
        StringBuilder sb = new StringBuilder();
        ClassDiagramMetadata classMeta = metadata != null ? metadata.getClassDiagram() : ClassDiagramMetadata.getDefault();
        List<UMLRelationship> classRelationships = getRelationshipsForClass(umlClass.getName(), relationships);
        
        // Package et imports
        sb.append("package ").append(packageName).append(".entity;\n\n");
        sb.append("import jakarta.persistence.*;\n");
        
        // Conditional imports based on metadata
        if (classMeta.isUseLombok()) {
            sb.append("import lombok.Data;\n");
            sb.append("import lombok.NoArgsConstructor;\n");
            sb.append("import lombok.AllArgsConstructor;\n");
        }
        
        // ID type import
        if ("UUID".equals(classMeta.getIdType())) {
            sb.append("import java.util.UUID;\n");
        }
        
        // Audit fields import
        if (classMeta.isAuditFields()) {
            sb.append("import java.time.LocalDateTime;\n");
            sb.append("import java.time.LocalDate;\n");
            sb.append("import java.math.BigDecimal;\n");
            sb.append("import org.hibernate.annotations.CreationTimestamp;\n");
            sb.append("import org.hibernate.annotations.UpdateTimestamp;\n");
        }
        
        // Validation imports
        if (classMeta.isConstraintAnnotations()) {
            sb.append("import jakarta.validation.constraints.*;\n");
        }
        
        // Cache imports
        if (!"none".equals(classMeta.getCacheStrategy())) {
            sb.append("import org.springframework.cache.annotation.Cacheable;\n");
        }
        
        // Soft delete imports
        if (classMeta.isSoftDelete()) {
            sb.append("import org.hibernate.annotations.SQLDelete;\n");
            sb.append("import org.hibernate.annotations.Where;\n");
        }
        
        // Relationship imports
        if (classRelationships != null && !classRelationships.isEmpty()) {
            sb.append("import java.util.List;\n");
            sb.append("import java.util.Set;\n");
            sb.append("import java.util.ArrayList;\n");
            sb.append("import java.util.HashSet;\n");
        }
        
        sb.append("\n");
        
        // Annotations JPA
        sb.append("@Entity\n");
        
        // Table name with metadata
        String tableName = convertNaming(umlClass.getName(), classMeta.getTableNamingConvention());
        if (classMeta.isTablePrefix()) {
            tableName = classMeta.getTablePrefixValue() + tableName;
        }
        
        sb.append("@Table(name = \"").append(tableName).append("\"");
        if (!"public".equals(classMeta.getSchemaName())) {
            sb.append(", schema = \"").append(classMeta.getSchemaName()).append("\"");
        }
        sb.append(")\n");
        
        // Lombok annotations based on metadata
        if (classMeta.isUseLombok()) {
            sb.append(classMeta.getLombokAnnotations()).append("\n");
            sb.append("@NoArgsConstructor\n");
            sb.append("@AllArgsConstructor\n");
        }
        
        // Cache annotation
        if (!"none".equals(classMeta.getCacheStrategy())) {
            sb.append("@Cacheable\n");
        }
        
        // Soft delete annotations
        if (classMeta.isSoftDelete()) {
            String softDeleteCol = convertNaming(classMeta.getSoftDeleteField(), classMeta.getColumnNamingConvention());
            sb.append("@SQLDelete(sql = \"UPDATE ").append(tableName).append(" SET ")
              .append(softDeleteCol).append(" = NOW() WHERE ").append(classMeta.getIdColumnName()).append(" = ?\")\n");
            sb.append("@Where(clause = \"").append(softDeleteCol).append(" IS NULL\")\n");
        }
        
        sb.append("public class ").append(umlClass.getName()).append(" {\n\n");
        
        // ID field with metadata
        sb.append("    @Id\n");
        
        // ID generation strategy based on metadata
        switch (classMeta.getIdGenerationStrategy()) {
            case "auto-increment":
                sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                break;
            case "sequence":
                sb.append("    @GeneratedValue(strategy = GenerationType.SEQUENCE)\n");
                break;
            case "uuid":
            default:
                sb.append("    @GeneratedValue(strategy = GenerationType.UUID)\n");
                break;
        }
        
        sb.append("    private ").append(classMeta.getIdType()).append(" ").append(classMeta.getIdColumnName()).append(";\n\n");
        
        // Attributes
        for (UMLAttribute attr : umlClass.getAttributes()) {
            generateAttribute(sb, attr, classMeta);
        }
        
        // Relationships
        if (classRelationships != null && !classRelationships.isEmpty()) {
            generateRelationships(sb, classRelationships, classMeta, packageName);
        }
        
        // Audit fields based on metadata
        if (classMeta.isAuditFields()) {
            String createdAtCol = convertNaming(classMeta.getCreatedAtField(), classMeta.getColumnNamingConvention());
            String updatedAtCol = convertNaming(classMeta.getUpdatedAtField(), classMeta.getColumnNamingConvention());
            
            sb.append("    @CreationTimestamp\n");
            sb.append("    @Column(name = \"").append(createdAtCol).append("\", updatable = false)\n");
            sb.append("    private LocalDateTime ").append(classMeta.getCreatedAtField()).append(";\n\n");
            
            sb.append("    @UpdateTimestamp\n");
            sb.append("    @Column(name = \"").append(updatedAtCol).append("\")\n");
            sb.append("    private LocalDateTime ").append(classMeta.getUpdatedAtField()).append(";\n\n");
            
            // Version field for optimistic locking
            String versionCol = convertNaming(classMeta.getVersionField(), classMeta.getColumnNamingConvention());
            sb.append("    @Version\n");
            sb.append("    @Column(name = \"").append(versionCol).append("\")\n");
            sb.append("    private Long ").append(classMeta.getVersionField()).append(";\n\n");
        }
        
        // Soft delete field
        if (classMeta.isSoftDelete()) {
            String softDeleteCol = convertNaming(classMeta.getSoftDeleteField(), classMeta.getColumnNamingConvention());
            sb.append("    @Column(name = \"").append(softDeleteCol).append("\")\n");
            sb.append("    private LocalDateTime ").append(classMeta.getSoftDeleteField()).append(";\n\n");
        }
        
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private void generateAttribute(StringBuilder sb, UMLAttribute attr, ClassDiagramMetadata metadata) {
        String javaType = mapToJavaType(attr.getType());
        String columnName = convertNaming(attr.getName(), metadata.getColumnNamingConvention());
        
        // Validation annotations based on metadata
        if (metadata.isConstraintAnnotations()) {
            if (isRequired(attr)) {
                if ("String".equals(javaType)) {
                    sb.append("    @NotBlank\n");
                } else {
                    sb.append("    @NotNull\n");
                }
            }
            
            if ("String".equals(javaType)) {
                sb.append("    @Size(max = 255)\n");
            }
        }
        
        // Column annotation
        sb.append("    @Column(name = \"").append(columnName).append("\"");
        
        if ("String".equals(javaType)) {
            sb.append(", length = 255");
        }
        
        if (isRequired(attr)) {
            sb.append(", nullable = false");
        }
        
        sb.append(")\n");
        
        // Field declaration
        sb.append("    private ").append(javaType).append(" ").append(attr.getName()).append(";\n\n");
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
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private boolean shouldAddAuditFields(UMLMetadata metadata) {
        return metadata != null && metadata.getClassMetadata() != null;
    }
    
    private boolean isRequired(UMLAttribute attr) {
        return attr.getName().toLowerCase().contains("name") || 
               attr.getName().toLowerCase().contains("email") ||
               attr.getName().toLowerCase().contains("username");
    }
    
    private String convertNaming(String input, String convention) {
        switch (convention) {
            case "camelCase":
                return toCamelCase(input);
            case "PascalCase":
                return toPascalCase(input);
            case "kebab-case":
                return toKebabCase(input);
            case "snake_case":
            default:
                return toSnakeCase(input);
        }
    }
    
    private String toCamelCase(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }
    
    private String toPascalCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    private String toKebabCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }
    
    @Override
    public String generateEntityWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateEntity(umlClass, packageName, (UMLMetadata) metadata);
    }
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        UMLClass umlClass = convertToUMLClass(enhancedClass);
        return generateEntity(umlClass, packageName, null);
    }
    
    @Override
    public String generateEntities(List<EnhancedClass> classes, String packageName, Object metadata) {
        return classes.stream()
                .map(clazz -> generateEntity(convertToUMLClass(clazz), packageName, (UMLMetadata) metadata))
                .collect(Collectors.joining("\n\n"));
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        return "";
    }
    
    @Override
    public String generateStateEnumWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata) {
        return "";
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getEntityDirectory() {
        return "entity";
    }
    
    @Override
    public String getEntityDirectoryWithMetadata(Object metadata) {
        return "entity";
    }
    
    private List<UMLRelationship> getRelationshipsForClass(String className, List<UMLRelationship> allRelationships) {
        if (allRelationships == null) return new ArrayList<>();
        
        return allRelationships.stream()
            .filter(rel -> className.equals(rel.getSourceClass()) || className.equals(rel.getTargetClass()))
            .collect(Collectors.toList());
    }
    
    private void generateRelationships(StringBuilder sb, List<UMLRelationship> relationships, ClassDiagramMetadata metadata, String packageName) {
        for (UMLRelationship rel : relationships) {
            generateRelationship(sb, rel, metadata, packageName);
        }
    }
    
    private void generateRelationship(StringBuilder sb, UMLRelationship rel, ClassDiagramMetadata metadata, String packageName) {
        String currentClass = rel.getSourceClass();
        String relatedClass = rel.getTargetClass();
        
        // Determine fetch type
        String fetchType = "lazy".equals(metadata.getFetchTypeDefault()) ? "FetchType.LAZY" : "FetchType.EAGER";
        
        // Determine cascade type
        String cascadeType = getCascadeType(metadata.getCascadeOperations());
        
        if (rel.isOneToMany()) {
            // @OneToMany relationship
            sb.append("    @OneToMany(mappedBy = \"").append(currentClass.toLowerCase()).append("\"");
            if (!"none".equals(cascadeType)) {
                sb.append(", cascade = ").append(cascadeType);
            }
            sb.append(", fetch = ").append(fetchType);
            if (metadata.isOrphanRemoval()) {
                sb.append(", orphanRemoval = true");
            }
            sb.append(")\n");
            
            String fieldName = rel.getTargetName() != null ? rel.getTargetName() : relatedClass.toLowerCase() + "s";
            sb.append("    private List<").append(relatedClass).append("> ").append(fieldName).append(" = new ArrayList<>();\n\n");
            
        } else if (rel.isManyToOne()) {
            // @ManyToOne relationship
            sb.append("    @ManyToOne(fetch = ").append(fetchType);
            if (!"none".equals(cascadeType)) {
                sb.append(", cascade = ").append(cascadeType);
            }
            sb.append(")\n");
            
            // @JoinColumn with foreign key
            String joinColumn = convertNaming(relatedClass.toLowerCase() + "_id", metadata.getColumnNamingConvention());
            sb.append("    @JoinColumn(name = \"").append(joinColumn).append("\"");
            if (metadata.isAutoIndexForeignKeys()) {
                sb.append(", foreignKey = @ForeignKey(name = \"fk_").append(currentClass.toLowerCase())
                  .append("_").append(relatedClass.toLowerCase()).append("\"))");
            }
            sb.append(")\n");
            
            String fieldName = rel.getTargetName() != null ? rel.getTargetName() : relatedClass.toLowerCase();
            sb.append("    private ").append(relatedClass).append(" ").append(fieldName).append(";\n\n");
            
        } else if (rel.isManyToMany()) {
            // @ManyToMany relationship
            sb.append("    @ManyToMany(fetch = ").append(fetchType);
            if (!"none".equals(cascadeType)) {
                sb.append(", cascade = ").append(cascadeType);
            }
            sb.append(")\n");
            
            // @JoinTable for many-to-many
            String joinTable = convertNaming(currentClass.toLowerCase() + "_" + relatedClass.toLowerCase(), metadata.getTableNamingConvention());
            sb.append("    @JoinTable(name = \"").append(joinTable).append("\",\n");
            sb.append("        joinColumns = @JoinColumn(name = \"").append(convertNaming(currentClass.toLowerCase() + "_id", metadata.getColumnNamingConvention())).append("\"),\n");
            sb.append("        inverseJoinColumns = @JoinColumn(name = \"").append(convertNaming(relatedClass.toLowerCase() + "_id", metadata.getColumnNamingConvention())).append("\"))\n");
            
            String fieldName = rel.getTargetName() != null ? rel.getTargetName() : relatedClass.toLowerCase() + "s";
            sb.append("    private Set<").append(relatedClass).append("> ").append(fieldName).append(" = new HashSet<>();\n\n");
        }
    }
    
    private String getCascadeType(String cascadeOperations) {
        switch (cascadeOperations) {
            case "all": return "CascadeType.ALL";
            case "persist": return "CascadeType.PERSIST";
            case "merge": return "CascadeType.MERGE";
            case "remove": return "CascadeType.REMOVE";
            case "refresh": return "CascadeType.REFRESH";
            case "detach": return "CascadeType.DETACH";
            default: return "none";
        }
    }
    
    private UMLClass convertToUMLClass(EnhancedClass enhancedClass) {
        UMLClass umlClass = new UMLClass();
        umlClass.setName(enhancedClass.getName());
        umlClass.setAttributes(enhancedClass.getAttributes());
        umlClass.setMethods(enhancedClass.getMethods());
        return umlClass;
    }
}
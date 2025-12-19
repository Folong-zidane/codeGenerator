package com.basiccode.generator.parser.metadata;

import com.basiccode.generator.model.metadata.*;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class UMLMetadataParserImpl implements UMLMetadataParser {
    
    private static final Pattern PROJECT_BLOCK = Pattern.compile(
        "%% @project\\s*\\n(.*?)%% @end-project", 
        Pattern.DOTALL | Pattern.MULTILINE
    );
    
    private static final Pattern TECH_STACK_BLOCK = Pattern.compile(
        "%% @tech-stack\\s*\\n(.*?)%% @end-tech-stack", 
        Pattern.DOTALL | Pattern.MULTILINE
    );
    
    private static final Pattern ARCHITECTURE_BLOCK = Pattern.compile(
        "%% @project-architecture\\s*\\n(.*?)%% @end-project-architecture", 
        Pattern.DOTALL | Pattern.MULTILINE
    );
    
    private static final Pattern PROPERTY_PATTERN = Pattern.compile(
        "%% ([a-zA-Z-]+):\\s*(.+)"
    );
    
    @Override
    public UMLMetadata parseMetadata(String diagramContent) {
        if (!hasMetadata(diagramContent)) {
            return UMLMetadata.getDefault();
        }
        
        UMLMetadata metadata = UMLMetadata.builder()
            .project(parseProjectMetadata(diagramContent))
            .techStack(parseTechStackMetadata(diagramContent))
            .architecture(parseArchitectureMetadata(diagramContent))
            .build();
        
        // Parse class diagram metadata
        metadata.setClassDiagram(parseClassDiagramMetadata(diagramContent));
        return metadata;
    }
    
    @Override
    public String extractDiagramContent(String diagramContent) {
        String result = diagramContent;
        
        result = PROJECT_BLOCK.matcher(result).replaceAll("");
        result = TECH_STACK_BLOCK.matcher(result).replaceAll("");
        result = ARCHITECTURE_BLOCK.matcher(result).replaceAll("");
        
        result = result.replaceAll("%%.*\\n", "");
        result = result.replaceAll("\\n\\s*\\n\\s*\\n", "\\n\\n");
        
        return result.trim();
    }
    
    @Override
    public boolean hasMetadata(String diagramContent) {
        return diagramContent.contains("%% @project") ||
               diagramContent.contains("%% @tech-stack") ||
               diagramContent.contains("%% @project-architecture");
    }
    
    private ProjectMetadata parseProjectMetadata(String diagramContent) {
        Matcher matcher = PROJECT_BLOCK.matcher(diagramContent);
        if (!matcher.find()) {
            return ProjectMetadata.getDefault();
        }
        
        String block = matcher.group(1);
        ProjectMetadata.ProjectMetadataBuilder builder = ProjectMetadata.builder();
        
        parseProperties(block).forEach((k, v) -> {
            if ("name".equals(k)) {
                builder.name(v);
            } else if ("version".equals(k)) {
                builder.version(v);
            } else if ("description".equals(k)) {
                builder.description(v);
            } else if ("author".equals(k)) {
                builder.author(v);
            }
        });
        
        return builder.build();
    }
    
    private TechStackMetadata parseTechStackMetadata(String diagramContent) {
        Matcher matcher = TECH_STACK_BLOCK.matcher(diagramContent);
        if (!matcher.find()) {
            return TechStackMetadata.getDefault();
        }
        
        String block = matcher.group(1);
        TechStackMetadata.TechStackMetadataBuilder builder = TechStackMetadata.builder();
        
        parseProperties(block).forEach((k, v) -> {
            if ("backendLanguage".equals(k)) {
                builder.backendLanguage(v);
            } else if ("backendFramework".equals(k)) {
                builder.backendFramework(v);
            } else if ("backendVersion".equals(k)) {
                builder.backendVersion(v);
            } else if ("databaseType".equals(k)) {
                builder.databaseType(v);
            } else if ("ormFramework".equals(k)) {
                builder.ormFramework(v);
            } else if ("buildTool".equals(k)) {
                builder.buildTool(v);
            } else if ("javaVersion".equals(k)) {
                builder.javaVersion(v);
            }
        });
        
        return builder.build();
    }
    
    private ArchitectureMetadata parseArchitectureMetadata(String diagramContent) {
        Matcher matcher = ARCHITECTURE_BLOCK.matcher(diagramContent);
        if (!matcher.find()) {
            return ArchitectureMetadata.getDefault();
        }
        
        String block = matcher.group(1);
        ArchitectureMetadata.ArchitectureMetadataBuilder builder = ArchitectureMetadata.builder();
        
        parseProperties(block).forEach((k, v) -> {
            if ("organizationStrategy".equals(k)) {
                builder.organizationStrategy(v);
            } else if ("basePackage".equals(k)) {
                builder.basePackage(v);
            } else if ("moduleDetection".equals(k)) {
                builder.moduleDetection(v);
            } else if ("springStructure".equals(k)) {
                builder.springStructure(v);
            } else if ("sharedModuleName".equals(k)) {
                builder.sharedModuleName(v);
            }
        });
        
        return builder.build();
    }
    
    private Map<String, String> parseProperties(String block) {
        Map<String, String> properties = new HashMap<>();
        
        Matcher matcher = PROPERTY_PATTERN.matcher(block);
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            properties.put(key, value);
        }
        
        return properties;
    }
    
    private ClassDiagramMetadata parseClassDiagramMetadata(String diagramContent) {
        ClassDiagramMetadata metadata = ClassDiagramMetadata.getDefault();
        
        // Parse @class-diagram block
        Pattern classDiagramBlock = Pattern.compile(
            "%% @class-diagram\\s*\\n(.*?)%% @end-class-diagram", 
            Pattern.DOTALL | Pattern.MULTILINE
        );
        
        Matcher matcher = classDiagramBlock.matcher(diagramContent);
        if (matcher.find()) {
            String block = matcher.group(1);
            Map<String, String> properties = parseProperties(block);
            
            // ID Generation
            if (properties.containsKey("id-generation-strategy")) {
                metadata.setIdGenerationStrategy(properties.get("id-generation-strategy"));
            }
            if (properties.containsKey("id-type")) {
                metadata.setIdType(properties.get("id-type"));
            }
            
            // Naming Conventions
            if (properties.containsKey("table-naming-convention")) {
                metadata.setTableNamingConvention(properties.get("table-naming-convention"));
            }
            if (properties.containsKey("column-naming-convention")) {
                metadata.setColumnNamingConvention(properties.get("column-naming-convention"));
            }
            
            // Audit Fields
            if ("true".equals(properties.get("audit-fields"))) {
                metadata.setAuditFields(true);
            }
            if (properties.containsKey("created-at-field")) {
                metadata.setCreatedAtField(properties.get("created-at-field"));
            }
            if (properties.containsKey("updated-at-field")) {
                metadata.setUpdatedAtField(properties.get("updated-at-field"));
            }
            
            // Soft Delete
            if ("true".equals(properties.get("soft-delete"))) {
                metadata.setSoftDelete(true);
            }
            if (properties.containsKey("soft-delete-field")) {
                metadata.setSoftDeleteField(properties.get("soft-delete-field"));
            }
            
            // Validation
            if (properties.containsKey("validation-framework")) {
                metadata.setValidationFramework(properties.get("validation-framework"));
            }
            
            // Lombok
            if ("false".equals(properties.get("use-lombok"))) {
                metadata.setUseLombok(false);
            }
            if (properties.containsKey("lombok-annotations")) {
                metadata.setLombokAnnotations(properties.get("lombok-annotations"));
            }
            
            // Cache
            if (properties.containsKey("cache-strategy")) {
                metadata.setCacheStrategy(properties.get("cache-strategy"));
            }
            
            // Relations
            if ("false".equals(properties.get("bidirectional-mapping"))) {
                metadata.setBidirectionalMapping(false);
            }
            if (properties.containsKey("cascade-operations")) {
                metadata.setCascadeOperations(properties.get("cascade-operations"));
            }
            if (properties.containsKey("fetch-type-default")) {
                metadata.setFetchTypeDefault(properties.get("fetch-type-default"));
            }
            if ("true".equals(properties.get("orphan-removal"))) {
                metadata.setOrphanRemoval(true);
            }
            
            // Inheritance
            if (properties.containsKey("inheritance-strategy")) {
                metadata.setInheritanceStrategy(properties.get("inheritance-strategy"));
            }
            if (properties.containsKey("discriminator-column-name")) {
                metadata.setDiscriminatorColumnName(properties.get("discriminator-column-name"));
            }
            
            // Indices
            if ("false".equals(properties.get("auto-index-foreign-keys"))) {
                metadata.setAutoIndexForeignKeys(false);
            }
            if ("true".equals(properties.get("composite-indices"))) {
                metadata.setCompositeIndices(true);
            }
        }
        
        return metadata;
    }
    
    private List<String> parseList(String value) {
        return Arrays.stream(value.split(","))
            .map(String::trim)
            .collect(Collectors.toList());
    }
}

package com.basiccode.generator.model.metadata;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import java.util.Arrays;

@Data
@Builder
public class ArchitectureMetadata {
    private String organizationStrategy;
    private String basePackage;
    private String moduleDetection;
    private List<String> moduleStrategy;
    private String springStructure;
    private List<String> perFeatureStructure;
    private String sharedModuleName;
    
    public static ArchitectureMetadata getDefault() {
        return ArchitectureMetadata.builder()
            .organizationStrategy("package-by-feature")
            .basePackage("com.example.generated")
            .moduleDetection("auto")
            .moduleStrategy(Arrays.asList("class-hierarchy", "aggregate-root"))
            .springStructure("feature")
            .perFeatureStructure(Arrays.asList("controller", "service", "repository", "entity", "dto"))
            .sharedModuleName("common")
            .build();
    }
}

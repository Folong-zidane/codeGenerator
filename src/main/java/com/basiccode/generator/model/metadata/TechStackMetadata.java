package com.basiccode.generator.model.metadata;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TechStackMetadata {
    private String backendLanguage;
    private String backendFramework;
    private String backendVersion;
    private String databaseType;
    private String ormFramework;
    private String buildTool;
    private String javaVersion;
    
    public static TechStackMetadata getDefault() {
        return TechStackMetadata.builder()
            .backendLanguage("java")
            .backendFramework("spring-boot")
            .backendVersion("3.2.0")
            .databaseType("h2")
            .ormFramework("jpa")
            .buildTool("maven")
            .javaVersion("17")
            .build();
    }
}

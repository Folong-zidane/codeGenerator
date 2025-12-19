package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.parser.metadata.UMLMetadataParser;
import com.basiccode.generator.parser.metadata.UMLMetadataParserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MetadataAwareSpringBootGeneratorFactory {
    
    private final UMLMetadataParser metadataParser;
    
    public MetadataAwareSpringBootGeneratorFactory() {
        this.metadataParser = new UMLMetadataParserImpl();
    }
    
    public IMetadataAwareEntityGenerator createEntityGenerator() {
        log.info("Creating metadata-aware Spring Boot entity generator");
        return new MetadataAwareSpringBootEntityGenerator();
    }
    
    public IMetadataAwareServiceGenerator createServiceGenerator() {
        log.info("Creating metadata-aware Spring Boot service generator");
        return new MetadataAwareSpringBootServiceGenerator(metadataParser);
    }
    
    public IMetadataAwareRepositoryGenerator createRepositoryGenerator() {
        log.info("Creating metadata-aware Spring Boot repository generator");
        return new MetadataAwareSpringBootRepositoryGenerator();
    }
    
    public IMetadataAwareControllerGenerator createControllerGenerator() {
        log.info("Creating metadata-aware Spring Boot controller generator");
        return new MetadataAwareSpringBootControllerGenerator();
    }
    
    public UMLMetadataParser getMetadataParser() {
        return metadataParser;
    }
    
    public boolean supportsMetadata() {
        return true;
    }
    
    public String getFrameworkName() {
        return "Spring Boot (Metadata-Aware)";
    }
}
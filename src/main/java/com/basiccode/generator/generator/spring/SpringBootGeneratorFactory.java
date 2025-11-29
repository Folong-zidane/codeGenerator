package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

/**
 * Spring Boot specific generator factory
 * Implements Abstract Factory pattern for Spring Boot code generation
 */
@Component
public class SpringBootGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new SpringBootEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new SpringBootRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new SpringBootServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new SpringBootControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new SpringBootMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new JavaFileWriter();
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.SPRING_BOOT;
    }
    
    @Override
    public String getLanguage() {
        return "java";
    }
}
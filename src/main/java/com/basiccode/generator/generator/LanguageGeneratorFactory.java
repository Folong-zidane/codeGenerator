package com.basiccode.generator.generator;

import com.basiccode.generator.config.Framework;

/**
 * Abstract Factory for creating language-specific generators
 * Implements Abstract Factory pattern for extensible code generation
 */
public interface LanguageGeneratorFactory {
    
    /**
     * Create entity generator for the target language/framework
     */
    IEntityGenerator createEntityGenerator();
    
    /**
     * Create repository generator
     */
    IRepositoryGenerator createRepositoryGenerator();
    
    /**
     * Create service generator
     */
    IServiceGenerator createServiceGenerator();
    
    /**
     * Create controller generator
     */
    IControllerGenerator createControllerGenerator();
    
    /**
     * Create migration generator (for database schemas)
     */
    IMigrationGenerator createMigrationGenerator();
    
    /**
     * Create file writer for the target language
     */
    IFileWriter createFileWriter();
    
    /**
     * Get supported framework
     */
    Framework getSupportedFramework();
    
    /**
     * Get language identifier
     */
    String getLanguage();
}
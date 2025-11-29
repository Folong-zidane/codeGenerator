package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Interface for entity code generation
 * Implements Strategy pattern for different entity generation approaches
 */
public interface IEntityGenerator {
    
    /**
     * Generate entity class code
     */
    String generateEntity(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Generate state enum if entity is stateful
     */
    String generateStateEnum(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Get file extension for generated entities
     */
    String getFileExtension();
    
    /**
     * Get entity directory path
     */
    String getEntityDirectory();
}
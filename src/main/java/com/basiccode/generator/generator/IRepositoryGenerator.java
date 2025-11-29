package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Interface for repository code generation
 */
public interface IRepositoryGenerator {
    
    /**
     * Generate repository interface/class
     */
    String generateRepository(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Get repository directory path
     */
    String getRepositoryDirectory();
}
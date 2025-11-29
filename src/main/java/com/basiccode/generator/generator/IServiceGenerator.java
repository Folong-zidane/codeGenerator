package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Interface for service code generation
 */
public interface IServiceGenerator {
    
    /**
     * Generate service class with business logic
     */
    String generateService(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Get service directory path
     */
    String getServiceDirectory();
}
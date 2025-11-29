package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Interface for controller code generation
 */
public interface IControllerGenerator {
    
    /**
     * Generate REST controller with endpoints
     */
    String generateController(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Get controller directory path
     */
    String getControllerDirectory();
}
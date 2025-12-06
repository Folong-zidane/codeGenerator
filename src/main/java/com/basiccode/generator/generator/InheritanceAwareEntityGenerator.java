package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Interface for entity generators that support inheritance
 */
public interface InheritanceAwareEntityGenerator extends IEntityGenerator {
    
    /**
     * Check if the class should skip inherited fields
     */
    default boolean shouldSkipInheritedFields(EnhancedClass enhancedClass) {
        return enhancedClass.getOriginalClass().getSuperClass() != null && 
               !enhancedClass.getOriginalClass().getSuperClass().isEmpty();
    }
    
    /**
     * Check if a field is typically inherited from base class
     */
    default boolean isInheritedField(String fieldName) {
        return "id".equalsIgnoreCase(fieldName) || 
               "createdAt".equalsIgnoreCase(fieldName) || 
               "updatedAt".equalsIgnoreCase(fieldName) ||
               "created_at".equalsIgnoreCase(fieldName) ||
               "updated_at".equalsIgnoreCase(fieldName);
    }
    
    /**
     * Generate inheritance declaration for the target language
     */
    String generateInheritanceDeclaration(EnhancedClass enhancedClass, String className);
    
    /**
     * Generate base class/interface if needed
     */
    default String generateBaseClass(EnhancedClass enhancedClass, String packageName) {
        return ""; // Override if needed
    }
}
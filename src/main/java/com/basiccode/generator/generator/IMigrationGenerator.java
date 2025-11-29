package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

/**
 * Interface for database migration generation
 */
public interface IMigrationGenerator {
    
    /**
     * Generate database migration scripts
     */
    String generateMigration(List<EnhancedClass> enhancedClasses, String packageName);
    
    /**
     * Get migration directory path
     */
    String getMigrationDirectory();
}
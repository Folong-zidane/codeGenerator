package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public interface IMigrationGenerator {
    String generateMigration(List<EnhancedClass> enhancedClasses, String packageName);
    String getFileExtension();
}
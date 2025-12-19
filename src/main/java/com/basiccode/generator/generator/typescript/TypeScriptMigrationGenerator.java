package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public class TypeScriptMigrationGenerator implements IMigrationGenerator {
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        return "// TypeScript Migration - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
}
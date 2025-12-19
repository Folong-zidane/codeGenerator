package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public class CSharpMigrationGenerator implements IMigrationGenerator {
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        return "// C# Migration - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
}
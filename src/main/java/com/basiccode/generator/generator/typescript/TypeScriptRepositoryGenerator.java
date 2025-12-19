package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String getRepositoryDirectory() {
        return "repositories";
    }
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        return "// TypeScript Repository - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
}
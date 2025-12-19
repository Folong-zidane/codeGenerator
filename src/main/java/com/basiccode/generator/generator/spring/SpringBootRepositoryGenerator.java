package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class SpringBootRepositoryGenerator implements IRepositoryGenerator {

    @Override
    public String getRepositoryDirectory() {
        return "repository";
    }
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        return "// Spring Boot Repository - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
}
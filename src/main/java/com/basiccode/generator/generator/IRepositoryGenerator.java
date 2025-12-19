package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

public interface IRepositoryGenerator {
    String generateRepository(EnhancedClass enhancedClass, String packageName);
    String getFileExtension();
    String getRepositoryDirectory();
}
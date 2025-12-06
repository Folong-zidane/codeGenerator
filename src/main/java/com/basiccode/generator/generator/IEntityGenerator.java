package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

public interface IEntityGenerator {
    String generateEntity(EnhancedClass enhancedClass, String packageName);
    String generateStateEnum(EnhancedClass enhancedClass, String packageName);
    String getFileExtension();
    String getEntityDirectory();
}
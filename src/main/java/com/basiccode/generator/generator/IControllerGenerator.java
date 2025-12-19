package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

public interface IControllerGenerator {
    String generateController(EnhancedClass enhancedClass, String packageName);
    String getFileExtension();
    String getControllerDirectory();
}
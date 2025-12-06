package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

public interface IServiceGenerator {
    String generateService(EnhancedClass enhancedClass, String packageName);
    String getServiceDirectory();
}
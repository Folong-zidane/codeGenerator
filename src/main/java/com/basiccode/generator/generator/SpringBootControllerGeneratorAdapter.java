package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import com.basiccode.generator.generator.spring.SpringBootControllerGenerator;

/**
 * Adapter for SpringBootControllerGenerator to IControllerGenerator interface
 */
public class SpringBootControllerGeneratorAdapter implements IControllerGenerator {
    private final SpringBootControllerGenerator controllerGenerator = new SpringBootControllerGenerator();
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        return controllerGenerator.generateController(enhancedClass, packageName);
    }
    
    @Override
    public String getControllerDirectory() {
        return controllerGenerator.getControllerDirectory();
    }
}
package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptControllerGenerator implements IControllerGenerator {
    
    @Override
    public String getControllerDirectory() {
        return "controllers";
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        return "// TypeScript Controller - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
}
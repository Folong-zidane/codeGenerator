package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PhpControllerGenerator implements IControllerGenerator {
    
    @Override
    public String getControllerDirectory() {
        return "Controllers";
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        return "<?php // PHP Controller - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".php";
    }
}
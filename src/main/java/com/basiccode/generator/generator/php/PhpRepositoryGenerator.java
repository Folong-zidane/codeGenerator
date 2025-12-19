package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PhpRepositoryGenerator implements IRepositoryGenerator {

    @Override
    public String getRepositoryDirectory() {
        return "Repositories";
    }
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        return "<?php // PHP Repository - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".php";
    }
}
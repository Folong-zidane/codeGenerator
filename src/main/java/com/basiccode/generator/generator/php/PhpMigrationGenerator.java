package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public class PhpMigrationGenerator implements IMigrationGenerator {
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        return "<?php // PHP Migration - TODO: Implement";
    }
    
    @Override
    public String getFileExtension() {
        return ".php";
    }
}
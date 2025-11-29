package com.basiccode.generator.initializer;

import java.nio.file.Path;

public interface ProjectInitializer {
    
    /**
     * Initialize project with latest framework version
     */
    Path initializeProject(String projectName, String packageName);
    
    /**
     * Get supported language
     */
    String getLanguage();
    
    /**
     * Get latest framework version
     */
    String getLatestVersion();
    
    /**
     * Merge generated code with template
     */
    void mergeGeneratedCode(Path templatePath, Path generatedCodePath);
}
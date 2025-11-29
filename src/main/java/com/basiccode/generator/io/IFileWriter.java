package com.basiccode.generator.io;

import com.basiccode.generator.model.ComprehensiveCodeResult;

import java.nio.file.Path;

/**
 * Strategy interface for writing generated code to different outputs
 * Supports multiple output formats (files, zip, templates)
 */
public interface IFileWriter {
    
    /**
     * Write entire project to specified directory
     */
    void writeProject(ComprehensiveCodeResult result, Path outputDir) throws Exception;
    
    /**
     * Write single file with merge strategy
     */
    void writeFile(String fileName, String content, Path outputDir) throws Exception;
    
    /**
     * Get supported file extensions
     */
    String[] getSupportedExtensions();
    
    /**
     * Check if writer supports specific file type
     */
    boolean supports(String fileName);
}
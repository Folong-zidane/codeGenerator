package com.basiccode.generator.generator;

import java.util.Map;

/**
 * Interface for file writing operations
 * Implements Strategy pattern for different file output formats
 */
public interface IFileWriter {
    
    /**
     * Write generated files to output
     */
    void writeFiles(Map<String, String> files, String outputPath);
    
    /**
     * Write single file
     */
    void writeFile(String fileName, String content, String outputPath);
    
    /**
     * Create directory structure
     */
    void createDirectories(String basePath, String... directories);
    
    /**
     * Get supported output format
     */
    String getOutputFormat();
}
package com.basiccode.generator.generator;

import java.util.Map;

public interface IFileWriter {
    void writeFiles(Map<String, String> files, String outputPath);
    String getFileExtension();
    
    default void writeFile(String fileName, String content, String outputPath) {
        // Default implementation - can be overridden
    }
    
    default void createDirectories(String basePath, String... directories) {
        // Default implementation - can be overridden
    }
    
    default String getOutputFormat() {
        return "standard";
    }
}
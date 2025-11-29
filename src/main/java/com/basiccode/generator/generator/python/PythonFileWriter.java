package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IFileWriter;

public class PythonFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(java.util.Map<String, String> files, String outputPath) {
        // Implementation for writing Python files
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        // Implementation for writing single Python file
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        // Implementation for creating Python project directories
    }
    
    @Override
    public String getOutputFormat() {
        return "python";
    }
}
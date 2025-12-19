package com.basiccode.generator.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public abstract class BaseFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        try {
            Path outputDir = Paths.get(outputPath);
            Files.createDirectories(outputDir);
            
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();
                
                Path filePath = outputDir.resolve(fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, content.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write files to " + outputPath, e);
        }
    }
}
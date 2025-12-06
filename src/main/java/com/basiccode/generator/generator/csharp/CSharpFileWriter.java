package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * CSharpFileWriter - Utility for writing C# files with correct extensions
 */
public class CSharpFileWriter implements IFileWriter {
    
    /**
     * Write C# files to the correct directories with .cs extensions
     */
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        try {
            Path basePath = Paths.get(outputPath);
            
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String relativePath = entry.getKey();
                String content = entry.getValue();
                
                // Ensure .cs extension for C# files
                if (relativePath.endsWith(".java")) {
                    relativePath = relativePath.replace(".java", ".cs");
                }
                if (!relativePath.endsWith(".cs") && !relativePath.endsWith(".json") && !relativePath.endsWith(".sql")) {
                    relativePath += ".cs";
                }
                
                Path filePath = basePath.resolve(relativePath);
                Files.createDirectories(filePath.getParent());
                Files.writeString(filePath, content);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write C# files", e);
        }
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            Path basePath = Paths.get(outputPath);
            String correctedFileName = getCorrectExtension(fileName);
            Path filePath = basePath.resolve(correctedFileName);
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write C# file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            Path base = Paths.get(basePath);
            for (String dir : directories) {
                Files.createDirectories(base.resolve(dir));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "csharp";
    }
    
    /**
     * Get correct file extension for C# files
     */
    public String getCorrectExtension(String fileName) {
        if (fileName.endsWith(".java")) {
            return fileName.replace(".java", ".cs");
        }
        if (!fileName.endsWith(".cs") && !fileName.endsWith(".json") && !fileName.endsWith(".sql")) {
            return fileName + ".cs";
        }
        return fileName;
    }
}
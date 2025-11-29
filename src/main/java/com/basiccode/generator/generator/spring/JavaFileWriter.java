package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Java file writer implementation
 */
public class JavaFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            writeFile(entry.getKey(), entry.getValue(), outputPath);
        }
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            Path filePath = Paths.get(outputPath, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        for (String directory : directories) {
            try {
                Path dirPath = Paths.get(basePath, directory);
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + directory, e);
            }
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "filesystem";
    }
}
package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PythonFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(java.util.Map<String, String> files, String outputPath) {
        files.forEach((fileName, content) -> writeFile(fileName, content, outputPath));
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            Path filePath = Paths.get(outputPath, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Python file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            for (String dir : directories) {
                Files.createDirectories(Paths.get(basePath, dir));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "python";
    }
}
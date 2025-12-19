package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IFileWriter;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Générateur de fichiers Django
 */
public class DjangoFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        if (files == null || files.isEmpty()) {
            return;
        }
        
        for (Map.Entry<String, String> entry : files.entrySet()) {
            writeFile(entry.getKey(), entry.getValue(), outputPath);
        }
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            File outputDir = new File(outputPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            File file = new File(outputDir, fileName);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + fileName, e);
        }
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
}
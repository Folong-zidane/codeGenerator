package com.basiccode.generator.io;

import com.basiccode.generator.model.ComprehensiveCodeResult;
import com.basiccode.generator.merge.IMergeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Default implementation of IFileWriter
 * Supports intelligent merging with existing files
 */
@Component
public class DefaultFileWriter implements IFileWriter {
    
    private final List<IMergeStrategy> mergeStrategies;
    
    @Autowired
    public DefaultFileWriter(List<IMergeStrategy> mergeStrategies) {
        this.mergeStrategies = mergeStrategies;
    }
    
    @Override
    public void writeProject(ComprehensiveCodeResult result, Path outputDir) throws Exception {
        // Create output directory if it doesn't exist
        Files.createDirectories(outputDir);
        
        // Write all files
        for (var entry : result.getFiles().entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            writeFile(fileName, content, outputDir);
        }
    }
    
    @Override
    public void writeFile(String fileName, String content, Path outputDir) throws Exception {
        Path filePath = outputDir.resolve(fileName);
        
        // Create parent directories if needed
        Files.createDirectories(filePath.getParent());
        
        if (Files.exists(filePath)) {
            // File exists - try to merge intelligently
            String mergedContent = mergeWithExisting(fileName, content, filePath);
            Files.writeString(filePath, mergedContent, StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            // New file - write directly
            Files.writeString(filePath, content, StandardOpenOption.CREATE);
        }
    }
    
    private String mergeWithExisting(String fileName, String newContent, Path existingFile) throws Exception {
        // Find appropriate merge strategy
        IMergeStrategy strategy = mergeStrategies.stream()
            .filter(s -> s.canHandle(fileName))
            .findFirst()
            .orElse(new OverwriteMergeStrategy()); // Default fallback
        
        IMergeStrategy.MergeResult result = strategy.merge(newContent, existingFile);
        
        if (result.hasChanges()) {
            System.out.println("Merged " + fileName + " with changes: " + String.join(", ", result.getChanges()));
        }
        
        return result.getMergedContent();
    }
    
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{".java", ".py", ".cs", ".ts", ".php", ".md", ".xml", ".json", ".yml"};
    }
    
    @Override
    public boolean supports(String fileName) {
        String extension = getFileExtension(fileName);
        for (String supported : getSupportedExtensions()) {
            if (supported.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
    
    /**
     * Simple overwrite strategy as fallback
     */
    private static class OverwriteMergeStrategy implements IMergeStrategy {
        @Override
        public MergeResult merge(String newContent, Path existingFile) throws Exception {
            return new MergeResult(newContent, true, "Overwritten with new content");
        }
        
        @Override
        public boolean canHandle(String fileName) {
            return true; // Can handle any file as fallback
        }
        
        @Override
        public String getStrategyName() {
            return "Overwrite";
        }
    }
}
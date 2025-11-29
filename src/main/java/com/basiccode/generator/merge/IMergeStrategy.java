package com.basiccode.generator.merge;

import java.nio.file.Path;

/**
 * Strategy interface for merging generated code with existing files
 * Implements Strategy pattern for different merge approaches
 */
public interface IMergeStrategy {
    
    /**
     * Merge new content with existing file
     */
    MergeResult merge(String newContent, Path existingFile) throws Exception;
    
    /**
     * Check if strategy can handle the file type
     */
    boolean canHandle(String fileName);
    
    /**
     * Get strategy name for logging/debugging
     */
    String getStrategyName();
    
    /**
     * Result of merge operation
     */
    class MergeResult {
        private final String mergedContent;
        private final boolean hasChanges;
        private final String[] changes;
        
        public MergeResult(String mergedContent, boolean hasChanges, String... changes) {
            this.mergedContent = mergedContent;
            this.hasChanges = hasChanges;
            this.changes = changes;
        }
        
        public String getMergedContent() { return mergedContent; }
        public boolean hasChanges() { return hasChanges; }
        public String[] getChanges() { return changes; }
    }
}
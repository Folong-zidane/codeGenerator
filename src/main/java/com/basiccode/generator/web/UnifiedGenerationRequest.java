package com.basiccode.generator.web;

/**
 * Unified request model supporting all generation scenarios
 */
public record UnifiedGenerationRequest(
    String classDiagramContent,        // Required for all scenarios
    String sequenceDiagramContent,     // Optional - adds behavioral logic
    String stateDiagramContent,        // Optional - adds state management
    String objectDiagramContent,       // Optional - adds test data
    String componentDiagramContent,    // Optional - adds modular architecture
    String activityDiagramContent,     // Optional - adds workflow engines
    String packageName,
    String language
) {
    public UnifiedGenerationRequest {
        // Default values
        if (language == null || language.isEmpty()) {
            language = "java";
        }
        if (packageName == null || packageName.isEmpty()) {
            packageName = "com.example";
        }
    }
    
    /**
     * Check if this is a behavioral generation request
     */
    public boolean isBehavioral() {
        return hasContent(sequenceDiagramContent);
    }
    
    /**
     * Check if this is a comprehensive generation request
     */
    public boolean isComprehensive() {
        return hasContent(sequenceDiagramContent) && hasContent(stateDiagramContent);
    }
    
    /**
     * Check if this is an ultimate generation request
     */
    public boolean isUltimate() {
        return hasContent(objectDiagramContent) || hasContent(componentDiagramContent);
    }
    
    /**
     * Check if this is a perfect generation request
     */
    public boolean isPerfect() {
        return hasContent(activityDiagramContent);
    }
    
    private boolean hasContent(String content) {
        return content != null && !content.trim().isEmpty();
    }
}
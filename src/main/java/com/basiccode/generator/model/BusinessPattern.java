package com.basiccode.generator.model;

public enum BusinessPattern {
    CALCULATION("calculate", "Performs calculations or computations"),
    VALIDATION("validate", "Validates data or business rules"),
    WORKFLOW("process", "Handles business workflows"),
    FACTORY("generate", "Creates or generates objects"),
    NOTIFICATION("send|notify", "Sends notifications or messages"),
    QUERY("find|get|search", "Retrieves data"),
    COMMAND("create|update|delete|save", "Modifies data"),
    GENERIC(".*", "Generic business method");
    
    private final String pattern;
    private final String description;
    
    BusinessPattern(String pattern, String description) {
        this.pattern = pattern;
        this.description = description;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static BusinessPattern detectPattern(String methodName) {
        String name = methodName.toLowerCase();
        
        for (BusinessPattern pattern : values()) {
            if (pattern != GENERIC && name.matches(".*(" + pattern.getPattern() + ").*")) {
                return pattern;
            }
        }
        
        return GENERIC;
    }
}
package com.basiccode.generator.model;

import lombok.Data;

/**
 * Modèle représentant une relation entre classes
 */
@Data
public class Relationship {
    private String type;
    private String sourceProperty;
    private String targetProperty;
    private int sourceMultiplicity;
    private int targetMultiplicity;
    private boolean cascadeDelete;
    
    public Relationship() {}
    
    public Relationship(String type, String sourceProperty, String targetProperty) {
        this.type = type;
        this.sourceProperty = sourceProperty;
        this.targetProperty = targetProperty;
    }
    
    public boolean getCascadeDelete() { return cascadeDelete; }
    public String getSourceClass() { return sourceProperty; }
    public String getTargetClass() { return targetProperty; }
    public String getFromClass() { return sourceProperty; }
    public String getToClass() { return targetProperty; }
    public int getFromMultiplicity() { return sourceMultiplicity; }
    public int getToMultiplicity() { return targetMultiplicity; }
}
package com.basiccode.generator.model;

import lombok.Data;

/**
 * Représente une relation UML entre deux classes
 */
@Data
public class UmlRelationship {
    private String type; // ONETOMANY, MANYTOMANY, ONETOONE
    private String sourceProperty;
    private String targetProperty;
    private int sourceMultiplicity;
    private int targetMultiplicity;
    private boolean cascadeDelete;
    
    public UmlRelationship() {}
    
    public UmlRelationship(String type, String sourceProperty, String targetProperty) {
        this.type = type;
        this.sourceProperty = sourceProperty;
        this.targetProperty = targetProperty;
    }
    
    public String getSourceName() { return sourceProperty; }
    public String getTargetEntity() { return targetProperty; }
    public String getSourceEntity() { return sourceProperty; }
    public String getTargetClass() { return targetProperty; }
}
package com.basiccode.generator.model;

import java.util.HashMap;
import java.util.Map;

public class UMLRelationship {
    private String sourceClass;
    private String targetClass;
    private String type; // association|composition|aggregation|inheritance|dependency
    private String sourceMultiplicity; // 1|0..1|1..*|*
    private String targetMultiplicity;
    private String sourceName; // role name
    private String targetName; // role name
    private boolean bidirectional = false;
    private Map<String, String> properties = new HashMap<>();
    
    public UMLRelationship() {}
    
    public UMLRelationship(String sourceClass, String targetClass, String type) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.type = type;
    }
    
    public boolean isOneToMany() {
        return "1".equals(sourceMultiplicity) && ("*".equals(targetMultiplicity) || "1..*".equals(targetMultiplicity));
    }
    
    public boolean isManyToOne() {
        return ("*".equals(sourceMultiplicity) || "1..*".equals(sourceMultiplicity)) && "1".equals(targetMultiplicity);
    }
    
    public boolean isManyToMany() {
        return ("*".equals(sourceMultiplicity) || "1..*".equals(sourceMultiplicity)) && 
               ("*".equals(targetMultiplicity) || "1..*".equals(targetMultiplicity));
    }
    
    public boolean isOneToOne() {
        return "1".equals(sourceMultiplicity) && "1".equals(targetMultiplicity);
    }
    
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }
    
    // Getters and setters
    public String getSourceClass() { return sourceClass; }
    public void setSourceClass(String sourceClass) { this.sourceClass = sourceClass; }
    
    public String getTargetClass() { return targetClass; }
    public void setTargetClass(String targetClass) { this.targetClass = targetClass; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getSourceMultiplicity() { return sourceMultiplicity; }
    public void setSourceMultiplicity(String sourceMultiplicity) { this.sourceMultiplicity = sourceMultiplicity; }
    
    public String getTargetMultiplicity() { return targetMultiplicity; }
    public void setTargetMultiplicity(String targetMultiplicity) { this.targetMultiplicity = targetMultiplicity; }
    
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    
    public boolean isBidirectional() { return bidirectional; }
    public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
    
    public Map<String, String> getProperties() { return properties; }
    public void setProperties(Map<String, String> properties) { this.properties = properties; }
}
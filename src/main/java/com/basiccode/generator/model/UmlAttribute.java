package com.basiccode.generator.model;

public class UmlAttribute {
    private String name;
    private String type;
    private Visibility visibility;
    private boolean isRelationship = false;
    private String relationshipType;
    private String targetClass;
    
    public UmlAttribute() {}
    
    public UmlAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    
    public boolean isRelationship() { return isRelationship; }
    public void setRelationship(boolean isRelationship) { this.isRelationship = isRelationship; }
    
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    
    public String getTargetClass() { return targetClass; }
    public void setTargetClass(String targetClass) { this.targetClass = targetClass; }
    
    public boolean isNullable() { return true; }
    public boolean isUnique() { return false; }
}
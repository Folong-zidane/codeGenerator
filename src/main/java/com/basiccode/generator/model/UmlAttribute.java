package com.basiccode.generator.model;

public class UmlAttribute {
    private String name;
    private String type;
    private Visibility visibility;
    
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
}
package com.basiccode.generator.model;

import java.util.List;
import java.util.ArrayList;

public class UmlClass {
    private String name;
    private List<UmlAttribute> attributes = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    
    public UmlClass() {}
    
    public UmlClass(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<UmlAttribute> getAttributes() { return attributes; }
    public void setAttributes(List<UmlAttribute> attributes) { this.attributes = attributes; }
    
    public List<Method> getMethods() { return methods; }
    public void setMethods(List<Method> methods) { this.methods = methods; }
}
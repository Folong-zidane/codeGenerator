package com.basiccode.generator.model;

import java.util.List;
import java.util.ArrayList;

public class UmlClass {
    private String name;
    private List<UmlAttribute> attributes = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private String superClass;
    private boolean isAbstract = false;
    private boolean isInterface = false;
    
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
    
    public String getSuperClass() { return superClass; }
    public void setSuperClass(String superClass) { this.superClass = superClass; }
    
    public boolean isAbstract() { return isAbstract; }
    public void setAbstract(boolean isAbstract) { this.isAbstract = isAbstract; }
    
    public boolean isInterface() { return isInterface; }
    public void setInterface(boolean isInterface) { this.isInterface = isInterface; }
    
    public List<UmlRelationship> getRelationships() { return new ArrayList<>(); }
}
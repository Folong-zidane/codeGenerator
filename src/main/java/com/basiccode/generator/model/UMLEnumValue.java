package com.basiccode.generator.model;

public class UMLEnumValue {
    private String name;
    private String value;
    
    public UMLEnumValue() {}
    
    public UMLEnumValue(String name) {
        this.name = name;
        this.value = name;
    }
    
    public UMLEnumValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
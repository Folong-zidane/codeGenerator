package com.basiccode.generator.model;

import java.util.List;

public class UMLEnum {
    private String name;
    private List<UMLEnumValue> values;
    private String visibility = "public";
    
    public UMLEnum() {}
    
    public UMLEnum(String name) {
        this.name = name;
        this.visibility = "public";
    }
    
    public UMLEnum(String name, List<UMLEnumValue> values, String visibility) {
        this.name = name;
        this.values = values;
        this.visibility = visibility;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<UMLEnumValue> getValues() { return values; }
    public void setValues(List<UMLEnumValue> values) { this.values = values; }
    
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
}
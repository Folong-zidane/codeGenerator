package com.basiccode.generator.model;

import java.util.List;
import java.util.ArrayList;

public class StateEnum {
    private String name;
    private List<StateEnumValue> values = new ArrayList<>();
    
    public StateEnum() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<StateEnumValue> getValues() { return values; }
    public void setValues(List<StateEnumValue> values) { this.values = values; }
    
    public static class StateEnumValue {
        private String name;
        private String description;
        
        public StateEnumValue() {}
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
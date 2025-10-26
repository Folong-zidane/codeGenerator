package com.basiccode.generator.model;

import lombok.Data;

@Data
public class Constraint {
    private String type;
    private Object value;
    
    public Constraint(String type) {
        this.type = type;
    }
    
    public Constraint(String type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    public String getStringValue() {
        return value != null ? value.toString() : null;
    }
    
    public Integer getIntValue() {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
    
    public Boolean getBooleanValue() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }
}
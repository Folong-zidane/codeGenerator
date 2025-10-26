package com.basiccode.generator.model;

import lombok.Data;
import java.util.Map;
import java.util.HashMap;

@Data
public class Annotation {
    private String type;
    private Map<String, Object> parameters = new HashMap<>();
    
    public Annotation(String type) {
        this.type = type;
    }
    
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }
    
    public Object getParameter(String key) {
        return parameters.get(key);
    }
    
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
    
    public String getStringParameter(String key) {
        Object value = parameters.get(key);
        return value != null ? value.toString() : null;
    }
    
    public Integer getIntParameter(String key) {
        Object value = parameters.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
    
    public Boolean getBooleanParameter(String key) {
        Object value = parameters.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }
}
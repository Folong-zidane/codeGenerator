package com.basiccode.generator.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Annotation {
    private String name;
    private Map<String, Object> parameters = new HashMap<>();
    
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
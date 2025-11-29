package com.basiccode.generator.model;

import java.util.*;

public class EnhancedCodeResult {
    private final Map<String, String> files = new HashMap<>();
    private final List<String> warnings = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();
    private final List<BusinessMethod> businessMethods = new ArrayList<>();
    
    public void addFile(String filename, String content) {
        files.put(filename, content);
    }
    
    public Map<String, String> getFiles() { return files; }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() { return warnings; }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Map<String, Object> getMetadata() { return metadata; }
    
    public void addBusinessMethod(BusinessMethod method) {
        businessMethods.add(method);
    }
    
    public List<BusinessMethod> getBusinessMethods() { return businessMethods; }
}
package com.basiccode.generator.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Result of comprehensive code generation
 */
public class ComprehensiveCodeResult {
    
    private Map<String, String> files = new HashMap<>();
    private String packageName;
    private String language;
    
    public Map<String, String> getFiles() {
        return files;
    }
    
    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
    
    public void addFile(String fileName, String content) {
        this.files.put(fileName, content);
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Map<String, String> getGeneratedFiles() {
        return files;
    }
}
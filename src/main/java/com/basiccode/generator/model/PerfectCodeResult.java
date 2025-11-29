package com.basiccode.generator.model;

import java.util.*;

public class PerfectCodeResult {
    private final Map<String, String> files = new HashMap<>();
    private final List<String> warnings = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();
    private final List<String> features = new ArrayList<>();
    private ProjectStructure projectStructure;
    private BuildConfiguration buildConfiguration;
    
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
    
    public void addFeature(String feature) {
        features.add(feature);
    }
    
    public List<String> getFeatures() { return features; }
    
    public ProjectStructure getProjectStructure() { return projectStructure; }
    public void setProjectStructure(ProjectStructure projectStructure) { this.projectStructure = projectStructure; }
    
    public BuildConfiguration getBuildConfiguration() { return buildConfiguration; }
    public void setBuildConfiguration(BuildConfiguration buildConfiguration) { this.buildConfiguration = buildConfiguration; }
}
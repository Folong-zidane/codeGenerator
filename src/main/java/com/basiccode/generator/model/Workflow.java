package com.basiccode.generator.model;

import java.util.*;

public class Workflow {
    private String name;
    private List<WorkflowStep> steps = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<WorkflowStep> getSteps() { return steps; }
    public void setSteps(List<WorkflowStep> steps) { this.steps = steps; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void addMetadata(String key, Object value) { this.metadata.put(key, value); }
}


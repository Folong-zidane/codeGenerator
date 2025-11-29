package com.basiccode.generator.model;

import java.util.*;

public class EnhancedDiagram {
    private Diagram classDiagram;
    private SequenceDiagram sequenceDiagram;
    private List<BusinessMethod> businessMethods = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    
    public Diagram getClassDiagram() { return classDiagram; }
    public void setClassDiagram(Diagram classDiagram) { this.classDiagram = classDiagram; }
    
    public SequenceDiagram getSequenceDiagram() { return sequenceDiagram; }
    public void setSequenceDiagram(SequenceDiagram sequenceDiagram) { this.sequenceDiagram = sequenceDiagram; }
    
    public List<BusinessMethod> getBusinessMethods() { return businessMethods; }
    public void setBusinessMethods(List<BusinessMethod> businessMethods) { this.businessMethods = businessMethods; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void addMetadata(String key, Object value) { this.metadata.put(key, value); }
}
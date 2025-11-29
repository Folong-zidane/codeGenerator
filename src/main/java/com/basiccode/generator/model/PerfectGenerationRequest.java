package com.basiccode.generator.model;

public class PerfectGenerationRequest {
    private String classDiagramContent;
    private String sequenceDiagramContent;
    private String stateDiagramContent;
    private String objectDiagramContent;
    private String componentDiagramContent;
    private String activityDiagramContent;
    private String packageName;
    private String language;
    
    public String getClassDiagramContent() { return classDiagramContent; }
    public void setClassDiagramContent(String classDiagramContent) { this.classDiagramContent = classDiagramContent; }
    
    public String getSequenceDiagramContent() { return sequenceDiagramContent; }
    public void setSequenceDiagramContent(String sequenceDiagramContent) { this.sequenceDiagramContent = sequenceDiagramContent; }
    
    public String getStateDiagramContent() { return stateDiagramContent; }
    public void setStateDiagramContent(String stateDiagramContent) { this.stateDiagramContent = stateDiagramContent; }
    
    public String getObjectDiagramContent() { return objectDiagramContent; }
    public void setObjectDiagramContent(String objectDiagramContent) { this.objectDiagramContent = objectDiagramContent; }
    
    public String getComponentDiagramContent() { return componentDiagramContent; }
    public void setComponentDiagramContent(String componentDiagramContent) { this.componentDiagramContent = componentDiagramContent; }
    
    public String getActivityDiagramContent() { return activityDiagramContent; }
    public void setActivityDiagramContent(String activityDiagramContent) { this.activityDiagramContent = activityDiagramContent; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
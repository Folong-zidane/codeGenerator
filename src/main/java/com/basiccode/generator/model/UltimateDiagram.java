package com.basiccode.generator.model;

import java.util.*;

/**
 * Ultimate model that combines all 5 UML diagrams
 */
public class UltimateDiagram {
    private Diagram classDiagram;
    private SequenceDiagram sequenceDiagram;
    private StateMachine stateMachine;
    private ObjectDiagram objectDiagram;
    private ComponentDiagram componentDiagram;
    private List<UltimateClass> ultimateClasses = new ArrayList<>();
    private ProjectStructure projectStructure;
    private BuildConfiguration buildConfiguration;
    private List<ValidationIssue> validationIssues = new ArrayList<>();
    
    // Getters and setters
    public Diagram getClassDiagram() { return classDiagram; }
    public void setClassDiagram(Diagram classDiagram) { this.classDiagram = classDiagram; }
    
    public SequenceDiagram getSequenceDiagram() { return sequenceDiagram; }
    public void setSequenceDiagram(SequenceDiagram sequenceDiagram) { this.sequenceDiagram = sequenceDiagram; }
    
    public StateMachine getStateMachine() { return stateMachine; }
    public void setStateMachine(StateMachine stateMachine) { this.stateMachine = stateMachine; }
    
    public ObjectDiagram getObjectDiagram() { return objectDiagram; }
    public void setObjectDiagram(ObjectDiagram objectDiagram) { this.objectDiagram = objectDiagram; }
    
    public ComponentDiagram getComponentDiagram() { return componentDiagram; }
    public void setComponentDiagram(ComponentDiagram componentDiagram) { this.componentDiagram = componentDiagram; }
    
    public List<UltimateClass> getUltimateClasses() { return ultimateClasses; }
    public void addUltimateClass(UltimateClass ultimateClass) { this.ultimateClasses.add(ultimateClass); }
    
    public ProjectStructure getProjectStructure() { return projectStructure; }
    public void setProjectStructure(ProjectStructure projectStructure) { this.projectStructure = projectStructure; }
    
    public BuildConfiguration getBuildConfiguration() { return buildConfiguration; }
    public void setBuildConfiguration(BuildConfiguration buildConfiguration) { this.buildConfiguration = buildConfiguration; }
    
    public List<ValidationIssue> getValidationIssues() { return validationIssues; }
    public void setValidationIssues(List<ValidationIssue> validationIssues) { this.validationIssues = validationIssues; }
}

/**
 * Ultimate class that combines all enhancements from all diagrams
 */
class UltimateClass {
    private UmlClass originalClass;
    private List<BusinessMethod> behavioralMethods = new ArrayList<>();
    private StateEnum stateEnum;
    private Map<String, List<StateValidationRule>> stateValidationRules = new HashMap<>();
    private List<StateTransitionMethod> transitionMethods = new ArrayList<>();
    private boolean stateful = false;
    private TestDataGenerator testData;
    private List<String> componentDependencies = new ArrayList<>();
    
    public UltimateClass(UmlClass originalClass) {
        this.originalClass = originalClass;
    }
    
    public UmlClass getOriginalClass() { return originalClass; }
    
    public List<BusinessMethod> getBehavioralMethods() { return behavioralMethods; }
    public void setBehavioralMethods(List<BusinessMethod> behavioralMethods) { 
        this.behavioralMethods = behavioralMethods; 
    }
    
    public StateEnum getStateEnum() { return stateEnum; }
    public void setStateEnum(StateEnum stateEnum) { this.stateEnum = stateEnum; }
    
    public Map<String, List<StateValidationRule>> getStateValidationRules() { 
        return stateValidationRules; 
    }
    public void setStateValidationRules(Map<String, List<StateValidationRule>> stateValidationRules) { 
        this.stateValidationRules = stateValidationRules; 
    }
    
    public List<StateTransitionMethod> getTransitionMethods() { return transitionMethods; }
    public void setTransitionMethods(List<StateTransitionMethod> transitionMethods) { 
        this.transitionMethods = transitionMethods; 
    }
    
    public boolean isStateful() { return stateful; }
    public void setStateful(boolean stateful) { this.stateful = stateful; }
    
    public TestDataGenerator getTestData() { return testData; }
    public void setTestData(TestDataGenerator testData) { this.testData = testData; }
    
    public List<String> getComponentDependencies() { return componentDependencies; }
    public void setComponentDependencies(List<String> componentDependencies) { 
        this.componentDependencies = componentDependencies; 
    }
}

/**
 * Request model for ultimate generation
 */
record UltimateGenerationRequest(
    String classDiagramContent,
    String sequenceDiagramContent,
    String stateDiagramContent,
    String objectDiagramContent,
    String componentDiagramContent,
    String packageName,
    String language
) {
    public UltimateGenerationRequest {
        if (language == null || language.isEmpty()) {
            language = "java";
        }
        if (packageName == null || packageName.isEmpty()) {
            packageName = "com.example";
        }
    }
}

/**
 * Result container for ultimate code generation
 */
class UltimateCodeResult {
    private final Map<String, String> files = new HashMap<>();
    private final List<String> warnings = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();
    private final List<String> features = new ArrayList<>();
    private final List<ValidationIssue> validationIssues = new ArrayList<>();
    
    public void addFile(String filename, String content) {
        files.put(filename, content);
    }
    
    public Map<String, String> getFiles() {
        return files;
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void addFeature(String feature) {
        features.add(feature);
    }
    
    public List<String> getFeatures() {
        return features;
    }
    
    public void addValidationIssue(ValidationIssue issue) {
        validationIssues.add(issue);
    }
    
    public List<ValidationIssue> getValidationIssues() {
        return validationIssues;
    }
}
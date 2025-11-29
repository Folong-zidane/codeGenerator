package com.basiccode.generator.model;

import java.util.*;

/**
 * Perfect model that combines all 6 UML diagrams for absolute completeness
 */
public class PerfectDiagram {
    private Diagram classDiagram;
    private SequenceDiagram sequenceDiagram;
    private StateMachine stateMachine;
    private ObjectDiagram objectDiagram;
    private ComponentDiagram componentDiagram;
    private ActivityDiagram activityDiagram;
    private List<PerfectClass> perfectClasses = new ArrayList<>();
    private ProjectStructure projectStructure;
    private BuildConfiguration buildConfiguration;
    private List<BusinessWorkflow> workflows = new ArrayList<>();
    
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
    
    public ActivityDiagram getActivityDiagram() { return activityDiagram; }
    public void setActivityDiagram(ActivityDiagram activityDiagram) { this.activityDiagram = activityDiagram; }
    
    public List<PerfectClass> getPerfectClasses() { return perfectClasses; }
    public void addPerfectClass(PerfectClass perfectClass) { this.perfectClasses.add(perfectClass); }
    
    public ProjectStructure getProjectStructure() { return projectStructure; }
    public void setProjectStructure(ProjectStructure projectStructure) { this.projectStructure = projectStructure; }
    
    public BuildConfiguration getBuildConfiguration() { return buildConfiguration; }
    public void setBuildConfiguration(BuildConfiguration buildConfiguration) { this.buildConfiguration = buildConfiguration; }
    
    public List<BusinessWorkflow> getWorkflows() { return workflows; }
    public void setWorkflows(List<BusinessWorkflow> workflows) { this.workflows = workflows; }
}






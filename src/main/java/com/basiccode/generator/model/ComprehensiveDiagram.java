package com.basiccode.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive diagram combining multiple UML diagrams
 */
public class ComprehensiveDiagram {
    
    private Diagram classDiagram;
    private SequenceDiagram sequenceDiagram;
    private StateMachine stateMachine;
    private List<EnhancedClass> enhancedClasses = new ArrayList<>();
    
    public Diagram getClassDiagram() {
        return classDiagram;
    }
    
    public void setClassDiagram(Diagram classDiagram) {
        this.classDiagram = classDiagram;
    }
    
    public SequenceDiagram getSequenceDiagram() {
        return sequenceDiagram;
    }
    
    public void setSequenceDiagram(SequenceDiagram sequenceDiagram) {
        this.sequenceDiagram = sequenceDiagram;
    }
    
    public StateMachine getStateMachine() {
        return stateMachine;
    }
    
    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
    
    public List<EnhancedClass> getEnhancedClasses() {
        return enhancedClasses;
    }
    
    public void setEnhancedClasses(List<EnhancedClass> enhancedClasses) {
        this.enhancedClasses = enhancedClasses;
    }
    
    public void addEnhancedClass(EnhancedClass enhancedClass) {
        this.enhancedClasses.add(enhancedClass);
    }
}
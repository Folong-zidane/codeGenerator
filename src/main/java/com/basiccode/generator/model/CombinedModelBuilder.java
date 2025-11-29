package com.basiccode.generator.model;

import com.basiccode.generator.service.BehaviorExtractor;
import com.basiccode.generator.service.StateEnhancer;
import java.util.List;
import java.util.Map;

/**
 * Builder for creating comprehensive diagrams
 * Implements Builder pattern for complex object construction
 */
public class CombinedModelBuilder {
    
    private Diagram classDiagram;
    private SequenceDiagram sequenceDiagram;
    private StateMachine stateMachine;
    private BehaviorExtractor behaviorExtractor;
    private StateEnhancer stateEnhancer;
    
    public CombinedModelBuilder withClassDiagram(Diagram classDiagram) {
        this.classDiagram = classDiagram;
        return this;
    }
    
    public CombinedModelBuilder withSequenceDiagram(SequenceDiagram sequenceDiagram) {
        this.sequenceDiagram = sequenceDiagram;
        return this;
    }
    
    public CombinedModelBuilder withStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
        return this;
    }
    
    public CombinedModelBuilder withBehaviorExtractor(BehaviorExtractor behaviorExtractor) {
        this.behaviorExtractor = behaviorExtractor;
        return this;
    }
    
    public CombinedModelBuilder withStateEnhancer(StateEnhancer stateEnhancer) {
        this.stateEnhancer = stateEnhancer;
        return this;
    }
    
    /**
     * Build comprehensive diagram with all enhancements
     */
    public ComprehensiveDiagram build() {
        validateInputs();
        
        ComprehensiveDiagram comprehensive = new ComprehensiveDiagram();
        comprehensive.setClassDiagram(classDiagram);
        comprehensive.setSequenceDiagram(sequenceDiagram);
        comprehensive.setStateMachine(stateMachine);
        
        // Extract behavioral information
        Map<String, List<BusinessMethod>> businessLogic = null;
        if (sequenceDiagram != null && behaviorExtractor != null) {
            businessLogic = behaviorExtractor.extractBusinessLogic(sequenceDiagram);
        }
        
        // Extract state information
        Map<String, List<StateValidationRule>> stateRules = null;
        List<StateTransitionMethod> transitionMethods = null;
        if (stateMachine != null && stateEnhancer != null) {
            stateRules = stateEnhancer.extractValidationRules(stateMachine);
            transitionMethods = stateEnhancer.generateTransitionMethods(stateMachine);
        }
        
        // Enhance classes - convert ClassModel to UmlClass
        for (ClassModel classModel : classDiagram.getClasses()) {
            // Convert ClassModel to UmlClass with all attributes
            UmlClass clazz = convertToUmlClass(classModel);
            EnhancedClass enhancedClass = new EnhancedClass(clazz);
            
            // Add behavioral methods
            if (businessLogic != null) {
                List<BusinessMethod> methods = businessLogic.get(clazz.getName());
                if (methods != null) {
                    enhancedClass.setBehavioralMethods(methods);
                }
            }
            
            // Add state management
            if (stateMachine != null && stateEnhancer != null && 
                stateEnhancer.shouldHaveStateManagement(clazz, stateMachine)) {
                
                StateEnum stateEnum = stateEnhancer.generateStateEnum(stateMachine, clazz.getName());
                enhancedClass.setStateEnum(stateEnum);
                enhancedClass.setStateValidationRules(stateRules);
                enhancedClass.setTransitionMethods(transitionMethods);
                enhancedClass.setStateful(true);
            }
            
            comprehensive.addEnhancedClass(enhancedClass);
        }
        
        return comprehensive;
    }
    
    private void validateInputs() {
        if (classDiagram == null) {
            throw new IllegalStateException("Class diagram is required");
        }
    }
    
    /**
     * Convert ClassModel to UmlClass with all attributes
     */
    private UmlClass convertToUmlClass(ClassModel classModel) {
        UmlClass umlClass = new UmlClass(classModel.getName());
        
        // Convert fields to UmlAttributes
        java.util.List<UmlAttribute> attributes = new java.util.ArrayList<>();
        for (Field field : classModel.getFields()) {
            UmlAttribute attr = new UmlAttribute();
            attr.setName(field.getName());
            attr.setType(field.getType());
            attr.setVisibility(field.getVisibility());
            attributes.add(attr);
        }
        umlClass.setAttributes(attributes);
        
        // Convert methods
        java.util.List<Method> methods = new java.util.ArrayList<>();
        for (Method method : classModel.getMethods()) {
            methods.add(method);
        }
        umlClass.setMethods(methods);
        
        return umlClass;
    }
}
package com.basiccode.generator.model;

import java.util.List;
import java.util.Map;

/**
 * Enhanced class with behavioral and state information
 */
public class EnhancedClass {
    
    private UmlClass originalClass;
    private List<BusinessMethod> behavioralMethods;
    private StateEnum stateEnum;
    private Map<String, List<StateValidationRule>> stateValidationRules;
    private List<StateTransitionMethod> transitionMethods;
    private boolean stateful = false;
    
    public EnhancedClass(UmlClass originalClass) {
        this.originalClass = originalClass;
    }
    
    public UmlClass getOriginalClass() {
        return originalClass;
    }
    
    public void setOriginalClass(UmlClass originalClass) {
        this.originalClass = originalClass;
    }
    
    public List<BusinessMethod> getBehavioralMethods() {
        return behavioralMethods;
    }
    
    public void setBehavioralMethods(List<BusinessMethod> behavioralMethods) {
        this.behavioralMethods = behavioralMethods;
    }
    
    public StateEnum getStateEnum() {
        return stateEnum;
    }
    
    public void setStateEnum(StateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }
    
    public Map<String, List<StateValidationRule>> getStateValidationRules() {
        return stateValidationRules;
    }
    
    public void setStateValidationRules(Map<String, List<StateValidationRule>> stateValidationRules) {
        this.stateValidationRules = stateValidationRules;
    }
    
    public List<StateTransitionMethod> getTransitionMethods() {
        return transitionMethods;
    }
    
    public void setTransitionMethods(List<StateTransitionMethod> transitionMethods) {
        this.transitionMethods = transitionMethods;
    }
    
    public boolean isStateful() {
        return stateful;
    }
    
    public void setStateful(boolean stateful) {
        this.stateful = stateful;
    }
}
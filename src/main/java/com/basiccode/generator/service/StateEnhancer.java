package com.basiccode.generator.service;

import com.basiccode.generator.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service responsible for enhancing entities with state management
 * Implements Single Responsibility Principle
 */
@Service
public class StateEnhancer {
    
    /**
     * Extract validation rules from state machine
     */
    public Map<String, List<StateValidationRule>> extractValidationRules(StateMachine stateMachine) {
        Map<String, List<StateValidationRule>> rules = new HashMap<>();
        
        for (StateTransition transition : stateMachine.getTransitions()) {
            String entityName = extractEntityName(transition);
            
            StateValidationRule rule = new StateValidationRule();
            rule.setFromState(transition.getFromState());
            rule.setToState(transition.getToState());
            rule.setTrigger(transition.getTrigger());
            // Note: Guard and Action methods not available in StateTransition model
            
            rules.computeIfAbsent(entityName, k -> new ArrayList<>()).add(rule);
        }
        
        return rules;
    }
    
    /**
     * Generate state transition methods
     */
    public List<StateTransitionMethod> generateTransitionMethods(StateMachine stateMachine) {
        Map<String, List<StateTransition>> transitionsByTrigger = new HashMap<>();
        
        // Group transitions by trigger
        for (StateTransition transition : stateMachine.getTransitions()) {
            String trigger = transition.getTrigger();
            if (trigger != null && !trigger.isEmpty()) {
                transitionsByTrigger.computeIfAbsent(trigger, k -> new ArrayList<>()).add(transition);
            }
        }
        
        List<StateTransitionMethod> methods = new ArrayList<>();
        
        for (Map.Entry<String, List<StateTransition>> entry : transitionsByTrigger.entrySet()) {
            StateTransitionMethod method = new StateTransitionMethod();
            method.setName(generateMethodName(entry.getKey()));
            method.setTrigger(entry.getKey());
            method.setTransitions(entry.getValue());
            
            methods.add(method);
        }
        
        return methods;
    }
    
    /**
     * Generate state enum from state machine
     */
    public StateEnum generateStateEnum(StateMachine stateMachine, String entityName) {
        StateEnum stateEnum = new StateEnum();
        stateEnum.setName(entityName + "Status");
        
        Set<String> stateNames = new HashSet<>();
        
        // Collect all states
        for (State state : stateMachine.getStates()) {
            stateNames.add(state.getName());
        }
        
        // Add states from transitions
        for (StateTransition transition : stateMachine.getTransitions()) {
            stateNames.add(transition.getFromState());
            stateNames.add(transition.getToState());
        }
        
        List<StateEnum.StateEnumValue> values = new ArrayList<>();
        for (String stateName : stateNames) {
            if (!stateName.equals("[*]")) { // Skip initial/final pseudo-states
                StateEnum.StateEnumValue value = new StateEnum.StateEnumValue();
                value.setName(stateName.toUpperCase());
                value.setDescription(formatStateDescription(stateName));
                values.add(value);
            }
        }
        
        stateEnum.setValues(values);
        return stateEnum;
    }
    
    /**
     * Check if entity should have state management
     */
    public boolean shouldHaveStateManagement(UmlClass clazz, StateMachine stateMachine) {
        String className = clazz.getName().toLowerCase();
        
        // Check for status/state attributes
        boolean hasStatusField = clazz.getAttributes().stream()
            .anyMatch(attr -> attr.getName().toLowerCase().contains("status") || 
                            attr.getName().toLowerCase().contains("state"));
        
        // Check if state machine is related to this class
        boolean hasRelatedStates = stateMachine.getStates().stream()
            .anyMatch(state -> state.getName().toLowerCase().contains(className));
        
        return hasStatusField || hasRelatedStates || !stateMachine.getStates().isEmpty();
    }
    
    /**
     * Extract entity name from transition context
     */
    private String extractEntityName(StateTransition transition) {
        // Try to infer from state names or use default
        String fromState = transition.getFromState();
        if (fromState != null && fromState.contains("_")) {
            return fromState.split("_")[0];
        }
        return "Entity";
    }
    
    /**
     * Generate method name from trigger
     */
    private String generateMethodName(String trigger) {
        if (trigger == null || trigger.isEmpty()) {
            return "changeState";
        }
        
        // Convert trigger to camelCase method name
        String[] words = trigger.toLowerCase().split("[\\s_-]+");
        StringBuilder methodName = new StringBuilder(words[0]);
        
        for (int i = 1; i < words.length; i++) {
            methodName.append(Character.toUpperCase(words[i].charAt(0)))
                     .append(words[i].substring(1));
        }
        
        return methodName.toString();
    }
    
    /**
     * Format state description for enum
     */
    private String formatStateDescription(String stateName) {
        return stateName.substring(0, 1).toUpperCase() + 
               stateName.substring(1).toLowerCase().replace("_", " ");
    }
}
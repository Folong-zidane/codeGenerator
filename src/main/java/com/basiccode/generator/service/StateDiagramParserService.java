package com.basiccode.generator.service;

import com.basiccode.generator.model.StateMachine;
import com.basiccode.generator.model.StateTransition;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StateDiagramParserService {
    
    private static final Pattern STATE_PATTERN = Pattern.compile("\\s*([A-Z_]+)\\s*-->\\s*([A-Z_]+)\\s*:\\s*(.+)");
    private static final Pattern INITIAL_STATE_PATTERN = Pattern.compile("\\s*\\[\\*\\]\\s*-->\\s*([A-Z_]+)");
    private static final Pattern FINAL_STATE_PATTERN = Pattern.compile("\\s*([A-Z_]+)\\s*-->\\s*\\[\\*\\]");
    
    public StateMachine parseStateDiagram(String content) {
        StateMachine stateMachine = new StateMachine();
        stateMachine.setName("DefaultStateMachine");
        
        Set<String> stateNames = new HashSet<>();
        List<StateTransition> transitions = new ArrayList<>();
        
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            
            // Parse initial state
            Matcher initialMatcher = INITIAL_STATE_PATTERN.matcher(line);
            if (initialMatcher.matches()) {
                String initialState = initialMatcher.group(1);
                stateMachine.setInitialState(initialState);
                stateNames.add(initialState);
                continue;
            }
            
            // Parse final state
            Matcher finalMatcher = FINAL_STATE_PATTERN.matcher(line);
            if (finalMatcher.matches()) {
                String finalState = finalMatcher.group(1);
                stateNames.add(finalState);
                continue;
            }
            
            // Parse state transitions
            Matcher stateMatcher = STATE_PATTERN.matcher(line);
            if (stateMatcher.matches()) {
                String fromState = stateMatcher.group(1);
                String toState = stateMatcher.group(2);
                String trigger = stateMatcher.group(3);
                
                stateNames.add(fromState);
                stateNames.add(toState);
                
                StateTransition transition = new StateTransition();
                transition.setFromState(fromState);
                transition.setToState(toState);
                transition.setTrigger(trigger);
                
                transitions.add(transition);
            }
        }
        
        // Create State objects
        List<com.basiccode.generator.model.State> states = new ArrayList<>();
        for (String stateName : stateNames) {
            com.basiccode.generator.model.State state = new com.basiccode.generator.model.State();
            state.setName(stateName);
            state.setInitial(stateName.equals(stateMachine.getInitialState()));
            states.add(state);
        }
        
        stateMachine.setStates(states);
        stateMachine.setTransitions(transitions);
        
        return stateMachine;
    }
    
    public boolean isValidStateDiagram(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        return content.contains("stateDiagram") || 
               content.contains("[*]") || 
               content.contains("-->");
    }
    
    public List<String> extractStates(String content) {
        Set<String> states = new HashSet<>();
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            Matcher matcher = STATE_PATTERN.matcher(line);
            if (matcher.matches()) {
                states.add(matcher.group(1));
                states.add(matcher.group(2));
            }
        }
        
        return new ArrayList<>(states);
    }
    
    public List<StateTransition> extractTransitions(String content) {
        List<StateTransition> transitions = new ArrayList<>();
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            Matcher matcher = STATE_PATTERN.matcher(line);
            if (matcher.matches()) {
                StateTransition transition = new StateTransition();
                transition.setFromState(matcher.group(1));
                transition.setToState(matcher.group(2));
                transition.setTrigger(matcher.group(3));
                transitions.add(transition);
            }
        }
        
        return transitions;
    }
}
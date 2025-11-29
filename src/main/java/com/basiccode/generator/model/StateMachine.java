package com.basiccode.generator.model;

import java.util.*;

public class StateMachine {
    private String name;
    private List<State> states = new ArrayList<>();
    private List<StateTransition> transitions = new ArrayList<>();
    private String initialState;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<State> getStates() { return states; }
    public void setStates(List<State> states) { this.states = states; }
    
    public List<StateTransition> getTransitions() { return transitions; }
    public void setTransitions(List<StateTransition> transitions) { this.transitions = transitions; }
    
    public String getInitialState() { return initialState; }
    public void setInitialState(String initialState) { this.initialState = initialState; }
}


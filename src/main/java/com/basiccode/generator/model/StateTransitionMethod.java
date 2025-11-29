package com.basiccode.generator.model;

import java.util.List;
import java.util.ArrayList;

public class StateTransitionMethod {
    private String name;
    private String trigger;
    private List<StateTransition> transitions = new ArrayList<>();
    
    public StateTransitionMethod() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getTrigger() { return trigger; }
    public void setTrigger(String trigger) { this.trigger = trigger; }
    
    public List<StateTransition> getTransitions() { return transitions; }
    public void setTransitions(List<StateTransition> transitions) { this.transitions = transitions; }
}
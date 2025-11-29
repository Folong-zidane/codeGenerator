package com.basiccode.generator.model;

public class StateTransition {
    private String fromState;
    private String toState;
    private String trigger;
    private String condition;
    private String action;
    
    public StateTransition() {}
    
    public String getFromState() { return fromState; }
    public void setFromState(String fromState) { this.fromState = fromState; }
    
    public String getToState() { return toState; }
    public void setToState(String toState) { this.toState = toState; }
    
    public String getTrigger() { return trigger; }
    public void setTrigger(String trigger) { this.trigger = trigger; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
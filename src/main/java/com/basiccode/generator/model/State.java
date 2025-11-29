package com.basiccode.generator.model;

public class State {
    private String name;
    private boolean isInitial;
    private boolean isFinal;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isInitial() { return isInitial; }
    public void setInitial(boolean initial) { isInitial = initial; }
    
    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean aFinal) { isFinal = aFinal; }
}
package com.basiccode.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une méthode
 */
public class Method {
    private String name;
    private String returnType;
    private Visibility visibility = Visibility.PUBLIC;
    private List<Parameter> parameters = new ArrayList<>();
    
    public Method() {}
    
    public Method(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    
    public List<Parameter> getParameters() { return parameters; }
    public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
}
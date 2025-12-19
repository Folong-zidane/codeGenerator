package com.basiccode.generator.model;

import java.util.*;

public class UMLMethod {
    private String name;
    private String returnType = "void";
    private String visibility = "public";
    private List<UMLParameter> parameters = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private String body;
    
    public void addParameter(UMLParameter parameter) { 
        parameters.add(parameter); 
    }
    
    public void addAnnotation(String annotation) { 
        annotations.add(annotation); 
    }
    
    public String getBusinessLogic() {
        return body; // Retourner le corps de la méthode
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public String getVisibility() {
        return visibility;
    }
    
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    
    public List<UMLParameter> getParameters() {
        return parameters;
    }
    
    public void setParameters(List<UMLParameter> parameters) {
        this.parameters = parameters;
    }
    
    public List<String> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
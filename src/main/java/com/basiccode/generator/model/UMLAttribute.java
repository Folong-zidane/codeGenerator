package com.basiccode.generator.model;

import java.util.*;

public class UMLAttribute {
    
    public static UMLAttributeBuilder builder() {
        return new UMLAttributeBuilder();
    }
    
    public static class UMLAttributeBuilder {
        private String name;
        private String type;
        private String visibility = "public";
        private String defaultValue;
        private List<String> annotations = new ArrayList<>();
        
        public UMLAttributeBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public UMLAttributeBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        public UMLAttributeBuilder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }
        
        public UMLAttributeBuilder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
        
        public UMLAttributeBuilder annotations(List<String> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public UMLAttribute build() {
            UMLAttribute attribute = new UMLAttribute();
            attribute.name = this.name;
            attribute.type = this.type;
            attribute.visibility = this.visibility;
            attribute.defaultValue = this.defaultValue;
            attribute.annotations = this.annotations;
            return attribute;
        }
    }
    private String name;
    private String type;
    private String visibility = "public";
    private String defaultValue;
    private List<String> annotations = new ArrayList<>();
    private List<String> constraints = new ArrayList<>();
    
    public void addAnnotation(String annotation) { 
        annotations.add(annotation); 
    }
    
    public void addConstraint(String constraint) {
        constraints.add(constraint);
    }
    
    public boolean isRelationship() {
        return false; // Implémentation basique
    }
    
    public String getRelationshipType() {
        return "OneToMany";
    }
    
    public String getTargetClass() {
        return "String";
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getVisibility() {
        return visibility;
    }
    
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public List<String> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
    
    public List<String> getConstraints() {
        return constraints;
    }
    
    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }
}
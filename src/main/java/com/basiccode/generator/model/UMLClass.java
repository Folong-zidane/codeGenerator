package com.basiccode.generator.model;

import java.util.*;

public class UMLClass {
    
    public static UMLClassBuilder builder() {
        return new UMLClassBuilder();
    }
    
    public static class UMLClassBuilder {
        private String name;
        private List<UMLAttribute> attributes = new ArrayList<>();
        private List<UMLMethod> methods = new ArrayList<>();
        private List<String> stereotypes = new ArrayList<>();
        private Map<String, String> metadata = new HashMap<>();
        
        public UMLClassBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public UMLClassBuilder attributes(List<UMLAttribute> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        public UMLClassBuilder methods(List<UMLMethod> methods) {
            this.methods = methods;
            return this;
        }
        
        public UMLClassBuilder stereotypes(List<String> stereotypes) {
            this.stereotypes = stereotypes;
            return this;
        }
        
        public UMLClassBuilder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public UMLClass build() {
            UMLClass umlClass = new UMLClass();
            umlClass.name = this.name;
            umlClass.attributes = this.attributes;
            umlClass.methods = this.methods;
            umlClass.stereotypes = this.stereotypes;
            umlClass.metadata = this.metadata;
            return umlClass;
        }
    }
    private String name;
    private List<UMLAttribute> attributes = new ArrayList<>();
    private List<UMLMethod> methods = new ArrayList<>();
    private List<String> stereotypes = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    
    public void addAttribute(UMLAttribute attribute) { 
        attributes.add(attribute); 
    }
    
    public void addMethod(UMLMethod method) { 
        methods.add(method); 
    }
    
    public void addStereotype(String stereotype) { 
        stereotypes.add(stereotype); 
    }
    
    public void addMetadata(String key, String value) { 
        metadata.put(key, value); 
    }
    
    public boolean hasStereotype(String stereotype) { 
        return stereotypes.contains(stereotype); 
    }
    
    public String getSuperClass() {
        return null; // Implémentation basique
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<UMLAttribute> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(List<UMLAttribute> attributes) {
        this.attributes = attributes;
    }
    
    public List<UMLMethod> getMethods() {
        return methods;
    }
    
    public void setMethods(List<UMLMethod> methods) {
        this.methods = methods;
    }
    
    public List<String> getStereotypes() {
        return stereotypes;
    }
    
    public void setStereotypes(List<String> stereotypes) {
        this.stereotypes = stereotypes;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
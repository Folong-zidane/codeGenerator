package com.basiccode.generator.model;

import java.util.*;

/**
 * 🎯 Classe enrichie pour la génération de code - Architecture modulaire
 */
public class EnhancedClass {
    
    public static EnhancedClassBuilder builder() {
        return new EnhancedClassBuilder();
    }
    
    public static class EnhancedClassBuilder {
        private UMLClass originalClass;
        private List<UMLMethod> behavioralMethods = new ArrayList<>();
        private boolean isStateful = false;
        private Map<String, String> generationHints = new HashMap<>();
        private Map<String, String> metadata = new HashMap<>();
        
        public EnhancedClassBuilder originalClass(UMLClass originalClass) {
            this.originalClass = originalClass;
            return this;
        }
        
        public EnhancedClassBuilder behavioralMethods(List<UMLMethod> behavioralMethods) {
            this.behavioralMethods = behavioralMethods;
            return this;
        }
        
        public EnhancedClassBuilder isStateful(boolean isStateful) {
            this.isStateful = isStateful;
            return this;
        }
        
        public EnhancedClassBuilder generationHints(Map<String, String> generationHints) {
            this.generationHints = generationHints;
            return this;
        }
        
        public EnhancedClassBuilder metadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public EnhancedClass build() {
            EnhancedClass enhancedClass = new EnhancedClass();
            enhancedClass.originalClass = this.originalClass;
            enhancedClass.behavioralMethods = this.behavioralMethods;
            enhancedClass.isStateful = this.isStateful;
            enhancedClass.generationHints = this.generationHints;
            if (this.metadata != null) {
                enhancedClass.generationHints.putAll(this.metadata);
            }
            return enhancedClass;
        }
    }
    private UMLClass originalClass;
    private List<UMLMethod> behavioralMethods = new ArrayList<>();
    private boolean isStateful = false;
    private Map<String, String> generationHints = new HashMap<>();
    
    public void addBehavioralMethod(UMLMethod method) {
        if (method != null) {
            behavioralMethods.add(method);
        }
    }
    
    public void addGenerationHint(String key, String value) {
        if (key != null && value != null) {
            generationHints.put(key, value);
        }
    }
    
    // Méthodes manquantes pour compatibilité
    public UMLEnum getStateEnum() {
        return null;
    }
    
    public String getSuperClass() {
        return null;
    }
    
    public String getBusinessLogic() {
        return null;
    }
    
    public List<String> getStates() {
        return Arrays.asList("ACTIVE", "INACTIVE", "PENDING");
    }
    
    public UMLClass getOriginalClass() {
        return originalClass;
    }
    
    public void setOriginalClass(UMLClass originalClass) {
        this.originalClass = originalClass;
    }
    
    public boolean isStateful() {
        return isStateful;
    }
    
    public void setStateful(boolean stateful) {
        isStateful = stateful;
    }
    
    public List<UMLMethod> getBehavioralMethods() {
        return behavioralMethods;
    }
    
    public void setBehavioralMethods(List<UMLMethod> behavioralMethods) {
        this.behavioralMethods = behavioralMethods;
    }
    
    public Map<String, String> getGenerationHints() {
        return generationHints;
    }
    
    public void setGenerationHints(Map<String, String> generationHints) {
        this.generationHints = generationHints;
    }
    
    // Delegate methods to originalClass
    public String getName() {
        return originalClass != null ? originalClass.getName() : null;
    }
    
    public void setName(String name) {
        if (originalClass != null) {
            originalClass.setName(name);
        }
    }
    
    public List<UMLAttribute> getAttributes() {
        return originalClass != null ? originalClass.getAttributes() : new ArrayList<>();
    }
    
    public void setAttributes(List<UMLAttribute> attributes) {
        if (originalClass != null) {
            originalClass.setAttributes(attributes);
        }
    }
    
    public List<UMLMethod> getMethods() {
        return originalClass != null ? originalClass.getMethods() : new ArrayList<>();
    }
    
    public void setMethods(List<UMLMethod> methods) {
        if (originalClass != null) {
            originalClass.setMethods(methods);
        }
    }
}
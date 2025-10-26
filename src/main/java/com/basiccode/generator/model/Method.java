package com.basiccode.generator.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Method {
    private String name;
    private String returnType;
    private Visibility visibility = Visibility.PUBLIC;
    private List<Parameter> parameters = new ArrayList<>();
    private List<Annotation> annotations = new ArrayList<>();
    private List<String> modifiers = new ArrayList<>();
    private BusinessPattern businessPattern;
    
    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }
    
    public void addModifier(String modifier) {
        modifiers.add(modifier);
    }
    
    public boolean hasAnnotation(String type) {
        return annotations.stream().anyMatch(a -> a.getType().equals(type));
    }
    
    public Annotation getAnnotation(String type) {
        return annotations.stream()
            .filter(a -> a.getType().equals(type))
            .findFirst()
            .orElse(null);
    }
    
    public boolean isAbstract() {
        return modifiers.contains("abstract");
    }
    
    public boolean isStatic() {
        return modifiers.contains("static");
    }
    
    public boolean isFinal() {
        return modifiers.contains("final");
    }
    
    public BusinessPattern getBusinessPattern() {
        if (businessPattern == null) {
            businessPattern = BusinessPattern.detectPattern(name);
        }
        return businessPattern;
    }
    
    public String generateImplementation() {
        return switch (getBusinessPattern()) {
            case CALCULATION -> "// TODO: Implement calculation logic\nreturn null;";
            case VALIDATION -> "// TODO: Implement validation logic\nreturn true;";
            case WORKFLOW -> "// TODO: Implement workflow logic";
            case FACTORY -> "// TODO: Implement factory logic\nreturn new " + returnType + "();";
            case NOTIFICATION -> "// TODO: Implement notification logic";
            case QUERY -> "// TODO: Implement query logic\nreturn null;";
            case COMMAND -> "// TODO: Implement command logic";
            default -> "// TODO: Implement business logic";
        };
    }
}
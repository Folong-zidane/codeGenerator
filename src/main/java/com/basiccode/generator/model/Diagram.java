package com.basiccode.generator.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Diagram {
    private List<ClassModel> classes = new ArrayList<>();
    private List<Relationship> relationships = new ArrayList<>();
    
    public void addClass(ClassModel clazz) {
        classes.add(clazz);
    }
    
    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }
    
    public List<ClassModel> getClasses() {
        return classes;
    }
}
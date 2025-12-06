package com.basiccode.generator.model;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * Modèle représentant une classe
 */
@Data
public class ClassModel {
    private String name;
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private String superClass;
    private boolean isAbstract = false;
    private boolean isInterface = false;
    
    public ClassModel() {}
    
    public ClassModel(String name) {
        this.name = name;
    }
    
    public String getParentClass() { return superClass; }
    public String getStereotype() { return isInterface ? "interface" : (isAbstract ? "abstract" : null); }
    public boolean isEnumeration() { return false; }
}
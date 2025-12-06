package com.basiccode.generator.model;

import lombok.Data;

/**
 * Modèle représentant un champ/attribut
 */
@Data
public class Field {
    private String name;
    private String type;
    
    public Field() {}
    
    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public boolean isNullable() { return true; }
    public boolean isUnique() { return false; }
    public Visibility getVisibility() { return Visibility.PUBLIC; }
    public void setVisibility(Visibility visibility) {}
    public int getMaxSize() { return 255; }
    public int getMinSize() { return 0; }
    public boolean hasAnnotation(String annotation) { return false; }
    public boolean hasConstraint(String constraint) { return false; }
}
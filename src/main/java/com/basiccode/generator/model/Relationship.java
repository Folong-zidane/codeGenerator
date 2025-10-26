package com.basiccode.generator.model;

import java.util.Objects;

public class Relationship {
    private String fromClass;
    private String toClass;
    private RelationshipType type;
    private String label;
    
    // Multiplicités
    private String fromMultiplicity;
    private String toMultiplicity;
    
    // Flags
    private boolean isBidirectional;
    private boolean isCascade;
    private boolean isOptional;
    
    // JPA Mapping
    private String mappedBy;
    private String joinColumn;
    private String joinTable;
    
    // Constructeurs
    public Relationship() {}
    
    public Relationship(String fromClass, String toClass, RelationshipType type) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.type = type;
    }
    
    // Getters & Setters
    public String getFromClass() { return fromClass; }
    public void setFromClass(String fromClass) { this.fromClass = fromClass; }
    
    public String getToClass() { return toClass; }
    public void setToClass(String toClass) { this.toClass = toClass; }
    
    public RelationshipType getType() { return type; }
    public void setType(RelationshipType type) { this.type = type; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public String getFromMultiplicity() { return fromMultiplicity; }
    public void setFromMultiplicity(String fromMultiplicity) { this.fromMultiplicity = fromMultiplicity; }
    
    public String getToMultiplicity() { return toMultiplicity; }
    public void setToMultiplicity(String toMultiplicity) { this.toMultiplicity = toMultiplicity; }
    
    public boolean isBidirectional() { return isBidirectional; }
    public void setBidirectional(boolean bidirectional) { isBidirectional = bidirectional; }
    
    public boolean isCascade() { return isCascade; }
    public void setCascade(boolean cascade) { isCascade = cascade; }
    
    public boolean isOptional() { return isOptional; }
    public void setOptional(boolean optional) { isOptional = optional; }
    
    public String getMappedBy() { return mappedBy; }
    public void setMappedBy(String mappedBy) { this.mappedBy = mappedBy; }
    
    public String getJoinColumn() { return joinColumn; }
    public void setJoinColumn(String joinColumn) { this.joinColumn = joinColumn; }
    
    public String getJoinTable() { return joinTable; }
    public void setJoinTable(String joinTable) { this.joinTable = joinTable; }
    
    // Compatibility avec l'ancien modèle
    public String getSourceClass() { return fromClass; }
    public void setSourceClass(String sourceClass) { this.fromClass = sourceClass; }
    
    public String getTargetClass() { return toClass; }
    public void setTargetClass(String targetClass) { this.toClass = targetClass; }
    
    public String getSourceMultiplicity() { return fromMultiplicity; }
    public void setSourceMultiplicity(String sourceMultiplicity) { this.fromMultiplicity = sourceMultiplicity; }
    
    public String getTargetMultiplicity() { return toMultiplicity; }
    public void setTargetMultiplicity(String targetMultiplicity) { this.toMultiplicity = targetMultiplicity; }
    
    @Override
    public String toString() {
        return String.format("%s --%s--> %s [%s]", fromClass, type, toClass, label);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(fromClass, that.fromClass) &&
               Objects.equals(toClass, that.toClass) &&
               Objects.equals(type, that.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fromClass, toClass, type);
    }
}
package com.basiccode.generator.relationship;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.basiccode.generator.model.Relationship;

import java.util.List;

public class RelationshipHandler {
    
    public void processRelationships(List<ClassModel> classes, List<Relationship> relationships) {
        for (Relationship rel : relationships) {
            processRelationship(classes, rel);
        }
    }
    
    private void processRelationship(List<ClassModel> classes, Relationship rel) {
        ClassModel sourceClass = findClass(classes, rel.getSourceClass());
        ClassModel targetClass = findClass(classes, rel.getTargetClass());
        
        if (sourceClass == null || targetClass == null) return;
        
        CardinalityType sourceCard = parseCardinality(String.valueOf(rel.getSourceMultiplicity()));
        CardinalityType targetCard = parseCardinality(String.valueOf(rel.getTargetMultiplicity()));
        
        if (sourceCard == CardinalityType.ONE && targetCard == CardinalityType.MANY) {
            addOneToManyRelation(sourceClass, targetClass);
        } else if (sourceCard == CardinalityType.MANY && targetCard == CardinalityType.ONE) {
            addManyToOneRelation(sourceClass, targetClass);
        } else if (sourceCard == CardinalityType.ONE && targetCard == CardinalityType.ONE) {
            addOneToOneRelation(sourceClass, targetClass);
        } else if (sourceCard == CardinalityType.MANY && targetCard == CardinalityType.MANY) {
            addManyToManyRelation(sourceClass, targetClass);
        }
    }
    
    private void addOneToManyRelation(ClassModel source, ClassModel target) {
        // Dans source : List<Target>
        Field listField = new Field();
        listField.setName(target.getName().toLowerCase() + "s");
        listField.setType("List<" + target.getName() + ">");
        source.getFields().add(listField);
        
        // Dans target : FK vers source
        Field fkField = new Field();
        fkField.setName(source.getName().toLowerCase() + "Id");
        fkField.setType("UUID");
        target.getFields().add(fkField);
    }
    
    private void addManyToOneRelation(ClassModel source, ClassModel target) {
        // Dans source : FK vers target
        Field fkField = new Field();
        fkField.setName(target.getName().toLowerCase() + "Id");
        fkField.setType("UUID");
        source.getFields().add(fkField);
        
        // Dans target : List<Source>
        Field listField = new Field();
        listField.setName(source.getName().toLowerCase() + "s");
        listField.setType("List<" + source.getName() + ">");
        target.getFields().add(listField);
    }
    
    private void addOneToOneRelation(ClassModel source, ClassModel target) {
        Field fkField = new Field();
        fkField.setName(target.getName().toLowerCase() + "Id");
        fkField.setType("UUID");
        source.getFields().add(fkField);
    }
    
    private void addManyToManyRelation(ClassModel source, ClassModel target) {
        // Dans source : Set<Target>
        Field sourceField = new Field();
        sourceField.setName(target.getName().toLowerCase() + "s");
        sourceField.setType("Set<" + target.getName() + ">");
        source.getFields().add(sourceField);
        
        // Dans target : Set<Source>
        Field targetField = new Field();
        targetField.setName(source.getName().toLowerCase() + "s");
        targetField.setType("Set<" + source.getName() + ">");
        target.getFields().add(targetField);
    }
    
    private CardinalityType parseCardinality(String multiplicity) {
        if (multiplicity == null) return CardinalityType.ONE;
        
        return switch (multiplicity) {
            case "1" -> CardinalityType.ONE;
            case "*", "0..*", "1..*" -> CardinalityType.MANY;
            case "0..1" -> CardinalityType.ZERO_OR_ONE;
            default -> CardinalityType.ONE;
        };
    }
    
    private ClassModel findClass(List<ClassModel> classes, String className) {
        return classes.stream()
            .filter(c -> c.getName().equals(className))
            .findFirst()
            .orElse(null);
    }
    
    public enum CardinalityType {
        ONE, MANY, ZERO_OR_ONE
    }
}
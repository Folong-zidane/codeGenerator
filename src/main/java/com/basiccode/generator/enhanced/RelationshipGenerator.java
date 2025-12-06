package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Relationship;
import com.basiccode.generator.model.RelationshipType;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

public class RelationshipGenerator {
    
    public static List<FieldSpec> generateRelationshipFields(ClassModel classModel, List<Relationship> relationships) {
        return relationships.stream()
            .filter(rel -> rel.getFromClass().equals(classModel.getName()) || rel.getToClass().equals(classModel.getName()))
            .map(rel -> generateRelationshipField(classModel, rel))
            .toList();
    }
    
    private static FieldSpec generateRelationshipField(ClassModel classModel, Relationship relationship) {
        boolean isOwner = relationship.getFromClass().equals(classModel.getName());
        String relatedClass = isOwner ? relationship.getToClass() : relationship.getFromClass();
        
        FieldSpec.Builder fieldBuilder;
        
        if (isOneToMany(relationship, isOwner)) {
            // @OneToMany
            TypeName listType = ParameterizedTypeName.get(
                ClassName.get("java.util", "List"),
                ClassName.get("", relatedClass)
            );
            
            fieldBuilder = FieldSpec.builder(listType, pluralize(relatedClass.toLowerCase()))
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "OneToMany"))
                    .addMember("mappedBy", "$S", classModel.getName().toLowerCase())
                    .addMember("cascade", "jakarta.persistence.CascadeType.ALL")
                    .addMember("fetch", "jakarta.persistence.FetchType.LAZY")
                    .build())
                .initializer("new java.util.ArrayList<>()");
                
        } else if (isManyToOne(relationship, isOwner)) {
            // @ManyToOne
            fieldBuilder = FieldSpec.builder(ClassName.get("", relatedClass), relatedClass.toLowerCase())
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "ManyToOne"))
                    .addMember("fetch", "jakarta.persistence.FetchType.LAZY")
                    .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "JoinColumn"))
                    .addMember("name", "$S", relatedClass.toLowerCase() + "_id")
                    .build());
                    
        } else {
            // @ManyToMany ou @OneToOne
            fieldBuilder = FieldSpec.builder(ClassName.get("", relatedClass), relatedClass.toLowerCase())
                .addModifiers(Modifier.PRIVATE);
        }
        
        return fieldBuilder.build();
    }
    
    private static boolean isOneToMany(Relationship relationship, boolean isOwner) {
        String fromMult = String.valueOf(relationship.getFromMultiplicity());
        String toMult = String.valueOf(relationship.getToMultiplicity());
        return (isOwner && fromMult.equals("1") && toMult.equals("*")) ||
               (!isOwner && fromMult.equals("*") && toMult.equals("1"));
    }
    
    private static boolean isManyToOne(Relationship relationship, boolean isOwner) {
        String fromMult = String.valueOf(relationship.getFromMultiplicity());
        String toMult = String.valueOf(relationship.getToMultiplicity());
        return (isOwner && fromMult.equals("*") && toMult.equals("1")) ||
               (!isOwner && fromMult.equals("1") && toMult.equals("*"));
    }
    
    private static String pluralize(String word) {
        if (word.endsWith("y")) {
            return word.substring(0, word.length() - 1) + "ies";
        } else if (word.endsWith("s") || word.endsWith("x") || word.endsWith("ch") || word.endsWith("sh")) {
            return word + "es";
        } else {
            return word + "s";
        }
    }
}
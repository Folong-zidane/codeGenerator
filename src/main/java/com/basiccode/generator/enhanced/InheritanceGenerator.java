package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.ClassModel;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class InheritanceGenerator {
    
    public static TypeSpec.Builder addInheritanceAnnotations(TypeSpec.Builder classBuilder, ClassModel classModel) {
        if (classModel.isAbstract()) {
            // Classe abstraite parent
            classBuilder.addModifiers(Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Entity"))
                    .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Inheritance"))
                    .addMember("strategy", "jakarta.persistence.InheritanceType.JOINED")
                    .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "DiscriminatorColumn"))
                    .addMember("name", "$S", "entity_type")
                    .addMember("discriminatorType", "jakarta.persistence.DiscriminatorType.STRING")
                    .build());
        } else if (hasParentClass(classModel)) {
            // Classe enfant
            String discriminatorValue = classModel.getName().toUpperCase();
            classBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Entity"))
                    .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "DiscriminatorValue"))
                    .addMember("value", "$S", discriminatorValue)
                    .build());
        }
        
        return classBuilder;
    }
    
    public static TypeSpec.Builder addParentClass(TypeSpec.Builder classBuilder, ClassModel classModel) {
        if (hasParentClass(classModel)) {
            String parentClass = getParentClass(classModel);
            classBuilder.superclass(ClassName.get("", parentClass));
        }
        return classBuilder;
    }
    
    private static boolean hasParentClass(ClassModel classModel) {
        // Logique pour détecter l'héritage basée sur le nom ou les annotations
        String className = classModel.getName();
        return className.equals("Client") || className.equals("Freelance") || 
               className.equals("Employee") || className.equals("AgencyOwner") ||
               className.equals("Deliverer");
    }
    
    private static String getParentClass(ClassModel classModel) {
        String className = classModel.getName();
        
        if (className.equals("Client")) {
            return "User";
        } else if (className.equals("Freelance") || className.equals("Employee") || 
                   className.equals("AgencyOwner") || className.equals("Deliverer")) {
            return "BusinessActor";
        }
        
        return null;
    }
}
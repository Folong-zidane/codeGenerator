package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;

public class TestGenerator {
    
    public JavaFile generateUnitTests(ClassModel model, String basePackage) {
        TypeSpec.Builder testClass = TypeSpec.classBuilder(model.getName() + "Test")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "TestInstance"))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.junit.jupiter.api.TestInstance", "Lifecycle"))
                .addMember("value", "$T.PER_CLASS", ClassName.get("org.junit.jupiter.api.TestInstance", "Lifecycle"))
                .build())
            .addAnnotation(ClassName.get("org.springframework.boot.test.context", "SpringBootTest"));
        
        // Add test fields
        testClass.addField(FieldSpec.builder(
            ClassName.get(basePackage + ".repository", model.getName() + "Repository"),
            "repository",
            Modifier.PRIVATE
        ).addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired")).build());
        
        testClass.addField(FieldSpec.builder(
            ClassName.get(basePackage + ".service", model.getName() + "Service"),
            "service",
            Modifier.PRIVATE
        ).addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired")).build());
        
        // Generate CRUD tests
        testClass.addMethod(generateCreateTest(model));
        testClass.addMethod(generateReadTest(model));
        testClass.addMethod(generateUpdateTest(model));
        testClass.addMethod(generateDeleteTest(model));
        testClass.addMethod(generateValidationTest(model));
        
        return JavaFile.builder(basePackage + ".test", testClass.build())
            .addFileComment("Generated Unit Tests")
            .build();
    }
    
    private MethodSpec generateCreateTest(ClassModel model) {
        return MethodSpec.methodBuilder("testCreate" + model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "Test"))
            .addStatement("$T entity = new $T()", 
                ClassName.get("", model.getName()), 
                ClassName.get("", model.getName()))
            .addStatement("// Set test data")
            .addStatement("$T saved = service.create(entity)", ClassName.get("", model.getName()))
            .addStatement("$T.assertNotNull(saved.getId())", ClassName.get("org.junit.jupiter.api", "Assertions"))
            .build();
    }
    
    private MethodSpec generateReadTest(ClassModel model) {
        return MethodSpec.methodBuilder("testRead" + model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "Test"))
            .addStatement("$T entity = new $T()", 
                ClassName.get("", model.getName()), 
                ClassName.get("", model.getName()))
            .addStatement("$T saved = service.create(entity)", ClassName.get("", model.getName()))
            .addStatement("$T<$T> found = service.findById(saved.getId())", 
                ClassName.get("java.util", "Optional"),
                ClassName.get("", model.getName()))
            .addStatement("$T.assertTrue(found.isPresent())", ClassName.get("org.junit.jupiter.api", "Assertions"))
            .build();
    }
    
    private MethodSpec generateUpdateTest(ClassModel model) {
        return MethodSpec.methodBuilder("testUpdate" + model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "Test"))
            .addStatement("$T entity = new $T()", 
                ClassName.get("", model.getName()), 
                ClassName.get("", model.getName()))
            .addStatement("$T saved = service.create(entity)", ClassName.get("", model.getName()))
            .addStatement("// Modify entity")
            .addStatement("$T updated = service.update(saved.getId(), saved)", ClassName.get("", model.getName()))
            .addStatement("$T.assertNotNull(updated)", ClassName.get("org.junit.jupiter.api", "Assertions"))
            .build();
    }
    
    private MethodSpec generateDeleteTest(ClassModel model) {
        return MethodSpec.methodBuilder("testDelete" + model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "Test"))
            .addStatement("$T entity = new $T()", 
                ClassName.get("", model.getName()), 
                ClassName.get("", model.getName()))
            .addStatement("$T saved = service.create(entity)", ClassName.get("", model.getName()))
            .addStatement("service.delete(saved.getId())")
            .addStatement("$T<$T> found = service.findById(saved.getId())", 
                ClassName.get("java.util", "Optional"),
                ClassName.get("", model.getName()))
            .addStatement("$T.assertFalse(found.isPresent())", ClassName.get("org.junit.jupiter.api", "Assertions"))
            .build();
    }
    
    private MethodSpec generateValidationTest(ClassModel model) {
        return MethodSpec.methodBuilder("testValidation" + model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.junit.jupiter.api", "Test"))
            .addStatement("$T entity = new $T()", 
                ClassName.get("", model.getName()), 
                ClassName.get("", model.getName()))
            .addStatement("// Test validation constraints")
            .addStatement("$T.assertThrows($T.class, () -> service.create(entity))", 
                ClassName.get("org.junit.jupiter.api", "Assertions"),
                ClassName.get("jakarta.validation", "ConstraintViolationException"))
            .build();
    }
}
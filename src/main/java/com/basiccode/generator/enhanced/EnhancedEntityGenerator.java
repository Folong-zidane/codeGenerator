package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;

public class EnhancedEntityGenerator {
    
    public JavaFile generateEnhancedEntity(ClassModel model, String basePackage) {
        TypeSpec.Builder entityBuilder = TypeSpec.classBuilder(model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(createEntityAnnotation())
            .addAnnotation(createTableAnnotation(model.getName()))
            .addAnnotation(createSwaggerSchemaAnnotation(model.getName()));
        
        addIdField(entityBuilder);
        
        // Ajouter seulement les champs du modèle (pas l'ID qui est déjà ajouté)
        for (Field field : model.getFields()) {
            if (!field.getName().equals("id")) {
                entityBuilder.addField(generateEnhancedField(field));
            }
        }
        
        addAuditFields(entityBuilder);
        addConstructors(entityBuilder, model);
        addAccessors(entityBuilder, model);
        addUtilityMethods(entityBuilder, model);
        
        return JavaFile.builder(basePackage + ".entity", entityBuilder.build())
            .addFileComment("Generated Enhanced Entity with ORM mapping and Swagger documentation")
            .build();
    }
    
    private AnnotationSpec createEntityAnnotation() {
        return AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Entity")).build();
    }
    
    private AnnotationSpec createTableAnnotation(String entityName) {
        return AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Table"))
            .addMember("name", "$S", toSnakeCase(entityName))
            .build();
    }
    
    private AnnotationSpec createSwaggerSchemaAnnotation(String entityName) {
        return AnnotationSpec.builder(ClassName.get("io.swagger.v3.oas.annotations.media", "Schema"))
            .addMember("description", "$S", entityName + " entity")
            .build();
    }
    
    private void addIdField(TypeSpec.Builder builder) {
        builder.addField(FieldSpec.builder(UUID.class, "id", Modifier.PRIVATE)
            .addAnnotation(ClassName.get("jakarta.persistence", "Id"))
            .addAnnotation(ClassName.get("jakarta.persistence", "GeneratedValue"))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "id")
                .addMember("updatable", "false")
                .build())
            .build());
    }
    
    private FieldSpec generateEnhancedField(Field field) {
        TypeName fieldType = getJavaType(field.getType());
        FieldSpec.Builder builder = FieldSpec.builder(fieldType, field.getName(), Modifier.PRIVATE);
        
        AnnotationSpec.Builder columnBuilder = AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
            .addMember("name", "$S", toSnakeCase(field.getName()))
            .addMember("nullable", "$L", field.isNullable());
        
        if (field.getType().equals("String")) {
            columnBuilder.addMember("length", "255");
            if (field.getName().toLowerCase().contains("email")) {
                columnBuilder.addMember("unique", "true");
                builder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.validation.constraints", "Email"))
                    .addMember("message", "$S", "Invalid email format")
                    .build());
            }
        }
        
        builder.addAnnotation(columnBuilder.build());
        
        builder.addAnnotation(AnnotationSpec.builder(ClassName.get("io.swagger.v3.oas.annotations.media", "Schema"))
            .addMember("description", "$S", generateFieldDescription(field))
            .addMember("required", "$L", !field.isNullable())
            .build());
        
        return builder.build();
    }
    
    private void addAuditFields(TypeSpec.Builder builder) {
        builder.addField(FieldSpec.builder(ClassName.get("java.time", "Instant"), "createdAt", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "created_at")
                .addMember("nullable", "false")
                .addMember("updatable", "false")
                .build())
            .build());
        
        builder.addField(FieldSpec.builder(ClassName.get("java.time", "Instant"), "updatedAt", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "updated_at")
                .addMember("nullable", "false")
                .build())
            .build());
        
        builder.addField(FieldSpec.builder(Long.class, "version", Modifier.PRIVATE)
            .addAnnotation(ClassName.get("jakarta.persistence", "Version"))
            .build());
    }
    
    private void addConstructors(TypeSpec.Builder builder, ClassModel model) {
        builder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addComment("Default constructor for JPA")
            .build());
    }
    
    private void addAccessors(TypeSpec.Builder builder, ClassModel model) {
        builder.addMethod(MethodSpec.methodBuilder("getId")
            .addModifiers(Modifier.PUBLIC)
            .returns(UUID.class)
            .addStatement("return id")
            .build());
        
        builder.addMethod(MethodSpec.methodBuilder("setId")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "id")
            .addStatement("this.id = id")
            .build());
        
        for (Field field : model.getFields()) {
            if (!field.getName().equals("id")) {
                String fieldName = field.getName();
                String capitalizedName = capitalize(fieldName);
                TypeName fieldType = getJavaType(field.getType());
                
                builder.addMethod(MethodSpec.methodBuilder("get" + capitalizedName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addStatement("return $L", fieldName)
                    .build());
                
                builder.addMethod(MethodSpec.methodBuilder("set" + capitalizedName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(fieldType, fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName)
                    .build());
            }
        }
    }
    
    private void addUtilityMethods(TypeSpec.Builder builder, ClassModel model) {
        builder.addMethod(MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addParameter(Object.class, "obj")
            .addStatement("if (this == obj) return true")
            .addStatement("if (obj == null || getClass() != obj.getClass()) return false")
            .addStatement("$T other = ($T) obj", ClassName.bestGuess(model.getName()), ClassName.bestGuess(model.getName()))
            .addStatement("return id != null && id.equals(other.id)")
            .build());
        
        builder.addMethod(MethodSpec.methodBuilder("hashCode")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(int.class)
            .addStatement("return id != null ? id.hashCode() : 0")
            .build());
    }
    
    private String generateFieldDescription(Field field) {
        String baseName = field.getName();
        if (baseName.toLowerCase().contains("email")) {
            return "Email address";
        } else if (baseName.toLowerCase().contains("name")) {
            return "Name";
        } else if (baseName.toLowerCase().contains("price")) {
            return "Price in currency units";
        } else {
            return baseName + " (" + field.getType() + ")";
        }
    }
    
    private TypeName getJavaType(String type) {
        return switch (type) {
            case "String" -> ClassName.get(String.class);
            case "UUID" -> ClassName.get(UUID.class);
            case "Boolean" -> ClassName.get(Boolean.class);
            case "Integer" -> ClassName.get(Integer.class);
            case "Float" -> ClassName.get(Float.class);
            case "Date" -> ClassName.get("java.time", "Instant");
            default -> ClassName.bestGuess(type);
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
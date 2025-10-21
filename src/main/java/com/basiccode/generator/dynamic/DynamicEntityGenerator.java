package com.basiccode.generator.dynamic;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;

public class DynamicEntityGenerator {
    
    public JavaFile generateDynamicEntity(ClassModel model, String basePackage) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("jakarta.persistence", "Entity"))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Table"))
                .addMember("name", "$S", toSnakeCase(model.getName()))
                .build());
        
        // Champs de base
        addBaseFields(classBuilder, model);
        
        // Champs dynamiques selon le contexte
        addDynamicFields(classBuilder, model);
        
        // Méthodes dynamiques
        addDynamicMethods(classBuilder, model);
        
        return JavaFile.builder(basePackage + ".entity", classBuilder.build())
            .addFileComment("Generated Dynamic Entity - Preserves manual changes")
            .build();
    }
    
    private void addBaseFields(TypeSpec.Builder classBuilder, ClassModel model) {
        // ID obligatoire
        classBuilder.addField(FieldSpec.builder(UUID.class, "id", Modifier.PRIVATE)
            .addAnnotation(ClassName.get("jakarta.persistence", "Id"))
            .addAnnotation(ClassName.get("jakarta.persistence", "GeneratedValue"))
            .build());
        
        // Champs du modèle
        for (Field field : model.getFields()) {
            classBuilder.addField(generateDynamicField(field));
        }
    }
    
    private void addDynamicFields(TypeSpec.Builder classBuilder, ClassModel model) {
        // Champs conditionnels selon le type d'entité
        if (isUserEntity(model)) {
            addUserDynamicFields(classBuilder);
        } else if (isColisEntity(model)) {
            addColisDynamicFields(classBuilder);
        } else if (isBusinessEntity(model)) {
            addBusinessDynamicFields(classBuilder);
        }
        
        // Champs d'audit universels
        addAuditFields(classBuilder);
    }
    
    private void addUserDynamicFields(TypeSpec.Builder classBuilder) {
        // Champs qui apparaissent selon AccountType
        classBuilder.addField(FieldSpec.builder(String.class, "businessName", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "business_name")
                .build())
            .addJavadoc("Appears when accountType is FREELANCE, EMPLOYEE, AGENCE, etc.")
            .build());
        
        classBuilder.addField(FieldSpec.builder(UUID.class, "agenceId", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "agence_id")
                .build())
            .addJavadoc("Appears when accountType is EMPLOYEE")
            .build());
        
        classBuilder.addField(FieldSpec.builder(UUID.class, "vehiculeId", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "vehicule_id")
                .build())
            .addJavadoc("Appears when accountType is LIVREUR")
            .build());
    }
    
    private void addColisDynamicFields(TypeSpec.Builder classBuilder) {
        // Champs qui apparaissent selon ColisStatus
        classBuilder.addField(FieldSpec.builder(String.class, "photoPreuveLivraisonUrl", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "photo_preuve_livraison_url")
                .build())
            .addJavadoc("Appears when status is LIVRE")
            .build());
        
        classBuilder.addField(FieldSpec.builder(String.class, "retirantNom", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "retirant_nom")
                .build())
            .addJavadoc("Appears when status is RETIRE")
            .build());
        
        classBuilder.addField(FieldSpec.builder(String.class, "signatureDestinataireUrl", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "signature_destinataire_url")
                .build())
            .addJavadoc("Appears when status is LIVRE or RETIRE")
            .build());
    }
    
    private void addBusinessDynamicFields(TypeSpec.Builder classBuilder) {
        // Champs métier communs
        classBuilder.addField(FieldSpec.builder(Boolean.class, "isVerified", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "is_verified")
                .addMember("nullable", "false")
                .build())
            .addJavadoc("Business verification status")
            .build());
    }
    
    private void addAuditFields(TypeSpec.Builder classBuilder) {
        // Champs d'audit automatiques
        classBuilder.addField(FieldSpec.builder(UUID.class, "createdBy", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "created_by")
                .build())
            .build());
        
        classBuilder.addField(FieldSpec.builder(UUID.class, "updatedBy", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "updated_by")
                .build())
            .build());
        
        classBuilder.addField(FieldSpec.builder(Integer.class, "version", Modifier.PRIVATE)
            .addAnnotation(ClassName.get("jakarta.persistence", "Version"))
            .addJavadoc("Optimistic locking version")
            .build());
    }
    
    private void addDynamicMethods(TypeSpec.Builder classBuilder, ClassModel model) {
        // Méthodes conditionnelles selon le type
        if (hasHistoryEntity(model)) {
            addHistoryMethods(classBuilder, model);
        }
        
        if (hasStatusField(model)) {
            addStatusMethods(classBuilder, model);
        }
        
        // Méthodes d'audit
        addAuditMethods(classBuilder);
    }
    
    private void addHistoryMethods(TypeSpec.Builder classBuilder, ClassModel model) {
        String historyType = model.getName() + "Historique";
        
        classBuilder.addMethod(MethodSpec.methodBuilder("addHistoryEntry")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "action")
            .addParameter(UUID.class, "acteurId")
            .addParameter(String.class, "commentaire")
            .addJavadoc("Adds entry to history table")
            .addStatement("// TODO: Implement history logging")
            .build());
        
        classBuilder.addMethod(MethodSpec.methodBuilder("getFullHistory")
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), 
                    ClassName.bestGuess(historyType)))
            .addJavadoc("Gets complete history for this entity")
            .addStatement("// TODO: Implement history retrieval")
            .addStatement("return new $T<>()", ClassName.get("java.util", "ArrayList"))
            .build());
    }
    
    private void addStatusMethods(TypeSpec.Builder classBuilder, ClassModel model) {
        classBuilder.addMethod(MethodSpec.methodBuilder("canTransitionTo")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "newStatus")
            .returns(boolean.class)
            .addJavadoc("Validates if status transition is allowed")
            .addStatement("// TODO: Implement status transition validation")
            .addStatement("return true")
            .build());
        
        classBuilder.addMethod(MethodSpec.methodBuilder("updateStatus")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "newStatus")
            .addParameter(UUID.class, "acteurId")
            .addParameter(String.class, "commentaire")
            .addJavadoc("Updates status with history tracking")
            .addStatement("// TODO: Implement status update with history")
            .build());
    }
    
    private void addAuditMethods(TypeSpec.Builder classBuilder) {
        classBuilder.addMethod(MethodSpec.methodBuilder("updateAuditFields")
            .addModifiers(Modifier.PRIVATE)
            .addParameter(UUID.class, "userId")
            .addAnnotation(ClassName.get("jakarta.persistence", "PreUpdate"))
            .addStatement("this.updatedBy = userId")
            .addStatement("// Version is auto-incremented by JPA")
            .build());
    }
    
    private FieldSpec generateDynamicField(Field field) {
        TypeName fieldType = getJavaType(field.getType());
        
        FieldSpec.Builder builder = FieldSpec.builder(fieldType, field.getName(), Modifier.PRIVATE);
        
        // Annotations JPA dynamiques
        if (field.getType().equals("String")) {
            builder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", toSnakeCase(field.getName()))
                .addMember("nullable", "$L", field.isNullable())
                .build());
        } else if (field.getType().equals("JSON")) {
            builder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("columnDefinition", "$S", "jsonb")
                .build());
        }
        
        return builder.build();
    }
    
    // Méthodes utilitaires
    private boolean isUserEntity(ClassModel model) {
        return model.getName().equals("User") || model.getName().contains("User");
    }
    
    private boolean isColisEntity(ClassModel model) {
        return model.getName().equals("Colis");
    }
    
    private boolean isBusinessEntity(ClassModel model) {
        return model.getName().equals("BusinessActor") || 
               model.getName().equals("Freelance") ||
               model.getName().equals("Employee") ||
               model.getName().equals("Agence");
    }
    
    private boolean hasHistoryEntity(ClassModel model) {
        return !model.getName().endsWith("Historique") && 
               !model.getName().endsWith("Log");
    }
    
    private boolean hasStatusField(ClassModel model) {
        return model.getFields().stream()
            .anyMatch(field -> field.getName().toLowerCase().contains("status"));
    }
    
    private TypeName getJavaType(String type) {
        return switch (type) {
            case "String" -> ClassName.get(String.class);
            case "UUID" -> ClassName.get(UUID.class);
            case "Boolean" -> ClassName.get(Boolean.class);
            case "Integer" -> ClassName.get(Integer.class);
            case "Float" -> ClassName.get(Float.class);
            case "Date" -> ClassName.get("java.time", "Instant");
            case "JSON" -> ClassName.get(String.class); // Stored as String with @Column(columnDefinition = "jsonb")
            default -> ClassName.bestGuess(type);
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
package com.basiccode.generator.dynamic;

import com.basiccode.generator.model.ClassModel;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;

public class DynamicRepositoryGenerator {
    
    public JavaFile generateDynamicRepository(ClassModel model, String basePackage) {
        String repositoryName = model.getName() + "Repository";
        ClassName entityClass = ClassName.get(basePackage + ".entity", model.getName());
        
        TypeSpec.Builder repositoryBuilder = TypeSpec.interfaceBuilder(repositoryName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.stereotype", "Repository"))
            .addSuperinterface(ParameterizedTypeName.get(
                ClassName.get("org.springframework.data.jpa.repository", "JpaRepository"),
                entityClass,
                ClassName.get(UUID.class)));
        
        // Dynamic query methods based on entity type
        addDynamicQueryMethods(repositoryBuilder, model, entityClass);
        
        return JavaFile.builder(basePackage + ".repository", repositoryBuilder.build())
            .addFileComment("Generated Dynamic Repository - Data access layer")
            .build();
    }
    
    private void addDynamicQueryMethods(TypeSpec.Builder repositoryBuilder, ClassModel model, ClassName entityClass) {
        // Entity-specific queries
        if (isUserEntity(model)) {
            addUserQueries(repositoryBuilder, entityClass);
        } else if (isColisEntity(model)) {
            addColisQueries(repositoryBuilder, entityClass);
        }
        
        // Status-based queries if applicable
        if (hasStatusField(model)) {
            addStatusQueries(repositoryBuilder, entityClass);
        }
        
        // Audit queries for all entities
        addAuditQueries(repositoryBuilder, entityClass);
    }
    
    private void addUserQueries(TypeSpec.Builder repositoryBuilder, ClassName entityClass) {
        // Account type queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByAccountType")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(String.class, "accountType")
            .build());
        
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByAgenceId")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(UUID.class, "agenceId")
            .build());
        
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByVehiculeId")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "Optional"), entityClass))
            .addParameter(UUID.class, "vehiculeId")
            .build());
        
        // Business name search
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByBusinessNameContainingIgnoreCase")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(String.class, "businessName")
            .build());
        
        // Complex query with @Query annotation
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findActiveFreelancers")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.data.jpa.repository", "Query"))
                .addMember("value", "$S", "SELECT u FROM User u WHERE u.accountType = 'FREELANCE' AND u.isVerified = true")
                .build())
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .build());
    }
    
    private void addColisQueries(TypeSpec.Builder repositoryBuilder, ClassName entityClass) {
        // Status-specific queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByStatusAndCreatedBy")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(String.class, "status")
            .addParameter(UUID.class, "createdBy")
            .build());
        
        // Delivery queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findDeliveredWithPhoto")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.data.jpa.repository", "Query"))
                .addMember("value", "$S", "SELECT c FROM Colis c WHERE c.status = 'LIVRE' AND c.photoPreuveLivraisonUrl IS NOT NULL")
                .build())
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .build());
        
        // Pickup queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByRetirantNomContaining")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(String.class, "retirantNom")
            .build());
        
        // Date range queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByCreatedAtBetween")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(ClassName.get("java.time", "Instant"), "startDate")
            .addParameter(ClassName.get("java.time", "Instant"), "endDate")
            .build());
    }
    
    private void addStatusQueries(TypeSpec.Builder repositoryBuilder, ClassName entityClass) {
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByStatus")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(String.class, "status")
            .build());
        
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByStatusIn")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(ParameterizedTypeName.get(ClassName.get("java.util", "List"), ClassName.get(String.class)), "statuses")
            .build());
        
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("countByStatus")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(long.class)
            .addParameter(String.class, "status")
            .build());
    }
    
    private void addAuditQueries(TypeSpec.Builder repositoryBuilder, ClassName entityClass) {
        // Created by queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByCreatedBy")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(UUID.class, "createdBy")
            .build());
        
        // Updated by queries
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByUpdatedBy")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(UUID.class, "updatedBy")
            .build());
        
        // Version queries for optimistic locking
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByIdAndVersion")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "Optional"), entityClass))
            .addParameter(UUID.class, "id")
            .addParameter(Integer.class, "version")
            .build());
        
        // Verification status
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findByIsVerified")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addParameter(Boolean.class, "isVerified")
            .build());
        
        // Complex audit query
        repositoryBuilder.addMethod(MethodSpec.methodBuilder("findRecentlyModified")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.data.jpa.repository", "Query"))
                .addMember("value", "$S", "SELECT e FROM #{#entityName} e WHERE e.updatedBy IS NOT NULL ORDER BY e.version DESC")
                .build())
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .build());
    }
    
    private boolean isUserEntity(ClassModel model) {
        return model.getName().equals("User") || model.getName().contains("User");
    }
    
    private boolean isColisEntity(ClassModel model) {
        return model.getName().equals("Colis");
    }
    
    private boolean hasStatusField(ClassModel model) {
        return model.getFields().stream()
            .anyMatch(field -> field.getName().toLowerCase().contains("status"));
    }
}
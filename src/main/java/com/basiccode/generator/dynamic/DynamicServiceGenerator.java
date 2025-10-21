package com.basiccode.generator.dynamic;

import com.basiccode.generator.model.ClassModel;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;

public class DynamicServiceGenerator {
    
    public JavaFile generateDynamicService(ClassModel model, String basePackage) {
        String serviceName = model.getName() + "Service";
        String repositoryName = model.getName() + "Repository";
        
        TypeSpec.Builder serviceBuilder = TypeSpec.classBuilder(serviceName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.stereotype", "Service"))
            .addAnnotation(ClassName.get("org.springframework.transaction.annotation", "Transactional"));
        
        // Repository field
        serviceBuilder.addField(FieldSpec.builder(
                ClassName.get(basePackage + ".repository", repositoryName), 
                "repository", 
                Modifier.PRIVATE, Modifier.FINAL)
            .build());
        
        // Constructor
        serviceBuilder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get(basePackage + ".repository", repositoryName), "repository")
            .addStatement("this.repository = repository")
            .build());
        
        // Dynamic methods based on entity type
        addDynamicMethods(serviceBuilder, model, basePackage);
        
        return JavaFile.builder(basePackage + ".service", serviceBuilder.build())
            .addFileComment("Generated Dynamic Service - Business logic layer")
            .build();
    }
    
    private void addDynamicMethods(TypeSpec.Builder serviceBuilder, ClassModel model, String basePackage) {
        String entityName = model.getName();
        ClassName entityClass = ClassName.get(basePackage + ".entity", entityName);
        
        // Standard CRUD
        addCrudMethods(serviceBuilder, entityClass, entityName);
        
        // Entity-specific methods
        if (isUserEntity(model)) {
            addUserMethods(serviceBuilder, entityClass);
        } else if (isColisEntity(model)) {
            addColisMethods(serviceBuilder, entityClass);
        }
        
        // Status management if applicable
        if (hasStatusField(model)) {
            addStatusMethods(serviceBuilder, entityClass, entityName);
        }
    }
    
    private void addCrudMethods(TypeSpec.Builder serviceBuilder, ClassName entityClass, String entityName) {
        // Create with audit
        serviceBuilder.addMethod(MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(entityClass, "entity")
            .addParameter(UUID.class, "createdBy")
            .returns(entityClass)
            .addStatement("entity.setCreatedBy(createdBy)")
            .addStatement("entity.addHistoryEntry($S, createdBy, $S)", "CREATED", "Entity created")
            .addStatement("return repository.save(entity)")
            .build());
        
        // Update with audit
        serviceBuilder.addMethod(MethodSpec.methodBuilder("update")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(entityClass, "entity")
            .addParameter(UUID.class, "updatedBy")
            .returns(entityClass)
            .addStatement("entity.setUpdatedBy(updatedBy)")
            .addStatement("entity.addHistoryEntry($S, updatedBy, $S)", "UPDATED", "Entity updated")
            .addStatement("return repository.save(entity)")
            .build());
    }
    
    private void addUserMethods(TypeSpec.Builder serviceBuilder, ClassName entityClass) {
        // Account type specific methods
        serviceBuilder.addMethod(MethodSpec.methodBuilder("promoteToFreelance")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "userId")
            .addParameter(String.class, "businessName")
            .addParameter(UUID.class, "promotedBy")
            .returns(entityClass)
            .addStatement("$T user = repository.findById(userId).orElseThrow()", entityClass)
            .addStatement("user.setAccountType($S)", "FREELANCE")
            .addStatement("user.setBusinessName(businessName)")
            .addStatement("user.addHistoryEntry($S, promotedBy, $S)", "PROMOTED", "Promoted to freelance")
            .addStatement("return repository.save(user)")
            .build());
        
        serviceBuilder.addMethod(MethodSpec.methodBuilder("assignToAgence")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "userId")
            .addParameter(UUID.class, "agenceId")
            .addParameter(UUID.class, "assignedBy")
            .returns(entityClass)
            .addStatement("$T user = repository.findById(userId).orElseThrow()", entityClass)
            .addStatement("user.setAccountType($S)", "EMPLOYEE")
            .addStatement("user.setAgenceId(agenceId)")
            .addStatement("user.addHistoryEntry($S, assignedBy, $S)", "ASSIGNED", "Assigned to agence")
            .addStatement("return repository.save(user)")
            .build());
    }
    
    private void addColisMethods(TypeSpec.Builder serviceBuilder, ClassName entityClass) {
        // Colis lifecycle methods
        serviceBuilder.addMethod(MethodSpec.methodBuilder("marquerLivre")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "colisId")
            .addParameter(String.class, "photoUrl")
            .addParameter(String.class, "signatureUrl")
            .addParameter(UUID.class, "livreurId")
            .returns(entityClass)
            .addStatement("$T colis = repository.findById(colisId).orElseThrow()", entityClass)
            .addStatement("if (!colis.canTransitionTo($S)) throw new IllegalStateException($S)", "LIVRE", "Cannot mark as delivered")
            .addStatement("colis.setStatus($S)", "LIVRE")
            .addStatement("colis.setPhotoPreuveLivraisonUrl(photoUrl)")
            .addStatement("colis.setSignatureDestinataireUrl(signatureUrl)")
            .addStatement("colis.addHistoryEntry($S, livreurId, $S)", "DELIVERED", "Package delivered")
            .addStatement("return repository.save(colis)")
            .build());
        
        serviceBuilder.addMethod(MethodSpec.methodBuilder("marquerRetire")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "colisId")
            .addParameter(String.class, "retirantNom")
            .addParameter(String.class, "signatureUrl")
            .addParameter(UUID.class, "agentId")
            .returns(entityClass)
            .addStatement("$T colis = repository.findById(colisId).orElseThrow()", entityClass)
            .addStatement("colis.setStatus($S)", "RETIRE")
            .addStatement("colis.setRetirantNom(retirantNom)")
            .addStatement("colis.setSignatureDestinataireUrl(signatureUrl)")
            .addStatement("colis.addHistoryEntry($S, agentId, $S)", "PICKED_UP", "Package picked up")
            .addStatement("return repository.save(colis)")
            .build());
    }
    
    private void addStatusMethods(TypeSpec.Builder serviceBuilder, ClassName entityClass, String entityName) {
        serviceBuilder.addMethod(MethodSpec.methodBuilder("updateStatus")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "entityId")
            .addParameter(String.class, "newStatus")
            .addParameter(UUID.class, "updatedBy")
            .addParameter(String.class, "comment")
            .returns(entityClass)
            .addStatement("$T entity = repository.findById(entityId).orElseThrow()", entityClass)
            .addStatement("if (!entity.canTransitionTo(newStatus)) throw new IllegalStateException($S)", "Invalid status transition")
            .addStatement("entity.updateStatus(newStatus, updatedBy, comment)")
            .addStatement("return repository.save(entity)")
            .build());
        
        serviceBuilder.addMethod(MethodSpec.methodBuilder("getByStatus")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.transaction.annotation", "Transactional"))
                .addMember("readOnly", "true")
                .build())
            .addParameter(String.class, "status")
            .returns(ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
            .addStatement("return repository.findByStatus(status)")
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
package com.basiccode.generator.dynamic;

import com.basiccode.generator.model.ClassModel;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;

public class DynamicControllerGenerator {
    
    public JavaFile generateDynamicController(ClassModel model, String basePackage) {
        String controllerName = model.getName() + "Controller";
        String serviceName = model.getName() + "Service";
        ClassName entityClass = ClassName.get(basePackage + ".entity", model.getName());
        ClassName serviceClass = ClassName.get(basePackage + ".service", serviceName);
        
        TypeSpec.Builder controllerBuilder = TypeSpec.classBuilder(controllerName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RestController"))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestMapping"))
                .addMember("value", "$S", "/api/" + model.getName().toLowerCase() + "s")
                .build());
        
        // Service field
        controllerBuilder.addField(FieldSpec.builder(serviceClass, "service", Modifier.PRIVATE, Modifier.FINAL)
            .build());
        
        // Constructor
        controllerBuilder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(serviceClass, "service")
            .addStatement("this.service = service")
            .build());
        
        // Standard CRUD endpoints
        addCrudEndpoints(controllerBuilder, entityClass, model);
        
        // Dynamic endpoints based on entity type
        addDynamicEndpoints(controllerBuilder, entityClass, model);
        
        return JavaFile.builder(basePackage + ".controller", controllerBuilder.build())
            .addFileComment("Generated Dynamic Controller - REST API layer")
            .build();
    }
    
    private void addCrudEndpoints(TypeSpec.Builder controllerBuilder, ClassName entityClass, ClassModel model) {
        // Create with audit
        controllerBuilder.addMethod(MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
            .addParameter(ParameterSpec.builder(entityClass, "entity")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestBody"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "createdBy")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T created = service.create(entity, createdBy)", entityClass)
            .addStatement("return $T.ok(created)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
        
        // Update with audit
        controllerBuilder.addMethod(MethodSpec.methodBuilder("update")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PutMapping"))
                .addMember("value", "$S", "/{id}")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "id")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(entityClass, "entity")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestBody"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "updatedBy")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("entity.setId(id)")
            .addStatement("$T updated = service.update(entity, updatedBy)", entityClass)
            .addStatement("return $T.ok(updated)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
        
        // Get by status if applicable
        if (hasStatusField(model)) {
            controllerBuilder.addMethod(MethodSpec.methodBuilder("getByStatus")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "GetMapping"))
                    .addMember("value", "$S", "/status/{status}")
                    .build())
                .addParameter(ParameterSpec.builder(String.class, "status")
                    .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                    .build())
                .returns(ParameterizedTypeName.get(
                    ClassName.get("org.springframework.http", "ResponseEntity"),
                    ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass)))
                .addStatement("$T entities = service.getByStatus(status)", 
                    ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass))
                .addStatement("return $T.ok(entities)", ClassName.get("org.springframework.http", "ResponseEntity"))
                .build());
        }
    }
    
    private void addDynamicEndpoints(TypeSpec.Builder controllerBuilder, ClassName entityClass, ClassModel model) {
        if (isUserEntity(model)) {
            addUserEndpoints(controllerBuilder, entityClass);
        } else if (isColisEntity(model)) {
            addColisEndpoints(controllerBuilder, entityClass);
        }
        
        // Status management endpoints
        if (hasStatusField(model)) {
            addStatusEndpoints(controllerBuilder, entityClass);
        }
    }
    
    private void addUserEndpoints(TypeSpec.Builder controllerBuilder, ClassName entityClass) {
        // Promote to freelance
        controllerBuilder.addMethod(MethodSpec.methodBuilder("promoteToFreelance")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
                .addMember("value", "$S", "/{userId}/promote-freelance")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "userId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "businessName")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "promotedBy")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T user = service.promoteToFreelance(userId, businessName, promotedBy)", entityClass)
            .addStatement("return $T.ok(user)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
        
        // Assign to agence
        controllerBuilder.addMethod(MethodSpec.methodBuilder("assignToAgence")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
                .addMember("value", "$S", "/{userId}/assign-agence")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "userId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "agenceId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "assignedBy")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T user = service.assignToAgence(userId, agenceId, assignedBy)", entityClass)
            .addStatement("return $T.ok(user)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
        
        // Get by account type
        controllerBuilder.addMethod(MethodSpec.methodBuilder("getByAccountType")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "GetMapping"))
                .addMember("value", "$S", "/account-type/{accountType}")
                .build())
            .addParameter(ParameterSpec.builder(String.class, "accountType")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"),
                ParameterizedTypeName.get(ClassName.get("java.util", "List"), entityClass)))
            .addStatement("// TODO: Implement getByAccountType in service")
            .addStatement("return $T.ok(new $T<>())", 
                ClassName.get("org.springframework.http", "ResponseEntity"),
                ClassName.get("java.util", "ArrayList"))
            .build());
    }
    
    private void addColisEndpoints(TypeSpec.Builder controllerBuilder, ClassName entityClass) {
        // Mark as delivered
        controllerBuilder.addMethod(MethodSpec.methodBuilder("marquerLivre")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
                .addMember("value", "$S", "/{colisId}/livrer")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "colisId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "photoUrl")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "signatureUrl")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "livreurId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T colis = service.marquerLivre(colisId, photoUrl, signatureUrl, livreurId)", entityClass)
            .addStatement("return $T.ok(colis)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
        
        // Mark as picked up
        controllerBuilder.addMethod(MethodSpec.methodBuilder("marquerRetire")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
                .addMember("value", "$S", "/{colisId}/retirer")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "colisId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "retirantNom")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "signatureUrl")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "agentId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T colis = service.marquerRetire(colisId, retirantNom, signatureUrl, agentId)", entityClass)
            .addStatement("return $T.ok(colis)", ClassName.get("org.springframework.http", "ResponseEntity"))
            .build());
    }
    
    private void addStatusEndpoints(TypeSpec.Builder controllerBuilder, ClassName entityClass) {
        // Update status
        controllerBuilder.addMethod(MethodSpec.methodBuilder("updateStatus")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PutMapping"))
                .addMember("value", "$S", "/{entityId}/status")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "entityId")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "newStatus")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                .build())
            .addParameter(ParameterSpec.builder(String.class, "comment")
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestParam"))
                    .addMember("required", "false")
                    .build())
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "updatedBy")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestHeader"))
                .build())
            .returns(ParameterizedTypeName.get(
                ClassName.get("org.springframework.http", "ResponseEntity"), entityClass))
            .addStatement("$T entity = service.updateStatus(entityId, newStatus, updatedBy, comment)", entityClass)
            .addStatement("return $T.ok(entity)", ClassName.get("org.springframework.http", "ResponseEntity"))
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
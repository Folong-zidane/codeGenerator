package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.basiccode.generator.model.Relationship;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.UUID;

public class CompleteEntityGenerator {
    
    public JavaFile generateCompleteEntity(ClassModel model, String basePackage, List<Relationship> relationships) {
        TypeSpec.Builder entityBuilder = TypeSpec.classBuilder(model.getName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(createEntityAnnotation())
            .addAnnotation(createTableAnnotation(model.getName()));
        
        // Ajouter héritage JPA si applicable
        InheritanceGenerator.addInheritanceAnnotations(entityBuilder, model);
        InheritanceGenerator.addParentClass(entityBuilder, model);
        
        // Champs de base
        if (!hasParentClass(model)) {
            addIdField(entityBuilder);
        }
        
        // Champs du modèle avec énumérations
        for (Field field : model.getFields()) {
            if (!field.getName().equals("id")) {
                entityBuilder.addField(generateEnhancedField(field));
            }
        }
        
        // Relations JPA
        List<FieldSpec> relationshipFields = RelationshipGenerator.generateRelationshipFields(model, relationships);
        relationshipFields.forEach(entityBuilder::addField);
        
        // Méthodes métier
        List<MethodSpec> businessMethods = BusinessMethodGenerator.generateBusinessMethods(model);
        businessMethods.forEach(entityBuilder::addMethod);
        
        // Audit fields et constructeurs
        if (!hasParentClass(model)) {
            addAuditFields(entityBuilder);
        }
        addConstructors(entityBuilder, model);
        addAccessors(entityBuilder, model);
        addUtilityMethods(entityBuilder, model);
        
        return JavaFile.builder(basePackage + ".entity", entityBuilder.build())
            .addFileComment("Generated Complete Entity with Business Logic, Relations and JPA Mapping")
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
        
        // Annotations JPA
        AnnotationSpec.Builder columnBuilder = AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
            .addMember("name", "$S", toSnakeCase(field.getName()))
            .addMember("nullable", "$L", field.isNullable());
        
        // Traitement spécial pour les énumérations
        if (isEnumType(field.getType())) {
            builder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Enumerated"))
                .addMember("value", "jakarta.persistence.EnumType.STRING")
                .build());
        }
        
        // Contraintes de validation
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
        return builder.build();
    }
    
    private void addAuditFields(TypeSpec.Builder builder) {
        builder.addField(FieldSpec.builder(ClassName.get("java.time", "LocalDateTime"), "createdAt", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "created_at")
                .addMember("nullable", "false")
                .addMember("updatable", "false")
                .build())
            .build());
        
        builder.addField(FieldSpec.builder(ClassName.get("java.time", "LocalDateTime"), "updatedAt", Modifier.PRIVATE)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.persistence", "Column"))
                .addMember("name", "$S", "updated_at")
                .addMember("nullable", "false")
                .build())
            .build());
    }
    
    private void addConstructors(TypeSpec.Builder builder, ClassModel model) {
        builder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("this.createdAt = java.time.LocalDateTime.now()")
            .addStatement("this.updatedAt = java.time.LocalDateTime.now()")
            .build());
    }
    
    private void addAccessors(TypeSpec.Builder builder, ClassModel model) {
        // Getters/Setters pour ID
        builder.addMethod(MethodSpec.methodBuilder("getId")
            .addModifiers(Modifier.PUBLIC)
            .returns(UUID.class)
            .addStatement("return id")
            .build());
        
        // Getters/Setters pour tous les champs
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
                    .addStatement("this.updatedAt = java.time.LocalDateTime.now()")
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
    
    private boolean hasParentClass(ClassModel model) {
        String className = model.getName();
        return className.equals("Client") || className.equals("Freelance") || 
               className.equals("Employee") || className.equals("AgencyOwner") ||
               className.equals("Deliverer");
    }
    
    private boolean isEnumType(String type) {
        return type.endsWith("Type") || type.endsWith("Status") || type.endsWith("Mode") || 
               type.endsWith("Option") || type.equals("QoS") || type.equals("ActorType") ||
               type.equals("StorageCapacity") || type.equals("VehicleType") || 
               type.equals("PackageType") || type.equals("DeliveryStatus") ||
               type.equals("ShipmentStatus") || type.equals("NotificationType") ||
               type.equals("NotificationStatus") || type.equals("DeliveryOption") ||
               type.equals("PaymentMode") || type.equals("AccountType");
    }
    
    private TypeName getJavaType(String type) {
        return switch (type) {
            case "String" -> ClassName.get(String.class);
            case "UUID" -> ClassName.get(UUID.class);
            case "Boolean" -> ClassName.get(Boolean.class);
            case "Integer", "Int" -> ClassName.get(Integer.class);
            case "Float" -> ClassName.get(Float.class);
            case "Date" -> ClassName.get("java.time", "LocalDateTime");
            case "JSON" -> ClassName.get(String.class);
            default -> {
                if (isEnumType(type)) {
                    yield ClassName.get(String.class); // Pour l'instant, traiter comme String
                }
                yield ClassName.get(String.class);
            }
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
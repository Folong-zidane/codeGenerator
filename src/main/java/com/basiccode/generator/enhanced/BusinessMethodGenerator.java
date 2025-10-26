package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Method;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

public class BusinessMethodGenerator {
    
    public static List<MethodSpec> generateBusinessMethods(ClassModel classModel) {
        return classModel.getMethods().stream()
            .map(BusinessMethodGenerator::generateMethod)
            .toList();
    }
    
    private static MethodSpec generateMethod(Method method) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName())
            .addModifiers(Modifier.PUBLIC);
        
        // Ajouter paramètres
        method.getParameters().forEach(param -> 
            methodBuilder.addParameter(getJavaType(param.getType()), param.getName())
        );
        
        // Type de retour
        if (!method.getReturnType().equals("void")) {
            methodBuilder.returns(getJavaType(method.getReturnType()));
        }
        
        // Générer corps de méthode selon le pattern
        String methodBody = generateMethodBody(method);
        methodBuilder.addCode(methodBody);
        
        return methodBuilder.build();
    }
    
    private static String generateMethodBody(Method method) {
        String methodName = method.getName();
        
        // Patterns de méthodes métier
        if (methodName.startsWith("update")) {
            return generateUpdateMethod(method);
        } else if (methodName.startsWith("calculate")) {
            return generateCalculationMethod(method);
        } else if (methodName.startsWith("validate") || methodName.startsWith("can")) {
            return generateValidationMethod(method);
        } else if (methodName.contains("Status")) {
            return generateStatusMethod(method);
        } else if (methodName.startsWith("generate")) {
            return generateGenerationMethod(method);
        }
        
        return generateDefaultMethod(method);
    }
    
    private static String generateUpdateMethod(Method method) {
        return """
            // TODO: Implement update logic
            this.updatedAt = java.time.LocalDateTime.now();
            // Add validation logic here
            // Save changes to repository
            throw new UnsupportedOperationException("Method not implemented: %s");
            """.formatted(method.getName());
    }
    
    private static String generateCalculationMethod(Method method) {
        if (method.getName().contains("Fee")) {
            return """
                // TODO: Implement fee calculation
                java.math.BigDecimal baseFee = java.math.BigDecimal.valueOf(5.00);
                // Add distance, weight, QoS factors
                return baseFee;
                """;
        }
        return """
            // TODO: Implement calculation logic for %s
            throw new UnsupportedOperationException("Method not implemented");
            """.formatted(method.getName());
    }
    
    private static String generateValidationMethod(Method method) {
        return """
            // TODO: Implement validation logic
            // Add business rules validation
            return true; // Placeholder
            """;
    }
    
    private static String generateStatusMethod(Method method) {
        return """
            // TODO: Implement status transition logic
            // Validate transition rules
            // Update status and publish event
            this.status = newStatus;
            this.updatedAt = java.time.LocalDateTime.now();
            """;
    }
    
    private static String generateGenerationMethod(Method method) {
        if (method.getName().contains("Tracking")) {
            return """
                // Generate unique tracking number
                return "TRK" + java.time.Instant.now().toEpochMilli() + 
                       java.util.concurrent.ThreadLocalRandom.current().nextInt(1000, 9999);
                """;
        }
        return """
            // TODO: Implement generation logic for %s
            return java.util.UUID.randomUUID().toString();
            """.formatted(method.getName());
    }
    
    private static String generateDefaultMethod(Method method) {
        return """
            // TODO: Implement business logic for %s
            throw new UnsupportedOperationException("Method not implemented: %s");
            """.formatted(method.getName(), method.getName());
    }
    
    private static TypeName getJavaType(String type) {
        return switch (type.toLowerCase()) {
            case "string" -> ClassName.get(String.class);
            case "uuid" -> ClassName.get("java.util", "UUID");
            case "date" -> ClassName.get("java.time", "LocalDateTime");
            case "boolean" -> TypeName.BOOLEAN;
            case "int", "integer" -> TypeName.INT;
            case "float" -> TypeName.FLOAT;
            case "bigdecimal" -> ClassName.get("java.math", "BigDecimal");
            default -> ClassName.get("java.lang", "Object");
        };
    }
}
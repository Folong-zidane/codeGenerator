package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;

public class EnumGenerator {
    
    public JavaFile generateEnum(ClassModel model, String basePackage) {
        if (!isEnumType(model)) {
            return null; // Pas une énumération
        }
        
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(model.getName())
            .addModifiers(Modifier.PUBLIC);
        
        // Ajouter les valeurs d'énumération détectées
        for (String value : getEnumValues(model)) {
            enumBuilder.addEnumConstant(value);
        }
        
        return JavaFile.builder(basePackage + ".enums", enumBuilder.build())
            .addFileComment("Generated Enumeration")
            .build();
    }
    
    private boolean isEnumType(ClassModel model) {
        String name = model.getName();
        return name.endsWith("Type") || name.endsWith("Status") || 
               name.endsWith("Mode") || name.endsWith("Option") ||
               name.equals("QoS") || model.isEnumeration();
    }
    
    private Iterable<String> getEnumValues(ClassModel model) {
        String name = model.getName();
        
        // Valeurs par défaut basées sur le type
        return switch (name) {
            case "AccountType" -> java.util.List.of("CLIENT", "FREELANCE", "EMPLOYEE", "AGENCY_OWNER", "DELIVERER");
            case "PackageStatus" -> java.util.List.of("PRE_REGISTERED", "REGISTERED", "IN_TRANSIT", "AT_RELAY_POINT", "DELIVERED", "CANCELLED");
            case "DeliveryStatus" -> java.util.List.of("AVAILABLE", "ON_DELIVERY", "ON_BREAK", "OFFLINE");
            case "VehicleType" -> java.util.List.of("BICYCLE", "MOTORCYCLE", "CAR", "VAN", "TRUCK");
            case "PaymentMode" -> java.util.List.of("CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CREDIT_CARD");
            default -> java.util.List.of("VALUE_1", "VALUE_2", "VALUE_3");
        };
    }
}
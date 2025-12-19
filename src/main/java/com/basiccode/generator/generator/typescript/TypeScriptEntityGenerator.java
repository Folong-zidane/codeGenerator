package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptEntityGenerator implements IEntityGenerator {
    
    @Override
    public String getEntityDirectory() {
        return "entities";
    }
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Imports
        code.append("import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';\n");
        code.append("import { IsNotEmpty, IsEmail, IsOptional } from 'class-validator';\n\n");
        
        // Entity decorator
        code.append("@Entity('").append(className.toLowerCase()).append("')\n");
        code.append("export class ").append(className).append(" {\n");
        
        // Primary key
        code.append("  @PrimaryGeneratedColumn('uuid')\n");
        code.append("  id: string;\n\n");
        
        // Generate properties from attributes
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (var attr : enhancedClass.getOriginalClass().getAttributes()) {
                if (!"id".equalsIgnoreCase(attr.getName())) {
                    code.append(generateTypeScriptProperty(attr));
                }
            }
        }
        
        // Audit fields
        code.append("  @CreateDateColumn()\n");
        code.append("  createdAt: Date;\n\n");
        code.append("  @UpdateDateColumn()\n");
        code.append("  updatedAt: Date;\n\n");
        
        // Business methods
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                code.append(generateTypeScriptMethod(method));
            }
        }
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        if (!enhancedClass.isStateful()) return "";
        
        StringBuilder code = new StringBuilder();
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        code.append("export enum ").append(enumName).append(" {\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (var value : enhancedClass.getStateEnum().getValues()) {
                code.append("  ").append(value.getName()).append(" = '").append(value.getName()).append("',\n");
            }
        } else {
            code.append("  ACTIVE = 'ACTIVE',\n");
            code.append("  INACTIVE = 'INACTIVE',\n");
            code.append("  PENDING = 'PENDING',\n");
        }
        
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateTypeScriptProperty(com.basiccode.generator.model.UMLAttribute attr) {
        StringBuilder property = new StringBuilder();
        
        String propertyName = attr.getName();
        String tsType = mapToTypeScriptType(attr.getType());
        
        // Validation decorators
        property.append(generateValidationDecorators(attr));
        
        // Column decorator
        property.append("  @Column()");
        if ("string".equals(tsType)) {
            property.append("\n");
        } else {
            property.append("\n");
        }
        
        // Property declaration
        property.append("  ").append(propertyName).append(": ").append(tsType).append(";\n\n");
        
        return property.toString();
    }
    
    private String generateValidationDecorators(com.basiccode.generator.model.UMLAttribute attr) {
        StringBuilder validation = new StringBuilder();
        String name = attr.getName().toLowerCase();
        String type = attr.getType();
        
        if ("string".equalsIgnoreCase(type)) {
            if (name.contains("email")) {
                validation.append("  @IsEmail()\n");
                validation.append("  @IsNotEmpty()\n");
            } else if (name.contains("name") || name.contains("title")) {
                validation.append("  @IsNotEmpty()\n");
            } else {
                validation.append("  @IsOptional()\n");
            }
        }
        
        return validation.toString();
    }
    
    private String generateTypeScriptMethod(com.basiccode.generator.model.UMLMethod method) {
        StringBuilder methodCode = new StringBuilder();
        String methodName = method.getName();
        String returnType = mapToTypeScriptType(method.getReturnType());
        
        methodCode.append("  ").append(methodName).append("(");
        
        // Parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) methodCode.append(", ");
                var param = method.getParameters().get(i);
                methodCode.append(param.getName()).append(": ").append(mapToTypeScriptType(param.getType()));
            }
        }
        
        methodCode.append("): ").append(returnType).append(" {\n");
        methodCode.append("    // TODO: Implement ").append(methodName).append("\n");
        
        if (!"void".equals(returnType)) {
            if ("boolean".equals(returnType)) {
                methodCode.append("    return false;\n");
            } else if ("string".equals(returnType)) {
                methodCode.append("    return '';\n");
            } else if ("number".equals(returnType)) {
                methodCode.append("    return 0;\n");
            } else {
                methodCode.append("    return null;\n");
            }
        }
        
        methodCode.append("  }\n\n");
        
        return methodCode.toString();
    }
    
    private String mapToTypeScriptType(String javaType) {
        if (javaType == null) return "any";
        
        switch (javaType.toLowerCase()) {
            case "string":
            case "str":
                return "string";
            case "integer":
            case "int":
            case "long":
            case "float":
            case "double":
            case "bigdecimal":
            case "decimal":
                return "number";
            case "boolean":
            case "bool":
                return "boolean";
            case "localdatetime":
            case "datetime":
            case "date":
                return "Date";
            case "void":
                return "void";
            default:
                return "any";
        }
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
}
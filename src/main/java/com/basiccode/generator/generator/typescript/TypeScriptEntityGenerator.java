package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

public class TypeScriptEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, OneToMany, ManyToOne, OneToOne, ManyToMany, JoinColumn, JoinTable } from 'typeorm';\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("import { ").append(enumName).append(" } from '../enums/").append(enumName).append("';\n");
        }
        
        code.append("\n@Entity('").append(className.toLowerCase()).append("s')\n");
        code.append("export class ").append(className).append(" {\n");
        
        // Generate properties
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            // Generate TypeORM relationship or column
            if (attr.isRelationship()) {
                generateTypeScriptRelationship(code, attr, className);
            } else {
                if ("id".equalsIgnoreCase(attr.getName())) {
                    code.append("  @PrimaryGeneratedColumn()\n");
                } else {
                    code.append("  @Column()\n");
                }
                code.append("  ").append(attr.getName()).append(": ").append(mapType(attr.getType())).append(";\n\n");
            }
        }
        
        // Add state property if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("  @Column({ type: 'enum', enum: ").append(enumName).append(" })\n");
            code.append("  status: ").append(enumName).append(";\n\n");
        }
        
        // Add audit fields
        code.append("  @CreateDateColumn()\n");
        code.append("  createdAt: Date;\n\n");
        
        code.append("  @UpdateDateColumn()\n");
        code.append("  updatedAt: Date;\n\n");
        
        // Add business methods from UML diagram
        generateBusinessMethods(code, enhancedClass, className);
        
        // Add state transition methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateTransitionMethods(code, enhancedClass);
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
            for (int i = 0; i < enhancedClass.getStateEnum().getValues().size(); i++) {
                var value = enhancedClass.getStateEnum().getValues().get(i);
                code.append("  ").append(value.getName()).append(" = '").append(value.getName()).append("'");
                if (i < enhancedClass.getStateEnum().getValues().size() - 1) {
                    code.append(",");
                }
                code.append("\n");
            }
        } else {
            code.append("  ACTIVE = 'ACTIVE',\n");
            code.append("  INACTIVE = 'INACTIVE',\n");
            code.append("  SUSPENDED = 'SUSPENDED'\n");
        }
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
    
    @Override
    public String getEntityDirectory() {
        return "entities";
    }
    
    private String mapType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long", "integer", "int", "float", "double" -> "number";
            case "boolean" -> "boolean";
            case "date", "datetime" -> "Date";
            default -> "string";
        };
    }
    
    private void generateStateTransitionMethods(StringBuilder code, EnhancedClass enhancedClass) {
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        // Suspend method
        code.append("  suspend(): void {\n");
        code.append("    if (this.status !== ").append(enumName).append(".ACTIVE) {\n");
        code.append("      throw new Error(`Cannot suspend entity in state: ${this.status}`);\n");
        code.append("    }\n");
        code.append("    this.status = ").append(enumName).append(".SUSPENDED;\n");
        code.append("    this.updatedAt = new Date();\n");
        code.append("  }\n\n");
        
        // Activate method
        code.append("  activate(): void {\n");
        code.append("    if (this.status !== ").append(enumName).append(".SUSPENDED) {\n");
        code.append("      throw new Error(`Cannot activate entity in state: ${this.status}`);\n");
        code.append("    }\n");
        code.append("    this.status = ").append(enumName).append(".ACTIVE;\n");
        code.append("    this.updatedAt = new Date();\n");
        code.append("  }\n\n");
    }
    
    private void generateBusinessMethods(StringBuilder code, EnhancedClass enhancedClass, String className) {
        // Generate methods from UML diagram
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                generateBusinessMethod(code, method, className);
            }
        }
    }
    
    private void generateBusinessMethod(StringBuilder code, com.basiccode.generator.model.Method method, String className) {
        String returnType = method.getReturnType() != null ? mapType(method.getReturnType()) : "void";
        
        code.append("  ").append(method.getName()).append("(");
        
        // Add parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                var param = method.getParameters().get(i);
                String paramType = param.getType() != null ? mapType(param.getType()) : "string";
                code.append(param.getName()).append(": ").append(paramType);
                if (i < method.getParameters().size() - 1) {
                    code.append(", ");
                }
            }
        }
        
        code.append("): ").append(returnType).append(" {\n");
        
        // Generate method body based on method name
        generateTypeScriptMethodBody(code, method, className, returnType);
        
        code.append("  }\n\n");
    }
    
    private void generateTypeScriptMethodBody(StringBuilder code, com.basiccode.generator.model.Method method, String className, String returnType) {
        String methodName = method.getName().toLowerCase();
        
        switch (methodName) {
            case "authenticate":
                code.append("    if (!password || password.length === 0) {\n");
                code.append("      return false;\n");
                code.append("    }\n");
                code.append("    // TODO: Implement password verification logic\n");
                code.append("    return true;\n");
                break;
                
            case "updateprofile":
                code.append("    if (!profile) {\n");
                code.append("      throw new Error('Profile cannot be null');\n");
                code.append("    }\n");
                code.append("    // TODO: Update user profile fields\n");
                code.append("    this.updatedAt = new Date();\n");
                break;
                
            case "calculatetotal":
                code.append("    // TODO: Calculate order total\n");
                if (!"void".equals(returnType)) {
                    code.append("    return 0;\n");
                }
                break;
                
            default:
                code.append("    // TODO: Implement ").append(methodName).append(" logic\n");
                if (!"void".equals(returnType)) {
                    if ("boolean".equals(returnType)) {
                        code.append("    return false;\n");
                    } else if ("string".equals(returnType)) {
                        code.append("    return '';\n");
                    } else if ("number".equals(returnType)) {
                        code.append("    return 0;\n");
                    } else {
                        code.append("    return null;\n");
                    }
                }
                break;
        }
    }
    
    private void generateTypeScriptRelationship(StringBuilder code, UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        
        switch (relationshipType) {
            case "OneToMany":
                code.append("  @OneToMany(() => ").append(targetClass).append(", ").append(targetClass.toLowerCase()).append(" => ").append(targetClass.toLowerCase()).append(".").append(currentClassName.toLowerCase()).append(")\n");
                code.append("  ").append(attr.getName()).append(": ").append(targetClass).append("[];\n\n");
                break;
            case "ManyToOne":
                code.append("  @ManyToOne(() => ").append(targetClass).append(", ").append(targetClass.toLowerCase()).append(" => ").append(targetClass.toLowerCase()).append(".").append(attr.getName()).append("s)\n");
                code.append("  @JoinColumn({ name: '").append(targetClass.toLowerCase()).append("_id' })\n");
                code.append("  ").append(attr.getName()).append(": ").append(targetClass).append(";\n\n");
                break;
            case "OneToOne":
                code.append("  @OneToOne(() => ").append(targetClass).append(")\n");
                code.append("  @JoinColumn({ name: '").append(targetClass.toLowerCase()).append("_id' })\n");
                code.append("  ").append(attr.getName()).append(": ").append(targetClass).append(";\n\n");
                break;
            case "ManyToMany":
                code.append("  @ManyToMany(() => ").append(targetClass).append(", ").append(targetClass.toLowerCase()).append(" => ").append(targetClass.toLowerCase()).append(".").append(currentClassName.toLowerCase()).append("s)\n");
                code.append("  @JoinTable({\n");
                code.append("    name: '").append(currentClassName.toLowerCase()).append("_").append(targetClass.toLowerCase()).append("',\n");
                code.append("    joinColumn: { name: '").append(currentClassName.toLowerCase()).append("_id' },\n");
                code.append("    inverseJoinColumn: { name: '").append(targetClass.toLowerCase()).append("_id' }\n");
                code.append("  })\n");
                code.append("  ").append(attr.getName()).append(": ").append(targetClass).append("[];\n\n");
                break;
        }
    }
}
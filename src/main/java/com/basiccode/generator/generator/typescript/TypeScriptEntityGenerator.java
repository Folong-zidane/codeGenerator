package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

public class TypeScriptEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';\n");
        
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
            if ("id".equalsIgnoreCase(attr.getName())) {
                code.append("  @PrimaryGeneratedColumn()\n");
            } else {
                code.append("  @Column()\n");
            }
            code.append("  ").append(attr.getName()).append(": ").append(mapType(attr.getType())).append(";\n\n");
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
}
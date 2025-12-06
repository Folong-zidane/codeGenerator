package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.BusinessMethod;

public class TypeScriptServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("import { ").append(className).append(" } from '../entities/").append(className).append("';\n");
        code.append("import { ").append(className).append("Repository, I").append(className).append("Repository } from '../repositories/").append(className).append("Repository';\n\n");
        
        code.append("export interface I").append(className).append("Service {\n");
        code.append("  getAll(): Promise<").append(className).append("[]>;\n");
        code.append("  getById(id: number): Promise<").append(className).append(" | null>;\n");
        code.append("  create(data: Partial<").append(className).append(">): Promise<").append(className).append(">;\n");
        code.append("  update(id: number, data: Partial<").append(className).append(">): Promise<").append(className).append(" | null>;\n");
        code.append("  delete(id: number): Promise<void>;\n");
        
        if (enhancedClass.isStateful()) {
            code.append("  suspend").append(className).append("(id: number): Promise<").append(className).append(" | null>;\n");
            code.append("  activate").append(className).append("(id: number): Promise<").append(className).append(" | null>;\n");
        }
        
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("  ").append(method.getName()).append("(): Promise<").append(mapReturnType(method.getReturnType())).append(">;\n");
            }
        }
        
        code.append("}\n\n");
        
        code.append("export class ").append(className).append("Service implements I").append(className).append("Service {\n");
        code.append("  private repository: I").append(className).append("Repository;\n\n");
        
        code.append("  constructor() {\n");
        code.append("    this.repository = new ").append(className).append("Repository();\n");
        code.append("  }\n\n");
        
        code.append("  async getAll(): Promise<").append(className).append("[]> {\n");
        code.append("    return await this.repository.findAll();\n");
        code.append("  }\n\n");
        
        code.append("  async getById(id: number): Promise<").append(className).append(" | null> {\n");
        code.append("    return await this.repository.findById(id);\n");
        code.append("  }\n\n");
        
        code.append("  async create(data: Partial<").append(className).append(">): Promise<").append(className).append("> {\n");
        code.append("    this.validateEntity(data);\n");
        code.append("    return await this.repository.create(data);\n");
        code.append("  }\n\n");
        
        code.append("  async update(id: number, data: Partial<").append(className).append(">): Promise<").append(className).append(" | null> {\n");
        code.append("    const existing = await this.repository.findById(id);\n");
        code.append("    if (!existing) {\n");
        code.append("      throw new Error(`").append(className).append(" with id ${id} not found`);\n");
        code.append("    }\n");
        code.append("    \n");
        code.append("    this.validateEntity(data);\n");
        code.append("    return await this.repository.update(id, data);\n");
        code.append("  }\n\n");
        
        code.append("  async delete(id: number): Promise<void> {\n");
        code.append("    const exists = await this.repository.exists(id);\n");
        code.append("    if (!exists) {\n");
        code.append("      throw new Error(`").append(className).append(" with id ${id} not found`);\n");
        code.append("    }\n");
        code.append("    \n");
        code.append("    await this.repository.delete(id);\n");
        code.append("  }\n\n");
        
        if (enhancedClass.isStateful()) {
            generateStateManagementMethods(code, className);
        }
        
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("  async ").append(method.getName()).append("(): Promise<").append(mapReturnType(method.getReturnType())).append("> {\n");
                code.append("    // Generated from sequence diagram\n");
                for (String logic : method.getBusinessLogic()) {
                    code.append("    // ").append(logic).append("\n");
                }
                code.append("    \n");
                code.append("    throw new Error('Method not implemented');\n");
                code.append("  }\n\n");
            }
        }
        
        code.append("  private validateEntity(data: Partial<").append(className).append(">): void {\n");
        code.append("    if (!data) {\n");
        code.append("      throw new Error('Entity data is required');\n");
        code.append("    }\n");
        code.append("    // Add custom validation logic here\n");
        code.append("  }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String getServiceDirectory() {
        return "services";
    }
    
    public String getFileExtension() {
        return ".ts";
    }
    
    private void generateStateManagementMethods(StringBuilder code, String className) {
        code.append("  async suspend").append(className).append("(id: number): Promise<").append(className).append(" | null> {\n");
        code.append("    const entity = await this.repository.findById(id);\n");
        code.append("    if (!entity) {\n");
        code.append("      throw new Error(`").append(className).append(" with id ${id} not found`);\n");
        code.append("    }\n");
        code.append("    \n");
        code.append("    entity.suspend();\n");
        code.append("    return await this.repository.update(id, entity);\n");
        code.append("  }\n\n");
        
        code.append("  async activate").append(className).append("(id: number): Promise<").append(className).append(" | null> {\n");
        code.append("    const entity = await this.repository.findById(id);\n");
        code.append("    if (!entity) {\n");
        code.append("      throw new Error(`").append(className).append(" with id ${id} not found`);\n");
        code.append("    }\n");
        code.append("    \n");
        code.append("    entity.activate();\n");
        code.append("    return await this.repository.update(id, entity);\n");
        code.append("  }\n\n");
    }
    
    private String mapReturnType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "void" -> "void";
            case "boolean" -> "boolean";
            case "integer", "int", "long", "float", "double" -> "number";
            default -> "any";
        };
    }
}
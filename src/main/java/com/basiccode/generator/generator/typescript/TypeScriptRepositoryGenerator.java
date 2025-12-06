package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("import { Repository } from 'typeorm';\n");
        code.append("import { ").append(className).append(" } from '../entities/").append(className).append("';\n");
        code.append("import { AppDataSource } from '../data-source';\n\n");
        
        code.append("export interface I").append(className).append("Repository {\n");
        code.append("  findAll(): Promise<").append(className).append("[]>;\n");
        code.append("  findById(id: number): Promise<").append(className).append(" | null>;\n");
        code.append("  create(entity: Partial<").append(className).append(">): Promise<").append(className).append(">;\n");
        code.append("  update(id: number, entity: Partial<").append(className).append(">): Promise<").append(className).append(" | null>;\n");
        code.append("  delete(id: number): Promise<void>;\n");
        code.append("  exists(id: number): Promise<boolean>;\n");
        code.append("}\n\n");
        
        code.append("export class ").append(className).append("Repository implements I").append(className).append("Repository {\n");
        code.append("  private repository: Repository<").append(className).append(">;\n\n");
        
        code.append("  constructor() {\n");
        code.append("    this.repository = AppDataSource.getRepository(").append(className).append(");\n");
        code.append("  }\n\n");
        
        code.append("  async findAll(): Promise<").append(className).append("[]> {\n");
        code.append("    return await this.repository.find();\n");
        code.append("  }\n\n");
        
        code.append("  async findById(id: number): Promise<").append(className).append(" | null> {\n");
        code.append("    return await this.repository.findOne({ where: { id } });\n");
        code.append("  }\n\n");
        
        code.append("  async create(entity: Partial<").append(className).append(">): Promise<").append(className).append("> {\n");
        code.append("    const newEntity = this.repository.create(entity);\n");
        code.append("    return await this.repository.save(newEntity);\n");
        code.append("  }\n\n");
        
        code.append("  async update(id: number, entity: Partial<").append(className).append(">): Promise<").append(className).append(" | null> {\n");
        code.append("    await this.repository.update(id, entity);\n");
        code.append("    return await this.findById(id);\n");
        code.append("  }\n\n");
        
        code.append("  async delete(id: number): Promise<void> {\n");
        code.append("    await this.repository.delete(id);\n");
        code.append("  }\n\n");
        
        code.append("  async exists(id: number): Promise<boolean> {\n");
        code.append("    const count = await this.repository.count({ where: { id } });\n");
        code.append("    return count > 0;\n");
        code.append("  }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String getRepositoryDirectory() {
        return "repositories";
    }
    
    public String getFileExtension() {
        return ".ts";
    }
}
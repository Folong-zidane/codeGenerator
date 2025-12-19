package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptServiceGenerator implements IServiceGenerator {
    
    @Override
    public String getServiceDirectory() {
        return "services";
    }
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("import { Injectable } from '@nestjs/common';\n");
        code.append("import { InjectRepository } from '@nestjs/typeorm';\n");
        code.append("import { Repository } from 'typeorm';\n");
        code.append("import { ").append(className).append(" } from '../entities/").append(className.toLowerCase()).append(".entity';\n\n");
        
        code.append("@Injectable()\n");
        code.append("export class ").append(className).append("Service {\n");
        
        // Constructor
        code.append("  constructor(\n");
        code.append("    @InjectRepository(").append(className).append(")\n");
        code.append("    private readonly repository: Repository<").append(className).append(">\n");
        code.append("  ) {}\n\n");
        
        // Create method
        code.append("  async create(data: Partial<").append(className).append(">): Promise<").append(className).append("> {\n");
        code.append("    const entity = this.repository.create(data);\n");
        code.append("    return await this.repository.save(entity);\n");
        code.append("  }\n\n");
        
        // Find all
        code.append("  async findAll(): Promise<").append(className).append("[]> {\n");
        code.append("    return await this.repository.find();\n");
        code.append("  }\n\n");
        
        // Find by ID
        code.append("  async findById(id: string): Promise<").append(className).append(" | null> {\n");
        code.append("    return await this.repository.findOne({ where: { id } });\n");
        code.append("  }\n\n");
        
        // Update
        code.append("  async update(id: string, data: Partial<").append(className).append(">): Promise<").append(className).append(" | null> {\n");
        code.append("    await this.repository.update(id, data);\n");
        code.append("    return await this.findById(id);\n");
        code.append("  }\n\n");
        
        // Delete
        code.append("  async delete(id: string): Promise<boolean> {\n");
        code.append("    const result = await this.repository.delete(id);\n");
        code.append("    return result.affected > 0;\n");
        code.append("  }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".ts";
    }
}
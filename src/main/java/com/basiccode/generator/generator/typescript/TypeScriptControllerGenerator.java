package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class TypeScriptControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String lowerClassName = className.toLowerCase();
        
        code.append("import { Request, Response } from 'express';\n");
        code.append("import { ").append(className).append("Service, I").append(className).append("Service } from '../services/").append(className).append("Service';\n\n");
        
        code.append("export class ").append(className).append("Controller {\n");
        code.append("  private service: I").append(className).append("Service;\n\n");
        
        code.append("  constructor() {\n");
        code.append("    this.service = new ").append(className).append("Service();\n");
        code.append("  }\n\n");
        
        // GET all endpoint
        code.append("  /**\n");
        code.append("   * Get all ").append(lowerClassName).append("s\n");
        code.append("   */\n");
        code.append("  getAll = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const entities = await this.service.getAll();\n");
        code.append("      res.json(entities);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(500).json({ error: 'Internal server error', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // GET by ID endpoint
        code.append("  /**\n");
        code.append("   * Get ").append(lowerClassName).append(" by ID\n");
        code.append("   */\n");
        code.append("  getById = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const id = parseInt(req.params.id);\n");
        code.append("      const entity = await this.service.getById(id);\n");
        code.append("      \n");
        code.append("      if (!entity) {\n");
        code.append("        res.status(404).json({ error: '").append(className).append(" not found', id });\n");
        code.append("        return;\n");
        code.append("      }\n");
        code.append("      \n");
        code.append("      res.json(entity);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(500).json({ error: 'Internal server error', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // POST endpoint
        code.append("  /**\n");
        code.append("   * Create new ").append(lowerClassName).append("\n");
        code.append("   */\n");
        code.append("  create = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const entity = await this.service.create(req.body);\n");
        code.append("      res.status(201).json(entity);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(400).json({ error: 'Bad request', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // PUT endpoint
        code.append("  /**\n");
        code.append("   * Update ").append(lowerClassName).append(" by ID\n");
        code.append("   */\n");
        code.append("  update = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const id = parseInt(req.params.id);\n");
        code.append("      const entity = await this.service.update(id, req.body);\n");
        code.append("      \n");
        code.append("      if (!entity) {\n");
        code.append("        res.status(404).json({ error: '").append(className).append(" not found', id });\n");
        code.append("        return;\n");
        code.append("      }\n");
        code.append("      \n");
        code.append("      res.json(entity);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(400).json({ error: 'Bad request', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // DELETE endpoint
        code.append("  /**\n");
        code.append("   * Delete ").append(lowerClassName).append(" by ID\n");
        code.append("   */\n");
        code.append("  delete = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const id = parseInt(req.params.id);\n");
        code.append("      await this.service.delete(id);\n");
        code.append("      res.status(204).send();\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(404).json({ error: '").append(className).append(" not found', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // State management endpoints if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementEndpoints(code, className);
        }
        
        code.append("}\n");
        
        return code.toString();
    }
    
    public String getControllerDirectory() {
        return "controllers";
    }
    
    public String getFileExtension() {
        return ".ts";
    }
    
    private void generateStateManagementEndpoints(StringBuilder code, String className) {
        String lowerClassName = className.toLowerCase();
        
        // Suspend endpoint
        code.append("  /**\n");
        code.append("   * Suspend ").append(lowerClassName).append(" by ID\n");
        code.append("   */\n");
        code.append("  suspend = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const id = parseInt(req.params.id);\n");
        code.append("      const entity = await this.service.suspend").append(className).append("(id);\n");
        code.append("      \n");
        code.append("      if (!entity) {\n");
        code.append("        res.status(404).json({ error: '").append(className).append(" not found', id });\n");
        code.append("        return;\n");
        code.append("      }\n");
        code.append("      \n");
        code.append("      res.json(entity);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(400).json({ error: 'Invalid state transition', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
        
        // Activate endpoint
        code.append("  /**\n");
        code.append("   * Activate ").append(lowerClassName).append(" by ID\n");
        code.append("   */\n");
        code.append("  activate = async (req: Request, res: Response): Promise<void> => {\n");
        code.append("    try {\n");
        code.append("      const id = parseInt(req.params.id);\n");
        code.append("      const entity = await this.service.activate").append(className).append("(id);\n");
        code.append("      \n");
        code.append("      if (!entity) {\n");
        code.append("        res.status(404).json({ error: '").append(className).append(" not found', id });\n");
        code.append("        return;\n");
        code.append("      }\n");
        code.append("      \n");
        code.append("      res.json(entity);\n");
        code.append("    } catch (error) {\n");
        code.append("      res.status(400).json({ error: 'Invalid state transition', message: (error as Error).message });\n");
        code.append("    }\n");
        code.append("  };\n\n");
    }
}
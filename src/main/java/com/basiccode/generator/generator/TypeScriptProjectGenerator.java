package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TypeScriptProjectGenerator {
    
    public void generateCompleteProject(List<ClassModel> classes, String packageName, Path outputDir) throws IOException {
        createProjectStructure(outputDir);
        generatePackageJson(outputDir);
        generateTsConfig(outputDir);
        generateAppTs(outputDir, classes);
        generateEntities(classes, outputDir);
        generateRepositories(classes, outputDir);
        generateServices(classes, outputDir);
        generateControllers(classes, outputDir);
        generateDatabase(outputDir);
    }
    
    private void createProjectStructure(Path outputDir) throws IOException {
        Files.createDirectories(outputDir.resolve("src/entities"));
        Files.createDirectories(outputDir.resolve("src/repositories"));
        Files.createDirectories(outputDir.resolve("src/services"));
        Files.createDirectories(outputDir.resolve("src/controllers"));
        Files.createDirectories(outputDir.resolve("src/config"));
    }
    
    private void generatePackageJson(Path outputDir) throws IOException {
        String packageJson = """
            {
              "name": "generated-typescript-api",
              "version": "1.0.0",
              "description": "Generated TypeScript API",
              "main": "dist/app.js",
              "scripts": {
                "build": "tsc",
                "start": "node dist/app.js",
                "dev": "ts-node src/app.ts"
              },
              "dependencies": {
                "express": "^4.18.2",
                "typeorm": "^0.3.17",
                "reflect-metadata": "^0.1.13",
                "sqlite3": "^5.1.6",
                "uuid": "^9.0.1"
              },
              "devDependencies": {
                "@types/express": "^4.17.17",
                "@types/node": "^20.5.0",
                "@types/uuid": "^9.0.2",
                "ts-node": "^10.9.1",
                "typescript": "^5.1.6"
              }
            }
            """;
        Files.writeString(outputDir.resolve("package.json"), packageJson);
    }
    
    private void generateTsConfig(Path outputDir) throws IOException {
        String tsConfig = """
            {
              "compilerOptions": {
                "target": "ES2020",
                "module": "commonjs",
                "lib": ["ES2020"],
                "outDir": "./dist",
                "rootDir": "./src",
                "strict": true,
                "esModuleInterop": true,
                "skipLibCheck": true,
                "forceConsistentCasingInFileNames": true,
                "experimentalDecorators": true,
                "emitDecoratorMetadata": true
              },
              "include": ["src/**/*"],
              "exclude": ["node_modules", "dist"]
            }
            """;
        Files.writeString(outputDir.resolve("tsconfig.json"), tsConfig);
    }
    
    private void generateAppTs(Path outputDir, List<ClassModel> classes) throws IOException {
        StringBuilder imports = new StringBuilder();
        StringBuilder routes = new StringBuilder();
        
        for (ClassModel cls : classes) {
            imports.append("import { ").append(cls.getName()).append("Controller } from './controllers/").append(cls.getName()).append("Controller';\n");
            routes.append("app.use('/api/").append(cls.getName().toLowerCase()).append("', new ").append(cls.getName()).append("Controller().router);\n");
        }
        
        String appTs = String.format("""
            import 'reflect-metadata';
            import express from 'express';
            import { AppDataSource } from './config/database';
            %s
            
            const app = express();
            const PORT = process.env.PORT || 3000;
            
            app.use(express.json());
            
            // Initialize database
            AppDataSource.initialize()
              .then(() => {
                console.log('Database connected successfully');
              })
              .catch((error) => console.log('Database connection error:', error));
            
            // Routes
            %s
            
            app.get('/', (req, res) => {
              res.json({ message: 'Generated TypeScript API is running', docs: '/api-docs' });
            });
            
            app.listen(PORT, () => {
              console.log(`Server running on port ${PORT}`);
            });
            """, imports.toString(), routes.toString());
        
        Files.writeString(outputDir.resolve("src/app.ts"), appTs);
    }
    
    private void generateDatabase(Path outputDir) throws IOException {
        String database = """
            import { DataSource } from 'typeorm';
            import { User } from '../entities/User';
            
            export const AppDataSource = new DataSource({
              type: 'sqlite',
              database: 'database.sqlite',
              synchronize: true,
              logging: false,
              entities: [User],
              migrations: [],
              subscribers: [],
            });
            """;
        Files.writeString(outputDir.resolve("src/config/database.ts"), database);
    }
    
    private void generateEntities(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            StringBuilder entity = new StringBuilder();
            entity.append("import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';\n\n");
            
            entity.append("@Entity('").append(toSnakeCase(cls.getName())).append("')\n");
            entity.append("export class ").append(cls.getName()).append(" {\n");
            entity.append("  @PrimaryGeneratedColumn('uuid')\n");
            entity.append("  id!: string;\n\n");
            
            for (Field field : cls.getFields()) {
                entity.append("  @Column()\n");
                entity.append("  ").append(field.getName()).append("!: ").append(getTypeScriptType(field.getType())).append(";\n\n");
            }
            
            entity.append("  @CreateDateColumn()\n");
            entity.append("  createdAt!: Date;\n\n");
            entity.append("  @UpdateDateColumn()\n");
            entity.append("  updatedAt!: Date;\n");
            entity.append("}\n");
            
            Files.writeString(outputDir.resolve("src/entities/" + cls.getName() + ".ts"), entity.toString());
        }
    }
    
    private void generateRepositories(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String repository = String.format("""
                import { Repository } from 'typeorm';
                import { AppDataSource } from '../config/database';
                import { %s } from '../entities/%s';
                
                export class %sRepository {
                  private repository: Repository<%s>;
                
                  constructor() {
                    this.repository = AppDataSource.getRepository(%s);
                  }
                
                  async findById(id: string): Promise<%s | null> {
                    return await this.repository.findOne({ where: { id } });
                  }
                
                  async findAll(): Promise<%s[]> {
                    return await this.repository.find();
                  }
                
                  async save(entity: %s): Promise<%s> {
                    return await this.repository.save(entity);
                  }
                
                  async delete(id: string): Promise<void> {
                    await this.repository.delete(id);
                  }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/repositories/" + cls.getName() + "Repository.ts"), repository);
        }
    }
    
    private void generateServices(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String service = String.format("""
                import { %s } from '../entities/%s';
                import { %sRepository } from '../repositories/%sRepository';
                
                export class %sService {
                  private repository: %sRepository;
                
                  constructor() {
                    this.repository = new %sRepository();
                  }
                
                  async create(entity: %s): Promise<%s> {
                    return await this.repository.save(entity);
                  }
                
                  async findById(id: string): Promise<%s | null> {
                    return await this.repository.findById(id);
                  }
                
                  async findAll(): Promise<%s[]> {
                    return await this.repository.findAll();
                  }
                
                  async update(id: string, entity: Partial<%s>): Promise<%s | null> {
                    const existing = await this.repository.findById(id);
                    if (!existing) return null;
                    
                    Object.assign(existing, entity);
                    return await this.repository.save(existing);
                  }
                
                  async delete(id: string): Promise<void> {
                    await this.repository.delete(id);
                  }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/services/" + cls.getName() + "Service.ts"), service);
        }
    }
    
    private void generateControllers(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String controller = String.format("""
                import { Router, Request, Response } from 'express';
                import { %s } from '../entities/%s';
                import { %sService } from '../services/%sService';
                
                export class %sController {
                  public router: Router;
                  private service: %sService;
                
                  constructor() {
                    this.router = Router();
                    this.service = new %sService();
                    this.initializeRoutes();
                  }
                
                  private initializeRoutes(): void {
                    this.router.post('/', this.create.bind(this));
                    this.router.get('/:id', this.getById.bind(this));
                    this.router.get('/', this.getAll.bind(this));
                    this.router.put('/:id', this.update.bind(this));
                    this.router.delete('/:id', this.delete.bind(this));
                  }
                
                  private async create(req: Request, res: Response): Promise<void> {
                    try {
                      const entity = await this.service.create(req.body);
                      res.status(201).json(entity);
                    } catch (error) {
                      res.status(500).json({ error: 'Internal server error' });
                    }
                  }
                
                  private async getById(req: Request, res: Response): Promise<void> {
                    try {
                      const entity = await this.service.findById(req.params.id);
                      if (!entity) {
                        res.status(404).json({ error: 'Not found' });
                        return;
                      }
                      res.json(entity);
                    } catch (error) {
                      res.status(500).json({ error: 'Internal server error' });
                    }
                  }
                
                  private async getAll(req: Request, res: Response): Promise<void> {
                    try {
                      const entities = await this.service.findAll();
                      res.json(entities);
                    } catch (error) {
                      res.status(500).json({ error: 'Internal server error' });
                    }
                  }
                
                  private async update(req: Request, res: Response): Promise<void> {
                    try {
                      const entity = await this.service.update(req.params.id, req.body);
                      if (!entity) {
                        res.status(404).json({ error: 'Not found' });
                        return;
                      }
                      res.json(entity);
                    } catch (error) {
                      res.status(500).json({ error: 'Internal server error' });
                    }
                  }
                
                  private async delete(req: Request, res: Response): Promise<void> {
                    try {
                      await this.service.delete(req.params.id);
                      res.status(204).send();
                    } catch (error) {
                      res.status(500).json({ error: 'Internal server error' });
                    }
                  }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/controllers/" + cls.getName() + "Controller.ts"), controller);
        }
    }
    
    private String getTypeScriptType(String type) {
        return switch (type) {
            case "String" -> "string";
            case "Integer", "int" -> "number";
            case "Float", "float" -> "number";
            case "Boolean", "boolean" -> "boolean";
            default -> "string";
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
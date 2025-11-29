package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public class TypeScriptMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        // Generate DataSource configuration
        code.append(generateDataSource(enhancedClasses, packageName));
        code.append("\n\n");
        
        // Generate Express app configuration
        code.append(generateExpressApp(enhancedClasses, packageName));
        code.append("\n\n");
        
        // Generate routes configuration
        code.append(generateRoutes(enhancedClasses, packageName));
        
        return code.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "config";
    }
    
    private String generateDataSource(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("import 'reflect-metadata';\n");
        code.append("import { DataSource } from 'typeorm';\n");
        
        for (EnhancedClass enhancedClass : enhancedClasses) {
            code.append("import { ").append(enhancedClass.getOriginalClass().getName()).append(" } from './entities/").append(enhancedClass.getOriginalClass().getName()).append("';\n");
        }
        
        code.append("\nexport const AppDataSource = new DataSource({\n");
        code.append("  type: 'postgres',\n");
        code.append("  host: process.env.DB_HOST || 'localhost',\n");
        code.append("  port: parseInt(process.env.DB_PORT || '5432'),\n");
        code.append("  username: process.env.DB_USERNAME || 'postgres',\n");
        code.append("  password: process.env.DB_PASSWORD || 'password',\n");
        code.append("  database: process.env.DB_NAME || '").append(packageName.toLowerCase().replace("-", "_")).append("',\n");
        code.append("  synchronize: process.env.NODE_ENV === 'development',\n");
        code.append("  logging: process.env.NODE_ENV === 'development',\n");
        code.append("  entities: [");
        
        for (int i = 0; i < enhancedClasses.size(); i++) {
            code.append(enhancedClasses.get(i).getOriginalClass().getName());
            if (i < enhancedClasses.size() - 1) {
                code.append(", ");
            }
        }
        
        code.append("],\n");
        code.append("  migrations: ['src/migrations/*.ts'],\n");
        code.append("  subscribers: ['src/subscribers/*.ts'],\n");
        code.append("});\n");
        
        return code.toString();
    }
    
    private String generateExpressApp(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("import express from 'express';\n");
        code.append("import cors from 'cors';\n");
        code.append("import helmet from 'helmet';\n");
        code.append("import { AppDataSource } from './data-source';\n");
        code.append("import { setupRoutes } from './routes';\n\n");
        
        code.append("const app = express();\n");
        code.append("const PORT = process.env.PORT || 3000;\n\n");
        
        code.append("// Middleware\n");
        code.append("app.use(helmet());\n");
        code.append("app.use(cors());\n");
        code.append("app.use(express.json());\n");
        code.append("app.use(express.urlencoded({ extended: true }));\n\n");
        
        code.append("// Routes\n");
        code.append("setupRoutes(app);\n\n");
        
        code.append("// Health check\n");
        code.append("app.get('/health', (req, res) => {\n");
        code.append("  res.json({ status: 'OK', timestamp: new Date().toISOString() });\n");
        code.append("});\n\n");
        
        code.append("// Error handling\n");
        code.append("app.use((err: Error, req: express.Request, res: express.Response, next: express.NextFunction) => {\n");
        code.append("  console.error(err.stack);\n");
        code.append("  res.status(500).json({ error: 'Internal server error' });\n");
        code.append("});\n\n");
        
        code.append("// Start server\n");
        code.append("AppDataSource.initialize()\n");
        code.append("  .then(() => {\n");
        code.append("    console.log('📊 Database connected successfully');\n");
        code.append("    \n");
        code.append("    app.listen(PORT, () => {\n");
        code.append("      console.log(`🚀 Server running on port ${PORT}`);\n");
        code.append("      console.log(`📖 API documentation: http://localhost:${PORT}/api`);\n");
        code.append("    });\n");
        code.append("  })\n");
        code.append("  .catch((error) => {\n");
        code.append("    console.error('❌ Database connection failed:', error);\n");
        code.append("    process.exit(1);\n");
        code.append("  });\n\n");
        
        code.append("export default app;\n");
        
        return code.toString();
    }
    
    private String generateRoutes(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("import { Express, Router } from 'express';\n");
        
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("import { ").append(className).append("Controller } from './controllers/").append(className).append("Controller';\n");
        }
        
        code.append("\nexport function setupRoutes(app: Express): void {\n");
        code.append("  const apiRouter = Router();\n\n");
        
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            String lowerClassName = className.toLowerCase();
            
            code.append("  // ").append(className).append(" routes\n");
            code.append("  const ").append(lowerClassName).append("Controller = new ").append(className).append("Controller();\n");
            code.append("  const ").append(lowerClassName).append("Router = Router();\n");
            
            code.append("  ").append(lowerClassName).append("Router.get('/', ").append(lowerClassName).append("Controller.getAll);\n");
            code.append("  ").append(lowerClassName).append("Router.get('/:id', ").append(lowerClassName).append("Controller.getById);\n");
            code.append("  ").append(lowerClassName).append("Router.post('/', ").append(lowerClassName).append("Controller.create);\n");
            code.append("  ").append(lowerClassName).append("Router.put('/:id', ").append(lowerClassName).append("Controller.update);\n");
            code.append("  ").append(lowerClassName).append("Router.delete('/:id', ").append(lowerClassName).append("Controller.delete);\n");
            
            if (enhancedClass.isStateful()) {
                code.append("  ").append(lowerClassName).append("Router.patch('/:id/suspend', ").append(lowerClassName).append("Controller.suspend);\n");
                code.append("  ").append(lowerClassName).append("Router.patch('/:id/activate', ").append(lowerClassName).append("Controller.activate);\n");
            }
            
            code.append("  apiRouter.use('/").append(lowerClassName).append("s', ").append(lowerClassName).append("Router);\n\n");
        }
        
        code.append("  app.use('/api', apiRouter);\n");
        code.append("}\n");
        
        return code.toString();
    }
}
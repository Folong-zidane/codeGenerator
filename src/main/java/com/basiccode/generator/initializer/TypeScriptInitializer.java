package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;

@Component
public class TypeScriptInitializer implements ProjectInitializer {
    
    private static final String EXPRESS_VERSION = "4.18.2";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            createTypeScriptStructure(projectPath, projectName);
            return projectPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize TypeScript project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "typescript";
    }
    
    @Override
    public String getLatestVersion() {
        return EXPRESS_VERSION;
    }
    
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        try {
            Path srcDir = templatePath.resolve("src");
            Files.createDirectories(srcDir);
            
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path target = srcDir.resolve(generatedCodePath.relativize(source));
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to merge TypeScript code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private void createTypeScriptStructure(Path projectPath, String projectName) throws IOException {
        // Create package.json
        String packageJson = String.format("""
            {
              "name": "%s",
              "version": "1.0.0",
              "description": "Generated TypeScript API",
              "main": "dist/index.js",
              "scripts": {
                "build": "tsc",
                "start": "node dist/index.js",
                "dev": "ts-node src/index.ts"
              },
              "dependencies": {
                "express": "%s",
                "typeorm": "^0.3.17",
                "reflect-metadata": "^0.1.13",
                "sqlite3": "^5.1.6"
              },
              "devDependencies": {
                "@types/express": "^4.17.21",
                "@types/node": "^20.8.0",
                "typescript": "^5.2.2",
                "ts-node": "^10.9.1"
              }
            }
            """, projectName, EXPRESS_VERSION);
        Files.write(projectPath.resolve("package.json"), packageJson.getBytes());
        
        // Create tsconfig.json
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
        Files.write(projectPath.resolve("tsconfig.json"), tsConfig.getBytes());
        
        // Create src/index.ts
        Path srcDir = projectPath.resolve("src");
        Files.createDirectories(srcDir);
        String indexTs = """
            import express from 'express';
            import 'reflect-metadata';
            
            const app = express();
            const PORT = process.env.PORT || 3000;
            
            app.use(express.json());
            
            app.get('/', (req, res) => {
              res.json({ message: 'API is running' });
            });
            
            app.listen(PORT, () => {
              console.log(`Server running on port ${PORT}`);
            });
            """;
        Files.write(srcDir.resolve("index.ts"), indexTs.getBytes());
    }
}
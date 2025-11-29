package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TypeScriptFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        files.forEach((filePath, content) -> {
            try {
                writeFile(filePath, content, outputPath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write file: " + filePath, e);
            }
        });
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            Path basePath = Paths.get(outputPath);
            Path filePath;
            
            // Determine the correct file path based on content type
            if (content.contains("@Entity")) {
                filePath = basePath.resolve("src/entities").resolve(ensureTsExtension(fileName));
            } else if (content.contains("Controller")) {
                filePath = basePath.resolve("src/controllers").resolve(ensureTsExtension(fileName));
            } else if (content.contains("Service")) {
                filePath = basePath.resolve("src/services").resolve(ensureTsExtension(fileName));
            } else if (content.contains("Repository")) {
                filePath = basePath.resolve("src/repositories").resolve(ensureTsExtension(fileName));
            } else if (content.contains("export enum")) {
                filePath = basePath.resolve("src/enums").resolve(ensureTsExtension(fileName));
            } else if (content.contains("DataSource") || content.contains("express")) {
                filePath = basePath.resolve("src").resolve(ensureTsExtension(fileName));
            } else {
                filePath = basePath.resolve(ensureTsExtension(fileName));
            }
            
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Write the file
            Files.writeString(filePath, content);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to write TypeScript file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            Path base = Paths.get(basePath);
            
            // Create standard Node.js TypeScript directory structure
            Files.createDirectories(base.resolve("src/entities"));
            Files.createDirectories(base.resolve("src/controllers"));
            Files.createDirectories(base.resolve("src/services"));
            Files.createDirectories(base.resolve("src/repositories"));
            Files.createDirectories(base.resolve("src/enums"));
            Files.createDirectories(base.resolve("src/migrations"));
            Files.createDirectories(base.resolve("src/subscribers"));
            Files.createDirectories(base.resolve("dist"));
            
            // Create additional directories if specified
            for (String directory : directories) {
                Files.createDirectories(base.resolve(directory));
            }
            
            // Create project files
            createProjectFiles(base);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to create TypeScript project directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "typescript-project";
    }
    
    private String ensureTsExtension(String fileName) {
        return fileName.endsWith(".ts") ? fileName : fileName + ".ts";
    }
    
    private void createProjectFiles(Path basePath) throws IOException {
        // Create package.json
        String packageJsonContent = generatePackageJson();
        Files.writeString(basePath.resolve("package.json"), packageJsonContent);
        
        // Create tsconfig.json
        String tsconfigContent = generateTsConfig();
        Files.writeString(basePath.resolve("tsconfig.json"), tsconfigContent);
        
        // Create .env.example
        String envExampleContent = generateEnvExample();
        Files.writeString(basePath.resolve(".env.example"), envExampleContent);
        
        // Create .gitignore
        String gitignoreContent = generateGitignore();
        Files.writeString(basePath.resolve(".gitignore"), gitignoreContent);
        
        // Create README.md
        String readmeContent = generateReadme();
        Files.writeString(basePath.resolve("README.md"), readmeContent);
        
        // Create start script
        String startScript = generateStartScript();
        Path startScriptPath = basePath.resolve("start.sh");
        Files.writeString(startScriptPath, startScript);
        startScriptPath.toFile().setExecutable(true);
    }
    
    private String generatePackageJson() {
        return """
            {
              "name": "generated-typescript-api",
              "version": "1.0.0",
              "description": "Generated TypeScript API with Express and TypeORM",
              "main": "dist/app.js",
              "scripts": {
                "build": "tsc",
                "start": "node dist/app.js",
                "dev": "ts-node-dev --respawn --transpile-only src/app.ts",
                "typeorm": "typeorm-ts-node-commonjs",
                "migration:generate": "npm run typeorm -- migration:generate",
                "migration:run": "npm run typeorm -- migration:run",
                "migration:revert": "npm run typeorm -- migration:revert"
              },
              "dependencies": {
                "express": "^4.18.2",
                "typeorm": "^0.3.17",
                "pg": "^8.11.3",
                "cors": "^2.8.5",
                "helmet": "^7.0.0",
                "reflect-metadata": "^0.1.13"
              },
              "devDependencies": {
                "@types/express": "^4.17.17",
                "@types/node": "^20.5.0",
                "@types/cors": "^2.8.13",
                "@types/pg": "^8.10.2",
                "typescript": "^5.1.6",
                "ts-node": "^10.9.1",
                "ts-node-dev": "^2.0.0"
              },
              "keywords": ["typescript", "express", "typeorm", "api", "rest"],
              "author": "UML Code Generator",
              "license": "MIT"
            }
            """;
    }
    
    private String generateTsConfig() {
        return """
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
                "emitDecoratorMetadata": true,
                "resolveJsonModule": true,
                "declaration": true,
                "declarationMap": true,
                "sourceMap": true
              },
              "include": ["src/**/*"],
              "exclude": ["node_modules", "dist"]
            }
            """;
    }
    
    private String generateEnvExample() {
        return """
            # Database Configuration
            DB_HOST=localhost
            DB_PORT=5432
            DB_USERNAME=postgres
            DB_PASSWORD=password
            DB_NAME=generated_app
            
            # Server Configuration
            PORT=3000
            NODE_ENV=development
            
            # Logging
            LOG_LEVEL=info
            """;
    }
    
    private String generateGitignore() {
        return """
            # Dependencies
            node_modules/
            
            # Build output
            dist/
            
            # Environment variables
            .env
            
            # Logs
            *.log
            logs/
            
            # Runtime data
            pids/
            *.pid
            *.seed
            
            # Coverage directory used by tools like istanbul
            coverage/
            
            # IDE
            .vscode/
            .idea/
            
            # OS
            .DS_Store
            Thumbs.db
            """;
    }
    
    private String generateReadme() {
        return """
            # 🚀 Generated TypeScript API
            
            This project was generated using UML-to-Code generator with comprehensive TypeScript support.
            
            ## 🏗️ Architecture
            
            - **Entities**: TypeORM entities with decorators
            - **Controllers**: Express.js controllers with REST endpoints
            - **Services**: Business logic layer with dependency injection
            - **Repositories**: Data access layer with TypeORM
            - **Config**: Database and Express configuration
            
            ## 🚀 Quick Start
            
            ```bash
            # Install dependencies
            npm install
            
            # Copy environment variables
            cp .env.example .env
            
            # Run in development mode
            npm run dev
            
            # Or use the start script
            ./start.sh
            ```
            
            ## 📊 API Endpoints
            
            Once running, the API will be available at:
            - Base URL: http://localhost:3000/api
            - Health check: http://localhost:3000/health
            
            ## 🗄️ Database
            
            The application uses TypeORM with PostgreSQL by default.
            Update the `.env` file with your database configuration.
            
            ## 🔧 Scripts
            
            - `npm run dev` - Start development server with hot reload
            - `npm run build` - Build for production
            - `npm start` - Start production server
            - `npm run migration:generate` - Generate database migration
            - `npm run migration:run` - Run pending migrations
            
            ## 📝 Features
            
            ✅ Complete CRUD operations
            ✅ State management (if applicable)
            ✅ TypeScript strict mode
            ✅ Express.js with middleware
            ✅ TypeORM with PostgreSQL
            ✅ Error handling
            ✅ CORS and security headers
            ✅ Audit fields (createdAt, updatedAt)
            ✅ Hot reload in development
            """;
    }
    
    private String generateStartScript() {
        return """
            #!/bin/bash
            echo "🚀 Starting TypeScript API..."
            
            # Check if Node.js is installed
            if ! command -v node &> /dev/null; then
                echo "❌ Node.js is not installed. Please install Node.js first."
                exit 1
            fi
            
            # Check if npm is installed
            if ! command -v npm &> /dev/null; then
                echo "❌ npm is not installed. Please install npm first."
                exit 1
            fi
            
            echo "📦 Installing dependencies..."
            npm install
            
            echo "📋 Setting up environment..."
            if [ ! -f .env ]; then
                cp .env.example .env
                echo "📝 Created .env file from .env.example"
                echo "⚠️  Please update the database configuration in .env"
            fi
            
            echo "🏗️ Building project..."
            npm run build
            
            echo "🌐 Starting development server..."
            echo "📊 API will be available at: http://localhost:3000/api"
            echo "🔍 Health check: http://localhost:3000/health"
            npm run dev
            """;
    }
}
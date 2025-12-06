package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

/**
 * Enhanced TypeScriptInitializer: Supports multiple project types
 * - API: Express.js REST API
 * - Library: Reusable npm package
 * - CLI: Command-line tool
 * - Fullstack: API + client code
 * - Monorepo: Multi-package workspace
 * 
 * Phase 3 Enhancement: WEEK 1
 */
@Component
public class TypeScriptInitializer implements ProjectInitializer {
    
    private static final String EXPRESS_VERSION = "4.18.2";
    private static final String TYPEORM_VERSION = "0.3.17";
    private static final String TYPESCRIPT_VERSION = "5.2.2";
    
    // Project type enumeration
    public enum ProjectType {
        API,       // Express.js REST API
        LIBRARY,   // Reusable npm package
        CLI,       // Command-line tool
        FULLSTACK, // API + frontend
        MONOREPO   // Multi-package workspace
    }
    
    private ProjectType projectType = ProjectType.API; // Default
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            createTypeScriptStructure(projectPath, projectName, packageName);
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
        return TYPESCRIPT_VERSION;
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
    
    /**
     * Set project type before initialization
     */
    public void setProjectType(ProjectType type) {
        this.projectType = type;
    }
    
    private void createTypeScriptStructure(Path projectPath, String projectName, String packageName) throws IOException {
        // Create base directories
        createDirectoryStructure(projectPath);
        
        // Create package.json with appropriate dependencies
        createPackageJson(projectPath, projectName);
        
        // Create tsconfig.json variants
        createTsConfigs(projectPath);
        
        // Create configuration files (ESLint, Prettier, Jest)
        createConfigFiles(projectPath);
        
        // Create source files based on project type
        createSourceFiles(projectPath, projectName, packageName);
        
        // Create documentation
        createDocumentation(projectPath, projectName);
    }
    
    private void createDirectoryStructure(Path projectPath) throws IOException {
        String[] dirs = {
            "src",
            "src/entities",
            "src/repositories",
            "src/services",
            "src/controllers",
            "src/middleware",
            "src/utils",
            "src/types",
            "src/config",
            "tests",
            "tests/__tests__",
            "tests/fixtures"
        };
        
        for (String dir : dirs) {
            Files.createDirectories(projectPath.resolve(dir));
        }
    }
    
    private void createPackageJson(Path projectPath, String projectName) throws IOException {
        Map<String, Object> deps = new LinkedHashMap<>();
        Map<String, Object> devDeps = new LinkedHashMap<>();
        Map<String, String> scripts = new LinkedHashMap<>();
        
        // Common dependencies
        deps.put("reflect-metadata", "^0.1.13");
        deps.put("class-validator", "^0.14.0");
        deps.put("class-transformer", "^0.5.1");
        
        // Project-specific dependencies
        switch (projectType) {
            case API:
                deps.put("express", EXPRESS_VERSION);
                deps.put("typeorm", TYPEORM_VERSION);
                deps.put("sqlite3", "^5.1.6");
                deps.put("cors", "^2.8.5");
                deps.put("helmet", "^7.0.0");
                deps.put("dotenv", "^16.3.1");
                break;
            
            case LIBRARY:
                // Minimal dependencies for library
                break;
            
            case CLI:
                deps.put("commander", "^11.0.0");
                deps.put("inquirer", "^8.2.5");
                deps.put("chalk", "^4.1.2");
                break;
            
            case FULLSTACK:
                deps.put("express", EXPRESS_VERSION);
                deps.put("typeorm", TYPEORM_VERSION);
                deps.put("cors", "^2.8.5");
                deps.put("dotenv", "^16.3.1");
                deps.put("react", "^18.2.0");
                deps.put("react-dom", "^18.2.0");
                break;
            
            case MONOREPO:
                // No specific dependencies for monorepo root
                break;
        }
        
        // Common dev dependencies
        devDeps.put("typescript", TYPESCRIPT_VERSION);
        devDeps.put("@types/node", "^20.8.0");
        devDeps.put("ts-node", "^10.9.1");
        devDeps.put("ts-node-dev", "^2.0.0");
        devDeps.put("eslint", "^8.50.0");
        devDeps.put("@typescript-eslint/eslint-plugin", "^6.7.0");
        devDeps.put("@typescript-eslint/parser", "^6.7.0");
        devDeps.put("prettier", "^3.0.3");
        devDeps.put("jest", "^29.7.0");
        devDeps.put("@types/jest", "^29.5.5");
        devDeps.put("ts-jest", "^29.1.1");
        devDeps.put("husky", "^8.0.3");
        devDeps.put("lint-staged", "^14.0.1");
        
        if (projectType == ProjectType.API) {
            devDeps.put("@types/express", "^4.17.21");
        }
        
        // Create scripts
        scripts.put("dev", "ts-node-dev --respawn src/index.ts");
        scripts.put("build", "tsc -p tsconfig.build.json");
        scripts.put("start", "node dist/index.js");
        scripts.put("test", "jest");
        scripts.put("test:watch", "jest --watch");
        scripts.put("test:cov", "jest --coverage");
        scripts.put("lint", "eslint 'src/**/*.ts'");
        scripts.put("lint:fix", "eslint 'src/**/*.ts' --fix");
        scripts.put("format", "prettier --write 'src/**/*.ts'");
        scripts.put("typecheck", "tsc --noEmit");
        
        if (projectType == ProjectType.LIBRARY) {
            scripts.put("prepublishOnly", "npm run build && npm run test");
        } else if (projectType == ProjectType.CLI) {
            scripts.put("cli", "node dist/cli/index.js");
            scripts.put("cli:dev", "ts-node src/cli/index.ts");
        }
        
        scripts.put("prepare", "husky install");
        
        // Build package.json
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append(String.format("  \"name\": \"%s\",\n", projectName));
        json.append("  \"version\": \"1.0.0\",\n");
        json.append(String.format("  \"description\": \"Generated TypeScript %s\",\n", projectType.toString().toLowerCase()));
        json.append("  \"main\": \"dist/index.js\",\n");
        json.append("  \"types\": \"dist/index.d.ts\",\n");
        json.append("  \"license\": \"MIT\",\n");
        json.append("  \"scripts\": {\n");
        
        Iterator<Map.Entry<String, String>> scriptIter = scripts.entrySet().iterator();
        while (scriptIter.hasNext()) {
            Map.Entry<String, String> entry = scriptIter.next();
            json.append(String.format("    \"%s\": \"%s\"", entry.getKey(), entry.getValue()));
            if (scriptIter.hasNext()) json.append(",");
            json.append("\n");
        }
        
        json.append("  },\n");
        json.append("  \"dependencies\": {\n");
        Iterator<Map.Entry<String, Object>> depIter = deps.entrySet().iterator();
        while (depIter.hasNext()) {
            Map.Entry<String, Object> entry = depIter.next();
            json.append(String.format("    \"%s\": \"%s\"", entry.getKey(), entry.getValue()));
            if (depIter.hasNext()) json.append(",");
            json.append("\n");
        }
        json.append("  },\n");
        json.append("  \"devDependencies\": {\n");
        Iterator<Map.Entry<String, Object>> devDepIter = devDeps.entrySet().iterator();
        while (devDepIter.hasNext()) {
            Map.Entry<String, Object> entry = devDepIter.next();
            json.append(String.format("    \"%s\": \"%s\"", entry.getKey(), entry.getValue()));
            if (devDepIter.hasNext()) json.append(",");
            json.append("\n");
        }
        json.append("  }\n");
        json.append("}\n");
        
        Files.write(projectPath.resolve("package.json"), json.toString().getBytes());
    }
    
    private void createTsConfigs(Path projectPath) throws IOException {
        // Base tsconfig.json
        String tsConfig = """
            {
              "compilerOptions": {
                "target": "ES2020",
                "module": "commonjs",
                "lib": ["ES2020"],
                "outDir": "./dist",
                "rootDir": "./src",
                "baseUrl": "./src",
                "paths": {
                  "@/*": ["*"],
                  "@entities/*": ["entities/*"],
                  "@services/*": ["services/*"],
                  "@repositories/*": ["repositories/*"],
                  "@controllers/*": ["controllers/*"],
                  "@utils/*": ["utils/*"],
                  "@types/*": ["types/*"],
                  "@config/*": ["config/*"]
                },
                "strict": true,
                "esModuleInterop": true,
                "skipLibCheck": true,
                "forceConsistentCasingInFileNames": true,
                "resolveJsonModule": true,
                "declaration": true,
                "sourceMap": true,
                "noImplicitAny": true,
                "experimentalDecorators": true,
                "emitDecoratorMetadata": true
              },
              "include": ["src/**/*"],
              "exclude": ["node_modules", "dist", "coverage"]
            }
            """;
        Files.write(projectPath.resolve("tsconfig.json"), tsConfig.getBytes());
        
        // Build-only tsconfig
        String tsBuildConfig = """
            {
              "extends": "./tsconfig.json",
              "compilerOptions": {
                "noEmitOnError": true,
                "sourceMap": false
              },
              "exclude": ["src/**/*.test.ts", "src/**/*.spec.ts", "tests"]
            }
            """;
        Files.write(projectPath.resolve("tsconfig.build.json"), tsBuildConfig.getBytes());
    }
    
    private void createConfigFiles(Path projectPath) throws IOException {
        // .eslintrc.js
        String eslint = """
                module.exports = {
                  parser: '@typescript-eslint/parser',
                  extends: ['eslint:recommended', 'plugin:@typescript-eslint/recommended', 'prettier'],
                  rules: {
                    '@typescript-eslint/explicit-function-return-types': 'warn',
                    'no-console': ['warn', { allow: ['warn', 'error'] }]
                  }
                };
                """;
        Files.write(projectPath.resolve(".eslintrc.js"), eslint.getBytes());
        
        // .prettierrc
        String prettier = """
                {
                  "semi": true,
                  "trailingComma": "es5",
                  "singleQuote": true,
                  "printWidth": 100,
                  "tabWidth": 2,
                  "useTabs": false
                }
                """;
        Files.write(projectPath.resolve(".prettierrc"), prettier.getBytes());
        
        // jest.config.js
        String jest = """
                module.exports = {
                  preset: 'ts-jest',
                  testEnvironment: 'node',
                  roots: ['<rootDir>/tests'],
                  testMatch: ['**/__tests__/**/*.ts', '**/?(*.)+(spec|test).ts'],
                  collectCoverageFrom: ['src/**/*.ts', '!src/**/*.d.ts'],
                  coverageThreshold: { global: { lines: 70, functions: 70 } }
                };
                """;
        Files.write(projectPath.resolve("jest.config.js"), jest.getBytes());
        
        // .gitignore
        String gitignore = """
                node_modules/
                dist/
                coverage/
                .env
                .env.local
                .vscode/
                .idea/
                *.swp
                *.swo
                .DS_Store
                """;
        Files.write(projectPath.resolve(".gitignore"), gitignore.getBytes());
    }
    
    private void createSourceFiles(Path projectPath, String projectName, String packageName) throws IOException {
        switch (projectType) {
            case API:
                createApiSourceFiles(projectPath);
                break;
            case LIBRARY:
                createLibrarySourceFiles(projectPath);
                break;
            case CLI:
                createCliSourceFiles(projectPath);
                break;
            default:
                createDefaultSourceFiles(projectPath);
        }
    }
    
    private void createApiSourceFiles(Path projectPath) throws IOException {
        String indexTs = """
            import 'reflect-metadata';
            import express from 'express';
            import cors from 'cors';
            import helmet from 'helmet';
            
            const app = express();
            const PORT = process.env.PORT || 3000;
            
            // Middleware
            app.use(helmet());
            app.use(cors());
            app.use(express.json());
            
            // Health check
            app.get('/health', (req, res) => {
              res.json({ status: 'ok' });
            });
            
            // Start server
            app.listen(PORT, () => {
              console.log(`API running on port ${PORT}`);
            });
            """;
        Files.write(projectPath.resolve("src/index.ts"), indexTs.getBytes());
    }
    
    private void createLibrarySourceFiles(Path projectPath) throws IOException {
        String indexTs = """
            /**
             * Main entry point for the library
             */
            
            export * from './entities';
            export * from './services';
            export * from './utils';
            """;
        Files.write(projectPath.resolve("src/index.ts"), indexTs.getBytes());
    }
    
    private void createCliSourceFiles(Path projectPath) throws IOException {
        String indexTs = """
            #!/usr/bin/env node
            
            import { program } from 'commander';
            import chalk from 'chalk';
            
            const version = '1.0.0';
            
            program
              .name('cli')
              .description('Command-line tool')
              .version(version);
            
            program
              .command('hello <name>')
              .description('Say hello')
              .action((name) => {
                console.log(chalk.green(`Hello, ${name}!`));
              });
            
            program.parse(process.argv);
            """;
        Files.write(projectPath.resolve("src/index.ts"), indexTs.getBytes());
    }
    
    private void createDefaultSourceFiles(Path projectPath) throws IOException {
        String indexTs = """
            /**
             * Generated TypeScript application
             */
            
            export const version = '1.0.0';
            
            export function hello(name: string): string {
              return `Hello, ${name}!`;
            }
            """;
        Files.write(projectPath.resolve("src/index.ts"), indexTs.getBytes());
    }
    
    private void createDocumentation(Path projectPath, String projectName) throws IOException {
        String readme = String.format("""
            # %s
            
            Generated TypeScript %s project.
            
            ## 🚀 Quick Start
            
            ```bash
            npm install
            npm run dev
            ```
            
            ## 📦 Available Scripts
            
            - `npm run dev` - Development mode
            - `npm run build` - Production build
            - `npm test` - Run tests
            - `npm run lint` - Check code style
            
            ## 📚 Documentation
            
            See README.md for more information.
            """, projectName, projectType.toString().toLowerCase());
        Files.write(projectPath.resolve("README.md"), readme.getBytes());
    }
}
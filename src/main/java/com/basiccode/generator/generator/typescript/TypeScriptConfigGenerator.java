package com.basiccode.generator.generator.typescript;

import org.springframework.stereotype.Component;
import lombok.Data;
import java.util.*;

/**
 * TypeScriptConfigGenerator: Generates comprehensive development configurations
 * for TypeScript projects including ESLint, Prettier, Jest, TypeORM, etc.
 * 
 * Generates:
 * - ESLint configuration (.eslintrc.js)
 * - Prettier configuration (.prettierrc)
 * - Jest configuration (jest.config.js)
 * - TypeScript configurations (tsconfig.json variants)
 * - Build tool configurations (webpack, esbuild, rollup)
 * - Pre-commit hooks (.husky, .lintstagedrc)
 * - CI/CD templates (.github/workflows)
 * - Docker configuration
 * - Environment templates
 * 
 * Phase 3 Implementation: WEEK 2 - INFRASTRUCTURE
 */
@Component
public class TypeScriptConfigGenerator {
    
    private static final String ESLINT_VERSION = "8.50.0";
    private static final String PRETTIER_VERSION = "3.0.3";
    private static final String JEST_VERSION = "29.7.0";
    private static final String WEBPACK_VERSION = "5.89.0";
    
    // ==================== CONFIGURATION DTOs ====================
    
    @Data
    public static class ProjectConfig {
        private String projectName;
        private String projectType; // library, cli, api, fullstack
        private String packageManager; // npm, yarn, pnpm
        private boolean useDocker;
        private boolean useCI;
        private String targetNodeVersion = "18.0.0";
        private String targetEsVersion = "ES2020";
    }
    
    // ==================== ESLINT CONFIGURATION ====================
    
    /**
     * Generate .eslintrc.js configuration
     */
    public String generateEslintConfig() {
        return """
                module.exports = {
                  parser: '@typescript-eslint/parser',
                  extends: [
                    'eslint:recommended',
                    'plugin:@typescript-eslint/recommended',
                    'plugin:@typescript-eslint/recommended-requiring-type-checking',
                    'prettier'
                  ],
                  plugins: [
                    '@typescript-eslint',
                    'import',
                    'unused-imports'
                  ],
                  env: {
                    node: true,
                    es2020: true,
                    jest: true
                  },
                  parserOptions: {
                    ecmaVersion: 2020,
                    sourceType: 'module',
                    project: './tsconfig.json'
                  },
                  settings: {
                    'import/resolver': {
                      typescript: {
                        alwaysTryTypes: true,
                        project: './tsconfig.json'
                      }
                    }
                  },
                  rules: {
                    '@typescript-eslint/explicit-function-return-types': [
                      'warn',
                      {
                        allowExpressions: true,
                        allowTypedFunctionExpressions: true,
                        allowHigherOrderFunctions: true
                      }
                    ],
                    '@typescript-eslint/no-unused-vars': 'off',
                    'unused-imports/no-unused-imports': 'error',
                    'unused-imports/no-unused-vars': [
                      'warn',
                      {
                        vars: 'all',
                        varsIgnorePattern: '^_',
                        args: 'after-used',
                        argsIgnorePattern: '^_'
                      }
                    ],
                    '@typescript-eslint/explicit-member-accessibility': [
                      'error',
                      { accessibility: 'explicit' }
                    ],
                    'no-console': ['warn', { allow: ['warn', 'error'] }],
                    'import/order': [
                      'error',
                      {
                        groups: [
                          'builtin',
                          'external',
                          'internal',
                          'parent',
                          'sibling',
                          'index'
                        ],
                        alphabeticalOrder: true,
                        newlines-between: 'always'
                      }
                    ]
                  },
                  overrides: [
                    {
                      files: ['*.test.ts', '*.spec.ts'],
                      env: {
                        jest: true
                      },
                      rules: {
                        '@typescript-eslint/no-explicit-any': 'off'
                      }
                    }
                  ]
                };
                """;
    }
    
    // ==================== PRETTIER CONFIGURATION ====================
    
    /**
     * Generate .prettierrc configuration
     */
    public String generatePrettierConfig() {
        return """
                {
                  "semi": true,
                  "trailingComma": "es5",
                  "singleQuote": true,
                  "printWidth": 100,
                  "tabWidth": 2,
                  "useTabs": false,
                  "arrowParens": "always",
                  "endOfLine": "lf",
                  "quoteProps": "as-needed",
                  "bracketSpacing": true,
                  "bracketSameLine": false
                }
                """;
    }
    
    /**
     * Generate .prettierignore
     */
    public String generatePrettierIgnore() {
        return """
                dist
                build
                coverage
                node_modules
                .next
                .nuxt
                out
                *.min.js
                .DS_Store
                .git
                .env
                .env.local
                """;
    }
    
    // ==================== JEST CONFIGURATION ====================
    
    /**
     * Generate jest.config.js configuration
     */
    public String generateJestConfig() {
        return """
                module.exports = {
                  preset: 'ts-jest',
                  testEnvironment: 'node',
                  roots: ['<rootDir>/src'],
                  testMatch: ['**/__tests__/**/*.ts', '**/?(*.)+(spec|test).ts'],
                  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
                  transform: {
                    '^.+\\\\.ts$': 'ts-jest'
                  },
                  collectCoverageFrom: [
                    'src/**/*.ts',
                    '!src/**/*.d.ts',
                    '!src/**/index.ts',
                    '!src/**/*.interface.ts',
                    '!src/**/*.type.ts'
                  ],
                  coverageThreshold: {
                    global: {
                      branches: 70,
                      functions: 80,
                      lines: 80,
                      statements: 80
                    }
                  },
                  setupFilesAfterEnv: ['<rootDir>/src/tests/setup.ts'],
                  globalSetup: undefined,
                  globalTeardown: undefined,
                  verbose: true,
                  testTimeout: 10000,
                  maxWorkers: '50%'
                };
                """;
    }
    
    // ==================== TYPESCRIPT CONFIGURATIONS ====================
    
    /**
     * Generate tsconfig.json (base configuration)
     */
    public String generateTsConfigBase() {
        return """
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
                      "@config/*": ["config/*"],
                      "@middleware/*": ["middleware/*"]
                    },
                    "strict": true,
                    "esModuleInterop": true,
                    "skipLibCheck": true,
                    "forceConsistentCasingInFileNames": true,
                    "resolveJsonModule": true,
                    "declaration": true,
                    "declarationMap": true,
                    "sourceMap": true,
                    "noImplicitAny": true,
                    "strictNullChecks": true,
                    "strictFunctionTypes": true,
                    "strictPropertyInitialization": true,
                    "noImplicitThis": true,
                    "noUnusedLocals": true,
                    "noUnusedParameters": true,
                    "noImplicitReturns": true,
                    "noFallthroughCasesInSwitch": true,
                    "allowSyntheticDefaultImports": true,
                    "experimentalDecorators": true,
                    "emitDecoratorMetadata": true,
                    "incremental": true,
                    "tsBuildInfoFile": "./dist/.tsbuildinfo"
                  },
                  "include": ["src/**/*"],
                  "exclude": ["node_modules", "dist", "coverage", "**/*.test.ts", "**/*.spec.ts"]
                }
                """;
    }
    
    /**
     * Generate tsconfig.build.json (for production build)
     */
    public String generateTsConfigBuild() {
        return """
                {
                  "extends": "./tsconfig.json",
                  "compilerOptions": {
                    "outDir": "./dist",
                    "declaration": true,
                    "declarationMap": true,
                    "sourceMap": false,
                    "noEmitOnError": true
                  },
                  "exclude": ["src/**/*.test.ts", "src/**/*.spec.ts", "src/**/tests/**/*"]
                }
                """;
    }
    
    // ==================== WEBPACK CONFIGURATION ====================
    
    /**
     * Generate webpack.config.js for bundling
     */
    public String generateWebpackConfig() {
        return """
                const path = require('path');
                const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
                
                module.exports = {
                  mode: process.env.NODE_ENV || 'development',
                  entry: './src/index.ts',
                  output: {
                    path: path.resolve(__dirname, 'dist'),
                    filename: 'bundle.js',
                    libraryTarget: 'umd',
                    library: '[name]'
                  },
                  resolve: {
                    extensions: ['.ts', '.tsx', '.js', '.jsx'],
                    plugins: [
                      new TsconfigPathsPlugin({
                        configFile: './tsconfig.json'
                      })
                    ]
                  },
                  module: {
                    rules: [
                      {
                        test: /\\\\.ts$/,
                        use: 'ts-loader',
                        exclude: /node_modules/
                      }
                    ]
                  },
                  devtool: process.env.NODE_ENV === 'production' ? false : 'source-map',
                  target: 'node',
                  optimization: {
                    minimize: process.env.NODE_ENV === 'production',
                    usedExports: true
                  }
                };
                """;
    }
    
    // ==================== PACKAGE.JSON SCRIPTS ====================
    
    /**
     * Generate npm scripts for package.json
     */
    public Map<String, String> generatePackageJsonScripts(String projectType) {
        Map<String, String> scripts = new LinkedHashMap<>();
        
        // Development
        scripts.put("dev", "ts-node-dev --respawn src/index.ts");
        scripts.put("start", "node dist/index.js");
        scripts.put("build", "tsc -p tsconfig.build.json");
        scripts.put("build:watch", "tsc -p tsconfig.build.json --watch");
        
        // Testing
        scripts.put("test", "jest");
        scripts.put("test:watch", "jest --watch");
        scripts.put("test:cov", "jest --coverage");
        scripts.put("test:debug", "node --inspect-brk -r tsconfig-paths/register -r ts-node/register node_modules/.bin/jest --runInBand");
        
        // Code quality
        scripts.put("lint", "eslint 'src/**/*.ts'");
        scripts.put("lint:fix", "eslint 'src/**/*.ts' --fix");
        scripts.put("format", "prettier --write 'src/**/*.ts'");
        scripts.put("format:check", "prettier --check 'src/**/*.ts'");
        scripts.put("typecheck", "tsc --noEmit");
        
        // Database
        scripts.put("migration:generate", "typeorm migration:generate");
        scripts.put("migration:run", "typeorm migration:run");
        scripts.put("migration:revert", "typeorm migration:revert");
        
        // Project-specific
        if ("cli".equals(projectType)) {
            scripts.put("cli", "node dist/cli/index.js");
            scripts.put("cli:dev", "ts-node src/cli/index.ts");
        } else if ("library".equals(projectType)) {
            scripts.put("prepublishOnly", "npm run build && npm run test");
            scripts.put("prebuild", "npm run clean");
            scripts.put("clean", "rm -rf dist coverage");
        }
        
        // Pre-commit hooks
        scripts.put("prepare", "husky install");
        
        return scripts;
    }
    
    // ==================== PRE-COMMIT HOOKS ====================
    
    /**
     * Generate .lintstagedrc configuration
     */
    public String generateLintStagedConfig() {
        return """
                {
                  "*.ts": [
                    "eslint --fix",
                    "prettier --write"
                  ],
                  "*.json": [
                    "prettier --write"
                  ],
                  "*.md": [
                    "prettier --write"
                  ]
                }
                """;
    }
    
    /**
     * Generate .husky/pre-commit hook
     */
    public String generateHuskyPreCommit() {
        return """
                #!/bin/sh
                . "$(dirname "$0")/_/husky.sh"
                
                npx lint-staged
                """;
    }
    
    // ==================== CI/CD GITHUB WORKFLOWS ====================
    
    /**
     * Generate GitHub Actions workflow for CI
     */
    public String generateGitHubWorkflow() {
        return """
                name: CI
                
                on:
                  push:
                    branches: [main, develop]
                  pull_request:
                    branches: [main, develop]
                
                jobs:
                  test:
                    runs-on: ubuntu-latest
                    
                    strategy:
                      matrix:
                        node-version: [18.x, 20.x]
                    
                    steps:
                      - uses: actions/checkout@v3
                      - name: Use Node.js ${{ matrix.node-version }}
                        uses: actions/setup-node@v3
                        with:
                          node-version: ${{ matrix.node-version }}
                          cache: 'npm'
                      
                      - name: Install dependencies
                        run: npm ci
                      
                      - name: Run linting
                        run: npm run lint
                      
                      - name: Run type check
                        run: npm run typecheck
                      
                      - name: Run tests
                        run: npm run test:cov
                      
                      - name: Upload coverage
                        uses: codecov/codecov-action@v3
                        with:
                          files: ./coverage/lcov.info
                      
                      - name: Build
                        run: npm run build
                """;
    }
    
    // ==================== DOCKER CONFIGURATION ====================
    
    /**
     * Generate Dockerfile for Node.js/TypeScript app
     */
    public String generateDockerfile(String projectType) {
        return switch(projectType) {
            case "api" -> generateDockerfileAPI();
            case "cli" -> generateDockerfileCLI();
            case "library" -> generateDockerfileLibrary();
            default -> generateDockerfileGeneric();
        };
    }
    
    private String generateDockerfileAPI() {
        return """
                # Build stage
                FROM node:18-alpine AS builder
                
                WORKDIR /app
                COPY package*.json ./
                RUN npm ci
                COPY . .
                RUN npm run build
                
                # Production stage
                FROM node:18-alpine
                
                WORKDIR /app
                ENV NODE_ENV=production
                
                COPY package*.json ./
                RUN npm ci --only=production
                COPY --from=builder /app/dist ./dist
                
                EXPOSE 3000
                HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \\
                  CMD node -e "require('http').get('http://localhost:3000/health', (r) => {if (r.statusCode !== 200) throw new Error(r.statusCode)})"
                
                CMD ["node", "dist/index.js"]
                """;
    }
    
    private String generateDockerfileCLI() {
        return """
                FROM node:18-alpine
                
                WORKDIR /app
                COPY package*.json ./
                RUN npm ci
                COPY . .
                
                RUN npm run build
                
                ENTRYPOINT ["node", "dist/cli/index.js"]
                """;
    }
    
    private String generateDockerfileLibrary() {
        return """
                FROM node:18-alpine
                
                WORKDIR /app
                COPY package*.json ./
                RUN npm ci
                COPY . .
                
                RUN npm run build
                RUN npm run test
                
                CMD ["npm", "run", "build"]
                """;
    }
    
    private String generateDockerfileGeneric() {
        return """
                FROM node:18-alpine
                
                WORKDIR /app
                COPY package*.json ./
                RUN npm ci
                COPY . .
                RUN npm run build
                
                EXPOSE 3000
                CMD ["node", "dist/index.js"]
                """;
    }
    
    /**
     * Generate .dockerignore
     */
    public String generateDockerIgnore() {
        return """
                node_modules
                npm-debug.log
                dist
                coverage
                .git
                .gitignore
                .env
                .env.local
                .vscode
                .idea
                *.swp
                *.swo
                *~
                .DS_Store
                README.md
                LICENSE
                """;
    }
    
    // ==================== ENVIRONMENT CONFIGURATION ====================
    
    /**
     * Generate .env.example
     */
    public String generateEnvExample(String projectType) {
        StringBuilder env = new StringBuilder();
        
        // Common variables
        env.append("# Environment\n");
        env.append("NODE_ENV=development\n");
        env.append("PORT=3000\n");
        env.append("LOG_LEVEL=debug\n\n");
        
        // Database
        env.append("# Database\n");
        env.append("DB_HOST=localhost\n");
        env.append("DB_PORT=5432\n");
        env.append("DB_NAME=app_dev\n");
        env.append("DB_USER=postgres\n");
        env.append("DB_PASSWORD=password\n\n");
        
        // API-specific
        if ("api".equals(projectType)) {
            env.append("# API\n");
            env.append("API_PREFIX=/api\n");
            env.append("API_VERSION=v1\n");
            env.append("CORS_ORIGIN=http://localhost:3000\n\n");
            
            env.append("# Authentication\n");
            env.append("JWT_SECRET=your-secret-key\n");
            env.append("JWT_EXPIRATION=24h\n\n");
        }
        
        // Redis
        env.append("# Redis\n");
        env.append("REDIS_HOST=localhost\n");
        env.append("REDIS_PORT=6379\n");
        env.append("REDIS_PASSWORD=\n\n");
        
        return env.toString();
    }
    
    // ==================== GITIGNORE ====================
    
    /**
     * Generate .gitignore for TypeScript projects
     */
    public String generateGitIgnore() {
        return """
                # Dependencies
                node_modules/
                package-lock.json
                yarn.lock
                pnpm-lock.yaml
                
                # Build output
                dist/
                build/
                .next
                .nuxt
                out/
                
                # Development
                .DS_Store
                .vscode/
                .idea/
                *.swp
                *.swo
                *~
                .env
                .env.local
                .env.*.local
                
                # Testing
                coverage/
                .nyc_output/
                
                # IDE
                .eslintcache
                .prettier-ignore
                
                # Logs
                logs/
                *.log
                npm-debug.log*
                yarn-debug.log*
                yarn-error.log*
                
                # Temporary files
                tmp/
                temp/
                cache/
                """;
    }
    
    // ==================== README TEMPLATE ====================
    
    /**
     * Generate README.md template
     */
    public String generateReadmeTemplate(ProjectConfig config) {
        return """
                # %s
                
                TypeScript %s project.
                
                ## ✨ Features
                
                - 🎯 Full TypeScript support with strict mode
                - 📦 Modern tooling (ESLint, Prettier, Jest)
                - 🗄️ TypeORM database integration
                - 🚀 Production-ready configuration
                - 📝 Comprehensive documentation
                
                ## 🚀 Quick Start
                
                ### Prerequisites
                - Node.js >= 18.0.0
                - npm / yarn / pnpm
                
                ### Installation
                
                ```bash
                npm install
                ```
                
                ### Development
                
                ```bash
                npm run dev
                ```
                
                ### Testing
                
                ```bash
                npm run test          # Run tests
                npm run test:watch    # Watch mode
                npm run test:cov      # With coverage
                ```
                
                ### Code Quality
                
                ```bash
                npm run lint          # Run linter
                npm run lint:fix      # Auto-fix issues
                npm run format        # Format code
                npm run typecheck     # Check types
                ```
                
                ### Build
                
                ```bash
                npm run build         # Build for production
                npm start             # Run production build
                ```
                
                ## 📁 Project Structure
                
                ```
                src/
                  ├── entities/       # TypeORM entities
                  ├── repositories/   # Data access layer
                  ├── services/       # Business logic
                  ├── controllers/    # HTTP handlers
                  ├── middleware/     # Express middleware
                  ├── utils/          # Utility functions
                  ├── types/          # TypeScript types
                  ├── config/         # Configuration
                  └── index.ts        # Application entry point
                tests/
                  └── fixtures/       # Test data
                ```
                
                ## 🔧 Configuration
                
                - **ESLint**: `.eslintrc.js`
                - **Prettier**: `.prettierrc`
                - **Jest**: `jest.config.js`
                - **TypeScript**: `tsconfig.json`
                - **TypeORM**: `ormconfig.ts`
                
                ## 📦 Scripts
                
                - `npm run dev` - Start development server
                - `npm run build` - Build for production
                - `npm start` - Run production build
                - `npm test` - Run test suite
                - `npm run lint` - Run ESLint
                - `npm run format` - Format code with Prettier
                
                ## 🐳 Docker
                
                ```bash
                docker build -t %s .
                docker run -p 3000:3000 %s
                ```
                
                ## 📄 License
                
                MIT
                """.formatted(config.getProjectName(), config.getProjectType(), 
                             config.getProjectName().toLowerCase(), 
                             config.getProjectName().toLowerCase());
    }
}

package com.basiccode.generator.web;

import com.basiccode.generator.enhanced.IncrementalGenerationManager;
import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.DiagramParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v2/generate")
@CrossOrigin(origins = "*")
public class FlexibleGeneratorController {
    
    @PostMapping("/files")
    public ResponseEntity<GenerationResult> generateFiles(@RequestBody GenerationRequest request) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(request.umlContent());
            
            Path outputPath = Paths.get(request.outputPath());
            Files.createDirectories(outputPath);
            
            if (request.generationType() == GenerationType.COMPLETE_PROJECT) {
                generateCompleteProject(diagram, request, outputPath);
            } else {
                generateFilesOnly(diagram, request, outputPath);
            }
            
            return ResponseEntity.ok(new GenerationResult(
                true, 
                "Files generated successfully at: " + outputPath.toAbsolutePath(),
                outputPath.toString(),
                diagram.getClasses().size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenerationResult(
                false, 
                "Error: " + e.getMessage(),
                null,
                0
            ));
        }
    }
    
    private void generateFilesOnly(Diagram diagram, GenerationRequest request, Path outputPath) throws IOException {
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), outputPath);
    }
    
    private void generateCompleteProject(Diagram diagram, GenerationRequest request, Path outputPath) throws IOException {
        // 1. Générer les fichiers CRUD
        generateFilesOnly(diagram, request, outputPath);
        
        // 2. Générer la structure complète selon le framework
        switch (request.framework()) {
            case SPRING_BOOT -> generateSpringBootProject(outputPath, request);
            case DJANGO -> generateDjangoProject(outputPath, request);
            case FLASK -> generateFlaskProject(outputPath, request);
            case DOTNET -> generateDotNetProject(outputPath, request);
            case EXPRESS -> generateExpressProject(outputPath, request);
        }
    }
    
    private void generateSpringBootProject(Path outputPath, GenerationRequest request) throws IOException {
        // pom.xml
        Files.writeString(outputPath.resolve("pom.xml"), generatePomXml(request.packageName()));
        
        // application.yml
        Path resourcesPath = outputPath.resolve("src/main/resources");
        Files.createDirectories(resourcesPath);
        Files.writeString(resourcesPath.resolve("application.yml"), generateApplicationYml());
        
        // Main class
        String packagePath = request.packageName().replace('.', '/');
        Path javaPath = outputPath.resolve("src/main/java").resolve(packagePath);
        Files.createDirectories(javaPath);
        Files.writeString(javaPath.resolve("Application.java"), generateSpringBootMain(request.packageName()));
        
        // Dockerfile
        Files.writeString(outputPath.resolve("Dockerfile"), generateDockerfile("java"));
        
        // README.md
        Files.writeString(outputPath.resolve("README.md"), generateReadme("Spring Boot"));
    }
    
    private void generateDjangoProject(Path outputPath, GenerationRequest request) throws IOException {
        // requirements.txt
        Files.writeString(outputPath.resolve("requirements.txt"), 
            "Django==4.2.7\ndjangorestframework==3.14.0\ndjango-cors-headers==4.3.1");
        
        // manage.py
        Files.writeString(outputPath.resolve("manage.py"), generateDjangoManage());
        
        // settings.py
        Path settingsPath = outputPath.resolve("config");
        Files.createDirectories(settingsPath);
        Files.writeString(settingsPath.resolve("settings.py"), generateDjangoSettings(request.packageName()));
        
        // Dockerfile
        Files.writeString(outputPath.resolve("Dockerfile"), generateDockerfile("python"));
    }
    
    private void generateFlaskProject(Path outputPath, GenerationRequest request) throws IOException {
        // requirements.txt
        Files.writeString(outputPath.resolve("requirements.txt"), 
            "Flask==2.3.3\nFlask-SQLAlchemy==3.0.5\nFlask-CORS==4.0.0");
        
        // app.py
        Files.writeString(outputPath.resolve("app.py"), generateFlaskApp());
        
        // Dockerfile
        Files.writeString(outputPath.resolve("Dockerfile"), generateDockerfile("python"));
    }
    
    private void generateDotNetProject(Path outputPath, GenerationRequest request) throws IOException {
        // .csproj
        Files.writeString(outputPath.resolve("GeneratedApp.csproj"), generateCsProj());
        
        // Program.cs
        Files.writeString(outputPath.resolve("Program.cs"), generateDotNetProgram());
        
        // Dockerfile
        Files.writeString(outputPath.resolve("Dockerfile"), generateDockerfile("dotnet"));
    }
    
    private void generateExpressProject(Path outputPath, GenerationRequest request) throws IOException {
        // package.json
        Files.writeString(outputPath.resolve("package.json"), generatePackageJson());
        
        // server.js
        Files.writeString(outputPath.resolve("server.js"), generateExpressServer());
        
        // Dockerfile
        Files.writeString(outputPath.resolve("Dockerfile"), generateDockerfile("node"));
    }
    
    // Template methods (simplified)
    private String generatePomXml(String packageName) {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0">
                <modelVersion>4.0.0</modelVersion>
                <groupId>%s</groupId>
                <artifactId>generated-crud-app</artifactId>
                <version>1.0.0</version>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                        <version>3.2.0</version>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-jpa</artifactId>
                        <version>3.2.0</version>
                    </dependency>
                </dependencies>
            </project>
            """.formatted(packageName);
    }
    
    private String generateApplicationYml() {
        return """
            server:
              port: 8080
            spring:
              datasource:
                url: jdbc:h2:mem:testdb
                driver-class-name: org.h2.Driver
              jpa:
                hibernate:
                  ddl-auto: create-drop
            """;
    }
    
    private String generateSpringBootMain(String packageName) {
        return """
            package %s;
            
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            
            @SpringBootApplication
            public class Application {
                public static void main(String[] args) {
                    SpringApplication.run(Application.class, args);
                }
            }
            """.formatted(packageName);
    }
    
    private String generateDockerfile(String language) {
        return switch (language) {
            case "java" -> """
                FROM openjdk:17-jdk-slim
                COPY target/*.jar app.jar
                EXPOSE 8080
                ENTRYPOINT ["java", "-jar", "/app.jar"]
                """;
            case "python" -> """
                FROM python:3.11-slim
                WORKDIR /app
                COPY requirements.txt .
                RUN pip install -r requirements.txt
                COPY . .
                EXPOSE 8000
                CMD ["python", "app.py"]
                """;
            case "dotnet" -> """
                FROM mcr.microsoft.com/dotnet/aspnet:8.0
                WORKDIR /app
                COPY . .
                EXPOSE 80
                ENTRYPOINT ["dotnet", "GeneratedApp.dll"]
                """;
            case "node" -> """
                FROM node:18-alpine
                WORKDIR /app
                COPY package*.json ./
                RUN npm install
                COPY . .
                EXPOSE 3000
                CMD ["node", "server.js"]
                """;
            default -> "";
        };
    }
    
    private String generateReadme(String framework) {
        return """
            # Generated CRUD Application
            
            Framework: %s
            Generated automatically from UML diagrams.
            
            ## Quick Start
            ```bash
            docker build -t generated-app .
            docker run -p 8080:8080 generated-app
            ```
            """.formatted(framework);
    }
    
    private String generateDjangoManage() {
        return """
            #!/usr/bin/env python
            import os
            import sys
            
            if __name__ == '__main__':
                os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'config.settings')
                from django.core.management import execute_from_command_line
                execute_from_command_line(sys.argv)
            """;
    }
    
    private String generateDjangoSettings(String packageName) {
        return """
            import os
            
            BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
            
            SECRET_KEY = 'generated-secret-key'
            DEBUG = True
            ALLOWED_HOSTS = ['*']
            
            INSTALLED_APPS = [
                'django.contrib.admin',
                'django.contrib.auth',
                'django.contrib.contenttypes',
                'rest_framework',
                'corsheaders',
            ]
            
            MIDDLEWARE = [
                'corsheaders.middleware.CorsMiddleware',
                'django.middleware.common.CommonMiddleware',
            ]
            
            ROOT_URLCONF = 'config.urls'
            
            DATABASES = {
                'default': {
                    'ENGINE': 'django.db.backends.sqlite3',
                    'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
                }
            }
            """;
    }
    
    private String generateFlaskApp() {
        return """
            from flask import Flask
            from flask_sqlalchemy import SQLAlchemy
            from flask_cors import CORS
            
            app = Flask(__name__)
            app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'
            db = SQLAlchemy(app)
            CORS(app)
            
            @app.route('/')
            def index():
                return {'message': 'Generated CRUD API'}
            
            if __name__ == '__main__':
                app.run(debug=True, host='0.0.0.0', port=8000)
            """;
    }
    
    private String generateCsProj() {
        return """
            <Project Sdk="Microsoft.NET.Sdk.Web">
              <PropertyGroup>
                <TargetFramework>net8.0</TargetFramework>
              </PropertyGroup>
              <ItemGroup>
                <PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
                <PackageReference Include="Swashbuckle.AspNetCore" Version="6.4.0" />
              </ItemGroup>
            </Project>
            """;
    }
    
    private String generateDotNetProgram() {
        return """
            var builder = WebApplication.CreateBuilder(args);
            
            builder.Services.AddControllers();
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            
            var app = builder.Build();
            
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }
            
            app.UseAuthorization();
            app.MapControllers();
            app.Run();
            """;
    }
    
    private String generatePackageJson() {
        return """
            {
              "name": "generated-crud-app",
              "version": "1.0.0",
              "dependencies": {
                "express": "^4.18.2",
                "cors": "^2.8.5",
                "sqlite3": "^5.1.6"
              },
              "scripts": {
                "start": "node server.js"
              }
            }
            """;
    }
    
    private String generateExpressServer() {
        return """
            const express = require('express');
            const cors = require('cors');
            
            const app = express();
            app.use(cors());
            app.use(express.json());
            
            app.get('/', (req, res) => {
                res.json({ message: 'Generated CRUD API' });
            });
            
            const PORT = process.env.PORT || 3000;
            app.listen(PORT, () => {
                console.log(`Server running on port ${PORT}`);
            });
            """;
    }
    
    public enum GenerationType {
        FILES_ONLY, COMPLETE_PROJECT
    }
    
    public enum Framework {
        SPRING_BOOT, DJANGO, FLASK, DOTNET, EXPRESS
    }
    
    public record GenerationRequest(
        String umlContent,
        String packageName,
        String outputPath,
        GenerationType generationType,
        Framework framework
    ) {}
    
    public record GenerationResult(
        boolean success,
        String message,
        String projectPath,
        int classCount
    ) {}
}
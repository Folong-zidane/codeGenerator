package com.basiccode.generator.web;

import com.basiccode.generator.enhanced.IncrementalGenerationManager;
import com.basiccode.generator.generator.PythonProjectGenerator;
import com.basiccode.generator.generator.CSharpProjectGenerator;
import com.basiccode.generator.generator.DjangoProjectGenerator;
import com.basiccode.generator.generator.TypeScriptProjectGenerator;
import com.basiccode.generator.generator.PhpProjectGenerator;
import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.DiagramParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/generate")
@CrossOrigin(origins = "*")
public class FileServerController {
    
    @PostMapping("/files")
    public ResponseEntity<GenerationResponse> generateToFiles(@RequestBody FileGenerationRequest request) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(request.umlContent());
            
            Path outputPath = Paths.get(request.outputPath());
            
            // Créer le dossier de sortie
            Files.createDirectories(outputPath);
            
            // Générer selon le type
            switch (request.generationType().toLowerCase()) {
                case "complete_project" -> generateCompleteProject(diagram, request, outputPath);
                case "files_only" -> generateFilesOnly(diagram, request, outputPath);
                default -> generateFilesOnly(diagram, request, outputPath);
            }
            
            return ResponseEntity.ok(new GenerationResponse(
                true,
                "Files generated successfully in " + outputPath.toAbsolutePath(),
                diagram.getClasses().size(),
                outputPath.toAbsolutePath().toString()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.ok(new GenerationResponse(
                false,
                "Error: " + e.getMessage(),
                0,
                null
            ));
        }
    }
    
    @PostMapping("/direct")
    public ResponseEntity<Map<String, Object>> generateDirect(@RequestBody DirectGenerationRequest request) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(request.umlContent());
            
            // Générer directement dans le dossier courant du client
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            Path projectDir = currentDir.resolve(request.projectName());
            
            Files.createDirectories(projectDir);
            
            // Générer le projet complet
            generateCompleteProjectStructure(diagram, request, projectDir);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Project generated in current directory",
                "projectPath", projectDir.toAbsolutePath().toString(),
                "classCount", diagram.getClasses().size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }
    
    private void generateCompleteProject(Diagram diagram, FileGenerationRequest request, Path outputPath) throws IOException {
        switch (request.framework().toLowerCase()) {
            case "spring_boot", "java" -> {
                IncrementalGenerationManager manager = new IncrementalGenerationManager();
                manager.generateIncremental(diagram.getClasses(), request.packageName(), outputPath);
                createSpringBootProject(outputPath, request.packageName());
            }
            case "python", "fastapi" -> {
                PythonProjectGenerator pythonGen = new PythonProjectGenerator();
                pythonGen.generateCompleteProject(diagram.getClasses(), request.packageName(), outputPath);
            }
            case "django" -> {
                DjangoProjectGenerator djangoGen = new DjangoProjectGenerator();
                djangoGen.generateCompleteProject(diagram.getClasses(), request.packageName(), outputPath);
            }
            case "csharp", "dotnet" -> {
                CSharpProjectGenerator csharpGen = new CSharpProjectGenerator();
                csharpGen.generateCompleteProject(diagram.getClasses(), request.packageName(), outputPath);
            }
            case "typescript" -> {
                TypeScriptProjectGenerator tsGen = new TypeScriptProjectGenerator();
                tsGen.generateCompleteProject(diagram.getClasses(), request.packageName(), outputPath);
            }
            case "php" -> {
                PhpProjectGenerator phpGen = new PhpProjectGenerator();
                phpGen.generateCompleteProject(diagram.getClasses(), request.packageName(), outputPath);
            }
            default -> {
                IncrementalGenerationManager manager = new IncrementalGenerationManager();
                manager.generateIncremental(diagram.getClasses(), request.packageName(), outputPath);
                createSpringBootProject(outputPath, request.packageName());
            }
        }
    }
    
    private void generateFilesOnly(Diagram diagram, FileGenerationRequest request, Path outputPath) throws IOException {
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), outputPath);
    }
    
    private void generateCompleteProjectStructure(Diagram diagram, DirectGenerationRequest request, Path projectDir) throws IOException {
        // Générer CRUD
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), projectDir);
        
        // Ajouter structure complète
        createSpringBootProject(projectDir, request.packageName());
    }
    
    private void createSpringBootProject(Path projectDir, String packageName) throws IOException {
        // pom.xml
        String pomContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                     http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>3.2.2</version>
                    <relativePath/>
                </parent>
                
                <groupId>%s</groupId>
                <artifactId>generated-app</artifactId>
                <version>1.0.0</version>
                <packaging>jar</packaging>
                
                <properties>
                    <java.version>17</java.version>
                </properties>
                
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-jpa</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <scope>runtime</scope>
                    </dependency>
                </dependencies>
                
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-maven-plugin</artifactId>
                        </plugin>
                    </plugins>
                </build>
            </project>
            """.formatted(packageName);
        
        Files.writeString(projectDir.resolve("pom.xml"), pomContent);
        
        // Application.java
        String packagePath = packageName.replace('.', '/');
        Path javaDir = projectDir.resolve("src/main/java").resolve(packagePath);
        Files.createDirectories(javaDir);
        
        String appContent = """
            package %s;
            
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            
            @SpringBootApplication
            public class GeneratedApplication {
                public static void main(String[] args) {
                    SpringApplication.run(GeneratedApplication.class, args);
                }
            }
            """.formatted(packageName);
        
        Files.writeString(javaDir.resolve("GeneratedApplication.java"), appContent);
        
        // application.yml
        Path resourcesDir = projectDir.resolve("src/main/resources");
        Files.createDirectories(resourcesDir);
        
        String ymlContent = """
            server:
              port: 8080
            
            spring:
              application:
                name: generated-app
              datasource:
                url: jdbc:h2:mem:testdb
                driver-class-name: org.h2.Driver
                username: sa
                password: 
              jpa:
                hibernate:
                  ddl-auto: create-drop
                show-sql: true
              h2:
                console:
                  enabled: true
            """;
        
        Files.writeString(resourcesDir.resolve("application.yml"), ymlContent);
    }
    
    private void createPythonProject(Path projectDir, String packageName) throws IOException {
        // requirements.txt
        Files.writeString(projectDir.resolve("requirements.txt"), 
            "fastapi==0.104.1\nuvicorn==0.24.0\nsqlalchemy==2.0.23\npydantic==2.5.0\n");
        
        // main.py
        String mainContent = """
            from fastapi import FastAPI
            from sqlalchemy import create_engine
            from sqlalchemy.ext.declarative import declarative_base
            from sqlalchemy.orm import sessionmaker
            
            app = FastAPI(title="Generated Python API")
            
            # Database setup
            SQLALCHEMY_DATABASE_URL = "sqlite:///./app.db"
            engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False})
            SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
            Base = declarative_base()
            
            @app.get("/")
            def read_root():
                return {"message": "Generated Python API is running"}
            
            if __name__ == "__main__":
                import uvicorn
                uvicorn.run(app, host="0.0.0.0", port=8000)
            """;
        
        Files.writeString(projectDir.resolve("main.py"), mainContent);
    }
    
    private void createDjangoProject(Path projectDir, String packageName) throws IOException {
        Files.writeString(projectDir.resolve("requirements.txt"), 
            "Django==4.2.7\ndjango-rest-framework==3.14.0\n");
        
        Files.writeString(projectDir.resolve("manage.py"), 
            "#!/usr/bin/env python\nimport os\nimport sys\n\nif __name__ == '__main__':\n    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'settings')\n    from django.core.management import execute_from_command_line\n    execute_from_command_line(sys.argv)\n");
    }
    
    private void createFlaskProject(Path projectDir, String packageName) throws IOException {
        Files.writeString(projectDir.resolve("requirements.txt"), 
            "Flask==2.3.3\nFlask-SQLAlchemy==3.0.5\n");
        
        Files.writeString(projectDir.resolve("app.py"), 
            "from flask import Flask\napp = Flask(__name__)\n\n@app.route('/')\ndef hello():\n    return 'Generated Flask API'\n\nif __name__ == '__main__':\n    app.run(debug=True)\n");
    }
    
    private void createDotNetProject(Path projectDir, String packageName) throws IOException {
        String csprojContent = """
            <Project Sdk="Microsoft.NET.Sdk.Web">
              <PropertyGroup>
                <TargetFramework>net8.0</TargetFramework>
              </PropertyGroup>
              <ItemGroup>
                <PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
              </ItemGroup>
            </Project>
            """;
        
        Files.writeString(projectDir.resolve("GeneratedApp.csproj"), csprojContent);
        
        Files.writeString(projectDir.resolve("Program.cs"), 
            "var builder = WebApplication.CreateBuilder(args);\nvar app = builder.Build();\napp.MapGet(\"/\", () => \"Generated .NET API\");\napp.Run();\n");
    }
    
    public record FileGenerationRequest(
        String umlContent,
        String packageName,
        String outputPath,
        String generationType,
        String framework
    ) {}
    
    public record DirectGenerationRequest(
        String umlContent,
        String packageName,
        String projectName
    ) {}
    
    public record GenerationResponse(
        boolean success,
        String message,
        int classCount,
        String outputPath
    ) {}
}
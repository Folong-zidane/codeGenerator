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
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "*")
public class ProjectGeneratorController {
    
    @PostMapping("/generate-structure")
    public ResponseEntity<GenerationResponse> generateStructureOnly(@RequestBody ProjectRequest request) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(request.umlContent());
            
            Path projectPath = Paths.get(request.outputPath());
            
            // Option 1: Générer seulement la structure de dossiers et les classes CRUD
            IncrementalGenerationManager manager = new IncrementalGenerationManager();
            manager.generateIncremental(diagram.getClasses(), request.packageName(), projectPath);
            
            return ResponseEntity.ok(new GenerationResponse(
                true, 
                "Structure generated successfully", 
                projectPath.toString(),
                diagram.getClasses().size(),
                "STRUCTURE_ONLY"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.ok(new GenerationResponse(
                false, 
                "Error: " + e.getMessage(), 
                null, 
                0,
                "ERROR"
            ));
        }
    }
    
    @PostMapping("/generate-complete")
    public ResponseEntity<GenerationResponse> generateCompleteProject(@RequestBody ProjectRequest request) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(request.umlContent());
            
            Path projectPath = Paths.get(request.outputPath());
            
            // Option 2: Générer le projet complet avec configuration
            generateCompleteProjectStructure(diagram, request, projectPath);
            
            return ResponseEntity.ok(new GenerationResponse(
                true, 
                "Complete project generated successfully", 
                projectPath.toString(),
                diagram.getClasses().size(),
                "COMPLETE_PROJECT"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.ok(new GenerationResponse(
                false, 
                "Error: " + e.getMessage(), 
                null, 
                0,
                "ERROR"
            ));
        }
    }
    
    private void generateCompleteProjectStructure(Diagram diagram, ProjectRequest request, Path projectPath) throws IOException {
        String language = request.language().toLowerCase();
        
        switch (language) {
            case "java" -> generateSpringBootProject(diagram, request, projectPath);
            case "python" -> generatePythonProject(diagram, request, projectPath);
            case "csharp" -> generateDotNetProject(diagram, request, projectPath);
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
    
    private void generateSpringBootProject(Diagram diagram, ProjectRequest request, Path projectPath) throws IOException {
        // 1. Générer la structure CRUD
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), projectPath);
        
        // 2. Générer pom.xml
        generateSpringBootPom(projectPath, request.packageName());
        
        // 3. Générer application.yml
        generateApplicationYml(projectPath);
        
        // 4. Générer Application.java
        generateSpringBootMain(projectPath, request.packageName());
        
        // 5. Générer README.md
        generateReadme(projectPath, "Spring Boot", request.packageName());
    }
    
    private void generatePythonProject(Diagram diagram, ProjectRequest request, Path projectPath) throws IOException {
        // 1. Générer la structure CRUD (adaptée pour Python)
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), projectPath);
        
        // 2. Générer requirements.txt
        generateRequirementsTxt(projectPath);
        
        // 3. Générer main.py (FastAPI)
        generateFastAPIMain(projectPath);
        
        // 4. Générer README.md
        generateReadme(projectPath, "FastAPI", request.packageName());
    }
    
    private void generateDotNetProject(Diagram diagram, ProjectRequest request, Path projectPath) throws IOException {
        // 1. Générer la structure CRUD
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), projectPath);
        
        // 2. Générer .csproj
        generateCsProj(projectPath, request.packageName());
        
        // 3. Générer Program.cs
        generateDotNetMain(projectPath, request.packageName());
        
        // 4. Générer README.md
        generateReadme(projectPath, ".NET", request.packageName());
    }
    
    private void generateSpringBootPom(Path projectPath, String packageName) throws IOException {
        String pomContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                     http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <groupId>%s</groupId>
                <artifactId>generated-crud-app</artifactId>
                <version>1.0.0</version>
                <packaging>jar</packaging>
                
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                </properties>
                
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                        <version>3.2.2</version>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-jpa</artifactId>
                        <version>3.2.2</version>
                    </dependency>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <version>2.2.224</version>
                        <scope>runtime</scope>
                    </dependency>
                </dependencies>
                
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-maven-plugin</artifactId>
                            <version>3.2.2</version>
                        </plugin>
                    </plugins>
                </build>
            </project>
            """.formatted(packageName);
        
        Files.writeString(projectPath.resolve("pom.xml"), pomContent);
    }
    
    private void generateApplicationYml(Path projectPath) throws IOException {
        String ymlContent = """
            server:
              port: 8080
            
            spring:
              application:
                name: generated-crud-app
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
        
        Path resourcesPath = projectPath.resolve("src/main/resources");
        Files.createDirectories(resourcesPath);
        Files.writeString(resourcesPath.resolve("application.yml"), ymlContent);
    }
    
    private void generateSpringBootMain(Path projectPath, String packageName) throws IOException {
        String mainContent = """
            package %s;
            
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            
            @SpringBootApplication
            public class GeneratedCrudApplication {
                public static void main(String[] args) {
                    SpringApplication.run(GeneratedCrudApplication.class, args);
                }
            }
            """.formatted(packageName);
        
        String packagePath = packageName.replace('.', '/');
        Path javaPath = projectPath.resolve("src/main/java").resolve(packagePath);
        Files.createDirectories(javaPath);
        Files.writeString(javaPath.resolve("GeneratedCrudApplication.java"), mainContent);
    }
    
    private void generateRequirementsTxt(Path projectPath) throws IOException {
        String requirements = """
            fastapi==0.104.1
            uvicorn==0.24.0
            sqlalchemy==2.0.23
            pydantic==2.5.0
            """;
        
        Files.writeString(projectPath.resolve("requirements.txt"), requirements);
    }
    
    private void generateFastAPIMain(Path projectPath) throws IOException {
        String mainContent = """
            from fastapi import FastAPI
            from sqlalchemy import create_engine
            from sqlalchemy.ext.declarative import declarative_base
            from sqlalchemy.orm import sessionmaker
            
            app = FastAPI(title="Generated CRUD API")
            
            # Database setup
            SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"
            engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False})
            SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
            Base = declarative_base()
            
            @app.get("/")
            def read_root():
                return {"message": "Generated CRUD API is running"}
            
            if __name__ == "__main__":
                import uvicorn
                uvicorn.run(app, host="0.0.0.0", port=8000)
            """;
        
        Files.writeString(projectPath.resolve("main.py"), mainContent);
    }
    
    private void generateCsProj(Path projectPath, String packageName) throws IOException {
        String csprojContent = """
            <Project Sdk="Microsoft.NET.Sdk.Web">
              <PropertyGroup>
                <TargetFramework>net8.0</TargetFramework>
                <Nullable>enable</Nullable>
                <ImplicitUsings>enable</ImplicitUsings>
              </PropertyGroup>
              
              <ItemGroup>
                <PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="8.0.0" />
                <PackageReference Include="Swashbuckle.AspNetCore" Version="6.4.0" />
              </ItemGroup>
            </Project>
            """;
        
        Files.writeString(projectPath.resolve("GeneratedCrudApp.csproj"), csprojContent);
    }
    
    private void generateDotNetMain(Path projectPath, String packageName) throws IOException {
        String programContent = """
            using Microsoft.EntityFrameworkCore;
            
            var builder = WebApplication.CreateBuilder(args);
            
            builder.Services.AddControllers();
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            builder.Services.AddDbContext<ApplicationDbContext>(options =>
                options.UseInMemoryDatabase("GeneratedCrudDb"));
            
            var app = builder.Build();
            
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }
            
            app.UseHttpsRedirection();
            app.UseAuthorization();
            app.MapControllers();
            
            app.Run();
            """;
        
        Files.writeString(projectPath.resolve("Program.cs"), programContent);
    }
    
    private void generateReadme(Path projectPath, String framework, String packageName) throws IOException {
        String readmeContent = """
            # Generated CRUD Application
            
            ## Framework: %s
            ## Package: %s
            
            This project was automatically generated from UML diagrams.
            
            ## Getting Started
            
            ### Prerequisites
            - %s
            
            ### Running the Application
            ```bash
            %s
            ```
            
            ## API Endpoints
            The application provides REST endpoints for all generated entities.
            
            ## Generated Structure
            - Entities with JPA annotations
            - Repositories for data access
            - Services for business logic
            - Controllers for REST endpoints
            """.formatted(
                framework, 
                packageName,
                getPrerequisites(framework),
                getRunCommand(framework)
            );
        
        Files.writeString(projectPath.resolve("README.md"), readmeContent);
    }
    
    private String getPrerequisites(String framework) {
        return switch (framework) {
            case "Spring Boot" -> "Java 17+, Maven 3.6+";
            case "FastAPI" -> "Python 3.8+, pip";
            case ".NET" -> ".NET 8.0+";
            default -> "See documentation";
        };
    }
    
    private String getRunCommand(String framework) {
        return switch (framework) {
            case "Spring Boot" -> "mvn spring-boot:run";
            case "FastAPI" -> "pip install -r requirements.txt && python main.py";
            case ".NET" -> "dotnet run";
            default -> "See documentation";
        };
    }
    
    public record ProjectRequest(
        String umlContent, 
        String packageName, 
        String outputPath, 
        String language
    ) {}
    
    public record GenerationResponse(
        boolean success, 
        String message, 
        String projectPath, 
        int classCount,
        String generationType
    ) {}
}
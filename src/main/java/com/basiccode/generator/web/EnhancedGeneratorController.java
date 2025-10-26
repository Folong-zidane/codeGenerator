package com.basiccode.generator.web;

import com.basiccode.generator.config.Framework;
import com.basiccode.generator.enhanced.*;
import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.DiagramParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/enhanced")
@CrossOrigin(origins = "*")
public class EnhancedGeneratorController {
    
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateEnhancedCrud(@RequestBody EnhancedGenerateRequest request) throws Exception {
        DiagramParser parser = new DiagramParser();
        Diagram diagram = parser.parse(request.umlContent());
        
        Path tempDir = Files.createTempDirectory("enhanced-generated");
        Framework framework = Framework.fromLanguage(request.language());
        
        // Generate enhanced code
        generateEnhancedProject(diagram, request, framework, tempDir);
        
        byte[] zipBytes = createZip(tempDir);
        deleteDirectory(tempDir);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=enhanced-" + request.language() + "-project.zip")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes);
    }
    
    private void generateEnhancedProject(Diagram diagram, EnhancedGenerateRequest request, 
                                       Framework framework, Path tempDir) throws IOException {
        
        // Generate entities with enhanced features
        EnhancedEntityGenerator entityGen = new EnhancedEntityGenerator();
        for (var clazz : diagram.getClasses()) {
            var entityFile = entityGen.generateEntity(clazz, request.packageName(), framework);
            writeJavaFile(entityFile, tempDir);
        }
        
        // Generate tests if requested
        if (request.generateTests()) {
            TestGenerator testGen = new TestGenerator();
            for (var clazz : diagram.getClasses()) {
                var testFile = testGen.generateUnitTests(clazz, request.packageName());
                writeJavaFile(testFile, tempDir);
            }
        }
        
        // Generate security configuration if requested
        if (request.generateSecurity()) {
            SecurityGenerator securityGen = new SecurityGenerator();
            var securityFile = securityGen.generateSecurityConfig(request.packageName());
            writeJavaFile(securityFile, tempDir);
        }
        
        // Generate OpenAPI documentation if requested
        if (request.generateDocumentation()) {
            OpenAPIGenerator apiGen = new OpenAPIGenerator();
            String swaggerSpec = apiGen.generateSwaggerSpec(diagram.getClasses(), request.packageName());
            Files.writeString(tempDir.resolve("openapi.yaml"), swaggerSpec);
        }
        
        // Generate project configuration files
        generateProjectFiles(framework, tempDir, request.packageName());
    }
    
    private void generateProjectFiles(Framework framework, Path tempDir, String packageName) throws IOException {
        switch (framework.getLanguage()) {
            case "java" -> generateJavaProjectFiles(framework, tempDir, packageName);
            case "python" -> generatePythonProjectFiles(framework, tempDir, packageName);
            case "csharp" -> generateCSharpProjectFiles(framework, tempDir, packageName);
            case "javascript" -> generateJavaScriptProjectFiles(framework, tempDir, packageName);
        }
    }
    
    private void generateJavaProjectFiles(Framework framework, Path tempDir, String packageName) throws IOException {
        // Generate pom.xml
        String pomXml = generateMavenPom(framework, packageName);
        Files.writeString(tempDir.resolve("pom.xml"), pomXml);
        
        // Generate application.properties
        String appProperties = generateApplicationProperties();
        Files.createDirectories(tempDir.resolve("src/main/resources"));
        Files.writeString(tempDir.resolve("src/main/resources/application.properties"), appProperties);
        
        // Generate main application class
        String mainClass = generateSpringBootMainClass(packageName);
        Files.createDirectories(tempDir.resolve("src/main/java/" + packageName.replace(".", "/")));
        Files.writeString(tempDir.resolve("src/main/java/" + packageName.replace(".", "/") + "/Application.java"), mainClass);
    }
    
    private void generatePythonProjectFiles(Framework framework, Path tempDir, String packageName) throws IOException {
        // Generate requirements.txt
        String requirements = String.join("\\n", framework.getDependencies());
        Files.writeString(tempDir.resolve("requirements.txt"), requirements);
        
        // Generate main.py
        String mainPy = generateFastAPIMain(packageName);
        Files.writeString(tempDir.resolve("main.py"), mainPy);
    }
    
    private void generateCSharpProjectFiles(Framework framework, Path tempDir, String packageName) throws IOException {
        // Generate .csproj file
        String csproj = generateCSharpProject(framework, packageName);
        Files.writeString(tempDir.resolve(packageName + ".csproj"), csproj);
        
        // Generate Program.cs
        String programCs = generateDotNetProgram(packageName);
        Files.writeString(tempDir.resolve("Program.cs"), programCs);
    }
    
    private void generateJavaScriptProjectFiles(Framework framework, Path tempDir, String packageName) throws IOException {
        // Generate package.json
        String packageJson = generateNodePackageJson(framework, packageName);
        Files.writeString(tempDir.resolve("package.json"), packageJson);
        
        // Generate app.js
        String appJs = generateExpressApp(packageName);
        Files.writeString(tempDir.resolve("app.js"), appJs);
    }
    
    private String generateMavenPom(Framework framework, String packageName) {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>%s</version>
                    <relativePath/>
                </parent>
                <groupId>%s</groupId>
                <artifactId>generated-crud-api</artifactId>
                <version>1.0.0</version>
                <properties>
                    <java.version>21</java.version>
                </properties>
                <dependencies>
                    %s
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
            """.formatted(
                framework.getConfig().get("version"),
                packageName,
                framework.getDependencies().stream()
                    .map(dep -> "<dependency><groupId>org.springframework.boot</groupId><artifactId>" + dep + "</artifactId></dependency>")
                    .reduce("", String::concat)
            );
    }
    
    private String generateApplicationProperties() {
        return """
            # Database Configuration
            spring.datasource.url=jdbc:h2:mem:testdb
            spring.datasource.driver-class-name=org.h2.Driver
            spring.datasource.username=sa
            spring.datasource.password=
            
            # JPA Configuration
            spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
            spring.jpa.hibernate.ddl-auto=create-drop
            spring.jpa.show-sql=true
            
            # Server Configuration
            server.port=8080
            
            # Logging
            logging.level.com.basiccode=DEBUG
            """;
    }
    
    private String generateSpringBootMainClass(String packageName) {
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
    
    private String generateFastAPIMain(String packageName) {
        return """
            from fastapi import FastAPI
            from fastapi.middleware.cors import CORSMiddleware
            
            app = FastAPI(title="Generated CRUD API", version="1.0.0")
            
            app.add_middleware(
                CORSMiddleware,
                allow_origins=["*"],
                allow_credentials=True,
                allow_methods=["*"],
                allow_headers=["*"],
            )
            
            @app.get("/")
            def read_root():
                return {"message": "Generated CRUD API"}
            
            if __name__ == "__main__":
                import uvicorn
                uvicorn.run(app, host="0.0.0.0", port=8000)
            """;
    }
    
    private String generateCSharpProject(Framework framework, String packageName) {
        return """
            <Project Sdk="Microsoft.NET.Sdk.Web">
                <PropertyGroup>
                    <TargetFramework>net%s</TargetFramework>
                    <Nullable>enable</Nullable>
                    <ImplicitUsings>enable</ImplicitUsings>
                </PropertyGroup>
                <ItemGroup>
                    %s
                </ItemGroup>
            </Project>
            """.formatted(
                framework.getConfig().get("version"),
                framework.getDependencies().stream()
                    .map(dep -> "<PackageReference Include=\"" + dep + "\" />")
                    .reduce("", String::concat)
            );
    }
    
    private String generateDotNetProgram(String packageName) {
        return """
            using Microsoft.EntityFrameworkCore;
            
            var builder = WebApplication.CreateBuilder(args);
            
            builder.Services.AddControllers();
            builder.Services.AddDbContext<ApplicationDbContext>(options =>
                options.UseInMemoryDatabase("GeneratedDb"));
            
            var app = builder.Build();
            
            if (app.Environment.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            
            app.UseRouting();
            app.MapControllers();
            
            app.Run();
            """;
    }
    
    private String generateNodePackageJson(Framework framework, String packageName) {
        return """
            {
                "name": "generated-crud-api",
                "version": "1.0.0",
                "description": "Generated CRUD API",
                "main": "app.js",
                "scripts": {
                    "start": "node app.js",
                    "dev": "nodemon app.js"
                },
                "dependencies": {
                    %s
                }
            }
            """.formatted(
                framework.getDependencies().stream()
                    .map(dep -> "\"" + dep + "\": \"latest\"")
                    .reduce("", (a, b) -> a.isEmpty() ? b : a + ",\\n    " + b)
            );
    }
    
    private String generateExpressApp(String packageName) {
        return """
            const express = require('express');
            const cors = require('cors');
            
            const app = express();
            const PORT = process.env.PORT || 3000;
            
            app.use(cors());
            app.use(express.json());
            
            app.get('/', (req, res) => {
                res.json({ message: 'Generated CRUD API' });
            });
            
            app.listen(PORT, () => {
                console.log(`Server running on port ${PORT}`);
            });
            """;
    }
    
    private void writeJavaFile(com.squareup.javapoet.JavaFile javaFile, Path tempDir) throws IOException {
        String packagePath = javaFile.packageName.replace(".", "/");
        Path packageDir = tempDir.resolve("src/main/java").resolve(packagePath);
        Files.createDirectories(packageDir);
        
        String fileName = javaFile.typeSpec.name + ".java";
        Files.writeString(packageDir.resolve(fileName), javaFile.toString());
    }
    
    private byte[] createZip(Path sourceDir) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String relativePath = sourceDir.relativize(file).toString();
                        ZipEntry entry = new ZipEntry(relativePath);
                        zos.putNextEntry(entry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
        return baos.toByteArray();
    }
    
    private void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {}
            });
    }
    
    public record EnhancedGenerateRequest(
        String umlContent,
        String packageName,
        String language,
        boolean generateTests,
        boolean generateSecurity,
        boolean generateDocumentation,
        String framework
    ) {
        public EnhancedGenerateRequest {
            if (language == null || language.isEmpty()) {
                language = "java";
            }
        }
    }
}
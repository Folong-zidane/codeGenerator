package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;

@Component
public class FastAPIInitializer implements ProjectInitializer {
    
    private static final String FASTAPI_VERSION = "0.104.1";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            createFastAPIStructure(projectPath, projectName);
            return projectPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FastAPI project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "python";
    }
    
    @Override
    public String getLatestVersion() {
        return FASTAPI_VERSION;
    }
    
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        try {
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path target = templatePath.resolve(generatedCodePath.relativize(source));
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to merge FastAPI code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private void createFastAPIStructure(Path projectPath, String projectName) throws IOException {
        // Create main.py
        String mainPy = """
            from fastapi import FastAPI
            from fastapi.middleware.cors import CORSMiddleware
            import uvicorn
            
            app = FastAPI(title="Generated API", version="1.0.0")
            
            app.add_middleware(
                CORSMiddleware,
                allow_origins=["*"],
                allow_credentials=True,
                allow_methods=["*"],
                allow_headers=["*"],
            )
            
            @app.get("/")
            def read_root():
                return {"message": "API is running"}
            
            if __name__ == "__main__":
                uvicorn.run(app, host="0.0.0.0", port=8000)
            """;
        Files.write(projectPath.resolve("main.py"), mainPy.getBytes());
        
        // Create requirements.txt
        String requirements = String.format("""
            fastapi==%s
            uvicorn[standard]==0.24.0
            sqlalchemy==2.0.23
            alembic==1.12.1
            pydantic==2.5.0
            """, FASTAPI_VERSION);
        Files.write(projectPath.resolve("requirements.txt"), requirements.getBytes());
    }
}
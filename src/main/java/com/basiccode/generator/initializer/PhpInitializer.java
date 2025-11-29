package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;

@Component
public class PhpInitializer implements ProjectInitializer {
    
    private static final String LARAVEL_VERSION = "10.0";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            createLaravelStructure(projectPath, projectName);
            return projectPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize PHP project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "php";
    }
    
    @Override
    public String getLatestVersion() {
        return LARAVEL_VERSION;
    }
    
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        try {
            Path appDir = templatePath.resolve("app");
            Files.createDirectories(appDir);
            
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path target = appDir.resolve(generatedCodePath.relativize(source));
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to merge PHP code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private void createLaravelStructure(Path projectPath, String projectName) throws IOException {
        // Create composer.json
        String composerJson = String.format("""
            {
                "name": "%s/%s",
                "type": "project",
                "description": "Generated Laravel API",
                "require": {
                    "php": "^8.1",
                    "laravel/framework": "^%s",
                    "laravel/sanctum": "^3.2"
                },
                "require-dev": {
                    "phpunit/phpunit": "^10.0"
                },
                "autoload": {
                    "psr-4": {
                        "App\\\\": "app/"
                    }
                }
            }
            """, projectName, projectName, LARAVEL_VERSION);
        Files.write(projectPath.resolve("composer.json"), composerJson.getBytes());
        
        // Create .env
        String env = """
            APP_NAME=GeneratedAPI
            APP_ENV=local
            APP_KEY=
            APP_DEBUG=true
            APP_URL=http://localhost
            
            DB_CONNECTION=sqlite
            DB_DATABASE=database/database.sqlite
            """;
        Files.write(projectPath.resolve(".env"), env.getBytes());
    }
}
package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.BaseFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class PhpFileWriter extends BaseFileWriter {

    @Override
    public String getFileExtension() {
        return ".php";
    }
    
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
        writeFile(outputPath + "/" + fileName, content);
    }
    
    public void writeFile(String fullPath, String content) {
        try {
            Path filePath = Paths.get(fullPath);
            
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Write the file
            Files.writeString(filePath, content);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to write PHP file: " + fullPath, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            Path base = Paths.get(basePath);
            
            // Create standard Laravel directory structure
            Files.createDirectories(base.resolve("app/Models"));
            Files.createDirectories(base.resolve("app/Http/Controllers/Api"));
            Files.createDirectories(base.resolve("app/Http/Requests"));
            Files.createDirectories(base.resolve("app/Http/Resources"));
            Files.createDirectories(base.resolve("app/Services"));
            Files.createDirectories(base.resolve("app/Repositories"));
            Files.createDirectories(base.resolve("app/Enums"));
            Files.createDirectories(base.resolve("app/Providers"));
            Files.createDirectories(base.resolve("database/migrations"));
            Files.createDirectories(base.resolve("database/seeders"));
            Files.createDirectories(base.resolve("routes"));
            Files.createDirectories(base.resolve("config"));
            Files.createDirectories(base.resolve("resources/views"));
            Files.createDirectories(base.resolve("storage/logs"));
            Files.createDirectories(base.resolve("tests/Feature"));
            Files.createDirectories(base.resolve("tests/Unit"));
            
            // Create additional directories if specified
            for (String directory : directories) {
                Files.createDirectories(base.resolve(directory));
            }
            
            // Create Laravel project files
            createLaravelProjectFiles(base);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Laravel project directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "laravel-project";
    }
    
    private String ensurePhpExtension(String fileName) {
        return fileName.endsWith(".php") ? fileName : fileName + ".php";
    }
    
    private void createLaravelProjectFiles(Path basePath) throws IOException {
        // Create composer.json
        String composerContent = generateComposerJson();
        Files.writeString(basePath.resolve("composer.json"), composerContent);
        
        // Create .env.example
        String envExampleContent = generateEnvExample();
        Files.writeString(basePath.resolve(".env.example"), envExampleContent);
        
        // Create artisan
        String artisanContent = generateArtisan();
        Path artisanPath = basePath.resolve("artisan");
        Files.writeString(artisanPath, artisanContent);
        artisanPath.toFile().setExecutable(true);
        
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
    
    private String generateComposerJson() {
        return "{\n" +
            "    \"name\": \"generated/laravel-api\",\n" +
            "    \"type\": \"project\",\n" +
            "    \"description\": \"Generated Laravel API\",\n" +
            "    \"require\": {\n" +
            "        \"php\": \"^8.1\",\n" +
            "        \"laravel/framework\": \"^10.10\"\n" +
            "    }\n" +
            "}\n";
    }
    
    private String generateEnvExample() {
        return "APP_NAME=\"Generated Laravel API\"\n" +
            "APP_ENV=local\n" +
            "APP_KEY=\n" +
            "APP_DEBUG=true\n" +
            "APP_URL=http://localhost\n" +
            "\n" +
            "DB_CONNECTION=mysql\n" +
            "DB_HOST=127.0.0.1\n" +
            "DB_PORT=3306\n" +
            "DB_DATABASE=generated_laravel_api\n" +
            "DB_USERNAME=root\n" +
            "DB_PASSWORD=\n";
    }
    
    private String generateArtisan() {
        return "#!/usr/bin/env php\n" +
            "<?php\n" +
            "\n" +
            "define('LARAVEL_START', microtime(true));\n" +
            "\n" +
            "require __DIR__.'/vendor/autoload.php';\n" +
            "\n" +
            "$app = require_once __DIR__.'/bootstrap/app.php';\n";
    }
    
    private String generateGitignore() {
        return "/node_modules\n" +
            "/vendor\n" +
            ".env\n" +
            ".env.backup\n" +
            ".phpunit.result.cache\n" +
            "/.idea\n" +
            "/.vscode\n";
    }
    
    private String generateReadme() {
        return "# Generated Laravel API\n" +
            "\n" +
            "This project was generated using UML-to-Code generator.\n" +
            "\n" +
            "## Quick Start\n" +
            "\n" +
            "```bash\n" +
            "composer install\n" +
            "cp .env.example .env\n" +
            "php artisan key:generate\n" +
            "php artisan migrate\n" +
            "php artisan serve\n" +
            "```\n";
    }
    
    private String generateStartScript() {
        return "#!/bin/bash\n" +
            "echo \"Starting Laravel API...\"\n" +
            "composer install\n" +
            "cp .env.example .env\n" +
            "php artisan key:generate\n" +
            "php artisan migrate --force\n" +
            "php artisan serve\n";
    }
}
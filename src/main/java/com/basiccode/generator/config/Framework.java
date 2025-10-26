package com.basiccode.generator.config;

import java.util.List;
import java.util.Map;

public enum Framework {
    SPRING_BOOT("java", "Spring Boot", 
        List.of("spring-boot-starter-web", "spring-boot-starter-data-jpa", "spring-boot-starter-validation"),
        Map.of("build", "maven", "version", "3.2.0")),
    
    FASTAPI("python", "FastAPI",
        List.of("fastapi", "sqlalchemy", "pydantic", "uvicorn"),
        Map.of("build", "pip", "version", "0.104.1")),
    
    DOTNET_CORE("csharp", ".NET Core",
        List.of("Microsoft.AspNetCore.App", "Microsoft.EntityFrameworkCore", "Microsoft.EntityFrameworkCore.SqlServer"),
        Map.of("build", "dotnet", "version", "8.0")),
    
    EXPRESS("javascript", "Express.js",
        List.of("express", "sequelize", "cors", "helmet"),
        Map.of("build", "npm", "version", "4.18.2")),
    
    LARAVEL("php", "Laravel",
        List.of("laravel/framework", "illuminate/database", "laravel/sanctum"),
        Map.of("build", "composer", "version", "10.0")),
    
    NODEJS_TYPESCRIPT("typescript", "Node.js TypeScript",
        List.of("express", "typescript", "@types/node", "@types/express", "sequelize", "cors"),
        Map.of("build", "npm", "version", "20.0")),
    
    DJANGO("django", "Django",
        List.of("Django>=4.0", "djangorestframework", "django-cors-headers", "psycopg2-binary"),
        Map.of("build", "pip", "version", "4.2")),
    
    PHP_LARAVEL("php", "PHP Laravel",
        List.of("laravel/framework", "laravel/sanctum", "doctrine/dbal"),
        Map.of("build", "composer", "version", "10.0"));
    
    private final String language;
    private final String name;
    private final List<String> dependencies;
    private final Map<String, String> config;
    
    Framework(String language, String name, List<String> dependencies, Map<String, String> config) {
        this.language = language;
        this.name = name;
        this.dependencies = dependencies;
        this.config = config;
    }
    
    public String getLanguage() { return language; }
    public String getName() { return name; }
    public List<String> getDependencies() { return dependencies; }
    public Map<String, String> getConfig() { return config; }
    
    public static Framework fromLanguage(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> SPRING_BOOT;
            case "python" -> FASTAPI;
            case "django" -> DJANGO;
            case "csharp" -> DOTNET_CORE;
            case "javascript" -> EXPRESS;
            case "typescript" -> NODEJS_TYPESCRIPT;
            case "php" -> PHP_LARAVEL;
            default -> SPRING_BOOT;
        };
    }
}
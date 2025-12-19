package com.basiccode.generator.util;

public class FileExtensionUtil {
    
    public static String getExtensionForLanguage(String language) {
        if (language == null) {
            return ".java";
        }
        
        switch (language.toLowerCase()) {
            case "java":
            case "spring":
            case "spring-boot":
                return ".java";
            case "python":
            case "django":
            case "fastapi":
                return ".py";
            case "typescript":
            case "nestjs":
            case "express":
                return ".ts";
            case "javascript":
            case "node":
                return ".js";
            case "csharp":
            case "dotnet":
                return ".cs";
            case "php":
            case "laravel":
                return ".php";
            default:
                return ".java";
        }
    }
    
    public static String getDirectoryForLanguage(String language) {
        if (language == null) {
            return "java";
        }
        
        switch (language.toLowerCase()) {
            case "java":
            case "spring":
            case "spring-boot":
                return "java";
            case "python":
            case "django":
            case "fastapi":
                return "python";
            case "typescript":
            case "nestjs":
            case "express":
                return "typescript";
            case "javascript":
            case "node":
                return "javascript";
            case "csharp":
            case "dotnet":
                return "csharp";
            case "php":
            case "laravel":
                return "php";
            default:
                return "java";
        }
    }
    
    public static String getEntityDirectoryForLanguage(String language) {
        return getDirectoryForLanguage(language);
    }
    
    public static String getRepositoryDirectoryForLanguage(String language) {
        return getDirectoryForLanguage(language);
    }
    
    public static String getServiceDirectoryForLanguage(String language) {
        return getDirectoryForLanguage(language);
    }
    
    public static String getControllerDirectoryForLanguage(String language) {
        return getDirectoryForLanguage(language);
    }
}
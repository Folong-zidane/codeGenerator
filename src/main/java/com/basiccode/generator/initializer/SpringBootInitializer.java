package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@Component
public class SpringBootInitializer implements ProjectInitializer {
    
    private static final String SPRING_INITIALIZR_URL = "https://start.spring.io";
    private static final String LATEST_SPRING_VERSION = "3.2.1";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            // Download Spring Boot template from start.spring.io
            String url = buildInitializrUrl(projectName, packageName);
            Path templatePath = downloadSpringTemplate(url, projectName);
            
            return templatePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Spring Boot project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "java";
    }
    
    @Override
    public String getLatestVersion() {
        return LATEST_SPRING_VERSION;
    }
    
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        try {
            // Copy generated entities, services, controllers to template
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path target = templatePath.resolve(generatedCodePath.relativize(source));
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to merge code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private String buildInitializrUrl(String projectName, String packageName) {
        return String.format("%s/starter.zip?type=maven-project&language=java&bootVersion=%s" +
                "&baseDir=%s&groupId=%s&artifactId=%s&name=%s&packageName=%s" +
                "&dependencies=web,data-jpa,h2,validation,actuator",
                SPRING_INITIALIZR_URL, LATEST_SPRING_VERSION, projectName, 
                packageName, projectName, projectName, packageName);
    }
    
    private Path downloadSpringTemplate(String url, String projectName) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
                
        HttpResponse<byte[]> response = client.send(request, 
                HttpResponse.BodyHandlers.ofByteArray());
                
        Path zipPath = Paths.get("temp", projectName + ".zip");
        Files.createDirectories(zipPath.getParent());
        Files.write(zipPath, response.body());
        
        // Extract ZIP
        return extractZip(zipPath, Paths.get("temp", projectName));
    }
    
    private Path extractZip(Path zipPath, Path targetDir) throws IOException {
        // ZIP extraction logic
        Files.createDirectories(targetDir);
        return targetDir;
    }
}
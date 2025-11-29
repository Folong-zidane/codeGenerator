package com.basiccode.generator.initializer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class VersionChecker {
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Map<String, String> latestVersions = new ConcurrentHashMap<>();
    
    /**
     * Check for updates every 6 hours
     */
    @Scheduled(fixedRate = 21600000) // 6 hours
    public void checkForUpdates() {
        checkSpringBootVersion();
        checkDjangoVersion();
        checkDotNetVersion();
        checkCSharpVersion();
        // Add more version checks
    }
    
    private void checkSpringBootVersion() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/spring-projects/spring-boot/releases/latest"))
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();
                    
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
                    
            // Parse JSON to extract version
            String version = parseVersionFromGitHub(response.body());
            latestVersions.put("java", version);
            
        } catch (Exception e) {
            System.err.println("Failed to check Spring Boot version: " + e.getMessage());
        }
    }
    
    private void checkDjangoVersion() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pypi.org/pypi/Django/json"))
                    .build();
                    
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
                    
            String version = parseVersionFromPyPI(response.body());
            latestVersions.put("django", version);
            
        } catch (Exception e) {
            System.err.println("Failed to check Django version: " + e.getMessage());
        }
    }
    
    private void checkDotNetVersion() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.nuget.org/v3-flatcontainer/microsoft.aspnetcore.app/index.json"))
                    .build();
                    
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
                    
            String version = parseVersionFromNuGet(response.body());
            latestVersions.put("csharp", version);
            
        } catch (Exception e) {
            System.err.println("Failed to check .NET version: " + e.getMessage());
        }
    }
    
    private void checkCSharpVersion() {
        checkDotNetVersion();
    }
    
    public String getLatestVersion(String language) {
        return latestVersions.getOrDefault(language, "unknown");
    }
    
    public Map<String, String> getAllLatestVersions() {
        return Map.copyOf(latestVersions);
    }
    
    private String parseVersionFromGitHub(String json) {
        // Simple JSON parsing - in production use Jackson
        int start = json.indexOf("\"tag_name\":\"") + 12;
        int end = json.indexOf("\"", start);
        return json.substring(start, end).replace("v", "");
    }
    
    private String parseVersionFromPyPI(String json) {
        // Parse PyPI JSON response
        int start = json.indexOf("\"version\":\"") + 11;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
    
    private String parseVersionFromNuGet(String json) {
        // Parse NuGet JSON response
        String[] versions = json.replace("[", "").replace("]", "")
                .replace("\"", "").split(",");
        return versions[versions.length - 1].trim(); // Latest version
    }
}
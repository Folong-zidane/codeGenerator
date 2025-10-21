package com.basiccode.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "crud-generator", 
         mixinStandardHelpOptions = true,
         version = "1.0.0",
         description = "UML-to-CRUD Generator Java CLI Client")
public class CrudGeneratorCLI implements Callable<Integer> {
    
    @Parameters(index = "0", description = "Command: generate, validate")
    private String command;
    
    @Option(names = {"-u", "--uml"}, required = true, description = "UML diagram file")
    private Path umlFile;
    
    @Option(names = {"-o", "--output"}, required = true, description = "Output directory")
    private String outputPath;
    
    @Option(names = {"-f", "--framework"}, description = "Target framework", defaultValue = "spring-boot")
    private String framework;
    
    @Option(names = {"-p", "--package"}, description = "Package name", defaultValue = "com.example")
    private String packageName;
    
    @Option(names = {"--api-url"}, description = "API URL", defaultValue = "http://localhost:8080")
    private String apiUrl;
    
    @Override
    public Integer call() throws Exception {
        System.out.println("🚀 UML-to-CRUD Generator (Java CLI)");
        
        switch (command.toLowerCase()) {
            case "generate" -> generateCode();
            case "validate" -> validateUml();
            default -> {
                System.err.println("❌ Unknown command: " + command);
                return 1;
            }
        }
        
        return 0;
    }
    
    private void generateCode() throws IOException, InterruptedException {
        System.out.println("📁 Generating " + framework + " project from " + umlFile);
        
        String umlContent = Files.readString(umlFile);
        
        String jsonPayload = String.format("""
            {
                "umlContent": "%s",
                "packageName": "%s",
                "outputPath": "%s",
                "generationType": "COMPLETE_PROJECT",
                "framework": "%s"
            }
            """, 
            umlContent.replace("\"", "\\\"").replace("\n", "\\n"),
            packageName,
            outputPath,
            framework.toUpperCase().replace("-", "_")
        );
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "/api/v2/generate/files"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("✅ Generation completed successfully!");
            System.out.println("📂 Project created at: " + outputPath);
            showNextSteps();
        } else {
            System.err.println("❌ API Error: " + response.statusCode());
            System.err.println(response.body());
        }
    }
    
    private void validateUml() throws IOException, InterruptedException {
        System.out.println("🔍 Validating " + umlFile);
        
        String umlContent = Files.readString(umlFile);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "/api/generate/validate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("\"" + umlContent.replace("\"", "\\\"") + "\""))
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("✅ UML validation completed");
        } else {
            System.err.println("❌ Validation failed");
        }
    }
    
    private void showNextSteps() {
        System.out.println("\n🎯 Next Steps:");
        switch (framework) {
            case "spring-boot" -> {
                System.out.println("  cd " + outputPath);
                System.out.println("  mvn spring-boot:run");
            }
            case "django" -> {
                System.out.println("  cd " + outputPath);
                System.out.println("  pip install -r requirements.txt");
                System.out.println("  python manage.py runserver");
            }
        }
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new CrudGeneratorCLI()).execute(args);
        System.exit(exitCode);
    }
}
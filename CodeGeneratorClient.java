import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorClient {
    private static final String API_URL = "https://codegenerator-cpyh.onrender.com";
    private final HttpClient client = HttpClient.newHttpClient();
    
    public boolean validateUML(String umlContent) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL + "/api/generate/validate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(umlContent))
            .build();
            
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().contains("\"valid\":true");
    }
    
    public void generateProject(String umlFile, String packageName, String outputDir) throws Exception {
        // Lire le fichier UML
        String umlContent = Files.readString(Paths.get(umlFile));
        
        // Créer la requête JSON
        String jsonRequest = String.format("""
            {
              "umlContent": %s,
              "packageName": "%s"
            }
            """, escapeJson(umlContent), packageName);
        
        // Appeler l'API
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL + "/api/generate/crud"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build();
            
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            // Sauvegarder le ZIP
            Files.write(Paths.get("generated-code.zip"), response.body());
            
            // Extraire et créer le projet complet
            extractAndSetupProject(outputDir, packageName);
            
            System.out.println("✅ Projet généré dans: " + outputDir);
        } else {
            throw new RuntimeException("Erreur API: " + response.statusCode());
        }
    }
    
    private void extractAndSetupProject(String outputDir, String packageName) throws Exception {
        // Extraire le ZIP
        ProcessBuilder pb = new ProcessBuilder("unzip", "-o", "generated-code.zip", "-d", outputDir);
        pb.start().waitFor();
        
        // Créer pom.xml
        createPomXml(outputDir, packageName);
        
        // Créer Application.java
        createMainApplication(outputDir, packageName);
        
        // Créer application.yml
        createApplicationYml(outputDir);
        
        // Nettoyer
        Files.deleteIfExists(Paths.get("generated-code.zip"));
    }
    
    private void createPomXml(String outputDir, String packageName) throws Exception {
        String pomContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                     http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>3.2.2</version>
                    <relativePath/>
                </parent>
                
                <groupId>%s</groupId>
                <artifactId>generated-app</artifactId>
                <version>1.0.0</version>
                <packaging>jar</packaging>
                
                <properties>
                    <java.version>17</java.version>
                </properties>
                
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-jpa</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <scope>runtime</scope>
                    </dependency>
                    <dependency>
                        <groupId>org.springdoc</groupId>
                        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                        <version>2.3.0</version>
                    </dependency>
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
            """.formatted(packageName);
            
        Files.writeString(Paths.get(outputDir, "pom.xml"), pomContent);
    }
    
    private void createMainApplication(String outputDir, String packageName) throws Exception {
        String appContent = """
            package %s;
            
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            
            @SpringBootApplication
            public class GeneratedApplication {
                public static void main(String[] args) {
                    SpringApplication.run(GeneratedApplication.class, args);
                }
            }
            """.formatted(packageName);
            
        String packagePath = packageName.replace('.', '/');
        Path javaDir = Paths.get(outputDir, "src/main/java", packagePath);
        Files.createDirectories(javaDir);
        Files.writeString(javaDir.resolve("GeneratedApplication.java"), appContent);
    }
    
    private void createApplicationYml(String outputDir) throws Exception {
        String ymlContent = """
            server:
              port: 8080
            
            spring:
              application:
                name: generated-app
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
            
            springdoc:
              api-docs:
                path: /api-docs
              swagger-ui:
                path: /swagger-ui.html
            """;
            
        Path resourcesDir = Paths.get(outputDir, "src/main/resources");
        Files.createDirectories(resourcesDir);
        Files.writeString(resourcesDir.resolve("application.yml"), ymlContent);
    }
    
    private String escapeJson(String str) {
        return "\"" + str.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }
    
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java CodeGeneratorClient <umlFile> <packageName> <outputDir>");
            return;
        }
        
        try {
            CodeGeneratorClient client = new CodeGeneratorClient();
            client.generateProject(args[0], args[1], args[2]);
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
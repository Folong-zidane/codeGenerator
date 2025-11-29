package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * C# File writer for .NET Core projects
 * Handles writing C# files with proper directory structure
 */
public class CSharpFileWriter implements IFileWriter {
    
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
        try {
            Path basePath = Paths.get(outputPath);
            Path filePath;
            
            // Determine the correct file path based on content type
            if (content.contains("namespace") && content.contains(".Models")) {
                filePath = basePath.resolve("Models").resolve(ensureCsExtension(fileName));
            } else if (content.contains("namespace") && content.contains(".Controllers")) {
                filePath = basePath.resolve("Controllers").resolve(ensureCsExtension(fileName));
            } else if (content.contains("namespace") && content.contains(".Services")) {
                filePath = basePath.resolve("Services").resolve(ensureCsExtension(fileName));
            } else if (content.contains("namespace") && content.contains(".Repositories")) {
                filePath = basePath.resolve("Repositories").resolve(ensureCsExtension(fileName));
            } else if (content.contains("namespace") && content.contains(".Data")) {
                filePath = basePath.resolve("Data").resolve(ensureCsExtension(fileName));
            } else if (content.contains("namespace") && content.contains(".Enums")) {
                filePath = basePath.resolve("Enums").resolve(ensureCsExtension(fileName));
            } else {
                filePath = basePath.resolve(ensureCsExtension(fileName));
            }
            
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Write the file
            Files.writeString(filePath, content);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to write C# file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            Path base = Paths.get(basePath);
            
            // Create standard .NET Core directory structure
            Files.createDirectories(base.resolve("Models"));
            Files.createDirectories(base.resolve("Controllers"));
            Files.createDirectories(base.resolve("Services"));
            Files.createDirectories(base.resolve("Repositories"));
            Files.createDirectories(base.resolve("Data"));
            Files.createDirectories(base.resolve("Enums"));
            Files.createDirectories(base.resolve("Properties"));
            
            // Create additional directories if specified
            for (String directory : directories) {
                Files.createDirectories(base.resolve(directory));
            }
            
            // Create project files
            createProjectFiles(base);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to create C# project directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "csharp-project";
    }
    
    private String ensureCsExtension(String fileName) {
        return fileName.endsWith(".cs") ? fileName : fileName + ".cs";
    }
    
    private void createProjectFiles(Path basePath) throws IOException {
        // Create .csproj file
        String projectContent = generateProjectFile();
        Files.writeString(basePath.resolve("Project.csproj"), projectContent);
        
        // Create appsettings.json
        String appSettingsContent = generateAppSettings();
        Files.writeString(basePath.resolve("appsettings.json"), appSettingsContent);
        
        // Create appsettings.Development.json
        String devAppSettingsContent = generateDevAppSettings();
        Files.writeString(basePath.resolve("appsettings.Development.json"), devAppSettingsContent);
        
        // Create launchSettings.json
        Path propertiesPath = basePath.resolve("Properties");
        Files.createDirectories(propertiesPath);
        String launchSettingsContent = generateLaunchSettings();
        Files.writeString(propertiesPath.resolve("launchSettings.json"), launchSettingsContent);
        
        // Create README.md
        String readmeContent = generateReadme();
        Files.writeString(basePath.resolve("README.md"), readmeContent);
        
        // Create start script
        String startScript = generateStartScript();
        Path startScriptPath = basePath.resolve("start.sh");
        Files.writeString(startScriptPath, startScript);
        startScriptPath.toFile().setExecutable(true);
    }
    
    private String generateProjectFile() {
        return """
            <Project Sdk="Microsoft.NET.Sdk.Web">
            
              <PropertyGroup>
                <TargetFramework>net8.0</TargetFramework>
                <Nullable>enable</Nullable>
                <ImplicitUsings>enable</ImplicitUsings>
              </PropertyGroup>
            
              <ItemGroup>
                <PackageReference Include="Microsoft.AspNetCore.OpenApi" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="8.0.0" />
                <PackageReference Include="Swashbuckle.AspNetCore" Version="6.4.0" />
              </ItemGroup>
            
            </Project>
            """;
    }
    
    private String generateAppSettings() {
        return """
            {
              "ConnectionStrings": {
                "DefaultConnection": "Server=(localdb)\\\\mssqllocaldb;Database=GeneratedAppDb;Trusted_Connection=true;MultipleActiveResultSets=true"
              },
              "Logging": {
                "LogLevel": {
                  "Default": "Information",
                  "Microsoft.AspNetCore": "Warning"
                }
              },
              "AllowedHosts": "*"
            }
            """;
    }
    
    private String generateDevAppSettings() {
        return """
            {
              "Logging": {
                "LogLevel": {
                  "Default": "Information",
                  "Microsoft.AspNetCore": "Warning",
                  "Microsoft.EntityFrameworkCore.Database.Command": "Information"
                }
              }
            }
            """;
    }
    
    private String generateLaunchSettings() {
        return """
            {
              "$schema": "http://json.schemastore.org/launchsettings.json",
              "iisSettings": {
                "windowsAuthentication": false,
                "anonymousAuthentication": true,
                "iisExpress": {
                  "applicationUrl": "http://localhost:5000",
                  "sslPort": 5001
                }
              },
              "profiles": {
                "http": {
                  "commandName": "Project",
                  "dotnetRunMessages": true,
                  "launchBrowser": true,
                  "launchUrl": "swagger",
                  "applicationUrl": "http://localhost:5000",
                  "environmentVariables": {
                    "ASPNETCORE_ENVIRONMENT": "Development"
                  }
                },
                "https": {
                  "commandName": "Project",
                  "dotnetRunMessages": true,
                  "launchBrowser": true,
                  "launchUrl": "swagger",
                  "applicationUrl": "https://localhost:5001;http://localhost:5000",
                  "environmentVariables": {
                    "ASPNETCORE_ENVIRONMENT": "Development"
                  }
                },
                "IIS Express": {
                  "commandName": "IISExpress",
                  "launchBrowser": true,
                  "launchUrl": "swagger",
                  "environmentVariables": {
                    "ASPNETCORE_ENVIRONMENT": "Development"
                  }
                }
              }
            }
            """;
    }
    
    private String generateReadme() {
        return """
            # 🚀 Generated .NET Core Web API
            
            This project was generated using UML-to-Code generator with comprehensive C# support.
            
            ## 🏗️ Architecture
            
            - **Models**: Entity classes with Entity Framework Core annotations
            - **Controllers**: ASP.NET Core Web API controllers with REST endpoints
            - **Services**: Business logic layer with dependency injection
            - **Repositories**: Data access layer with async/await patterns
            - **Data**: Entity Framework Core DbContext and configuration
            
            ## 🚀 Quick Start
            
            ```bash
            # Restore packages
            dotnet restore
            
            # Run the application
            dotnet run
            
            # Or use the start script
            ./start.sh
            ```
            
            ## 📊 API Documentation
            
            Once running, visit:
            - Swagger UI: https://localhost:5001/swagger
            - API: https://localhost:5001/api
            
            ## 🗄️ Database
            
            The application uses Entity Framework Core with SQL Server LocalDB by default.
            The database will be created automatically on first run.
            
            ## 🔧 Configuration
            
            Update `appsettings.json` to configure:
            - Database connection string
            - Logging levels
            - Other application settings
            
            ## 📝 Features
            
            ✅ Complete CRUD operations
            ✅ State management (if applicable)
            ✅ Async/await patterns
            ✅ Dependency injection
            ✅ Entity Framework Core
            ✅ Swagger documentation
            ✅ Error handling
            ✅ Audit fields (CreatedAt, UpdatedAt)
            """;
    }
    
    private String generateStartScript() {
        return """
            #!/bin/bash
            echo "🚀 Starting .NET Core Web API..."
            echo "📦 Restoring packages..."
            dotnet restore
            
            echo "🏗️ Building project..."
            dotnet build
            
            echo "🌐 Starting application..."
            echo "📊 Swagger UI will be available at: https://localhost:5001/swagger"
            dotnet run
            """;
    }
}
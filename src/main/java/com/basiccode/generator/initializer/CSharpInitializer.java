package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;

@Component
public class CSharpInitializer implements ProjectInitializer {
    
    private static final String DOTNET_VERSION = "8.0";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            createDotNetStructure(projectPath, projectName, packageName);
            return projectPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize C# project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "csharp";
    }
    
    @Override
    public String getLatestVersion() {
        return DOTNET_VERSION;
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
                        throw new RuntimeException("Failed to merge C# code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private void createDotNetStructure(Path projectPath, String projectName, String packageName) throws IOException {
        // Create .csproj file
        String csproj = String.format("""
            <Project Sdk="Microsoft.NET.Sdk.Web">
              <PropertyGroup>
                <TargetFramework>net%s</TargetFramework>
                <RootNamespace>%s</RootNamespace>
              </PropertyGroup>
              <ItemGroup>
                <PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="8.0.0" />
                <PackageReference Include="Swashbuckle.AspNetCore" Version="6.5.0" />
              </ItemGroup>
            </Project>
            """, DOTNET_VERSION, packageName);
        Files.write(projectPath.resolve(projectName + ".csproj"), csproj.getBytes());
        
        // Create Program.cs
        String program = String.format("""
            using Microsoft.EntityFrameworkCore;
            
            var builder = WebApplication.CreateBuilder(args);
            
            builder.Services.AddControllers();
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            
            var app = builder.Build();
            
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }
            
            app.UseHttpsRedirection();
            app.UseAuthorization();
            app.MapControllers();
            
            app.Run();
            """);
        Files.write(projectPath.resolve("Program.cs"), program.getBytes());
    }
}
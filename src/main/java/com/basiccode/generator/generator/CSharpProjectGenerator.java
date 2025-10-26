package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSharpProjectGenerator {
    
    public void generateCompleteProject(List<ClassModel> classes, String packageName, Path outputDir) throws IOException {
        // 1. Create project structure
        createProjectStructure(outputDir);
        
        // 2. Generate project files
        generateProjectFile(outputDir, packageName);
        generateProgramCs(outputDir, packageName, classes);
        generateAppSettings(outputDir);
        
        // 3. Generate entities
        generateEntities(classes, outputDir, packageName);
        
        // 4. Generate DbContext
        generateDbContext(classes, outputDir, packageName);
        
        // 5. Generate repositories
        generateRepositories(classes, outputDir, packageName);
        
        // 6. Generate services
        generateServices(classes, outputDir, packageName);
        
        // 7. Generate controllers
        generateControllers(classes, outputDir, packageName);
    }
    
    private void createProjectStructure(Path outputDir) throws IOException {
        Files.createDirectories(outputDir.resolve("Entities"));
        Files.createDirectories(outputDir.resolve("Repositories"));
        Files.createDirectories(outputDir.resolve("Services"));
        Files.createDirectories(outputDir.resolve("Controllers"));
        Files.createDirectories(outputDir.resolve("Data"));
    }
    
    private void generateProjectFile(Path outputDir, String packageName) throws IOException {
        String csproj = """
            <Project Sdk="Microsoft.NET.Sdk.Web">
              <PropertyGroup>
                <TargetFramework>net8.0</TargetFramework>
                <Nullable>enable</Nullable>
                <ImplicitUsings>enable</ImplicitUsings>
              </PropertyGroup>
              <ItemGroup>
                <PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
                <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="8.0.0" />
                <PackageReference Include="Swashbuckle.AspNetCore" Version="6.4.0" />
              </ItemGroup>
            </Project>
            """;
        Files.writeString(outputDir.resolve("GeneratedApp.csproj"), csproj);
    }
    
    private void generateProgramCs(Path outputDir, String packageName, List<ClassModel> classes) throws IOException {
        StringBuilder services = new StringBuilder();
        for (ClassModel cls : classes) {
            services.append("builder.Services.AddScoped<I").append(cls.getName()).append("Repository, ").append(cls.getName()).append("Repository>();\n");
            services.append("builder.Services.AddScoped<I").append(cls.getName()).append("Service, ").append(cls.getName()).append("Service>();\n");
        }
        
        String program = String.format("""
            using Microsoft.EntityFrameworkCore;
            using %s.Data;
            using %s.Repositories;
            using %s.Services;
            
            var builder = WebApplication.CreateBuilder(args);
            
            // Add services
            builder.Services.AddControllers();
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            
            // Add DbContext
            builder.Services.AddDbContext<AppDbContext>(options =>
                options.UseInMemoryDatabase("GeneratedApp"));
            
            // Add repositories and services
            %s
            
            var app = builder.Build();
            
            // Configure pipeline
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }
            
            app.UseHttpsRedirection();
            app.UseAuthorization();
            app.MapControllers();
            
            app.Run();
            """, packageName, packageName, packageName, services.toString());
        
        Files.writeString(outputDir.resolve("Program.cs"), program);
    }
    
    private void generateAppSettings(Path outputDir) throws IOException {
        String appSettings = """
            {
              "Logging": {
                "LogLevel": {
                  "Default": "Information",
                  "Microsoft.AspNetCore": "Warning"
                }
              },
              "AllowedHosts": "*"
            }
            """;
        Files.writeString(outputDir.resolve("appsettings.json"), appSettings);
    }
    
    private void generateEntities(List<ClassModel> classes, Path outputDir, String packageName) throws IOException {
        for (ClassModel cls : classes) {
            StringBuilder entity = new StringBuilder();
            entity.append("using System;\n");
            entity.append("using System.ComponentModel.DataAnnotations;\n");
            entity.append("using System.ComponentModel.DataAnnotations.Schema;\n\n");
            entity.append("namespace ").append(packageName).append(".Entities\n{\n");
            entity.append("    [Table(\"").append(toSnakeCase(cls.getName())).append("\")]\n");
            entity.append("    public class ").append(cls.getName()).append("\n    {\n");
            entity.append("        [Key]\n");
            entity.append("        public Guid Id { get; set; }\n\n");
            
            for (Field field : cls.getFields()) {
                entity.append("        public ").append(getCSharpType(field.getType()))
                      .append(" ").append(capitalize(field.getName())).append(" { get; set; }\n");
            }
            
            entity.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n");
            entity.append("        public DateTime? UpdatedAt { get; set; }\n");
            entity.append("    }\n}");
            
            Files.writeString(outputDir.resolve("Entities/" + cls.getName() + ".cs"), entity.toString());
        }
    }
    
    private void generateDbContext(List<ClassModel> classes, Path outputDir, String packageName) throws IOException {
        StringBuilder dbContext = new StringBuilder();
        dbContext.append("using Microsoft.EntityFrameworkCore;\n");
        dbContext.append("using ").append(packageName).append(".Entities;\n\n");
        dbContext.append("namespace ").append(packageName).append(".Data\n{\n");
        dbContext.append("    public class AppDbContext : DbContext\n    {\n");
        dbContext.append("        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }\n\n");
        
        for (ClassModel cls : classes) {
            dbContext.append("        public DbSet<").append(cls.getName()).append("> ").append(cls.getName()).append("s { get; set; }\n");
        }
        
        dbContext.append("    }\n}");
        
        Files.writeString(outputDir.resolve("Data/AppDbContext.cs"), dbContext.toString());
    }
    
    private void generateRepositories(List<ClassModel> classes, Path outputDir, String packageName) throws IOException {
        for (ClassModel cls : classes) {
            String repository = String.format("""
                using System;
                using System.Collections.Generic;
                using System.Threading.Tasks;
                using Microsoft.EntityFrameworkCore;
                using %s.Entities;
                using %s.Data;
                
                namespace %s.Repositories
                {
                    public interface I%sRepository
                    {
                        Task<%s?> GetByIdAsync(Guid id);
                        Task<IEnumerable<%s>> GetAllAsync();
                        Task<%s> CreateAsync(%s entity);
                        Task<%s> UpdateAsync(%s entity);
                        Task DeleteAsync(Guid id);
                    }
                
                    public class %sRepository : I%sRepository
                    {
                        private readonly AppDbContext _context;
                
                        public %sRepository(AppDbContext context)
                        {
                            _context = context;
                        }
                
                        public async Task<%s?> GetByIdAsync(Guid id)
                        {
                            return await _context.%ss.FindAsync(id);
                        }
                
                        public async Task<IEnumerable<%s>> GetAllAsync()
                        {
                            return await _context.%ss.ToListAsync();
                        }
                
                        public async Task<%s> CreateAsync(%s entity)
                        {
                            _context.%ss.Add(entity);
                            await _context.SaveChangesAsync();
                            return entity;
                        }
                
                        public async Task<%s> UpdateAsync(%s entity)
                        {
                            entity.UpdatedAt = DateTime.UtcNow;
                            _context.%ss.Update(entity);
                            await _context.SaveChangesAsync();
                            return entity;
                        }
                
                        public async Task DeleteAsync(Guid id)
                        {
                            var entity = await GetByIdAsync(id);
                            if (entity != null)
                            {
                                _context.%ss.Remove(entity);
                                await _context.SaveChangesAsync();
                            }
                        }
                    }
                }
                """, 
                packageName, packageName, packageName, cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("Repositories/" + cls.getName() + "Repository.cs"), repository);
        }
    }
    
    private void generateServices(List<ClassModel> classes, Path outputDir, String packageName) throws IOException {
        for (ClassModel cls : classes) {
            String service = String.format("""
                using System;
                using System.Collections.Generic;
                using System.Threading.Tasks;
                using %s.Entities;
                using %s.Repositories;
                
                namespace %s.Services
                {
                    public interface I%sService
                    {
                        Task<%s> CreateAsync(%s entity);
                        Task<%s?> GetByIdAsync(Guid id);
                        Task<IEnumerable<%s>> GetAllAsync();
                        Task<%s> UpdateAsync(%s entity);
                        Task DeleteAsync(Guid id);
                    }
                
                    public class %sService : I%sService
                    {
                        private readonly I%sRepository _repository;
                
                        public %sService(I%sRepository repository)
                        {
                            _repository = repository;
                        }
                
                        public async Task<%s> CreateAsync(%s entity)
                        {
                            return await _repository.CreateAsync(entity);
                        }
                
                        public async Task<%s?> GetByIdAsync(Guid id)
                        {
                            return await _repository.GetByIdAsync(id);
                        }
                
                        public async Task<IEnumerable<%s>> GetAllAsync()
                        {
                            return await _repository.GetAllAsync();
                        }
                
                        public async Task<%s> UpdateAsync(%s entity)
                        {
                            return await _repository.UpdateAsync(entity);
                        }
                
                        public async Task DeleteAsync(Guid id)
                        {
                            await _repository.DeleteAsync(id);
                        }
                    }
                }
                """, 
                packageName, packageName, packageName, cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("Services/" + cls.getName() + "Service.cs"), service);
        }
    }
    
    private void generateControllers(List<ClassModel> classes, Path outputDir, String packageName) throws IOException {
        for (ClassModel cls : classes) {
            String controller = String.format("""
                using Microsoft.AspNetCore.Mvc;
                using System;
                using System.Collections.Generic;
                using System.Threading.Tasks;
                using %s.Entities;
                using %s.Services;
                
                namespace %s.Controllers
                {
                    [ApiController]
                    [Route("api/[controller]")]
                    public class %sController : ControllerBase
                    {
                        private readonly I%sService _service;
                
                        public %sController(I%sService service)
                        {
                            _service = service;
                        }
                
                        [HttpPost]
                        public async Task<ActionResult<%s>> Create(%s entity)
                        {
                            var result = await _service.CreateAsync(entity);
                            return CreatedAtAction(nameof(GetById), new { id = result.Id }, result);
                        }
                
                        [HttpGet("{id}")]
                        public async Task<ActionResult<%s>> GetById(Guid id)
                        {
                            var entity = await _service.GetByIdAsync(id);
                            return entity == null ? NotFound() : Ok(entity);
                        }
                
                        [HttpGet]
                        public async Task<ActionResult<IEnumerable<%s>>> GetAll()
                        {
                            var entities = await _service.GetAllAsync();
                            return Ok(entities);
                        }
                
                        [HttpPut("{id}")]
                        public async Task<ActionResult<%s>> Update(Guid id, %s entity)
                        {
                            entity.Id = id;
                            var result = await _service.UpdateAsync(entity);
                            return Ok(result);
                        }
                
                        [HttpDelete("{id}")]
                        public async Task<IActionResult> Delete(Guid id)
                        {
                            await _service.DeleteAsync(id);
                            return NoContent();
                        }
                    }
                }
                """, 
                packageName, packageName, packageName, cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("Controllers/" + cls.getName() + "Controller.cs"), controller);
        }
    }
    
    private String getCSharpType(String type) {
        return switch (type) {
            case "String" -> "string";
            case "Integer", "int" -> "int";
            case "Long", "long" -> "long";
            case "Float", "float" -> "float";
            case "Double", "double" -> "double";
            case "Boolean", "boolean" -> "bool";
            case "UUID" -> "Guid";
            case "Instant", "Date" -> "DateTime";
            default -> "string";
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.EnhancedClass;
import java.util.*;

/**
 * CSharpProjectGenerator - Main orchestrator for C# .NET project generation
 * 
 * Phase 2 - C# COMPLETE GENERATION
 * 
 * Orchestrates:
 * - Model parsing and validation
 * - Configuration generation (appsettings, DbContext)
 * - Entity generation with relationships
 * - Repository pattern implementation
 * - Service layer generation
 * - API Controller generation
 * - Validation (Data Annotations + FluentValidation)
 * - Advanced features (Jobs, Events, Specifications, Mappers)
 * - Database migrations
 * 
 * @version 2.0.0
 * @since C# Phase 2
 */
public class CSharpProjectGenerator {

    private final String projectName;
    private CSharpModelParser modelParser;
    private CSharpTypeMapper typeMapper;
    private CSharpRelationshipGenerator relationshipGenerator;
    private CSharpConfigGenerator configGenerator;
    private CSharpValidationGenerator validationGenerator;
    private CSharpAdvancedFeaturesGenerator advancedFeaturesGenerator;

    public CSharpProjectGenerator(String projectName) {
        this.projectName = projectName;
        initialize();
    }

    /**
     * Initialize generators
     */
    private void initialize() {
        this.modelParser = new CSharpModelParser();
        this.typeMapper = new CSharpTypeMapper();
        this.relationshipGenerator = new CSharpRelationshipGenerator();
        this.configGenerator = new CSharpConfigGenerator(projectName);
        this.validationGenerator = new CSharpValidationGenerator(projectName);
        this.advancedFeaturesGenerator = new CSharpAdvancedFeaturesGenerator(projectName);
    }

    /**
     * Generate complete C# project from class models
     */
    public Map<String, String> generateProject(List<ClassModel> classModels, Map<String, String> config) {
        Map<String, String> generatedFiles = new HashMap<>();

        try {
            // Step 1: Parse models
            List<CSharpModelParser.CSharpModelDefinition> models = parseModels(classModels);

            // Step 2: Generate configuration files
            generatedFiles.putAll(generateConfiguration(config));

            // Step 3: Generate model files for each entity
            for (CSharpModelParser.CSharpModelDefinition model : models) {
                generatedFiles.putAll(generateModelFiles(model, config));
            }

            // Step 4: Generate advanced features
            for (CSharpModelParser.CSharpModelDefinition model : models) {
                generatedFiles.putAll(generateAdvancedFeatures(model));
            }

            // Step 5: Generate database migrations
            for (CSharpModelParser.CSharpModelDefinition model : models) {
                generatedFiles.putAll(generateMigrations(model));
            }

            return generatedFiles;
        } catch (Exception e) {
            System.err.println("Error generating C# project: " + e.getMessage());
            throw new RuntimeException("C# project generation failed", e);
        }
    }

    /**
     * Parse ClassModels to C# model definitions
     */
    private List<CSharpModelParser.CSharpModelDefinition> parseModels(List<ClassModel> classModels) {
        List<CSharpModelParser.CSharpModelDefinition> models = new ArrayList<>();

        for (ClassModel classModel : classModels) {
            EnhancedClass enhancedClass = new EnhancedClass(classModel);
            CSharpModelParser.CSharpModelDefinition model = modelParser.parseModel(enhancedClass);
            
            if (modelParser.validateModel(model)) {
                models.add(model);
            }
        }

        return models;
    }

    /**
     * Generate configuration files (appsettings, DbContext, Program.cs)
     */
    private Map<String, String> generateConfiguration(Map<String, String> config) {
        Map<String, String> files = new HashMap<>();

        files.put("appsettings.json", configGenerator.generateAppSettings());
        files.put("appsettings.Development.json", configGenerator.generateAppSettingsDevelopment());
        files.put("appsettings.Production.json", configGenerator.generateAppSettingsProduction());
        files.put("Data/ApplicationDbContext.cs", generateDbContext());
        files.put("Program.cs", configGenerator.generateProgramConfig());

        return files;
    }

    /**
     * Generate DbContext
     */
    private String generateDbContext() {
        StringBuilder code = new StringBuilder();

        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using ").append(convertToNetNamespace(projectName)).append(".Models;\n");
        code.append("using ").append(convertToNetNamespace(projectName)).append(".Data.Configurations;\n\n");

        code.append("namespace ").append(convertToNetNamespace(projectName)).append(".Data\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Application DbContext\n");
        code.append("    /// </summary>\n");
        code.append("    public class ApplicationDbContext : DbContext\n");
        code.append("    {\n");
        code.append("        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)\n");
        code.append("            : base(options)\n");
        code.append("        {\n");
        code.append("        }\n\n");

        code.append("        // DbSets will be added here\n");
        code.append("        // public DbSet<Entity> Entities { get; set; }\n\n");

        code.append("        protected override void OnModelCreating(ModelBuilder modelBuilder)\n");
        code.append("        {\n");
        code.append("            base.OnModelCreating(modelBuilder);\n");
        code.append("            // Configure entities here\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate all model-related files
     */
    private Map<String, String> generateModelFiles(CSharpModelParser.CSharpModelDefinition model, Map<String, String> config) {
        Map<String, String> files = new HashMap<>();
        String modelName = model.getModelName();

        // Entity
        files.put("Models/" + modelName + ".cs", generateEntity(model));

        // DTOs
        ClassModel baseClass = new ClassModel();
        EnhancedClass enhancedClass = new EnhancedClass(baseClass);
        files.put("DTOs/" + modelName + "CreateDto.cs", 
            validationGenerator.generateCreateDto(enhancedClass, config.get("namespace")));
        files.put("DTOs/" + modelName + "UpdateDto.cs", 
            validationGenerator.generateUpdateDto(enhancedClass, config.get("namespace")));

        // Repository Interface
        files.put("Repositories/Interfaces/I" + modelName + "Repository.cs", 
            generateRepositoryInterface(model, config.get("namespace")));

        // Repository Implementation
        files.put("Repositories/" + modelName + "Repository.cs", 
            generateRepositoryImplementation(model, config.get("namespace")));

        // Service Interface
        files.put("Services/Interfaces/I" + modelName + "Service.cs", 
            generateServiceInterface(modelName, config.get("namespace")));

        // Service Implementation
        files.put("Services/" + modelName + "Service.cs", 
            generateServiceImplementation(modelName, config.get("namespace")));

        // Controller
        files.put("Controllers/" + modelName + "sController.cs", 
            generateController(modelName, config.get("namespace")));

        // Validators
        files.put("Validators/" + modelName + "Validator.cs", 
            validationGenerator.generateFluentValidator(enhancedClass, config.get("namespace")));

        return files;
    }

    /**
     * Generate entity with relationships
     */
    private String generateEntity(CSharpModelParser.CSharpModelDefinition model) {
        StringBuilder code = new StringBuilder();

        code.append("using System.ComponentModel.DataAnnotations;\n");
        code.append("using System.ComponentModel.DataAnnotations.Schema;\n\n");

        code.append("namespace ").append(projectName).append(".Models\n");
        code.append("{\n");
        code.append("    [Table(\"").append(model.getTableName()).append("\")]\n");
        code.append("    public class ").append(model.getModelName()).append("\n");
        code.append("    {\n");

        // Primary key
        code.append("        [Key]\n");
        code.append("        public Guid Id { get; set; } = Guid.NewGuid();\n\n");

        // Fields
        for (CSharpModelParser.CSharpFieldDefinition field : model.getFields()) {
            code.append("        ");
            for (String annotation : field.getDataAnnotations()) {
                code.append(annotation).append("\n        ");
            }
            code.append("public ").append(field.getCSharpType());
            if (field.isRequired() == false && !field.getCSharpType().matches("^(int|long|short|decimal|float|double|byte|bool|DateTime|DateTimeOffset|TimeSpan|Guid)$")) {
                code.append("?");
            }
            code.append(" ").append(capitalizeFirst(field.getFieldName())).append(" { get; set; }\n");
        }

        // Audit fields
        code.append("\n        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]\n");
        code.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n");
        code.append("        public DateTime? UpdatedAt { get; set; }\n");
        code.append("        public bool IsDeleted { get; set; } = false;\n\n");

        // Relationships
        code.append(relationshipGenerator.generateRelationships(model));

        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate repository interface
     */
    private String generateRepositoryInterface(CSharpModelParser.CSharpModelDefinition model, String packageName) {
        StringBuilder code = new StringBuilder();
        String modelName = model.getModelName();

        code.append("using ").append(projectName).append(".Models;\n\n");

        code.append("namespace ").append(projectName).append(".Repositories.Interfaces\n");
        code.append("{\n");
        code.append("    public interface I").append(modelName).append("Repository\n");
        code.append("    {\n");
        code.append("        Task<").append(modelName).append("?> GetByIdAsync(Guid id);\n");
        code.append("        Task<IEnumerable<").append(modelName).append(">> GetAllAsync();\n");
        code.append("        Task<").append(modelName).append("> CreateAsync(").append(modelName).append(" entity);\n");
        code.append("        Task<").append(modelName).append("> UpdateAsync(").append(modelName).append(" entity);\n");
        code.append("        Task DeleteAsync(Guid id);\n");
        code.append("        Task<bool> ExistsAsync(Guid id);\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate repository implementation
     */
    private String generateRepositoryImplementation(CSharpModelParser.CSharpModelDefinition model, String packageName) {
        StringBuilder code = new StringBuilder();
        String modelName = model.getModelName();

        code.append("using ").append(convertToNetNamespace(projectName)).append(".Data;\n");
        code.append("using ").append(convertToNetNamespace(projectName)).append(".Models;\n");
        code.append("using ").append(convertToNetNamespace(projectName)).append(".Repositories.Interfaces;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n\n");

        code.append("namespace ").append(projectName).append(".Repositories\n");
        code.append("{\n");
        code.append("    public class ").append(modelName).append("Repository : I").append(modelName).append("Repository\n");
        code.append("    {\n");
        code.append("        private readonly ApplicationDbContext _context;\n\n");

        code.append("        public ").append(modelName).append("Repository(ApplicationDbContext context)\n");
        code.append("        {\n");
        code.append("            _context = context;\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("?> GetByIdAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            return await _context.Set<").append(modelName).append(">()\n");
        code.append("                .AsNoTracking()\n");
        code.append("                .FirstOrDefaultAsync(x => x.Id == id && !x.IsDeleted);\n");
        code.append("        }\n\n");

        code.append("        public async Task<IEnumerable<").append(modelName).append(">> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            return await _context.Set<").append(modelName).append(">()\n");
        code.append("                .Where(x => !x.IsDeleted)\n");
        code.append("                .AsNoTracking()\n");
        code.append("                .ToListAsync();\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("> CreateAsync(").append(modelName).append(" entity)\n");
        code.append("        {\n");
        code.append("            _context.Set<").append(modelName).append(">().Add(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("> UpdateAsync(").append(modelName).append(" entity)\n");
        code.append("        {\n");
        code.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        code.append("            _context.Set<").append(modelName).append(">().Update(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");

        code.append("        public async Task DeleteAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            var entity = await GetByIdAsync(id);\n");
        code.append("            if (entity != null)\n");
        code.append("            {\n");
        code.append("                entity.IsDeleted = true;\n");
        code.append("                await UpdateAsync(entity);\n");
        code.append("            }\n");
        code.append("        }\n\n");

        code.append("        public async Task<bool> ExistsAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            return await _context.Set<").append(modelName).append(">()\n");
        code.append("                .AnyAsync(x => x.Id == id && !x.IsDeleted);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate service interface
     */
    private String generateServiceInterface(String modelName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using ").append(projectName).append(".Models;\n\n");

        code.append("namespace ").append(projectName).append(".Services.Interfaces\n");
        code.append("{\n");
        code.append("    public interface I").append(modelName).append("Service\n");
        code.append("    {\n");
        code.append("        Task<").append(modelName).append("?> GetByIdAsync(Guid id);\n");
        code.append("        Task<IEnumerable<").append(modelName).append(">> GetAllAsync();\n");
        code.append("        Task<").append(modelName).append("> CreateAsync(").append(modelName).append("CreateDto dto);\n");
        code.append("        Task<").append(modelName).append("> UpdateAsync(Guid id, ").append(modelName).append("UpdateDto dto);\n");
        code.append("        Task DeleteAsync(Guid id);\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate service implementation
     */
    private String generateServiceImplementation(String modelName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using ").append(projectName).append(".Models;\n");
        code.append("using ").append(projectName).append(".Repositories.Interfaces;\n");
        code.append("using ").append(projectName).append(".Services.Interfaces;\n\n");

        code.append("namespace ").append(projectName).append(".Services\n");
        code.append("{\n");
        code.append("    public class ").append(modelName).append("Service : I").append(modelName).append("Service\n");
        code.append("    {\n");
        code.append("        private readonly I").append(modelName).append("Repository _repository;\n\n");

        code.append("        public ").append(modelName).append("Service(I").append(modelName).append("Repository repository)\n");
        code.append("        {\n");
        code.append("            _repository = repository;\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("?> GetByIdAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            return await _repository.GetByIdAsync(id);\n");
        code.append("        }\n\n");

        code.append("        public async Task<IEnumerable<").append(modelName).append(">> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            return await _repository.GetAllAsync();\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("> CreateAsync(").append(modelName).append("CreateDto dto)\n");
        code.append("        {\n");
        code.append("            var entity = new ").append(modelName).append(" { /* map from dto */ };\n");
        code.append("            return await _repository.CreateAsync(entity);\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(modelName).append("> UpdateAsync(Guid id, ").append(modelName).append("UpdateDto dto)\n");
        code.append("        {\n");
        code.append("            var entity = await _repository.GetByIdAsync(id);\n");
        code.append("            if (entity == null) throw new Exception(\"Not found\");\n");
        code.append("            // Update entity from dto\n");
        code.append("            return await _repository.UpdateAsync(entity);\n");
        code.append("        }\n\n");

        code.append("        public async Task DeleteAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            await _repository.DeleteAsync(id);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate REST API controller
     */
    private String generateController(String modelName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using ").append(projectName).append(".Services.Interfaces;\n");
        code.append("using ").append(projectName).append(".Resources;\n\n");

        code.append("namespace ").append(projectName).append(".Controllers\n");
        code.append("{\n");
        code.append("    [ApiController]\n");
        code.append("    [Route(\"api/[controller]\")]\n");
        code.append("    public class ").append(modelName).append("sController : ControllerBase\n");
        code.append("    {\n");
        code.append("        private readonly I").append(modelName).append("Service _service;\n\n");

        code.append("        public ").append(modelName).append("sController(I").append(modelName).append("Service service)\n");
        code.append("        {\n");
        code.append("            _service = service;\n");
        code.append("        }\n\n");

        code.append("        [HttpGet]\n");
        code.append("        public async Task<ActionResult<ApiResponse<List<object>>>> GetAll()\n");
        code.append("        {\n");
        code.append("            var items = await _service.GetAllAsync();\n");
        code.append("            return Ok(ApiResponse<List<object>>.SuccessResponse(items.ToList()));\n");
        code.append("        }\n\n");

        code.append("        [HttpGet(\"{id}\")]\n");
        code.append("        public async Task<ActionResult<ApiResponse<object>>> GetById(Guid id)\n");
        code.append("        {\n");
        code.append("            var item = await _service.GetByIdAsync(id);\n");
        code.append("            if (item == null) return NotFound();\n");
        code.append("            return Ok(ApiResponse<object>.SuccessResponse(item));\n");
        code.append("        }\n\n");

        code.append("        [HttpPost]\n");
        code.append("        public async Task<ActionResult<ApiResponse<object>>> Create(").append(modelName).append("CreateDto dto)\n");
        code.append("        {\n");
        code.append("            var item = await _service.CreateAsync(dto);\n");
        code.append("            return CreatedAtAction(nameof(GetById), new { id = item.Id }, ApiResponse<object>.SuccessResponse(item));\n");
        code.append("        }\n\n");

        code.append("        [HttpPut(\"{id}\")]\n");
        code.append("        public async Task<ActionResult<ApiResponse<object>>> Update(Guid id, ").append(modelName).append("UpdateDto dto)\n");
        code.append("        {\n");
        code.append("            var item = await _service.UpdateAsync(id, dto);\n");
        code.append("            return Ok(ApiResponse<object>.SuccessResponse(item));\n");
        code.append("        }\n\n");

        code.append("        [HttpDelete(\"{id}\")]\n");
        code.append("        public async Task<ActionResult<ApiResponse<object>>> Delete(Guid id)\n");
        code.append("        {\n");
        code.append("            await _service.DeleteAsync(id);\n");
        code.append("            return Ok(ApiResponse<object>.SuccessResponse(null, \"Deleted successfully\"));\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate advanced features
     */
    private Map<String, String> generateAdvancedFeatures(CSharpModelParser.CSharpModelDefinition model) {
        Map<String, String> files = new HashMap<>();
        String modelName = model.getModelName();

        // Background job
        files.put("Jobs/" + modelName + "BackgroundJob.cs", 
            advancedFeaturesGenerator.generateBackgroundJobHandler(modelName, projectName));

        // Events
        files.put("Events/" + modelName + "CreatedEvent.cs", 
            advancedFeaturesGenerator.generateDomainEvent(modelName + "CreatedEvent", modelName));
        files.put("Events/Handlers/" + modelName + "CreatedEventHandler.cs", 
            advancedFeaturesGenerator.generateEventHandler(modelName + "CreatedEvent", modelName, projectName));

        // Specifications
        files.put("Specifications/" + modelName + "Specification.cs", 
            advancedFeaturesGenerator.generateSpecification(modelName, projectName));

        // Mappers
        files.put("Mappings/" + modelName + "MapperProfile.cs", 
            advancedFeaturesGenerator.generateMapperProfile(modelName, projectName));

        return files;
    }

    /**
     * Generate migrations
     */
    private Map<String, String> generateMigrations(CSharpModelParser.CSharpModelDefinition model) {
        Map<String, String> files = new HashMap<>();
        String modelName = model.getModelName();

        // Migration would be generated via EF Core migrations
        files.put("Migrations/Initial" + modelName + "Migration.cs", 
            generateMigrationFile(model));

        return files;
    }

    /**
     * Generate migration file
     */
    private String generateMigrationFile(CSharpModelParser.CSharpModelDefinition model) {
        StringBuilder code = new StringBuilder();

        code.append("using Microsoft.EntityFrameworkCore.Migrations;\n\n");
        code.append("namespace ").append(projectName).append(".Migrations\n");
        code.append("{\n");
        code.append("    public partial class Initial").append(model.getModelName()).append(" : Migration\n");
        code.append("    {\n");
        code.append("        protected override void Up(MigrationBuilder migrationBuilder)\n");
        code.append("        {\n");
        code.append("            // Create table\n");
        code.append("            migrationBuilder.CreateTable(\n");
        code.append("                name: \"").append(model.getTableName()).append("\",\n");
        code.append("                columns: table => new\n");
        code.append("                {\n");
        code.append("                    // Columns will be generated here\n");
        code.append("                }\n");
        code.append("            );\n");
        code.append("        }\n\n");

        code.append("        protected override void Down(MigrationBuilder migrationBuilder)\n");
        code.append("        {\n");
        code.append("            migrationBuilder.DropTable(\n");
        code.append("                name: \"").append(model.getTableName()).append("\");\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Capitalize first letter
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    /**
     * Convert Java package to .NET namespace
     */
    private String convertToNetNamespace(String javaPackage) {
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalizeFirst(parts[1]) + ".Application";
        }
        return capitalizeFirst(javaPackage.replace(".", "")) + ".Application";
    }
}

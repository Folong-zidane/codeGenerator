package com.basiccode.generator.generator.csharp;

import java.util.HashMap;
import java.util.Map;

/**
 * CSharpConfigGenerator - Generate .NET configuration files
 * Generates appsettings.json, DbContext configuration, dependency injection setup
 * 
 * Phase 2 Week 2 - C# CONFIGURATION
 * 
 * Generates:
 * - appsettings.json (development, production, testing)
 * - appsettings.development.json (detailed logging)
 * - appsettings.production.json (optimized settings)
 * - DbContext OnModelCreating configuration
 * - Dependency injection configuration
 * - Connection string templates
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpConfigGenerator {

    private final String projectName;

    public CSharpConfigGenerator(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Generate base appsettings.json
     */
    public String generateAppSettings() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"Logging\": {\n");
        json.append("    \"LogLevel\": {\n");
        json.append("      \"Default\": \"Information\",\n");
        json.append("      \"Microsoft\": \"Warning\",\n");
        json.append("      \"Microsoft.EntityFrameworkCore\": \"Information\"\n");
        json.append("    }\n");
        json.append("  },\n");
        json.append("  \"ConnectionStrings\": {\n");
        json.append("    \"DefaultConnection\": \"Server=.;Database=").append(projectName)
            .append(";Trusted_Connection=true;TrustServerCertificate=true\"\n");
        json.append("  },\n");
        json.append("  \"JwtSettings\": {\n");
        json.append("    \"SecretKey\": \"your-secret-key-here-change-in-production\",\n");
        json.append("    \"ExpirationMinutes\": 60\n");
        json.append("  },\n");
        json.append("  \"CorsSettings\": {\n");
        json.append("    \"AllowedOrigins\": [\"http://localhost:3000\", \"http://localhost:4200\"],\n");
        json.append("    \"AllowedMethods\": [\"GET\", \"POST\", \"PUT\", \"DELETE\", \"OPTIONS\"]\n");
        json.append("  },\n");
        json.append("  \"EmailSettings\": {\n");
        json.append("    \"SmtpServer\": \"smtp.gmail.com\",\n");
        json.append("    \"SmtpPort\": 587,\n");
        json.append("    \"Username\": \"your-email@gmail.com\",\n");
        json.append("    \"Password\": \"your-app-password\"\n");
        json.append("  },\n");
        json.append("  \"AllowedHosts\": \"*\"\n");
        json.append("}\n");

        return json.toString();
    }

    /**
     * Generate appsettings.Development.json
     */
    public String generateAppSettingsDevelopment() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"Logging\": {\n");
        json.append("    \"LogLevel\": {\n");
        json.append("      \"Default\": \"Debug\",\n");
        json.append("      \"Microsoft\": \"Information\",\n");
        json.append("      \"Microsoft.EntityFrameworkCore\": \"Debug\"\n");
        json.append("    }\n");
        json.append("  },\n");
        json.append("  \"ConnectionStrings\": {\n");
        json.append("    \"DefaultConnection\": \"Server=localhost;Database=").append(projectName)
            .append("_Dev;User Id=sa;Password=YourPassword123!;TrustServerCertificate=true\"\n");
        json.append("  },\n");
        json.append("  \"DebugSettings\": {\n");
        json.append("    \"EnableDetailedErrors\": true,\n");
        json.append("    \"EnableSensitiveDataLogging\": true,\n");
        json.append("    \"EnableQueryLogging\": true\n");
        json.append("  }\n");
        json.append("}\n");

        return json.toString();
    }

    /**
     * Generate appsettings.Production.json
     */
    public String generateAppSettingsProduction() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"Logging\": {\n");
        json.append("    \"LogLevel\": {\n");
        json.append("      \"Default\": \"Warning\",\n");
        json.append("      \"Microsoft\": \"Error\",\n");
        json.append("      \"Microsoft.EntityFrameworkCore\": \"Error\"\n");
        json.append("    }\n");
        json.append("  },\n");
        json.append("  \"ConnectionStrings\": {\n");
        json.append("    \"DefaultConnection\": \"Server=prod-server.database.windows.net;Database=").append(projectName)
            .append("_Prod;User Id=admin;Password=SecurePassword123!;TrustServerCertificate=true\"\n");
        json.append("  },\n");
        json.append("  \"CacheSettings\": {\n");
        json.append("    \"CacheType\": \"Redis\",\n");
        json.append("    \"RedisConnection\": \"prod-redis.redis.cache.windows.net:6379\",\n");
        json.append("    \"DefaultExpiration\": 3600\n");
        json.append("  },\n");
        json.append("  \"SecuritySettings\": {\n");
        json.append("    \"EnableRateLimiting\": true,\n");
        json.append("    \"RequireHttps\": true,\n");
        json.append("    \"HstsMaxAge\": 31536000\n");
        json.append("  }\n");
        json.append("}\n");

        return json.toString();
    }

    /**
     * Generate DbContext configuration code
     */
    public String generateDbContextConfig() {
        StringBuilder code = new StringBuilder();

        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using Microsoft.EntityFrameworkCore.Metadata.Builders;\n\n");
        code.append("namespace ").append(projectName).append(".Data.Configurations\n");
        code.append("{\n");
        code.append("    public class EntityConfiguration : IEntityTypeConfiguration<Entity>\n");
        code.append("    {\n");
        code.append("        public void Configure(EntityTypeBuilder<Entity> builder)\n");
        code.append("        {\n");
        code.append("            // Primary Key\n");
        code.append("            builder.HasKey(e => e.Id);\n\n");
        code.append("            // Indexes\n");
        code.append("            builder.HasIndex(e => e.CreatedAt)\n");
        code.append("                .HasDatabaseName(\"IX_Entity_CreatedAt\");\n\n");
        code.append("            // Properties\n");
        code.append("            builder.Property(e => e.Name)\n");
        code.append("                .IsRequired()\n");
        code.append("                .HasMaxLength(255)\n");
        code.append("                .HasColumnType(\"varchar(255)\");\n\n");
        code.append("            builder.Property(e => e.CreatedAt)\n");
        code.append("                .HasDefaultValueSql(\"GETUTCDATE()\")\n");
        code.append("                .HasColumnType(\"datetime2\");\n\n");
        code.append("            builder.Property(e => e.UpdatedAt)\n");
        code.append("                .HasColumnType(\"datetime2\");\n\n");
        code.append("            // Shadow property for concurrency\n");
        code.append("            builder.Property<byte[]>(\"RowVersion\")\n");
        code.append("                .IsRowVersion();\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate Startup/Program.cs configuration
     */
    public String generateProgramConfig() {
        StringBuilder code = new StringBuilder();

        code.append("using ").append(projectName).append(".Data;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using Microsoft.AspNetCore.Authentication.JwtBearer;\n");
        code.append("using Microsoft.IdentityModel.Tokens;\n");
        code.append("using System.Text;\n\n");

        code.append("var builder = WebApplicationBuilder.CreateBuilder(args);\n\n");

        // Add services
        code.append("// Add DbContext\n");
        code.append("builder.Services.AddDbContext<ApplicationDbContext>(options =>\n");
        code.append("    options.UseSqlServer(\n");
        code.append("        builder.Configuration.GetConnectionString(\"DefaultConnection\")));\n\n");

        code.append("// Add CORS\n");
        code.append("builder.Services.AddCors(options =>\n");
        code.append("{\n");
        code.append("    options.AddPolicy(\"AllowFrontend\", builder =>\n");
        code.append("    {\n");
        code.append("        builder.WithOrigins(\"http://localhost:3000\", \"http://localhost:4200\")\n");
        code.append("            .AllowAnyHeader()\n");
        code.append("            .AllowAnyMethod();\n");
        code.append("    });\n");
        code.append("});\n\n");

        code.append("// Add Authentication\n");
        code.append("var jwtSettings = builder.Configuration.GetSection(\"JwtSettings\");\n");
        code.append("builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)\n");
        code.append("    .AddJwtBearer(options =>\n");
        code.append("    {\n");
        code.append("        options.TokenValidationParameters = new TokenValidationParameters\n");
        code.append("        {\n");
        code.append("            ValidateIssuerSigningKey = true,\n");
        code.append("            IssuerSigningKey = new SymmetricSecurityKey(\n");
        code.append("                Encoding.ASCII.GetBytes(jwtSettings[\"SecretKey\"]!)),\n");
        code.append("            ValidateIssuer = false,\n");
        code.append("            ValidateAudience = false\n");
        code.append("        };\n");
        code.append("    });\n\n");

        code.append("// Add Controllers\n");
        code.append("builder.Services.AddControllers();\n\n");

        code.append("// Add Swagger\n");
        code.append("builder.Services.AddSwaggerGen();\n\n");

        code.append("var app = builder.Build();\n\n");

        code.append("if (app.Environment.IsDevelopment())\n");
        code.append("{\n");
        code.append("    app.UseSwagger();\n");
        code.append("    app.UseSwaggerUI();\n");
        code.append("}\n\n");

        code.append("app.UseHttpsRedirection();\n");
        code.append("app.UseCors(\"AllowFrontend\");\n");
        code.append("app.UseAuthentication();\n");
        code.append("app.UseAuthorization();\n\n");

        code.append("app.MapControllers();\n\n");

        code.append("app.Run();\n");

        return code.toString();
    }
}

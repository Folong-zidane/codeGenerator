package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

/**
 * C# Migration generator for .NET Core
 * Generates Entity Framework Core DbContext and configuration
 */
public class CSharpMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        // Generate DbContext
        code.append(generateDbContext(enhancedClasses, packageName));
        code.append("\n\n");
        
        // Generate Startup configuration
        code.append(generateStartupConfiguration(packageName));
        code.append("\n\n");
        
        // Generate Program.cs configuration
        code.append(generateProgramConfiguration(packageName));
        
        return code.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "Data";
    }
    
    private String generateDbContext(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using System;\n\n");
        
        code.append("namespace ").append(packageName).append(".Data\n");
        code.append("{\n");
        code.append("    public class ApplicationDbContext : DbContext\n");
        code.append("    {\n");
        code.append("        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        // Generate DbSet properties for each entity
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("        public DbSet<").append(className).append("> ").append(className).append("s { get; set; }\n");
        }
        code.append("\n");
        
        // OnModelCreating method
        code.append("        protected override void OnModelCreating(ModelBuilder modelBuilder)\n");
        code.append("        {\n");
        code.append("            base.OnModelCreating(modelBuilder);\n\n");
        
        // Configure each entity
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("            // Configure ").append(className).append(" entity\n");
            code.append("            modelBuilder.Entity<").append(className).append(">()\n");
            code.append("                .ToTable(\"").append(className.toLowerCase()).append("s\")\n");
            code.append("                .HasKey(e => e.Id);\n\n");
            
            code.append("            modelBuilder.Entity<").append(className).append(">()\n");
            code.append("                .Property(e => e.Id)\n");
            code.append("                .ValueGeneratedOnAdd();\n\n");
            
            code.append("            modelBuilder.Entity<").append(className).append(">()\n");
            code.append("                .Property(e => e.CreatedAt)\n");
            code.append("                .HasDefaultValueSql(\"GETUTCDATE()\");\n\n");
            
            code.append("            modelBuilder.Entity<").append(className).append(">()\n");
            code.append("                .Property(e => e.UpdatedAt)\n");
            code.append("                .HasDefaultValueSql(\"GETUTCDATE()\");\n\n");
            
            // Configure state enum if stateful
            if (enhancedClass.isStateful()) {
                code.append("            modelBuilder.Entity<").append(className).append(">()\n");
                code.append("                .Property(e => e.Status)\n");
                code.append("                .HasConversion<string>();\n\n");
            }
        }
        
        code.append("        }\n\n");
        
        // Override SaveChanges to handle audit fields
        code.append("        public override int SaveChanges()\n");
        code.append("        {\n");
        code.append("            UpdateAuditFields();\n");
        code.append("            return base.SaveChanges();\n");
        code.append("        }\n\n");
        
        code.append("        public override async Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)\n");
        code.append("        {\n");
        code.append("            UpdateAuditFields();\n");
        code.append("            return await base.SaveChangesAsync(cancellationToken);\n");
        code.append("        }\n\n");
        
        code.append("        private void UpdateAuditFields()\n");
        code.append("        {\n");
        code.append("            var entries = ChangeTracker.Entries()\n");
        code.append("                .Where(e => e.State == EntityState.Added || e.State == EntityState.Modified);\n\n");
        
        code.append("            foreach (var entry in entries)\n");
        code.append("            {\n");
        code.append("                if (entry.Entity.GetType().GetProperty(\"UpdatedAt\") != null)\n");
        code.append("                {\n");
        code.append("                    entry.Property(\"UpdatedAt\").CurrentValue = DateTime.UtcNow;\n");
        code.append("                }\n\n");
        
        code.append("                if (entry.State == EntityState.Added && entry.Entity.GetType().GetProperty(\"CreatedAt\") != null)\n");
        code.append("                {\n");
        code.append("                    entry.Property(\"CreatedAt\").CurrentValue = DateTime.UtcNow;\n");
        code.append("                }\n");
        code.append("            }\n");
        code.append("        }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateStartupConfiguration(String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("// Startup.cs configuration for ").append(packageName).append("\n");
        code.append("using ").append(packageName).append(".Data;\n");
        code.append("using ").append(packageName).append(".Services;\n");
        code.append("using ").append(packageName).append(".Repositories;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using Microsoft.Extensions.DependencyInjection;\n");
        code.append("using Microsoft.Extensions.Configuration;\n\n");
        
        code.append("namespace ").append(packageName).append("\n");
        code.append("{\n");
        code.append("    public class Startup\n");
        code.append("    {\n");
        code.append("        public IConfiguration Configuration { get; }\n\n");
        
        code.append("        public Startup(IConfiguration configuration)\n");
        code.append("        {\n");
        code.append("            Configuration = configuration;\n");
        code.append("        }\n\n");
        
        code.append("        public void ConfigureServices(IServiceCollection services)\n");
        code.append("        {\n");
        code.append("            // Add Entity Framework\n");
        code.append("            services.AddDbContext<ApplicationDbContext>(options =>\n");
        code.append("                options.UseSqlServer(Configuration.GetConnectionString(\"DefaultConnection\")));\n\n");
        
        code.append("            // Add repositories\n");
        code.append("            // services.AddScoped<IUserRepository, UserRepository>();\n");
        code.append("            // Add more repositories here\n\n");
        
        code.append("            // Add services\n");
        code.append("            // services.AddScoped<IUserService, UserService>();\n");
        code.append("            // Add more services here\n\n");
        
        code.append("            // Add controllers\n");
        code.append("            services.AddControllers();\n\n");
        
        code.append("            // Add Swagger\n");
        code.append("            services.AddEndpointsApiExplorer();\n");
        code.append("            services.AddSwaggerGen();\n");
        code.append("        }\n\n");
        
        code.append("        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)\n");
        code.append("        {\n");
        code.append("            if (env.IsDevelopment())\n");
        code.append("            {\n");
        code.append("                app.UseSwagger();\n");
        code.append("                app.UseSwaggerUI();\n");
        code.append("            }\n\n");
        
        code.append("            app.UseHttpsRedirection();\n");
        code.append("            app.UseRouting();\n");
        code.append("            app.UseAuthorization();\n");
        code.append("            app.UseEndpoints(endpoints => { endpoints.MapControllers(); });\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateProgramConfiguration(String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("// Program.cs for ").append(packageName).append("\n");
        code.append("using ").append(packageName).append(".Data;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n\n");
        
        code.append("namespace ").append(packageName).append("\n");
        code.append("{\n");
        code.append("    public class Program\n");
        code.append("    {\n");
        code.append("        public static void Main(string[] args)\n");
        code.append("        {\n");
        code.append("            var builder = WebApplication.CreateBuilder(args);\n\n");
        
        code.append("            // Add services to the container\n");
        code.append("            builder.Services.AddDbContext<ApplicationDbContext>(options =>\n");
        code.append("                options.UseSqlServer(builder.Configuration.GetConnectionString(\"DefaultConnection\")));\n\n");
        
        code.append("            builder.Services.AddControllers();\n");
        code.append("            builder.Services.AddEndpointsApiExplorer();\n");
        code.append("            builder.Services.AddSwaggerGen();\n\n");
        
        code.append("            var app = builder.Build();\n\n");
        
        code.append("            // Configure the HTTP request pipeline\n");
        code.append("            if (app.Environment.IsDevelopment())\n");
        code.append("            {\n");
        code.append("                app.UseSwagger();\n");
        code.append("                app.UseSwaggerUI();\n");
        code.append("            }\n\n");
        
        code.append("            app.UseHttpsRedirection();\n");
        code.append("            app.UseAuthorization();\n");
        code.append("            app.MapControllers();\n\n");
        
        code.append("            // Ensure database is created\n");
        code.append("            using (var scope = app.Services.CreateScope())\n");
        code.append("            {\n");
        code.append("                var context = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();\n");
        code.append("                context.Database.EnsureCreated();\n");
        code.append("            }\n\n");
        
        code.append("            app.Run();\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
}
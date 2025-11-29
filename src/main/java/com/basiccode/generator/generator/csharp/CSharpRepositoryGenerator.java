package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * C# Repository generator for .NET Core
 * Generates repository interfaces and implementations using Entity Framework Core
 */
public class CSharpRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Generate Repository Interface
        code.append(generateRepositoryInterface(className, packageName));
        code.append("\n\n");
        
        // Generate Repository Implementation
        code.append(generateRepositoryImplementation(className, packageName));
        
        return code.toString();
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "Repositories";
    }
    
    private String generateRepositoryInterface(String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n\n");
        
        code.append("namespace ").append(packageName).append(".Repositories\n");
        code.append("{\n");
        code.append("    public interface I").append(className).append("Repository\n");
        code.append("    {\n");
        code.append("        Task<IEnumerable<").append(className).append(">> GetAllAsync();\n");
        code.append("        Task<").append(className).append("?> GetByIdAsync(int id);\n");
        code.append("        Task<").append(className).append("> CreateAsync(").append(className).append(" entity);\n");
        code.append("        Task<").append(className).append("> UpdateAsync(").append(className).append(" entity);\n");
        code.append("        Task DeleteAsync(int id);\n");
        code.append("        Task<bool> ExistsAsync(int id);\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateRepositoryImplementation(String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using ").append(packageName).append(".Data;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n\n");
        
        code.append("namespace ").append(packageName).append(".Repositories\n");
        code.append("{\n");
        code.append("    public class ").append(className).append("Repository : I").append(className).append("Repository\n");
        code.append("    {\n");
        code.append("        private readonly ApplicationDbContext _context;\n\n");
        
        // Constructor
        code.append("        public ").append(className).append("Repository(ApplicationDbContext context)\n");
        code.append("        {\n");
        code.append("            _context = context;\n");
        code.append("        }\n\n");
        
        // GetAllAsync
        code.append("        public async Task<IEnumerable<").append(className).append(">> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            return await _context.").append(className).append("s.ToListAsync();\n");
        code.append("        }\n\n");
        
        // GetByIdAsync
        code.append("        public async Task<").append(className).append("?> GetByIdAsync(int id)\n");
        code.append("        {\n");
        code.append("            return await _context.").append(className).append("s.FindAsync(id);\n");
        code.append("        }\n\n");
        
        // CreateAsync
        code.append("        public async Task<").append(className).append("> CreateAsync(").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            entity.CreatedAt = DateTime.UtcNow;\n");
        code.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        code.append("            \n");
        code.append("            _context.").append(className).append("s.Add(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");
        
        // UpdateAsync
        code.append("        public async Task<").append(className).append("> UpdateAsync(").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        code.append("            \n");
        code.append("            _context.Entry(entity).State = EntityState.Modified;\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");
        
        // DeleteAsync
        code.append("        public async Task DeleteAsync(int id)\n");
        code.append("        {\n");
        code.append("            var entity = await GetByIdAsync(id);\n");
        code.append("            if (entity != null)\n");
        code.append("            {\n");
        code.append("                _context.").append(className).append("s.Remove(entity);\n");
        code.append("                await _context.SaveChangesAsync();\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // ExistsAsync
        code.append("        public async Task<bool> ExistsAsync(int id)\n");
        code.append("        {\n");
        code.append("            return await _context.").append(className).append("s.AnyAsync(e => e.Id == id);\n");
        code.append("        }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
}
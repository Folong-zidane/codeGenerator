package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLAttribute;
import java.util.Map;

/**
 * Générateur de repositories C# avec Entity Framework
 */
public class CSharpRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String getRepositoryDirectory() {
        return "Repositories";
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        return generateRepository(enhancedClass, packageName, null);
    }
    
    public String generateRepository(EnhancedClass enhancedClass, String packageName, Map<String, String> metadata) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder repository = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String repositoryName = className + "Repository";
        String interfaceName = "I" + repositoryName;
        
        // Generate interface first
        repository.append(generateRepositoryInterface(enhancedClass, packageName));
        repository.append("\n\n");
        
        // Using statements
        repository.append("using System;\n");
        repository.append("using System.Collections.Generic;\n");
        repository.append("using System.Linq;\n");
        repository.append("using System.Threading.Tasks;\n");
        repository.append("using Microsoft.EntityFrameworkCore;\n");
        repository.append("using ").append(packageName).append(".Entities;\n");
        repository.append("using ").append(packageName).append(".Data;\n\n");
        
        // Namespace
        repository.append("namespace ").append(packageName).append(".Repositories\n{\n");
        
        // Repository class
        repository.append("    public class ").append(repositoryName).append(" : ").append(interfaceName).append("\n    {\n");
        
        // Fields
        repository.append("        private readonly ApplicationDbContext _context;\n");
        repository.append("        private readonly DbSet<").append(className).append("> _dbSet;\n\n");
        
        // Constructor
        repository.append("        public ").append(repositoryName).append("(ApplicationDbContext context)\n");
        repository.append("        {\n");
        repository.append("            _context = context ?? throw new ArgumentNullException(nameof(context));\n");
        repository.append("            _dbSet = _context.Set<").append(className).append(">();\n");
        repository.append("        }\n\n");
        
        // CRUD methods
        repository.append(generateCrudMethods(className));
        
        // Custom query methods
        repository.append(generateCustomQueryMethods(enhancedClass, className));
        
        repository.append("    }\n");
        repository.append("}\n");
        
        return repository.toString();
    }
    
    private String generateRepositoryInterface(EnhancedClass enhancedClass, String packageName) {
        StringBuilder interfaceCode = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String interfaceName = "I" + className + "Repository";
        
        interfaceCode.append("using System;\n");
        interfaceCode.append("using System.Collections.Generic;\n");
        interfaceCode.append("using System.Threading.Tasks;\n");
        interfaceCode.append("using ").append(packageName).append(".Entities;\n\n");
        
        interfaceCode.append("namespace ").append(packageName).append(".Repositories\n{\n");
        interfaceCode.append("    public interface ").append(interfaceName).append("\n    {\n");
        
        // CRUD interface methods
        interfaceCode.append("        Task<").append(className).append("> GetByIdAsync(long id);\n");
        interfaceCode.append("        Task<IEnumerable<").append(className).append(">> GetAllAsync();\n");
        interfaceCode.append("        Task<").append(className).append("> CreateAsync(").append(className).append(" entity);\n");
        interfaceCode.append("        Task<").append(className).append("> UpdateAsync(").append(className).append(" entity);\n");
        interfaceCode.append("        Task<bool> DeleteAsync(long id);\n");
        interfaceCode.append("        Task<bool> ExistsAsync(long id);\n");
        
        // Custom query method signatures
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                if (!"id".equalsIgnoreCase(attr.getName())) {
                    String attrName = toPascalCase(attr.getName());
                    String csharpType = mapToCSharpType(attr.getType());
                    interfaceCode.append("        Task<IEnumerable<").append(className).append(">> FindBy").append(attrName).append("Async(").append(csharpType).append(" ").append(attr.getName()).append(");\n");
                }
            }
        }
        
        interfaceCode.append("    }\n");
        interfaceCode.append("}\n");
        
        return interfaceCode.toString();
    }
    
    private String generateCrudMethods(String className) {
        StringBuilder methods = new StringBuilder();
        
        // GetByIdAsync
        methods.append("        public async Task<").append(className).append("> GetByIdAsync(long id)\n");
        methods.append("        {\n");
        methods.append("            return await _dbSet.FindAsync(id);\n");
        methods.append("        }\n\n");
        
        // GetAllAsync
        methods.append("        public async Task<IEnumerable<").append(className).append(">> GetAllAsync()\n");
        methods.append("        {\n");
        methods.append("            return await _dbSet.ToListAsync();\n");
        methods.append("        }\n\n");
        
        // CreateAsync
        methods.append("        public async Task<").append(className).append("> CreateAsync(").append(className).append(" entity)\n");
        methods.append("        {\n");
        methods.append("            if (entity == null)\n");
        methods.append("                throw new ArgumentNullException(nameof(entity));\n");
        methods.append("            \n");
        methods.append("            _dbSet.Add(entity);\n");
        methods.append("            await _context.SaveChangesAsync();\n");
        methods.append("            return entity;\n");
        methods.append("        }\n\n");
        
        // UpdateAsync
        methods.append("        public async Task<").append(className).append("> UpdateAsync(").append(className).append(" entity)\n");
        methods.append("        {\n");
        methods.append("            if (entity == null)\n");
        methods.append("                throw new ArgumentNullException(nameof(entity));\n");
        methods.append("            \n");
        methods.append("            _context.Entry(entity).State = EntityState.Modified;\n");
        methods.append("            await _context.SaveChangesAsync();\n");
        methods.append("            return entity;\n");
        methods.append("        }\n\n");
        
        // DeleteAsync
        methods.append("        public async Task<bool> DeleteAsync(long id)\n");
        methods.append("        {\n");
        methods.append("            var entity = await _dbSet.FindAsync(id);\n");
        methods.append("            if (entity == null)\n");
        methods.append("                return false;\n");
        methods.append("            \n");
        methods.append("            _dbSet.Remove(entity);\n");
        methods.append("            await _context.SaveChangesAsync();\n");
        methods.append("            return true;\n");
        methods.append("        }\n\n");
        
        // ExistsAsync
        methods.append("        public async Task<bool> ExistsAsync(long id)\n");
        methods.append("        {\n");
        methods.append("            return await _dbSet.AnyAsync(e => e.Id == id);\n");
        methods.append("        }\n\n");
        
        return methods.toString();
    }
    
    private String generateCustomQueryMethods(EnhancedClass enhancedClass, String className) {
        StringBuilder methods = new StringBuilder();
        
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                if (!"id".equalsIgnoreCase(attr.getName())) {
                    String attrName = toPascalCase(attr.getName());
                    String csharpType = mapToCSharpType(attr.getType());
                    
                    methods.append("        public async Task<IEnumerable<").append(className).append(">> FindBy").append(attrName).append("Async(").append(csharpType).append(" ").append(attr.getName()).append(")\n");
                    methods.append("        {\n");
                    methods.append("            return await _dbSet.Where(e => e.").append(attrName).append(" == ").append(attr.getName()).append(").ToListAsync();\n");
                    methods.append("        }\n\n");
                }
            }
        }
        
        return methods.toString();
    }
    
    private String mapToCSharpType(String umlType) {
        if (umlType == null) return "string";
        
        switch (umlType.toLowerCase()) {
            case "string": case "str": return "string";
            case "integer": case "int": return "int";
            case "long": return "long";
            case "float": return "float";
            case "double": return "double";
            case "boolean": case "bool": return "bool";
            case "bigdecimal": case "decimal": return "decimal";
            case "localdatetime": case "datetime": return "DateTime";
            case "localdate": case "date": return "DateTime";
            case "uuid": return "Guid";
            default: return "string";
        }
    }
    
    private String toPascalCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
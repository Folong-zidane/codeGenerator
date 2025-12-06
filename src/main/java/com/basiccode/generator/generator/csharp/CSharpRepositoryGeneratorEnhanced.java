package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.EnhancedClass;
import java.util.ArrayList;
import java.util.List;

/**
 * CSharpRepositoryGeneratorEnhanced - Enhanced Repository generation
 * Generates generic repository interface and implementation with CRUD and advanced operations
 * 
 * Phase 2 Week 2 - C# REPOSITORY GENERATION
 * 
 * Generates:
 * - Generic repository interface (IRepository<T>)
 * - Repository implementation with EF Core integration
 * - CRUD operations (Create, Read, Update, Delete)
 * - Query operations (Find, GetAll, GetPaginated)
 * - Advanced operations (GetBy*, Search, Count, Exists)
 * - Relationship loading (Include)
 * - Unit of Work integration
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpRepositoryGeneratorEnhanced {

    /**
     * Generate repository interface
     */
    public String generateRepositoryInterface(String entityName) {
        StringBuilder code = new StringBuilder();

        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Linq.Expressions;\n");
        code.append("using System.Threading.Tasks;\n\n");

        code.append("namespace YourApp.Infrastructure.Repositories\n");
        code.append("{\n");

        code.append("    /// <summary>\n");
        code.append("    /// Generic repository interface for ").append(entityName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public interface IRepository<T> where T : class\n");
        code.append("    {\n");

        // Query operations
        code.append("        // ==================== QUERY OPERATIONS ====================\n\n");

        code.append("        /// <summary>Get all entities</summary>\n");
        code.append("        Task<IEnumerable<T>> GetAllAsync();\n\n");

        code.append("        /// <summary>Get paginated entities</summary>\n");
        code.append("        Task<(IEnumerable<T> items, int total)> GetPaginatedAsync(int page, int pageSize);\n\n");

        code.append("        /// <summary>Get entity by id</summary>\n");
        code.append("        Task<T> GetByIdAsync(Guid id);\n\n");

        code.append("        /// <summary>Find entities with predicate</summary>\n");
        code.append("        Task<IEnumerable<T>> FindAsync(Expression<Func<T, bool>> predicate);\n\n");

        code.append("        /// <summary>Get first match or null</summary>\n");
        code.append("        Task<T> FirstOrDefaultAsync(Expression<Func<T, bool>> predicate);\n\n");

        // Count operations
        code.append("        // ==================== COUNT OPERATIONS ====================\n\n");

        code.append("        /// <summary>Count total entities</summary>\n");
        code.append("        Task<int> CountAsync();\n\n");

        code.append("        /// <summary>Count with predicate</summary>\n");
        code.append("        Task<int> CountAsync(Expression<Func<T, bool>> predicate);\n\n");

        // Check operations
        code.append("        // ==================== CHECK OPERATIONS ====================\n\n");

        code.append("        /// <summary>Check if entity exists</summary>\n");
        code.append("        Task<bool> ExistsAsync(Guid id);\n\n");

        code.append("        /// <summary>Check with predicate</summary>\n");
        code.append("        Task<bool> ExistsAsync(Expression<Func<T, bool>> predicate);\n\n");

        // CRUD operations
        code.append("        // ==================== CRUD OPERATIONS ====================\n\n");

        code.append("        /// <summary>Add entity</summary>\n");
        code.append("        Task<T> AddAsync(T entity);\n\n");

        code.append("        /// <summary>Add multiple entities</summary>\n");
        code.append("        Task<IEnumerable<T>> AddRangeAsync(IEnumerable<T> entities);\n\n");

        code.append("        /// <summary>Update entity</summary>\n");
        code.append("        Task<T> UpdateAsync(T entity);\n\n");

        code.append("        /// <summary>Delete entity</summary>\n");
        code.append("        Task<bool> DeleteAsync(Guid id);\n\n");

        code.append("        /// <summary>Delete entity object</summary>\n");
        code.append("        Task<bool> DeleteAsync(T entity);\n\n");

        code.append("        /// <summary>Delete multiple entities</summary>\n");
        code.append("        Task<int> DeleteRangeAsync(Expression<Func<T, bool>> predicate);\n\n");

        // Include operations
        code.append("        // ==================== INCLUDE OPERATIONS ====================\n\n");

        code.append("        /// <summary>Get with related entities</summary>\n");
        code.append("        Task<T> GetWithIncludesAsync(Guid id, params Expression<Func<T, object>>[] includes);\n\n");

        code.append("        /// <summary>Get all with includes</summary>\n");
        code.append("        Task<IEnumerable<T>> GetAllWithIncludesAsync(params Expression<Func<T, object>>[] includes);\n\n");

        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate repository implementation
     */
    public String generateRepositoryImplementation(String entityName) {
        StringBuilder code = new StringBuilder();

        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Linq;\n");
        code.append("using System.Linq.Expressions;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using Microsoft.EntityFrameworkCore;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using YourApp.Infrastructure.Data;\n\n");

        code.append("namespace YourApp.Infrastructure.Repositories\n");
        code.append("{\n");

        code.append("    /// <summary>\n");
        code.append("    /// Repository implementation for ").append(entityName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(entityName).append("Repository : IRepository<").append(entityName).append(">\n");
        code.append("    {\n");

        code.append("        private readonly ApplicationDbContext _context;\n");
        code.append("        private readonly ILogger<").append(entityName).append("Repository> _logger;\n");
        code.append("        private readonly DbSet<").append(entityName).append("> _dbSet;\n\n");

        code.append("        public ").append(entityName).append("Repository(\n");
        code.append("            ApplicationDbContext context,\n");
        code.append("            ILogger<").append(entityName).append("Repository> logger)\n");
        code.append("        {\n");
        code.append("            _context = context ?? throw new ArgumentNullException(nameof(context));\n");
        code.append("            _logger = logger ?? throw new ArgumentNullException(nameof(logger));\n");
        code.append("            _dbSet = _context.Set<").append(entityName).append(">();\n");
        code.append("        }\n\n");

        // Query operations
        code.append("        public async Task<IEnumerable<").append(entityName).append(">> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation(\"Fetching all ").append(entityName).append(" records\");\n");
        code.append("            return await _dbSet.Where(e => !e.IsDeleted).ToListAsync();\n");
        code.append("        }\n\n");

        code.append("        public async Task<(IEnumerable<").append(entityName).append(">, int)> GetPaginatedAsync(int page, int pageSize)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Fetching page {page} with size {pageSize}\");\n");
        code.append("            var query = _dbSet.Where(e => !e.IsDeleted);\n");
        code.append("            var total = await query.CountAsync();\n");
        code.append("            var items = await query.Skip((page - 1) * pageSize).Take(pageSize).ToListAsync();\n");
        code.append("            return (items, total);\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(entityName).append("> GetByIdAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Fetching ").append(entityName).append(" with id {id}\");\n");
        code.append("            return await _dbSet.FirstOrDefaultAsync(e => e.Id == id && !e.IsDeleted);\n");
        code.append("        }\n\n");

        code.append("        public async Task<IEnumerable<").append(entityName).append(">> FindAsync(Expression<Func<").append(entityName).append(", bool>> predicate)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation(\"Finding ").append(entityName).append(" with predicate\");\n");
        code.append("            return await _dbSet.Where(predicate).Where(e => !e.IsDeleted).ToListAsync();\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(entityName).append("> FirstOrDefaultAsync(Expression<Func<").append(entityName).append(", bool>> predicate)\n");
        code.append("        {\n");
        code.append("            return await _dbSet.Where(e => !e.IsDeleted).FirstOrDefaultAsync(predicate);\n");
        code.append("        }\n\n");

        // Count operations
        code.append("        public async Task<int> CountAsync()\n");
        code.append("        {\n");
        code.append("            return await _dbSet.Where(e => !e.IsDeleted).CountAsync();\n");
        code.append("        }\n\n");

        code.append("        public async Task<int> CountAsync(Expression<Func<").append(entityName).append(", bool>> predicate)\n");
        code.append("        {\n");
        code.append("            return await _dbSet.Where(e => !e.IsDeleted).Where(predicate).CountAsync();\n");
        code.append("        }\n\n");

        // Check operations
        code.append("        public async Task<bool> ExistsAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            return await _dbSet.AnyAsync(e => e.Id == id && !e.IsDeleted);\n");
        code.append("        }\n\n");

        code.append("        public async Task<bool> ExistsAsync(Expression<Func<").append(entityName).append(", bool>> predicate)\n");
        code.append("        {\n");
        code.append("            return await _dbSet.Where(e => !e.IsDeleted).AnyAsync(predicate);\n");
        code.append("        }\n\n");

        // CRUD operations
        code.append("        public async Task<").append(entityName).append("> AddAsync(").append(entityName).append(" entity)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Adding new ").append(entityName).append(": {entity.Id}\");\n");
        code.append("            await _dbSet.AddAsync(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");

        code.append("        public async Task<IEnumerable<").append(entityName).append(">> AddRangeAsync(IEnumerable<").append(entityName).append("> entities)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Adding {entities.Count()} ").append(entityName).append(" records\");\n");
        code.append("            await _dbSet.AddRangeAsync(entities);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entities;\n");
        code.append("        }\n\n");

        code.append("        public async Task<").append(entityName).append("> UpdateAsync(").append(entityName).append(" entity)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Updating ").append(entityName).append(": {entity.Id}\");\n");
        code.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        code.append("            _dbSet.Update(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return entity;\n");
        code.append("        }\n\n");

        code.append("        public async Task<bool> DeleteAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Deleting ").append(entityName).append(": {id}\");\n");
        code.append("            var entity = await GetByIdAsync(id);\n");
        code.append("            if (entity == null) return false;\n");
        code.append("            entity.Delete();\n");
        code.append("            _dbSet.Update(entity);\n");
        code.append("            await _context.SaveChangesAsync();\n");
        code.append("            return true;\n");
        code.append("        }\n\n");

        code.append("        public async Task<bool> DeleteAsync(").append(entityName).append(" entity)\n");
        code.append("        {\n");
        code.append("            return await DeleteAsync(entity.Id);\n");
        code.append("        }\n\n");

        code.append("        public async Task<int> DeleteRangeAsync(Expression<Func<").append(entityName).append(", bool>> predicate)\n");
        code.append("        {\n");
        code.append("            var entities = await _dbSet.Where(predicate).ToListAsync();\n");
        code.append("            foreach (var entity in entities) entity.Delete();\n");
        code.append("            _dbSet.UpdateRange(entities);\n");
        code.append("            return await _context.SaveChangesAsync();\n");
        code.append("        }\n\n");

        // Include operations
        code.append("        public async Task<").append(entityName).append("> GetWithIncludesAsync(Guid id, params Expression<Func<").append(entityName).append(", object>>[] includes)\n");
        code.append("        {\n");
        code.append("            var query = _dbSet.AsQueryable();\n");
        code.append("            foreach (var include in includes)\n");
        code.append("                query = query.Include(include);\n");
        code.append("            return await query.FirstOrDefaultAsync(e => e.Id == id && !e.IsDeleted);\n");
        code.append("        }\n\n");

        code.append("        public async Task<IEnumerable<").append(entityName).append(">> GetAllWithIncludesAsync(params Expression<Func<").append(entityName).append(", object>>[] includes)\n");
        code.append("        {\n");
        code.append("            var query = _dbSet.AsQueryable();\n");
        code.append("            foreach (var include in includes)\n");
        code.append("                query = query.Include(include);\n");
        code.append("            return await query.Where(e => !e.IsDeleted).ToListAsync();\n");
        code.append("        }\n\n");

        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }
}

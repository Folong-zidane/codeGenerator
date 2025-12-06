package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;

/**
 * Enhanced C# Service Generator with business logic
 */
public class CSharpServiceGeneratorEnhanced {
    
    public String generateServiceInterface(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using ").append(netNamespace).append(".Models;\n");
        code.append("using ").append(netNamespace).append(".DTOs;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Services.Interfaces\n");
        code.append("{\n");
        
        // Interface
        code.append("    /// <summary>\n");
        code.append("    /// Service interface for ").append(className).append(" business logic\n");
        code.append("    /// </summary>\n");
        code.append("    public interface I").append(className).append("Service\n");
        code.append("    {\n");
        
        // CRUD operations
        code.append("        /// <summary>\n");
        code.append("        /// Get all ").append(className.toLowerCase()).append("s\n");
        code.append("        /// </summary>\n");
        code.append("        Task<IEnumerable<").append(className).append("ReadDto>> GetAllAsync();\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Get paginated ").append(className.toLowerCase()).append("s\n");
        code.append("        /// </summary>\n");
        code.append("        Task<(IEnumerable<").append(className).append("ReadDto>, int totalCount)> GetPaginatedAsync(int page, int pageSize);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Get ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        Task<").append(className).append("ReadDto?> GetByIdAsync(Guid id);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Create new ").append(className.toLowerCase()).append("\n");
        code.append("        /// </summary>\n");
        code.append("        Task<").append(className).append("ReadDto> CreateAsync(").append(className).append("CreateDto dto);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Update ").append(className.toLowerCase()).append("\n");
        code.append("        /// </summary>\n");
        code.append("        Task<").append(className).append("ReadDto> UpdateAsync(Guid id, ").append(className).append("UpdateDto dto);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Delete ").append(className.toLowerCase()).append("\n");
        code.append("        /// </summary>\n");
        code.append("        Task<bool> DeleteAsync(Guid id);\n\n");
        
        // Business operations
        code.append("        /// <summary>\n");
        code.append("        /// Check if ").append(className.toLowerCase()).append(" exists\n");
        code.append("        /// </summary>\n");
        code.append("        Task<bool> ExistsAsync(Guid id);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Search ").append(className.toLowerCase()).append("s\n");
        code.append("        /// </summary>\n");
        code.append("        Task<IEnumerable<").append(className).append("ReadDto>> SearchAsync(string query);\n\n");
        
        // Bulk operations
        code.append("        /// <summary>\n");
        code.append("        /// Bulk create ").append(className.toLowerCase()).append("s\n");
        code.append("        /// </summary>\n");
        code.append("        Task<IEnumerable<").append(className).append("ReadDto>> BulkCreateAsync(IEnumerable<").append(className).append("CreateDto> dtos);\n\n");
        
        code.append("        /// <summary>\n");
        code.append("        /// Get statistics\n");
        code.append("        /// </summary>\n");
        code.append("        Task<object> GetStatisticsAsync();\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateServiceImplementation(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Linq;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using AutoMapper;\n");
        code.append("using ").append(netNamespace).append(".Models;\n");
        code.append("using ").append(netNamespace).append(".DTOs;\n");
        code.append("using ").append(netNamespace).append(".Repositories.Interfaces;\n");
        code.append("using ").append(netNamespace).append(".Services.Interfaces;\n");
        code.append("using ").append(netNamespace).append(".Exceptions;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Services\n");
        code.append("{\n");
        
        // Service class
        code.append("    /// <summary>\n");
        code.append("    /// Service implementation for ").append(className).append(" business logic\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("Service : I").append(className).append("Service\n");
        code.append("    {\n");
        
        // Fields
        code.append("        private readonly I").append(className).append("Repository _repository;\n");
        code.append("        private readonly IMapper _mapper;\n");
        code.append("        private readonly ILogger<").append(className).append("Service> _logger;\n\n");
        
        // Constructor
        code.append("        public ").append(className).append("Service(\n");
        code.append("            I").append(className).append("Repository repository,\n");
        code.append("            IMapper mapper,\n");
        code.append("            ILogger<").append(className).append("Service> logger)\n");
        code.append("        {\n");
        code.append("            _repository = repository ?? throw new ArgumentNullException(nameof(repository));\n");
        code.append("            _mapper = mapper ?? throw new ArgumentNullException(nameof(mapper));\n");
        code.append("            _logger = logger ?? throw new ArgumentNullException(nameof(logger));\n");
        code.append("        }\n\n");
        
        // GetAllAsync
        code.append("        public async Task<IEnumerable<").append(className).append("ReadDto>> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Getting all ").append(className.toLowerCase()).append("s\");\n");
        code.append("                var entities = await _repository.GetAllAsync();\n");
        code.append("                return _mapper.Map<IEnumerable<").append(className).append("ReadDto>>(entities);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting all ").append(className.toLowerCase()).append("s\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // GetPaginatedAsync
        code.append("        public async Task<(IEnumerable<").append(className).append("ReadDto>, int totalCount)> GetPaginatedAsync(int page, int pageSize)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Getting paginated ").append(className.toLowerCase()).append("s - Page: {Page}, Size: {PageSize}\", page, pageSize);\n");
        code.append("                \n");
        code.append("                if (page < 1) page = 1;\n");
        code.append("                if (pageSize < 1) pageSize = 10;\n");
        code.append("                if (pageSize > 100) pageSize = 100;\n");
        code.append("                \n");
        code.append("                var (entities, totalCount) = await _repository.GetPaginatedAsync(page, pageSize);\n");
        code.append("                var dtos = _mapper.Map<IEnumerable<").append(className).append("ReadDto>>(entities);\n");
        code.append("                \n");
        code.append("                return (dtos, totalCount);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting paginated ").append(className.toLowerCase()).append("s\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // GetByIdAsync
        code.append("        public async Task<").append(className).append("ReadDto?> GetByIdAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Getting ").append(className.toLowerCase()).append(" by ID: {Id}\", id);\n");
        code.append("                \n");
        code.append("                var entity = await _repository.GetByIdAsync(id);\n");
        code.append("                return entity != null ? _mapper.Map<").append(className).append("ReadDto>(entity) : null;\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting ").append(className.toLowerCase()).append(" by ID: {Id}\", id);\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // CreateAsync
        code.append("        public async Task<").append(className).append("ReadDto> CreateAsync(").append(className).append("CreateDto dto)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Creating new ").append(className.toLowerCase()).append("\");\n");
        code.append("                \n");
        code.append("                // Business validation\n");
        code.append("                await ValidateForCreateAsync(dto);\n");
        code.append("                \n");
        code.append("                var entity = _mapper.Map<").append(className).append(">(dto);\n");
        code.append("                entity.Id = Guid.NewGuid();\n");
        code.append("                entity.CreatedAt = DateTime.UtcNow;\n");
        code.append("                \n");
        code.append("                var created = await _repository.CreateAsync(entity);\n");
        code.append("                \n");
        code.append("                _logger.LogInformation(\"Created ").append(className.toLowerCase()).append(" with ID: {Id}\", created.Id);\n");
        code.append("                return _mapper.Map<").append(className).append("ReadDto>(created);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error creating ").append(className.toLowerCase()).append("\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // UpdateAsync
        code.append("        public async Task<").append(className).append("ReadDto> UpdateAsync(Guid id, ").append(className).append("UpdateDto dto)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Updating ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                \n");
        code.append("                var existing = await _repository.GetByIdAsync(id);\n");
        code.append("                if (existing == null)\n");
        code.append("                {\n");
        code.append("                    throw new EntityNotFoundException($\"").append(className).append(" with ID {id} not found\");\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                // Business validation\n");
        code.append("                await ValidateForUpdateAsync(dto, existing);\n");
        code.append("                \n");
        code.append("                _mapper.Map(dto, existing);\n");
        code.append("                existing.UpdatedAt = DateTime.UtcNow;\n");
        code.append("                \n");
        code.append("                var updated = await _repository.UpdateAsync(existing);\n");
        code.append("                \n");
        code.append("                _logger.LogInformation(\"Updated ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                return _mapper.Map<").append(className).append("ReadDto>(updated);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error updating ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // DeleteAsync
        code.append("        public async Task<bool> DeleteAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Deleting ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                \n");
        code.append("                var exists = await _repository.ExistsAsync(id);\n");
        code.append("                if (!exists)\n");
        code.append("                {\n");
        code.append("                    throw new EntityNotFoundException($\"").append(className).append(" with ID {id} not found\");\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                await _repository.DeleteAsync(id);\n");
        code.append("                \n");
        code.append("                _logger.LogInformation(\"Deleted ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                return true;\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error deleting ").append(className.toLowerCase()).append(" with ID: {Id}\", id);\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // Business validation methods
        code.append("        private async Task ValidateForCreateAsync(").append(className).append("CreateDto dto)\n");
        code.append("        {\n");
        code.append("            // Add business validation logic here\n");
        code.append("            await Task.CompletedTask;\n");
        code.append("        }\n\n");
        
        code.append("        private async Task ValidateForUpdateAsync(").append(className).append("UpdateDto dto, ").append(className).append(" existing)\n");
        code.append("        {\n");
        code.append("            // Add business validation logic here\n");
        code.append("            await Task.CompletedTask;\n");
        code.append("        }\n\n");
        
        // Additional methods
        generateAdditionalMethods(code, className);
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private void generateAdditionalMethods(StringBuilder code, String className) {
        // ExistsAsync
        code.append("        public async Task<bool> ExistsAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            return await _repository.ExistsAsync(id);\n");
        code.append("        }\n\n");
        
        // SearchAsync
        code.append("        public async Task<IEnumerable<").append(className).append("ReadDto>> SearchAsync(string query)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Searching ").append(className.toLowerCase()).append("s with query: {Query}\", query);\n");
        code.append("                \n");
        code.append("                var entities = await _repository.SearchAsync(query);\n");
        code.append("                return _mapper.Map<IEnumerable<").append(className).append("ReadDto>>(entities);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error searching ").append(className.toLowerCase()).append("s\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // BulkCreateAsync
        code.append("        public async Task<IEnumerable<").append(className).append("ReadDto>> BulkCreateAsync(IEnumerable<").append(className).append("CreateDto> dtos)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Bulk creating {Count} ").append(className.toLowerCase()).append("s\", dtos.Count());\n");
        code.append("                \n");
        code.append("                var entities = new List<").append(className).append(">();\n");
        code.append("                foreach (var dto in dtos)\n");
        code.append("                {\n");
        code.append("                    await ValidateForCreateAsync(dto);\n");
        code.append("                    var entity = _mapper.Map<").append(className).append(">(dto);\n");
        code.append("                    entity.Id = Guid.NewGuid();\n");
        code.append("                    entity.CreatedAt = DateTime.UtcNow;\n");
        code.append("                    entities.Add(entity);\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                var created = await _repository.BulkCreateAsync(entities);\n");
        code.append("                return _mapper.Map<IEnumerable<").append(className).append("ReadDto>>(created);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error bulk creating ").append(className.toLowerCase()).append("s\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // GetStatisticsAsync
        code.append("        public async Task<object> GetStatisticsAsync()\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation(\"Getting ").append(className.toLowerCase()).append(" statistics\");\n");
        code.append("                \n");
        code.append("                var totalCount = await _repository.CountAsync();\n");
        code.append("                var recentCount = await _repository.CountRecentAsync(TimeSpan.FromDays(30));\n");
        code.append("                \n");
        code.append("                return new\n");
        code.append("                {\n");
        code.append("                    TotalCount = totalCount,\n");
        code.append("                    RecentCount = recentCount,\n");
        code.append("                    LastUpdated = DateTime.UtcNow\n");
        code.append("                };\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting ").append(className.toLowerCase()).append(" statistics\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n");
    }
    
    private String convertToNetNamespace(String javaPackage) {
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalize(parts[1]) + ".Application";
        }
        return capitalize(javaPackage.replace(".", "")) + ".Application";
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
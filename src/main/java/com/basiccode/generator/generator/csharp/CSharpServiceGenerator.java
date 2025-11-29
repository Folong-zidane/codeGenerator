package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.BusinessMethod;

/**
 * C# Service generator for .NET Core
 * Generates service classes with business logic and dependency injection
 */
public class CSharpServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Generate Service Interface
        code.append(generateServiceInterface(className, packageName, enhancedClass));
        code.append("\n\n");
        
        // Generate Service Implementation
        code.append(generateServiceImplementation(className, packageName, enhancedClass));
        
        return code.toString();
    }
    
    @Override
    public String getServiceDirectory() {
        return "Services";
    }
    
    private String generateServiceInterface(String className, String packageName, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();
        
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("using ").append(packageName).append(".Enums;\n\n");
        }
        
        code.append("namespace ").append(packageName).append(".Services\n");
        code.append("{\n");
        code.append("    public interface I").append(className).append("Service\n");
        code.append("    {\n");
        
        // CRUD methods
        code.append("        Task<IEnumerable<").append(className).append(">> GetAllAsync();\n");
        code.append("        Task<").append(className).append("?> GetByIdAsync(int id);\n");
        code.append("        Task<").append(className).append("> CreateAsync(").append(className).append(" entity);\n");
        code.append("        Task<").append(className).append("> UpdateAsync(int id, ").append(className).append(" entity);\n");
        code.append("        Task DeleteAsync(int id);\n");
        
        // State management methods if stateful
        if (enhancedClass.isStateful()) {
            code.append("        Task<").append(className).append("> Suspend").append(className).append("Async(int id);\n");
            code.append("        Task<").append(className).append("> Activate").append(className).append("Async(int id);\n");
        }
        
        // Behavioral methods if available
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("        Task<").append(mapReturnType(method.getReturnType())).append("> ");
                code.append(capitalize(method.getName())).append("Async();\n");
            }
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateServiceImplementation(String className, String packageName, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();
        
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using ").append(packageName).append(".Repositories;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using System;\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("using ").append(packageName).append(".Enums;\n\n");
        }
        
        code.append("namespace ").append(packageName).append(".Services\n");
        code.append("{\n");
        code.append("    public class ").append(className).append("Service : I").append(className).append("Service\n");
        code.append("    {\n");
        code.append("        private readonly I").append(className).append("Repository _repository;\n\n");
        
        // Constructor with DI
        code.append("        public ").append(className).append("Service(I").append(className).append("Repository repository)\n");
        code.append("        {\n");
        code.append("            _repository = repository;\n");
        code.append("        }\n\n");
        
        // GetAllAsync
        code.append("        public async Task<IEnumerable<").append(className).append(">> GetAllAsync()\n");
        code.append("        {\n");
        code.append("            return await _repository.GetAllAsync();\n");
        code.append("        }\n\n");
        
        // GetByIdAsync
        code.append("        public async Task<").append(className).append("?> GetByIdAsync(int id)\n");
        code.append("        {\n");
        code.append("            return await _repository.GetByIdAsync(id);\n");
        code.append("        }\n\n");
        
        // CreateAsync
        code.append("        public async Task<").append(className).append("> CreateAsync(").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            // Add business logic and validation here\n");
        code.append("            ValidateEntity(entity);\n");
        code.append("            \n");
        code.append("            return await _repository.CreateAsync(entity);\n");
        code.append("        }\n\n");
        
        // UpdateAsync
        code.append("        public async Task<").append(className).append("> UpdateAsync(int id, ").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            var existing = await _repository.GetByIdAsync(id);\n");
        code.append("            if (existing == null)\n");
        code.append("            {\n");
        code.append("                throw new ArgumentException($\"").append(className).append(" with id {id} not found\");\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            // Add business logic and validation here\n");
        code.append("            ValidateEntity(entity);\n");
        code.append("            \n");
        code.append("            // Update properties\n");
        code.append("            entity.Id = id;\n");
        code.append("            return await _repository.UpdateAsync(entity);\n");
        code.append("        }\n\n");
        
        // DeleteAsync
        code.append("        public async Task DeleteAsync(int id)\n");
        code.append("        {\n");
        code.append("            var exists = await _repository.ExistsAsync(id);\n");
        code.append("            if (!exists)\n");
        code.append("            {\n");
        code.append("                throw new ArgumentException($\"").append(className).append(" with id {id} not found\");\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            await _repository.DeleteAsync(id);\n");
        code.append("        }\n\n");
        
        // State management methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementMethods(code, className, enhancedClass);
        }
        
        // Behavioral methods if available
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("        public async Task<").append(mapReturnType(method.getReturnType())).append("> ");
                code.append(capitalize(method.getName())).append("Async()\n");
                code.append("        {\n");
                code.append("            // Generated from sequence diagram\n");
                for (String logic : method.getBusinessLogic()) {
                    code.append("            ").append(logic).append("\n");
                }
                code.append("            \n");
                code.append("            // TODO: Implement business logic\n");
                code.append("            throw new NotImplementedException();\n");
                code.append("        }\n\n");
            }
        }
        
        // Validation method
        code.append("        private void ValidateEntity(").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            if (entity == null)\n");
        code.append("            {\n");
        code.append("                throw new ArgumentNullException(nameof(entity));\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            // Add custom validation logic here\n");
        code.append("        }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private void generateStateManagementMethods(StringBuilder code, String className, EnhancedClass enhancedClass) {
        // Suspend method
        code.append("        public async Task<").append(className).append("> Suspend").append(className).append("Async(int id)\n");
        code.append("        {\n");
        code.append("            var entity = await _repository.GetByIdAsync(id);\n");
        code.append("            if (entity == null)\n");
        code.append("            {\n");
        code.append("                throw new ArgumentException($\"").append(className).append(" with id {id} not found\");\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            entity.Suspend();\n");
        code.append("            return await _repository.UpdateAsync(entity);\n");
        code.append("        }\n\n");
        
        // Activate method
        code.append("        public async Task<").append(className).append("> Activate").append(className).append("Async(int id)\n");
        code.append("        {\n");
        code.append("            var entity = await _repository.GetByIdAsync(id);\n");
        code.append("            if (entity == null)\n");
        code.append("            {\n");
        code.append("                throw new ArgumentException($\"").append(className).append(" with id {id} not found\");\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            entity.Activate();\n");
        code.append("            return await _repository.UpdateAsync(entity);\n");
        code.append("        }\n\n");
    }
    
    private String mapReturnType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "void" -> "void";
            case "boolean" -> "bool";
            case "integer", "int" -> "int";
            case "long" -> "long";
            case "float" -> "float";
            case "double" -> "double";
            default -> "object";
        };
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
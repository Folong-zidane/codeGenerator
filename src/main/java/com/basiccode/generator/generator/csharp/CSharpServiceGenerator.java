package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLMethod;
import java.util.Map;

/**
 * Générateur de services C# avec injection de dépendances
 */
public class CSharpServiceGenerator implements IServiceGenerator {
    
    @Override
    public String getServiceDirectory() {
        return "Services";
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        return generateService(enhancedClass, packageName, null);
    }
    
    public String generateService(EnhancedClass enhancedClass, String packageName, Map<String, String> metadata) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder service = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String serviceName = className + "Service";
        String interfaceName = "I" + serviceName;
        
        // Generate interface first
        service.append(generateServiceInterface(enhancedClass, packageName));
        service.append("\n\n");
        
        // Using statements
        service.append("using System;\n");
        service.append("using System.Collections.Generic;\n");
        service.append("using System.Threading.Tasks;\n");
        service.append("using Microsoft.Extensions.Logging;\n");
        service.append("using ").append(packageName).append(".Entities;\n");
        service.append("using ").append(packageName).append(".Repositories;\n\n");
        
        // Namespace
        service.append("namespace ").append(packageName).append(".Services\n{\n");
        
        // Service class
        service.append("    public class ").append(serviceName).append(" : ").append(interfaceName).append("\n    {\n");
        
        // Fields
        service.append("        private readonly I").append(className).append("Repository _repository;\n");
        service.append("        private readonly ILogger<").append(serviceName).append("> _logger;\n\n");
        
        // Constructor
        service.append("        public ").append(serviceName).append("(\n");
        service.append("            I").append(className).append("Repository repository,\n");
        service.append("            ILogger<").append(serviceName).append("> logger)\n");
        service.append("        {\n");
        service.append("            _repository = repository ?? throw new ArgumentNullException(nameof(repository));\n");
        service.append("            _logger = logger ?? throw new ArgumentNullException(nameof(logger));\n");
        service.append("        }\n\n");
        
        // CRUD methods
        service.append(generateCrudMethods(className));
        
        // Business methods from UML
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (UMLMethod method : enhancedClass.getOriginalClass().getMethods()) {
                service.append(generateBusinessMethod(method, className));
            }
        }
        
        service.append("    }\n");
        service.append("}\n");
        
        return service.toString();
    }
    
    private String generateServiceInterface(EnhancedClass enhancedClass, String packageName) {
        StringBuilder interfaceCode = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String interfaceName = "I" + className + "Service";
        
        interfaceCode.append("using System;\n");
        interfaceCode.append("using System.Collections.Generic;\n");
        interfaceCode.append("using System.Threading.Tasks;\n");
        interfaceCode.append("using ").append(packageName).append(".Entities;\n\n");
        
        interfaceCode.append("namespace ").append(packageName).append(".Services\n{\n");
        interfaceCode.append("    public interface ").append(interfaceName).append("\n    {\n");
        
        // CRUD interface methods
        interfaceCode.append("        Task<").append(className).append("> GetByIdAsync(long id);\n");
        interfaceCode.append("        Task<IEnumerable<").append(className).append(">> GetAllAsync();\n");
        interfaceCode.append("        Task<").append(className).append("> CreateAsync(").append(className).append(" entity);\n");
        interfaceCode.append("        Task<").append(className).append("> UpdateAsync(").append(className).append(" entity);\n");
        interfaceCode.append("        Task<bool> DeleteAsync(long id);\n");
        
        // Business method signatures
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (UMLMethod method : enhancedClass.getOriginalClass().getMethods()) {
                interfaceCode.append("        ").append(generateMethodSignature(method)).append(";\n");
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
        methods.append("            _logger.LogInformation(\"Getting ").append(className).append(" with ID: {Id}\", id);\n");
        methods.append("            \n");
        methods.append("            if (id <= 0)\n");
        methods.append("                throw new ArgumentException(\"ID must be greater than 0\", nameof(id));\n");
        methods.append("            \n");
        methods.append("            var entity = await _repository.GetByIdAsync(id);\n");
        methods.append("            if (entity == null)\n");
        methods.append("                throw new KeyNotFoundException($\"").append(className).append(" with ID {id} not found\");\n");
        methods.append("            \n");
        methods.append("            return entity;\n");
        methods.append("        }\n\n");
        
        // GetAllAsync
        methods.append("        public async Task<IEnumerable<").append(className).append(">> GetAllAsync()\n");
        methods.append("        {\n");
        methods.append("            _logger.LogInformation(\"Getting all ").append(className).append(" entities\");\n");
        methods.append("            return await _repository.GetAllAsync();\n");
        methods.append("        }\n\n");
        
        // CreateAsync
        methods.append("        public async Task<").append(className).append("> CreateAsync(").append(className).append(" entity)\n");
        methods.append("        {\n");
        methods.append("            _logger.LogInformation(\"Creating new ").append(className).append("\");\n");
        methods.append("            \n");
        methods.append("            if (entity == null)\n");
        methods.append("                throw new ArgumentNullException(nameof(entity));\n");
        methods.append("            \n");
        methods.append("            entity.CreatedAt = DateTime.UtcNow;\n");
        methods.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        methods.append("            \n");
        methods.append("            return await _repository.CreateAsync(entity);\n");
        methods.append("        }\n\n");
        
        // UpdateAsync
        methods.append("        public async Task<").append(className).append("> UpdateAsync(").append(className).append(" entity)\n");
        methods.append("        {\n");
        methods.append("            _logger.LogInformation(\"Updating ").append(className).append(" with ID: {Id}\", entity.Id);\n");
        methods.append("            \n");
        methods.append("            if (entity == null)\n");
        methods.append("                throw new ArgumentNullException(nameof(entity));\n");
        methods.append("            \n");
        methods.append("            var existingEntity = await _repository.GetByIdAsync(entity.Id);\n");
        methods.append("            if (existingEntity == null)\n");
        methods.append("                throw new KeyNotFoundException($\"").append(className).append(" with ID {entity.Id} not found\");\n");
        methods.append("            \n");
        methods.append("            entity.UpdatedAt = DateTime.UtcNow;\n");
        methods.append("            return await _repository.UpdateAsync(entity);\n");
        methods.append("        }\n\n");
        
        // DeleteAsync
        methods.append("        public async Task<bool> DeleteAsync(long id)\n");
        methods.append("        {\n");
        methods.append("            _logger.LogInformation(\"Deleting ").append(className).append(" with ID: {Id}\", id);\n");
        methods.append("            \n");
        methods.append("            if (id <= 0)\n");
        methods.append("                throw new ArgumentException(\"ID must be greater than 0\", nameof(id));\n");
        methods.append("            \n");
        methods.append("            var entity = await _repository.GetByIdAsync(id);\n");
        methods.append("            if (entity == null)\n");
        methods.append("                return false;\n");
        methods.append("            \n");
        methods.append("            return await _repository.DeleteAsync(id);\n");
        methods.append("        }\n\n");
        
        return methods.toString();
    }
    
    private String generateBusinessMethod(UMLMethod method, String className) {
        StringBuilder methodCode = new StringBuilder();
        String methodName = method.getName();
        String returnType = mapToCSharpType(method.getReturnType());
        
        methodCode.append("        public async Task<").append(returnType).append("> ").append(methodName).append("Async(");
        
        // Parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) methodCode.append(", ");
                var param = method.getParameters().get(i);
                methodCode.append(mapToCSharpType(param.getType())).append(" ").append(param.getName());
            }
        }
        
        methodCode.append(")\n        {\n");
        methodCode.append("            _logger.LogInformation(\"Executing ").append(methodName).append("\");\n");
        methodCode.append("            \n");
        methodCode.append("            // TODO: Implement business logic for ").append(methodName).append("\n");
        methodCode.append("            throw new NotImplementedException(\"Business method ").append(methodName).append(" not implemented\");\n");
        methodCode.append("        }\n\n");
        
        return methodCode.toString();
    }
    
    private String generateMethodSignature(UMLMethod method) {
        StringBuilder signature = new StringBuilder();
        String returnType = mapToCSharpType(method.getReturnType());
        
        signature.append("Task<").append(returnType).append("> ").append(method.getName()).append("Async(");
        
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) signature.append(", ");
                var param = method.getParameters().get(i);
                signature.append(mapToCSharpType(param.getType())).append(" ").append(param.getName());
            }
        }
        
        signature.append(")");
        return signature.toString();
    }
    
    private String mapToCSharpType(String umlType) {
        if (umlType == null) return "object";
        
        switch (umlType.toLowerCase()) {
            case "string": return "string";
            case "integer": case "int": return "int";
            case "long": return "long";
            case "boolean": case "bool": return "bool";
            case "double": return "double";
            case "float": return "float";
            case "decimal": case "bigdecimal": return "decimal";
            case "datetime": case "localdatetime": return "DateTime";
            case "void": return "bool";
            default: return "object";
        }
    }
}
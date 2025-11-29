package com.basiccode.generator.service;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.model.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Orchestrates the code generation process
 * Implements Template Method pattern for consistent generation flow
 */
@Service
public class CodeGenerationOrchestrator {
    
    /**
     * Generate complete project using factory
     */
    public ComprehensiveCodeResult generateProject(ComprehensiveDiagram model, 
                                                  String packageName, 
                                                  LanguageGeneratorFactory factory) {
        
        ComprehensiveCodeResult result = new ComprehensiveCodeResult();
        Map<String, String> allFiles = new HashMap<>();
        
        // Create generators
        IEntityGenerator entityGen = factory.createEntityGenerator();
        IRepositoryGenerator repoGen = factory.createRepositoryGenerator();
        IServiceGenerator serviceGen = factory.createServiceGenerator();
        IControllerGenerator controllerGen = factory.createControllerGenerator();
        IMigrationGenerator migrationGen = factory.createMigrationGenerator();
        IFileWriter fileWriter = factory.createFileWriter();
        
        // Generate code for each enhanced class
        for (EnhancedClass enhancedClass : model.getEnhancedClasses()) {
            String className = enhancedClass.getOriginalClass().getName();
            
            // Generate entity
            String entityCode = entityGen.generateEntity(enhancedClass, packageName);
            allFiles.put(getEntityPath(className, entityGen), entityCode);
            
            // Generate state enum if stateful
            if (enhancedClass.isStateful()) {
                String enumCode = entityGen.generateStateEnum(enhancedClass, packageName);
                allFiles.put(getEnumPath(enhancedClass.getStateEnum().getName(), entityGen), enumCode);
            }
            
            // Generate repository
            String repoCode = repoGen.generateRepository(enhancedClass, packageName);
            allFiles.put(getRepositoryPath(className, repoGen), repoCode);
            
            // Generate service
            String serviceCode = serviceGen.generateService(enhancedClass, packageName);
            allFiles.put(getServicePath(className, serviceGen), serviceCode);
            
            // Generate controller
            String controllerCode = controllerGen.generateController(enhancedClass, packageName);
            allFiles.put(getControllerPath(className, controllerGen), controllerCode);
        }
        
        // Generate migrations
        String migrationCode = migrationGen.generateMigration(model.getEnhancedClasses(), packageName);
        allFiles.put(getMigrationPath(migrationGen), migrationCode);
        
        // Generate documentation
        String documentation = generateDocumentation(model);
        allFiles.put("README.md", documentation);
        
        result.setFiles(allFiles);
        return result;
    }
    
    private String getEntityPath(String className, IEntityGenerator generator) {
        return generator.getEntityDirectory() + "/" + className + generator.getFileExtension();
    }
    
    private String getEnumPath(String enumName, IEntityGenerator generator) {
        return "enums/" + enumName + generator.getFileExtension();
    }
    
    private String getRepositoryPath(String className, IRepositoryGenerator generator) {
        return generator.getRepositoryDirectory() + "/" + className + "Repository.java";
    }
    
    private String getServicePath(String className, IServiceGenerator generator) {
        return generator.getServiceDirectory() + "/" + className + "Service.java";
    }
    
    private String getControllerPath(String className, IControllerGenerator generator) {
        return generator.getControllerDirectory() + "/" + className + "Controller.java";
    }
    
    private String getMigrationPath(IMigrationGenerator generator) {
        return generator.getMigrationDirectory() + "/V001__Initial_Schema.sql";
    }
    
    private String generateDocumentation(ComprehensiveDiagram model) {
        StringBuilder doc = new StringBuilder();
        
        doc.append("# Generated Application\n\n");
        doc.append("This application was generated from UML diagrams using the modular code generator.\n\n");
        doc.append("## Architecture\n\n");
        doc.append("- **Entities**: Domain models with state management\n");
        doc.append("- **Repositories**: Data access layer\n");
        doc.append("- **Services**: Business logic layer\n");
        doc.append("- **Controllers**: REST API endpoints\n\n");
        
        doc.append("## Generated Classes\n\n");
        for (EnhancedClass enhancedClass : model.getEnhancedClasses()) {
            doc.append("### ").append(enhancedClass.getOriginalClass().getName()).append("\n");
            if (enhancedClass.isStateful()) {
                doc.append("- **Stateful entity** with state transitions\n");
            }
            if (enhancedClass.getBehavioralMethods() != null) {
                doc.append("- **Business methods**: ").append(enhancedClass.getBehavioralMethods().size()).append(" methods\n");
            }
            doc.append("\n");
        }
        
        return doc.toString();
    }
}
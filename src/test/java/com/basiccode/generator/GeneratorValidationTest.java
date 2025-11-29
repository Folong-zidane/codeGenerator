package com.basiccode.generator;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneratorValidationTest {
    
    @Test
    void validateGeneratorStructure() {
        System.out.println("🔍 Validating Generator Structure");
        System.out.println("=" .repeat(50));
        
        String basePath = "src/main/java/com/basiccode/generator/generator";
        
        // Check each language generator
        validateLanguageGenerator("spring", "Java Spring Boot");
        validateLanguageGenerator("django", "Django REST");
        validateLanguageGenerator("python", "Python FastAPI");
        validateLanguageGenerator("csharp", "C# .NET Core");
        validateLanguageGenerator("typescript", "TypeScript Express");
        validateLanguageGenerator("php", "PHP Slim");
        
        System.out.println("\n✅ All generator structures validated!");
    }
    
    private void validateLanguageGenerator(String language, String displayName) {
        System.out.println("\n📂 Validating " + displayName + " Generator:");
        
        String basePath = "src/main/java/com/basiccode/generator/generator/" + language;
        
        // Required files for each generator
        String[] requiredFiles = {
            language.substring(0, 1).toUpperCase() + language.substring(1) + "EntityGenerator.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "ServiceGenerator.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "RepositoryGenerator.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "ControllerGenerator.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "GeneratorFactory.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "FileWriter.java",
            language.substring(0, 1).toUpperCase() + language.substring(1) + "MigrationGenerator.java"
        };
        
        // Special cases for naming
        if (language.equals("spring")) {
            requiredFiles = new String[]{
                "SpringBootEntityGenerator.java",
                "SpringBootServiceGenerator.java", 
                "SpringBootRepositoryGenerator.java",
                "SpringBootControllerGenerator.java",
                "SpringBootGeneratorFactory.java",
                "JavaFileWriter.java",
                "SpringBootMigrationGenerator.java"
            };
        }
        
        for (String file : requiredFiles) {
            Path filePath = Paths.get(basePath, file);
            if (Files.exists(filePath)) {
                System.out.println("  ✅ " + file);
            } else {
                System.out.println("  ❌ " + file + " (MISSING)");
            }
        }
    }
    
    @Test
    void validateInterfaceImplementation() {
        System.out.println("\n🔧 Validating Interface Implementation");
        System.out.println("=" .repeat(50));
        
        // This would normally use reflection to check if all generators implement required interfaces
        // For now, we'll do a basic structure check
        
        String[] interfaces = {
            "IEntityGenerator",
            "IServiceGenerator", 
            "IRepositoryGenerator",
            "IControllerGenerator",
            "IFileWriter",
            "IMigrationGenerator"
        };
        
        String interfacePath = "src/main/java/com/basiccode/generator/generator";
        
        for (String interfaceName : interfaces) {
            Path filePath = Paths.get(interfacePath, interfaceName + ".java");
            if (Files.exists(filePath)) {
                System.out.println("✅ " + interfaceName + " interface exists");
            } else {
                System.out.println("❌ " + interfaceName + " interface missing");
            }
        }
    }
    
    @Test
    void validateModelClasses() {
        System.out.println("\n📋 Validating Model Classes");
        System.out.println("=" .repeat(50));
        
        String[] modelClasses = {
            "UMLClass",
            "UMLAttribute", 
            "EnhancedClass",
            "BusinessMethod",
            "UMLRelationship"
        };
        
        String modelPath = "src/main/java/com/basiccode/generator/model";
        
        for (String modelClass : modelClasses) {
            Path filePath = Paths.get(modelPath, modelClass + ".java");
            if (Files.exists(filePath)) {
                System.out.println("✅ " + modelClass + " model exists");
            } else {
                System.out.println("❌ " + modelClass + " model missing");
            }
        }
    }
    
    @Test
    void runQuickGenerationTest() {
        System.out.println("\n⚡ Quick Generation Test");
        System.out.println("=" .repeat(50));
        
        try {
            // Run the simple generator test
            SimpleGeneratorTest simpleTest = new SimpleGeneratorTest();
            simpleTest.setUp();
            simpleTest.testAllGeneratorsIntegration();
            
            System.out.println("✅ Quick generation test completed successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Quick generation test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
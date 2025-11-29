package com.basiccode.generator;

import com.basiccode.generator.generator.django.DjangoGeneratorFactory;
import com.basiccode.generator.generator.spring.SpringBootGeneratorFactory;
import com.basiccode.generator.generator.python.PythonGeneratorFactory;
import com.basiccode.generator.generator.csharp.CSharpGeneratorFactory;
import com.basiccode.generator.generator.typescript.TypeScriptGeneratorFactory;
import com.basiccode.generator.generator.php.PhpGeneratorFactory;
import com.basiccode.generator.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.ArrayList;

public class SimpleGeneratorTest {
    
    private EnhancedClass testClass;
    private String packageName = "com.test.app";
    
    @BeforeEach
    void setUp() {
        // Create test UML class
        UmlClass umlClass = new UmlClass("User");
        
        // Add attributes manually
        UmlAttribute id = new UmlAttribute("id", "UUID");
        UmlAttribute username = new UmlAttribute("username", "String");
        UmlAttribute email = new UmlAttribute("email", "String");
        UmlAttribute status = new UmlAttribute("status", "UserStatus");
        
        umlClass.setAttributes(Arrays.asList(id, username, email, status));
        
        // Create enhanced class
        testClass = new EnhancedClass(umlClass);
        testClass.setStateful(true);
        
        // Add behavioral method
        BusinessMethod method = new BusinessMethod();
        method.setName("validateUser");
        method.setReturnType("boolean");
        method.setBusinessLogic(Arrays.asList("Check email format", "Validate username uniqueness"));
        
        testClass.setBehavioralMethods(Arrays.asList(method));
    }
    
    @Test
    void testJavaSpringBootGenerator() {
        System.out.println("=== Testing Java Spring Boot Generator ===");
        
        var entityGen = new SpringBootGeneratorFactory().createEntityGenerator();
        var serviceGen = new SpringBootGeneratorFactory().createServiceGenerator();
        var repoGen = new SpringBootGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new SpringBootGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify Java code generation
        assertTrue(entity.contains("@Entity"));
        assertTrue(entity.contains("class User"));
        assertTrue(service.contains("@Service"));
        assertTrue(repository.contains("@Repository"));
        assertTrue(controller.contains("@RestController"));
        
        System.out.println("✅ Java Spring Boot Generator: PASSED");
    }
    
    @Test
    void testDjangoGenerator() {
        System.out.println("=== Testing Django Generator ===");
        
        var entityGen = new DjangoGeneratorFactory().createEntityGenerator();
        var serviceGen = new DjangoGeneratorFactory().createServiceGenerator();
        var repoGen = new DjangoGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new DjangoGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify Django code generation
        assertTrue(entity.contains("class User(models.Model)"));
        assertTrue(service.contains("class UserViewSet"));
        assertTrue(repository.contains("class UserSerializer"));
        assertTrue(controller.contains("router.register"));
        
        System.out.println("✅ Django Generator: PASSED");
    }
    
    @Test
    void testPythonFastAPIGenerator() {
        System.out.println("=== Testing Python FastAPI Generator ===");
        
        var entityGen = new PythonGeneratorFactory().createEntityGenerator();
        var serviceGen = new PythonGeneratorFactory().createServiceGenerator();
        var repoGen = new PythonGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new PythonGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify Python FastAPI code generation
        assertTrue(entity.contains("class User(BaseModel)"));
        assertTrue(service.contains("class UserService"));
        assertTrue(repository.contains("class UserRepository"));
        assertTrue(controller.contains("@router."));
        
        System.out.println("✅ Python FastAPI Generator: PASSED");
    }
    
    @Test
    void testCSharpGenerator() {
        System.out.println("=== Testing C# .NET Generator ===");
        
        var entityGen = new CSharpGeneratorFactory().createEntityGenerator();
        var serviceGen = new CSharpGeneratorFactory().createServiceGenerator();
        var repoGen = new CSharpGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new CSharpGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify C# code generation
        assertTrue(entity.contains("public class User"));
        assertTrue(service.contains("public interface IUserService"));
        assertTrue(repository.contains("public interface IUserRepository"));
        assertTrue(controller.contains("[ApiController]"));
        
        System.out.println("✅ C# .NET Generator: PASSED");
    }
    
    @Test
    void testTypeScriptGenerator() {
        System.out.println("=== Testing TypeScript Generator ===");
        
        var entityGen = new TypeScriptGeneratorFactory().createEntityGenerator();
        var serviceGen = new TypeScriptGeneratorFactory().createServiceGenerator();
        var repoGen = new TypeScriptGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new TypeScriptGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify TypeScript code generation
        assertTrue(entity.contains("export class User"));
        assertTrue(service.contains("export class UserService"));
        assertTrue(repository.contains("export class UserRepository"));
        assertTrue(controller.contains("export class UserController"));
        
        System.out.println("✅ TypeScript Generator: PASSED");
    }
    
    @Test
    void testPhpGenerator() {
        System.out.println("=== Testing PHP Generator ===");
        
        var entityGen = new PhpGeneratorFactory().createEntityGenerator();
        var serviceGen = new PhpGeneratorFactory().createServiceGenerator();
        var repoGen = new PhpGeneratorFactory().createRepositoryGenerator();
        var controllerGen = new PhpGeneratorFactory().createControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, packageName);
        String service = serviceGen.generateService(testClass, packageName);
        String repository = repoGen.generateRepository(testClass, packageName);
        String controller = controllerGen.generateController(testClass, packageName);
        
        // Verify PHP code generation
        assertTrue(entity.contains("class User"));
        assertTrue(service.contains("class UserService"));
        assertTrue(repository.contains("class UserRepository"));
        assertTrue(controller.contains("class UserController"));
        
        System.out.println("✅ PHP Generator: PASSED");
    }
    
    @Test
    void testAllGeneratorsIntegration() {
        System.out.println("=== Testing All Generators Integration ===");
        
        String[] languages = {"Java Spring Boot", "Django", "Python FastAPI", "C# .NET", "TypeScript", "PHP"};
        
        for (String language : languages) {
            try {
                switch (language) {
                    case "Java Spring Boot":
                        testJavaSpringBootGenerator();
                        break;
                    case "Django":
                        testDjangoGenerator();
                        break;
                    case "Python FastAPI":
                        testPythonFastAPIGenerator();
                        break;
                    case "C# .NET":
                        testCSharpGenerator();
                        break;
                    case "TypeScript":
                        testTypeScriptGenerator();
                        break;
                    case "PHP":
                        testPhpGenerator();
                        break;
                }
                System.out.println("✅ " + language + " integration: PASSED");
            } catch (Exception e) {
                System.out.println("❌ " + language + " integration: FAILED - " + e.getMessage());
                fail("Integration test failed for " + language);
            }
        }
        
        System.out.println("\n🎉 All generators tested successfully!");
    }
}
package com.basiccode.generator;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests simples pour vérifier que les générateurs principaux fonctionnent
 */
public class SimpleGeneratorTest {
    
    private ClassModel testClass;
    
    @BeforeEach
    void setUp() {
        testClass = new ClassModel();
        testClass.setName("User");
        
        List<Field> fields = new ArrayList<>();
        
        Field nameField = new Field("name", "String");
        fields.add(nameField);
        
        Field emailField = new Field("email", "String");
        fields.add(emailField);
        
        testClass.setFields(fields);
    }
    
    @Test
    void testSpringBootEntityGenerator() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        
        String result = generator.generateEntity(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("@Entity"));
        assertTrue(result.contains("class User"));
        assertTrue(result.contains("private String name"));
        assertTrue(result.contains("private String email"));
        
        System.out.println("✅ Entity Generator: " + result.length() + " chars generated");
    }
    
    @Test
    void testSpringBootRepositoryGenerator() {
        SpringBootRepositoryGenerator generator = new SpringBootRepositoryGenerator();
        
        String result = generator.generateRepository(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("interface UserRepository"));
        assertTrue(result.contains("JpaRepository"));
        
        System.out.println("✅ Repository Generator: " + result.length() + " chars generated");
    }
    
    @Test
    void testSpringBootServiceGenerator() {
        SpringBootServiceGenerator generator = new SpringBootServiceGenerator();
        
        String result = generator.generateService(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("class UserService"));
        assertTrue(result.contains("@Service"));
        
        System.out.println("✅ Service Generator: " + result.length() + " chars generated");
    }
    
    @Test
    void testSpringBootControllerGenerator() {
        SpringBootControllerGenerator generator = new SpringBootControllerGenerator();
        
        String result = generator.generateController(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("class UserController"));
        assertTrue(result.contains("@RestController"));
        assertTrue(result.contains("@GetMapping"));
        assertTrue(result.contains("@PostMapping"));
        
        System.out.println("✅ Controller Generator: " + result.length() + " chars generated");
    }
    
    @Test
    void testCompleteGeneration() {
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
        SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
        SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
        SpringBootMigrationGenerator migrationGen = new SpringBootMigrationGenerator();
        
        String entity = entityGen.generateEntity(testClass, "com.test");
        String repository = repoGen.generateRepository(testClass, "com.test");
        String service = serviceGen.generateService(testClass, "com.test");
        String controller = controllerGen.generateController(testClass, "com.test");
        String migration = migrationGen.generateMigration(testClass, "com.test");
        
        assertNotNull(entity);
        assertNotNull(repository);
        assertNotNull(service);
        assertNotNull(controller);
        assertNotNull(migration);
        
        int totalChars = entity.length() + repository.length() + service.length() + 
                        controller.length() + migration.length();
        
        System.out.println("✅ Complete Generation: 5 files, " + totalChars + " chars total");
        assertTrue(totalChars > 1000, "Generated code should be substantial");
    }
}
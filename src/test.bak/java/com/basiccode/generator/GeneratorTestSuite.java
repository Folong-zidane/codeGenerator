package com.basiccode.generator;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

/**
 * Suite de tests complète pour tous les générateurs
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GeneratorTestSuite {

    private List<EnhancedClass> testClasses;
    private String packageName = "com.test.generated";

    @BeforeEach
    void setUp() {
        testClasses = createTestData();
        log.info("Test data created with {} classes", testClasses.size());
    }

    @Test
    @Order(1)
    @DisplayName("Test Spring Boot Entity Generator")
    void testSpringBootEntityGenerator() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateEntity(enhancedClass, packageName);
            
            assertNotNull(result);
            assertTrue(result.contains("@Entity"));
            assertTrue(result.contains("@Table"));
            assertTrue(result.contains("@Id"));
            assertTrue(result.contains("@GeneratedValue"));
            assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Entity generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test Spring Boot Repository Generator")
    void testSpringBootRepositoryGenerator() {
        SpringBootRepositoryGenerator generator = new SpringBootRepositoryGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateRepository(enhancedClass, packageName);
            
            assertNotNull(result);
            assertTrue(result.contains("@Repository"));
            assertTrue(result.contains("extends JpaRepository"));
            assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Repository generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test Spring Boot Service Generator")
    void testSpringBootServiceGenerator() {
        SpringBootServiceGenerator generator = new SpringBootServiceGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateService(enhancedClass, packageName);
            
            assertNotNull(result);
            assertTrue(result.contains("@Service"));
            assertTrue(result.contains("@Transactional"));
            assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Service generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test Spring Boot Controller Generator")
    void testSpringBootControllerGenerator() {
        SpringBootControllerGenerator generator = new SpringBootControllerGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateController(enhancedClass, packageName);
            
            assertNotNull(result);
            assertTrue(result.contains("@RestController"));
            assertTrue(result.contains("@RequestMapping"));
            assertTrue(result.contains("@GetMapping"));
            assertTrue(result.contains("@PostMapping"));
            assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Controller generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test Spring Boot Migration Generator")
    void testSpringBootMigrationGenerator() {
        SpringBootMigrationGenerator generator = new SpringBootMigrationGenerator();
        
        String result = generator.generateMigration(testClasses, packageName);
        
        assertNotNull(result);
        assertTrue(result.contains("CREATE TABLE"));
        assertTrue(result.contains("Flyway Migration"));
        assertTrue(result.contains("utf8mb4"));
        
        log.info("✅ Migration generated: {} chars", result.length());
    }

    @Test
    @Order(6)
    @DisplayName("Test Spring Boot Configuration Generator")
    void testSpringBootConfigGenerator() {
        SpringBootConfigGenerator generator = new SpringBootConfigGenerator();
        
        String result = generator.generateConfiguration(testClasses, packageName);
        
        assertNotNull(result);
        assertTrue(result.contains("@Configuration"));
        assertTrue(result.contains("@EnableJpaRepositories"));
        assertTrue(result.contains("package " + packageName));
        
        log.info("✅ Configuration generated: {} chars", result.length());
    }

    @Test
    @Order(7)
    @DisplayName("Test Spring Boot Application Generator")
    void testSpringBootApplicationGenerator() {
        SpringBootApplicationGenerator generator = new SpringBootApplicationGenerator();
        
        String result = generator.generateApplication(testClasses, packageName);
        
        assertNotNull(result);
        assertTrue(result.contains("@SpringBootApplication"));
        assertTrue(result.contains("SpringApplication.run"));
        assertTrue(result.contains("package " + packageName));
        
        log.info("✅ Application generated: {} chars", result.length());
    }

    @Test
    @Order(8)
    @DisplayName("Test Complete Spring Boot Project Generation")
    void testCompleteProjectGeneration() {
        log.info("🚀 Testing complete Spring Boot project generation...");
        
        // Test all generators together
        Map<String, String> generatedFiles = new HashMap<>();
        
        // Entities
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        for (EnhancedClass clazz : testClasses) {
            String entity = entityGen.generateEntity(clazz, packageName);
            generatedFiles.put("entity_" + clazz.getOriginalClass().getName(), entity);
        }
        
        // Repositories
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
        for (EnhancedClass clazz : testClasses) {
            String repo = repoGen.generateRepository(clazz, packageName);
            generatedFiles.put("repository_" + clazz.getOriginalClass().getName(), repo);
        }
        
        // Services
        SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
        for (EnhancedClass clazz : testClasses) {
            String service = serviceGen.generateService(clazz, packageName);
            generatedFiles.put("service_" + clazz.getOriginalClass().getName(), service);
        }
        
        // Controllers
        SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
        for (EnhancedClass clazz : testClasses) {
            String controller = controllerGen.generateController(clazz, packageName);
            generatedFiles.put("controller_" + clazz.getOriginalClass().getName(), controller);
        }
        
        // Configuration
        SpringBootConfigGenerator configGen = new SpringBootConfigGenerator();
        String config = configGen.generateConfiguration(testClasses, packageName);
        generatedFiles.put("configuration", config);
        
        // Application
        SpringBootApplicationGenerator appGen = new SpringBootApplicationGenerator();
        String app = appGen.generateApplication(testClasses, packageName);
        generatedFiles.put("application", app);
        
        // Migration
        SpringBootMigrationGenerator migrationGen = new SpringBootMigrationGenerator();
        String migration = migrationGen.generateMigration(testClasses, packageName);
        generatedFiles.put("migration", migration);
        
        // Verify all files generated
        assertEquals(testClasses.size() * 4 + 3, generatedFiles.size());
        
        log.info("✅ Complete project generated: {} files", generatedFiles.size());
        
        // Log summary
        generatedFiles.forEach((name, content) -> {
            log.info("📄 {}: {} chars", name, content.length());
        });
    }

    private List<EnhancedClass> createTestData() {
        List<EnhancedClass> classes = new ArrayList<>();
        
        // User class
        UmlClass userClass = new UmlClass();
        userClass.setName("User");
        userClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("username", "String"),
            new UmlAttribute("email", "String"),
            new UmlAttribute("firstName", "String"),
            new UmlAttribute("lastName", "String"),
            new UmlAttribute("isActive", "Boolean")
        ));
        
        EnhancedClass enhancedUser = new EnhancedClass(userClass);
        enhancedUser.setStateful(true);
        classes.add(enhancedUser);
        
        // Product class
        UmlClass productClass = new UmlClass();
        productClass.setName("Product");
        productClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("name", "String"),
            new UmlAttribute("description", "String"),
            new UmlAttribute("price", "Double"),
            new UmlAttribute("stock", "Integer")
        ));
        
        EnhancedClass enhancedProduct = new EnhancedClass(productClass);
        enhancedProduct.setStateful(false);
        classes.add(enhancedProduct);
        
        // Order class
        UmlClass orderClass = new UmlClass();
        orderClass.setName("Order");
        orderClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("userId", "Long"),
            new UmlAttribute("total", "Double"),
            new UmlAttribute("orderDate", "LocalDateTime"),
            new UmlAttribute("status", "String")
        ));
        
        EnhancedClass enhancedOrder = new EnhancedClass(orderClass);
        enhancedOrder.setStateful(true);
        classes.add(enhancedOrder);
        
        return classes;
    }

    private void assertNotNull(String value) {
        Assertions.assertNotNull(value, "Generated content should not be null");
    }

    private void assertTrue(boolean condition) {
        Assertions.assertTrue(condition, "Condition should be true");
    }

    private void assertEquals(int expected, int actual) {
        Assertions.assertEquals(expected, actual, "Values should be equal");
    }
}
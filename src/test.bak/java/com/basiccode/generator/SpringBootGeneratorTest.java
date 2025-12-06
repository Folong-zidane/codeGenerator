package com.basiccode.generator;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * Test simple pour les générateurs Spring Boot principaux
 */
@SpringBootTest
@Slf4j
class SpringBootGeneratorTest {

    private List<EnhancedClass> testClasses;
    private String packageName = "com.test.generated";

    @BeforeEach
    void setUp() {
        testClasses = createSimpleTestData();
        log.info("Test data created with {} classes", testClasses.size());
    }

    @Test
    @DisplayName("Test Spring Boot Entity Generator")
    void testSpringBootEntityGenerator() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateEntity(enhancedClass, packageName);
            
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.contains("@Entity"));
            Assertions.assertTrue(result.contains("@Table"));
            Assertions.assertTrue(result.contains("@Id"));
            Assertions.assertTrue(result.contains("@GeneratedValue"));
            Assertions.assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Entity generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @DisplayName("Test Spring Boot Repository Generator")
    void testSpringBootRepositoryGenerator() {
        SpringBootRepositoryGenerator generator = new SpringBootRepositoryGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateRepository(enhancedClass, packageName);
            
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.contains("@Repository"));
            Assertions.assertTrue(result.contains("extends JpaRepository"));
            Assertions.assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Repository generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @DisplayName("Test Spring Boot Service Generator")
    void testSpringBootServiceGenerator() {
        SpringBootServiceGenerator generator = new SpringBootServiceGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateService(enhancedClass, packageName);
            
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.contains("@Service"));
            Assertions.assertTrue(result.contains("@Transactional"));
            Assertions.assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Service generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @DisplayName("Test Spring Boot Controller Generator")
    void testSpringBootControllerGenerator() {
        SpringBootControllerGenerator generator = new SpringBootControllerGenerator();
        
        for (EnhancedClass enhancedClass : testClasses) {
            String result = generator.generateController(enhancedClass, packageName);
            
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.contains("@RestController"));
            Assertions.assertTrue(result.contains("@RequestMapping"));
            Assertions.assertTrue(result.contains("@GetMapping"));
            Assertions.assertTrue(result.contains("@PostMapping"));
            Assertions.assertTrue(result.contains("package " + packageName));
            
            log.info("✅ Controller generated for {}: {} chars", 
                enhancedClass.getOriginalClass().getName(), result.length());
        }
    }

    @Test
    @DisplayName("Test Spring Boot Migration Generator")
    void testSpringBootMigrationGenerator() {
        SpringBootMigrationGenerator generator = new SpringBootMigrationGenerator();
        
        String result = generator.generateMigration(testClasses, packageName);
        
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("CREATE TABLE"));
        Assertions.assertTrue(result.contains("Flyway Migration"));
        Assertions.assertTrue(result.contains("utf8mb4"));
        
        log.info("✅ Migration generated: {} chars", result.length());
    }

    @Test
    @DisplayName("Test Spring Boot Configuration Generator")
    void testSpringBootConfigGenerator() {
        SpringBootConfigGenerator generator = new SpringBootConfigGenerator();
        
        String result = generator.generateConfiguration(testClasses, packageName);
        
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("@Configuration"));
        Assertions.assertTrue(result.contains("@EnableJpaRepositories"));
        Assertions.assertTrue(result.contains("package " + packageName));
        
        log.info("✅ Configuration generated: {} chars", result.length());
    }

    @Test
    @DisplayName("Test Spring Boot Application Generator")
    void testSpringBootApplicationGenerator() {
        SpringBootApplicationGenerator generator = new SpringBootApplicationGenerator();
        
        String result = generator.generateApplication(testClasses, packageName);
        
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("@SpringBootApplication"));
        Assertions.assertTrue(result.contains("SpringApplication.run"));
        Assertions.assertTrue(result.contains("package " + packageName));
        
        log.info("✅ Application generated: {} chars", result.length());
    }

    private List<EnhancedClass> createSimpleTestData() {
        List<EnhancedClass> classes = new ArrayList<>();
        
        // User class
        UmlClass userClass = new UmlClass();
        userClass.setName("User");
        userClass.setAttributes(Arrays.asList(
            new UmlAttribute("id", "Long"),
            new UmlAttribute("username", "String"),
            new UmlAttribute("email", "String"),
            new UmlAttribute("firstName", "String"),
            new UmlAttribute("lastName", "String")
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
        
        return classes;
    }
}
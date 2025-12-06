package com.basiccode.generator.spring;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests complets pour tous les générateurs Spring Boot
 * - 13 générateurs Spring couverts
 * - Tests unitaires et d'intégration
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringBootGeneratorsTest {

    private EnhancedClass testEntity;
    private String packageName = "com.ecommerce.generated";

    @BeforeEach
    void setUp() {
        testEntity = createTestEntity();
        log.info("Spring test data initialized");
    }

    // =============== CORE GENERATORS ===============

    @Test
    @Order(1)
    @DisplayName("Spring Boot Entity Generator: Generate JPA entity")
    void testSpringBootEntityGenerator() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        String result = generator.generateEntity(testEntity, packageName);

        assertNotNull(result, "Generated entity should not be null");
        assertTrue(result.contains("@Entity"), "Should have @Entity annotation");
        assertTrue(result.contains("@Table"), "Should have @Table annotation");
        assertTrue(result.contains("@Id"), "Should have @Id annotation");
        assertTrue(result.contains("@GeneratedValue"), "Should have @GeneratedValue");
        assertTrue(result.contains("private Long id;"), "Should have id field");

        log.info("✅ SpringBootEntityGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(2)
    @DisplayName("Spring Boot Repository Generator: Generate JPA repository")
    void testSpringBootRepositoryGenerator() {
        SpringBootRepositoryGenerator generator = new SpringBootRepositoryGenerator();
        String result = generator.generateRepository(testEntity, packageName);

        assertNotNull(result, "Generated repository should not be null");
        assertTrue(result.contains("interface"), "Should be interface");
        assertTrue(result.contains("JpaRepository"), "Should extend JpaRepository");
        assertTrue(result.contains("@Repository"), "Should have @Repository annotation");

        log.info("✅ SpringBootRepositoryGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(3)
    @DisplayName("Spring Boot Service Generator: Generate business logic")
    void testSpringBootServiceGenerator() {
        SpringBootServiceGenerator generator = new SpringBootServiceGenerator();
        String result = generator.generateService(testEntity, packageName);

        assertNotNull(result, "Generated service should not be null");
        assertTrue(result.contains("@Service"), "Should have @Service annotation");
        assertTrue(result.contains("public class"), "Should be public class");
        assertTrue(result.contains("Repository"), "Should use repository");
        assertTrue(result.contains("@Transactional"), "Should have @Transactional");

        log.info("✅ SpringBootServiceGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(4)
    @DisplayName("Spring Boot Controller Generator: Generate REST endpoints")
    void testSpringBootControllerGenerator() {
        SpringBootControllerGenerator generator = new SpringBootControllerGenerator();
        String result = generator.generateController(testEntity, packageName);

        assertNotNull(result, "Generated controller should not be null");
        assertTrue(result.contains("@RestController"), "Should have @RestController");
        assertTrue(result.contains("@RequestMapping"), "Should have @RequestMapping");
        assertTrue(result.contains("@GetMapping"), "Should have @GetMapping");
        assertTrue(result.contains("@PostMapping"), "Should have @PostMapping");

        log.info("✅ SpringBootControllerGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(5)
    @DisplayName("Spring Boot DTO Generator: Generate data transfer objects")
    void testSpringBootDtoGenerator() {
        SpringBootDtoGenerator generator = new SpringBootDtoGenerator();
        String result = generator.generateDTO(testEntity, packageName);

        assertNotNull(result, "Generated DTO should not be null");
        assertTrue(result.contains("@Data"), "Should have @Data annotation");
        assertTrue(result.contains("public class"), "Should be public class");
        assertTrue(result.contains("private"), "Should have private fields");

        log.info("✅ SpringBootDtoGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(6)
    @DisplayName("Spring Boot Migration Generator: Generate Flyway migrations")
    void testSpringBootMigrationGenerator() {
        SpringBootMigrationGenerator generator = new SpringBootMigrationGenerator();
        String result = generator.generateMigration(testEntity, "1", packageName);

        assertNotNull(result, "Generated migration should not be null");
        assertTrue(result.contains("CREATE TABLE"), "Should have CREATE TABLE");
        assertTrue(result.contains("id"), "Should have id column");
        assertTrue(result.contains("PRIMARY KEY"), "Should have primary key");

        log.info("✅ SpringBootMigrationGenerator: {} chars generated", result.length());
    }

    // =============== ADVANCED GENERATORS ===============

    @Test
    @Order(7)
    @DisplayName("Spring Boot Security Generator: Generate security config")
    void testSpringBootSecurityGenerator() {
        SpringBootSecurityGenerator generator = new SpringBootSecurityGenerator();
        String result = generator.generateSecurityConfig(packageName);

        assertNotNull(result, "Generated security config should not be null");
        assertTrue(result.contains("@Configuration"), "Should have @Configuration");
        assertTrue(result.contains("SecurityFilterChain"), "Should have SecurityFilterChain");
        assertTrue(result.contains("@Bean"), "Should have @Bean method");

        log.info("✅ SpringBootSecurityGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(8)
    @DisplayName("Spring Boot Exception Generator: Generate exception handling")
    void testSpringBootExceptionGenerator() {
        SpringBootExceptionGenerator generator = new SpringBootExceptionGenerator();
        String result = generator.generateExceptionHandler(packageName);

        assertNotNull(result, "Generated exception handler should not be null");
        assertTrue(result.contains("@ControllerAdvice"), "Should have @ControllerAdvice");
        assertTrue(result.contains("@ExceptionHandler"), "Should have @ExceptionHandler");
        assertTrue(result.contains("ResponseEntity"), "Should return ResponseEntity");

        log.info("✅ SpringBootExceptionGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(9)
    @DisplayName("Spring Boot Config Generator: Generate configuration classes")
    void testSpringBootConfigGenerator() {
        SpringBootConfigGenerator generator = new SpringBootConfigGenerator();
        String result = generator.generateConfig(packageName);

        assertNotNull(result, "Generated config should not be null");
        assertTrue(result.contains("@Configuration"), "Should have @Configuration");
        assertTrue(result.contains("@Bean"), "Should have @Bean methods");

        log.info("✅ SpringBootConfigGenerator: {} chars generated", result.length());
    }

    @Test
    @Order(10)
    @DisplayName("Spring Boot Reactive Entity Generator: Generate R2DBC entities")
    void testSpringBootReactiveEntityGenerator() {
        SpringBootReactiveEntityGenerator generator = new SpringBootReactiveEntityGenerator();
        String result = generator.generateEntity(testEntity, packageName);

        assertNotNull(result, "Generated reactive entity should not be null");
        assertTrue(result.contains("@Table"), "Should have @Table annotation");
        assertTrue(result.contains("@Data"), "Should have @Data annotation");
        assertTrue(result.contains("@Builder"), "Should have @Builder annotation");

        log.info("✅ SpringBootReactiveEntityGenerator: {} chars generated", result.length());
    }

    // =============== VALIDATION & QUALITY TESTS ===============

    @Test
    @Order(11)
    @DisplayName("Spring Entity Validation: Verify generated code quality")
    void testEntityValidation() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        String entity = generator.generateEntity(testEntity, packageName);

        // Check Java syntax validity
        assertTrue(entity.contains("package " + packageName + ";"), "Should have correct package");
        assertTrue(entity.contains("public class"), "Should be public class");
        assertTrue(entity.contains("private"), "Should have private fields");
        assertTrue(entity.contains("{") && entity.contains("}"), "Should have proper braces");

        // Check imports
        assertTrue(entity.contains("import javax.persistence") || entity.contains("import jakarta.persistence"),
                "Should have persistence imports");

        log.info("✅ Entity validation passed");
    }

    @Test
    @Order(12)
    @DisplayName("Spring Repository Validation: Verify repository interface")
    void testRepositoryValidation() {
        SpringBootRepositoryGenerator generator = new SpringBootRepositoryGenerator();
        String repository = generator.generateRepository(testEntity, packageName);

        // Check interface definition
        assertTrue(repository.contains("interface"), "Should be interface");
        assertTrue(repository.contains("JpaRepository"), "Should extend JpaRepository");

        // Check correct generic types
        assertTrue(repository.contains("<") && repository.contains(">"), "Should have generic types");

        log.info("✅ Repository validation passed");
    }

    @Test
    @Order(13)
    @DisplayName("Spring Service Validation: Verify service layer")
    void testServiceValidation() {
        SpringBootServiceGenerator generator = new SpringBootServiceGenerator();
        String service = generator.generateService(testEntity, packageName);

        assertTrue(service.contains("@Service"), "Should have @Service annotation");
        assertTrue(service.contains("@Transactional"), "Should have @Transactional");
        assertTrue(service.contains("private"), "Should have private repository");

        log.info("✅ Service validation passed");
    }

    @Test
    @Order(14)
    @DisplayName("Spring Controller Validation: Verify REST endpoints")
    void testControllerValidation() {
        SpringBootControllerGenerator generator = new SpringBootControllerGenerator();
        String controller = generator.generateController(testEntity, packageName);

        assertTrue(controller.contains("@RestController"), "Should have @RestController");
        assertTrue(controller.contains("@RequestMapping"), "Should have @RequestMapping");
        assertTrue(controller.contains("@GetMapping") || controller.contains("@PostMapping"),
                "Should have mapping annotations");

        log.info("✅ Controller validation passed");
    }

    // =============== INTEGRATION TESTS ===============

    @Test
    @Order(15)
    @DisplayName("Spring Generators Integration: Full CRUD workflow")
    void testSpringGeneratorsIntegration() {
        // Generate all layers
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
        SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
        SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();

        String entity = entityGen.generateEntity(testEntity, packageName);
        String repository = repoGen.generateRepository(testEntity, packageName);
        String service = serviceGen.generateService(testEntity, packageName);
        String controller = controllerGen.generateController(testEntity, packageName);

        // Verify all components exist
        assertNotNull(entity);
        assertNotNull(repository);
        assertNotNull(service);
        assertNotNull(controller);

        // Verify layering
        assertTrue(repository.contains("JpaRepository"), "Repository should use JPA");
        assertTrue(service.contains(testEntity.getOriginalClass().getName() + "Repository"),
                "Service should reference Repository");
        assertTrue(controller.contains(testEntity.getOriginalClass().getName() + "Service"),
                "Controller should reference Service");

        log.info("✅ Full integration test passed for Spring generators");
    }

    @Test
    @Order(16)
    @DisplayName("Spring Code Generation Performance: Benchmark generators")
    void testGeneratorPerformance() {
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            entityGen.generateEntity(testEntity, packageName);
            repoGen.generateRepository(testEntity, packageName);
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("✅ Generated 200 files in {} ms (~{} ms per file)",
                duration, duration / 200.0);

        assertTrue(duration < 5000, "Should generate 200 files in less than 5 seconds");
    }

    // =============== HELPER METHODS ===============

    private EnhancedClass createTestEntity() {
        UmlClass umlClass = new UmlClass("Product");

        UmlAttribute id = new UmlAttribute("id", "Long", true, false);
        UmlAttribute name = new UmlAttribute("name", "String", true, false);
        UmlAttribute description = new UmlAttribute("description", "String", false, false);
        UmlAttribute price = new UmlAttribute("price", "BigDecimal", true, false);
        UmlAttribute stock = new UmlAttribute("stock", "Integer", false, false);
        UmlAttribute active = new UmlAttribute("active", "Boolean", false, false);
        UmlAttribute createdAt = new UmlAttribute("createdAt", "LocalDateTime", false, false);
        UmlAttribute updatedAt = new UmlAttribute("updatedAt", "LocalDateTime", false, false);

        umlClass.addAttribute(id);
        umlClass.addAttribute(name);
        umlClass.addAttribute(description);
        umlClass.addAttribute(price);
        umlClass.addAttribute(stock);
        umlClass.addAttribute(active);
        umlClass.addAttribute(createdAt);
        umlClass.addAttribute(updatedAt);

        UmlMethod getName = new UmlMethod("getName", "String", false, false);
        UmlMethod getPrice = new UmlMethod("getPrice", "BigDecimal", false, false);
        umlClass.addMethod(getName);
        umlClass.addMethod(getPrice);

        EnhancedClass enhanced = new EnhancedClass(umlClass);
        enhanced.setStateful(false);

        return enhanced;
    }
}

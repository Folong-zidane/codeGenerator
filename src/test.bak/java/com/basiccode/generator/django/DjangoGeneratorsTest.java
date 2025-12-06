package com.basiccode.generator.django;

import com.basiccode.generator.generator.python.django.generators.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour les générateurs Django
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DjangoGeneratorsTest {

    private String moduleName = "ecommerce";
    private String appName = "products";

    @BeforeEach
    void setUp() {
        log.info("Django test initialized");
    }

    // =============== TESTS ===============

    @Test
    @Order(1)
    @DisplayName("Django - DjangoRelationshipEnhancedGenerator: Can instantiate generator")
    void testDjangoRelationshipEnhancedGenerator() {
        DjangoRelationshipEnhancedGenerator generator = new DjangoRelationshipEnhancedGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoRelationshipEnhancedGenerator: generator created successfully");
    }

    @Test
    @Order(2)
    @DisplayName("Django - DjangoAuthenticationJWTGenerator: Can instantiate generator")
    void testDjangoAuthenticationJWTGenerator() {
        DjangoAuthenticationJWTGenerator generator = new DjangoAuthenticationJWTGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoAuthenticationJWTGenerator: generator created successfully");
    }

    @Test
    @Order(3)
    @DisplayName("Django - DjangoFilteringPaginationGenerator: Can instantiate generator")
    void testDjangoFilteringPaginationGenerator() {
        DjangoFilteringPaginationGenerator generator = new DjangoFilteringPaginationGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoFilteringPaginationGenerator: generator created successfully");
    }

    @Test
    @Order(4)
    @DisplayName("Django - DjangoCachingRedisGenerator: Can instantiate generator")
    void testDjangoCachingRedisGenerator() {
        DjangoCachingRedisGenerator generator = new DjangoCachingRedisGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoCachingRedisGenerator: generator created successfully");
    }

    @Test
    @Order(5)
    @DisplayName("Django - DjangoWebSocketGenerator: Can instantiate generator")
    void testDjangoWebSocketGenerator() {
        DjangoWebSocketGenerator generator = new DjangoWebSocketGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoWebSocketGenerator: generator created successfully");
    }

    @Test
    @Order(6)
    @DisplayName("Django - DjangoEventSourcingGenerator: Can instantiate generator")
    void testDjangoEventSourcingGenerator() {
        DjangoEventSourcingGenerator generator = new DjangoEventSourcingGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoEventSourcingGenerator: generator created successfully");
    }

    @Test
    @Order(7)
    @DisplayName("Django - DjangoCQRSPatternGenerator: Can instantiate generator")
    void testDjangoCQRSPatternGenerator() {
        DjangoCQRSPatternGenerator generator = new DjangoCQRSPatternGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoCQRSPatternGenerator: generator created successfully");
    }

    @Test
    @Order(8)
    @DisplayName("Django - DjangoAdvancedFeaturesGenerator: Can instantiate generator")
    void testDjangoAdvancedFeaturesGenerator() {
        DjangoAdvancedFeaturesGenerator generator = new DjangoAdvancedFeaturesGenerator(moduleName, appName);
        assertNotNull(generator, "Generator should be created");
        log.info("✅ DjangoAdvancedFeaturesGenerator: generator created successfully");
    }

    // =============== INTEGRATION TESTS ===============

    @Test
    @Order(9)
    @DisplayName("Django Generators: All generators can be instantiated")
    void testAllDjangoGeneratorsInstantiation() {
        DjangoRelationshipEnhancedGenerator gen1 = new DjangoRelationshipEnhancedGenerator(moduleName, appName);
        DjangoAuthenticationJWTGenerator gen2 = new DjangoAuthenticationJWTGenerator(moduleName, appName);
        DjangoFilteringPaginationGenerator gen3 = new DjangoFilteringPaginationGenerator(moduleName, appName);
        DjangoCachingRedisGenerator gen4 = new DjangoCachingRedisGenerator(moduleName, appName);
        DjangoWebSocketGenerator gen5 = new DjangoWebSocketGenerator(moduleName, appName);
        DjangoEventSourcingGenerator gen6 = new DjangoEventSourcingGenerator(moduleName, appName);
        DjangoCQRSPatternGenerator gen7 = new DjangoCQRSPatternGenerator(moduleName, appName);
        DjangoAdvancedFeaturesGenerator gen8 = new DjangoAdvancedFeaturesGenerator(moduleName, appName);

        assertNotNull(gen1);
        assertNotNull(gen2);
        assertNotNull(gen3);
        assertNotNull(gen4);
        assertNotNull(gen5);
        assertNotNull(gen6);
        assertNotNull(gen7);
        assertNotNull(gen8);

        log.info("✅ All 8 Django generators instantiated successfully");
    }

}

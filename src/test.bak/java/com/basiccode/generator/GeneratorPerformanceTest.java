package com.basiccode.generator;

import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Tests de performance pour les générateurs
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class GeneratorPerformanceTest {

    private List<EnhancedClass> largeDataset;
    private String packageName = "com.test.performance";

    @BeforeEach
    void setUp() {
        largeDataset = createLargeDataset(50); // 50 classes
        log.info("Large dataset created with {} classes", largeDataset.size());
    }

    @Test
    @DisplayName("Performance Test - Entity Generation")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testEntityGenerationPerformance() {
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        
        long startTime = System.currentTimeMillis();
        
        for (EnhancedClass clazz : largeDataset) {
            String result = generator.generateEntity(clazz, packageName);
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.length() > 100);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        log.info("✅ Entity generation for {} classes: {}ms (avg: {}ms/class)", 
            largeDataset.size(), duration, duration / largeDataset.size());
        
        Assertions.assertTrue(duration < 10000, "Generation should complete within 10 seconds");
    }

    @Test
    @DisplayName("Performance Test - Complete Project Generation")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testCompleteProjectPerformance() {
        long startTime = System.currentTimeMillis();
        
        // Generate all components
        SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
        SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
        SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
        SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
        SpringBootConfigGenerator configGen = new SpringBootConfigGenerator();
        SpringBootApplicationGenerator appGen = new SpringBootApplicationGenerator();
        SpringBootMigrationGenerator migrationGen = new SpringBootMigrationGenerator();
        
        int totalFiles = 0;
        
        for (EnhancedClass clazz : largeDataset) {
            entityGen.generateEntity(clazz, packageName);
            repoGen.generateRepository(clazz, packageName);
            serviceGen.generateService(clazz, packageName);
            controllerGen.generateController(clazz, packageName);
            totalFiles += 4;
        }
        
        configGen.generateConfiguration(largeDataset, packageName);
        appGen.generateApplication(largeDataset, packageName);
        migrationGen.generateMigration(largeDataset, packageName);
        totalFiles += 3;
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        log.info("✅ Complete project generation: {} files in {}ms (avg: {}ms/file)", 
            totalFiles, duration, duration / totalFiles);
        
        Assertions.assertTrue(duration < 30000, "Complete generation should complete within 30 seconds");
    }

    @Test
    @DisplayName("Memory Usage Test")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection
        System.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
        List<String> results = new ArrayList<>();
        
        for (EnhancedClass clazz : largeDataset) {
            String result = generator.generateEntity(clazz, packageName);
            results.add(result);
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        
        log.info("✅ Memory usage for {} entities: {} bytes ({} MB)", 
            largeDataset.size(), memoryUsed, memoryUsed / (1024 * 1024));
        
        // Should use less than 100MB for 50 entities
        Assertions.assertTrue(memoryUsed < 100 * 1024 * 1024, 
            "Memory usage should be reasonable");
    }

    private List<EnhancedClass> createLargeDataset(int count) {
        List<EnhancedClass> classes = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            UmlClass umlClass = new UmlClass();
            umlClass.setName("Entity" + i);
            
            List<UmlAttribute> attributes = new ArrayList<>();
            attributes.add(new UmlAttribute("id", "Long"));
            attributes.add(new UmlAttribute("name", "String"));
            attributes.add(new UmlAttribute("description", "String"));
            attributes.add(new UmlAttribute("value" + i, "Double"));
            attributes.add(new UmlAttribute("count" + i, "Integer"));
            attributes.add(new UmlAttribute("isActive", "Boolean"));
            attributes.add(new UmlAttribute("createdDate", "LocalDateTime"));
            
            umlClass.setAttributes(attributes);
            
            EnhancedClass enhanced = new EnhancedClass(umlClass);
            enhanced.setStateful(i % 2 == 0); // Alternate stateful/stateless
            
            classes.add(enhanced);
        }
        
        return classes;
    }
}
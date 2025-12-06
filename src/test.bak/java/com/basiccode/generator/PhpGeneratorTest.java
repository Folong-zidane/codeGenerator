package com.basiccode.generator;

import com.basiccode.generator.generator.php.*;
import com.basiccode.generator.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests pour les générateurs PHP
 */
public class PhpGeneratorTest {
    
    private ClassModel testClass;
    
    @BeforeEach
    void setUp() {
        testClass = new ClassModel();
        testClass.setName("Order");
        
        List<Field> fields = new ArrayList<>();
        
        Field customerField = new Field("customerName", "String");
        fields.add(customerField);
        
        Field totalField = new Field("total", "Float");
        fields.add(totalField);
        
        testClass.setFields(fields);
    }
    
    @Test
    void testPhpEntityGenerator() {
        PhpEntityGenerator generator = new PhpEntityGenerator();
        
        String result = generator.generateEntity(testClass, "App\\Entity");
        
        assertNotNull(result);
        assertTrue(result.contains("<?php"));
        assertTrue(result.contains("class Order"));
        assertTrue(result.contains("$customerName"));
        assertTrue(result.contains("$total"));
        
        System.out.println("✅ PHP Entity: " + result.length() + " chars");
    }
    
    @Test
    void testPhpRepositoryGenerator() {
        PhpRepositoryGenerator generator = new PhpRepositoryGenerator();
        
        String result = generator.generateRepository(testClass, "App\\Repository");
        
        assertNotNull(result);
        assertTrue(result.contains("class OrderRepository"));
        assertTrue(result.contains("findAll"));
        assertTrue(result.contains("findById"));
        assertTrue(result.contains("save"));
        
        System.out.println("✅ PHP Repository: " + result.length() + " chars");
    }
    
    @Test
    void testPhpServiceGenerator() {
        PhpServiceGenerator generator = new PhpServiceGenerator();
        
        String result = generator.generateService(testClass, "App\\Service");
        
        assertNotNull(result);
        assertTrue(result.contains("class OrderService"));
        assertTrue(result.contains("getAll"));
        assertTrue(result.contains("create"));
        assertTrue(result.contains("update"));
        
        System.out.println("✅ PHP Service: " + result.length() + " chars");
    }
    
    @Test
    void testPhpControllerGenerator() {
        PhpControllerGenerator generator = new PhpControllerGenerator();
        
        String result = generator.generateController(testClass, "App\\Controller");
        
        assertNotNull(result);
        assertTrue(result.contains("class OrderController"));
        assertTrue(result.contains("index"));
        assertTrue(result.contains("show"));
        assertTrue(result.contains("store"));
        
        System.out.println("✅ PHP Controller: " + result.length() + " chars");
    }
    
    @Test
    void testCompletePhpGeneration() {
        PhpEntityGenerator entityGen = new PhpEntityGenerator();
        PhpRepositoryGenerator repoGen = new PhpRepositoryGenerator();
        PhpServiceGenerator serviceGen = new PhpServiceGenerator();
        PhpControllerGenerator controllerGen = new PhpControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, "App\\Entity");
        String repository = repoGen.generateRepository(testClass, "App\\Repository");
        String service = serviceGen.generateService(testClass, "App\\Service");
        String controller = controllerGen.generateController(testClass, "App\\Controller");
        
        assertNotNull(entity);
        assertNotNull(repository);
        assertNotNull(service);
        assertNotNull(controller);
        
        int totalChars = entity.length() + repository.length() + 
                        service.length() + controller.length();
        
        System.out.println("✅ Complete PHP: 4 files, " + totalChars + " chars");
        assertTrue(totalChars > 500, "Generated PHP code should be substantial");
    }
}
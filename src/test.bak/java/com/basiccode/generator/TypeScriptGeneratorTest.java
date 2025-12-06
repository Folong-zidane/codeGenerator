package com.basiccode.generator;

import com.basiccode.generator.generator.typescript.*;
import com.basiccode.generator.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests pour les générateurs TypeScript
 */
public class TypeScriptGeneratorTest {
    
    private EnhancedClass testClass;
    
    @BeforeEach
    void setUp() {
        UmlClass umlClass = new UmlClass();
        umlClass.setName("Product");
        
        List<UmlAttribute> attributes = new ArrayList<>();
        
        UmlAttribute nameAttr = new UmlAttribute();
        nameAttr.setName("name");
        nameAttr.setType("String");
        attributes.add(nameAttr);
        
        UmlAttribute priceAttr = new UmlAttribute();
        priceAttr.setName("price");
        priceAttr.setType("Float");
        attributes.add(priceAttr);
        
        umlClass.setAttributes(attributes);
        testClass = new EnhancedClass(umlClass);
    }
    
    @Test
    void testTypeScriptEntityGenerator() {
        TypeScriptEntityGenerator generator = new TypeScriptEntityGenerator();
        
        String result = generator.generateEntity(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("@Entity"));
        assertTrue(result.contains("export class Product"));
        assertTrue(result.contains("name: string"));
        assertTrue(result.contains("price: number"));
        
        System.out.println("✅ TypeScript Entity: " + result.length() + " chars");
    }
    
    @Test
    void testTypeScriptRepositoryGenerator() {
        TypeScriptRepositoryGenerator generator = new TypeScriptRepositoryGenerator();
        
        String result = generator.generateRepository(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("interface IProductRepository"));
        assertTrue(result.contains("class ProductRepository"));
        assertTrue(result.contains("findAll()"));
        assertTrue(result.contains("findById"));
        
        System.out.println("✅ TypeScript Repository: " + result.length() + " chars");
    }
    
    @Test
    void testTypeScriptServiceGenerator() {
        TypeScriptServiceGenerator generator = new TypeScriptServiceGenerator();
        
        String result = generator.generateService(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("interface IProductService"));
        assertTrue(result.contains("class ProductService"));
        assertTrue(result.contains("getAll()"));
        assertTrue(result.contains("create"));
        
        System.out.println("✅ TypeScript Service: " + result.length() + " chars");
    }
    
    @Test
    void testTypeScriptControllerGenerator() {
        TypeScriptControllerGenerator generator = new TypeScriptControllerGenerator();
        
        String result = generator.generateController(testClass, "com.test");
        
        assertNotNull(result);
        assertTrue(result.contains("class ProductController"));
        assertTrue(result.contains("Request, Response"));
        assertTrue(result.contains("getAll ="));
        assertTrue(result.contains("create ="));
        
        System.out.println("✅ TypeScript Controller: " + result.length() + " chars");
    }
    
    @Test
    void testCompleteTypeScriptGeneration() {
        TypeScriptEntityGenerator entityGen = new TypeScriptEntityGenerator();
        TypeScriptRepositoryGenerator repoGen = new TypeScriptRepositoryGenerator();
        TypeScriptServiceGenerator serviceGen = new TypeScriptServiceGenerator();
        TypeScriptControllerGenerator controllerGen = new TypeScriptControllerGenerator();
        
        String entity = entityGen.generateEntity(testClass, "com.test");
        String repository = repoGen.generateRepository(testClass, "com.test");
        String service = serviceGen.generateService(testClass, "com.test");
        String controller = controllerGen.generateController(testClass, "com.test");
        
        assertNotNull(entity);
        assertNotNull(repository);
        assertNotNull(service);
        assertNotNull(controller);
        
        int totalChars = entity.length() + repository.length() + 
                        service.length() + controller.length();
        
        System.out.println("✅ Complete TypeScript: 4 files, " + totalChars + " chars");
        assertTrue(totalChars > 500, "Generated TypeScript code should be substantial");
    }
}
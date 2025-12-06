package com.basiccode.generator;

import com.basiccode.generator.service.TripleDiagramCodeGeneratorService;
import com.basiccode.generator.model.ComprehensiveCodeResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test for the refactored architecture
 */
@SpringBootTest(classes = TestConfiguration.class)
public class RefactoredArchitectureTest {
    
    @Autowired
    private TripleDiagramCodeGeneratorService generatorService;
    
    @Test
    public void testRefactoredCodeGeneration() throws Exception {
        String classDiagram = """
            classDiagram
                class User {
                    +Long id
                    +String username
                    +String email
                }
            """;
        
        String sequenceDiagram = """
            sequenceDiagram
                Client->>UserService: createUser(userData)
                UserService->>UserRepository: save(user)
                UserRepository-->>UserService: User created
            """;
        
        String stateDiagram = """
            stateDiagram-v2
                [*] --> ACTIVE
                ACTIVE --> SUSPENDED : suspend()
                SUSPENDED --> ACTIVE : reactivate()
            """;
        
        ComprehensiveCodeResult result = generatorService.generateComprehensiveCode(
            classDiagram, sequenceDiagram, stateDiagram, 
            "com.example.refactored", "java"
        );
        
        // Verify that files were generated
        assert result != null;
        assert !result.getFiles().isEmpty();
        
        System.out.println("✅ Refactored architecture test passed!");
        System.out.println("Generated files: " + result.getFiles().keySet());
    }
}
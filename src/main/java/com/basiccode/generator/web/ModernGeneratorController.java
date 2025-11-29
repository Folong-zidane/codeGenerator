package com.basiccode.generator.web;

import com.basiccode.generator.config.Framework;
import com.basiccode.generator.initializer.InitializerRegistry;
import com.basiccode.generator.service.ModernProjectGenerationService;
import com.basiccode.generator.service.ModernProjectGenerationService.ModernProjectRequest;
import com.basiccode.generator.service.ModernProjectGenerationService.ModernProjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for modern project generation using native initializers
 */
@RestController
@RequestMapping("/api/modern")
@CrossOrigin(origins = "*")
public class ModernGeneratorController {
    
    private final ModernProjectGenerationService modernService;
    private final InitializerRegistry initializerRegistry;
    
    @Autowired
    public ModernGeneratorController(ModernProjectGenerationService modernService,
                                   InitializerRegistry initializerRegistry) {
        this.modernService = modernService;
        this.initializerRegistry = initializerRegistry;
    }
    
    /**
     * Generate a modern project using native initializers + UML
     */
    @PostMapping("/generate")
    public ResponseEntity<ModernProjectResult> generateModernProject(@RequestBody ModernGenerationRequest request) {
        try {
            ModernProjectRequest serviceRequest = new ModernProjectRequest(
                request.projectName(),
                request.packageName(),
                request.language(),
                request.classDiagram(),
                request.sequenceDiagram(),
                request.stateDiagram(),
                request.outputPath() != null ? request.outputPath() : "/tmp/generated",
                request.options() != null ? request.options() : new HashMap<>()
            );
            
            ModernProjectResult result = modernService.generateModernProject(serviceRequest);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ModernProjectResult(
                    null,
                    false,
                    "Error",
                    "Generation failed: " + e.getMessage()
                )
            );
        }
    }
    
    /**
     * Get status of all available initializers
     */
    @GetMapping("/initializers/status")
    public ResponseEntity<Map<String, Object>> getInitializerStatus() {
        Map<String, Object> status = new HashMap<>();
        
        Map<String, String> initializerStatus = initializerRegistry.getInitializerStatus();
        Map<String, String> availableFrameworks = initializerRegistry.getAvailableFrameworks();
        
        status.put("available", availableFrameworks);
        status.put("status", initializerStatus);
        status.put("totalFrameworks", Framework.values().length);
        status.put("availableCount", availableFrameworks.size());
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * Check if a specific framework initializer is available
     */
    @GetMapping("/initializers/{framework}/available")
    public ResponseEntity<Map<String, Object>> checkFrameworkAvailability(@PathVariable String framework) {
        try {
            Framework fw = Framework.valueOf(framework.toUpperCase());
            boolean available = initializerRegistry.isAvailable(fw);
            
            Map<String, Object> response = new HashMap<>();
            response.put("framework", fw.getName());
            response.put("available", available);
            response.put("language", fw.getLanguage());
            
            if (available) {
                response.put("message", "Native initializer available for " + fw.getName());
            } else {
                response.put("message", "Native initializer not available, will use fallback generation");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Unknown framework: " + framework);
            error.put("availableFrameworks", List.of(Framework.values()));
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get example request for testing
     */
    @GetMapping("/example")
    public ResponseEntity<ModernGenerationRequest> getExampleRequest() {
        ModernGenerationRequest example = new ModernGenerationRequest(
            "my-awesome-app",
            "com.example.awesome",
            "java",
            """
            classDiagram
                class User {
                    +Long id
                    +String username
                    +String email
                    +UserStatus status
                }
                class Order {
                    +Long id
                    +Long userId
                    +Float total
                    +OrderStatus status
                }
                User "1" --> "*" Order
            """,
            """
            sequenceDiagram
                Client->>UserController: POST /api/users
                UserController->>UserService: createUser(userData)
                UserService->>UserRepository: save(user)
                UserRepository-->>UserService: User created
                UserService-->>UserController: Success
                UserController-->>Client: 201 Created
            """,
            """
            stateDiagram-v2
                [*] --> INACTIVE
                INACTIVE --> ACTIVE : activate()
                ACTIVE --> SUSPENDED : suspend()
                SUSPENDED --> ACTIVE : reactivate()
                ACTIVE --> INACTIVE : deactivate()
            """,
            "/tmp/generated",
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0")
        );
        
        return ResponseEntity.ok(example);
    }
    
    /**
     * Request record for modern generation
     */
    public record ModernGenerationRequest(
        String projectName,
        String packageName,
        String language,
        String classDiagram,
        String sequenceDiagram,
        String stateDiagram,
        String outputPath,
        Map<String, String> options
    ) {}
}
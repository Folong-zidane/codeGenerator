package com.basiccode.generator.service;

import com.basiccode.generator.config.Framework;
import com.basiccode.generator.config.FrameworkRegistry;
import com.basiccode.generator.generator.LanguageGeneratorFactory;
import com.basiccode.generator.initializer.InitializerRegistry;
import com.basiccode.generator.initializer.ProjectInitializer;
import com.basiccode.generator.model.ComprehensiveCodeResult;
import com.basiccode.generator.model.ComprehensiveDiagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Modern project generation service that combines:
 * 1. Native framework initializers (Spring Initializr, django-admin, etc.)
 * 2. UML-based code generation
 * 
 * This approach ensures:
 * - Always up-to-date project structure
 * - Latest framework versions and best practices
 * - No obsolescence when frameworks evolve
 */
@Service
public class ModernProjectGenerationService {
    
    private final InitializerRegistry initializerRegistry;
    private final FrameworkRegistry frameworkRegistry;
    private final CodeGenerationOrchestrator orchestrator;
    private final TripleDiagramCodeGeneratorService diagramService;
    
    @Autowired
    public ModernProjectGenerationService(InitializerRegistry initializerRegistry,
                                        FrameworkRegistry frameworkRegistry,
                                        CodeGenerationOrchestrator orchestrator,
                                        TripleDiagramCodeGeneratorService diagramService) {
        this.initializerRegistry = initializerRegistry;
        this.frameworkRegistry = frameworkRegistry;
        this.orchestrator = orchestrator;
        this.diagramService = diagramService;
    }
    
    /**
     * Generate a complete modern project using native initializers + UML generation
     */
    public ModernProjectResult generateModernProject(ModernProjectRequest request) {
        try {
            // 1. Determine framework
            Framework framework = Framework.fromLanguage(request.language());
            
            // 2. Check if native initializer is available
            if (!initializerRegistry.isAvailable(framework)) {
                // Fallback to traditional generation
                return generateWithFallback(request, framework);
            }
            
            // 3. Initialize project with native tools
            ProjectInitializer initializer = initializerRegistry.getInitializer(framework.toString().toLowerCase());
            Path projectPath = initializer.initializeProject(
                request.projectName(),
                request.packageName()
            );
            
            // 4. Generate UML-based code
            ComprehensiveCodeResult codeResult = diagramService.generateComprehensiveCode(
                request.classDiagram(),
                request.sequenceDiagram(),
                request.stateDiagram(),
                request.packageName(),
                request.language()
            );
            
            // 5. Merge generated code into initialized project
            mergeCodeIntoProject(projectPath, codeResult, framework);
            
            // 6. Create project scripts and documentation
            createProjectScripts(projectPath, request.projectName(), framework);
            
            return new ModernProjectResult(
                projectPath.toString(),
                true, // Used native initializer
                framework.getName(),
                "Project successfully generated with " + framework.getName() + " initializer"
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate modern project: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fallback to traditional generation when native initializers are not available
     */
    private ModernProjectResult generateWithFallback(ModernProjectRequest request, Framework framework) {
        try {
            // Use existing generation logic
            ComprehensiveCodeResult result = diagramService.generateComprehensiveCode(
                request.classDiagram(),
                request.sequenceDiagram(),
                request.stateDiagram(),
                request.packageName(),
                request.language()
            );
        
            // Create basic project structure
            Path projectPath = createBasicProjectStructure(request, framework);
            
            // Write generated files
            writeGeneratedFiles(projectPath, result);
            
            return new ModernProjectResult(
                projectPath.toString(),
                false, // Did not use native initializer
                framework.getName(),
                "Project generated with fallback method (native tools not available)"
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate project with fallback method: " + e.getMessage(), e);
        }
    }
    
    /**
     * Merge UML-generated code into the initialized project
     */
    private void mergeCodeIntoProject(Path projectPath, ComprehensiveCodeResult codeResult, Framework framework) {
        // Implementation depends on framework structure
        switch (framework) {
            case SPRING_BOOT -> mergeSpringBootCode(projectPath, codeResult);
            case DJANGO -> mergeDjangoCode(projectPath, codeResult);
            case FASTAPI -> mergeFastAPICode(projectPath, codeResult);
            // Add other frameworks...
        }
    }
    
    private void mergeSpringBootCode(Path projectPath, ComprehensiveCodeResult codeResult) {
        // Find src/main/java directory and merge entities, services, etc.
        Path srcPath = projectPath.resolve("src/main/java");
        
        codeResult.getFiles().forEach((filePath, content) -> {
            try {
                Path targetFile = srcPath.resolve(filePath);
                targetFile.getParent().toFile().mkdirs();
                java.nio.file.Files.writeString(targetFile, content);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write file: " + filePath, e);
            }
        });
    }
    
    private void mergeDjangoCode(Path projectPath, ComprehensiveCodeResult codeResult) {
        // Merge into Django app structure
        // Implementation for Django-specific merging
    }
    
    private void mergeFastAPICode(Path projectPath, ComprehensiveCodeResult codeResult) {
        // Merge into FastAPI structure
        // Implementation for FastAPI-specific merging
    }
    
    private Path createBasicProjectStructure(ModernProjectRequest request, Framework framework) {
        // Fallback project structure creation
        Path projectPath = Paths.get(request.outputPath(), request.projectName());
        projectPath.toFile().mkdirs();
        return projectPath;
    }
    
    private void writeGeneratedFiles(Path projectPath, ComprehensiveCodeResult result) {
        result.getFiles().forEach((filePath, content) -> {
            try {
                Path targetFile = projectPath.resolve(filePath);
                targetFile.getParent().toFile().mkdirs();
                java.nio.file.Files.writeString(targetFile, content);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write file: " + filePath, e);
            }
        });
    }
    
    private void createProjectScripts(Path projectPath, String projectName, Framework framework) {
        // Create start scripts, README, etc.
        createStartScript(projectPath, framework);
        createReadme(projectPath, projectName, framework);
    }
    
    private void createStartScript(Path projectPath, Framework framework) {
        String script = switch (framework) {
            case SPRING_BOOT -> "#!/bin/bash\necho \"🚀 Starting Spring Boot...\"\n./mvnw spring-boot:run\n";
            case DJANGO -> "#!/bin/bash\necho \"🚀 Starting Django...\"\npython manage.py runserver\n";
            case FASTAPI -> "#!/bin/bash\necho \"🚀 Starting FastAPI...\"\nuvicorn main:app --reload\n";
            default -> "#!/bin/bash\necho \"🚀 Starting application...\"\n";
        };
        
        try {
            Path startScript = projectPath.resolve("start.sh");
            java.nio.file.Files.writeString(startScript, script);
            startScript.toFile().setExecutable(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create start script", e);
        }
    }
    
    private void createReadme(Path projectPath, String projectName, Framework framework) {
        String readme = String.format("""
            # 🚀 %s - %s
            
            Generated with modern %s initializer + UML code generation.
            
            ## 🚀 Quick Start
            ```bash
            ./start.sh
            ```
            
            ## 📁 Project Structure
            This project was initialized using native %s tools, ensuring:
            - ✅ Latest framework version
            - ✅ Best practices structure
            - ✅ Modern dependencies
            - ✅ UML-generated business logic
            
            ## 🔄 Development
            The project structure follows %s conventions and can be extended normally.
            """, projectName, framework.getName(), framework.getName(), framework.getName(), framework.getName());
        
        try {
            java.nio.file.Files.writeString(projectPath.resolve("README.md"), readme);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create README", e);
        }
    }
    
    /**
     * Request record for modern project generation
     */
    public record ModernProjectRequest(
        String projectName,
        String packageName,
        String language,
        String classDiagram,
        String sequenceDiagram,
        String stateDiagram,
        String outputPath,
        Map<String, String> options
    ) {
        public ModernProjectRequest {
            if (options == null) options = new HashMap<>();
        }
    }
    
    /**
     * Result record for modern project generation
     */
    public record ModernProjectResult(
        String projectPath,
        boolean usedNativeInitializer,
        String frameworkName,
        String message
    ) {}
}
package com.basiccode.generator.controller;

import com.basiccode.generator.service.MetadataAwareGenerationOrchestrator;
import com.basiccode.generator.util.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/metadata-aware")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MetadataAwareGenerationController {
    
    private final MetadataAwareGenerationOrchestrator orchestrator;
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateApplication(
            @RequestBody Map<String, Object> request) {
        
        try {
            String diagramContent = (String) request.get("diagramContent");
            String packageName = (String) request.getOrDefault("packageName", "com.generated.app");
            String language = (String) request.getOrDefault("language", "java");
            
            log.info("Generating metadata-aware application for package: {} in language: {}", packageName, language);
            
            Map<String, String> generatedFiles = orchestrator.generateCompleteApplication(diagramContent, packageName, language);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Application generated successfully with metadata awareness",
                "filesCount", generatedFiles.size(),
                "files", generatedFiles
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating metadata-aware application", e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Generation failed: " + e.getMessage(),
                "error", e.getClass().getSimpleName()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/generate/zip")
    public ResponseEntity<byte[]> generateApplicationZip(
            @RequestBody Map<String, Object> request) {
        
        try {
            String diagramContent = (String) request.get("diagramContent");
            String packageName = (String) request.getOrDefault("packageName", "com.generated.app");
            String projectName = (String) request.getOrDefault("projectName", "generated-app");
            String language = (String) request.getOrDefault("language", "java");
            
            log.info("Generating metadata-aware application ZIP for package: {} in language: {}", packageName, language);
            
            Map<String, String> generatedFiles = orchestrator.generateCompleteApplication(diagramContent, packageName, language);
            
            String zipName = "python".equalsIgnoreCase(language) || "django".equalsIgnoreCase(language) ? 
                projectName + "-django-ultra-pur.zip" : projectName + "-metadata-aware.zip";
            
            byte[] zipBytes = ZipUtils.createZip(generatedFiles);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", zipName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipBytes);
            
        } catch (Exception e) {
            log.error("Error generating metadata-aware application ZIP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "Metadata-Aware Code Generation",
            "features", Map.of(
                "metadataSupport", true,
                "springBootGeneration", true,
                "djangoUltraPurGeneration", true,
                "comprehensiveGeneration", true,
                "multiLanguageSupport", true
            )
        );
        return ResponseEntity.ok(health);
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDiagram(
            @RequestBody Map<String, Object> request) {
        
        try {
            String diagramContent = (String) request.get("diagramContent");
            
            // Basic validation
            boolean hasClasses = diagramContent.contains("class ");
            boolean hasMetadata = diagramContent.contains("%%{") || diagramContent.contains("note ");
            
            Map<String, Object> validation = Map.of(
                "valid", hasClasses,
                "hasClasses", hasClasses,
                "hasMetadata", hasMetadata,
                "message", hasClasses ? "Diagram is valid" : "No classes found in diagram"
            );
            
            return ResponseEntity.ok(validation);
            
        } catch (Exception e) {
            log.error("Error validating diagram", e);
            Map<String, Object> errorResponse = Map.of(
                "valid", false,
                "message", "Validation failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
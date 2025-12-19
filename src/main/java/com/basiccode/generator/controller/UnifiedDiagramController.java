package com.basiccode.generator.controller;

import com.basiccode.generator.service.ComprehensiveGenerationOrchestrator;
import com.basiccode.generator.service.MetadataAwareGenerationOrchestrator;
import com.basiccode.generator.util.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 🎯 Contrôleur unifié pour tous les types de diagrammes
 * Supporte: Class, Sequence, State, Activity, ER, Git diagrams
 */
@Slf4j
@RestController
@RequestMapping("/api/unified")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UnifiedDiagramController {
    
    private final ComprehensiveGenerationOrchestrator comprehensiveOrchestrator;
    private final MetadataAwareGenerationOrchestrator metadataOrchestrator;
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateFromDiagrams(
            @RequestBody UnifiedGenerationRequest request) {
        
        try {
            log.info("🚀 Unified generation - Language: {}, Diagrams: {}", 
                request.getLanguage(), request.getDiagramTypes());
            
            Map<String, String> generatedFiles;
            
            // Choisir l'orchestrateur approprié
            if (isMetadataAwareLanguage(request.getLanguage())) {
                String combinedDiagram = combineDiagrams(request);
                generatedFiles = metadataOrchestrator.generateCompleteApplication(
                    combinedDiagram, 
                    request.getPackageName(), 
                    request.getLanguage()
                );
            } else {
                generatedFiles = comprehensiveOrchestrator.generateComprehensiveCode(
                    request.getClassDiagramContent(),
                    request.getSequenceDiagramContent(),
                    request.getStateDiagramContent(),
                    request.getLanguage(),
                    request.getPackageName()
                );
            }
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Project generated successfully from " + request.getDiagramTypes().size() + " diagram(s)",
                "filesCount", generatedFiles.size(),
                "diagramTypes", request.getDiagramTypes(),
                "files", generatedFiles
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Unified generation failed", e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Generation failed: " + e.getMessage(),
                "error", e.getClass().getSimpleName()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/generate/zip")
    public ResponseEntity<byte[]> generateZipFromDiagrams(
            @RequestBody UnifiedGenerationRequest request) {
        
        try {
            log.info("🚀 Unified ZIP generation - Language: {}, Project: {}", 
                request.getLanguage(), request.getProjectName());
            
            Map<String, String> generatedFiles;
            
            if (isMetadataAwareLanguage(request.getLanguage())) {
                String combinedDiagram = combineDiagrams(request);
                generatedFiles = metadataOrchestrator.generateCompleteApplication(
                    combinedDiagram, 
                    request.getPackageName(), 
                    request.getLanguage()
                );
            } else {
                generatedFiles = comprehensiveOrchestrator.generateComprehensiveCode(
                    request.getClassDiagramContent(),
                    request.getSequenceDiagramContent(),
                    request.getStateDiagramContent(),
                    request.getLanguage(),
                    request.getPackageName()
                );
            }
            
            String zipName = generateZipName(request);
            byte[] zipBytes = ZipUtils.createZip(generatedFiles);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", zipName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipBytes);
            
        } catch (Exception e) {
            log.error("❌ Unified ZIP generation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDiagrams(
            @RequestBody UnifiedGenerationRequest request) {
        
        try {
            Map<String, Object> validation = new HashMap<>();
            validation.put("valid", true);
            validation.put("diagramTypes", request.getDiagramTypes());
            
            // Validation par type de diagramme
            Map<String, Object> diagramValidation = new HashMap<>();
            
            if (request.getClassDiagramContent() != null) {
                boolean hasClasses = request.getClassDiagramContent().contains("class ");
                diagramValidation.put("classDiagram", Map.of(
                    "valid", hasClasses,
                    "entities", countMatches(request.getClassDiagramContent(), "class \\w+")
                ));
            }
            
            if (request.getSequenceDiagramContent() != null) {
                boolean hasParticipants = request.getSequenceDiagramContent().contains("participant");
                diagramValidation.put("sequenceDiagram", Map.of(
                    "valid", hasParticipants,
                    "participants", countMatches(request.getSequenceDiagramContent(), "participant \\w+")
                ));
            }
            
            if (request.getStateDiagramContent() != null) {
                boolean hasStates = request.getStateDiagramContent().contains("state") || 
                                   request.getStateDiagramContent().contains("[*]");
                diagramValidation.put("stateDiagram", Map.of(
                    "valid", hasStates,
                    "states", countMatches(request.getStateDiagramContent(), "state \\w+")
                ));
            }
            
            if (request.getActivityDiagramContent() != null) {
                boolean hasActivities = request.getActivityDiagramContent().contains("-->") ||
                                       request.getActivityDiagramContent().contains("flowchart");
                diagramValidation.put("activityDiagram", Map.of(
                    "valid", hasActivities,
                    "activities", countMatches(request.getActivityDiagramContent(), "\\w+\\[.*?\\]")
                ));
            }
            
            validation.put("diagrams", diagramValidation);
            validation.put("message", "All diagrams validated successfully");
            
            return ResponseEntity.ok(validation);
            
        } catch (Exception e) {
            log.error("❌ Diagram validation failed", e);
            Map<String, Object> errorResponse = Map.of(
                "valid", false,
                "message", "Validation failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "Unified Diagram Code Generation",
            "supportedDiagrams", Map.of(
                "classDiagram", "Entity and relationship modeling",
                "sequenceDiagram", "Interaction and behavior modeling",
                "stateDiagram", "State machine modeling",
                "activityDiagram", "Process flow modeling",
                "erDiagram", "Database entity relationship",
                "gitDiagram", "Version control flow"
            ),
            "supportedLanguages", Map.of(
                "java", "Spring Boot with JPA",
                "python", "FastAPI with SQLAlchemy",
                "django", "Django REST Framework",
                "csharp", ".NET Core with Entity Framework",
                "typescript", "Express with TypeORM",
                "php", "Slim Framework with Eloquent"
            )
        );
        return ResponseEntity.ok(health);
    }
    
    private boolean isMetadataAwareLanguage(String language) {
        return language != null && 
               ("java".equalsIgnoreCase(language) || 
                "python".equalsIgnoreCase(language) || 
                "django".equalsIgnoreCase(language));
    }
    
    private String combineDiagrams(UnifiedGenerationRequest request) {
        StringBuilder combined = new StringBuilder();
        
        if (request.getClassDiagramContent() != null) {
            combined.append(request.getClassDiagramContent()).append("\n\n");
        }
        if (request.getSequenceDiagramContent() != null) {
            combined.append(request.getSequenceDiagramContent()).append("\n\n");
        }
        if (request.getStateDiagramContent() != null) {
            combined.append(request.getStateDiagramContent()).append("\n\n");
        }
        if (request.getActivityDiagramContent() != null) {
            combined.append(request.getActivityDiagramContent()).append("\n\n");
        }
        if (request.getErDiagramContent() != null) {
            combined.append(request.getErDiagramContent()).append("\n\n");
        }
        if (request.getGitDiagramContent() != null) {
            combined.append(request.getGitDiagramContent()).append("\n\n");
        }
        
        return combined.toString().trim();
    }
    
    private String generateZipName(UnifiedGenerationRequest request) {
        String projectName = request.getProjectName() != null ? 
            request.getProjectName() : "generated-project";
        String language = request.getLanguage();
        
        if ("python".equalsIgnoreCase(language) || "django".equalsIgnoreCase(language)) {
            return projectName + "-django-unified.zip";
        } else if ("java".equalsIgnoreCase(language)) {
            return projectName + "-spring-unified.zip";
        } else {
            return projectName + "-" + language + "-unified.zip";
        }
    }
    
    private int countMatches(String content, String regex) {
        if (content == null) return 0;
        return content.split(regex, -1).length - 1;
    }
    
    // DTO pour les requêtes unifiées
    public static class UnifiedGenerationRequest {
        private String classDiagramContent;
        private String sequenceDiagramContent;
        private String stateDiagramContent;
        private String activityDiagramContent;
        private String erDiagramContent;
        private String gitDiagramContent;
        private String language;
        private String packageName;
        private String projectName;
        
        // Getters et Setters
        public String getClassDiagramContent() { return classDiagramContent; }
        public void setClassDiagramContent(String classDiagramContent) { this.classDiagramContent = classDiagramContent; }
        
        public String getSequenceDiagramContent() { return sequenceDiagramContent; }
        public void setSequenceDiagramContent(String sequenceDiagramContent) { this.sequenceDiagramContent = sequenceDiagramContent; }
        
        public String getStateDiagramContent() { return stateDiagramContent; }
        public void setStateDiagramContent(String stateDiagramContent) { this.stateDiagramContent = stateDiagramContent; }
        
        public String getActivityDiagramContent() { return activityDiagramContent; }
        public void setActivityDiagramContent(String activityDiagramContent) { this.activityDiagramContent = activityDiagramContent; }
        
        public String getErDiagramContent() { return erDiagramContent; }
        public void setErDiagramContent(String erDiagramContent) { this.erDiagramContent = erDiagramContent; }
        
        public String getGitDiagramContent() { return gitDiagramContent; }
        public void setGitDiagramContent(String gitDiagramContent) { this.gitDiagramContent = gitDiagramContent; }
        
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        
        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
        
        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        
        public java.util.List<String> getDiagramTypes() {
            java.util.List<String> types = new java.util.ArrayList<>();
            if (classDiagramContent != null && !classDiagramContent.trim().isEmpty()) types.add("class");
            if (sequenceDiagramContent != null && !sequenceDiagramContent.trim().isEmpty()) types.add("sequence");
            if (stateDiagramContent != null && !stateDiagramContent.trim().isEmpty()) types.add("state");
            if (activityDiagramContent != null && !activityDiagramContent.trim().isEmpty()) types.add("activity");
            if (erDiagramContent != null && !erDiagramContent.trim().isEmpty()) types.add("er");
            if (gitDiagramContent != null && !gitDiagramContent.trim().isEmpty()) types.add("git");
            return types;
        }
    }
}
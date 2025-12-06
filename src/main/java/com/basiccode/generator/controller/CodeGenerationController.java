package com.basiccode.generator.controller;

import com.basiccode.generator.dto.GenerationRequest;
import com.basiccode.generator.dto.GenerationResponse;
import com.basiccode.generator.service.CodeGenerationService;
import com.basiccode.generator.util.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

/**
 * REST API Controller for Code Generation Quality Testing
 * 
 * Endpoints:
 * - POST /api/v1/generate/{language}  : Generate code for a specific language
 * - GET /api/v1/generated/{generationId} : Retrieve generated code
 * - GET /api/v1/quality-report/{generationId} : Get quality analysis report
 */
@RestController
@RequestMapping("/api/v1/generate")
@RequiredArgsConstructor
@Slf4j
public class CodeGenerationController {
    
    private final CodeGenerationService generationService;
    
    /**
     * Generate code for a specific language based on UML diagrams
     * 
     * @param language Programming language (java, python, csharp, typescript, php)
     * @param request Generation request with project metadata
     * @return Generated code location and metadata
     */
    @PostMapping("/{language}")
    public ResponseEntity<GenerationResponse> generateCode(
            @PathVariable String language,
            @RequestBody GenerationRequest request) {
        
        try {
            log.info("📝 Generating {} code for project: {}", language, request.getProjectName());
            
            // Validate language
            if (!isValidLanguage(language)) {
                return ResponseEntity.badRequest().build();
            }
            
            // Generate code based on language
            var result = generationService.generateForLanguage(
                request.getDiagramContent() != null ? request.getDiagramContent() : "",
                language,
                request.getPackageName()
            );
            
            // Convert to response format
            GenerationResponse response = GenerationResponse.builder()
                .generationId(java.util.UUID.randomUUID().toString())
                .language(language)
                .projectName(request.getProjectName())
                .outputPath("generated/" + request.getProjectName())
                .generatedFiles(result.getFiles().keySet().stream().toList())
                .status(GenerationResponse.GenerationStatus.SUCCESS)
                .generatedAt(java.time.Instant.now().toEpochMilli())
                .build();
            
            log.info("✅ Successfully generated {} code", language);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error generating {} code: {}", language, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Generate and download code as ZIP file
     */
    @PostMapping("/{language}/download")
    public ResponseEntity<byte[]> generateAndDownloadCode(
            @PathVariable String language,
            @RequestBody GenerationRequest request) {
        
        try {
            log.info("📦 Generating {} code ZIP for project: {}", language, request.getProjectName());
            
            if (!isValidLanguage(language)) {
                return ResponseEntity.badRequest().build();
            }
            
            // Generate code
            var result = generationService.generateForLanguage(
                request.getDiagramContent() != null ? request.getDiagramContent() : "",
                language,
                request.getPackageName()
            );
            
            // Create ZIP from generated files
            byte[] zipBytes = ZipUtils.createZipFromFiles(result.getFiles());
            
            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                request.getProjectName() + "-" + language + ".zip");
            
            log.info("✅ Successfully generated {} ZIP ({} bytes)", language, zipBytes.length);
            return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
            
        } catch (Exception e) {
            log.error("❌ Error generating {} ZIP: {}", language, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Validate language is supported
     */
    private boolean isValidLanguage(String language) {
        return language.toLowerCase().matches("java|python|csharp|typescript|php");
    }
}

package com.basiccode.generator.web;

import com.basiccode.generator.service.TripleDiagramCodeGeneratorService;
import com.basiccode.generator.model.ComprehensiveCodeResult;
import com.basiccode.generator.exception.GenerationException;
import com.basiccode.generator.initializer.InitializerService;
import com.basiccode.generator.initializer.VersionChecker;
import com.basiccode.generator.util.ZipUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
public class CodeGeneratorController {
    
    private final TripleDiagramCodeGeneratorService generatorService;
    private final InitializerService initializerService;
    private final VersionChecker versionChecker;
    
    @PostMapping("/comprehensive")
    public ResponseEntity<byte[]> generateCode(@RequestBody GenerationRequest request) throws GenerationException {
        validateRequest(request);
        
        try {
            ComprehensiveCodeResult result = generatorService.generateComprehensiveCode(
                request.classDiagram(),
                request.sequenceDiagram(), 
                request.stateDiagram(),
                request.packageName(),
                request.language()
            );
            
            // Create proper ZIP file
            byte[] zipBytes = ZipUtils.createZipFromFiles(result.getFiles());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                request.packageName().replace(".", "-") + "-" + request.language() + ".zip");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
                
        } catch (IOException e) {
            throw new GenerationException("Failed to create ZIP file", e);
        }
    }
    
    private void validateRequest(GenerationRequest request) {
        if (request.classDiagram() == null || request.classDiagram().trim().isEmpty()) {
            throw new IllegalArgumentException("Class diagram is required");
        }
        if (request.packageName() == null || request.packageName().trim().isEmpty()) {
            throw new IllegalArgumentException("Package name is required");
        }
        if (request.language() == null || request.language().trim().isEmpty()) {
            throw new IllegalArgumentException("Language is required");
        }
    }
    
    /**
     * Get supported languages with their latest versions
     */
    @GetMapping("/languages")
    public Map<String, String> getSupportedLanguages() {
        return initializerService.getSupportedLanguages();
    }
    
    /**
     * Get latest framework versions
     */
    @GetMapping("/versions")
    public Map<String, String> getLatestVersions() {
        return versionChecker.getAllLatestVersions();
    }
    
    public record GenerationRequest(
        String classDiagram,
        String sequenceDiagram,
        String stateDiagram,
        String packageName,
        String language
    ) {}
}
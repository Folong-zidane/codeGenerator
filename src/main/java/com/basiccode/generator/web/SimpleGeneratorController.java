package com.basiccode.generator.web;

import com.basiccode.generator.service.ComprehensiveGenerationOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 🎯 Contrôleur simple pour la génération de code
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SimpleGeneratorController {
    
    private final ComprehensiveGenerationOrchestrator orchestrator;
    
    @PostMapping("/generate/crud")
    public ResponseEntity<byte[]> generateCrud(@RequestBody GenerationRequest request) {
        log.info("🚀 CRUD generation - Language: {}", request.getLanguage());
        
        try {
            Map<String, String> files = orchestrator.generateComprehensiveCode(
                request.getUmlContent(), "", "", 
                request.getLanguage(), 
                request.getPackageName()
            );
            
            byte[] zipBytes = createZip(files);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated-crud.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
                
        } catch (Exception e) {
            log.error("❌ Generation failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/behavioral/generate")
    public ResponseEntity<byte[]> generateBehavioral(@RequestBody BehavioralRequest request) {
        log.info("🚀 Behavioral generation - Language: {}", request.getLanguage());
        
        try {
            Map<String, String> files = orchestrator.generateComprehensiveCode(
                request.getClassDiagramContent(),
                request.getSequenceDiagramContent(), 
                "",
                request.getLanguage(), 
                request.getPackageName()
            );
            
            byte[] zipBytes = createZip(files);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated-behavioral.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
                
        } catch (Exception e) {
            log.error("❌ Behavioral generation failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/comprehensive/generate")
    public ResponseEntity<byte[]> generateComprehensive(@RequestBody ComprehensiveRequest request) {
        log.info("🚀 Comprehensive generation - Language: {}", request.getLanguage());
        
        try {
            Map<String, String> files = orchestrator.generateComprehensiveCode(
                request.getClassDiagramContent(),
                request.getSequenceDiagramContent(),
                request.getStateDiagramContent(),
                request.getLanguage(), 
                request.getPackageName()
            );
            
            byte[] zipBytes = createZip(files);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated-comprehensive.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
                
        } catch (Exception e) {
            log.error("❌ Comprehensive generation failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/actuator/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"code-generator\"}");
    }
    
    private byte[] createZip(Map<String, String> files) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Map.Entry<String, String> entry : files.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue().getBytes("UTF-8"));
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }
    
    // DTOs
    public static class GenerationRequest {
        private String umlContent;
        private String language;
        private String packageName;
        
        public String getUmlContent() { return umlContent; }
        public void setUmlContent(String umlContent) { this.umlContent = umlContent; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
    }
    
    public static class BehavioralRequest {
        private String classDiagramContent;
        private String sequenceDiagramContent;
        private String language;
        private String packageName;
        
        public String getClassDiagramContent() { return classDiagramContent; }
        public void setClassDiagramContent(String classDiagramContent) { this.classDiagramContent = classDiagramContent; }
        public String getSequenceDiagramContent() { return sequenceDiagramContent; }
        public void setSequenceDiagramContent(String sequenceDiagramContent) { this.sequenceDiagramContent = sequenceDiagramContent; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
    }
    
    public static class ComprehensiveRequest {
        private String classDiagramContent;
        private String sequenceDiagramContent;
        private String stateDiagramContent;
        private String language;
        private String packageName;
        
        public String getClassDiagramContent() { return classDiagramContent; }
        public void setClassDiagramContent(String classDiagramContent) { this.classDiagramContent = classDiagramContent; }
        public String getSequenceDiagramContent() { return sequenceDiagramContent; }
        public void setSequenceDiagramContent(String sequenceDiagramContent) { this.sequenceDiagramContent = sequenceDiagramContent; }
        public String getStateDiagramContent() { return stateDiagramContent; }
        public void setStateDiagramContent(String stateDiagramContent) { this.stateDiagramContent = stateDiagramContent; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
    }
}
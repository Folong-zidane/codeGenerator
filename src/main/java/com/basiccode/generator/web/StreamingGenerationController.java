package com.basiccode.generator.web;

import com.basiccode.generator.service.TripleDiagramCodeGeneratorService;
import com.basiccode.generator.model.ComprehensiveCodeResult;
import com.basiccode.generator.util.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/v2/stream")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class StreamingGenerationController {
    
    private final TripleDiagramCodeGeneratorService generatorService;
    private final Map<String, ComprehensiveCodeResult> generationCache = new ConcurrentHashMap<>();
    
    /**
     * Initier génération (non-blocking)
     */
    @PostMapping("/generate")
    public ResponseEntity<StreamResponse> initiateGeneration(@RequestBody GenerationRequest request) {
        String generationId = UUID.randomUUID().toString();
        
        log.info("🚀 Initiating generation: {}", generationId);
        
        // Génération async
        new Thread(() -> {
            try {
                ComprehensiveCodeResult result = generatorService.generateComprehensiveCode(
                    request.classDiagram(),
                    request.sequenceDiagram(),
                    request.stateDiagram(),
                    request.packageName(),
                    request.language()
                );
                
                generationCache.put(generationId, result);
                log.info("✅ Generation complete: {} ({} files)", generationId, result.getFiles().size());
                
            } catch (Exception e) {
                log.error("❌ Generation failed: {}", generationId, e);
            }
        }).start();
        
        return ResponseEntity.accepted()
            .body(new StreamResponse(
                generationId,
                "/api/v2/stream/download/" + generationId,
                "PROCESSING"
            ));
    }
    
    /**
     * Télécharger par chunks avec streaming
     */
    @GetMapping("/download/{generationId}")
    public ResponseEntity<byte[]> downloadGeneration(@PathVariable String generationId) {
        
        ComprehensiveCodeResult result = generationCache.get(generationId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        
        log.info("📥 Downloading: {}", generationId);
        
        try {
            // Créer ZIP en mémoire
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            
            int fileCount = 0;
            for (var entry : result.getFiles().entrySet()) {
                String filePath = entry.getKey();
                String content = entry.getValue();
                
                ZipEntry zipEntry = new ZipEntry(filePath);
                zos.putNextEntry(zipEntry);
                zos.write(content.getBytes());
                zos.closeEntry();
                fileCount++;
            }
            
            zos.finish();
            zos.close();
            
            byte[] zipBytes = baos.toByteArray();
            log.info("✅ ZIP created: {} files, {} bytes", fileCount, zipBytes.length);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Content-Disposition", "attachment; filename=\"" + generationId + ".zip\"");
            headers.setContentLength(zipBytes.length);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
                
        } catch (IOException e) {
            log.error("❌ ZIP creation error", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Vérifier statut génération
     */
    @GetMapping("/status/{generationId}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable String generationId) {
        
        ComprehensiveCodeResult result = generationCache.get(generationId);
        
        if (result != null) {
            return ResponseEntity.ok(new StatusResponse(
                generationId,
                "COMPLETED",
                result.getFiles().size(),
                "Generation ready for download"
            ));
        }
        
        return ResponseEntity.ok(new StatusResponse(
            generationId,
            "PROCESSING",
            0,
            "Generation in progress..."
        ));
    }
    
    /**
     * Nettoyer cache (cleanup)
     */
    @DeleteMapping("/cleanup/{generationId}")
    public ResponseEntity<Void> cleanup(@PathVariable String generationId) {
        generationCache.remove(generationId);
        log.info("🗑️ Cleaned up generation: {}", generationId);
        return ResponseEntity.ok().build();
    }
    
    // Records
    public record GenerationRequest(
        String classDiagram,
        String sequenceDiagram,
        String stateDiagram,
        String packageName,
        String language
    ) {}
    
    public record StreamResponse(
        String generationId,
        String downloadUrl,
        String status
    ) {}
    
    public record StatusResponse(
        String generationId,
        String status,
        int fileCount,
        String message
    ) {}
}
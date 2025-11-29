package com.basiccode.generator.strategy;

import com.basiccode.generator.service.TripleDiagramCodeGeneratorService;
import com.basiccode.generator.web.UnifiedGenerationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

/**
 * Stratégie pour génération CRUD basique (classe seule)
 */
@Component
public class BasicGenerationStrategy implements CodeGenerationStrategy {
    
    @Autowired
    private TripleDiagramCodeGeneratorService tripleService;
    
    @Override
    public byte[] generateCode(UnifiedGenerationRequest request) throws Exception {
        var result = tripleService.generateComprehensiveCode(
            request.classDiagramContent(),
            "", "", // Pas de séquence ni état
            request.packageName(),
            request.language()
        );
        
        return createZip(result.getFiles(), "basic");
    }
    
    @Override
    public DiagramDetector.GenerationStrategy getStrategyType() {
        return DiagramDetector.GenerationStrategy.BASIC;
    }
    
    @Override
    public boolean canHandle(DiagramDetector.DiagramSignature signature) {
        return signature.hasClass() && !signature.hasSequence();
    }
    
    private byte[] createZip(java.util.Map<String, String> files, String type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (var entry : files.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue().getBytes());
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }
}
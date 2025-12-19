package com.basiccode.generator.controller;

import com.basiccode.generator.model.metadata.UMLMetadata;
import com.basiccode.generator.parser.metadata.UMLMetadataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import lombok.Builder;

@RestController
@RequestMapping("/api/metadata")
@CrossOrigin(origins = "*")
public class MetadataTestController {
    
    @Autowired
    private UMLMetadataParser metadataParser;
    
    @PostMapping("/parse")
    public ResponseEntity<UMLMetadata> parseMetadata(@RequestBody String diagramContent) {
        try {
            UMLMetadata metadata = metadataParser.parseMetadata(diagramContent);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/extract-diagram")
    public ResponseEntity<String> extractDiagram(@RequestBody String diagramContent) {
        try {
            String pureDiagram = metadataParser.extractDiagramContent(diagramContent);
            return ResponseEntity.ok(pureDiagram);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<MetadataValidationResult> validate(@RequestBody String diagramContent) {
        try {
            boolean hasMetadata = metadataParser.hasMetadata(diagramContent);
            UMLMetadata metadata = metadataParser.parseMetadata(diagramContent);
            
            MetadataValidationResult result = MetadataValidationResult.builder()
                .hasMetadata(hasMetadata)
                .valid(true)
                .metadata(metadata)
                .message("Métadonnées parsées avec succès")
                .build();
                
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            MetadataValidationResult result = MetadataValidationResult.builder()
                .hasMetadata(false)
                .valid(false)
                .error(e.getMessage())
                .message("Erreur lors du parsing des métadonnées")
                .build();
                
            return ResponseEntity.ok(result);
        }
    }
    
    @Data
    @Builder
    public static class MetadataValidationResult {
        private boolean hasMetadata;
        private boolean valid;
        private UMLMetadata metadata;
        private String message;
        private String error;
    }
}

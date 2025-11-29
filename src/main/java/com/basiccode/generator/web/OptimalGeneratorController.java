package com.basiccode.generator.web;

import com.basiccode.generator.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur optimal avec une seule route et Strategy Pattern
 */
@RestController
@RequestMapping("/api/generate")
@CrossOrigin(origins = "*")
public class OptimalGeneratorController {
    
    @Autowired
    private DiagramDetector detector;
    
    @Autowired
    private StrategyFactory strategyFactory;
    
    /**
     * SEULE ROUTE - Génération automatique selon les diagrammes fournis
     */
    @PostMapping
    public ResponseEntity<byte[]> generate(@RequestBody UnifiedGenerationRequest request) {
        try {
            // 1. Détection précise des diagrammes
            DiagramDetector.GenerationStrategy strategy = detector.detectStrategy(request);
            
            // 2. Sélection automatique de la stratégie
            CodeGenerationStrategy generationStrategy = strategyFactory.getStrategy(strategy);
            
            // 3. Génération du code
            byte[] zipBytes = generationStrategy.generateCode(request);
            
            // 4. Réponse avec nom de fichier dynamique
            String filename = strategy.name().toLowerCase() + "-" + request.language() + "-project.zip";
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
                
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(("Erreur de génération: " + e.getMessage()).getBytes());
        }
    }
    
    /**
     * Analyse des diagrammes fournis (pour debug/info)
     */
    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResult> analyze(@RequestBody UnifiedGenerationRequest request) {
        try {
            DiagramDetector.DiagramSignature signature = detector.analyzeDiagrams(request);
            DiagramDetector.GenerationStrategy strategy = detector.detectStrategy(request);
            
            return ResponseEntity.ok(new AnalysisResult(
                signature,
                strategy,
                strategyFactory.hasStrategy(strategy),
                "Stratégie détectée: " + strategy + " (complexité: " + signature.complexity() + ")"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.ok(new AnalysisResult(
                null, null, false, "Erreur d'analyse: " + e.getMessage()
            ));
        }
    }
    
    public record AnalysisResult(
        DiagramDetector.DiagramSignature signature,
        DiagramDetector.GenerationStrategy strategy,
        boolean supported,
        String message
    ) {}
}
package com.basiccode.generator.service;

import com.basiccode.generator.model.ComprehensiveCodeResult;
import com.basiccode.generator.exception.GenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Simple service for basic code generation using existing architecture
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CodeGenerationService {
    
    private final TripleDiagramCodeGeneratorService generatorService;
    
    /**
     * Generate code for a specific language using simple class diagram
     */
    public ComprehensiveCodeResult generateForLanguage(
            String umlContent,
            String language,
            String packageName) throws GenerationException {
        
        log.info("🚀 Starting code generation - Language: {}, Package: {}", language, packageName);
        
        try {
            // Use existing service with empty sequence and state diagrams for simple generation
            return generatorService.generateComprehensiveCode(
                umlContent,
                "", // Empty sequence diagram
                "", // Empty state diagram
                packageName,
                language
            );
        } catch (Exception e) {
            log.error("❌ Generation failed: {}", e.getMessage(), e);
            throw new GenerationException("Code generation failed: " + e.getMessage(), e);
        }
    }
}
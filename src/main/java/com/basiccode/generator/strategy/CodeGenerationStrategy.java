package com.basiccode.generator.strategy;

import com.basiccode.generator.web.UnifiedGenerationRequest;

/**
 * Interface Strategy pour la génération de code
 */
public interface CodeGenerationStrategy {
    
    /**
     * Génère le code selon la stratégie spécifique
     */
    byte[] generateCode(UnifiedGenerationRequest request) throws Exception;
    
    /**
     * Retourne le type de stratégie
     */
    DiagramDetector.GenerationStrategy getStrategyType();
    
    /**
     * Valide si la requête est compatible avec cette stratégie
     */
    boolean canHandle(DiagramDetector.DiagramSignature signature);
}
package com.basiccode.generator.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory pour sélectionner la stratégie de génération appropriée
 */
@Component
public class StrategyFactory {
    
    private final Map<DiagramDetector.GenerationStrategy, CodeGenerationStrategy> strategies;
    
    @Autowired
    public StrategyFactory(List<CodeGenerationStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                CodeGenerationStrategy::getStrategyType,
                Function.identity()
            ));
    }
    
    /**
     * Sélectionne la stratégie appropriée
     */
    public CodeGenerationStrategy getStrategy(DiagramDetector.GenerationStrategy strategyType) {
        CodeGenerationStrategy strategy = strategies.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("Stratégie non supportée: " + strategyType);
        }
        return strategy;
    }
    
    /**
     * Vérifie si une stratégie est disponible
     */
    public boolean hasStrategy(DiagramDetector.GenerationStrategy strategyType) {
        return strategies.containsKey(strategyType);
    }
}
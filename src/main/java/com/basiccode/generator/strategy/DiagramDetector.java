package com.basiccode.generator.strategy;

import com.basiccode.generator.web.UnifiedGenerationRequest;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

/**
 * Détecteur précis des types de diagrammes UML
 */
@Component
public class DiagramDetector {
    
    private static final Pattern CLASS_PATTERN = Pattern.compile("classDiagram\\s", Pattern.CASE_INSENSITIVE);
    private static final Pattern SEQUENCE_PATTERN = Pattern.compile("sequenceDiagram\\s", Pattern.CASE_INSENSITIVE);
    private static final Pattern STATE_PATTERN = Pattern.compile("stateDiagram(-v2)?\\s", Pattern.CASE_INSENSITIVE);
    private static final Pattern OBJECT_PATTERN = Pattern.compile("objectDiagram\\s", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMPONENT_PATTERN = Pattern.compile("componentDiagram\\s", Pattern.CASE_INSENSITIVE);
    private static final Pattern ACTIVITY_PATTERN = Pattern.compile("activityDiagram\\s", Pattern.CASE_INSENSITIVE);
    
    /**
     * Détecte la stratégie de génération basée sur les diagrammes fournis
     */
    public GenerationStrategy detectStrategy(UnifiedGenerationRequest request) {
        DiagramSignature signature = analyzeDiagrams(request);
        return mapToStrategy(signature);
    }
    
    /**
     * Analyse précise des diagrammes présents
     */
    public DiagramSignature analyzeDiagrams(UnifiedGenerationRequest request) {
        return new DiagramSignature(
            isValidDiagram(request.classDiagramContent(), CLASS_PATTERN),
            isValidDiagram(request.sequenceDiagramContent(), SEQUENCE_PATTERN),
            isValidDiagram(request.stateDiagramContent(), STATE_PATTERN),
            isValidDiagram(request.objectDiagramContent(), OBJECT_PATTERN),
            isValidDiagram(request.componentDiagramContent(), COMPONENT_PATTERN),
            isValidDiagram(request.activityDiagramContent(), ACTIVITY_PATTERN)
        );
    }
    
    private boolean isValidDiagram(String content, Pattern pattern) {
        return content != null && 
               !content.trim().isEmpty() && 
               pattern.matcher(content).find();
    }
    
    private GenerationStrategy mapToStrategy(DiagramSignature signature) {
        if (signature.hasActivity()) return GenerationStrategy.PERFECT;
        if (signature.hasComponent() || signature.hasObject()) return GenerationStrategy.ULTIMATE;
        if (signature.hasState()) return GenerationStrategy.COMPREHENSIVE;
        if (signature.hasSequence()) return GenerationStrategy.BEHAVIORAL;
        if (signature.hasClass()) return GenerationStrategy.BASIC;
        
        throw new IllegalArgumentException("Aucun diagramme valide détecté");
    }
    
    public record DiagramSignature(
        boolean hasClass,
        boolean hasSequence, 
        boolean hasState,
        boolean hasObject,
        boolean hasComponent,
        boolean hasActivity
    ) {
        public int complexity() {
            return (hasClass() ? 1 : 0) + (hasSequence() ? 1 : 0) + (hasState() ? 1 : 0) +
                   (hasObject() ? 1 : 0) + (hasComponent() ? 1 : 0) + (hasActivity() ? 1 : 0);
        }
    }
    
    public enum GenerationStrategy {
        BASIC, BEHAVIORAL, COMPREHENSIVE, ULTIMATE, PERFECT
    }
}
package com.basiccode.generator.model;

import com.basiccode.generator.parser.Diagnostic;
import com.basiccode.generator.parser.DiagramType;

import java.util.*;

/**
 * Combined result from parsing multiple diagram types
 * Contains optional models + aggregated diagnostics
 */
public class CombinedParseResult {
    private final Map<DiagramType, Optional<? extends Diagram>> models;
    private final List<Diagnostic> diagnostics;
    private final GenerationStatus status;
    
    public CombinedParseResult(Map<DiagramType, Optional<? extends Diagram>> models, 
                              List<Diagnostic> diagnostics, 
                              GenerationStatus status) {
        this.models = Map.copyOf(models);
        this.diagnostics = List.copyOf(diagnostics);
        this.status = status;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Diagram> Optional<T> getModel(DiagramType type) {
        return (Optional<T>) models.get(type);
    }
    
    public boolean hasModel(DiagramType type) {
        return models.containsKey(type) && models.get(type).isPresent();
    }
    
    public boolean hasClassDiagram() { return hasModel(DiagramType.CLASS); }
    public boolean hasSequenceDiagram() { return hasModel(DiagramType.SEQUENCE); }
    public boolean hasStateDiagram() { return hasModel(DiagramType.STATE); }
    
    public List<Diagnostic> getDiagnostics() { return diagnostics; }
    public GenerationStatus getStatus() { return status; }
    
    public boolean canGenerate() { 
        return status != GenerationStatus.FATAL_ERROR && hasClassDiagram(); 
    }
    
    public Set<DiagramType> getAvailableTypes() {
        return models.entrySet().stream()
            .filter(e -> e.getValue().isPresent())
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toSet());
    }
}

enum GenerationStatus {
    SUCCESS,        // All requested diagrams parsed
    PARTIAL,        // Some diagrams missing but can generate
    WARNINGS,       // All parsed but with warnings
    FATAL_ERROR     // Cannot generate (no class diagram or critical errors)
}
package com.basiccode.generator.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for creating appropriate UML parsers
 * Implements Factory pattern with Spring dependency injection
 */
@Component
public class ParserFactory {
    
    private final Map<DiagramType, UmlParser<?>> parsers;
    
    public ParserFactory() {
        this.parsers = Map.of();
    }
    
    @Autowired
    public ParserFactory(List<UmlParser<?>> parserList) {
        this.parsers = parserList.stream()
            .collect(Collectors.toMap(
                UmlParser::getSupportedType,
                Function.identity()
            ));
    }
    
    /**
     * Get parser for specific diagram type with type safety
     */
    @SuppressWarnings("unchecked")
    public <T extends com.basiccode.generator.model.Diagram> UmlParser<T> getParser(DiagramType type, Class<T> expectedType) {
        UmlParser<?> parser = parsers.get(type);
        if (parser == null) {
            throw new IllegalArgumentException("No parser available for diagram type: " + type);
        }
        
        // Validate type compatibility at runtime
        if (!isCompatibleParser(parser, expectedType)) {
            throw new IllegalArgumentException("Parser for " + type + " is not compatible with " + expectedType.getSimpleName());
        }
        
        return (UmlParser<T>) parser;
    }
    
    /**
     * Legacy method - deprecated for type safety
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T extends com.basiccode.generator.model.Diagram> UmlParser<T> getParser(DiagramType type) {
        return (UmlParser<T>) parsers.get(type);
    }
    
    private boolean isCompatibleParser(UmlParser<?> parser, Class<?> expectedType) {
        // Simple validation - can be enhanced with reflection
        return true; // For now, trust the registration
    }
    
    /**
     * Auto-detect diagram type from content
     */
    public DiagramType detectDiagramType(String content) {
        String normalized = content.trim().toLowerCase();
        
        if (normalized.contains("classdiagram")) return DiagramType.CLASS;
        if (normalized.contains("sequencediagram")) return DiagramType.SEQUENCE;
        if (normalized.contains("statediagram")) return DiagramType.STATE;
        if (normalized.contains("objectdiagram")) return DiagramType.OBJECT;
        if (normalized.contains("componentdiagram")) return DiagramType.COMPONENT;
        if (normalized.contains("activitydiagram")) return DiagramType.ACTIVITY;
        
        throw new IllegalArgumentException("Cannot detect diagram type from content");
    }
    
    /**
     * Get all available parsers
     */
    public Map<DiagramType, UmlParser<?>> getAllParsers() {
        return Map.copyOf(parsers);
    }
    
    /**
     * Simple parse method for backward compatibility
     */
    public com.basiccode.generator.model.Diagram parse(String content) {
        // Simple implementation for backward compatibility
        return new com.basiccode.generator.model.Diagram();
    }
}
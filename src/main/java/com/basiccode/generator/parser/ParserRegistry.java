package com.basiccode.generator.parser;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Thread-safe registry for parser suppliers
 * Ensures each parsing operation gets a fresh parser instance
 */
@Component
public class ParserRegistry {
    
    private final Map<DiagramType, Supplier<UmlParser<?>>> parserSuppliers = new ConcurrentHashMap<>();
    
    /**
     * Register parser supplier for thread-safe instantiation
     */
    public <T extends com.basiccode.generator.model.Diagram> void registerParser(
            DiagramType type, Supplier<UmlParser<T>> supplier) {
        parserSuppliers.put(type, () -> supplier.get());
    }
    
    /**
     * Get fresh parser instance for thread safety
     */
    @SuppressWarnings("unchecked")
    public <T extends com.basiccode.generator.model.Diagram> UmlParser<T> createParser(DiagramType type) {
        Supplier<UmlParser<?>> supplier = parserSuppliers.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("No parser registered for type: " + type);
        }
        return (UmlParser<T>) supplier.get();
    }
    
    /**
     * Check if parser is available for type
     */
    public boolean hasParser(DiagramType type) {
        return parserSuppliers.containsKey(type);
    }
}
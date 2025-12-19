package com.basiccode.generator.parser;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLRelationship;
import java.util.List;
import java.util.Map;

/**
 * Represents a parsed UML diagram with metadata
 */
public class ParsedDiagram {
    private final List<EnhancedClass> classes;
    private final List<UMLRelationship> relationships;
    private final Map<String, String> metadata;
    
    public ParsedDiagram(List<EnhancedClass> classes, List<UMLRelationship> relationships, Map<String, String> metadata) {
        this.classes = classes;
        this.relationships = relationships;
        this.metadata = metadata;
    }
    
    public List<EnhancedClass> getClasses() { 
        return classes; 
    }
    
    public List<UMLRelationship> getRelationships() { 
        return relationships; 
    }
    
    public Map<String, String> getMetadata() { 
        return metadata; 
    }
}
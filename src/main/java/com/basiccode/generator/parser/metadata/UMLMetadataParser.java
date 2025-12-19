package com.basiccode.generator.parser.metadata;

import com.basiccode.generator.model.metadata.UMLMetadata;

public interface UMLMetadataParser {
    
    UMLMetadata parseMetadata(String diagramContent);
    
    String extractDiagramContent(String diagramContent);
    
    boolean hasMetadata(String diagramContent);
}

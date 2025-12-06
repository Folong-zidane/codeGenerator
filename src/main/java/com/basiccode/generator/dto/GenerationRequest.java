package com.basiccode.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for code generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerationRequest {
    
    /** Project name */
    private String projectName;
    
    /** Base package name */
    private String packageName;
    
    /** Project description */
    private String description;
    
    /** UML diagram content (Mermaid format) */
    private String diagramContent;
    
    /** Include tests in generation */
    private Boolean includeTests;
    
    /** Generate documentation */
    private Boolean generateDocs;
}

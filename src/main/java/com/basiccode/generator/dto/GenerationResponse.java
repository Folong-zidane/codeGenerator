package com.basiccode.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for code generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerationResponse {
    
    /** Unique generation ID */
    private String generationId;
    
    /** Programming language */
    private String language;
    
    /** Project name */
    private String projectName;
    
    /** Root path of generated code */
    private String outputPath;
    
    /** List of generated files */
    private List<String> generatedFiles;
    
    /** Code quality metrics */
    private QualityMetrics qualityMetrics;
    
    /** Generation status */
    private GenerationStatus status;
    
    /** Timestamp of generation */
    private Long generatedAt;
    
    /**
     * Quality metrics for generated code
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QualityMetrics {
        private Integer classCount;
        private Integer methodCount;
        private Double averageMethodLines;
        private Double codeCompleteness;
        private Integer violations;
        private String summary;
    }
    
    public enum GenerationStatus {
        PENDING, IN_PROGRESS, SUCCESS, FAILED
    }
}

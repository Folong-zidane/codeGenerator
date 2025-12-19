package com.basiccode.generator.exception;

/**
 * 🚨 Exception spécialisée pour les erreurs de génération de code
 */
public class GenerationException extends RuntimeException {
    
    private final String errorCode;
    private final String phase;
    
    public GenerationException(String message) {
        super(message);
        this.errorCode = "GENERATION_ERROR";
        this.phase = "UNKNOWN";
    }
    
    public GenerationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERATION_ERROR";
        this.phase = "UNKNOWN";
    }
    
    public GenerationException(String message, String errorCode, String phase) {
        super(message);
        this.errorCode = errorCode;
        this.phase = phase;
    }
    
    public GenerationException(String message, Throwable cause, String errorCode, String phase) {
        super(message, cause);
        this.errorCode = errorCode;
        this.phase = phase;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getPhase() {
        return phase;
    }
    
    /**
     * Exceptions spécialisées pour différentes phases
     */
    public static class MetadataParsingException extends GenerationException {
        public MetadataParsingException(String message) {
            super(message, "METADATA_PARSING_ERROR", "PARSING");
        }
        
        public MetadataParsingException(String message, Throwable cause) {
            super(message, cause, "METADATA_PARSING_ERROR", "PARSING");
        }
    }
    
    public static class CodeGenerationException extends GenerationException {
        public CodeGenerationException(String message) {
            super(message, "CODE_GENERATION_ERROR", "GENERATION");
        }
        
        public CodeGenerationException(String message, Throwable cause) {
            super(message, cause, "CODE_GENERATION_ERROR", "GENERATION");
        }
    }
    
    public static class ValidationException extends GenerationException {
        public ValidationException(String message) {
            super(message, "VALIDATION_ERROR", "VALIDATION");
        }
        
        public ValidationException(String message, Throwable cause) {
            super(message, cause, "VALIDATION_ERROR", "VALIDATION");
        }
    }
}
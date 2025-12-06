package com.basiccode.generator.generator.spring;

import com.basiccode.generator.model.EnhancedClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Spring Boot exception generator
 * Generates custom exceptions and global exception handler
 */
@Component
@Slf4j
public class SpringBootExceptionGenerator {
    
    public String generateEntityNotFoundException(String packageName) {
        log.info("Generating EntityNotFoundException");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".exception;").append("\n\n");
        code.append("import org.springframework.http.HttpStatus;").append("\n");
        code.append("import org.springframework.web.bind.annotation.ResponseStatus;").append("\n\n");
        
        code.append("@ResponseStatus(HttpStatus.NOT_FOUND)").append("\n");
        code.append("public class EntityNotFoundException extends RuntimeException {").append("\n\n");
        
        code.append("    public EntityNotFoundException(String entityName, Long id) {").append("\n");
        code.append("        super(String.format(\"%s not found with id: %d\", entityName, id));").append("\n");
        code.append("    }").append("\n\n");
        
        code.append("    public EntityNotFoundException(String entityName, String field, Object value) {").append("\n");
        code.append("        super(String.format(\"%s not found with %s: %s\", entityName, field, value));").append("\n");
        code.append("    }").append("\n\n");
        
        code.append("    public EntityNotFoundException(String message) {").append("\n");
        code.append("        super(message);").append("\n");
        code.append("    }").append("\n");
        code.append("}").append("\n");
        
        return code.toString();
    }
    
    public String generateValidationException(String packageName) {
        log.info("Generating ValidationException");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".exception;").append("\n\n");
        code.append("import org.springframework.http.HttpStatus;").append("\n");
        code.append("import org.springframework.web.bind.annotation.ResponseStatus;").append("\n");
        code.append("import java.util.Map;").append("\n");
        code.append("import java.util.HashMap;").append("\n\n");
        
        code.append("@ResponseStatus(HttpStatus.BAD_REQUEST)").append("\n");
        code.append("public class ValidationException extends RuntimeException {").append("\n\n");
        
        code.append("    private final Map<String, String> errors = new HashMap<>();").append("\n\n");
        
        code.append("    public ValidationException(String message) {").append("\n");
        code.append("        super(message);").append("\n");
        code.append("    }").append("\n\n");
        
        code.append("    public ValidationException(String field, String message) {").append("\n");
        code.append("        super(String.format(\"Validation failed for field '%s': %s\", field, message));").append("\n");
        code.append("        this.errors.put(field, message);").append("\n");
        code.append("    }").append("\n\n");
        
        code.append("    public ValidationException(Map<String, String> errors) {").append("\n");
        code.append("        super(\"Validation failed for multiple fields\");").append("\n");
        code.append("        this.errors.putAll(errors);").append("\n");
        code.append("    }").append("\n\n");
        
        code.append("    public Map<String, String> getErrors() {").append("\n");
        code.append("        return errors;").append("\n");
        code.append("    }").append("\n");
        code.append("}").append("\n");
        
        return code.toString();
    }
    
    public String generateGlobalExceptionHandler(String packageName, List<EnhancedClass> classes) {
        log.info("Generating GlobalExceptionHandler");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".exception;").append("\n\n");
        code.append("import lombok.extern.slf4j.Slf4j;").append("\n");
        code.append("import org.springframework.http.HttpStatus;").append("\n");
        code.append("import org.springframework.http.ResponseEntity;").append("\n");
        code.append("import org.springframework.web.bind.MethodArgumentNotValidException;").append("\n");
        code.append("import org.springframework.web.bind.annotation.ExceptionHandler;").append("\n");
        code.append("import org.springframework.web.bind.annotation.RestControllerAdvice;").append("\n");
        code.append("import org.springframework.web.context.request.WebRequest;").append("\n");
        code.append("import javax.validation.ConstraintViolationException;").append("\n");
        code.append("import java.time.LocalDateTime;").append("\n");
        code.append("import java.util.HashMap;").append("\n");
        code.append("import java.util.Map;").append("\n\n");
        
        code.append("@RestControllerAdvice").append("\n");
        code.append("@Slf4j").append("\n");
        code.append("public class GlobalExceptionHandler {").append("\n\n");
        
        // EntityNotFoundException handler
        code.append("    @ExceptionHandler(EntityNotFoundException.class)").append("\n");
        code.append("    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(").append("\n");
        code.append("            EntityNotFoundException ex, WebRequest request) {").append("\n");
        code.append("        log.warn(\"Entity not found: {}\", ex.getMessage());").append("\n");
        code.append("        ").append("\n");
        code.append("        ErrorResponse errorResponse = ErrorResponse.builder()").append("\n");
        code.append("            .timestamp(LocalDateTime.now())").append("\n");
        code.append("            .status(HttpStatus.NOT_FOUND.value())").append("\n");
        code.append("            .error(\"Not Found\")").append("\n");
        code.append("            .message(ex.getMessage())").append("\n");
        code.append("            .path(request.getDescription(false).replace(\"uri=\", \"\"))").append("\n");
        code.append("            .build();").append("\n");
        code.append("        ").append("\n");
        code.append("        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);").append("\n");
        code.append("    }").append("\n\n");
        
        // ValidationException handler
        code.append("    @ExceptionHandler(ValidationException.class)").append("\n");
        code.append("    public ResponseEntity<ErrorResponse> handleValidationException(").append("\n");
        code.append("            ValidationException ex, WebRequest request) {").append("\n");
        code.append("        log.warn(\"Validation error: {}\", ex.getMessage());").append("\n");
        code.append("        ").append("\n");
        code.append("        ErrorResponse errorResponse = ErrorResponse.builder()").append("\n");
        code.append("            .timestamp(LocalDateTime.now())").append("\n");
        code.append("            .status(HttpStatus.BAD_REQUEST.value())").append("\n");
        code.append("            .error(\"Validation Failed\")").append("\n");
        code.append("            .message(ex.getMessage())").append("\n");
        code.append("            .path(request.getDescription(false).replace(\"uri=\", \"\"))").append("\n");
        code.append("            .errors(ex.getErrors())").append("\n");
        code.append("            .build();").append("\n");
        code.append("        ").append("\n");
        code.append("        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);").append("\n");
        code.append("    }").append("\n\n");
        
        // MethodArgumentNotValidException handler
        code.append("    @ExceptionHandler(MethodArgumentNotValidException.class)").append("\n");
        code.append("    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(").append("\n");
        code.append("            MethodArgumentNotValidException ex, WebRequest request) {").append("\n");
        code.append("        log.warn(\"Method argument validation failed: {}\", ex.getMessage());").append("\n");
        code.append("        ").append("\n");
        code.append("        Map<String, String> errors = new HashMap<>();").append("\n");
        code.append("        ex.getBindingResult().getFieldErrors().forEach(error -> ").append("\n");
        code.append("            errors.put(error.getField(), error.getDefaultMessage())").append("\n");
        code.append("        );").append("\n");
        code.append("        ").append("\n");
        code.append("        ErrorResponse errorResponse = ErrorResponse.builder()").append("\n");
        code.append("            .timestamp(LocalDateTime.now())").append("\n");
        code.append("            .status(HttpStatus.BAD_REQUEST.value())").append("\n");
        code.append("            .error(\"Validation Failed\")").append("\n");
        code.append("            .message(\"Invalid input parameters\")").append("\n");
        code.append("            .path(request.getDescription(false).replace(\"uri=\", \"\"))").append("\n");
        code.append("            .errors(errors)").append("\n");
        code.append("            .build();").append("\n");
        code.append("        ").append("\n");
        code.append("        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);").append("\n");
        code.append("    }").append("\n\n");
        
        // Generic exception handler
        code.append("    @ExceptionHandler(Exception.class)").append("\n");
        code.append("    public ResponseEntity<ErrorResponse> handleGenericException(").append("\n");
        code.append("            Exception ex, WebRequest request) {").append("\n");
        code.append("        log.error(\"Unexpected error occurred: {}\", ex.getMessage(), ex);").append("\n");
        code.append("        ").append("\n");
        code.append("        ErrorResponse errorResponse = ErrorResponse.builder()").append("\n");
        code.append("            .timestamp(LocalDateTime.now())").append("\n");
        code.append("            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())").append("\n");
        code.append("            .error(\"Internal Server Error\")").append("\n");
        code.append("            .message(\"An unexpected error occurred\")").append("\n");
        code.append("            .path(request.getDescription(false).replace(\"uri=\", \"\"))").append("\n");
        code.append("            .build();").append("\n");
        code.append("        ").append("\n");
        code.append("        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);").append("\n");
        code.append("    }").append("\n");
        code.append("}").append("\n");
        
        return code.toString();
    }
    
    public String generateErrorResponse(String packageName) {
        log.info("Generating ErrorResponse");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".exception;").append("\n\n");
        code.append("import lombok.Builder;").append("\n");
        code.append("import lombok.Data;").append("\n");
        code.append("import com.fasterxml.jackson.annotation.JsonInclude;").append("\n");
        code.append("import java.time.LocalDateTime;").append("\n");
        code.append("import java.util.Map;").append("\n\n");
        
        code.append("@Data").append("\n");
        code.append("@Builder").append("\n");
        code.append("@JsonInclude(JsonInclude.Include.NON_NULL)").append("\n");
        code.append("public class ErrorResponse {").append("\n\n");
        
        code.append("    private LocalDateTime timestamp;").append("\n");
        code.append("    private int status;").append("\n");
        code.append("    private String error;").append("\n");
        code.append("    private String message;").append("\n");
        code.append("    private String path;").append("\n");
        code.append("    private Map<String, String> errors;").append("\n");
        code.append("}").append("\n");
        
        return code.toString();
    }
    
    public String getExceptionDirectory() {
        return "exception";
    }
}
package com.basiccode.generator.parser;

import java.util.List;
import java.util.Optional;

/**
 * Rich result object for parsing operations
 * Contains model, diagnostics and success status
 */
public class ParserResult<T> {
    private final Optional<T> model;
    private final List<Diagnostic> diagnostics;
    private final boolean success;
    
    private ParserResult(Optional<T> model, List<Diagnostic> diagnostics, boolean success) {
        this.model = model;
        this.diagnostics = diagnostics;
        this.success = success;
    }
    
    public static <T> ParserResult<T> success(T model, List<Diagnostic> diagnostics) {
        return new ParserResult<>(Optional.of(model), diagnostics, true);
    }
    
    public static <T> ParserResult<T> failure(List<Diagnostic> diagnostics) {
        return new ParserResult<>(Optional.empty(), diagnostics, false);
    }
    
    public Optional<T> getModel() { return model; }
    public List<Diagnostic> getDiagnostics() { return diagnostics; }
    public boolean isSuccess() { return success; }
    public boolean hasWarnings() { return diagnostics.stream().anyMatch(d -> d.getLevel() == DiagnosticLevel.WARNING); }
}
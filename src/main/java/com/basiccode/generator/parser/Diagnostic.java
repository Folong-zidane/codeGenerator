package com.basiccode.generator.parser;

/**
 * Diagnostic information for parsing operations
 */
public class Diagnostic {
    private final DiagnosticLevel level;
    private final String message;
    private final int line;
    private final int column;
    private final String suggestion;
    
    public Diagnostic(DiagnosticLevel level, String message, int line, int column, String suggestion) {
        this.level = level;
        this.message = message;
        this.line = line;
        this.column = column;
        this.suggestion = suggestion;
    }
    
    public static Diagnostic error(String message, int line, int column) {
        return new Diagnostic(DiagnosticLevel.ERROR, message, line, column, null);
    }
    
    public static Diagnostic warning(String message, int line, int column, String suggestion) {
        return new Diagnostic(DiagnosticLevel.WARNING, message, line, column, suggestion);
    }
    
    public DiagnosticLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    public String getSuggestion() { return suggestion; }
}

enum DiagnosticLevel {
    ERROR, WARNING, INFO
}
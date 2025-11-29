package com.basiccode.generator.parser;

/**
 * Exception thrown during UML parsing operations
 */
public class ParseException extends Exception {
    
    private final String diagramType;
    private final int lineNumber;
    
    public ParseException(String message) {
        super(message);
        this.diagramType = null;
        this.lineNumber = -1;
    }
    
    public ParseException(String message, String diagramType) {
        super(message);
        this.diagramType = diagramType;
        this.lineNumber = -1;
    }
    
    public ParseException(String message, String diagramType, int lineNumber) {
        super(message);
        this.diagramType = diagramType;
        this.lineNumber = lineNumber;
    }
    
    public ParseException(String message, Throwable cause) {
        super(message, cause);
        this.diagramType = null;
        this.lineNumber = -1;
    }
    
    public String getDiagramType() { return diagramType; }
    public int getLineNumber() { return lineNumber; }
}
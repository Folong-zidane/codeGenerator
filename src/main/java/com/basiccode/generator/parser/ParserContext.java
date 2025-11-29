package com.basiccode.generator.parser;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Context information for parsing operations
 * Provides metadata about the input source
 */
public class ParserContext {
    private final String sourceId;
    private final Optional<Path> filePath;
    private final String mimeType;
    private final String encoding;
    
    private ParserContext(String sourceId, Optional<Path> filePath, String mimeType, String encoding) {
        this.sourceId = sourceId;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }
    
    public static ParserContext fromFile(Path filePath) {
        return new ParserContext(
            filePath.getFileName().toString(),
            Optional.of(filePath),
            detectMimeType(filePath),
            "UTF-8"
        );
    }
    
    public static ParserContext fromString(String sourceId) {
        return new ParserContext(sourceId, Optional.empty(), "text/plain", "UTF-8");
    }
    
    public static ParserContext web(String sourceId, String mimeType) {
        return new ParserContext(sourceId, Optional.empty(), mimeType, "UTF-8");
    }
    
    private static String detectMimeType(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".mermaid") || fileName.endsWith(".mmd")) {
            return "text/mermaid";
        }
        if (fileName.endsWith(".puml") || fileName.endsWith(".plantuml")) {
            return "text/plantuml";
        }
        return "text/plain";
    }
    
    public String getSourceId() { return sourceId; }
    public Optional<Path> getFilePath() { return filePath; }
    public String getMimeType() { return mimeType; }
    public String getEncoding() { return encoding; }
}
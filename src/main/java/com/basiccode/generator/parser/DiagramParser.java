package com.basiccode.generator.parser;

import com.basiccode.generator.model.Diagram;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class DiagramParser {
    
    public Diagram parse(String input) throws IOException {
        try {
            // Essayer d'abord le parser ANTLR
            return parseWithAntlr(input);
        } catch (Exception e) {
            // Fallback vers le parser regex
            System.out.println("ANTLR failed, using regex parser: " + e.getMessage());
            return parseWithRegex(input);
        }
    }
    
    private Diagram parseWithAntlr(String input) throws IOException {
        CharStream charStream = CharStreams.fromString(input);
        MermaidClassLexer lexer = new MermaidClassLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MermaidClassParser parser = new MermaidClassParser(tokens);
        
        ParseTree tree = parser.classDiagram();
        ModelBuilderVisitor visitor = new ModelBuilderVisitor();
        return (Diagram) visitor.visit(tree);
    }
    
    private Diagram parseWithRegex(String input) {
        RegexMermaidParser regexParser = new RegexMermaidParser();
        return regexParser.parse(input);
    }
}
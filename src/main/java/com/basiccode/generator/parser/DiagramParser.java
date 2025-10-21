package com.basiccode.generator.parser;

import com.basiccode.generator.model.Diagram;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class DiagramParser {
    
    public Diagram parse(String input) throws IOException {
        // Create input stream
        CharStream charStream = CharStreams.fromString(input);
        
        // Create lexer
        MermaidClassLexer lexer = new MermaidClassLexer(charStream);
        
        // Create token stream
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Create parser
        MermaidClassParser parser = new MermaidClassParser(tokens);
        
        // Parse
        ParseTree tree = parser.diagram();
        
        // Visit tree to build model
        ModelBuilderVisitor visitor = new ModelBuilderVisitor();
        return (Diagram) visitor.visit(tree);
    }
}
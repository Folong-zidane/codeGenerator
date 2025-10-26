package com.basiccode.generator;

import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.RegexMermaidParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class RegexParserTest {
    
    @Test
    public void testComplexDiagram() throws Exception {
        String content = Files.readString(Path.of("diagrams/pickndrop.mermaid"));
        
        RegexMermaidParser parser = new RegexMermaidParser();
        Diagram diagram = parser.parse(content);
        
        System.out.println("Classes trouvées: " + diagram.getClasses().size());
        diagram.getClasses().forEach(c -> {
            System.out.println("- " + c.getName() + 
                " (abstract: " + c.isAbstract() + 
                ", enum: " + c.isEnumeration() + 
                ", fields: " + c.getFields().size() + 
                ", methods: " + c.getMethods().size() + ")");
        });
        
        System.out.println("Relations trouvées: " + diagram.getRelationships().size());
        diagram.getRelationships().forEach(r -> {
            System.out.println("- " + r.getFromClass() + " -> " + r.getToClass() + " (" + r.getType() + ")");
        });
    }
}
package com.basiccode.generator.parser;

import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.UMLEnum;
import com.basiccode.generator.model.UMLRelationship;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UMLClassDiagramParser {
    
    public List<UMLClass> parseClasses(String diagramContent) {
        // Implémentation simple pour le moment
        return new ArrayList<>();
    }
    
    public List<UMLRelationship> parseRelationships(String diagramContent) {
        // Implémentation simple pour le moment
        return new ArrayList<>();
    }
    
    public List<UMLEnum> parseEnums(String diagramContent) {
        // Implémentation simple pour le moment
        return new ArrayList<>();
    }
}
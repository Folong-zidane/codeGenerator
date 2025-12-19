package com.basiccode.generator.parser;

import com.basiccode.generator.model.UMLClass;
import com.basiccode.generator.model.UMLAttribute;
import com.basiccode.generator.model.UMLRelationship;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
@Slf4j
public class SimpleUMLParser {
    
    public List<UMLClass> parseClasses(String diagramContent) {
        log.info("🔍 Parsing classes UML (Java 8 compatible)");
        List<UMLClass> classes = new ArrayList<>();
        
        Pattern classPattern = Pattern.compile(
            "class\\s+(\\w+)\\s*(?:<<(\\w+)>>)?\\s*\\{([^}]*)\\}", 
            Pattern.MULTILINE | Pattern.DOTALL
        );
        
        Matcher matcher = classPattern.matcher(diagramContent);
        while (matcher.find()) {
            String className = matcher.group(1);
            String stereotype = matcher.group(2);
            String classBody = matcher.group(3);
            
            if (!"enumeration".equals(stereotype) && !"enum".equals(stereotype)) {
                UMLClass umlClass = parseClass(className, stereotype, classBody);
                classes.add(umlClass);
                log.debug("✅ Parsed class: {}", className);
            }
        }
        
        log.info("✅ Parsed {} classes", classes.size());
        return classes;
    }
    
    public List<UMLRelationship> parseRelationships(String diagramContent) {
        log.info("🔗 Parsing UML relationships");
        List<UMLRelationship> relationships = new ArrayList<>();
        
        // Pattern for relationships: Class1 ||--o{ Class2 : relationName
        Pattern relationPattern = Pattern.compile(
            "(\\w+)\\s+(\\|\\|--o\\{|\\}o--\\|\\||-->|<--|\\|\\|--\\|\\|)\\s+(\\w+)(?:\\s*:\\s*(\\w+))?", 
            Pattern.MULTILINE
        );
        
        Matcher matcher = relationPattern.matcher(diagramContent);
        while (matcher.find()) {
            String sourceClass = matcher.group(1);
            String relationSymbol = matcher.group(2);
            String targetClass = matcher.group(3);
            String relationName = matcher.group(4);
            
            UMLRelationship relationship = new UMLRelationship(sourceClass, targetClass, "association");
            
            // Determine multiplicity and type from symbol
            if ("||--o{".equals(relationSymbol)) {
                relationship.setSourceMultiplicity("1");
                relationship.setTargetMultiplicity("*");
                relationship.setType("composition");
            } else if ("}o--||".equals(relationSymbol)) {
                relationship.setSourceMultiplicity("*");
                relationship.setTargetMultiplicity("1");
                relationship.setType("composition");
            } else if ("-->".equals(relationSymbol)) {
                relationship.setType("association");
            } else if ("||--||" .equals(relationSymbol)) {
                relationship.setSourceMultiplicity("1");
                relationship.setTargetMultiplicity("1");
                relationship.setType("association");
            }
            
            if (relationName != null) {
                relationship.setTargetName(relationName);
            }
            
            relationships.add(relationship);
            log.debug("✅ Parsed relationship: {} {} {}", sourceClass, relationSymbol, targetClass);
        }
        
        log.info("✅ Parsed {} relationships", relationships.size());
        return relationships;
    }
    
    private UMLClass parseClass(String className, String stereotype, String classBody) {
        List<UMLAttribute> attributes = parseAttributes(classBody);
        
        UMLClass.UMLClassBuilder builder = UMLClass.builder()
            .name(className)
            .attributes(attributes)
            .methods(new ArrayList<>());
        
        if (stereotype != null) {
            List<String> stereotypes = new ArrayList<>();
            stereotypes.add(stereotype);
            builder.stereotypes(stereotypes);
        }
        
        return builder.build();
    }
    
    private List<UMLAttribute> parseAttributes(String classBody) {
        List<UMLAttribute> attributes = new ArrayList<>();
        String[] lines = classBody.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.contains("(")) continue;
            
            Pattern attrPattern = Pattern.compile("([+\\-#~])?\\s*(\\w+)\\s+(\\w+)");
            Matcher matcher = attrPattern.matcher(line);
            
            if (matcher.find()) {
                String visibility = matcher.group(1);
                String type = matcher.group(2);
                String name = matcher.group(3);
                
                UMLAttribute attribute = UMLAttribute.builder()
                    .name(name)
                    .type(type)
                    .visibility(mapVisibility(visibility))
                    .build();
                attributes.add(attribute);
            }
        }
        
        return attributes;
    }
    
    private String mapVisibility(String visibility) {
        if (visibility == null) return "package";
        if ("+".equals(visibility)) return "public";
        if ("-".equals(visibility)) return "private";
        if ("#".equals(visibility)) return "protected";
        if ("~".equals(visibility)) return "package";
        return "public";
    }
}

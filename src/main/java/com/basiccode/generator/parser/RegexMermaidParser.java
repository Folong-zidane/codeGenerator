package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import java.util.*;
import java.util.regex.*;

public class RegexMermaidParser {
    
    public Diagram parse(String content) {
        Diagram diagram = new Diagram();
        String[] lines = content.split("\n");
        
        ClassModel currentClass = null;
        boolean inClass = false;
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("%%") || line.startsWith("//")) continue;
            
            // Classe avec stéréotype: class User <<abstract>> {
            if (line.matches("class\\s+\\w+\\s*<<.*>>.*")) {
                currentClass = parseClassWithStereotype(line);
                diagram.addClass(currentClass);
                inClass = line.contains("{");
            }
            // Classe simple: class User {
            else if (line.startsWith("class ")) {
                currentClass = parseSimpleClass(line);
                diagram.addClass(currentClass);
                inClass = line.contains("{");
            }
            // Fin de classe
            else if (line.equals("}")) {
                inClass = false;
                currentClass = null;
            }
            // Attribut: +String email
            else if (inClass && currentClass != null && isAttribute(line)) {
                Field field = parseAttribute(line);
                if (field != null) currentClass.addField(field);
            }
            // Méthode: +login()
            else if (inClass && currentClass != null && isMethod(line)) {
                Method method = parseMethod(line);
                if (method != null) currentClass.addMethod(method);
            }
            // Relations: User <|-- Client
            else if (line.contains("<|--") || line.contains("-->") || line.contains("\"")) {
                Relationship rel = parseRelationship(line);
                if (rel != null) diagram.addRelationship(rel);
            }
        }
        
        // Post-traitement : appliquer les relations d'héritage
        applyInheritanceRelationships(diagram);
        
        return diagram;
    }
    
    private void applyInheritanceRelationships(Diagram diagram) {
        for (Relationship rel : diagram.getRelationships()) {
            if (rel.getType() == RelationshipType.INHERITANCE) {
                // Trouver la classe enfant et définir son parent
                ClassModel childClass = diagram.getClasses().stream()
                    .filter(c -> c.getName().equals(rel.getToClass()))
                    .findFirst()
                    .orElse(null);
                    
                if (childClass != null) {
                    childClass.setParentClass(rel.getFromClass());
                }
                
                // Marquer la classe parent comme abstraite si elle a des enfants
                ClassModel parentClass = diagram.getClasses().stream()
                    .filter(c -> c.getName().equals(rel.getFromClass()))
                    .findFirst()
                    .orElse(null);
                    
                if (parentClass != null && !parentClass.isAbstract()) {
                    // Vérifier si elle a le stéréotype abstract
                    if ("abstract".equals(parentClass.getStereotype())) {
                        parentClass.setAbstract(true);
                    }
                }
            }
        }
    }
    
    private ClassModel parseClassWithStereotype(String line) {
        Pattern pattern = Pattern.compile("class\\s+(\\w+)\\s*<<(\\w+)>>");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            ClassModel model = new ClassModel();
            model.setName(matcher.group(1));
            String stereotype = matcher.group(2);
            
            if ("abstract".equals(stereotype)) {
                model.setAbstract(true);
            } else if ("enumeration".equals(stereotype)) {
                model.setEnumeration(true);
            }
            return model;
        }
        return null;
    }
    
    private ClassModel parseSimpleClass(String line) {
        Pattern pattern = Pattern.compile("class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            ClassModel model = new ClassModel();
            model.setName(matcher.group(1));
            return model;
        }
        return null;
    }
    
    private boolean isAttribute(String line) {
        return line.matches("[+\\-#~]?\\w+\\s+\\w+") || 
               line.matches("[+\\-#~]?\\w+\\s*:\\s*\\w+");
    }
    
    private boolean isMethod(String line) {
        return line.contains("(") && line.contains(")");
    }
    
    private Field parseAttribute(String line) {
        // Format: +String email ou +email: String
        Pattern pattern1 = Pattern.compile("([+\\-#~]?)(\\w+)\\s+(\\w+)");
        Pattern pattern2 = Pattern.compile("([+\\-#~]?)(\\w+)\\s*:\\s*(\\w+)");
        
        Matcher matcher = pattern1.matcher(line);
        if (!matcher.find()) {
            matcher = pattern2.matcher(line);
            if (!matcher.find()) return null;
        }
        
        Field field = new Field();
        field.setVisibility(parseVisibility(matcher.group(1)));
        
        // Déterminer nom et type selon le format
        if (line.contains(":")) {
            field.setName(matcher.group(2));
            field.setType(matcher.group(3));
        } else {
            field.setType(matcher.group(2));
            field.setName(matcher.group(3));
        }
        
        return field;
    }
    
    private Method parseMethod(String line) {
        // Format: +login() ou +updateProfile(newType)
        Pattern pattern = Pattern.compile("([+\\-#~]?)(\\w+)\\((.*?)\\)");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            Method method = new Method();
            method.setVisibility(parseVisibility(matcher.group(1)));
            method.setName(matcher.group(2));
            method.setReturnType("void");
            
            // Parse paramètres
            String params = matcher.group(3).trim();
            if (!params.isEmpty()) {
                String[] paramArray = params.split(",");
                for (String param : paramArray) {
                    Parameter parameter = new Parameter();
                    parameter.setName(param.trim());
                    parameter.setType("Object");
                    method.getParameters().add(parameter);
                }
            }
            
            return method;
        }
        return null;
    }
    
    private Relationship parseRelationship(String line) {
        // Héritage: User <|-- Client
        Pattern inheritance = Pattern.compile("(\\w+)\\s*<\\|--\\s*(\\w+)");
        Matcher matcher = inheritance.matcher(line);
        
        if (matcher.find()) {
            Relationship rel = new Relationship();
            rel.setFromClass(matcher.group(1)); // Parent
            rel.setToClass(matcher.group(2));   // Enfant
            rel.setType(RelationshipType.INHERITANCE);
            
            // Marquer la classe enfant avec son parent
            // Note: Ceci nécessite un post-traitement du diagramme
            return rel;
        }
        
        // Association: User "1" --> "*" Order
        Pattern association = Pattern.compile("(\\w+)\\s*\"([^\"]+)\"\\s*-->\\s*\"([^\"]+)\"\\s*(\\w+)");
        matcher = association.matcher(line);
        
        if (matcher.find()) {
            Relationship rel = new Relationship();
            rel.setFromClass(matcher.group(1));
            rel.setToClass(matcher.group(4));
            rel.setFromMultiplicity(matcher.group(2));
            rel.setToMultiplicity(matcher.group(3));
            rel.setType(RelationshipType.ASSOCIATION);
            return rel;
        }
        
        return null;
    }
    
    private Visibility parseVisibility(String symbol) {
        return switch (symbol) {
            case "+" -> Visibility.PUBLIC;
            case "-" -> Visibility.PRIVATE;
            case "#" -> Visibility.PROTECTED;
            case "~" -> Visibility.PACKAGE;
            default -> Visibility.PUBLIC;
        };
    }
}
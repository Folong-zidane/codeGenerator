package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SimpleClassParser {
    
    public Diagram parseClassDiagram(String content) {
        Diagram diagram = new Diagram();
        List<UmlClass> classes = new ArrayList<>();
        List<String> inheritanceLines = new ArrayList<>();
        List<String> relationshipLines = new ArrayList<>();
        
        String[] lines = content.split("\n");
        UmlClass currentClass = null;
        boolean inClassBody = false;
        
        for (String line : lines) {
            line = line.trim();
            
            // Parse inheritance relationships
            if (line.contains("<|--") || line.contains("--|>")) {
                inheritanceLines.add(line);
            }
            // Parse other relationships
            else if (line.contains("-->") && !line.contains("<|--") && !line.contains("--|>")) {
                relationshipLines.add(line);
            }
            // Parse class declaration
            else if (line.startsWith("class ")) {
                String className = extractClassName(line);
                currentClass = new UmlClass(className);
                currentClass.setAttributes(new ArrayList<>());
                currentClass.setMethods(new ArrayList<>());
                
                // Check if it's abstract or interface
                if (line.contains("<<abstract>>") || content.contains(className + " {\n        <<abstract>>")) {
                    currentClass.setAbstract(true);
                }
                if (line.contains("<<interface>>") || content.contains(className + " {\n        <<interface>>")) {
                    currentClass.setInterface(true);
                }
                
                classes.add(currentClass);
                inClassBody = false;
            }
            // Parse class body start
            else if (line.contains("{")) {
                inClassBody = true;
            }
            // Parse class body end
            else if (line.contains("}")) {
                inClassBody = false;
            }
            // Parse stereotypes
            else if (line.contains("<<abstract>>") && currentClass != null) {
                currentClass.setAbstract(true);
            }
            else if (line.contains("<<interface>>") && currentClass != null) {
                currentClass.setInterface(true);
            }
            // Parse attributes in class body
            else if (currentClass != null && inClassBody && !line.isEmpty()) {
                parseAttribute(line, currentClass);
            }
            // Parse single-line class with attributes
            else if (currentClass != null && line.contains(" ") && !inClassBody) {
                parseAttribute(line, currentClass);
            }
        }
        
        // Process inheritance relationships
        processInheritance(classes, inheritanceLines);
        
        // Process other relationships
        processRelationships(classes, relationshipLines);
        
        // Convert UmlClass to ClassModel with attributes, methods, and inheritance
        for (UmlClass umlClass : classes) {
            ClassModel classModel = new ClassModel();
            classModel.setName(umlClass.getName());
            classModel.setAbstract(umlClass.isAbstract());
            classModel.setInterface(umlClass.isInterface());
            classModel.setSuperClass(umlClass.getSuperClass());
            
            // Copy attributes to ClassModel
            List<Field> fields = new ArrayList<>();
            for (UmlAttribute attr : umlClass.getAttributes()) {
                Field field = new Field();
                field.setName(attr.getName());
                field.setType(attr.getType());
                field.setVisibility(attr.getVisibility());
                fields.add(field);
            }
            classModel.setFields(fields);
            
            // Copy methods to ClassModel
            if (umlClass.getMethods() != null) {
                classModel.setMethods(new ArrayList<>(umlClass.getMethods()));
            }
            
            diagram.addClass(classModel);
        }
        return diagram;
    }
    
    private String extractClassName(String line) {
        // Handle "class User {" or "class User"
        String className = line.substring(6).trim();
        if (className.contains("{")) {
            className = className.substring(0, className.indexOf("{")).trim();
        }
        return className;
    }
    
    private void parseAttribute(String line, UmlClass currentClass) {
        // Parse "+Long id" or "+String username" or "+authenticate(password) boolean"
        line = line.trim();
        if (line.startsWith("+") || line.startsWith("-") || line.startsWith("#")) {
            String visibility = line.substring(0, 1);
            String rest = line.substring(1).trim();
            
            // Check if it's a method (contains parentheses)
            if (rest.contains("(") && rest.contains(")")) {
                parseMethod(rest, currentClass, visibility);
            } else {
                // It's an attribute
                String[] parts = rest.split("\\s+", 2);
                if (parts.length >= 2) {
                    UmlAttribute attr = new UmlAttribute();
                    attr.setType(parts[0]); // Long, String, etc.
                    attr.setName(parts[1]); // id, username, etc.
                    attr.setVisibility(parseVisibility(visibility));
                    currentClass.getAttributes().add(attr);
                }
            }
        }
    }
    
    private void parseMethod(String methodLine, UmlClass currentClass, String visibility) {
        // Parse "authenticate(password) boolean" or "updateProfile(profile) void"
        try {
            int parenIndex = methodLine.indexOf("(");
            int closeParenIndex = methodLine.indexOf(")");
            
            if (parenIndex > 0 && closeParenIndex > parenIndex) {
                String methodName = methodLine.substring(0, parenIndex).trim();
                String parameters = methodLine.substring(parenIndex + 1, closeParenIndex).trim();
                String returnType = methodLine.substring(closeParenIndex + 1).trim();
                
                if (returnType.isEmpty()) {
                    returnType = "void";
                }
                
                Method method = new Method();
                method.setName(methodName);
                method.setReturnType(returnType);
                method.setVisibility(parseVisibility(visibility));
                
                // Parse parameters
                List<Parameter> paramList = new ArrayList<>();
                if (!parameters.isEmpty()) {
                    String[] paramParts = parameters.split(",");
                    for (String param : paramParts) {
                        param = param.trim();
                        if (!param.isEmpty()) {
                            Parameter parameter = new Parameter();
                            parameter.setName(param);
                            parameter.setType("String"); // Default type
                            paramList.add(parameter);
                        }
                    }
                }
                method.setParameters(paramList);
                
                currentClass.getMethods().add(method);
            }
        } catch (Exception e) {
            // If parsing fails, ignore the method
        }
    }
    
    private Visibility parseVisibility(String symbol) {
        return switch (symbol) {
            case "+" -> Visibility.PUBLIC;
            case "-" -> Visibility.PRIVATE;
            case "#" -> Visibility.PROTECTED;
            default -> Visibility.PUBLIC;
        };
    }
    
    public SequenceDiagram parseSequenceDiagram(String content) {
        SequenceDiagram diagram = new SequenceDiagram();
        diagram.setParticipants(new ArrayList<>());
        diagram.setMessages(new ArrayList<>());
        return diagram;
    }
    
    public StateMachine parseStateDiagram(String content) {
        StateMachine machine = new StateMachine();
        List<State> states = new ArrayList<>();
        List<StateTransition> transitions = new ArrayList<>();
        
        if (content != null && !content.trim().isEmpty()) {
            String[] lines = content.split("\n");
            
            for (String line : lines) {
                line = line.trim();
                if (line.contains("-->")) {
                    parseStateTransition(line, states, transitions);
                }
            }
        }
        
        // Add default states if none found
        if (states.isEmpty()) {
            states.add(createState("ACTIVE", true, false));
            states.add(createState("SUSPENDED", false, false));
            
            transitions.add(createTransition("ACTIVE", "SUSPENDED", "suspend"));
            transitions.add(createTransition("SUSPENDED", "ACTIVE", "activate"));
        }
        
        machine.setStates(states);
        machine.setTransitions(transitions);
        return machine;
    }
    
    private void parseStateTransition(String line, List<State> states, List<StateTransition> transitions) {
        // Parse "[*] --> ACTIVE" or "ACTIVE --> SUSPENDED"
        String[] parts = line.split("-->");
        if (parts.length == 2) {
            String fromState = parts[0].trim().replace("[*]", "INITIAL");
            String toState = parts[1].trim();
            
            // Add states if not exists
            addStateIfNotExists(states, fromState);
            addStateIfNotExists(states, toState);
            
            // Add transition
            StateTransition transition = new StateTransition();
            transition.setFromState(fromState);
            transition.setToState(toState);
            transition.setTrigger(inferTrigger(fromState, toState));
            transitions.add(transition);
        }
    }
    
    private void addStateIfNotExists(List<State> states, String stateName) {
        if (states.stream().noneMatch(s -> s.getName().equals(stateName))) {
            states.add(createState(stateName, stateName.equals("ACTIVE"), false));
        }
    }
    
    private State createState(String name, boolean isInitial, boolean isFinal) {
        State state = new State();
        state.setName(name);
        state.setInitial(isInitial);
        state.setFinal(isFinal);
        return state;
    }
    
    private StateTransition createTransition(String from, String to, String trigger) {
        StateTransition transition = new StateTransition();
        transition.setFromState(from);
        transition.setToState(to);
        transition.setTrigger(trigger);
        return transition;
    }
    
    private String inferTrigger(String fromState, String toState) {
        if ("ACTIVE".equals(fromState) && "SUSPENDED".equals(toState)) {
            return "suspend";
        } else if ("SUSPENDED".equals(fromState) && "ACTIVE".equals(toState)) {
            return "activate";
        }
        return "transition";
    }
    
    private void processInheritance(List<UmlClass> classes, List<String> inheritanceLines) {
        for (String line : inheritanceLines) {
            // Parse "Entity <|-- User" or "User --|> Entity"
            String[] parts;
            String superClass, subClass;
            
            if (line.contains("<|--")) {
                parts = line.split("<\\|--");
                superClass = parts[0].trim();
                subClass = parts[1].trim();
            } else if (line.contains("--|>")) {
                parts = line.split("--\\|>");
                subClass = parts[0].trim();
                superClass = parts[1].trim();
            } else {
                continue;
            }
            
            // Find the subclass and set its superclass
            for (UmlClass umlClass : classes) {
                if (umlClass.getName().equals(subClass)) {
                    umlClass.setSuperClass(superClass);
                    break;
                }
            }
        }
    }
    
    private void processRelationships(List<UmlClass> classes, List<String> relationshipLines) {
        for (String line : relationshipLines) {
            // Parse "User \"1\" --> \"*\" Order : places"
            try {
                String[] parts = line.split("-->");
                if (parts.length == 2) {
                    String leftPart = parts[0].trim();
                    String rightPart = parts[1].trim();
                    
                    // Extract class names and cardinalities
                    String[] leftTokens = leftPart.split("\\s+");
                    String[] rightTokens = rightPart.split("\\s+");
                    
                    String fromClass = leftTokens[0];
                    String fromCardinality = extractCardinality(leftPart);
                    String toClass = rightTokens[0];
                    String toCardinality = extractCardinality(rightPart);
                    String relationName = extractRelationName(rightPart);
                    
                    // Add relationship to classes
                    addRelationshipToClass(classes, fromClass, toClass, fromCardinality, toCardinality, relationName);
                }
            } catch (Exception e) {
                // Skip malformed relationships
            }
        }
    }
    
    private String extractCardinality(String part) {
        if (part.contains("\"1\"")) return "1";
        if (part.contains("\"*\"")) return "*";
        if (part.contains("\"0..1\"")) return "0..1";
        if (part.contains("\"1..*\"")) return "1..*";
        return "1"; // default
    }
    
    private String extractRelationName(String rightPart) {
        if (rightPart.contains(":")) {
            String[] parts = rightPart.split(":");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        return "";
    }
    
    private void addRelationshipToClass(List<UmlClass> classes, String fromClass, String toClass, 
                                       String fromCardinality, String toCardinality, String relationName) {
        // Find the from class and add relationship info
        for (UmlClass umlClass : classes) {
            if (umlClass.getName().equals(fromClass)) {
                // Add relationship as attribute for now
                UmlAttribute relationAttr = new UmlAttribute();
                
                if ("*".equals(toCardinality) || "1..*".equals(toCardinality)) {
                    relationAttr.setType("List<" + toClass + ">");
                    relationAttr.setName(toClass.toLowerCase() + "s");
                } else {
                    relationAttr.setType(toClass);
                    relationAttr.setName(toClass.toLowerCase());
                }
                
                relationAttr.setVisibility(Visibility.PRIVATE);
                relationAttr.setRelationship(true);
                relationAttr.setRelationshipType(determineRelationshipType(fromCardinality, toCardinality));
                relationAttr.setTargetClass(toClass);
                
                umlClass.getAttributes().add(relationAttr);
                break;
            }
        }
    }
    
    private String determineRelationshipType(String fromCardinality, String toCardinality) {
        if ("1".equals(fromCardinality) && "*".equals(toCardinality)) {
            return "OneToMany";
        } else if ("*".equals(fromCardinality) && "1".equals(toCardinality)) {
            return "ManyToOne";
        } else if ("*".equals(fromCardinality) && "*".equals(toCardinality)) {
            return "ManyToMany";
        } else {
            return "OneToOne";
        }
    }
}
package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SimpleClassParser {
    
    public Diagram parseClassDiagram(String content) {
        Diagram diagram = new Diagram();
        List<UmlClass> classes = new ArrayList<>();
        
        String[] lines = content.split("\n");
        UmlClass currentClass = null;
        boolean inClassBody = false;
        
        for (String line : lines) {
            line = line.trim();
            
            // Parse class declaration
            if (line.startsWith("class ")) {
                String className = extractClassName(line);
                currentClass = new UmlClass(className);
                currentClass.setAttributes(new ArrayList<>());
                currentClass.setMethods(new ArrayList<>());
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
            // Parse attributes in class body
            else if (currentClass != null && inClassBody && !line.isEmpty()) {
                parseAttribute(line, currentClass);
            }
            // Parse single-line class with attributes
            else if (currentClass != null && line.contains(" ") && !inClassBody) {
                parseAttribute(line, currentClass);
            }
        }
        
        // Convert UmlClass to ClassModel with attributes
        for (UmlClass umlClass : classes) {
            ClassModel classModel = new ClassModel();
            classModel.setName(umlClass.getName());
            
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
        // Parse "+Long id" or "+String username"
        line = line.trim();
        if (line.startsWith("+") || line.startsWith("-") || line.startsWith("#")) {
            String visibility = line.substring(0, 1);
            String rest = line.substring(1).trim();
            
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
}
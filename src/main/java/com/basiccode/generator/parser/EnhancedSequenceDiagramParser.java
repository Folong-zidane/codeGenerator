package com.basiccode.generator.parser;

import com.basiccode.generator.model.Diagram;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ✅ IMPROVED: Enhanced Sequence Diagram Parser
 * 
 * Extracts method signatures from sequence diagrams using Mermaid syntax.
 * 
 * Supported syntax:
 * - Actor->>Service: methodName(param1: Type1, param2: Type2) -> ReturnType
 * - Actor-->>Service: asyncMethod(param: Type) -> Future<ReturnType>
 * 
 * Example:
 * sequenceDiagram
 *     User->>UserService: validateEmail(email: String) -> Boolean
 *     User->>UserService: changePassword(old: String, new: String) -> void
 *     Order->>OrderService: updateStock(productId: Long, qty: Int) -> void
 * 
 * Output: List of SequenceMethod with extracted signatures
 */
@Component
@Slf4j
public class EnhancedSequenceDiagramParser implements UmlParser {
    
    // Mermaid sequence diagram marker
    private static final String SEQUENCE_DIAGRAM_MARKER = "sequenceDiagram";
    
    // Pattern for sequence messages:
    // Actor->>Service: methodName(param1: Type1, param2: Type2) -> ReturnType
    private static final Pattern MESSAGE_PATTERN = Pattern.compile(
        "(\\w+)\\s*(?:->>|--|\\|\\|)\\s*(\\w+)\\s*:\\s*([a-zA-Z_]\\w*)\\(([^)]*)\\)\\s*(?:->\\s*(\\w+(?:<\\w+>)?))?"
    );
    
    // Pattern for individual parameters: name: Type
    private static final Pattern PARAM_PATTERN = Pattern.compile(
        "\\s*([a-zA-Z_]\\w*)\\s*:\\s*([\\w<>]+(?:\\[\\])?)"
    );
    
    @Override
    public boolean canParse(String content) {
        return content != null && content.contains(SEQUENCE_DIAGRAM_MARKER);
    }
    
    @Override
    public Diagram parse(String content) {
        log.info("🔍 Parsing sequence diagram...");
        
        SequenceDiagram diagram = new SequenceDiagram();
        
        if (!content.contains(SEQUENCE_DIAGRAM_MARKER)) {
            log.warn("⚠️ Not a sequence diagram: missing '{}' marker", SEQUENCE_DIAGRAM_MARKER);
            return diagram;
        }
        
        // Extract methods from each line
        String[] lines = content.split("\n");
        int lineNum = 0;
        
        for (String line : lines) {
            lineNum++;
            String trimmed = line.trim();
            
            // Skip comments and empty lines
            if (trimmed.isEmpty() || trimmed.startsWith("%")) {
                continue;
            }
            
            // Skip diagram definition line
            if (trimmed.contains("sequenceDiagram")) {
                continue;
            }
            
            // Parse message line
            Matcher matcher = MESSAGE_PATTERN.matcher(trimmed);
            if (matcher.find()) {
                try {
                    SequenceMethod method = extractMethod(matcher, lineNum);
                    diagram.addMethod(method);
                    
                    log.debug("✅ [Line {}] Extracted method: {}.{}({}) -> {}",
                        lineNum,
                        method.getTargetClass(),
                        method.getMethodName(),
                        method.getFormattedParameters(),
                        method.getReturnType()
                    );
                } catch (Exception e) {
                    log.warn("⚠️ [Line {}] Failed to parse: {} - {}", lineNum, trimmed, e.getMessage());
                }
            }
        }
        
        log.info("✅ Sequence diagram parsed: {} methods extracted", diagram.getMethods().size());
        return diagram;
    }
    
    /**
     * Extract method information from regex matcher
     */
    private SequenceMethod extractMethod(Matcher matcher, int lineNum) {
        String fromActor = matcher.group(1);          // e.g., "User"
        String toActor = matcher.group(2);            // e.g., "UserService"
        String methodName = matcher.group(3);         // e.g., "validateEmail"
        String parametersStr = matcher.group(4);      // e.g., "email: String"
        String returnType = matcher.group(5);         // e.g., "Boolean"
        
        // Parse parameters
        List<MethodParameter> parameters = parseParameters(parametersStr);
        
        // Default return type
        if (returnType == null || returnType.trim().isEmpty()) {
            returnType = "void";
        }
        
        return SequenceMethod.builder()
            .sourceActor(fromActor)
            .targetClass(toActor)
            .methodName(methodName)
            .parameters(parameters)
            .returnType(returnType)
            .lineNumber(lineNum)
            .build();
    }
    
    /**
     * Parse method parameters from string
     * Format: "param1: Type1, param2: Type2"
     */
    private List<MethodParameter> parseParameters(String parametersStr) {
        if (parametersStr == null || parametersStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MethodParameter> params = new ArrayList<>();
        
        // Split by comma but respect generic types
        String[] paramParts = splitParameters(parametersStr);
        
        for (String param : paramParts) {
            Matcher matcher = PARAM_PATTERN.matcher(param);
            if (matcher.find()) {
                String paramName = matcher.group(1);
                String paramType = matcher.group(2);
                
                params.add(new MethodParameter(paramName, paramType));
            }
        }
        
        return params;
    }
    
    /**
     * Split parameters respecting generic types
     * E.g., "email: String, page: Page<User>, items: List<String>"
     */
    private String[] splitParameters(String parametersStr) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int genericDepth = 0;
        
        for (char c : parametersStr.toCharArray()) {
            if (c == '<') {
                genericDepth++;
                current.append(c);
            } else if (c == '>') {
                genericDepth--;
                current.append(c);
            } else if (c == ',' && genericDepth == 0) {
                parts.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            parts.add(current.toString().trim());
        }
        
        return parts.toArray(new String[0]);
    }
    
    public boolean validate(String content) {
        try {
            parse(content);
            return true;
        } catch (Exception e) {
            log.error("Validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    public DiagramType getSupportedType() {
        return DiagramType.SEQUENCE;
    }
    
    public String getFormat() {
        return "mermaid";
    }
    
    // ======================== MODEL CLASSES ========================
    
    /**
     * Represents a sequence diagram containing multiple method calls
     */
    @Data
    public static class SequenceDiagram extends Diagram {
        private List<SequenceMethod> methods = new ArrayList<>();
        
        public void addMethod(SequenceMethod method) {
            this.methods.add(method);
        }
        
        public List<SequenceMethod> getMethods() {
            return methods;
        }
        
        /**
         * Get all methods targeting a specific class
         */
        public List<SequenceMethod> getMethodsFor(String className) {
            return methods.stream()
                .filter(m -> m.getTargetClass().equals(className))
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Represents a single method call extracted from sequence diagram
     */
    @Data
    public static class SequenceMethod {
        private String sourceActor;       // Who calls the method (e.g., "User", "Controller")
        private String targetClass;       // Class where method is defined (e.g., "UserService")
        private String methodName;        // Method name (e.g., "validateEmail")
        private List<MethodParameter> parameters; // Method parameters
        private String returnType;        // Return type (e.g., "Boolean", "void", "Mono<User>")
        private int lineNumber;          // Line number in diagram
        
        /**
         * Generate method signature as Java code
         * E.g., "public Boolean validateEmail(String email)"
         */
        public String generateSignature() {
            StringBuilder sig = new StringBuilder();
            sig.append("public ");
            sig.append(mapType(returnType)).append(" ");
            sig.append(methodName).append("(");
            
            sig.append(parameters.stream()
                .map(p -> mapType(p.getType()) + " " + p.getName())
                .collect(Collectors.joining(", ")));
            
            sig.append(")");
            return sig.toString();
        }
        
        /**
         * Get formatted parameter list for logging
         */
        public String getFormattedParameters() {
            return parameters.stream()
                .map(p -> p.getName() + ": " + p.getType())
                .collect(Collectors.joining(", "));
        }
        
        /**
         * Generate method stub with TODO comment
         */
        public String generateMethodStub(String indent) {
            StringBuilder method = new StringBuilder();
            method.append(indent).append("@Transactional\n");
            method.append(indent).append(generateSignature()).append(" {\n");
            method.append(indent).append("    log.info(\"Executing ").append(methodName)
                .append(" with params: {}\", ").append(
                    parameters.isEmpty() ? "\"no params\"" : 
                    "\"" + getFormattedParameters() + "\"").append(");\n");
            method.append(indent).append("    // TODO: Implement from sequence diagram (line ")
                .append(lineNumber).append(")\n");
            
            // Generate return statement
            if (!"void".equals(returnType)) {
                method.append(indent).append("    return null; // TODO\n");
            }
            
            method.append(indent).append("}\n");
            return method.toString();
        }
        
        /**
         * Generate reactive method stub (returns Mono/Flux)
         */
        public String generateReactiveMethodStub(String indent) {
            StringBuilder method = new StringBuilder();
            method.append(indent).append("@Transactional\n");
            method.append(indent).append("public ").append(wrapInReactive(returnType)).append(" ")
                .append(methodName).append("(");
            
            method.append(parameters.stream()
                .map(p -> mapType(p.getType()) + " " + p.getName())
                .collect(Collectors.joining(", ")));
            
            method.append(") {\n");
            method.append(indent).append("    log.info(\"Executing ").append(methodName)
                .append(" with params: {}\", ")
                .append(parameters.isEmpty() ? "\"no params\"" : "\"" + getFormattedParameters() + "\"")
                .append(");\n");
            method.append(indent).append("    // TODO: Implement from sequence diagram (line ")
                .append(lineNumber).append(")\n");
            
            // Generate return statement
            if ("void".equals(returnType)) {
                method.append(indent).append("    return Mono.empty();\n");
            } else {
                method.append(indent).append("    return Mono.just(null); // TODO\n");
            }
            
            method.append(indent).append("}\n");
            return method.toString();
        }
        
        /**
         * Create builder pattern
         */
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private String sourceActor;
            private String targetClass;
            private String methodName;
            private List<MethodParameter> parameters = new ArrayList<>();
            private String returnType = "void";
            private int lineNumber = 0;
            
            public Builder sourceActor(String sourceActor) {
                this.sourceActor = sourceActor;
                return this;
            }
            
            public Builder targetClass(String targetClass) {
                this.targetClass = targetClass;
                return this;
            }
            
            public Builder methodName(String methodName) {
                this.methodName = methodName;
                return this;
            }
            
            public Builder parameters(List<MethodParameter> parameters) {
                this.parameters = parameters;
                return this;
            }
            
            public Builder returnType(String returnType) {
                this.returnType = returnType;
                return this;
            }
            
            public Builder lineNumber(int lineNumber) {
                this.lineNumber = lineNumber;
                return this;
            }
            
            public SequenceMethod build() {
                SequenceMethod method = new SequenceMethod();
                method.sourceActor = this.sourceActor;
                method.targetClass = this.targetClass;
                method.methodName = this.methodName;
                method.parameters = this.parameters;
                method.returnType = this.returnType;
                method.lineNumber = this.lineNumber;
                return method;
            }
        }
        
        /**
         * Map UML types to Java types
         */
        private static String mapType(String umlType) {
            if (umlType == null) return "void";
            
            return switch(umlType.toLowerCase()) {
                case "string" -> "String";
                case "int", "integer" -> "Integer";
                case "long" -> "Long";
                case "float" -> "Float";
                case "double" -> "Double";
                case "boolean", "bool" -> "Boolean";
                case "uuid" -> "UUID";
                case "date", "localdatetime" -> "LocalDateTime";
                case "list" -> "List";
                case "set" -> "Set";
                case "map" -> "Map";
                case "void" -> "void";
                default -> umlType;
            };
        }
        
        /**
         * Wrap type in Mono for reactive
         */
        private static String wrapInReactive(String returnType) {
            if ("void".equals(returnType)) {
                return "Mono<Void>";
            }
            if ("List".equals(returnType) || returnType.startsWith("List<")) {
                return "Flux<" + returnType + ">";
            }
            if (returnType.contains("<")) {
                return "Mono<" + returnType + ">";
            }
            return "Mono<" + returnType + ">";
        }
    }
    
    /**
     * Represents a method parameter with name and type
     */
    @Data
    public static class MethodParameter {
        private String name;
        private String type;
        
        public MethodParameter(String name, String type) {
            this.name = name;
            this.type = type;
        }
        
        public String getName() {
            return name;
        }
        
        public String getType() {
            return type;
        }
    }
}

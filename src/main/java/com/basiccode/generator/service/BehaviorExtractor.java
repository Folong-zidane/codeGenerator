package com.basiccode.generator.service;

import com.basiccode.generator.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for extracting behavioral information from sequence diagrams
 * Implements Single Responsibility Principle
 */
@Service
public class BehaviorExtractor {
    
    public Map<String, List<BusinessMethod>> extractBusinessLogic(SequenceDiagram sequenceDiagram) {
        List<MethodCall> methodCalls = extractMethodCalls(sequenceDiagram);
        return groupMethodCallsByParticipant(methodCalls);
    }
    
    public List<MethodCall> extractMethodCalls(SequenceDiagram sequenceDiagram) {
        List<MethodCall> methodCalls = new ArrayList<>();
        
        for (Message message : sequenceDiagram.getMessages()) {
            MethodCall call = convertMessageToMethodCall(message);
            if (call != null) {
                methodCalls.add(call);
            }
        }
        
        return methodCalls;
    }
    
    private Map<String, List<BusinessMethod>> groupMethodCallsByParticipant(List<MethodCall> methodCalls) {
        Map<String, List<MethodCall>> callsByParticipant = methodCalls.stream()
            .collect(Collectors.groupingBy(MethodCall::getCallee));
        
        Map<String, List<BusinessMethod>> businessLogic = new HashMap<>();
        
        for (Map.Entry<String, List<MethodCall>> entry : callsByParticipant.entrySet()) {
            String className = entry.getKey();
            List<MethodCall> calls = entry.getValue();
            
            List<BusinessMethod> methods = calls.stream()
                .map(this::convertToBusinessMethod)
                .collect(Collectors.toList());
            
            businessLogic.put(className, methods);
        }
        
        return businessLogic;
    }
    
    private MethodCall convertMessageToMethodCall(Message message) {
        if (message.getType() == MessageType.SOLID_ARROW || message.getType() == MessageType.SOLID_ASYNC) {
            MethodCall call = new MethodCall();
            call.setCaller(message.getFrom());
            call.setCallee(message.getTo());
            call.setMethodName(extractMethodName(message.getText()));
            call.setParameters(extractParameters(message.getText()));
            call.setAsync(message.getType() == MessageType.SOLID_ASYNC);
            return call;
        }
        return null;
    }
    
    private BusinessMethod convertToBusinessMethod(MethodCall call) {
        BusinessMethod method = new BusinessMethod();
        method.setName(call.getMethodName());
        method.setParameters(call.getParameters());
        method.setAsync(call.isAsync());
        method.setReturnType(inferReturnType(call));
        method.setBusinessLogic(generateBusinessLogic(call));
        
        return method;
    }
    
    private String extractMethodName(String messageText) {
        if (messageText.contains("(")) {
            return messageText.substring(0, messageText.indexOf("(")).trim();
        }
        return messageText.trim();
    }
    
    private List<String> extractParameters(String messageText) {
        List<String> parameters = new ArrayList<>();
        
        if (messageText.contains("(") && messageText.contains(")")) {
            String paramString = messageText.substring(
                messageText.indexOf("(") + 1, 
                messageText.lastIndexOf(")")
            ).trim();
            
            if (!paramString.isEmpty()) {
                String[] params = paramString.split(",");
                for (String param : params) {
                    parameters.add(param.trim());
                }
            }
        }
        
        return parameters;
    }
    
    private String inferReturnType(MethodCall call) {
        String methodName = call.getMethodName().toLowerCase();
        
        if (methodName.startsWith("create") || methodName.startsWith("save")) {
            return extractEntityType(call.getCallee());
        } else if (methodName.startsWith("find") || methodName.startsWith("get")) {
            return methodName.contains("all") ? 
                "List<" + extractEntityType(call.getCallee()) + ">" : 
                extractEntityType(call.getCallee());
        } else if (methodName.startsWith("update")) {
            return extractEntityType(call.getCallee());
        } else if (methodName.startsWith("delete")) {
            return "void";
        }
        
        return "void";
    }
    
    private String extractEntityType(String participantName) {
        if (participantName.endsWith("Service")) {
            return participantName.replace("Service", "");
        } else if (participantName.endsWith("Repository")) {
            return participantName.replace("Repository", "");
        } else if (participantName.endsWith("Controller")) {
            return participantName.replace("Controller", "");
        }
        return participantName;
    }
    
    private List<String> generateBusinessLogic(MethodCall call) {
        List<String> logic = new ArrayList<>();
        String methodName = call.getMethodName().toLowerCase();
        
        if (methodName.contains("create") || methodName.contains("save")) {
            logic.add("// Validate input data");
            logic.add("if (entity == null) throw new IllegalArgumentException(\"Entity cannot be null\");");
            logic.add("// Set audit fields");
            logic.add("entity.setCreatedAt(LocalDateTime.now());");
            logic.add("entity.setUpdatedAt(LocalDateTime.now());");
            logic.add("// Save to database");
            logic.add("return repository.save(entity);");
        } else if (methodName.contains("update")) {
            logic.add("// Validate entity exists");
            logic.add("Optional<Entity> existing = repository.findById(id);");
            logic.add("if (existing.isEmpty()) throw new EntityNotFoundException(\"Entity not found\");");
            logic.add("// Update fields and save");
            logic.add("Entity entity = existing.get();");
            logic.add("entity.setUpdatedAt(LocalDateTime.now());");
            logic.add("return repository.save(entity);");
        }
        
        return logic;
    }
}
package com.basiccode.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method call extracted from sequence diagram
 */
public class MethodCall {
    private String caller;
    private String callee;
    private String methodName;
    private List<String> parameters = new ArrayList<>();
    private boolean async;
    private String returnType = "void";
    
    // Getters and setters
    public String getCaller() { return caller; }
    public void setCaller(String caller) { this.caller = caller; }
    
    public String getCallee() { return callee; }
    public void setCallee(String callee) { this.callee = callee; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    public List<String> getParameters() { return parameters; }
    public void setParameters(List<String> parameters) { this.parameters = parameters; }
    
    public boolean isAsync() { return async; }
    public void setAsync(boolean async) { this.async = async; }
    
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
}
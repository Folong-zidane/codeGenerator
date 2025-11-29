package com.basiccode.generator.model;

import java.util.List;

/**
 * Business method extracted from sequence diagrams
 */
public class BusinessMethod {
    
    private String name;
    private List<String> parameters;
    private String returnType;
    private boolean async;
    private List<String> businessLogic;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public boolean isAsync() {
        return async;
    }
    
    public void setAsync(boolean async) {
        this.async = async;
    }
    
    public List<String> getBusinessLogic() {
        return businessLogic;
    }
    
    public void setBusinessLogic(List<String> businessLogic) {
        this.businessLogic = businessLogic;
    }
}
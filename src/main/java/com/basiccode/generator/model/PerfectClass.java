package com.basiccode.generator.model;

import java.util.*;

public class PerfectClass {
    private UmlClass baseClass;
    private List<BusinessMethod> businessMethods = new ArrayList<>();
    private List<WorkflowMethod> workflowMethods = new ArrayList<>();
    private StateEnum stateEnum;
    private TestDataGenerator testDataGenerator;
    private Map<String, Object> metadata = new HashMap<>();
    
    public PerfectClass(UmlClass baseClass) {
        this.baseClass = baseClass;
    }
    
    public UmlClass getBaseClass() { return baseClass; }
    
    public List<BusinessMethod> getBusinessMethods() { return businessMethods; }
    public void setBusinessMethods(List<BusinessMethod> businessMethods) { this.businessMethods = businessMethods; }
    
    public List<WorkflowMethod> getWorkflowMethods() { return workflowMethods; }
    public void setWorkflowMethods(List<WorkflowMethod> workflowMethods) { this.workflowMethods = workflowMethods; }
    
    public StateEnum getStateEnum() { return stateEnum; }
    public void setStateEnum(StateEnum stateEnum) { this.stateEnum = stateEnum; }
    
    public TestDataGenerator getTestDataGenerator() { return testDataGenerator; }
    public void setTestDataGenerator(TestDataGenerator testDataGenerator) { this.testDataGenerator = testDataGenerator; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void addMetadata(String key, Object value) { this.metadata.put(key, value); }
}
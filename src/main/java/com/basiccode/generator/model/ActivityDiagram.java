package com.basiccode.generator.model;

import java.util.*;

/**
 * Model classes for activity diagrams
 */
public class ActivityDiagram {
    private List<ActivityNode> nodes = new ArrayList<>();
    private List<ActivityTransition> transitions = new ArrayList<>();
    private List<Swimlane> swimlanes = new ArrayList<>();
    private List<ActivityNote> notes = new ArrayList<>();
    
    public List<ActivityNode> getNodes() { return nodes; }
    public void addNode(ActivityNode node) { this.nodes.add(node); }
    
    public List<ActivityTransition> getTransitions() { return transitions; }
    public void addTransition(ActivityTransition transition) { this.transitions.add(transition); }
    
    public List<Swimlane> getSwimlanes() { return swimlanes; }
    public void addSwimlane(Swimlane swimlane) { this.swimlanes.add(swimlane); }
    
    public List<ActivityNote> getNotes() { return notes; }
    public void addNote(ActivityNote note) { this.notes.add(note); }
    
    public ActivityNode getNode(String id) {
        return nodes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }
}

/**
 * Represents a node in the activity diagram
 */
class ActivityNode {
    private String id;
    private String name;
    private ActivityNodeType type;
    private String condition;
    private String swimlaneId;
    private Map<String, Object> properties = new HashMap<>();
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ActivityNodeType getType() { return type; }
    public void setType(ActivityNodeType type) { this.type = type; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public String getSwimlaneId() { return swimlaneId; }
    public void setSwimlaneId(String swimlaneId) { this.swimlaneId = swimlaneId; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperty(String key, Object value) { this.properties.put(key, value); }
}

enum ActivityNodeType {
    INITIAL, FINAL, ACTIVITY, DECISION, MERGE, FORK, JOIN
}

/**
 * Represents a transition between activity nodes
 */
class ActivityTransition {
    private String source;
    private String target;
    private String guard;
    private ActivityTransitionType type;
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public String getGuard() { return guard; }
    public void setGuard(String guard) { this.guard = guard; }
    
    public ActivityTransitionType getType() { return type; }
    public void setType(ActivityTransitionType type) { this.type = type; }
}

enum ActivityTransitionType {
    CONTROL_FLOW, OBJECT_FLOW
}

/**
 * Represents a swimlane in the activity diagram
 */
class Swimlane {
    private String id;
    private String name;
    private List<String> nodeIds = new ArrayList<>();
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<String> getNodeIds() { return nodeIds; }
    public void addNodeId(String nodeId) { this.nodeIds.add(nodeId); }
}

/**
 * Represents a note in the activity diagram
 */
class ActivityNote {
    private String position;
    private String nodeId;
    private String text;
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

/**
 * Represents a business workflow extracted from activity diagram
 */
class BusinessWorkflow {
    private String name;
    private String description;
    private List<WorkflowStep> steps = new ArrayList<>();
    private List<String> preconditions = new ArrayList<>();
    private List<String> postconditions = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<WorkflowStep> getSteps() { return steps; }
    public void setSteps(List<WorkflowStep> steps) { this.steps = steps; }
    
    public List<String> getPreconditions() { return preconditions; }
    public void setPreconditions(List<String> preconditions) { this.preconditions = preconditions; }
    
    public List<String> getPostconditions() { return postconditions; }
    public void setPostconditions(List<String> postconditions) { this.postconditions = postconditions; }
}

/**
 * Represents a step in a business workflow
 */
class WorkflowStep {
    private String name;
    private WorkflowStepType type;
    private String description;
    private String condition;
    private List<String> actions = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public WorkflowStepType getType() { return type; }
    public void setType(WorkflowStepType type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public List<String> getActions() { return actions; }
    public void addAction(String action) { this.actions.add(action); }
}

enum WorkflowStepType {
    ACTION, DECISION, PARALLEL, LOOP
}

/**
 * Represents a path through the activity diagram
 */
class ActivityPath {
    private List<String> nodes = new ArrayList<>();
    
    public List<String> getNodes() { return nodes; }
    public void addNode(String nodeId) { this.nodes.add(nodeId); }
}

/**
 * Represents a method generated from workflow
 */
class WorkflowMethod {
    private String name;
    private String returnType;
    private List<String> parameters = new ArrayList<>();
    private BusinessWorkflow workflow;
    private List<String> implementation = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    
    public List<String> getParameters() { return parameters; }
    public void setParameters(List<String> parameters) { this.parameters = parameters; }
    
    public BusinessWorkflow getWorkflow() { return workflow; }
    public void setWorkflow(BusinessWorkflow workflow) { this.workflow = workflow; }
    
    public List<String> getImplementation() { return implementation; }
    public void setImplementation(List<String> implementation) { this.implementation = implementation; }
}
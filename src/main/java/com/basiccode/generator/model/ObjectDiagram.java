package com.basiccode.generator.model;

import java.util.*;

/**
 * Model classes for object diagrams
 */
public class ObjectDiagram {
    private List<ObjectInstance> objects = new ArrayList<>();
    private List<ObjectLink> links = new ArrayList<>();
    private List<ObjectNote> notes = new ArrayList<>();
    
    public List<ObjectInstance> getObjects() { return objects; }
    public void addObject(ObjectInstance object) { this.objects.add(object); }
    
    public List<ObjectLink> getLinks() { return links; }
    public void addLink(ObjectLink link) { this.links.add(link); }
    
    public List<ObjectNote> getNotes() { return notes; }
    public void addNote(ObjectNote note) { this.notes.add(note); }
}

/**
 * Represents an object instance in the diagram
 */
class ObjectInstance {
    private String name;
    private String alias;
    private List<ObjectAttribute> attributes = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    
    public List<ObjectAttribute> getAttributes() { return attributes; }
    public void addAttribute(ObjectAttribute attribute) { this.attributes.add(attribute); }
}

/**
 * Represents an attribute with its value in an object instance
 */
class ObjectAttribute {
    private String name;
    private String value;
    private String type;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

/**
 * Represents a link between object instances
 */
class ObjectLink {
    private String source;
    private String target;
    private String type;
    private String label;
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}

/**
 * Represents a note in the object diagram
 */
class ObjectNote {
    private String position;
    private String objectName;
    private String text;
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

/**
 * Test data generator from object instances
 */
class TestDataGenerator {
    private String className;
    private String instanceName;
    private Map<String, Object> attributeValues = new HashMap<>();
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getInstanceName() { return instanceName; }
    public void setInstanceName(String instanceName) { this.instanceName = instanceName; }
    
    public Map<String, Object> getAttributeValues() { return attributeValues; }
    public void setAttributeValues(Map<String, Object> attributeValues) { this.attributeValues = attributeValues; }
}

/**
 * Validation issue for object diagram validation
 */
class ValidationIssue {
    private ValidationLevel level;
    private String message;
    
    public ValidationIssue(ValidationLevel level, String message) {
        this.level = level;
        this.message = message;
    }
    
    public ValidationLevel getLevel() { return level; }
    public String getMessage() { return message; }
}

enum ValidationLevel {
    INFO, WARNING, ERROR
}
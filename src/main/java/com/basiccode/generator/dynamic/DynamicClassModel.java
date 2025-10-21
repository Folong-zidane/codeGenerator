package com.basiccode.generator.dynamic;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import java.util.List;
import java.util.Map;

public class DynamicClassModel extends ClassModel {
    private Map<String, List<Field>> conditionalFields;
    private String discriminatorField;
    
    public DynamicClassModel(String name) {
        super();
        setName(name);
    }
    
    public Map<String, List<Field>> getConditionalFields() {
        return conditionalFields;
    }
    
    public void setConditionalFields(Map<String, List<Field>> conditionalFields) {
        this.conditionalFields = conditionalFields;
    }
    
    public String getDiscriminatorField() {
        return discriminatorField;
    }
    
    public void setDiscriminatorField(String discriminatorField) {
        this.discriminatorField = discriminatorField;
    }
}
package com.basiccode.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

/**
 * Alias pour UMLMethod pour compatibilité
 */
@Data
@NoArgsConstructor
public class Method {
    private String name;
    private String returnType = "void";
    private String visibility = "public";
    private List<Parameter> parameters = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private String body;
    
    public void addParameter(Parameter parameter) { 
        parameters.add(parameter); 
    }
    
    public void addAnnotation(String annotation) { 
        annotations.add(annotation); 
    }
    
    public String getBusinessLogic() {
        return body;
    }
}
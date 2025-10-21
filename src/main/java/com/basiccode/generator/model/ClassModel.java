package com.basiccode.generator.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClassModel {
    private String name;
    private String packageName;
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<Annotation> annotations = new ArrayList<>();
    
    public void addField(Field field) {
        fields.add(field);
    }
    
    public void addMethod(Method method) {
        methods.add(method);
    }
}
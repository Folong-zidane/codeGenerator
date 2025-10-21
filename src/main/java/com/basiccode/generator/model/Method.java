package com.basiccode.generator.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Method {
    private String name;
    private String returnType;
    private Visibility visibility = Visibility.PUBLIC;
    private List<Parameter> parameters = new ArrayList<>();
    private List<Annotation> annotations = new ArrayList<>();
}
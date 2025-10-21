package com.basiccode.generator.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Field {
    private String name;
    private String type;
    private Visibility visibility = Visibility.PRIVATE;
    private List<Annotation> annotations = new ArrayList<>();
    private boolean nullable = true;
    private boolean unique = false;
}
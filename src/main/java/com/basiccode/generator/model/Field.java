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
    private List<Constraint> constraints = new ArrayList<>();
    private boolean nullable = true;
    private boolean unique = false;
    private String multiplicity;
    
    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }
    
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }
    
    public boolean hasAnnotation(String type) {
        return annotations.stream().anyMatch(a -> a.getType().equals(type));
    }
    
    public Annotation getAnnotation(String type) {
        return annotations.stream()
            .filter(a -> a.getType().equals(type))
            .findFirst()
            .orElse(null);
    }
    
    public boolean hasConstraint(String type) {
        return constraints.stream().anyMatch(c -> c.getType().equals(type));
    }
    
    public Constraint getConstraint(String type) {
        return constraints.stream()
            .filter(c -> c.getType().equals(type))
            .findFirst()
            .orElse(null);
    }
    
    public Integer getMinSize() {
        Constraint minConstraint = getConstraint("min");
        return minConstraint != null ? minConstraint.getIntValue() : null;
    }
    
    public Integer getMaxSize() {
        Constraint maxConstraint = getConstraint("max");
        return maxConstraint != null ? maxConstraint.getIntValue() : null;
    }
    
    public boolean isPrimitive() {
        return type.matches("String|Integer|Long|Float|Double|Boolean|UUID|Date|LocalDateTime|BigDecimal|Instant|LocalDate|LocalTime|Byte|Short|Character|byte\\[\\]|JSON");
    }
    
    public boolean isCollection() {
        return type.startsWith("List<") || type.startsWith("Set<") || type.startsWith("Map<") || type.startsWith("Collection<");
    }
}
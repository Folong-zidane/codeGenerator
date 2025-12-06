package com.basiccode.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Représente une entité (classe) avec ses attributs et méthodes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entity {
    private String name;
    private String packageName;
    private String description;
    private List<Attribute> attributes;
    private List<Method> methods;
    private List<String> interfaces;
    private boolean abstract_;
    private String parentClass;
    private List<String> annotations;
}

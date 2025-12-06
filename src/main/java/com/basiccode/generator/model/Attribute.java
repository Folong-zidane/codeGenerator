package com.basiccode.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Représente un attribut d'une entité
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
    private String name;
    private String type;
    private String description;
    private boolean nullable;
    private boolean unique;
    private String defaultValue;
    private List<String> constraints;
    private List<String> annotations;
    private String relationship; // OneToOne, OneToMany, ManyToOne, ManyToMany
}

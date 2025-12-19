package com.basiccode.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Alias pour UMLParameter pour compatibilité
 */
@Data
@NoArgsConstructor
public class Parameter {
    private String name;
    private String type;
}
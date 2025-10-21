package com.basiccode.generator.model;

import lombok.Data;

@Data
public class Relationship {
    private String sourceClass;
    private String targetClass;
    private RelationshipType type;
    private String sourceMultiplicity;
    private String targetMultiplicity;
}
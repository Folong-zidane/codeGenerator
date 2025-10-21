package com.basiccode.generator.model;

public enum RelationshipType {
    ASSOCIATION("-->"),
    INHERITANCE("<|--"),
    COMPOSITION("*--"),
    AGGREGATION("o--");
    
    private final String symbol;
    
    RelationshipType(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public static RelationshipType fromSymbol(String symbol) {
        for (RelationshipType type : values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        return ASSOCIATION;
    }
}
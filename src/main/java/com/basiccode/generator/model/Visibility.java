package com.basiccode.generator.model;

import javax.lang.model.element.Modifier;

public enum Visibility {
    PUBLIC("+", Modifier.PUBLIC),
    PRIVATE("-", Modifier.PRIVATE),
    PROTECTED("#", Modifier.PROTECTED),
    PACKAGE("~", Modifier.DEFAULT);
    
    private final String symbol;
    private final Modifier modifier;
    
    Visibility(String symbol, Modifier modifier) {
        this.symbol = symbol;
        this.modifier = modifier;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public Modifier toModifier() {
        return modifier;
    }
    
    public static Visibility fromSymbol(String symbol) {
        for (Visibility v : values()) {
            if (v.symbol.equals(symbol)) {
                return v;
            }
        }
        return PUBLIC;
    }
}
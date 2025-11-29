package com.basiccode.generator.parser;

import com.basiccode.generator.model.Diagram;

public interface UmlParser<T extends Diagram> {
    T parse(String content);
    DiagramType getSupportedType();
    boolean canParse(String content);
}
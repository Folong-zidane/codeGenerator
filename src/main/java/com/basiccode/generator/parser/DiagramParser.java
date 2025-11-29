package com.basiccode.generator.parser;

import com.basiccode.generator.model.Diagram;

public interface DiagramParser {
    Diagram parse(String content);
    boolean canParse(String content);
}
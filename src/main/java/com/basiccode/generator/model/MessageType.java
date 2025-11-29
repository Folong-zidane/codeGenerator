package com.basiccode.generator.model;

/**
 * Types of messages in sequence diagrams
 */
public enum MessageType {
    SOLID_ARROW,        // ->
    DOTTED_ARROW,       // -->
    SOLID_ARROWHEAD,    // ->>
    DOTTED_ARROWHEAD,   // -->>
    BIDIRECTIONAL_SOLID, // <<->>
    BIDIRECTIONAL_DOTTED, // <<-->>
    SOLID_CROSS,        // -x
    DOTTED_CROSS,       // --x
    SOLID_ASYNC,        // -)
    DOTTED_ASYNC        // --)
}
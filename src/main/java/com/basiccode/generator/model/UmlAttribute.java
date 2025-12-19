package com.basiccode.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Alias pour UMLAttribute pour compatibilité
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UmlAttribute {
    private String name;
    private String type;
    private String visibility = "public";
    private String defaultValue;
    
    // Constructeur de compatibilité
    public UmlAttribute(String name, String type) {
        this.name = name;
        this.type = type;
        this.visibility = "public";
        this.defaultValue = null;
    }
    
    public String getJavaType() {
        return mapToJavaType(type);
    }
    
    public String getPythonType() {
        return mapToPythonType(type);
    }
    
    public String getCSharpType() {
        return mapToCSharpType(type);
    }
    
    public String getTypeScriptType() {
        return mapToTypeScriptType(type);
    }
    
    public String getPhpType() {
        return mapToPhpType(type);
    }
    
    private String mapToJavaType(String umlType) {
        if (umlType == null) return "String";
        switch (umlType.toLowerCase()) {
            case "string":
            case "varchar":
            case "text":
                return "String";
            case "integer":
            case "int":
                return "Integer";
            case "long":
                return "Long";
            case "float":
                return "Float";
            case "double":
            case "decimal":
                return "Double";
            case "boolean":
            case "bool":
                return "Boolean";
            case "datetime":
            case "localdatetime":
                return "LocalDateTime";
            case "uuid":
                return "UUID";
            default:
                return "String";
        }
    }
    
    private String mapToPythonType(String umlType) {
        if (umlType == null) return "str";
        switch (umlType.toLowerCase()) {
            case "string":
            case "varchar":
            case "text":
                return "str";
            case "integer":
            case "int":
                return "int";
            case "long":
                return "int";
            case "float":
                return "float";
            case "double":
            case "decimal":
                return "float";
            case "boolean":
            case "bool":
                return "bool";
            case "datetime":
            case "localdatetime":
                return "datetime";
            case "uuid":
                return "UUID";
            default:
                return "str";
        }
    }
    
    private String mapToCSharpType(String umlType) {
        if (umlType == null) return "string";
        switch (umlType.toLowerCase()) {
            case "string":
            case "varchar":
            case "text":
                return "string";
            case "integer":
            case "int":
                return "int";
            case "long":
                return "long";
            case "float":
                return "float";
            case "double":
            case "decimal":
                return "decimal";
            case "boolean":
            case "bool":
                return "bool";
            case "datetime":
            case "localdatetime":
                return "DateTime";
            case "uuid":
                return "Guid";
            default:
                return "string";
        }
    }
    
    private String mapToTypeScriptType(String umlType) {
        if (umlType == null) return "string";
        switch (umlType.toLowerCase()) {
            case "string":
            case "varchar":
            case "text":
                return "string";
            case "integer":
            case "int":
            case "long":
                return "number";
            case "float":
            case "double":
            case "decimal":
                return "number";
            case "boolean":
            case "bool":
                return "boolean";
            case "datetime":
            case "localdatetime":
                return "Date";
            case "uuid":
                return "string";
            default:
                return "string";
        }
    }
    
    private String mapToPhpType(String umlType) {
        if (umlType == null) return "string";
        switch (umlType.toLowerCase()) {
            case "string":
            case "varchar":
            case "text":
                return "string";
            case "integer":
            case "int":
                return "int";
            case "long":
                return "int";
            case "float":
            case "double":
            case "decimal":
                return "float";
            case "boolean":
            case "bool":
                return "bool";
            case "datetime":
            case "localdatetime":
                return "\\DateTime";
            case "uuid":
                return "string";
            default:
                return "string";
        }
    }
}
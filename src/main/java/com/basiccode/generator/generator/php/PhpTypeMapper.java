package com.basiccode.generator.generator.php;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PhpTypeMapper - Maps UML/Generic types to Laravel column types
 * 
 * Phase 2 Week 1 - TYPE EXPANSION
 * 
 * Supports 20+ Laravel column types:
 * - String types: string, text, email, url, slug, phone, uuid
 * - Numeric types: integer, bigInteger, smallInteger, decimal, float, double
 * - Date/Time types: date, time, timestamp, datetime
 * - Advanced types: json, binary, enum, uuid
 * - Special types: boolean, coordinates
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpTypeMapper {
    
    /**
     * Maps UML types to Laravel column types (20+ types)
     */
    private static final Map<String, String> LARAVEL_TYPE_MAPPING = new LinkedHashMap<>();
    
    /**
     * Maps types to migration code
     */
    private static final Map<String, String> MIGRATION_CODE_MAPPING = new LinkedHashMap<>();
    
    /**
     * Maps types to cast type
     */
    private static final Map<String, String> CAST_TYPE_MAPPING = new LinkedHashMap<>();
    
    static {
        // ==================== STRING TYPES ====================
        LARAVEL_TYPE_MAPPING.put("string", "string");
        LARAVEL_TYPE_MAPPING.put("String", "string");
        LARAVEL_TYPE_MAPPING.put("text", "text");
        LARAVEL_TYPE_MAPPING.put("Text", "text");
        LARAVEL_TYPE_MAPPING.put("longtext", "longText");
        LARAVEL_TYPE_MAPPING.put("slug", "string");
        LARAVEL_TYPE_MAPPING.put("Slug", "string");
        LARAVEL_TYPE_MAPPING.put("email", "string");
        LARAVEL_TYPE_MAPPING.put("Email", "string");
        LARAVEL_TYPE_MAPPING.put("url", "string");
        LARAVEL_TYPE_MAPPING.put("URL", "string");
        LARAVEL_TYPE_MAPPING.put("uri", "string");
        LARAVEL_TYPE_MAPPING.put("phone", "string");
        LARAVEL_TYPE_MAPPING.put("Phone", "string");
        LARAVEL_TYPE_MAPPING.put("tel", "string");
        
        // ==================== UUID TYPES ====================
        LARAVEL_TYPE_MAPPING.put("uuid", "uuid");
        LARAVEL_TYPE_MAPPING.put("UUID", "uuid");
        LARAVEL_TYPE_MAPPING.put("guid", "uuid");
        
        // ==================== NUMERIC TYPES ====================
        LARAVEL_TYPE_MAPPING.put("int", "integer");
        LARAVEL_TYPE_MAPPING.put("integer", "integer");
        LARAVEL_TYPE_MAPPING.put("Integer", "integer");
        LARAVEL_TYPE_MAPPING.put("long", "bigInteger");
        LARAVEL_TYPE_MAPPING.put("Long", "bigInteger");
        LARAVEL_TYPE_MAPPING.put("bigint", "bigInteger");
        LARAVEL_TYPE_MAPPING.put("BigInt", "bigInteger");
        LARAVEL_TYPE_MAPPING.put("short", "smallInteger");
        LARAVEL_TYPE_MAPPING.put("Short", "smallInteger");
        LARAVEL_TYPE_MAPPING.put("smallint", "smallInteger");
        LARAVEL_TYPE_MAPPING.put("float", "float");
        LARAVEL_TYPE_MAPPING.put("Float", "float");
        LARAVEL_TYPE_MAPPING.put("double", "double");
        LARAVEL_TYPE_MAPPING.put("Double", "double");
        LARAVEL_TYPE_MAPPING.put("decimal", "decimal");
        LARAVEL_TYPE_MAPPING.put("Decimal", "decimal");
        LARAVEL_TYPE_MAPPING.put("numeric", "decimal");
        LARAVEL_TYPE_MAPPING.put("money", "decimal");
        LARAVEL_TYPE_MAPPING.put("price", "decimal");
        
        // ==================== BOOLEAN ====================
        LARAVEL_TYPE_MAPPING.put("bool", "boolean");
        LARAVEL_TYPE_MAPPING.put("boolean", "boolean");
        LARAVEL_TYPE_MAPPING.put("Boolean", "boolean");
        LARAVEL_TYPE_MAPPING.put("flag", "boolean");
        
        // ==================== DATE/TIME TYPES ====================
        LARAVEL_TYPE_MAPPING.put("date", "date");
        LARAVEL_TYPE_MAPPING.put("Date", "date");
        LARAVEL_TYPE_MAPPING.put("time", "time");
        LARAVEL_TYPE_MAPPING.put("Time", "time");
        LARAVEL_TYPE_MAPPING.put("datetime", "dateTime");
        LARAVEL_TYPE_MAPPING.put("DateTime", "dateTime");
        LARAVEL_TYPE_MAPPING.put("timestamp", "timestamp");
        LARAVEL_TYPE_MAPPING.put("Timestamp", "timestamp");
        LARAVEL_TYPE_MAPPING.put("created_at", "timestamp");
        LARAVEL_TYPE_MAPPING.put("updated_at", "timestamp");
        
        // ==================== JSON/DATA TYPES ====================
        LARAVEL_TYPE_MAPPING.put("json", "json");
        LARAVEL_TYPE_MAPPING.put("JSON", "json");
        LARAVEL_TYPE_MAPPING.put("object", "json");
        LARAVEL_TYPE_MAPPING.put("Object", "json");
        LARAVEL_TYPE_MAPPING.put("jsonb", "jsonb");
        
        // ==================== BINARY ====================
        LARAVEL_TYPE_MAPPING.put("binary", "binary");
        LARAVEL_TYPE_MAPPING.put("Binary", "binary");
        LARAVEL_TYPE_MAPPING.put("blob", "binary");
        LARAVEL_TYPE_MAPPING.put("Blob", "binary");
        
        // ==================== ENUM ====================
        LARAVEL_TYPE_MAPPING.put("enum", "enum");
        LARAVEL_TYPE_MAPPING.put("Enum", "enum");
        
        // ==================== SPECIAL ====================
        LARAVEL_TYPE_MAPPING.put("coordinates", "json");
        LARAVEL_TYPE_MAPPING.put("point", "geometry");
        LARAVEL_TYPE_MAPPING.put("geometry", "geometry");
        
        // ==================== MIGRATION CODE ====================
        MIGRATION_CODE_MAPPING.put("string", "$table->string('{name}');");
        MIGRATION_CODE_MAPPING.put("text", "$table->text('{name}');");
        MIGRATION_CODE_MAPPING.put("longText", "$table->longText('{name}');");
        MIGRATION_CODE_MAPPING.put("uuid", "$table->uuid('{name}');");
        MIGRATION_CODE_MAPPING.put("integer", "$table->integer('{name}');");
        MIGRATION_CODE_MAPPING.put("bigInteger", "$table->bigInteger('{name}');");
        MIGRATION_CODE_MAPPING.put("smallInteger", "$table->smallInteger('{name}');");
        MIGRATION_CODE_MAPPING.put("float", "$table->float('{name}');");
        MIGRATION_CODE_MAPPING.put("double", "$table->double('{name}');");
        MIGRATION_CODE_MAPPING.put("decimal", "$table->decimal('{name}', 8, 2);");
        MIGRATION_CODE_MAPPING.put("boolean", "$table->boolean('{name}');");
        MIGRATION_CODE_MAPPING.put("date", "$table->date('{name}');");
        MIGRATION_CODE_MAPPING.put("time", "$table->time('{name}');");
        MIGRATION_CODE_MAPPING.put("dateTime", "$table->dateTime('{name}');");
        MIGRATION_CODE_MAPPING.put("timestamp", "$table->timestamp('{name}');");
        MIGRATION_CODE_MAPPING.put("json", "$table->json('{name}');");
        MIGRATION_CODE_MAPPING.put("jsonb", "$table->jsonb('{name}');");
        MIGRATION_CODE_MAPPING.put("binary", "$table->binary('{name}');");
        MIGRATION_CODE_MAPPING.put("enum", "$table->enum('{name}', [{values}]);");
        
        // ==================== CAST TYPES ====================
        CAST_TYPE_MAPPING.put("string", "'string'");
        CAST_TYPE_MAPPING.put("integer", "'integer'");
        CAST_TYPE_MAPPING.put("bigInteger", "'integer'");
        CAST_TYPE_MAPPING.put("smallInteger", "'integer'");
        CAST_TYPE_MAPPING.put("float", "'float'");
        CAST_TYPE_MAPPING.put("double", "'float'");
        CAST_TYPE_MAPPING.put("decimal", "'decimal:2'");
        CAST_TYPE_MAPPING.put("boolean", "'boolean'");
        CAST_TYPE_MAPPING.put("date", "'date'");
        CAST_TYPE_MAPPING.put("time", "'date:H:i'");
        CAST_TYPE_MAPPING.put("dateTime", "'datetime'");
        CAST_TYPE_MAPPING.put("timestamp", "'datetime'");
        CAST_TYPE_MAPPING.put("json", "'collection'");
        CAST_TYPE_MAPPING.put("jsonb", "'collection'");
        CAST_TYPE_MAPPING.put("uuid", "'string'");
        CAST_TYPE_MAPPING.put("binary", "'string'");
    }
    
    /**
     * Map UML/Generic type to Laravel column type
     */
    public String mapToLaravelType(String umlType) {
        if (umlType == null || umlType.isEmpty()) {
            return "string";
        }
        
        umlType = umlType.trim();
        
        // Direct lookup
        String laravelType = LARAVEL_TYPE_MAPPING.get(umlType);
        if (laravelType != null) {
            return laravelType;
        }
        
        // Case-insensitive lookup
        for (Map.Entry<String, String> entry : LARAVEL_TYPE_MAPPING.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(umlType)) {
                return entry.getValue();
            }
        }
        
        log.warn("Unknown type: {}, defaulting to string", umlType);
        return "string";
    }
    
    /**
     * Get migration code for type
     */
    public String getMigrationCode(String laravelType, String fieldName) {
        String template = MIGRATION_CODE_MAPPING.getOrDefault(laravelType, "$table->string('{name}');");
        return template.replace("{name}", fieldName);
    }
    
    /**
     * Get cast type for model
     */
    public String getCastType(String laravelType) {
        return CAST_TYPE_MAPPING.getOrDefault(laravelType, "'string'");
    }
    
    /**
     * Check if type needs special handling in migration
     */
    public boolean requiresSpecialHandling(String laravelType) {
        return laravelType.equals("decimal") || 
               laravelType.equals("enum") ||
               laravelType.equals("decimal");
    }
    
    /**
     * Get SQL type for a Laravel column type
     */
    public String toSqlType(String laravelType) {
        return switch (laravelType) {
            case "string" -> "varchar(255)";
            case "text" -> "text";
            case "longText" -> "longtext";
            case "uuid" -> "char(36)";
            case "integer" -> "int";
            case "bigInteger" -> "bigint";
            case "smallInteger" -> "smallint";
            case "float", "double" -> "float";
            case "decimal" -> "decimal(8,2)";
            case "boolean" -> "tinyint(1)";
            case "date" -> "date";
            case "time" -> "time";
            case "dateTime", "timestamp" -> "timestamp";
            case "json", "jsonb" -> "json";
            case "binary" -> "blob";
            case "enum" -> "enum";
            default -> "varchar(255)";
        };
    }
    
    /**
     * Check if type is numeric
     */
    public boolean isNumeric(String laravelType) {
        return laravelType.contains("Integer") || 
               laravelType.contains("float") || 
               laravelType.contains("double") ||
               laravelType.equals("decimal");
    }
    
    /**
     * Check if type is date/time
     */
    public boolean isDatetime(String laravelType) {
        return laravelType.equals("date") ||
               laravelType.equals("time") ||
               laravelType.equals("dateTime") ||
               laravelType.equals("timestamp");
    }
    
    /**
     * Get all supported types
     */
    public int getTotalSupportedTypes() {
        return LARAVEL_TYPE_MAPPING.size();
    }
    
    /**
     * Get all type names
     */
    public java.util.Set<String> getAllTypes() {
        return LARAVEL_TYPE_MAPPING.keySet();
    }
    
    /**
     * Map UML type to PHP type (alias for mapToLaravelType)
     */
    public String mapToPhpType(String umlType) {
        return mapToLaravelType(umlType);
    }
    
    /**
     * Map Laravel type to cast type for models
     */
    public String mapToCastType(String laravelType) {
        return getCastType(laravelType);
    }
}

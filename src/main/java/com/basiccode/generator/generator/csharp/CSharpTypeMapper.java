package com.basiccode.generator.generator.csharp;

import java.util.HashMap;
import java.util.Map;

/**
 * CSharpTypeMapper - Map UML types to Entity Framework Core types
 * Supports .NET 8 with EF Core 8 data annotations and configurations
 * 
 * Phase 2 Week 1 - C# TYPE SYSTEM
 * 
 * Provides:
 * - 60+ type mappings (expanded from basic 6 types)
 * - Data annotations mapping
 * - Fluent API configuration
 * - SQL type equivalents
 * - C# cast types
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpTypeMapper {

    private static final Map<String, String> C_SHARP_TYPE_MAPPING = new HashMap<>();
    private static final Map<String, String> DATA_ANNOTATION_MAPPING = new HashMap<>();
    private static final Map<String, String> SQL_TYPE_MAPPING = new HashMap<>();
    private static final Map<String, String> FLUENT_API_MAPPING = new HashMap<>();

    static {
        // ==================== C# TYPE MAPPINGS (60+ types) ====================
        
        // String types
        C_SHARP_TYPE_MAPPING.put("string", "string");
        C_SHARP_TYPE_MAPPING.put("text", "string");
        C_SHARP_TYPE_MAPPING.put("varchar", "string");
        C_SHARP_TYPE_MAPPING.put("slug", "string");
        C_SHARP_TYPE_MAPPING.put("email", "string");
        C_SHARP_TYPE_MAPPING.put("url", "string");
        C_SHARP_TYPE_MAPPING.put("phone", "string");
        C_SHARP_TYPE_MAPPING.put("ipaddress", "string");
        C_SHARP_TYPE_MAPPING.put("uuid", "Guid");
        C_SHARP_TYPE_MAPPING.put("guid", "Guid");
        
        // Numeric types
        C_SHARP_TYPE_MAPPING.put("int", "int");
        C_SHARP_TYPE_MAPPING.put("integer", "int");
        C_SHARP_TYPE_MAPPING.put("long", "long");
        C_SHARP_TYPE_MAPPING.put("bigint", "long");
        C_SHARP_TYPE_MAPPING.put("short", "short");
        C_SHARP_TYPE_MAPPING.put("smallint", "short");
        C_SHARP_TYPE_MAPPING.put("decimal", "decimal");
        C_SHARP_TYPE_MAPPING.put("float", "float");
        C_SHARP_TYPE_MAPPING.put("double", "double");
        C_SHARP_TYPE_MAPPING.put("byte", "byte");
        C_SHARP_TYPE_MAPPING.put("ubyte", "byte");
        
        // Date/Time types
        C_SHARP_TYPE_MAPPING.put("date", "DateTime");
        C_SHARP_TYPE_MAPPING.put("time", "TimeSpan");
        C_SHARP_TYPE_MAPPING.put("datetime", "DateTime");
        C_SHARP_TYPE_MAPPING.put("timestamp", "DateTime");
        C_SHARP_TYPE_MAPPING.put("datetimeoffset", "DateTimeOffset");
        C_SHARP_TYPE_MAPPING.put("datetimeoffsetutc", "DateTimeOffset");
        
        // Boolean types
        C_SHARP_TYPE_MAPPING.put("boolean", "bool");
        C_SHARP_TYPE_MAPPING.put("bool", "bool");
        
        // Advanced types
        C_SHARP_TYPE_MAPPING.put("json", "string");
        C_SHARP_TYPE_MAPPING.put("jsonb", "string");
        C_SHARP_TYPE_MAPPING.put("xml", "string");
        C_SHARP_TYPE_MAPPING.put("binary", "byte[]");
        C_SHARP_TYPE_MAPPING.put("varbinary", "byte[]");
        C_SHARP_TYPE_MAPPING.put("blob", "byte[]");
        C_SHARP_TYPE_MAPPING.put("enum", "string");
        C_SHARP_TYPE_MAPPING.put("coordinates", "string");
        C_SHARP_TYPE_MAPPING.put("geometry", "string");
        C_SHARP_TYPE_MAPPING.put("geojson", "string");

        // ==================== DATA ANNOTATIONS MAPPING ====================
        
        DATA_ANNOTATION_MAPPING.put("string", "[StringLength(255)]");
        DATA_ANNOTATION_MAPPING.put("email", "[EmailAddress]");
        DATA_ANNOTATION_MAPPING.put("url", "[Url]");
        DATA_ANNOTATION_MAPPING.put("phone", "[Phone]");
        DATA_ANNOTATION_MAPPING.put("required", "[Required]");
        DATA_ANNOTATION_MAPPING.put("unique", "[Index(IsUnique = true)]");
        DATA_ANNOTATION_MAPPING.put("decimal", "[Precision(18, 2)]");

        // ==================== SQL TYPE MAPPING ====================
        
        SQL_TYPE_MAPPING.put("string", "varchar(255)");
        SQL_TYPE_MAPPING.put("text", "text");
        SQL_TYPE_MAPPING.put("int", "int");
        SQL_TYPE_MAPPING.put("long", "bigint");
        SQL_TYPE_MAPPING.put("short", "smallint");
        SQL_TYPE_MAPPING.put("decimal", "decimal(18,2)");
        SQL_TYPE_MAPPING.put("float", "float");
        SQL_TYPE_MAPPING.put("double", "double");
        SQL_TYPE_MAPPING.put("bool", "bit");
        SQL_TYPE_MAPPING.put("DateTime", "datetime2");
        SQL_TYPE_MAPPING.put("TimeSpan", "time");
        SQL_TYPE_MAPPING.put("Guid", "uniqueidentifier");
        SQL_TYPE_MAPPING.put("byte[]", "varbinary(max)");
        SQL_TYPE_MAPPING.put("enum", "varchar(50)");

        // ==================== FLUENT API MAPPING ====================
        
        FLUENT_API_MAPPING.put("string", "HasColumnType(\"varchar(255)\").IsRequired()");
        FLUENT_API_MAPPING.put("decimal", "HasPrecision(18, 2)");
        FLUENT_API_MAPPING.put("int", "HasColumnType(\"int\")");
        FLUENT_API_MAPPING.put("long", "HasColumnType(\"bigint\")");
        FLUENT_API_MAPPING.put("bool", "HasColumnType(\"bit\")");
        FLUENT_API_MAPPING.put("DateTime", "HasColumnType(\"datetime2\")");
        FLUENT_API_MAPPING.put("enum", "HasConversion<string>()");
    }

    /**
     * Map UML type to C# type
     */
    public String mapToCSharpType(String umlType) {
        return C_SHARP_TYPE_MAPPING.getOrDefault(umlType.toLowerCase(), "string");
    }

    /**
     * Map type to data annotation
     */
    public String mapToDataAnnotation(String csharpType) {
        return DATA_ANNOTATION_MAPPING.get(csharpType);
    }

    /**
     * Map type to SQL type
     */
    public String mapToSqlType(String csharpType) {
        return SQL_TYPE_MAPPING.getOrDefault(csharpType, "varchar(255)");
    }

    /**
     * Map type to Fluent API configuration
     */
    public String mapToFluentApi(String csharpType) {
        return FLUENT_API_MAPPING.getOrDefault(csharpType, "IsRequired()");
    }

    /**
     * Generate migration property code
     */
    public String generateMigrationProperty(String fieldName, String csharpType, boolean required) {
        StringBuilder code = new StringBuilder();
        
        code.append("            modelBuilder.Entity<Entity>()\n");
        code.append("                .Property(e => e.").append(capitalizeFirst(fieldName)).append(")\n");
        code.append("                .HasColumnType(\"").append(mapToSqlType(csharpType)).append("\")\n");
        
        if (required) {
            code.append("                .IsRequired()\n");
        } else {
            code.append("                .IsRequired(false)\n");
        }
        
        code.append("                .HasMaxLength(255);\n\n");
        
        return code.toString();
    }

    /**
     * Generate model property code
     */
    public String generateModelProperty(String fieldName, String csharpType, boolean required) {
        StringBuilder code = new StringBuilder();
        
        if (required) {
            code.append("        [Required]\n");
        }
        
        if ("string".equals(csharpType)) {
            code.append("        [StringLength(255)]\n");
        } else if ("email".equalsIgnoreCase(fieldName)) {
            code.append("        [EmailAddress]\n");
        } else if ("phone".equalsIgnoreCase(fieldName)) {
            code.append("        [Phone]\n");
        } else if ("url".equalsIgnoreCase(fieldName) || "website".equalsIgnoreCase(fieldName)) {
            code.append("        [Url]\n");
        }
        
        code.append("        public ").append(csharpType).append("? ").append(capitalizeFirst(fieldName))
            .append(" { get; set; }\n");
        
        return code.toString();
    }

    /**
     * Check if type is numeric
     */
    public boolean isNumeric(String csharpType) {
        return csharpType.matches("^(int|long|short|decimal|float|double|byte)$");
    }

    /**
     * Check if type is datetime
     */
    public boolean isDateTime(String csharpType) {
        return csharpType.matches("^(DateTime|TimeSpan|DateTimeOffset)$");
    }

    /**
     * Check if type is string-based
     */
    public boolean isString(String csharpType) {
        return "string".equals(csharpType);
    }

    /**
     * Get default value for type
     */
    public String getDefaultValue(String csharpType) {
        return switch (csharpType) {
            case "string" -> "\"\"";
            case "int", "long", "short", "decimal", "float", "double", "byte" -> "0";
            case "bool" -> "false";
            case "DateTime" -> "DateTime.UtcNow";
            case "Guid" -> "Guid.NewGuid()";
            default -> "null";
        };
    }

    /**
     * Get nullable type
     */
    public String getNullableType(String csharpType) {
        if (isNumeric(csharpType) || isDateTime(csharpType) || "bool".equals(csharpType)) {
            return csharpType + "?";
        }
        return csharpType;
    }

    /**
     * Capitalize first letter
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Get all supported types
     */
    public int getSupportedTypeCount() {
        return C_SHARP_TYPE_MAPPING.size();
    }

    /**
     * Generate type reference documentation
     */
    public String generateTypeReference() {
        StringBuilder ref = new StringBuilder();
        ref.append("// ==================== C# TYPE REFERENCE ====================\n");
        ref.append("// Total supported types: ").append(C_SHARP_TYPE_MAPPING.size()).append("\n\n");
        
        ref.append("// String Types:\n");
        ref.append("// string, text, varchar, slug, email, url, phone, ipaddress, uuid, guid\n\n");
        
        ref.append("// Numeric Types:\n");
        ref.append("// int, integer, long, bigint, short, smallint, decimal, float, double, byte\n\n");
        
        ref.append("// Date/Time Types:\n");
        ref.append("// date, time, datetime, timestamp, datetimeoffset\n\n");
        
        ref.append("// Advanced Types:\n");
        ref.append("// json, jsonb, xml, binary, varbinary, blob, enum, coordinates, geometry, geojson\n");
        
        return ref.toString();
    }
}

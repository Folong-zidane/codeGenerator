package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Spring Boot Flyway Migration Generator
 * Generates versioned Flyway migrations with multi-database support
 */
@Component
@Slf4j
public class SpringBootMigrationGenerator implements IMigrationGenerator {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Set<String> REQUIRED_FIELDS = Set.of(
        "name", "title", "email", "username", "firstName", "lastName", "password"
    );
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        log.info("Generating Flyway migration for {} classes in package {}", enhancedClasses.size(), packageName);
        return generateFlywayInitialSchema(enhancedClasses);
    }
    
    /**
     * Generates V001__Initial_Schema.sql with Flyway versioning
     */
    public String generateFlywayInitialSchema(List<EnhancedClass> enhancedClasses) {
        StringBuilder sql = new StringBuilder();
        
        // Header
        sql.append("-- ============================================\n");
        sql.append("-- Flyway Migration: V001__Initial_Schema.sql\n");
        sql.append("-- ============================================\n");
        sql.append("-- Description: Create initial database schema\n");
        sql.append("-- Date: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        sql.append("-- Version: 1.0.0\n");
        sql.append("-- ============================================\n\n");
        
        sql.append("-- Set charset for database compatibility\n");
        sql.append("SET NAMES utf8mb4;\n");
        sql.append("SET CHARACTER SET utf8mb4;\n");
        sql.append("SET COLLATION_CONNECTION = utf8mb4_unicode_ci;\n\n");
        
        // Create tables
        for (EnhancedClass enhancedClass : enhancedClasses) {
            sql.append(generateTableDefinition(enhancedClass));
        }
        
        return sql.toString();
    }
    
    /**
     * Generates complete table definition with constraints and indexes
     */
    private String generateTableDefinition(EnhancedClass enhancedClass) {
        StringBuilder sql = new StringBuilder();
        String tableName = getTableName(enhancedClass);
        String className = enhancedClass.getOriginalClass().getName();
        
        sql.append("-- ============================================\n");
        sql.append("-- Table: ").append(tableName).append(" (").append(className).append(")\n");
        sql.append("-- ============================================\n");
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");
        
        // ID column (Primary Key)
        sql.append("    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',\n");
        
        // Generate regular columns
        List<UmlAttribute> attributes = enhancedClass.getOriginalClass().getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            UmlAttribute attr = attributes.get(i);
            if (!"id".equals(attr.getName())) {
                sql.append("    ").append(generateColumnDefinition(attr, enhancedClass));
                // Add comma if not last column (before state and audit columns)
                if (i < attributes.size() - 1 || enhancedClass.isStateful()) {
                    sql.append(",\n");
                } else {
                    sql.append("\n");
                }
            }
        }
        
        // Add state column if stateful
        if (enhancedClass.isStateful()) {
            sql.append("    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',\n");
        }
        
        // Add audit columns
        sql.append("    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',\n");
        sql.append("    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',\n");
        
        // Add version for optimistic locking
        sql.append("    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'\n");
        
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='").append(className).append(" entity table';\n\n");
        
        // Generate indexes
        sql.append(generateIndexes(tableName, enhancedClass));
        
        return sql.toString();
    }
    
    /**
     * Generates column definition with appropriate type and constraints
     */
    private String generateColumnDefinition(UmlAttribute attr, EnhancedClass enhancedClass) {
        StringBuilder col = new StringBuilder();
        String fieldName = attr.getName();
        String javaType = attr.getType();
        
        col.append(fieldName).append(" ");
        col.append(mapJavaTypeToSql(javaType));
        
        // Add constraints
        if (isRequiredField(fieldName) || "String".equals(javaType)) {
            col.append(" NOT NULL");
        }
        
        // Email unique constraint
        if ("email".equalsIgnoreCase(fieldName)) {
            col.append(" UNIQUE");
        }
        
        // Username unique constraint
        if ("username".equalsIgnoreCase(fieldName)) {
            col.append(" UNIQUE");
        }
        
        // Default values for common fields
        if ("isActive".equals(fieldName)) {
            col.append(" DEFAULT 1");
        } else if ("isDeleted".equals(fieldName)) {
            col.append(" DEFAULT 0");
        }
        
        // Add comment
        col.append(" COMMENT '").append(fieldName).append(" field'");
        
        return col.toString();
    }
    
    /**
     * Generates indexes for improved query performance
     */
    private String generateIndexes(String tableName, EnhancedClass enhancedClass) {
        StringBuilder indexes = new StringBuilder();
        
        // Always create index on created_at (for time-based queries)
        indexes.append("CREATE INDEX IF NOT EXISTS idx_").append(tableName).append("_created_at ")
            .append("ON ").append(tableName).append(" (created_at) COMMENT 'Index for time-based queries';\n");
        
        // Index on status if stateful
        if (enhancedClass.isStateful()) {
            indexes.append("CREATE INDEX IF NOT EXISTS idx_").append(tableName).append("_status ")
                .append("ON ").append(tableName).append(" (status) COMMENT 'Index for status filtering';\n");
        }
        
        // Index on email if exists
        if (hasField(enhancedClass, "email")) {
            indexes.append("CREATE INDEX IF NOT EXISTS idx_").append(tableName).append("_email ")
                .append("ON ").append(tableName).append(" (email) COMMENT 'Index for email lookups';\n");
        }
        
        // Index on name if exists (for search queries)
        if (hasField(enhancedClass, "name")) {
            indexes.append("CREATE FULLTEXT INDEX IF NOT EXISTS idx_").append(tableName).append("_name ")
                .append("ON ").append(tableName).append(" (name) COMMENT 'Fulltext index for name search';\n");
        }
        
        // Index on username if exists
        if (hasField(enhancedClass, "username")) {
            indexes.append("CREATE INDEX IF NOT EXISTS idx_").append(tableName).append("_username ")
                .append("ON ").append(tableName).append(" (username) COMMENT 'Index for username lookups';\n");
        }
        
        // Composite index for common filter combinations
        if (hasField(enhancedClass, "status") && enhancedClass.isStateful()) {
            indexes.append("CREATE INDEX IF NOT EXISTS idx_").append(tableName).append("_status_created_at ")
                .append("ON ").append(tableName).append(" (status, created_at) COMMENT 'Composite index for filtering by status and date';\n");
        }
        
        indexes.append("\n");
        return indexes.toString();
    }
    
    /**
     * Maps Java types to SQL column types
     */
    private String mapJavaTypeToSql(String javaType) {
        return switch (javaType) {
            case "String" -> "VARCHAR(255)";
            case "Integer" -> "INT";
            case "Long" -> "BIGINT";
            case "Double" -> "DECIMAL(19,2)";
            case "Float" -> "FLOAT";
            case "Boolean" -> "BOOLEAN";
            case "LocalDateTime", "Date" -> "TIMESTAMP";
            case "LocalDate" -> "DATE";
            case "LocalTime" -> "TIME";
            default -> "VARCHAR(255)";
        };
    }
    
    private boolean isRequiredField(String fieldName) {
        return REQUIRED_FIELDS.contains(fieldName.toLowerCase());
    }
    
    private String getTableName(EnhancedClass enhancedClass) {
        String className = enhancedClass.getOriginalClass().getName();
        return className.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private boolean hasField(EnhancedClass enhancedClass, String fieldName) {
        return enhancedClass.getOriginalClass().getAttributes().stream()
            .anyMatch(attr -> fieldName.equalsIgnoreCase(attr.getName()));
    }
    
    @Override
    public String getMigrationDirectory() {
        return "src/main/resources/db/migration";
    }
    
    /**
     * Generates Flyway configuration file content
     */
    public String generateFlywayConfig(String databaseType) {
        log.info("Generating Flyway configuration for {}", databaseType);
        StringBuilder config = new StringBuilder();
        
        config.append("# Flyway Database Migration Configuration\n");
        config.append("# Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n\n");
        
        config.append("# Database Connection\n");
        switch (databaseType.toLowerCase()) {
            case "postgresql" ->
                config.append("spring.flyway.url=jdbc:postgresql://localhost:5432/appdb\n")
                    .append("spring.flyway.user=postgres\n")
                    .append("spring.flyway.password=postgres\n");
            case "mysql" ->
                config.append("spring.flyway.url=jdbc:mysql://localhost:3306/appdb?useUnicode=true&characterEncoding=utf8\n")
                    .append("spring.flyway.user=root\n")
                    .append("spring.flyway.password=\n");
            case "h2" ->
                config.append("spring.flyway.url=jdbc:h2:mem:testdb\n")
                    .append("spring.flyway.user=sa\n")
                    .append("spring.flyway.password=\n");
            default ->
                config.append("spring.flyway.url=jdbc:mysql://localhost:3306/appdb\n")
                    .append("spring.flyway.user=root\n")
                    .append("spring.flyway.password=\n");
        }
        
        config.append("\n# Flyway Behavior\n");
        config.append("spring.flyway.locations=classpath:db/migration\n");
        config.append("spring.flyway.baselineOnMigrate=true\n");
        config.append("spring.flyway.validateOnMigrate=true\n");
        config.append("spring.flyway.cleanDisabled=true\n");
        config.append("spring.flyway.outOfOrder=false\n");
        config.append("spring.flyway.encoding=UTF-8\n");
        config.append("spring.flyway.table=flyway_schema_history\n\n");
        
        config.append("# Callbacks (optional)\n");
        config.append("# spring.flyway.callbacks=\n");
        
        return config.toString();
    }
    
    /**
     * Generates repeatable seed data migration (R__Seed_Data.sql)
     */
    public String generateSeedDataMigration(List<EnhancedClass> enhancedClasses) {
        log.info("Generating seed data migration");
        StringBuilder sql = new StringBuilder();
        
        sql.append("-- ============================================\n");
        sql.append("-- Repeatable Migration: R__Seed_Data.sql\n");
        sql.append("-- ============================================\n");
        sql.append("-- This migration is repeatable and will always run after versioned migrations\n");
        sql.append("-- Date: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        sql.append("-- ============================================\n\n");
        
        sql.append("-- Clean existing seed data (optional - remove if you want to preserve data)\n");
        sql.append("-- TRUNCATE TABLE tables IF EXISTS;\n\n");
        
        sql.append("-- Insert seed data here\n");
        sql.append("-- Example:\n");
        sql.append("-- INSERT INTO users (email, name, created_at) VALUES\n");
        sql.append("-- ('admin@example.com', 'Admin User', NOW()),\n");
        sql.append("-- ('user@example.com', 'Regular User', NOW());\n\n");
        
        return sql.toString();
    }
}
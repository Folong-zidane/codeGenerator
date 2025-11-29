package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;

/**
 * Spring Boot migration generator
 */
public class SpringBootMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("-- Generated database migration\n");
        sql.append("-- Package: ").append(packageName).append("\n\n");
        
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String tableName = enhancedClass.getOriginalClass().getName().toLowerCase() + "s";
            
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");
            
            // Generate columns
            for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                sql.append("    ").append(attr.getName()).append(" ");
                sql.append(mapJavaTypeToSql(attr.getType()));
                
                if ("id".equals(attr.getName())) {
                    sql.append(" PRIMARY KEY AUTO_INCREMENT");
                }
                
                sql.append(",\n");
            }
            
            // Add state column if stateful
            if (enhancedClass.isStateful()) {
                sql.append("    status VARCHAR(50),\n");
            }
            
            // Add audit columns
            sql.append("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n");
            sql.append("    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n");
            
            sql.append(");\n\n");
        }
        
        return sql.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "db/migration";
    }
    
    private String mapJavaTypeToSql(String javaType) {
        switch (javaType.toLowerCase()) {
            case "string": return "VARCHAR(255)";
            case "integer": case "int": return "INT";
            case "long": return "BIGINT";
            case "double": case "float": return "DECIMAL(10,2)";
            case "boolean": return "BOOLEAN";
            case "localdatetime": return "TIMESTAMP";
            case "localdate": return "DATE";
            default: return "VARCHAR(255)";
        }
    }
}
package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

public class PythonMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> classes, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("# Database migration for ").append(packageName).append("\n");
        code.append("from alembic import op\n");
        code.append("import sqlalchemy as sa\n\n");
        
        code.append("def upgrade():\n");
        
        for (EnhancedClass clazz : classes) {
            String className = clazz.getOriginalClass().getName();
            String tableName = className.toLowerCase() + "s";
            
            code.append("    # Create ").append(className).append(" table\n");
            code.append("    op.create_table('").append(tableName).append("',\n");
            code.append("        sa.Column('id', sa.Integer(), primary_key=True, autoincrement=True),\n");
            
            // Add attributes
            clazz.getOriginalClass().getAttributes().forEach(attr -> {
                if (!"id".equals(attr.getName())) {
                    String sqlType = mapToSQLType(attr.getType());
                    code.append("        sa.Column('").append(attr.getName()).append("', ").append(sqlType).append("),\n");
                }
            });
            
            // Add state column if stateful
            if (clazz.isStateful()) {
                code.append("        sa.Column('status', sa.String(50), default='ACTIVE'),\n");
            }
            
            code.append("        sa.Column('created_at', sa.DateTime(), default=sa.func.now()),\n");
            code.append("        sa.Column('updated_at', sa.DateTime(), default=sa.func.now(), onupdate=sa.func.now())\n");
            code.append("    )\n\n");
        }
        
        code.append("def downgrade():\n");
        for (EnhancedClass clazz : classes) {
            String tableName = clazz.getOriginalClass().getName().toLowerCase() + "s";
            code.append("    op.drop_table('").append(tableName).append("')\n");
        }
        
        return code.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "migrations";
    }
    
    private String mapToSQLType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "long", "integer", "int" -> "sa.Integer()";
            case "string" -> "sa.String(255)";
            case "float", "double" -> "sa.Float()";
            case "boolean" -> "sa.Boolean()";
            default -> "sa.String(255)";
        };
    }
}
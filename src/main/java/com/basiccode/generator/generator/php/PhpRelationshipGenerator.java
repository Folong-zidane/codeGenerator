package com.basiccode.generator.generator.php;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PhpRelationshipGenerator - Generates Laravel relationship methods
 * 
 * Phase 2 Week 2 - RELATIONSHIPS
 * 
 * Supports:
 * - OneToMany (hasMany)
 * - ManyToMany (belongsToMany)
 * - OneToOne (hasOne, belongsTo)
 * - Eager loading optimization
 * - Foreign keys and pivot tables
 * 
 * Generates:
 * - Relationship methods in models
 * - Migration foreign key constraints
 * - Pivot table migrations
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpRelationshipGenerator {
    
    /**
     * Generate all relationship methods for a model
     */
    public String generateRelationships(PhpModelParser.PhpModelDefinition model) {
        StringBuilder code = new StringBuilder();
        
        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }
        
        code.append("\n    // ==================== RELATIONSHIPS ====================\n\n");
        
        for (PhpModelParser.PhpRelationshipDefinition rel : model.getRelationships()) {
            code.append(generateRelationshipMethod(rel));
        }
        
        return code.toString();
    }
    
    /**
     * Generate individual relationship method based on type
     */
    private String generateRelationshipMethod(PhpModelParser.PhpRelationshipDefinition rel) {
        return switch (rel.getType()) {
            case ONE_TO_MANY -> generateOneToMany(rel);
            case MANY_TO_MANY -> generateManyToMany(rel);
            case ONE_TO_ONE -> generateOneToOne(rel);
            default -> "";
        };
    }
    
    /**
     * Generate OneToMany relationship (hasMany)
     * 
     * Example: User hasMany Posts
     */
    private String generateOneToMany(PhpModelParser.PhpRelationshipDefinition rel) {
        String methodName = pluralize(lcFirst(rel.getTargetModel()));
        String foreignKey = lcFirst(rel.getSourceModel()) + "_id";
        
        return String.format("""
            /**
             * Get all %s for this %s (OneToMany relationship)
             */
            public function %s(): \\Illuminate\\Database\\Eloquent\\Relations\\HasMany
            {
                return $this->hasMany(%s::class, '%s', 'id');
            }
            
            """,
            methodName, rel.getSourceModel(),
            methodName, rel.getTargetModel(), foreignKey
        );
    }
    
    /**
     * Generate ManyToMany relationship (belongsToMany)
     * 
     * Example: User belongsToMany Roles through role_user pivot table
     */
    private String generateManyToMany(PhpModelParser.PhpRelationshipDefinition rel) {
        String methodName = pluralize(lcFirst(rel.getTargetModel()));
        String pivotTable = getPivotTableName(rel.getSourceModel(), rel.getTargetModel());
        String sourceKey = lcFirst(rel.getSourceModel()) + "_id";
        String targetKey = lcFirst(rel.getTargetModel()) + "_id";
        
        return String.format("""
            /**
             * Get all %s for this %s (ManyToMany relationship)
             */
            public function %s(): \\Illuminate\\Database\\Eloquent\\Relations\\BelongsToMany
            {
                return $this->belongsToMany(
                    %s::class,
                    '%s',
                    '%s',
                    '%s'
                )%s;
            }
            
            """,
            methodName, rel.getSourceModel(),
            methodName, rel.getTargetModel(), pivotTable, sourceKey, targetKey,
            rel.isEager() ? ".with(['pivot'])" : ""
        );
    }
    
    /**
     * Generate OneToOne relationship
     * 
     * Example: User hasOne Profile or Comment belongsTo Post
     */
    private String generateOneToOne(PhpModelParser.PhpRelationshipDefinition rel) {
        String methodName = lcFirst(rel.getTargetModel());
        String foreignKey = lcFirst(rel.getSourceModel()) + "_id";
        
        return String.format("""
            /**
             * Get the %s for this %s (OneToOne relationship)
             */
            public function %s(): \\Illuminate\\Database\\Eloquent\\Relations\\HasOne
            {
                return $this->hasOne(%s::class, '%s', 'id');
            }
            
            """,
            methodName, rel.getSourceModel(),
            methodName, rel.getTargetModel(), foreignKey
        );
    }
    
    /**
     * Generate migration code for relationship foreign keys
     */
    public String generateRelationshipMigrations(PhpModelParser.PhpModelDefinition model) {
        StringBuilder code = new StringBuilder();
        
        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }
        
        for (PhpModelParser.PhpRelationshipDefinition rel : model.getRelationships()) {
            if (rel.getType() == PhpModelParser.PhpRelationshipDefinition.RelationType.ONE_TO_MANY) {
                code.append(generateOneToManyMigration(rel));
            } else if (rel.getType() == PhpModelParser.PhpRelationshipDefinition.RelationType.MANY_TO_MANY) {
                code.append(generateManyToManyMigration(rel));
            } else if (rel.getType() == PhpModelParser.PhpRelationshipDefinition.RelationType.ONE_TO_ONE) {
                code.append(generateOneToOneMigration(rel));
            }
        }
        
        return code.toString();
    }
    
    /**
     * Generate foreign key migration for OneToMany
     */
    private String generateOneToManyMigration(PhpModelParser.PhpRelationshipDefinition rel) {
        String tableName = toTableName(rel.getTargetModel());
        String foreignKey = lcFirst(rel.getSourceModel()) + "_id";
        String foreignTable = toTableName(rel.getSourceModel());
        
        return String.format("""
            // Foreign key for %s -> %s relationship
            $table->foreignId('%s')
                ->constrained('%s')
                ->onDelete('cascade')
                ->onUpdate('cascade');
            
            """,
            rel.getSourceModel(), rel.getTargetModel(),
            foreignKey, foreignTable
        );
    }
    
    /**
     * Generate pivot table migration for ManyToMany
     */
    private String generateManyToManyMigration(PhpModelParser.PhpRelationshipDefinition rel) {
        String pivotTable = getPivotTableName(rel.getSourceModel(), rel.getTargetModel());
        String sourceKey = lcFirst(rel.getSourceModel()) + "_id";
        String targetKey = lcFirst(rel.getTargetModel()) + "_id";
        String sourceTable = toTableName(rel.getSourceModel());
        String targetTable = toTableName(rel.getTargetModel());
        
        return String.format("""
            /**
             * Create pivot table for %s <-> %s ManyToMany relationship
             */
            Schema::create('%s', function (Blueprint $table) {
                $table->id();
                
                // Foreign keys
                $table->foreignId('%s')
                    ->constrained('%s')
                    ->onDelete('cascade')
                    ->onUpdate('cascade');
                    
                $table->foreignId('%s')
                    ->constrained('%s')
                    ->onDelete('cascade')
                    ->onUpdate('cascade');
                
                // Timestamps
                $table->timestamp('created_at')->useCurrent();
                $table->timestamp('updated_at')->useCurrent();
                
                // Composite unique key
                $table->unique(['%s', '%s']);
            });
            
            """,
            rel.getSourceModel(), rel.getTargetModel(),
            pivotTable,
            sourceKey, sourceTable,
            targetKey, targetTable,
            sourceKey, targetKey
        );
    }
    
    /**
     * Generate foreign key migration for OneToOne
     */
    private String generateOneToOneMigration(PhpModelParser.PhpRelationshipDefinition rel) {
        String tableName = toTableName(rel.getTargetModel());
        String foreignKey = lcFirst(rel.getSourceModel()) + "_id";
        String foreignTable = toTableName(rel.getSourceModel());
        
        return String.format("""
            // Foreign key for %s -> %s OneToOne relationship
            $table->foreignId('%s')
                ->unique()
                ->constrained('%s')
                ->onDelete('cascade')
                ->onUpdate('cascade');
            
            """,
            rel.getSourceModel(), rel.getTargetModel(),
            foreignKey, foreignTable
        );
    }
    
    /**
     * Generate Repository method for eager loading relationships
     */
    public String generateRepositoryRelationshipMethod(PhpModelParser.PhpModelDefinition model) {
        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }
        
        StringBuilder code = new StringBuilder();
        code.append("\n    /**\n");
        code.append("     * Get all records with eager loaded relationships\n");
        code.append("     */\n");
        code.append("    public function getAllWithRelationships()\n");
        code.append("    {\n");
        code.append("        return ").append(model.getName()).append("::with([\n");
        
        for (int i = 0; i < model.getRelationships().size(); i++) {
            PhpModelParser.PhpRelationshipDefinition rel = model.getRelationships().get(i);
            String methodName = pluralize(lcFirst(rel.getTargetModel()));
            code.append("            '").append(methodName).append("'");
            if (i < model.getRelationships().size() - 1) {
                code.append(",");
            }
            code.append("\n");
        }
        
        code.append("        ])->get();\n");
        code.append("    }\n");
        
        return code.toString();
    }
    
    /**
     * Get pivot table name for ManyToMany relationship
     */
    private String getPivotTableName(String model1, String model2) {
        String table1 = toTableName(model1).replaceAll("s$", "");
        String table2 = toTableName(model2).replaceAll("s$", "");
        
        // Alphabetical order for consistency
        if (table1.compareTo(table2) > 0) {
            return table2 + "_" + table1;
        }
        return table1 + "_" + table2;
    }
    
    /**
     * Convert model name to table name
     */
    private String toTableName(String modelName) {
        String snakeCase = modelName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        if (snakeCase.endsWith("y")) {
            return snakeCase.substring(0, snakeCase.length() - 1) + "ies";
        }
        return snakeCase + "s";
    }
    
    /**
     * Pluralize word
     */
    private String pluralize(String word) {
        if (word.endsWith("y")) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        return word + "s";
    }
    
    /**
     * Lowercase first character
     */
    private String lcFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}

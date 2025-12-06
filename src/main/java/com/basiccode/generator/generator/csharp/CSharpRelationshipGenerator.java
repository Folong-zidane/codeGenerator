package com.basiccode.generator.generator.csharp;

import java.util.ArrayList;
import java.util.List;

/**
 * CSharpRelationshipGenerator - Generate Entity Framework Core relationships
 * Supports One-to-Many, Many-to-Many, and One-to-One relationships with navigation properties
 * 
 * Phase 2 Week 2 - C# RELATIONSHIPS
 * 
 * Generates:
 * - Navigation properties in models
 * - Fluent API relationship configurations
 * - Migration foreign key constraints
 * - Pivot/Join table configurations
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpRelationshipGenerator {

    /**
     * Generate all relationships for a model
     */
    public String generateRelationships(CSharpModelParser.CSharpModelDefinition model) {
        StringBuilder code = new StringBuilder();

        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }

        code.append("        // ==================== RELATIONSHIPS ====================\n\n");

        for (CSharpModelParser.CSharpRelationshipDefinition rel : model.getRelationships()) {
            code.append(generateRelationshipMethod(rel));
        }

        return code.toString();
    }

    /**
     * Generate individual relationship method based on type
     */
    private String generateRelationshipMethod(CSharpModelParser.CSharpRelationshipDefinition rel) {
        return switch (rel.getType()) {
            case ONE_TO_MANY -> generateOneToMany(rel);
            case MANY_TO_MANY -> generateManyToMany(rel);
            case ONE_TO_ONE -> generateOneToOne(rel);
        };
    }

    /**
     * Generate OneToMany relationship (ICollection navigation property)
     * Example: User has many Posts
     */
    private String generateOneToMany(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String navProperty = pluralize(targetModel);
        String foreignKey = sourceModel + "Id";

        code.append("        /// <summary>\n");
        code.append("        /// ").append(sourceModel).append(" has many ").append(navProperty).append("\n");
        code.append("        /// </summary>\n");
        code.append("        public ICollection<").append(targetModel).append(">? ").append(navProperty)
            .append(" { get; set; } = new List<").append(targetModel).append(">();\n\n");

        return code.toString();
    }

    /**
     * Generate ManyToMany relationship (ICollection navigation properties with join table)
     * Example: User has many Roles, Role has many Users (through UserRole join table)
     */
    private String generateManyToMany(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String joinTableName = sourceModel + targetModel;
        String sourceNavProperty = pluralize(targetModel);
        String targetNavProperty = pluralize(sourceModel);

        code.append("        /// <summary>\n");
        code.append("        /// ").append(sourceModel).append(" has many ").append(targetModel).append("s through ")
            .append(joinTableName).append("\n");
        code.append("        /// </summary>\n");
        code.append("        public ICollection<").append(targetModel).append(">? ").append(sourceNavProperty)
            .append(" { get; set; } = new List<").append(targetModel).append(">();\n\n");

        return code.toString();
    }

    /**
     * Generate OneToOne relationship (single navigation property with optional key)
     * Example: User has one Profile
     */
    private String generateOneToOne(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String navProperty = targetModel;
        String foreignKey = sourceModel + "Id";

        code.append("        /// <summary>\n");
        code.append("        /// ").append(sourceModel).append(" has one ").append(targetModel).append("\n");
        code.append("        /// </summary>\n");
        code.append("        public ").append(targetModel).append("? ").append(navProperty)
            .append(" { get; set; }\n\n");

        return code.toString();
    }

    /**
     * Generate migration code for relationship configuration
     */
    public String generateRelationshipMigrations(CSharpModelParser.CSharpModelDefinition model) {
        StringBuilder code = new StringBuilder();

        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }

        code.append("        // ==================== RELATIONSHIP CONFIGURATIONS ====================\n\n");

        for (CSharpModelParser.CSharpRelationshipDefinition rel : model.getRelationships()) {
            if (rel.getType() == CSharpModelParser.CSharpRelationshipDefinition.RelationType.ONE_TO_MANY) {
                code.append(generateOneToManyMigration(rel));
            } else if (rel.getType() == CSharpModelParser.CSharpRelationshipDefinition.RelationType.MANY_TO_MANY) {
                code.append(generateManyToManyMigration(rel));
            } else if (rel.getType() == CSharpModelParser.CSharpRelationshipDefinition.RelationType.ONE_TO_ONE) {
                code.append(generateOneToOneMigration(rel));
            }
        }

        return code.toString();
    }

    /**
     * Generate OneToMany foreign key migration
     */
    private String generateOneToManyMigration(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String tableName = targetModel.toLowerCase() + "s";
        String foreignKey = sourceModel + "Id";
        String sourceTable = sourceModel.toLowerCase() + "s";

        code.append("        // ").append(sourceModel).append(" -> ").append(targetModel).append(" (One-to-Many)\n");
        code.append("        modelBuilder.Entity<").append(targetModel).append(">()\n");
        code.append("            .HasOne<").append(sourceModel).append(">()\n");
        code.append("            .WithMany(a => a.").append(pluralize(targetModel)).append(")\n");
        code.append("            .HasForeignKey(\"").append(foreignKey).append("\")\n");
        code.append("            .OnDelete(DeleteBehavior.Cascade);\n\n");

        return code.toString();
    }

    /**
     * Generate ManyToMany migration with join table
     */
    private String generateManyToManyMigration(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String joinTable = sourceModel + targetModel;
        String sourceTable = sourceModel.toLowerCase() + "s";
        String targetTable = targetModel.toLowerCase() + "s";

        code.append("        // ").append(sourceModel).append(" <-> ").append(targetModel).append(" (Many-to-Many)\n");
        code.append("        modelBuilder.Entity<").append(sourceModel).append(">()\n");
        code.append("            .HasMany(a => a.").append(pluralize(targetModel)).append(")\n");
        code.append("            .WithMany(b => b.").append(pluralize(sourceModel)).append(")\n");
        code.append("            .UsingEntity(j => j\n");
        code.append("                .ToTable(\"").append(joinTable).append("\")\n");
        code.append("                .HasOne(typeof(").append(sourceModel).append("))\n");
        code.append("                    .WithMany()\n");
        code.append("                    .HasForeignKey(\"").append(sourceModel).append("Id\")\n");
        code.append("                    .OnDelete(DeleteBehavior.Cascade)\n");
        code.append("                .HasOne(typeof(").append(targetModel).append("))\n");
        code.append("                    .WithMany()\n");
        code.append("                    .HasForeignKey(\"").append(targetModel).append("Id\")\n");
        code.append("                    .OnDelete(DeleteBehavior.Cascade));\n\n");

        return code.toString();
    }

    /**
     * Generate OneToOne migration
     */
    private String generateOneToOneMigration(CSharpModelParser.CSharpRelationshipDefinition rel) {
        StringBuilder code = new StringBuilder();

        String sourceModel = rel.getSourceModel();
        String targetModel = rel.getTargetModel();
        String foreignKey = sourceModel + "Id";

        code.append("        // ").append(sourceModel).append(" -> ").append(targetModel).append(" (One-to-One)\n");
        code.append("        modelBuilder.Entity<").append(sourceModel).append(">()\n");
        code.append("            .HasOne(a => a.").append(targetModel).append(")\n");
        code.append("            .WithOne()\n");
        code.append("            .HasForeignKey<").append(targetModel).append(">(\"").append(foreignKey).append("\")\n");
        code.append("            .OnDelete(DeleteBehavior.Cascade);\n\n");

        return code.toString();
    }

    /**
     * Generate repository relationship loading methods
     */
    public String generateRepositoryRelationshipMethods(CSharpModelParser.CSharpModelDefinition model) {
        StringBuilder code = new StringBuilder();

        if (model.getRelationships() == null || model.getRelationships().isEmpty()) {
            return "";
        }

        code.append("        // ==================== RELATIONSHIP QUERIES ====================\n\n");

        for (CSharpModelParser.CSharpRelationshipDefinition rel : model.getRelationships()) {
            String targetModel = rel.getTargetModel();
            String navProperty = pluralize(targetModel);

            code.append("        /// <summary>\n");
            code.append("        /// Get with ").append(navProperty).append(" included\n");
            code.append("        /// </summary>\n");
            code.append("        public IQueryable<").append(rel.getSourceModel()).append("> With")
                .append(navProperty).append("()\n");
            code.append("        {\n");
            code.append("            return _context.Set<").append(rel.getSourceModel()).append(">()\n");
            code.append("                .Include(e => e.").append(navProperty).append(");\n");
            code.append("        }\n\n");
        }

        return code.toString();
    }

    /**
     * Generate eager loading helper method
     */
    public String generateEagerLoadingHelper(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("        /// <summary>\n");
        code.append("        /// Include all relationships\n");
        code.append("        /// </summary>\n");
        code.append("        public IQueryable<").append(modelName).append("> IncludeAll()\n");
        code.append("        {\n");
        code.append("            var query = _context.Set<").append(modelName).append(">()\n");
        code.append("                .AsNoTracking();\n");
        code.append("            // Add .Include() for each relationship\n");
        code.append("            return query;\n");
        code.append("        }\n\n");

        return code.toString();
    }

    /**
     * Pluralize word
     */
    private String pluralize(String name) {
        return name + "s";
    }
}

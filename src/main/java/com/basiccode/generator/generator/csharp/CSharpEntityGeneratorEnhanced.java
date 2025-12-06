package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;

/**
 * CSharpEntityGeneratorEnhanced - Enhanced Entity generation with full features
 * Generates complete Eloquent-like ORM models with relationships, scopes, and accessors
 * 
 * Phase 2 Week 1 - C# ENTITY GENERATION
 * 
 * Generates:
 * - Complete entity classes with all properties
 * - Data annotations for validation
 * - Relationships (navigation properties)
 * - Audit fields (CreatedAt, UpdatedAt, IsDeleted)
 * - Computed properties
 * - Lifecycle configuration
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpEntityGeneratorEnhanced {

    private final CSharpTypeMapper typeMapper;
    private final CSharpRelationshipGenerator relationshipGenerator;

    public CSharpEntityGeneratorEnhanced(CSharpTypeMapper typeMapper, 
                                       CSharpRelationshipGenerator relationshipGenerator) {
        this.typeMapper = typeMapper;
        this.relationshipGenerator = relationshipGenerator;
    }

    /**
     * Generate complete entity with all features
     */
    public String generateCompleteEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();

        // Header
        code.append(generateUsings());

        // Namespace
        code.append("\nnamespace ").append(packageName).append(".Models\n");
        code.append("{\n");

        // Class attributes
        code.append("    /// <summary>\n");
        code.append("    /// Entity: ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    [Table(\"").append(pluralize(className.toLowerCase())).append("\")]\n");
        code.append("    [Index(nameof(CreatedAt))]\n");

        // Class declaration
        code.append("    public class ").append(className).append("\n");
        code.append("    {\n");

        // Primary key
        code.append("        [Key]\n");
        code.append("        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]\n");
        code.append("        public Guid Id { get; set; }\n\n");

        // Properties
        code.append(generateProperties(enhancedClass));

        // Audit fields
        code.append(generateAuditFields());

        // Soft delete
        code.append("        public bool IsDeleted { get; set; } = false;\n\n");

        // Relationships
        code.append(relationshipGenerator.generateRelationships(
            new CSharpModelParser().parseModel(enhancedClass)
        ));

        // Computed properties
        code.append(generateComputedProperties(enhancedClass));

        // Lifecycle methods
        code.append(generateLifecycleMethods());

        // Close class
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate using statements
     */
    private String generateUsings() {
        StringBuilder usings = new StringBuilder();
        usings.append("using System.ComponentModel.DataAnnotations;\n");
        usings.append("using System.ComponentModel.DataAnnotations.Schema;\n");
        usings.append("using System.Collections.Generic;\n");
        return usings.toString();
    }

    /**
     * Generate properties with validation
     */
    private String generateProperties(EnhancedClass enhancedClass) {
        StringBuilder props = new StringBuilder();

        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            String fieldName = capitalizeFirst(attr.getName());
            String csharpType = typeMapper.mapToCSharpType(attr.getType());

            // Data annotations
            if (!attr.isNullable()) {
                props.append("        [Required(ErrorMessage = \"").append(fieldName).append(" is required\")]\n");
            }

            if ("string".equals(csharpType)) {
                props.append("        [StringLength(255, MinimumLength = 1)]\n");
            }

            if (attr.getName().toLowerCase().contains("email")) {
                props.append("        [EmailAddress]\n");
            }

            if (attr.getName().toLowerCase().contains("phone")) {
                props.append("        [Phone]\n");
            }

            // Property
            props.append("        public ").append(csharpType);
            if (attr.isNullable() && !isValueType(csharpType)) {
                props.append("?");
            }
            props.append(" ").append(fieldName).append(" { get; set; }");

            if ("string".equals(csharpType) && !attr.isNullable()) {
                props.append(" = string.Empty;");
            }

            props.append("\n\n");
        }

        return props.toString();
    }

    /**
     * Generate audit fields
     */
    private String generateAuditFields() {
        StringBuilder audit = new StringBuilder();

        audit.append("        /// <summary>\n");
        audit.append("        /// Record creation timestamp\n");
        audit.append("        /// </summary>\n");
        audit.append("        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]\n");
        audit.append("        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;\n\n");

        audit.append("        /// <summary>\n");
        audit.append("        /// Record last update timestamp\n");
        audit.append("        /// </summary>\n");
        audit.append("        public DateTime? UpdatedAt { get; set; }\n\n");

        audit.append("        /// <summary>\n");
        audit.append("        /// Record deletion timestamp\n");
        audit.append("        /// </summary>\n");
        audit.append("        public DateTime? DeletedAt { get; set; }\n\n");

        return audit.toString();
    }

    /**
     * Generate computed properties
     */
    private String generateComputedProperties(EnhancedClass enhancedClass) {
        StringBuilder computed = new StringBuilder();

        computed.append("        // ==================== COMPUTED PROPERTIES ====================\n\n");

        // Age in days
        computed.append("        /// <summary>\n");
        computed.append("        /// Days since creation\n");
        computed.append("        /// </summary>\n");
        computed.append("        [NotMapped]\n");
        computed.append("        public int DaysSinceCreation => (int)(DateTime.UtcNow - CreatedAt).TotalDays;\n\n");

        // Is updated
        computed.append("        /// <summary>\n");
        computed.append("        /// Whether record has been updated\n");
        computed.append("        /// </summary>\n");
        computed.append("        [NotMapped]\n");
        computed.append("        public bool IsUpdated => UpdatedAt.HasValue;\n\n");

        // Display name
        computed.append("        /// <summary>\n");
        computed.append("        /// Display representation\n");
        computed.append("        /// </summary>\n");
        computed.append("        [NotMapped]\n");
        computed.append("        public string Display => $\"{Id} - Created: {CreatedAt:yyyy-MM-dd}\";\n\n");

        return computed.toString();
    }

    /**
     * Generate lifecycle methods
     */
    private String generateLifecycleMethods() {
        StringBuilder lifecycle = new StringBuilder();

        lifecycle.append("        // ==================== LIFECYCLE METHODS ====================\n\n");

        lifecycle.append("        /// <summary>\n");
        lifecycle.append("        /// Called before save\n");
        lifecycle.append("        /// </summary>\n");
        lifecycle.append("        public virtual void OnSaving()\n");
        lifecycle.append("        {\n");
        lifecycle.append("            if (UpdatedAt == null && CreatedAt != default)\n");
        lifecycle.append("                UpdatedAt = DateTime.UtcNow;\n");
        lifecycle.append("        }\n\n");

        lifecycle.append("        /// <summary>\n");
        lifecycle.append("        /// Soft delete\n");
        lifecycle.append("        /// </summary>\n");
        lifecycle.append("        public virtual void Delete()\n");
        lifecycle.append("        {\n");
        lifecycle.append("            IsDeleted = true;\n");
        lifecycle.append("            DeletedAt = DateTime.UtcNow;\n");
        lifecycle.append("            UpdatedAt = DateTime.UtcNow;\n");
        lifecycle.append("        }\n\n");

        lifecycle.append("        /// <summary>\n");
        lifecycle.append("        /// Restore soft deleted entity\n");
        lifecycle.append("        /// </summary>\n");
        lifecycle.append("        public virtual void Restore()\n");
        lifecycle.append("        {\n");
        lifecycle.append("            IsDeleted = false;\n");
        lifecycle.append("            DeletedAt = null;\n");
        lifecycle.append("            UpdatedAt = DateTime.UtcNow;\n");
        lifecycle.append("        }\n\n");

        return lifecycle.toString();
    }

    /**
     * Check if type is value type
     */
    private boolean isValueType(String csharpType) {
        return csharpType.matches("^(int|long|short|decimal|float|double|byte|bool|DateTime|DateTimeOffset|TimeSpan|Guid)$");
    }

    /**
     * Capitalize first letter
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Pluralize name
     */
    private String pluralize(String name) {
        return name + "s";
    }
}

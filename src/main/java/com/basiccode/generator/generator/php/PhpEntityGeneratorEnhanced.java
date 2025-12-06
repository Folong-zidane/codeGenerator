package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import com.basiccode.generator.model.UmlRelationship;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PhpEntityGeneratorEnhanced - Enhanced Eloquent Model generation with full features.
 * Builds complete Eloquent models with relationships, casts, scopes, and accessors.
 */
public class PhpEntityGeneratorEnhanced {

    private final PhpTypeMapper typeMapper;

    public PhpEntityGeneratorEnhanced() {
        this.typeMapper = new PhpTypeMapper();
    }
    
    public PhpEntityGeneratorEnhanced(PhpTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    /**
     * Generate complete Eloquent model with all features
     */
    public String generateCompleteEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();

        // 1. PHP Header and Namespace
        code.append(generatePhpHeader(className, packageName));

        // 2. Imports
        code.append(generateImports(enhancedClass, packageName));

        // 3. Class Declaration
        code.append("\nclass ").append(className).append(" extends Model\n");
        code.append("{\n");
        code.append("    use HasFactory, SoftDeletes;\n\n");

        // 4. Table Configuration
        code.append(generateTableConfiguration(className));

        // 5. Mass Assignment
        code.append(generateMassAssignment(enhancedClass));

        // 6. Casts
        code.append(generateCasts(enhancedClass));

        // 7. Hidden Fields
        code.append(generateHiddenFields(enhancedClass));

        // 8. Appends (virtual attributes)
        code.append(generateAppends(enhancedClass));

        // 9. Relationships
        code.append(generateRelationships(enhancedClass));

        // 10. Scopes (query builders)
        code.append(generateScopes(enhancedClass));

        // 11. Accessors and Mutators
        code.append(generateAccessorsAndMutators(enhancedClass));

        // 12. Helper Methods
        code.append(generateHelperMethods(enhancedClass));

        // 13. Lifecycle Hooks
        code.append(generateLifecycleHooks(enhancedClass));

        // Close class
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate PHP header with namespace and class declaration
     */
    private String generatePhpHeader(String className, String packageName) {
        StringBuilder header = new StringBuilder();
        header.append("<?php\n\n");
        header.append("namespace ").append(packageName).append("\\Models;\n\n");
        return header.toString();
    }

    /**
     * Generate all required imports
     */
    private String generateImports(EnhancedClass enhancedClass, String packageName) {
        StringBuilder imports = new StringBuilder();
        
        // Core Eloquent
        imports.append("use Illuminate\\Database\\Eloquent\\Factories\\HasFactory;\n");
        imports.append("use Illuminate\\Database\\Eloquent\\Model;\n");
        imports.append("use Illuminate\\Database\\Eloquent\\SoftDeletes;\n");
        imports.append("use Illuminate\\Database\\Eloquent\\Casts\\Attribute;\n");
        
        // Collections
        imports.append("use Illuminate\\Support\\Collection;\n");
        imports.append("use Carbon\\Carbon;\n\n");

        // Relationships
        List<UmlRelationship> relationships = enhancedClass.getOriginalClass().getRelationships();
        if (relationships != null && !relationships.isEmpty()) {
            imports.append("use Illuminate\\Database\\Eloquent\\Relations\\HasMany;\n");
            imports.append("use Illuminate\\Database\\Eloquent\\Relations\\HasOne;\n");
            imports.append("use Illuminate\\Database\\Eloquent\\Relations\\BelongsTo;\n");
            imports.append("use Illuminate\\Database\\Eloquent\\Relations\\BelongsToMany;\n");
        }

        // Enums/Traits based on features
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : "Status";
            imports.append("use ").append(packageName).append("\\Enums\\").append(enumName).append(";\n");
        }

        // Scopes trait if needed
        imports.append("use ").append(packageName).append("\\Traits\\HasScopes;\n\n");

        return imports.toString();
    }

    /**
     * Generate table configuration
     */
    private String generateTableConfiguration(String className) {
        StringBuilder config = new StringBuilder();
        
        String tableName = inflect(className);
        
        config.append("    protected $table = '").append(tableName).append("';\n");
        config.append("    protected $primaryKey = 'id';\n");
        config.append("    public $timestamps = true;\n\n");
        config.append("    const CREATED_AT = 'created_at';\n");
        config.append("    const UPDATED_AT = 'updated_at';\n\n");

        return config.toString();
    }

    /**
     * Generate mass assignment (fillable/guarded)
     */
    private String generateMassAssignment(EnhancedClass enhancedClass) {
        StringBuilder mass = new StringBuilder();

        List<String> fillable = enhancedClass.getOriginalClass().getAttributes()
            .stream()
            .map(UmlAttribute::getName)
            .filter(name -> !name.equalsIgnoreCase("id"))
            .collect(Collectors.toList());

        mass.append("    protected $fillable = [\n");
        for (String field : fillable) {
            mass.append("        '").append(field).append("',\n");
        }

        if (enhancedClass.isStateful()) {
            mass.append("        'status',\n");
        }

        mass.append("    ];\n\n");

        // Guarded
        mass.append("    protected $guarded = [\n");
        mass.append("        'id',\n");
        mass.append("        'created_at',\n");
        mass.append("        'updated_at',\n");
        mass.append("    ];\n\n");

        return mass.toString();
    }

    /**
     * Generate type casts
     */
    private String generateCasts(EnhancedClass enhancedClass) {
        StringBuilder casts = new StringBuilder();

        casts.append("    protected $casts = [\n");

        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            String phpType = typeMapper.mapToPhpType(attr.getType());
            String castType = typeMapper.mapToCastType(phpType);
            
            if (castType != null && !castType.isEmpty()) {
                casts.append("        '").append(attr.getName()).append("' => '").append(castType).append("',\n");
            }
        }

        if (enhancedClass.isStateful()) {
            casts.append("        'status' => Status::class,\n");
        }

        casts.append("    ];\n\n");

        return casts.toString();
    }

    /**
     * Generate hidden fields (not serialized)
     */
    private String generateHiddenFields(EnhancedClass enhancedClass) {
        StringBuilder hidden = new StringBuilder();

        hidden.append("    protected $hidden = [\n");
        hidden.append("        // Add sensitive fields here\n");
        hidden.append("        // 'password',\n");
        hidden.append("        // 'api_token',\n");
        hidden.append("    ];\n\n");

        return hidden.toString();
    }

    /**
     * Generate appended virtual attributes
     */
    private String generateAppends(EnhancedClass enhancedClass) {
        StringBuilder appends = new StringBuilder();

        appends.append("    protected $appends = [\n");
        appends.append("        'full_display',\n");
        appends.append("    ];\n\n");

        return appends.toString();
    }

    /**
     * Generate scopes for query building
     */
    private String generateScopes(EnhancedClass enhancedClass) {
        StringBuilder scopes = new StringBuilder();

        String className = enhancedClass.getOriginalClass().getName();

        scopes.append("    // ==================== SCOPES ====================\n\n");

        // Active scope
        if (enhancedClass.isStateful()) {
            scopes.append("    /**\n");
            scopes.append("     * Filter active records\n");
            scopes.append("     */\n");
            scopes.append("    public function scopeActive($query)\n");
            scopes.append("    {\n");
            scopes.append("        return $query->where('status', Status::ACTIVE);\n");
            scopes.append("    }\n\n");

            scopes.append("    /**\n");
            scopes.append("     * Filter inactive records\n");
            scopes.append("     */\n");
            scopes.append("    public function scopeInactive($query)\n");
            scopes.append("    {\n");
            scopes.append("        return $query->where('status', Status::INACTIVE);\n");
            scopes.append("    }\n\n");
        }

        // Recent scope
        scopes.append("    /**\n");
        scopes.append("     * Filter recently created records\n");
        scopes.append("     */\n");
        scopes.append("    public function scopeRecent($query)\n");
        scopes.append("    {\n");
        scopes.append("        return $query->orderBy('created_at', 'desc');\n");
        scopes.append("    }\n\n");

        // Paginated scope
        scopes.append("    /**\n");
        scopes.append("     * Filter with pagination\n");
        scopes.append("     */\n");
        scopes.append("    public function scopePaginated($query, $perPage = 15)\n");
        scopes.append("    {\n");
        scopes.append("        return $query->paginate($perPage);\n");
        scopes.append("    }\n\n");

        // Search scope
        scopes.append("    /**\n");
        scopes.append("     * Search scope\n");
        scopes.append("     */\n");
        scopes.append("    public function scopeSearch($query, $search)\n");
        scopes.append("    {\n");
        scopes.append("        if (empty($search)) {\n");
        scopes.append("            return $query;\n");
        scopes.append("        }\n\n");
        scopes.append("        return $query->where(function ($q) use ($search) {\n");
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (typeMapper.mapToPhpType(attr.getType()).equals("string")) {
                scopes.append("                $q->orWhere('").append(attr.getName()).append("', 'like', \"%{$search}%\")\n");
            }
        }
        scopes.append("            });\n");
        scopes.append("    }\n\n");

        return scopes.toString();
    }

    /**
     * Generate accessors and mutators
     */
    private String generateAccessorsAndMutators(EnhancedClass enhancedClass) {
        StringBuilder accessors = new StringBuilder();

        String className = enhancedClass.getOriginalClass().getName();

        accessors.append("    // ==================== ACCESSORS & MUTATORS ====================\n\n");

        // Full display accessor
        accessors.append("    /**\n");
        accessors.append("     * Get full display representation\n");
        accessors.append("     */\n");
        accessors.append("    public function getFullDisplayAttribute()\n");
        accessors.append("    {\n");
        accessors.append("        return \"{$this->id} - \" . get_class($this);\n");
        accessors.append("    }\n\n");

        // Timestamp accessors
        accessors.append("    /**\n");
        accessors.append("     * Format created at\n");
        accessors.append("     */\n");
        accessors.append("    public function getCreatedAtFormattedAttribute()\n");
        accessors.append("    {\n");
        accessors.append("        return $this->created_at?->format('Y-m-d H:i:s');\n");
        accessors.append("    }\n\n");

        accessors.append("    /**\n");
        accessors.append("     * Format updated at\n");
        accessors.append("     */\n");
        accessors.append("    public function getUpdatedAtFormattedAttribute()\n");
        accessors.append("    {\n");
        accessors.append("        return $this->updated_at?->format('Y-m-d H:i:s');\n");
        accessors.append("    }\n\n");

        return accessors.toString();
    }
    
    /**
     * Generate basic relationships (simplified)
     */
    private String generateRelationships(EnhancedClass enhancedClass) {
        StringBuilder relationships = new StringBuilder();
        
        relationships.append("    // ==================== RELATIONSHIPS ====================\n\n");
        
        List<UmlRelationship> relations = enhancedClass.getOriginalClass().getRelationships();
        if (relations != null && !relations.isEmpty()) {
            for (UmlRelationship rel : relations) {
                relationships.append("    /**\n");
                relationships.append("     * Relationship with ").append(rel.getTargetClass()).append("\n");
                relationships.append("     */\n");
                relationships.append("    public function ").append(lcFirst(rel.getTargetClass())).append("()\n");
                relationships.append("    {\n");
                relationships.append("        // TODO: Implement relationship\n");
                relationships.append("        return $this->hasMany(").append(rel.getTargetClass()).append("::class);\n");
                relationships.append("    }\n\n");
            }
        }
        
        return relationships.toString();
    }
    
    /**
     * Helper: Convert first character to lowercase
     */
    private String lcFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * Generate helper methods
     */
    private String generateHelperMethods(EnhancedClass enhancedClass) {
        StringBuilder helpers = new StringBuilder();

        String className = enhancedClass.getOriginalClass().getName();

        helpers.append("    // ==================== HELPER METHODS ====================\n\n");

        // Get by ID helper
        helpers.append("    /**\n");
        helpers.append("     * Find by ID or throw\n");
        helpers.append("     */\n");
        helpers.append("    public static function findOrFail($id)\n");
        helpers.append("    {\n");
        helpers.append("        return static::query()\n");
        helpers.append("            ->findOrFail($id);\n");
        helpers.append("    }\n\n");

        // To array with relationships
        helpers.append("    /**\n");
        helpers.append("     * Convert to array with all relationships\n");
        helpers.append("     */\n");
        helpers.append("    public function toDetailedArray()\n");
        helpers.append("    {\n");
        helpers.append("        return array_merge($this->toArray(), [\n");
        helpers.append("            'created_at_formatted' => $this->created_at_formatted,\n");
        helpers.append("            'updated_at_formatted' => $this->updated_at_formatted,\n");
        helpers.append("        ]);\n");
        helpers.append("    }\n\n");

        // Get all active
        if (enhancedClass.isStateful()) {
            helpers.append("    /**\n");
            helpers.append("     * Get all active records\n");
            helpers.append("     */\n");
            helpers.append("    public static function getAllActive()\n");
            helpers.append("    {\n");
            helpers.append("        return static::active()->get();\n");
            helpers.append("    }\n\n");
        }

        return helpers.toString();
    }

    /**
     * Generate lifecycle hooks
     */
    private String generateLifecycleHooks(EnhancedClass enhancedClass) {
        StringBuilder hooks = new StringBuilder();

        hooks.append("    // ==================== LIFECYCLE HOOKS ====================\n\n");

        hooks.append("    /**\n");
        hooks.append("     * Boot the model\n");
        hooks.append("     */\n");
        hooks.append("    protected static function boot()\n");
        hooks.append("    {\n");
        hooks.append("        parent::boot();\n\n");
        hooks.append("        // Model events\n");
        hooks.append("        static::creating(function ($model) {\n");
        hooks.append("            // Before creating\n");
        hooks.append("        });\n\n");
        hooks.append("        static::created(function ($model) {\n");
        hooks.append("            // After creating\n");
        hooks.append("        });\n\n");
        hooks.append("        static::updating(function ($model) {\n");
        hooks.append("            // Before updating\n");
        hooks.append("        });\n\n");
        hooks.append("        static::updated(function ($model) {\n");
        hooks.append("            // After updating\n");
        hooks.append("        });\n");
        hooks.append("    }\n\n");

        return hooks.toString();
    }

    /**
     * Helper: Convert class name to table name (inflection)
     */
    private String inflect(String className) {
        // Simple pluralization: add 's' and lowercase
        return className.toLowerCase() + "s";
    }
}

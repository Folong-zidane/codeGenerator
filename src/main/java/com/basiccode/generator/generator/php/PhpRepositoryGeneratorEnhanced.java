package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import com.basiccode.generator.model.UmlRelationship;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PhpRepositoryGeneratorEnhanced - Enhanced Repository pattern implementation.
 * Generates repository interfaces and implementations with advanced query patterns.
 */
public class PhpRepositoryGeneratorEnhanced {

    private final PhpTypeMapper typeMapper;

    public PhpRepositoryGeneratorEnhanced(PhpTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    /**
     * Generate complete repository interface and implementation
     */
    public String generateRepositoryInterface(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String interfaceName = className + "Repository";

        // PHP Header
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Repositories\\Contracts;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n");
        code.append("use Illuminate\\Support\\Collection;\n\n");

        // Interface
        code.append("interface ").append(interfaceName).append("\n");
        code.append("{\n");

        // CRUD operations
        code.append("    // CRUD Operations\n");
        code.append("    public function getAll(): Collection;\n");
        code.append("    public function getAllPaginated(int $perPage = 15): LengthAwarePaginator;\n");
        code.append("    public function getById(int $id): ?").append(className).append(";\n");
        code.append("    public function create(array $data): ").append(className).append(";\n");
        code.append("    public function update(").append(className).append(" $model, array $data): ").append(className).append(";\n");
        code.append("    public function delete(").append(className).append(" $model): bool;\n\n");

        // Search and filter
        code.append("    // Search & Filter\n");
        code.append("    public function search(string $query): Collection;\n");
        code.append("    public function filter(array $filters): Collection;\n");
        code.append("    public function filterPaginated(array $filters, int $perPage = 15): LengthAwarePaginator;\n\n");

        // Relationships
        code.append("    // Relationships\n");
        code.append("    public function with(array $relations): self;\n");
        code.append("    public function withCount(array $relations): self;\n\n");

        // Advanced queries
        code.append("    // Advanced Queries\n");
        code.append("    public function exists(array $conditions): bool;\n");
        code.append("    public function count(array $conditions = []): int;\n");
        code.append("    public function first(array $conditions = []): ?").append(className).append(";\n");
        code.append("    public function pluck(string $column, string $key = null): Collection;\n\n");

        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate repository implementation
     */
    public String generateRepositoryImplementation(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String interfaceName = className + "Repository";
        String implName = className + "RepositoryImpl";

        // PHP Header
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Repositories;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use ").append(packageName).append("\\Repositories\\Contracts\\").append(interfaceName).append(";\n");
        code.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n");
        code.append("use Illuminate\\Support\\Collection;\n\n");

        // Class
        code.append("class ").append(implName).append(" implements ").append(interfaceName).append("\n");
        code.append("{\n");

        // Query builder property
        code.append("    protected $query;\n");
        code.append("    protected $relations = [];\n\n");

        // Constructor
        code.append("    public function __construct()\n");
        code.append("    {\n");
        code.append("        $this->resetQuery();\n");
        code.append("    }\n\n");

        // Reset query
        code.append("    /**\n");
        code.append("     * Reset query builder\n");
        code.append("     */\n");
        code.append("    protected function resetQuery()\n");
        code.append("    {\n");
        code.append("        $this->query = ").append(className).append("::query();\n");
        code.append("        if (!empty($this->relations)) {\n");
        code.append("            $this->query->with($this->relations);\n");
        code.append("        }\n");
        code.append("    }\n\n");

        // CRUD operations
        code.append(generateCrudMethods(className));

        // Search and filter
        code.append(generateSearchAndFilterMethods(enhancedClass, className));

        // Relationships
        code.append(generateRelationshipMethods(className));

        // Advanced queries
        code.append(generateAdvancedQueryMethods(enhancedClass, className));

        // Close class
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate CRUD methods for repository
     */
    private String generateCrudMethods(String className) {
        StringBuilder crud = new StringBuilder();

        crud.append("    // ==================== CRUD OPERATIONS ====================\n\n");

        // getAll
        crud.append("    /**\n");
        crud.append("     * Get all records\n");
        crud.append("     */\n");
        crud.append("    public function getAll(): Collection\n");
        crud.append("    {\n");
        crud.append("        return $this->query->get();\n");
        crud.append("    }\n\n");

        // getAllPaginated
        crud.append("    /**\n");
        crud.append("     * Get all records paginated\n");
        crud.append("     */\n");
        crud.append("    public function getAllPaginated(int $perPage = 15): LengthAwarePaginator\n");
        crud.append("    {\n");
        crud.append("        return $this->query->paginate($perPage);\n");
        crud.append("    }\n\n");

        // getById
        crud.append("    /**\n");
        crud.append("     * Get record by ID\n");
        crud.append("     */\n");
        crud.append("    public function getById(int $id): ?").append(className).append("\n");
        crud.append("    {\n");
        crud.append("        return $this->query->find($id);\n");
        crud.append("    }\n\n");

        // create
        crud.append("    /**\n");
        crud.append("     * Create new record\n");
        crud.append("     */\n");
        crud.append("    public function create(array $data): ").append(className).append("\n");
        crud.append("    {\n");
        crud.append("        return ").append(className).append("::create($data);\n");
        crud.append("    }\n\n");

        // update
        crud.append("    /**\n");
        crud.append("     * Update existing record\n");
        crud.append("     */\n");
        crud.append("    public function update(").append(className).append(" $model, array $data): ").append(className).append("\n");
        crud.append("    {\n");
        crud.append("        $model->update($data);\n");
        crud.append("        return $model->fresh();\n");
        crud.append("    }\n\n");

        // delete
        crud.append("    /**\n");
        crud.append("     * Delete record\n");
        crud.append("     */\n");
        crud.append("    public function delete(").append(className).append(" $model): bool\n");
        crud.append("    {\n");
        crud.append("        return $model->delete();\n");
        crud.append("    }\n\n");

        return crud.toString();
    }

    /**
     * Generate search and filter methods
     */
    private String generateSearchAndFilterMethods(EnhancedClass enhancedClass, String className) {
        StringBuilder search = new StringBuilder();

        search.append("    // ==================== SEARCH & FILTER ====================\n\n");

        // Search
        search.append("    /**\n");
        search.append("     * Search across searchable fields\n");
        search.append("     */\n");
        search.append("    public function search(string $query): Collection\n");
        search.append("    {\n");
        search.append("        if (empty($query)) {\n");
        search.append("            return $this->getAll();\n");
        search.append("        }\n\n");
        search.append("        return $this->query->where(function ($q) use ($query) {\n");

        // Add search conditions for string fields
        List<UmlAttribute> stringAttrs = enhancedClass.getOriginalClass().getAttributes()
            .stream()
            .filter(attr -> typeMapper.mapToPhpType(attr.getType()).equals("string"))
            .collect(Collectors.toList());

        for (UmlAttribute attr : stringAttrs) {
            search.append("            $q->orWhere('").append(attr.getName()).append("', 'like', \"%{$query}%\")\n");
        }

        search.append("        })->get();\n");
        search.append("    }\n\n");

        // Filter
        search.append("    /**\n");
        search.append("     * Filter by multiple criteria\n");
        search.append("     */\n");
        search.append("    public function filter(array $filters): Collection\n");
        search.append("    {\n");
        search.append("        $query = ").append(className).append("::query();\n\n");
        search.append("        foreach ($filters as $field => $value) {\n");
        search.append("            if ($value !== null && $value !== '') {\n");
        search.append("                if (is_array($value)) {\n");
        search.append("                    $query->whereIn($field, $value);\n");
        search.append("                } else {\n");
        search.append("                    $query->where($field, $value);\n");
        search.append("                }\n");
        search.append("            }\n");
        search.append("        }\n\n");
        search.append("        return $query->get();\n");
        search.append("    }\n\n");

        // FilterPaginated
        search.append("    /**\n");
        search.append("     * Filter with pagination\n");
        search.append("     */\n");
        search.append("    public function filterPaginated(array $filters, int $perPage = 15): LengthAwarePaginator\n");
        search.append("    {\n");
        search.append("        return $this->filter($filters)->paginate($perPage);\n");
        search.append("    }\n\n");

        return search.toString();
    }

    /**
     * Generate relationship methods
     */
    private String generateRelationshipMethods(String className) {
        StringBuilder relations = new StringBuilder();

        relations.append("    // ==================== RELATIONSHIPS ====================\n\n");

        // with
        relations.append("    /**\n");
        relations.append("     * Load relations\n");
        relations.append("     */\n");
        relations.append("    public function with(array $relations): self\n");
        relations.append("    {\n");
        relations.append("        $this->relations = $relations;\n");
        relations.append("        $this->resetQuery();\n");
        relations.append("        return $this;\n");
        relations.append("    }\n\n");

        // withCount
        relations.append("    /**\n");
        relations.append("     * Load relationship counts\n");
        relations.append("     */\n");
        relations.append("    public function withCount(array $relations): self\n");
        relations.append("    {\n");
        relations.append("        $this->query->withCount($relations);\n");
        relations.append("        return $this;\n");
        relations.append("    }\n\n");

        return relations.toString();
    }

    /**
     * Generate advanced query methods
     */
    private String generateAdvancedQueryMethods(EnhancedClass enhancedClass, String className) {
        StringBuilder advanced = new StringBuilder();

        advanced.append("    // ==================== ADVANCED QUERIES ====================\n\n");

        // exists
        advanced.append("    /**\n");
        advanced.append("     * Check if record exists\n");
        advanced.append("     */\n");
        advanced.append("    public function exists(array $conditions): bool\n");
        advanced.append("    {\n");
        advanced.append("        $query = ").append(className).append("::query();\n");
        advanced.append("        foreach ($conditions as $field => $value) {\n");
        advanced.append("            $query->where($field, $value);\n");
        advanced.append("        }\n");
        advanced.append("        return $query->exists();\n");
        advanced.append("    }\n\n");

        // count
        advanced.append("    /**\n");
        advanced.append("     * Count records\n");
        advanced.append("     */\n");
        advanced.append("    public function count(array $conditions = []): int\n");
        advanced.append("    {\n");
        advanced.append("        $query = ").append(className).append("::query();\n");
        advanced.append("        foreach ($conditions as $field => $value) {\n");
        advanced.append("            $query->where($field, $value);\n");
        advanced.append("        }\n");
        advanced.append("        return $query->count();\n");
        advanced.append("    }\n\n");

        // first
        advanced.append("    /**\n");
        advanced.append("     * Get first record\n");
        advanced.append("     */\n");
        advanced.append("    public function first(array $conditions = []): ?").append(className).append("\n");
        advanced.append("    {\n");
        advanced.append("        $query = ").append(className).append("::query();\n");
        advanced.append("        foreach ($conditions as $field => $value) {\n");
        advanced.append("            $query->where($field, $value);\n");
        advanced.append("        }\n");
        advanced.append("        return $query->first();\n");
        advanced.append("    }\n\n");

        // pluck
        advanced.append("    /**\n");
        advanced.append("     * Get column values\n");
        advanced.append("     */\n");
        advanced.append("    public function pluck(string $column, string $key = null): Collection\n");
        advanced.append("    {\n");
        advanced.append("        if ($key) {\n");
        advanced.append("            return ").append(className).append("::pluck($column, $key);\n");
        advanced.append("        }\n");
        advanced.append("        return ").append(className).append("::pluck($column);\n");
        advanced.append("    }\n\n");

        return advanced.toString();
    }
}

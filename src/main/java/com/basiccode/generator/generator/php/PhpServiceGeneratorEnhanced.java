package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PhpServiceGeneratorEnhanced - Service layer generation with advanced patterns.
 * Generates complete service classes with business logic separation, transactions, and validation.
 */
public class PhpServiceGeneratorEnhanced {

    private final PhpValidationGenerator validationGenerator;
    private final PhpTypeMapper typeMapper;

    public PhpServiceGeneratorEnhanced(PhpValidationGenerator validationGenerator, PhpTypeMapper typeMapper) {
        this.validationGenerator = validationGenerator;
        this.typeMapper = typeMapper;
    }

    /**
     * Generate complete service class
     */
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String serviceName = className + "Service";

        // PHP Header
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Services;\n\n");
        code.append(generateImports(className, packageName));

        // Class
        code.append("class ").append(serviceName).append("\n");
        code.append("{\n");

        // Properties
        code.append(generateProperties(className));

        // Constructor
        code.append(generateConstructor(className));

        // CRUD Methods
        code.append(generateAllMethods(enhancedClass, className));

        // Close class
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate all imports
     */
    private String generateImports(String className, String packageName) {
        StringBuilder imports = new StringBuilder();

        imports.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        imports.append("use ").append(packageName).append("\\Repositories\\Contracts\\").append(className).append("Repository;\n");
        imports.append("use ").append(packageName).append("\\Resources\\").append(className).append("Resource;\n");
        imports.append("use Illuminate\\Support\\Facades\\DB;\n");
        imports.append("use Illuminate\\Support\\Facades\\Log;\n");
        imports.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n");
        imports.append("use Illuminate\\Support\\Collection;\n");
        imports.append("use Exception;\n\n");

        return imports.toString();
    }

    /**
     * Generate service properties
     */
    private String generateProperties(String className) {
        StringBuilder props = new StringBuilder();

        props.append("    /**\n");
        props.append("     * Repository instance\n");
        props.append("     */\n");
        props.append("    protected $repository;\n\n");

        return props.toString();
    }

    /**
     * Generate service constructor
     */
    private String generateConstructor(String className) {
        StringBuilder constructor = new StringBuilder();

        constructor.append("    /**\n");
        constructor.append("     * Initialize service\n");
        constructor.append("     */\n");
        constructor.append("    public function __construct(").append(className).append("Repository $repository)\n");
        constructor.append("    {\n");
        constructor.append("        $this->repository = $repository;\n");
        constructor.append("    }\n\n");

        return constructor.toString();
    }

    /**
     * Generate all service methods
     */
    private String generateAllMethods(EnhancedClass enhancedClass, String className) {
        StringBuilder methods = new StringBuilder();

        methods.append("    // ==================== CRUD OPERATIONS ====================\n\n");
        methods.append(generateGetAllMethod(className));
        methods.append(generateGetByIdMethod(className));
        methods.append(generateCreateMethod(enhancedClass, className));
        methods.append(generateUpdateMethod(enhancedClass, className));
        methods.append(generateDeleteMethod(className));

        methods.append("    // ==================== SEARCH & FILTER ====================\n\n");
        methods.append(generateSearchMethod(className));
        methods.append(generateFilterMethod(className));

        methods.append("    // ==================== BULK OPERATIONS ====================\n\n");
        methods.append(generateBulkCreateMethod(enhancedClass, className));
        methods.append(generateBulkUpdateMethod(className));
        methods.append(generateBulkDeleteMethod(className));

        methods.append("    // ==================== ADVANCED OPERATIONS ====================\n\n");
        methods.append(generateDuplicateMethod(className));
        methods.append(generateExportMethod(className));
        methods.append(generateImportMethod(enhancedClass, className));

        methods.append("    // ==================== TRANSACTION HANDLING ====================\n\n");
        methods.append(generateTransactionMethod());
        methods.append(generateRollbackMethod());

        return methods.toString();
    }

    /**
     * Generate getAll method
     */
    private String generateGetAllMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Get all records\n");
        method.append("     */\n");
        method.append("    public function getAll(): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return $this->repository->getAll();\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error fetching all records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        method.append("    /**\n");
        method.append("     * Get all records paginated\n");
        method.append("     */\n");
        method.append("    public function getAllPaginated(int $perPage = 15): LengthAwarePaginator\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return $this->repository->getAllPaginated($perPage);\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error paginating records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate getById method
     */
    private String generateGetByIdMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Get record by ID\n");
        method.append("     */\n");
        method.append("    public function getById(int $id): ?").append(className).append("\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            $record = $this->repository->getById($id);\n");
        method.append("            if (!$record) {\n");
        method.append("                Log::warning('Record not found with ID: ' . $id);\n");
        method.append("                return null;\n");
        method.append("            }\n");
        method.append("            return $record;\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error fetching record: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate create method
     */
    private String generateCreateMethod(EnhancedClass enhancedClass, String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Create new record\n");
        method.append("     */\n");
        method.append("    public function create(array $data): ").append(className).append("\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($data) {\n");
        method.append("                $record = $this->repository->create($data);\n");
        method.append("                Log::info('Record created with ID: ' . $record->id);\n");
        method.append("                return $record;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error creating record: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate update method
     */
    private String generateUpdateMethod(EnhancedClass enhancedClass, String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Update record\n");
        method.append("     */\n");
        method.append("    public function update(int $id, array $data): ?").append(className).append("\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($id, $data) {\n");
        method.append("                $record = $this->repository->getById($id);\n");
        method.append("                if (!$record) {\n");
        method.append("                    Log::warning('Record not found for update with ID: ' . $id);\n");
        method.append("                    return null;\n");
        method.append("                }\n");
        method.append("                $updated = $this->repository->update($record, $data);\n");
        method.append("                Log::info('Record updated with ID: ' . $id);\n");
        method.append("                return $updated;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error updating record: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate delete method
     */
    private String generateDeleteMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Delete record\n");
        method.append("     */\n");
        method.append("    public function delete(int $id): bool\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($id) {\n");
        method.append("                $record = $this->repository->getById($id);\n");
        method.append("                if (!$record) {\n");
        method.append("                    Log::warning('Record not found for deletion with ID: ' . $id);\n");
        method.append("                    return false;\n");
        method.append("                }\n");
        method.append("                $deleted = $this->repository->delete($record);\n");
        method.append("                Log::info('Record deleted with ID: ' . $id);\n");
        method.append("                return $deleted;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error deleting record: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate search method
     */
    private String generateSearchMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Search records\n");
        method.append("     */\n");
        method.append("    public function search(string $query): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return $this->repository->search($query);\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error searching records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate filter method
     */
    private String generateFilterMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Filter records\n");
        method.append("     */\n");
        method.append("    public function filter(array $filters): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return $this->repository->filter($filters);\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error filtering records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate bulk create method
     */
    private String generateBulkCreateMethod(EnhancedClass enhancedClass, String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Create multiple records\n");
        method.append("     */\n");
        method.append("    public function bulkCreate(array $records): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($records) {\n");
        method.append("                $created = collect();\n");
        method.append("                foreach ($records as $data) {\n");
        method.append("                    $created->push($this->create($data));\n");
        method.append("                }\n");
        method.append("                Log::info('Bulk created ' . $created->count() . ' records');\n");
        method.append("                return $created;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error bulk creating records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate bulk update method
     */
    private String generateBulkUpdateMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Update multiple records\n");
        method.append("     */\n");
        method.append("    public function bulkUpdate(array $updates): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($updates) {\n");
        method.append("                $updated = collect();\n");
        method.append("                foreach ($updates as $id => $data) {\n");
        method.append("                    $updated->push($this->update($id, $data));\n");
        method.append("                }\n");
        method.append("                Log::info('Bulk updated ' . $updated->count() . ' records');\n");
        method.append("                return $updated;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error bulk updating records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate bulk delete method
     */
    private String generateBulkDeleteMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Delete multiple records\n");
        method.append("     */\n");
        method.append("    public function bulkDelete(array $ids): int\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($ids) {\n");
        method.append("                $deleted = 0;\n");
        method.append("                foreach ($ids as $id) {\n");
        method.append("                    if ($this->delete($id)) {\n");
        method.append("                        $deleted++;\n");
        method.append("                    }\n");
        method.append("                }\n");
        method.append("                Log::info('Bulk deleted ' . $deleted . ' records');\n");
        method.append("                return $deleted;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error bulk deleting records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate duplicate method
     */
    private String generateDuplicateMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Duplicate a record\n");
        method.append("     */\n");
        method.append("    public function duplicate(int $id): ?").append(className).append("\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($id) {\n");
        method.append("                $record = $this->repository->getById($id);\n");
        method.append("                if (!$record) {\n");
        method.append("                    return null;\n");
        method.append("                }\n");
        method.append("                $data = $record->toArray();\n");
        method.append("                unset($data['id'], $data['created_at'], $data['updated_at']);\n");
        method.append("                return $this->create($data);\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error duplicating record: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate export method
     */
    private String generateExportMethod(String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Export records\n");
        method.append("     */\n");
        method.append("    public function export(array $filters = []): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            $records = !empty($filters) ? $this->filter($filters) : $this->getAll();\n");
        method.append("            return $records->map(function ($record) {\n");
        method.append("                return new ").append(className).append("Resource($record);\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error exporting records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate import method
     */
    private String generateImportMethod(EnhancedClass enhancedClass, String className) {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Import records from array\n");
        method.append("     */\n");
        method.append("    public function import(array $records): Collection\n");
        method.append("    {\n");
        method.append("        try {\n");
        method.append("            return DB::transaction(function () use ($records) {\n");
        method.append("                $imported = collect();\n");
        method.append("                foreach ($records as $data) {\n");
        method.append("                    $imported->push($this->create($data));\n");
        method.append("                }\n");
        method.append("                Log::info('Imported ' . $imported->count() . ' records');\n");
        method.append("                return $imported;\n");
        method.append("            });\n");
        method.append("        } catch (Exception $e) {\n");
        method.append("            Log::error('Error importing records: ' . $e->getMessage());\n");
        method.append("            throw $e;\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate transaction method
     */
    private String generateTransactionMethod() {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Begin transaction\n");
        method.append("     */\n");
        method.append("    public function beginTransaction(): void\n");
        method.append("    {\n");
        method.append("        DB::beginTransaction();\n");
        method.append("    }\n\n");

        return method.toString();
    }

    /**
     * Generate rollback method
     */
    private String generateRollbackMethod() {
        StringBuilder method = new StringBuilder();

        method.append("    /**\n");
        method.append("     * Commit or rollback transaction\n");
        method.append("     */\n");
        method.append("    public function commitOrRollback(bool $commit = true): void\n");
        method.append("    {\n");
        method.append("        if ($commit) {\n");
        method.append("            DB::commit();\n");
        method.append("        } else {\n");
        method.append("            DB::rollBack();\n");
        method.append("        }\n");
        method.append("    }\n\n");

        return method.toString();
    }
}

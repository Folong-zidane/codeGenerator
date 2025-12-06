package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PhpRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        // Return only the implementation, interface will be generated separately
        String className = enhancedClass.getOriginalClass().getName();
        return generateRepositoryImplementation(className, packageName);
    }
    
    public String generateRepositoryInterface(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        return generateRepositoryInterface(className, packageName);
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "Repositories";
    }
    
    private String generateRepositoryInterface(String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Repositories;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use Illuminate\\Database\\Eloquent\\Collection;\n");
        code.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n\n");
        
        code.append("interface ").append(className).append("RepositoryInterface\n");
        code.append("{\n");
        code.append("    public function getAll(): Collection;\n");
        code.append("    public function getPaginated(int $perPage = 15): LengthAwarePaginator;\n");
        code.append("    public function findById(int $id): ?").append(className).append(";\n");
        code.append("    public function create(array $data): ").append(className).append(";\n");
        code.append("    public function update(int $id, array $data): ?").append(className).append(";\n");
        code.append("    public function delete(int $id): bool;\n");
        code.append("    public function exists(int $id): bool;\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateRepositoryImplementation(String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Repositories;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use Illuminate\\Database\\Eloquent\\Collection;\n");
        code.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n");
        code.append("use Illuminate\\Support\\Facades\\Log;\n\n");
        
        code.append("class ").append(className).append("Repository implements ").append(className).append("RepositoryInterface\n");
        code.append("{\n");
        code.append("    protected ").append(className).append(" $model;\n\n");
        
        // Constructor
        code.append("    public function __construct(").append(className).append(" $model)\n");
        code.append("    {\n");
        code.append("        $this->model = $model;\n");
        code.append("    }\n\n");
        
        // GetAll
        code.append("    public function getAll(): Collection\n");
        code.append("    {\n");
        code.append("        return $this->model->all();\n");
        code.append("    }\n\n");
        
        // GetPaginated
        code.append("    public function getPaginated(int $perPage = 15): LengthAwarePaginator\n");
        code.append("    {\n");
        code.append("        return $this->model->paginate($perPage);\n");
        code.append("    }\n\n");
        
        // FindById
        code.append("    public function findById(int $id): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        return $this->model->find($id);\n");
        code.append("    }\n\n");
        
        // Create
        code.append("    public function create(array $data): ").append(className).append("\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            return $this->model->create($data);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error creating ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Update
        code.append("    public function update(int $id, array $data): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->findById($id);\n");
        code.append("            if ($entity) {\n");
        code.append("                $entity->update($data);\n");
        code.append("                return $entity->fresh();\n");
        code.append("            }\n");
        code.append("            return null;\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error updating ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Delete
        code.append("    public function delete(int $id): bool\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->findById($id);\n");
        code.append("            if ($entity) {\n");
        code.append("                return $entity->delete();\n");
        code.append("            }\n");
        code.append("            return false;\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error deleting ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Exists
        code.append("    public function exists(int $id): bool\n");
        code.append("    {\n");
        code.append("        return $this->model->where('id', $id)->exists();\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
}
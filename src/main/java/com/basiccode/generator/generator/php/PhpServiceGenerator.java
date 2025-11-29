package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.BusinessMethod;

public class PhpServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Services;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use ").append(packageName).append("\\Repositories\\").append(className).append("RepositoryInterface;\n");
        code.append("use Illuminate\\Database\\Eloquent\\Collection;\n");
        code.append("use Illuminate\\Pagination\\LengthAwarePaginator;\n");
        code.append("use Illuminate\\Validation\\ValidationException;\n");
        code.append("use Illuminate\\Support\\Facades\\Validator;\n");
        code.append("use Illuminate\\Support\\Facades\\Log;\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("use ").append(packageName).append("\\Enums\\").append(enumName).append(";\n\n");
        }
        
        code.append("class ").append(className).append("Service\n");
        code.append("{\n");
        code.append("    protected ").append(className).append("RepositoryInterface $repository;\n\n");
        
        // Constructor
        code.append("    public function __construct(").append(className).append("RepositoryInterface $repository)\n");
        code.append("    {\n");
        code.append("        $this->repository = $repository;\n");
        code.append("    }\n\n");
        
        // GetAll
        code.append("    public function getAll(): Collection\n");
        code.append("    {\n");
        code.append("        return $this->repository->getAll();\n");
        code.append("    }\n\n");
        
        // GetPaginated
        code.append("    public function getPaginated(int $perPage = 15): LengthAwarePaginator\n");
        code.append("    {\n");
        code.append("        return $this->repository->getPaginated($perPage);\n");
        code.append("    }\n\n");
        
        // FindById
        code.append("    public function findById(int $id): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        return $this->repository->findById($id);\n");
        code.append("    }\n\n");
        
        // Create
        code.append("    public function create(array $data): ").append(className).append("\n");
        code.append("    {\n");
        code.append("        $this->validateData($data);\n");
        code.append("        \n");
        code.append("        try {\n");
        code.append("            return $this->repository->create($data);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Service error creating ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Update
        code.append("    public function update(int $id, array $data): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        $entity = $this->repository->findById($id);\n");
        code.append("        if (!$entity) {\n");
        code.append("            throw new \\InvalidArgumentException('").append(className).append(" not found with id: ' . $id);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        $this->validateData($data, $id);\n");
        code.append("        \n");
        code.append("        try {\n");
        code.append("            return $this->repository->update($id, $data);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Service error updating ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Delete
        code.append("    public function delete(int $id): bool\n");
        code.append("    {\n");
        code.append("        $entity = $this->repository->findById($id);\n");
        code.append("        if (!$entity) {\n");
        code.append("            throw new \\InvalidArgumentException('").append(className).append(" not found with id: ' . $id);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        try {\n");
        code.append("            return $this->repository->delete($id);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Service error deleting ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // State management methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementMethods(code, className);
        }
        
        // Behavioral methods if available
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("    public function ").append(method.getName()).append("(): mixed\n");
                code.append("    {\n");
                code.append("        // Generated from sequence diagram\n");
                for (String logic : method.getBusinessLogic()) {
                    code.append("        // ").append(logic).append("\n");
                }
                code.append("        \n");
                code.append("        throw new \\Exception('Method not implemented');\n");
                code.append("    }\n\n");
            }
        }
        
        // Validation method
        code.append("    protected function validateData(array $data, ?int $id = null): void\n");
        code.append("    {\n");
        code.append("        $rules = [\n");
        code.append("            // Add validation rules here\n");
        code.append("            // 'name' => 'required|string|max:255',\n");
        code.append("            // 'email' => 'required|email|unique:").append(className.toLowerCase()).append("s,email' . ($id ? ',' . $id : ''),\n");
        code.append("        ];\n");
        code.append("        \n");
        code.append("        $validator = Validator::make($data, $rules);\n");
        code.append("        \n");
        code.append("        if ($validator->fails()) {\n");
        code.append("            throw new ValidationException($validator);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getServiceDirectory() {
        return "Services";
    }
    
    private void generateStateManagementMethods(StringBuilder code, String className) {
        // Suspend method
        code.append("    public function suspend").append(className).append("(int $id): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        $entity = $this->repository->findById($id);\n");
        code.append("        if (!$entity) {\n");
        code.append("            throw new \\InvalidArgumentException('").append(className).append(" not found with id: ' . $id);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        try {\n");
        code.append("            $entity->suspend();\n");
        code.append("            return $entity;\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Service error suspending ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Activate method
        code.append("    public function activate").append(className).append("(int $id): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        $entity = $this->repository->findById($id);\n");
        code.append("        if (!$entity) {\n");
        code.append("            throw new \\InvalidArgumentException('").append(className).append(" not found with id: ' . $id);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        try {\n");
        code.append("            $entity->activate();\n");
        code.append("            return $entity;\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Service error activating ").append(className).append(": ' . $e->getMessage());\n");
        code.append("            throw $e;\n");
        code.append("        }\n");
        code.append("    }\n\n");
    }
}
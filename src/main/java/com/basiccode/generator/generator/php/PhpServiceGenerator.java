package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PhpServiceGenerator implements IServiceGenerator {

    @Override
    public String getServiceDirectory() {
        return "Services";
    }
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Services;\n\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use Illuminate\\Support\\Facades\\Log;\n\n");
        
        code.append("class ").append(className).append("Service\n");
        code.append("{\n");
        
        // Create method
        code.append("    public function create(array $data): ").append(className).append("\n");
        code.append("    {\n");
        code.append("        Log::info('Creating new ").append(className).append("');\n");
        code.append("        return ").append(className).append("::create($data);\n");
        code.append("    }\n\n");
        
        // Find by ID
        code.append("    public function findById(int $id): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        return ").append(className).append("::find($id);\n");
        code.append("    }\n\n");
        
        // Update method
        code.append("    public function update(int $id, array $data): ?").append(className).append("\n");
        code.append("    {\n");
        code.append("        $entity = ").append(className).append("::find($id);\n");
        code.append("        if ($entity) {\n");
        code.append("            $entity->update($data);\n");
        code.append("            return $entity;\n");
        code.append("        }\n");
        code.append("        return null;\n");
        code.append("    }\n\n");
        
        // Delete method
        code.append("    public function delete(int $id): bool\n");
        code.append("    {\n");
        code.append("        $entity = ").append(className).append("::find($id);\n");
        code.append("        return $entity ? $entity->delete() : false;\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".php";
    }
}
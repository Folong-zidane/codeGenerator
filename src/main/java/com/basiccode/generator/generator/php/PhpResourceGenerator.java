package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.EnhancedClass;

public class PhpResourceGenerator {
    
    public String generateResource(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Http\\Resources;\n\n");
        code.append("use Illuminate\\Http\\Request;\n");
        code.append("use Illuminate\\Http\\Resources\\Json\\JsonResource;\n\n");
        
        code.append("class ").append(className).append("Resource extends JsonResource\n");
        code.append("{\n");
        
        // toArray method
        code.append("    /**\n");
        code.append("     * Transform the resource into an array.\n");
        code.append("     *\n");
        code.append("     * @return array<string, mixed>\n");
        code.append("     */\n");
        code.append("    public function toArray(Request $request): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            'id' => $this->id,\n");
        
        // Add all attributes
        for (com.basiccode.generator.model.UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!"id".equalsIgnoreCase(attr.getName())) {
                code.append("            '").append(attr.getName()).append("' => $this->").append(attr.getName()).append(",\n");
            }
        }
        
        // Add status if stateful
        if (enhancedClass.isStateful()) {
            code.append("            'status' => $this->status?->value,\n");
            code.append("            'status_label' => $this->status?->label(),\n");
        }
        
        // Add timestamps
        code.append("            'created_at' => $this->created_at?->toISOString(),\n");
        code.append("            'updated_at' => $this->updated_at?->toISOString(),\n");
        
        // Add relationships (if any)
        code.append("\n            // Relationships\n");
        code.append("            // 'related_model' => new RelatedModelResource($this->whenLoaded('relatedModel')),\n");
        
        code.append("        ];\n");
        code.append("    }\n\n");
        
        // with method for additional metadata
        code.append("    /**\n");
        code.append("     * Get additional data that should be returned with the resource array.\n");
        code.append("     *\n");
        code.append("     * @return array<string, mixed>\n");
        code.append("     */\n");
        code.append("    public function with(Request $request): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            'meta' => [\n");
        code.append("                'version' => '1.0',\n");
        code.append("                'generated_at' => now()->toISOString(),\n");
        code.append("            ],\n");
        code.append("        ];\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateResourceCollection(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Http\\Resources;\n\n");
        code.append("use Illuminate\\Http\\Request;\n");
        code.append("use Illuminate\\Http\\Resources\\Json\\ResourceCollection;\n\n");
        
        code.append("class ").append(className).append("Collection extends ResourceCollection\n");
        code.append("{\n");
        
        // toArray method
        code.append("    /**\n");
        code.append("     * Transform the resource collection into an array.\n");
        code.append("     *\n");
        code.append("     * @return array<int|string, mixed>\n");
        code.append("     */\n");
        code.append("    public function toArray(Request $request): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            'data' => ").append(className).append("Resource::collection($this->collection),\n");
        code.append("        ];\n");
        code.append("    }\n\n");
        
        // with method for pagination metadata
        code.append("    /**\n");
        code.append("     * Get additional data that should be returned with the resource array.\n");
        code.append("     *\n");
        code.append("     * @return array<string, mixed>\n");
        code.append("     */\n");
        code.append("    public function with(Request $request): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            'meta' => [\n");
        code.append("                'total' => $this->collection->count(),\n");
        code.append("                'version' => '1.0',\n");
        code.append("                'generated_at' => now()->toISOString(),\n");
        code.append("            ],\n");
        code.append("        ];\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
}